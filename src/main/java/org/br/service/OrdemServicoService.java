package org.br.service;

import lombok.extern.slf4j.Slf4j;
import org.br.interfaceDao.*;
import org.br.dto.OrdensServicoDTO;
import org.br.dto.OsItemDTO;
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

    @Autowired private OrdensServicoInterface ordensServicoDAO;
    @Autowired private VeiculoInterface veiculoDAO;
    @Autowired private MecanicoInterface mecanicoDAO;
    @Autowired private OsServicosInterface osServicosDAO;
    @Autowired private OsPecasInterface osPecasDAO;
    @Autowired private ServicosCatalogoInterface servicosDAO;
    @Autowired private PecasCatalogoInterface pecasDAO;
    @Autowired private EstoqueService estoqueService;
    @Autowired private FinanceiroService financeiroService;

    private OrdensServicoDTO toDTO(OrdensServico entity) {
        return OrdensServicoDTO.builder()
                .id(entity.getId())
                .idVeiculo(entity.getVeiculo().getId())
                .idMecanico(entity.getMecanico() != null ? entity.getMecanico().getId() : null)
                .dataAbertura(entity.getDataAbertura())
                .dataEncerramento(entity.getDataEncerramento())
                .inicioAtendimento(entity.getInicioAtendimento()) // Campo mapeado
                .fimAtendimento(entity.getFimAtendimento())       // Campo mapeado
                .status(entity.getStatus())
                .valorTotalServicos(entity.getValorTotalServicos())
                .valorTotalPecas(entity.getValorTotalPecas())
                .desconto(entity.getDesconto())
                .valorGeralTotal(entity.getValorGeralTotal())
                .relatoCliente(entity.getRelatoCliente())
                .build();
    }

    private OrdensServico toEntity(OrdensServicoDTO dto) {
        OrdensServico os = ordensServicoDAO.findById(dto.getId()).orElse(new OrdensServico());
        
        os.setVeiculo(veiculoDAO.findById(dto.getIdVeiculo()).orElseThrow());
        os.setMecanico(dto.getIdMecanico() != null ? mecanicoDAO.findById(dto.getIdMecanico()).orElse(null) : null);
        os.setRelatoCliente(dto.getRelatoCliente());
        os.setQuilometragemEntrada(dto.getQuilometragemEntrada());
        os.setNivelCombustivel(dto.getNivelCombustivel());
        os.setStatus(dto.getStatus());
        os.setValorTotalServicos(dto.getValorTotalServicos() != null ? dto.getValorTotalServicos() : BigDecimal.ZERO);
        os.setValorTotalPecas(dto.getValorTotalPecas() != null ? dto.getValorTotalPecas() : BigDecimal.ZERO);
        os.setDesconto(dto.getDesconto() != null ? dto.getDesconto() : BigDecimal.ZERO);
        os.setDataEncerramento(dto.getDataEncerramento());
        os.setInicioAtendimento(dto.getInicioAtendimento());
        os.setFimAtendimento(dto.getFimAtendimento());
        
        return os;
    }
    
    // ... (demais métodos permanecem iguais)
    public List<OrdensServicoDTO> listarTodas() {
        return ordensServicoDAO.findAll().stream().map(this::toDTO).collect(Collectors.toList());
    }
    public OrdensServicoDTO buscarPorId(Long id) {
        return ordensServicoDAO.findById(id).map(this::toDTO).orElseThrow();
    }
    @Transactional
    public void adicionarServico(Long idOs, Long idServico, int qtd) {
        OrdensServico os = ordensServicoDAO.findById(idOs).orElseThrow();
        ServicosCatalogo servico = servicosDAO.findById(idServico).orElseThrow();
        osServicosDAO.save(OsServicos.builder().ordemServico(os).servico(servico).quantidade(qtd).valorUnitario(servico.getPrecoBase()).build());
        atualizarTotais(os);
    }
    @Transactional
    public void adicionarPeca(Long idOs, Long idPeca, int qtd) {
        OrdensServico os = ordensServicoDAO.findById(idOs).orElseThrow();
        estoqueService.registrarSaida(idPeca, qtd, "Saída para OS #" + idOs);
        osPecasDAO.save(OsPecas.builder().ordemServico(os).peca(pecasDAO.findById(idPeca).orElseThrow()).quantidade(qtd).valorUnitario(pecasDAO.findById(idPeca).orElseThrow().getPrecoVenda()).build());
        atualizarTotais(os);
    }
    @Transactional
    public void removerItem(Long id, String tipo) {
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
        BigDecimal totalServicos = osServicosDAO.findByOrdemServico(os).stream().map(s -> s.getValorUnitario().multiply(new BigDecimal(s.getQuantidade()))).reduce(BigDecimal.ZERO, BigDecimal::add);
        BigDecimal totalPecas = osPecasDAO.findByOrdemServico(os).stream().map(p -> p.getValorUnitario().multiply(new BigDecimal(p.getQuantidade()))).reduce(BigDecimal.ZERO, BigDecimal::add);
        os.setValorTotalServicos(totalServicos);
        os.setValorTotalPecas(totalPecas);
        ordensServicoDAO.save(os);
    }
    public OrdensServicoDTO salvar(OrdensServicoDTO dto) {
        OrdensServico os = toEntity(dto);
        if (os.getId() == null) {
            os.setStatus(StatusOS.ORCAMENTO);
            os.setValorTotalServicos(BigDecimal.ZERO);
            os.setValorTotalPecas(BigDecimal.ZERO);
            os.setDesconto(BigDecimal.ZERO);
        }
        return toDTO(ordensServicoDAO.save(os));
    }
    public List<OsItemDTO> listarItensDaOS(Long idOs) {
        OrdensServico os = ordensServicoDAO.findById(idOs).orElseThrow();
        List<OsItemDTO> itens = new ArrayList<>();
        osServicosDAO.findByOrdemServico(os).forEach(s -> itens.add(OsItemDTO.builder().id(s.getId()).tipo("SERVICO").descricao(s.getServico().getDescricao()).quantidade(s.getQuantidade()).valorUnitario(s.getValorUnitario()).subtotal(s.getValorUnitario().multiply(new BigDecimal(s.getQuantidade()))).build()));
        osPecasDAO.findByOrdemServico(os).forEach(p -> itens.add(OsItemDTO.builder().id(p.getId()).tipo("PECA").descricao(p.getPeca().getNome()).quantidade(p.getQuantidade()).valorUnitario(p.getValorUnitario()).subtotal(p.getValorUnitario().multiply(new BigDecimal(p.getQuantidade()))).build()));
        return itens;
    }
    @Transactional
    public void alterarStatus(Long idOs, StatusOS novoStatus) {
        OrdensServico os = ordensServicoDAO.findById(idOs).orElseThrow();
        os.setStatus(novoStatus);
        if (novoStatus == StatusOS.ENTREGUE) {
            os.setDataEncerramento(LocalDateTime.now());
            financeiroService.criarReceitaDeOS(os);
        }
        ordensServicoDAO.save(os);
    }
    @Transactional
    public void iniciarAtendimento(Long idOs) {
        OrdensServico os = ordensServicoDAO.findById(idOs).orElseThrow();
        if (os.getInicioAtendimento() == null) {
            os.setInicioAtendimento(LocalDateTime.now());
            os.setStatus(StatusOS.EM_EXECUCAO);
            ordensServicoDAO.save(os);
        }
    }
    @Transactional
    public void finalizarAtendimento(Long idOs) {
        OrdensServico os = ordensServicoDAO.findById(idOs).orElseThrow();
        if (os.getFimAtendimento() == null) {
            os.setFimAtendimento(LocalDateTime.now());
            os.setStatus(StatusOS.PRONTO);
            ordensServicoDAO.save(os);
        }
    }
}
