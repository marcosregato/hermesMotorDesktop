package org.br.utils;

import com.lowagie.text.*;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;
import org.br.dto.OrdensServicoDTO;
import org.br.dto.OsItemDTO;

import java.io.FileOutputStream;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class PdfGenerator {

    public static void gerarRelatorioOS(OrdensServicoDTO os, List<OsItemDTO> itens, String path) {
        Document document = new Document();
        try {
            PdfWriter.getInstance(document, new FileOutputStream(path));
            document.open();

            // Cabeçalho
            Font fontTitulo = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 18);
            Paragraph titulo = new Paragraph("HERMES MOTOR - ORDEM DE SERVIÇO #" + os.getId(), fontTitulo);
            titulo.setAlignment(Element.ALIGN_CENTER);
            document.add(titulo);
            document.add(new Paragraph(" "));

            // Informações Básicas
            document.add(new Paragraph("Status: " + os.getStatus()));
            document.add(new Paragraph("Veículo ID: " + os.getIdVeiculo()));
            if (os.getDataAbertura() != null) {
                document.add(new Paragraph("Data Abertura: " + os.getDataAbertura().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm"))));
            }
            document.add(new Paragraph("Relato do Cliente: " + os.getRelatoCliente()));
            document.add(new Paragraph(" "));

            // Tabela de Itens
            PdfPTable table = new PdfPTable(4);
            table.setWidthPercentage(100);
            table.addCell("Tipo");
            table.addCell("Descrição");
            table.addCell("Qtd");
            table.addCell("Subtotal");

            for (OsItemDTO item : itens) {
                table.addCell(item.getTipo());
                table.addCell(item.getDescricao());
                table.addCell(String.valueOf(item.getQuantidade()));
                table.addCell("R$ " + item.getSubtotal());
            }
            document.add(table);

            // Totais
            document.add(new Paragraph(" "));
            Paragraph total = new Paragraph("VALOR TOTAL GERAL: R$ " + os.getValorGeralTotal(), 
                    FontFactory.getFont(FontFactory.HELVETICA_BOLD, 14));
            total.setAlignment(Element.ALIGN_RIGHT);
            document.add(total);

            // Termo de Garantia
            if (os.getTermoGarantia() != null && !os.getTermoGarantia().isEmpty()) {
                document.add(new Paragraph(" "));
                document.add(new Paragraph("Termos de Garantia:", FontFactory.getFont(FontFactory.HELVETICA_BOLD, 12)));
                document.add(new Paragraph(os.getTermoGarantia()));
            }

            document.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
