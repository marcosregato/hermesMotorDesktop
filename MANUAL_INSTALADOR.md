# Manual de Geração de Instaladores - Hermes Motor Sport

Este manual descreve o processo para gerar instaladores nativos (`.exe`, `.deb`, `.dmg`) da aplicação Hermes Motor Desktop utilizando o Maven e o `jpackage`.

---

## 1. Pré-requisitos

Antes de gerar o instalador, garanta que seu ambiente de desenvolvimento atenda aos seguintes requisitos:

### 1.1. Para Qualquer Sistema Operacional:
- **JDK 21 ou superior**: O `jpackage` é uma ferramenta incluída no JDK. Certifique-se de que o seu `JAVA_HOME` aponta para uma instalação completa do JDK, não apenas um JRE.
- **Apache Maven**: Necessário para executar os comandos de build do projeto.

### 1.2. Requisitos Específicos por Plataforma:

> **Atenção:** Você só pode gerar um instalador para o sistema operacional em que você está. Para gerar um `.dmg`, você precisa estar em um macOS. Para gerar um `.exe`, em um Windows.

- **Para gerar `.exe` no Windows**:
  - É necessário ter o **WiX Toolset** instalado. Faça o download em: [https://wixtoolset.org/releases/](https://wixtoolset.org/releases/)

- **Para gerar `.deb` ou `.rpm` no Linux**:
  - **Explicação**: O `jpackage` precisa de algumas ferramentas do sistema para "empacotar" sua aplicação. O `build-essential` instala compiladores e o `fakeroot` simula permissões de administrador, ambos necessários para criar os pacotes `.deb` ou `.rpm` corretamente.
  - **Como Instalar (Exemplo para Ubuntu/Debian)**:
    ```sh
    sudo apt-get update
    sudo apt-get install build-essential fakeroot
    ```
  - Na maioria das distribuições Linux usadas para desenvolvimento, esses pacotes já vêm instalados.

- **Para gerar `.dmg` no macOS**:
  - É necessário ter as **Xcode Command Line Tools** instaladas. Execute no terminal: `xcode-select --install`

---

## 2. Preparando os Ícones da Aplicação

O instalador e o atalho da aplicação usarão um ícone personalizado.

1.  Crie a pasta `src/main/resources/images` se ela não existir.
2.  Coloque os arquivos de ícone dentro dela. Cada sistema operacional usa um formato diferente:
    - **Windows**: `icon.ico`
    - **macOS**: `icon.icns`
    - **Linux**: `icon.png`

> **Nota:** O `pom.xml` está configurado para usar `icon.ico` por padrão. Para compilar em outros sistemas, você precisará ajustar o `pom.xml` ou usar perfis do Maven (veja a seção de customização avançada).

---

## 3. Processo de Geração do Instalador

O processo é dividido em duas etapas simples via terminal.

### Passo 1: Empacotar a Aplicação (`.jar`)

Primeiro, precisamos gerar o arquivo `.jar` da nossa aplicação Spring Boot. Este JAR contém todo o código e as dependências.

```sh
mvn clean package
```

Este comando limpará o projeto e criará o arquivo `hermesMotorDesktop-1.0-SNAPSHOT.jar` dentro da pasta `target/`.

### Passo 2: Gerar o Instalador Nativo

Com o `.jar` pronto, podemos invocar o `jpackage` através do plugin do Maven.

```sh
mvn jpackage:jpackage
```

O plugin lerá as configurações do `pom.xml`, encontrará o `.jar` gerado, empacotará um Java Runtime junto com ele e criará o instalador nativo.

### Passo 3: Encontrar o Instalador

Após a conclusão do comando, o instalador final estará na pasta `target/dist/`.

- No **Windows**, você encontrará um arquivo `.exe`.
- No **Linux**, um arquivo `.deb` ou `.rpm`.
- No **macOS**, um arquivo `.dmg`.

---

## 4. (Avançado) Customização por Plataforma com Perfis Maven

Para automatizar a seleção do ícone correto (`.ico`, `.icns`, `.png`) para cada sistema operacional, você pode adicionar perfis ao seu `pom.xml`.

Substitua a configuração do plugin `jpackage-maven-plugin` pela seguinte:

```xml
<plugin>
    <groupId>org.panteleyev</groupId>
    <artifactId>jpackage-maven-plugin</artifactId>
    <version>1.5.0</version>
    <configuration>
        <name>Hermes Motor Sport</name>
        <appVersion>1.0.0</appVersion>
        <vendor>Sua Oficina</vendor>
        <destination>target/dist</destination>
        <mainClass>org.br.Main</mainClass>
        <mainJar>${project.build.finalName}.jar</mainJar>
        <winMenu>true</winMenu>
        <winShortcut>true</winShortcut>
    </configuration>
    <profiles>
        <profile>
            <id>windows</id>
            <activation>
                <os><family>windows</family></os>
            </activation>
            <properties>
                <jpackage.icon>src/main/resources/images/icon.ico</jpackage.icon>
            </properties>
        </profile>
        <profile>
            <id>mac</id>
            <activation>
                <os><family>mac</family></os>
            </activation>
            <properties>
                <jpackage.icon>src/main/resources/images/icon.icns</jpackage.icon>
            </properties>
        </profile>
        <profile>
            <id>linux</id>
            <activation>
                <os><family>unix</family></os>
            </activation>
            <properties>
                <jpackage.icon>src/main/resources/images/icon.png</jpackage.icon>
            </properties>
        </profile>
    </profiles>
</plugin>
```

Com essa configuração, o Maven detectará automaticamente o sistema operacional e usará o ícone correto, tornando o processo de build ainda mais robusto.
