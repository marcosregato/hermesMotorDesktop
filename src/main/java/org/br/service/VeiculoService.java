package org.br.service;

import lombok.extern.slf4j.Slf4j;
import org.br.dto.OrdensServicoDTO;
import org.br.dto.VeiculoDTO;
import org.br.interfaceDao.OrdensServicoInterface;
import org.br.interfaceDao.VeiculoInterface;
import org.br.mapper.OrdemServicoMapper;
import org.br.mapper.VeiculoMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class VeiculoService {

    private final VeiculoInterface veiculoDAO;
    private final OrdensServicoInterface osDAO;
    private final VeiculoMapper veiculoMapper;
    private final OrdemServicoMapper osMapper; // Novo mapper injetado

    @Autowired
    public VeiculoService(VeiculoInterface veiculoDAO, OrdensServicoInterface osDAO, VeiculoMapper veiculoMapper, OrdemServicoMapper osMapper) {
        this.veiculoDAO = veiculoDAO;
        this.osDAO = osDAO;
        this.veiculoMapper = veiculoMapper;
        this.osMapper = osMapper;
    }

    public List<VeiculoDTO> listarAtivos() {
        log.info("Serviço: Buscando todos os veículos ativos.");
        return veiculoDAO.findByAtivoTrue().stream()
                .map(veiculoMapper::toDTO)
                .collect(Collectors.toList());
    }

    public List<OrdensServicoDTO> buscarHistorico(String placa) {
        log.info("Serviço: Buscando histórico para a placa: {}", placa);
        return veiculoDAO.findByPlaca(placa)
                .map(osDAO::findByVeiculo)
                .orElse(Collections.emptyList())
                .stream()
                .map(osMapper::toDTO) // Usa o mapper de OS
                .collect(Collectors.toList());
    }

    @Transactional
    public VeiculoDTO salvar(VeiculoDTO dto) {
        log.info("Serviço: Salvando veículo placa: {}", dto.getPlaca());
        var veiculo = veiculoMapper.toEntity(dto);
        var salvo = veiculoDAO.save(veiculo);
        return veiculoMapper.toDTO(salvo);
    }

    @Transactional
    public void excluirLogico(Long id) {
        log.warn("Serviço: Realizando exclusão lógica do veículo ID: {}", id);
        veiculoDAO.findById(id).ifPresent(v -> {
            v.setAtivo(false);
            veiculoDAO.save(v);
            log.info("Veículo ID: {} desativado com sucesso.", id);
        });
    }
}
