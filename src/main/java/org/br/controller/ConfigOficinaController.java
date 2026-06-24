package org.br.controller;

import org.br.model.ConfigOficina;
import org.br.service.ConfigOficinaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ConfigOficinaController {

    @Autowired
    private ConfigOficinaService service;

    public ConfigOficina getConfig() {
        return service.getConfig();
    }

    public void salvar(ConfigOficina config) {
        service.salvar(config);
    }
}
