# Guia de Layout e UI - Hermes Motor Desktop

Este documento define as diretrizes para a criação de interfaces Swing utilizando o **FlatLaf**, garantindo um visual moderno, limpo e consistente para o sistema de oficina.

---

## 1. Configuração do Look and Feel

O sistema utiliza o **FlatLaf Mac Dark** (ou Light) para uma aparência moderna similar ao macOS. A inicialização deve ocorrer no método `main` da aplicação:

```java
import com.formdev.flatlaf.themes.FlatMacDarkLaf;
import javax.swing.UIManager;

public class Main {
    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(new FlatMacDarkLaf());
        } catch (Exception ex) {
            System.err.println("Falha ao inicializar o Look and Feel");
        }
        // Iniciar JFrame principal aqui
    }
}
```

---

## 2. Padrões Visuais

### Cores (FlatLaf Mac Dark)
- **Fundo Principal**: `#282828` (automático pelo tema)
- **Ações Primárias (Botões)**: Utilize o estilo padrão do FlatLaf.
- **Sucesso**: Verde suave (Ex: `#2ecc71`) para botões de "Salvar" ou "Finalizar".
- **Perigo/Aviso**: Vermelho suave (Ex: `#e74c3c`) para botões de "Excluir" ou "Cancelar".

### Fontes
- **Família**: Sans-serif padrão (Inter ou Segoe UI se disponível).
- **Títulos**: Negrito, tamanho 18px a 24px.
- **Rótulos (Labels)**: Tamanho 13px ou 14px.

---

## 3. Estrutura das Telas (Layouts)

Evite o uso de `AbsoluteLayout`. Utilize gerenciadores de layout modernos:

1.  **MigLayout (Recomendado)**: Altamente flexível para formulários complexos.
2.  **BorderLayout**: Para a estrutura principal (Topo, Menu Lateral, Centro).
3.  **GridBagLayout**: Para formulários onde o MigLayout não estiver disponível.

### Exemplo de Estrutura de Formulário
- **Padding Externo**: 20px em todos os lados.
- **Espaçamento entre Componentes**: 10px a 15px.
- **Alinhamento**: Rótulos acima dos campos de texto (Top-aligned labels).

---

## 4. Componentes Customizados FlatLaf

O FlatLaf permite customizar componentes via `client properties`:

### Arredondamento de Botões e Campos
```java
// Botão com bordas muito arredondadas (estilo pílula)
jButton.putClientProperty("JButton.buttonType", "roundRect");

// Campo de busca com ícone de lupa e botão de limpar
JTextField searchField = new JTextField();
searchField.putClientProperty("JTextField.placeholderText", "Pesquisar placa ou cliente...");
searchField.putClientProperty("JTextField.showClearButton", true);
```

### Tabelas (JTable)
- **Estilo**: Use `FlatLaf` para alternar cores de linhas.
- **Headers**: Deixe-os altos e com alinhamento à esquerda.

---

## 5. Organização do Código de UI

Para manter o código limpo, siga o padrão:

1.  **Classe de Tela**: Estende `JFrame` ou `JPanel`.
2.  **Método `initComponents()`**: Configura hierarquia de componentes.
3.  **Método `initEvents()`**: Configura Listeners e ações de botões.
4.  **Uso de DTOs**: As telas devem receber e retornar DTOs, nunca as Entidades JPA diretamente.

---

## 6. Dicas de Experiência do Usuário (UX)

- **Feedback Visual**: Mostre um `JOptionPane` ou uma barra de status após salvar dados.
- **Teclas de Atalho**: Configure `Mnemonic` para botões principais (Ex: `Alt+S` para Salvar).
- **Validação**: Valide campos obrigatórios antes de enviar para a camada de serviço/DAO.
- **Foco Inicial**: Sempre defina o foco no primeiro campo de entrada ao abrir uma tela.

---
*Este guia deve ser seguido por todos os desenvolvedores para manter a integridade visual do Hermes Motor.*
