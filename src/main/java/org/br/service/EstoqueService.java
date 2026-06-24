package org.br.service;

import lombok.extern.slf4j.Slf4j;
import org.br.dto.EstoqueMovimentacaoDTO;
import org.br.interfaceDao.EstoqueMovimentacaoInterface;
import org.br.interfaceDao.PecasCatalogoInterface;
import org.br.model.EstoqueMovimentacao;
import org.br.model.PecasCatalogo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class EstoqueService {

    @Autowired
    private PecasCatalogoInterface pecasDAO;

    @Autowired
    private EstoqueMovimentacaoInterface movimentacaoDAO;

    public List<EstoqueMovimentacaoDTO> listarMovimentacoes() {
        return movimentacaoDAO.findAll().stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    @Transactional
    public void registrarEntrada(Long idPeca, int quantidade, String observacao) {
        log.info("Registrando entrada de {} unidade(s) para a peça ID: {}", quantidade, idPeca);
        PecasCatalogo peca = pecasDAO.findById(idPeca).orElseThrow(() -> new RuntimeException("Peça não encontrada"));
        
        peca.setQuantidadeEstoque(peca.getQuantidadeEstoque() + quantidade);
        pecasDAO.save(peca);

        movimentacaoDAO.save(EstoqueMovimentacao.builder()
                .peca(peca)
                .tipo(EstoqueMovimentacao.TipoMovimentacao.ENTRADA)
                .quantidade(quantidade)
                .observacao(observacao)
                .build());
    }

    @Transactional
    public void registrarSaida(Long idPeca, int quantidade, String observacao) {
        log.info("Registrando saída de {} unidade(s) para a peça ID: {}", quantidade, idPeca);
        PecasCatalogo peca = pecasDAO.findById(idPeca).orElseThrow(() -> new RuntimeException("Peça não encontrada"));

        if (peca.getQuantidadeEstoque() < quantidade) {
            log.warn("Tentativa de saída de estoque sem saldo suficiente para a peça {}", peca.getNome());
            throw new RuntimeException("Estoque insuficiente para " + peca.getNome());
        }

        peca.setQuantidadeEstoque(peca.getQuantidadeEstoque() - quantidade);
        pecasDAO.save(peca);

        movimentacaoDAO.save(EstoqueMovimentacao.builder()
                .peca(peca)
                .tipo(EstoqueMovimentacao.TipoMovimentacao.SAIDA_OS)
                .quantidade(quantidade)
                .observacao(observacao)
                .build());
    }

    private EstoqueMovimentacaoDTO toDTO(EstoqueMovimentacao entity) {
        return EstoqueMovimentacaoDTO.builder()
                .id(entity.getId())
                .nomePeca(entity.getPeca().getNome())
                .tipo(entity.getTipo())
                .quantidade(entity.getQuantidade())
                .dataMovimentacao(entity.getDataMovimentacao())
                .observacao(entity.getObservacao())
                .build();
    }
}
