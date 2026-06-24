package org.br.controller;

import org.br.model.ConfigOficina;
import org.br.service.ConfigOficinaService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ConfigOficinaControllerTest {

    @Mock
    private ConfigOficinaService service;

    @InjectMocks
    private ConfigOficinaController controller;

    @Test
    void getConfig_DeveChamarService() {
        ConfigOficina config = ConfigOficina.builder().nomeOficina("Teste").build();
        when(service.getConfig()).thenReturn(config);

        ConfigOficina resultado = controller.getConfig();

        assertEquals("Teste", resultado.getNomeOficina());
        verify(service, times(1)).getConfig();
    }

    @Test
    void salvar_DeveChamarService() {
        ConfigOficina config = ConfigOficina.builder().nomeOficina("Teste").build();
        controller.salvar(config);
        verify(service, times(1)).salvar(config);
    }
}
