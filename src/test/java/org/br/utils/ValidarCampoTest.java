package org.br.utils;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class ValidarCampoTest {

    private ValidarCampo validarCampo;

    @BeforeEach
    void setUp() {
        validarCampo = new ValidarCampo();
    }

    @Test
    void campoVazio_DeveRetornarTrueParaValoresVaziosOuNulos() {
        assertTrue(validarCampo.campoVazio(null));
        assertTrue(validarCampo.campoVazio(""));
        assertTrue(validarCampo.campoVazio("   "));
        assertFalse(validarCampo.campoVazio("Teste"));
    }

    @Test
    void qtdMinimaCaracter_DeveValidarCorretamente() {
        assertTrue(validarCampo.qtdMinimaCaracter("123", 5)); // Menor que o mínimo
        assertFalse(validarCampo.qtdMinimaCaracter("12345", 5)); // Igual ao mínimo
        assertFalse(validarCampo.qtdMinimaCaracter("123456", 5)); // Maior que o mínimo
    }

    @Test
    void qtdMaximaCaracter_DeveValidarCorretamente() {
        assertTrue(validarCampo.qtdMaximaCaracter("123456", 5)); // Maior que o máximo
        assertFalse(validarCampo.qtdMaximaCaracter("12345", 5)); // Igual ao máximo
        assertFalse(validarCampo.qtdMaximaCaracter("123", 5)); // Menor que o máximo
    }

    @Test
    void somenteNumeros_DeveRetornarTrueApenasParaDigitos() {
        assertTrue(validarCampo.somenteNumeros("123456"));
        assertFalse(validarCampo.somenteNumeros("123a45"));
        assertFalse(validarCampo.somenteNumeros("abc"));
        assertFalse(validarCampo.somenteNumeros(""));
        assertFalse(validarCampo.somenteNumeros(null));
    }

    @Test
    void validarTamanhoCpfCnpj_DeveValidarCpfECnpj() {
        assertTrue(validarCampo.validarTamanhoCpfCnpj("12345678901")); // 11 dígitos
        assertTrue(validarCampo.validarTamanhoCpfCnpj("12345678901234")); // 14 dígitos
        assertFalse(validarCampo.validarTamanhoCpfCnpj("1234567890")); // 10 dígitos
        assertFalse(validarCampo.validarTamanhoCpfCnpj("1234567890123")); // 13 dígitos
        assertFalse(validarCampo.validarTamanhoCpfCnpj("123a5678901")); // Com letras
    }
}
