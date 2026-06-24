package org.br.service;

import lombok.extern.slf4j.Slf4j;
import org.br.interfaceDao.OrdensServicoInterface;
import org.br.model.Veiculo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Service
public class NotificacaoService {

    @Autowired
    private OrdensServicoInterface osDAO;

    /**
     * Busca veículos que não fazem uma revisão (qualquer serviço) há mais de 6 meses.
     */
    public List<Veiculo> getVeiculosParaLembrete() {
        LocalDateTime dataLimite = LocalDateTime.now().minusMonths(6);
        log.info("Buscando veículos com última revisão antes de {}", dataLimite);
        return osDAO.findVeiculosParaLembreteRevisao(dataLimite);
    }
}
