package org.br.service;

import org.br.dto.MecanicoDTO;
import org.br.interfaceDao.MecanicoInterface;
import org.br.model.Mecanico;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class MecanicoServiceTest {

    @Mock
    private MecanicoInterface mecanicoDAO;

    @InjectMocks
    private MecanicoService mecanicoService;

    @Test
    void listarTodos_DeveRetornarTodosOsMecanicos() {
        Mecanico mecanico = Mecanico.builder().id(1L).nome("Mestre").build();
        when(mecanicoDAO.findAll()).thenReturn(Collections.singletonList(mecanico));

        List<MecanicoDTO> resultado = mecanicoService.listarTodos();

        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        assertEquals("Mestre", resultado.get(0).getNome());
        verify(mecanicoDAO, times(1)).findAll();
    }

    @Test
    void salvar_DevePersistirMecanicoNoBanco() {
        MecanicoDTO dto = MecanicoDTO.builder().nome("Novo Mecanico").cpf("123").build();
        Mecanico entity = Mecanico.builder().id(10L).nome("Novo Mecanico").build();
        
        when(mecanicoDAO.save(any(Mecanico.class))).thenReturn(entity);

        MecanicoDTO salvo = mecanicoService.salvar(dto);

        assertNotNull(salvo);
        assertEquals(10L, salvo.getId());
        verify(mecanicoDAO, times(1)).save(any(Mecanico.class));
    }
}
