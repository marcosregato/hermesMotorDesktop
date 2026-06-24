package org.br.service;

import org.br.interfaceDao.PecasCatalogoInterface;
import org.br.interfaceDao.ServicosCatalogoInterface;
import org.br.dto.PecasCatalogoDTO;
import org.br.dto.ServicosCatalogoDTO;
import org.br.model.PecasCatalogo;
import org.br.model.ServicosCatalogo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CatalogoService {

    @Autowired
    private ServicosCatalogoInterface servicosDAO;

    @Autowired
    private PecasCatalogoInterface pecasDAO;

    // --- Serviços ---
    public List<ServicosCatalogoDTO> listarServicos() {
        return servicosDAO.findAll().stream()
                .map(s -> ServicosCatalogoDTO.builder()
                        .id(s.getId())
                        .descricao(s.getDescricao())
                        .precoBase(s.getPrecoBase())
                        .build())
                .collect(Collectors.toList());
    }

    public void salvarServico(ServicosCatalogoDTO dto) {
        servicosDAO.save(ServicosCatalogo.builder()
                .id(dto.getId())
                .descricao(dto.getDescricao())
                .precoBase(dto.getPrecoBase())
                .build());
    }

    // --- Peças ---
    public List<PecasCatalogoDTO> listarPecas() {
        return pecasDAO.findAll().stream()
                .map(p -> PecasCatalogoDTO.builder()
                        .id(p.getId())
                        .codigoPeca(p.getCodigoPeca())
                        .nome(p.getNome())
                        .marca(p.getMarca())
                        .precoVenda(p.getPrecoVenda())
                        .quantidadeEstoque(p.getQuantidadeEstoque())
                        .build())
                .collect(Collectors.toList());
    }

    public void salvarPeca(PecasCatalogoDTO dto) {
        pecasDAO.save(PecasCatalogo.builder()
                .id(dto.getId())
                .codigoPeca(dto.getCodigoPeca())
                .nome(dto.getNome())
                .marca(dto.getMarca())
                .precoVenda(dto.getPrecoVenda())
                .quantidadeEstoque(dto.getQuantidadeEstoque())
                .build());
    }
}
