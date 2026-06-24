package org.br.dto;

import org.br.model.StatusOS;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class DtoTest {

    @Test
    void testClienteDTO() {
        ClienteDTO dto = new ClienteDTO();
        dto.setNome("João");
        assertEquals("João", dto.getNome());
        assertTrue(dto.toString().contains("João"));
    }

    @Test
    void testVeiculoDTO() {
        VeiculoDTO dto = VeiculoDTO.builder().placa("ABC1234").marca("Honda").build();
        assertTrue(dto.toString().contains("ABC1234"));
    }

    @Test
    void testOrdensServicoDTO() {
        OrdensServicoDTO dto = new OrdensServicoDTO();
        dto.setStatus(StatusOS.ORCAMENTO);
        dto.setValorGeralTotal(BigDecimal.TEN);
        assertEquals(StatusOS.ORCAMENTO, dto.getStatus());
        assertEquals(BigDecimal.TEN, dto.getValorGeralTotal());
    }

    @Test
    void testOsItemDTO() {
        OsItemDTO dto = OsItemDTO.builder().tipo("PECA").subtotal(BigDecimal.ONE).build();
        assertEquals("PECA", dto.getTipo());
        assertEquals(BigDecimal.ONE, dto.getSubtotal());
    }
}
