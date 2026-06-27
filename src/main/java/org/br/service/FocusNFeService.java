package org.br.service;

// import com.fasterxml.jackson.databind.JsonNode;
import lombok.extern.slf4j.Slf4j;
import org.br.model.ConfigOficina;
import org.br.model.LancamentoFinanceiro;
import org.br.model.OrdensServico;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
// import org.springframework.web.reactive.function.client.WebClient;
// import reactor.core.publisher.Mono;

import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Service
public class FocusNFeService {

    // private final WebClient webClient;
    // private final ConfigOficinaService configService;
    // private final LancamentoFinanceiroInterface lancamentoDAO;

    // @Autowired
    // public FocusNFeService(ConfigOficinaService configService, LancamentoFinanceiroInterface lancamentoDAO) {
    //     this.configService = configService;
    //     this.lancamentoDAO = lancamentoDAO;
    //     this.webClient = WebClient.builder().baseUrl("https://api.focusnfe.com.br").build();
    // }

    public void emitirNFS(LancamentoFinanceiro lancamento) {
        log.warn("FUNCIONALIDADE DE EMISSÃO DE NOTA FISCAL DESATIVADA TEMPORARIAMENTE.");
        // OrdensServico os = lancamento.getOrdemServico();
        // ConfigOficina config = configService.getConfig();
        // String token = config.getTokenApiFiscal();
        // String url = "/v2/nfse?ref=" + lancamento.getId();

        // lancamento.setStatusNotaFiscal(LancamentoFinanceiro.StatusNotaFiscal.PROCESSANDO);
        // lancamentoDAO.save(lancamento);

        // Map<String, Object> body = new HashMap<>();
        // body.put("data_emissao", os.getDataEncerramento().toString());
        // body.put("prestador_cnpj", config.getCnpj().replaceAll("[^0-9]", ""));
        // body.put("tomador_cnpj", os.getVeiculo().getCliente().getCpfCnpj().replaceAll("[^0-9]", ""));
        // body.put("servico_valor_servicos", os.getValorTotalServicos().toString());
        // // ... outros campos

        // log.info("Enviando requisição de emissão de NFS-e para a Focus NFe. Ref: {}", lancamento.getId());

        // webClient.post().uri(url)
        //         .header("Authorization", "Basic " + Base64.getEncoder().encodeToString((token + ":").getBytes()))
        //         .bodyValue(body)
        //         .retrieve()
        //         .bodyToMono(JsonNode.class)
        //         .subscribe(
        //             response -> {
        //                 log.info("Resposta da API Focus NFe: {}", response);
        //                 if (response.get("status").asText().equals("autorizado")) {
        //                     lancamento.setStatusNotaFiscal(LancamentoFinanceiro.StatusNotaFiscal.EMITIDA);
        //                     lancamento.setCaminhoPdfNotaFiscal(response.get("caminho_danfse").asText());
        //                 } else {
        //                     lancamento.setStatusNotaFiscal(LancamentoFinanceiro.StatusNotaFiscal.ERRO);
        //                 }
        //                 lancamentoDAO.save(lancamento);
        //             },
        //             error -> {
        //                 log.error("Erro ao emitir NFS-e: {}", error.getMessage());
        //                 lancamento.setStatusNotaFiscal(LancamentoFinanceiro.StatusNotaFiscal.ERRO);
        //                 lancamentoDAO.save(lancamento);
        //             }
        //         );
    }
}
