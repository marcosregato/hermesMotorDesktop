package org.br.service;

import org.br.interfaceDao.ConfigOficinaInterface;
import org.br.model.ConfigOficina;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ConfigOficinaService {

    @Autowired
    private ConfigOficinaInterface configDAO;

    public ConfigOficina getConfig() {
        return configDAO.findById(1L).orElse(ConfigOficina.builder()
                .id(1L)
                .nomeOficina("Minha Oficina de Motos")
                .mensagemPadraoWhatsapp("Olá! Sua moto está pronta.")
                .logoPath("/images/default_logo.png") // Caminho padrão para o logo
                .build());
    }

    public void salvar(ConfigOficina config) {
        config.setId(1L);
        configDAO.save(config);
    }
}
