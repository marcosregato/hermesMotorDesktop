package org.br.service;

import org.br.interfaceDao.MecanicoInterface;
import org.br.dto.MecanicoDTO;
import org.br.mapper.MecanicoMapper;
import org.br.model.Mecanico;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class MecanicoService {

    private final MecanicoInterface mecanicoDAO;
    private final MecanicoMapper mecanicoMapper;

    @Autowired
    public MecanicoService(MecanicoInterface mecanicoDAO, MecanicoMapper mecanicoMapper) {
        this.mecanicoDAO = mecanicoDAO;
        this.mecanicoMapper = mecanicoMapper;
    }

    public List<MecanicoDTO> listarTodos() {
        return mecanicoDAO.findAll().stream()
                .map(mecanicoMapper::toDTO)
                .collect(Collectors.toList());
    }

    public MecanicoDTO salvar(MecanicoDTO dto) {
        Mecanico mecanico = mecanicoMapper.toEntity(dto);
        return mecanicoMapper.toDTO(mecanicoDAO.save(mecanico));
    }

    public void excluir(Long id) {
        mecanicoDAO.deleteById(id);
    }
}
