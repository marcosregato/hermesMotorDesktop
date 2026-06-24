package org.br.service;

import org.br.interfaceDao.ConfigOficinaInterface;
import org.br.model.ConfigOficina;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ConfigOficinaServiceTest {

    @Mock
    private ConfigOficinaInterface configDAO;

    @InjectMocks
    private ConfigOficinaService configService;

    @Test
    void getConfig_DeveRetornarConfigExistente() {
        ConfigOficina config = ConfigOficina.builder().id(1L).nomeOficina("Teste").build();
        when(configDAO.findById(1L)).thenReturn(Optional.of(config));

        ConfigOficina resultado = configService.getConfig();

        assertEquals("Teste", resultado.getNomeOficina());
        verify(configDAO, times(1)).findById(1L);
    }

    @Test
    void getConfig_DeveRetornarDefaultSeNaoExistir() {
        when(configDAO.findById(1L)).thenReturn(Optional.empty());

        ConfigOficina resultado = configService.getConfig();

        assertEquals("Minha Oficina de Motos", resultado.getNomeOficina());
    }

    @Test
    void salvar_DeveForcarIdUmEPersistir() {
        ConfigOficina config = ConfigOficina.builder().nomeOficina("Nova").build();
        
        configService.salvar(config);

        assertEquals(1L, config.getId());
        verify(configDAO, times(1)).save(config);
    }
}
