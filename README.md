# ADA LocateCar - Sistema de Locação de Veículos

## Grupo

- **Matheus Gomes de Moura** ([Demouraa](https://github.com/Demouraa))
- **Roberto Luiz de Andrade Barreto** ([RLdAB](https://github.com/RLdAB))
- **Vinícius Nunes de Bona** ([viniciusbona](https://github.com/viniciusbona))
- **Elaine Fabiola Soares** ([elainefabiola](https://github.com/elainefabiola))

---

## Sobre o Projeto

Sistema completo de gerenciamento de locação de veículos desenvolvido em **Java**, seguindo os princípios **SOLID** e arquitetura em camadas. O projeto foi refatorado para usar **Streams**, **Interfaces Funcionais** e **Files API**.

### Principais Tecnologias
- **Java Streams** - Substituição de loops por pipelines funcionais
- **Paginação** - Implementada com `skip()` e `limit()`
- **Interfaces Funcionais** - Predicate, Function, Consumer, Supplier
- **Files + Streams** - Sistema de relatórios robusto
- **Interfaces Funcionais Personalizadas** - ValidadorDocumento, CalculadoraDesconto

---

## Início Rápido

### 1. Compilar o projeto
```bash
# Criar diretório bin
mkdir bin

# Compilar todos os arquivos Java
javac -d bin -sourcepath . Main.java src/model/*.java src/repositories/*.java src/services/*.java src/utils/*.java src/views/*.java src/functional/*.java
```

### 2. Executar o sistema
```bash
java -cp bin Main
```

### 3. Alternativa: Usar script de compilação (Linux/Mac)
```bash
./compilar.sh
```

### Comandos para Windows (PowerShell)
```powershell
# Criar diretório bin
mkdir bin

# Compilar todos os arquivos Java
javac -d bin -sourcepath . Main.java src/model/*.java src/repositories/*.java src/services/*.java src/utils/*.java src/views/*.java src/functional/*.java

# Executar o sistema
java -cp bin Main
```

### 5. Demonstração Automática
O sistema exibe automaticamente exemplos de todas as refatorações:
- Paginação de clientes e veículos
- Filtros com Predicate
- Agrupamentos e rankings
- Geração de relatórios

---

## Arquitetura do Sistema

### Estrutura de Camadas

```
src/
├── model/                    # Entidades de Domínio
│   ├── Cliente.java          # Classe abstrata base
│   ├── PessoaFisica.java     # Cliente PF (CPF + desconto 5% >5 dias)
│   ├── PessoaJuridica.java   # Cliente PJ (CNPJ + desconto 10% >3 dias)
│   ├── Veiculo.java          # Entidade veículo
│   ├── TipoVeiculo.java      # Enum: PEQUENO(R$100), MEDIO(R$150), SUV(R$200)
│   └── Aluguel.java          # Entidade aluguel
├── repositories/             # Camada de Persistência
│   ├── ClienteRepository.java
│   ├── VeiculoRepository.java
│   └── AluguelRepository.java
├── services/                 # Camada de Negócio
│   ├── ClienteService.java
│   ├── VeiculoService.java
│   ├── AluguelService.java
│   └── RelatorioService.java # Sistema de relatórios
├── functional/               # Interfaces Funcionais Personalizadas
│   ├── ValidadorDocumento.java
│   ├── CalculadoraDesconto.java
│   ├── FormatadorRelatorio.java
│   └── GeradorDados.java
├── utils/                    # Utilitários
│   └── GeradorDadosTeste.java # 13+ Suppliers para dados de teste
└── views/                    # Interface do Usuário
    └── MenuPrincipal.java    # Interface completa com 5 menus
```

---

## Funcionalidades Principais

### Gestão de Clientes
- Cadastro PF/PJ com validações
- Listagem com paginação
- Busca por nome parcial
- Filtros com Predicate

### Gestão de Veículos
- Cadastro por categoria (PEQUENO/MEDIO/SUV)
- Controle automático de disponibilidade
- Agrupamento por tipo
- Rankings de mais alugados

### Gestão de Aluguéis
- Processo completo de aluguel/devolução
- Cálculo automático com descontos
- Input opcional de data/hora de devolução
- Relatórios detalhados

### Sistema de Relatórios
- Faturamento por período
- Veículos mais alugados
- Clientes que mais alugaram
- Recibos de aluguel/devolução

---

## Exemplos de Código Refatorado

### Paginação com Streams
```java
// Listar primeira página com 5 clientes
List<Cliente> pagina = clienteService.listarComPaginacao(0, 5);
// Implementação: .skip(pagina * tamanho).limit(tamanho)
```

### Filtros com Predicate
```java
// Listar apenas Pessoas Físicas
List<Cliente> pf = clienteService.listarPessoasFisicas();
// Predicate: c -> c instanceof PessoaFisica
```

### Agrupamento e Rankings
```java
// Agrupar veículos por tipo
Map<TipoVeiculo, List<Veiculo>> porTipo = veiculoService.agruparPorTipo();

// Ranking de veículos mais alugados
List<Map.Entry<String, Long>> ranking = aluguelService.obterVeiculosMaisAlugados();
```

### Interfaces Funcionais Personalizadas
```java
// Validação de CPF usando interface personalizada
ValidadorDocumento validadorCPF = ValidadorDocumento.cpf();
boolean valido = validadorCPF.validar("12345678901");
```

---

## Regras de Negócio

### Sistema de Descontos
- **Pessoa Física**: 5% de desconto para aluguéis > 5 diárias
- **Pessoa Jurídica**: 10% de desconto para aluguéis > 3 diárias

### Valores por Categoria
- **PEQUENO**: R$ 100,00/dia
- **MEDIO**: R$ 150,00/dia  
- **SUV**: R$ 200,00/dia

### Cálculo de Diárias
- Qualquer fração de hora = 1 diária completa
- Baseado em `LocalDateTime` preciso

---

## Como Usar o Sistema

### Menu Principal
```
==================================================
         ADA LOCATECAR - MENU PRINCIPAL
==================================================
1 - Gestão de Clientes
2 - Gestão de Veículos  
3 - Gestão de Aluguéis
4 - 🔍 Demonstração das Refatorações
5 - 📊 Relatórios (Files + Streams)
0 - Sair
==================================================
```

### Demonstração das Refatorações
- **Opção 4**: Mostra exemplos práticos de todas as funcionalidades implementadas
- **Opção 5**: Gera relatórios usando Files + Streams

---

## Estatísticas do Projeto

- **Arquivos criados**: 9 novos arquivos
- **Arquivos modificados**: 7 arquivos refatorados
- **Linhas de código**: ~1.800 novas linhas
- **Métodos adicionados**: 70+ métodos
- **Interfaces funcionais**: 4 personalizadas + 4 padrão
- **Relatórios**: 5 tipos implementados

---

## Verificar Relatórios Gerados

Após executar o sistema, verifique os relatórios:

```bash
# Listar relatórios
ls -la relatorios/

# Ler relatório de veículos
cat relatorios/veiculos_mais_alugados.txt

# Ler relatório de faturamento
cat relatorios/faturamento_*.txt
```

---

## Documentação Adicional

- `RESUMO_REFATORACAO.txt` - Resumo visual das implementações
- `SOLID.md` - Princípios SOLID aplicados
- `UserStories.md` - Histórias de usuário
- `uml.md` - Diagramas UML do sistema

---

## Checklist de Refatoração Completo

- Substituir laços por Streams
- Implementar paginação com skip() e limit()
- Criar Comparator com lambda
- Usar Predicate para validações
- Usar Function para cálculos
- Usar Consumer para impressão
- Usar Supplier para dados de teste
- Persistir com Files
- Usar InputStream/OutputStream
- Criar interfaces funcionais personalizadas

---

**Versão**: 2.0 - Refatorada com Streams e Interfaces Funcionais  
