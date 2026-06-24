package org.br.service;

import org.br.interfaceDao.MecanicoInterface;
import org.br.dto.MecanicoDTO;
import org.br.model.Mecanico;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class MecanicoService {

    @Autowired
    private MecanicoInterface mecanicoDAO;

    public List<MecanicoDTO> listarTodos() {
        return mecanicoDAO.findAll().stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    public MecanicoDTO salvar(MecanicoDTO dto) {
        Mecanico mecanico = toEntity(dto);
        return toDTO(mecanicoDAO.save(mecanico));
    }

    public void excluir(Long id) {
        mecanicoDAO.deleteById(id);
    }

    private MecanicoDTO toDTO(Mecanico entity) {
        return MecanicoDTO.builder()
                .id(entity.getId())
                .nome(entity.getNome())
                .cpf(entity.getCpf())
                .telefone(entity.getTelefone())
                .especialidade(entity.getEspecialidade())
                .ativo(entity.getAtivo())
                .build();
    }

    private Mecanico toEntity(MecanicoDTO dto) {
        return Mecanico.builder()
                .id(dto.getId())
                .nome(dto.getNome())
                .cpf(dto.getCpf())
                .telefone(dto.getTelefone())
                .especialidade(dto.getEspecialidade())
                .ativo(dto.getAtivo() != null ? dto.getAtivo() : true)
                .build();
    }
}
