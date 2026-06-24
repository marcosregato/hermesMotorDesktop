package org.br.service;

import org.br.dto.DashboardDTO;
import org.br.dto.PecasCatalogoDTO;
import org.br.interfaceDao.OrdensServicoInterface;
import org.br.interfaceDao.PecasCatalogoInterface;
import org.br.model.StatusOS;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class DashboardService {

    @Autowired
    private OrdensServicoInterface osDAO;

    @Autowired
    private PecasCatalogoInterface pecasDAO;

    public DashboardDTO getDadosDashboard() {
        List<StatusOS> statusPatio = Arrays.asList(StatusOS.APROVADO, StatusOS.EM_EXECUCAO, StatusOS.PRONTO);
        
        Double faturamento = osDAO.sumFaturamentoEntregue();
        
        List<PecasCatalogoDTO> estoqueBaixo = pecasDAO.findEstoqueBaixo().stream()
                .map(p -> PecasCatalogoDTO.builder()
                        .nome(p.getNome())
                        .quantidadeEstoque(p.getQuantidadeEstoque())
                        .build())
                .collect(Collectors.toList());

        return DashboardDTO.builder()
                .motosNoPatio(osDAO.countByStatusIn(statusPatio))
                .orçamentosPendentes(osDAO.countByStatus(StatusOS.ORCAMENTO))
                .faturamentoMes(faturamento != null ? BigDecimal.valueOf(faturamento) : BigDecimal.ZERO)
                .pecasEstoqueBaixo(estoqueBaixo)
                .build();
    }
}
