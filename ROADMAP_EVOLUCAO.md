# Roadmap de Evolução - Hermes Motor Sport 🏍️

Este documento descreve as especificações técnicas e funcionais para os próximos módulos do sistema, visando transformar o software em uma ferramenta de gestão completa de ponta a ponta.

---

## 1. Controle de Estoque Avançado 📦
*Otimização de capital e rastreabilidade total de componentes.*

### Funcionalidades:
- **Baixa Automática Real-time**: Integração profunda com o módulo de O.S. para que qualquer parafuso ou litro de óleo adicionado seja debitado do saldo instantaneamente.
- **Painel de Reposição**: Tela dedicada listando apenas itens abaixo do estoque mínimo, com botão de "Sugestão de Compra".
- **Módulo de Importação XML**:
    - Leitura de arquivos `.xml` de NF-e (Nota Fiscal Eletrônica).
    - Cadastro automático de novos produtos.
    - Atualização de preços de custo e margem de lucro sugerida.

---

## 2. Gestão Financeira e Fiscal 💰
*Controle de lucratividade e conformidade legal.*

### Funcionalidades:
- **Emissão Fiscal (NFS-e / NF-e)**:
    - Integração com APIs de mensageria fiscal (Ex: Focus NFe ou PlugNotas).
    - Separação automática entre Mão de Obra (Serviço) e Peças (Produto).
- **Fluxo de Caixa Profissional**:
    - Dashboard de "Contas a Receber" com alertas de inadimplência.
    - Registro de "Contas a Pagar" (Aluguel, Luz, Fornecedores).
- **Módulo de Comissionamento**:
    - Configuração de % de comissão por mecânico ou por tipo de serviço.
    - Relatório mensal de fechamento de folha por produtividade.

---

## 3. Gestão da Produtividade e Equipe 🛠️
*Maximização do uso do tempo e organização do pátio.*

### Funcionalidades:
- **Kanban de Oficina**: Visualização estilo Trello com colunas: "Aguardando", "Em Rampa", "Teste de Rua", "Lavagem", "Pronto".
- **Apontamento de Horas (Time Tracking)**:
    - Botões de "Play/Pause" dentro da O.S. para o mecânico registrar o tempo real gasto.
    - Relatório de tempo médio por tipo de serviço (Benchmarking interno).

---

## 4. CRM e Agendamento Inteligente 🤝
*Fidelização ativa e organização de recepção.*

### Funcionalidades:
- **Agenda de Atendimento**: Calendário visual para marcação de revisões, evitando sobrecarga de motos em um único dia.
- **Revisão Preditiva**:
    - O sistema calcula a média de KM rodado pelo cliente.
    - Envio automático de lembrete via WhatsApp após X meses da última troca de óleo.
- **Ficha Técnica de Histórico**: Acesso rápido a todas as intervenções feitas na moto desde o primeiro cadastro.

---

## Estrutura Técnica de Implementação Sugerida

| Módulo | Camada Model | Camada Service | Prioridade |
| :--- | :--- | :--- | :--- |
| **Estoque** | `EstoqueMovimentacao` | `EstoqueService.importarXML()` | Alta |
| **Financeiro** | `ContaCorrente`, `Lancamento` | `FinanceiroService.fecharCaixa()` | Alta |
| **Produtividade**| `TarefaMecanico` | `ProdutividadeService.iniciarTimer()` | Média |
| **CRM** | `Agendamento` | `NotificacaoService.enviarLembrete()` | Média |

---
*Documento gerado para guiar o desenvolvimento das próximas sprints do Hermes Motor Sport.*
