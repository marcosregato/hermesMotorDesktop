package org.br.utils;

import org.br.dto.OrdensServicoDTO;
import org.br.dto.OsItemDTO;
import org.br.model.StatusOS;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.File;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class PdfGeneratorTest {

    @TempDir
    File tempDir; // Cria um diretório temporário para os testes

    @Test
    void gerarRelatorioOS_DeveCriarArquivoPDF() {
        // Dados de teste
        OrdensServicoDTO os = OrdensServicoDTO.builder()
                .id(123L)
                .idVeiculo(456L)
                .dataAbertura(LocalDateTime.now())
                .status(StatusOS.ORCAMENTO)
                .relatoCliente("Barulho estranho no motor.")
                .valorGeralTotal(new BigDecimal("350.75"))
                .termoGarantia("Garantia de 90 dias para peças e serviços.")
                .build();

        List<OsItemDTO> itens = Arrays.asList(
                OsItemDTO.builder().tipo("SERVICO").descricao("Troca de Óleo").quantidade(1).valorUnitario(new BigDecimal("50.00")).subtotal(new BigDecimal("50.00")).build(),
                OsItemDTO.builder().tipo("PECA").descricao("Filtro de Óleo").quantidade(1).valorUnitario(new BigDecimal("30.75")).subtotal(new BigDecimal("30.75")).build(),
                OsItemDTO.builder().tipo("SERVICO").descricao("Revisão Geral").quantidade(1).valorUnitario(new BigDecimal("250.00")).subtotal(new BigDecimal("250.00")).build()
        );

        // Caminho do arquivo PDF temporário
        String filePath = tempDir.getAbsolutePath() + File.separator + "test_os_report.pdf";

        // Gera o PDF
        PdfGenerator.gerarRelatorioOS(os, itens, filePath);

        // Verifica se o arquivo foi criado
        File pdfFile = new File(filePath);
        assertTrue(pdfFile.exists());
        assertTrue(pdfFile.length() > 0); // Verifica se o arquivo não está vazio
    }
}
