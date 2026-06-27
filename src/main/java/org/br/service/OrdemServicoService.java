package org.br.service;

import lombok.extern.slf4j.Slf4j;
import org.br.interfaceDao.*;
import org.br.dto.OrdensServicoDTO;
import org.br.dto.OsItemDTO;
import org.br.mapper.OrdemServicoMapper;
import org.br.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class OrdemServicoService {

    private final OrdensServicoInterface ordensServicoDAO;
    private final OsServicosInterface osServicosDAO;
    private final OsPecasInterface osPecasDAO;
    private final ServicosCatalogoInterface servicosDAO;
    private final PecasCatalogoInterface pecasDAO;
    private final EstoqueService estoqueService;
    private final FinanceiroService financeiroService;
    private final OrdemServicoMapper osMapper;

    @Autowired
    public OrdemServicoService(OrdensServicoInterface ordensServicoDAO, OsServicosInterface osServicosDAO, OsPecasInterface osPecasDAO, ServicosCatalogoInterface servicosDAO, PecasCatalogoInterface pecasDAO, EstoqueService estoqueService, FinanceiroService financeiroService, OrdemServicoMapper osMapper) {
        this.ordensServicoDAO = ordensServicoDAO;
        this.osServicosDAO = osServicosDAO;
        this.osPecasDAO = osPecasDAO;
        this.servicosDAO = servicosDAO;
        this.pecasDAO = pecasDAO;
        this.estoqueService = estoqueService;
        this.financeiroService = financeiroService;
        this.osMapper = osMapper;
    }

    public List<OrdensServicoDTO> listarTodas() {
        log.info("Serviço: Buscando todas as Ordens de Serviço.");
        return ordensServicoDAO.findAll().stream().map(osMapper::toDTO).collect(Collectors.toList());
    }

    public OrdensServicoDTO buscarPorId(Long id) {
        log.info("Serviço: Buscando O.S. de ID: {}", id);
        return ordensServicoDAO.findById(id).map(osMapper::toDTO).orElseThrow();
    }

    @Transactional
    public OrdensServicoDTO salvar(OrdensServicoDTO dto) {
        log.info("Serviço: Salvando/Atualizando O.S. para veículo ID: {}", dto.getIdVeiculo());
        OrdensServico os = osMapper.toEntity(dto);
        if (os.getId() == null) {
            os.setStatus(StatusOS.ORCAMENTO);
            os.setValorTotalServicos(BigDecimal.ZERO);
            os.setValorTotalPecas(BigDecimal.ZERO);
            os.setDesconto(BigDecimal.ZERO);
        }
        return osMapper.toDTO(ordensServicoDAO.save(os));
    }

    @Transactional
    public void adicionarServico(Long idOs, Long idServico, int qtd) {
        log.info("Serviço: Adicionando {} serviço(s) ID: {} na O.S. ID: {}", qtd, idServico, idOs);
        OrdensServico os = ordensServicoDAO.findById(idOs).orElseThrow();
        ServicosCatalogo servico = servicosDAO.findById(idServico).orElseThrow();
        osServicosDAO.save(OsServicos.builder().ordemServico(os).servico(servico).quantidade(qtd).valorUnitario(servico.getPrecoBase()).build());
        atualizarTotais(os);
    }

    @Transactional
    public void adicionarPeca(Long idOs, Long idPeca, int qtd) {
        log.info("Serviço: Adicionando {} peça(s) ID: {} na O.S. ID: {}", qtd, idPeca, idOs);
        OrdensServico os = ordensServicoDAO.findById(idOs).orElseThrow();
        estoqueService.registrarSaida(idPeca, qtd, "Saída para OS #" + idOs);
        osPecasDAO.save(OsPecas.builder().ordemServico(os).peca(pecasDAO.findById(idPeca).orElseThrow()).quantidade(qtd).valorUnitario(pecasDAO.findById(idPeca).orElseThrow().getPrecoVenda()).build());
        atualizarTotais(os);
    }

    @Transactional
    public void removerItem(Long id, String tipo) {
        log.warn("Serviço: Removendo item {} de ID: {}", tipo, id);
        if ("SERVICO".equals(tipo)) {
            OsServicos item = osServicosDAO.findById(id).orElseThrow();
            OrdensServico os = item.getOrdemServico();
            osServicosDAO.deleteById(id);
            atualizarTotais(os);
        } else {
            OsPecas item = osPecasDAO.findById(id).orElseThrow();
            OrdensServico os = item.getOrdemServico();
            estoqueService.registrarEntrada(item.getPeca().getId(), item.getQuantidade(), "Devolução da OS #" + os.getId());
            osPecasDAO.deleteById(id);
            atualizarTotais(os);
        }
    }

    private void atualizarTotais(OrdensServico os) {
        log.debug("Serviço: Recalculando totais para a O.S. {}", os.getId());
        BigDecimal totalServicos = osServicosDAO.findByOrdemServico(os).stream().map(s -> s.getValorUnitario().multiply(new BigDecimal(s.getQuantidade()))).reduce(BigDecimal.ZERO, BigDecimal::add);
        BigDecimal totalPecas = osPecasDAO.findByOrdemServico(os).stream().map(p -> p.getValorUnitario().multiply(new BigDecimal(p.getQuantidade()))).reduce(BigDecimal.ZERO, BigDecimal::add);
        os.setValorTotalServicos(totalServicos);
        os.setValorTotalPecas(totalPecas);
        ordensServicoDAO.save(os);
        log.info("Serviço: O.S. {} atualizada. Total Serviços: {}, Total Peças: {}", os.getId(), totalServicos, totalPecas);
    }

    @Transactional
    public void alterarStatus(Long idOs, StatusOS novoStatus) {
        log.info("Serviço: Alterando status da O.S. {} para {}", idOs, novoStatus);
        OrdensServico os = ordensServicoDAO.findById(idOs).orElseThrow();
        os.setStatus(novoStatus);
        if (novoStatus == StatusOS.ENTREGUE) {
            os.setDataEncerramento(LocalDateTime.now());
            log.debug("Data de encerramento registrada para a O.S. {}", idOs);
            financeiroService.criarReceitaDeOS(os);
        }
        ordensServicoDAO.save(os);
    }

    @Transactional
    public void iniciarAtendimento(Long idOs) {
        log.info("Serviço: Iniciando atendimento para O.S. {}", idOs);
        OrdensServico os = ordensServicoDAO.findById(idOs).orElseThrow();
        if (os.getInicioAtendimento() == null) {
            os.setInicioAtendimento(LocalDateTime.now());
            os.setStatus(StatusOS.EM_EXECUCAO);
            ordensServicoDAO.save(os);
        }
    }

    @Transactional
    public void finalizarAtendimento(Long idOs) {
        log.info("Serviço: Finalizando atendimento para O.S. {}", idOs);
        OrdensServico os = ordensServicoDAO.findById(idOs).orElseThrow();
        if (os.getFimAtendimento() == null) {
            os.setFimAtendimento(LocalDateTime.now());
            os.setStatus(StatusOS.PRONTO);
            ordensServicoDAO.save(os);
        }
    }

    public List<OsItemDTO> listarItensDaOS(Long idOs) {
        OrdensServico os = ordensServicoDAO.findById(idOs).orElseThrow();
        List<OsItemDTO> itens = new ArrayList<>();
        osServicosDAO.findByOrdemServico(os).forEach(s -> itens.add(OsItemDTO.builder().id(s.getId()).tipo("SERVICO").descricao(s.getServico().getDescricao()).quantidade(s.getQuantidade()).valorUnitario(s.getValorUnitario()).subtotal(s.getValorUnitario().multiply(new BigDecimal(s.getQuantidade()))).build()));
        osPecasDAO.findByOrdemServico(os).forEach(p -> itens.add(OsItemDTO.builder().id(p.getId()).tipo("PECA").descricao(p.getPeca().getNome()).quantidade(p.getQuantidade()).valorUnitario(p.getValorUnitario()).subtotal(p.getValorUnitario().multiply(new BigDecimal(p.getQuantidade()))).build()));
        return itens;
    }
}
