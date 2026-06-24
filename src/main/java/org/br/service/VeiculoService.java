package org.br.service;

import org.br.dto.OrdensServicoDTO;
import org.br.dto.VeiculoDTO;
import org.br.interfaceDao.ClienteInterface;
import org.br.interfaceDao.OrdensServicoInterface;
import org.br.interfaceDao.VeiculoInterface;
import org.br.model.Veiculo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class VeiculoService {

    @Autowired
    private VeiculoInterface veiculoDAO;
    
    @Autowired
    private ClienteInterface clienteDAO;

    @Autowired
    private OrdensServicoInterface osDAO;

    public List<VeiculoDTO> listarAtivos() {
        return veiculoDAO.findByAtivoTrue().stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    public List<OrdensServicoDTO> buscarHistorico(String placa) {
        Veiculo veiculo = veiculoDAO.findByPlaca(placa)
                .orElseThrow(() -> new RuntimeException("Veículo não encontrado"));
        return osDAO.findByVeiculo(veiculo).stream()
                .map(os -> OrdensServicoDTO.builder().id(os.getId()).dataAbertura(os.getDataAbertura()).status(os.getStatus()).valorGeralTotal(os.getValorGeralTotal()).relatoCliente(os.getRelatoCliente()).build())
                .collect(Collectors.toList());
    }

    public VeiculoDTO salvar(VeiculoDTO dto) {
        Veiculo veiculo = toEntity(dto);
        veiculo.setAtivo(true);
        return toDTO(veiculoDAO.save(veiculo));
    }

    public void excluirLogico(Long id) {
        Veiculo v = veiculoDAO.findById(id).orElseThrow();
        v.setAtivo(false);
        veiculoDAO.save(v);
    }

    private VeiculoDTO toDTO(Veiculo entity) {
        return VeiculoDTO.builder().id(entity.getId()).idCliente(entity.getCliente().getId()).placa(entity.getPlaca()).marca(entity.getMarca()).modelo(entity.getModelo()).anoFabricacao(entity.getAnoFabricacao()).anoModelo(entity.getAnoModelo()).cor(entity.getCor()).build();
    }

    private Veiculo toEntity(VeiculoDTO dto) {
        return Veiculo.builder().id(dto.getId()).cliente(clienteDAO.findById(dto.getIdCliente()).orElse(null)).placa(dto.getPlaca()).marca(dto.getMarca()).modelo(dto.getModelo()).anoFabricacao(dto.getAnoFabricacao()).anoModelo(dto.getAnoModelo()).cor(dto.getCor()).build();
    }
}
