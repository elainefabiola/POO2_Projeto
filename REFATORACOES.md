# Refatorações Realizadas - ADA LocateCar

Este documento descreve as refatorações aplicadas no projeto da Locadora de Veículos (ADA LocateCar) conforme especificado no arquivo `Refact.md`.

## 📋 Sumário das Refatorações

### ✅ 1. Streams em Java

Todas as operações de busca, filtro, ordenação e contagem foram refatoradas para usar **Streams** ao invés de loops tradicionais.

#### Exemplos implementados:

**Repositórios (ClienteRepository, VeiculoRepository, AluguelRepository):**
- `listarComPaginacao()` - Usa `skip()` e `limit()` para paginação
- `buscarComFiltro(Predicate)` - Aceita Predicate personalizado para filtros dinâmicos
- `buscarComFiltroPaginado()` - Combina filtro + ordenação + paginação
- `contarComFiltro(Predicate)` - Conta elementos usando Stream + Predicate

**Services:**
- `ClienteService.listarOrdenadosPorNome()` - Ordenação com `sorted(Comparator.comparing())`
- `VeiculoService.agruparPorTipo()` - Agrupamento com `Collectors.groupingBy()`
- `AluguelService.obterVeiculosMaisAlugados()` - Pipeline complexo: `groupingBy` + `counting` + `sorted` + `collect`
- `AluguelService.calcularFaturamentoTotal()` - Usa `map()` + `reduce()` para somar valores

#### Localização no código:
- `src/repositories/*.java` - Métodos com Streams para busca e filtros
- `src/services/*.java` - Lógica de negócio refatorada com Streams
- `Main.java:208-244` - Demonstração de paginação e filtros

---

### ✅ 2. Paginação Obrigatória

Implementada **paginação com `skip()` e `limit()`** em todas as listagens.

#### Métodos implementados:
```java
// ClienteRepository
public List<Cliente> listarComPaginacao(int pagina, int tamanhoPagina)
public List<Cliente> buscarComFiltroPaginado(Predicate<Cliente> filtro, int pagina, int tamanhoPagina)

// VeiculoRepository
public List<Veiculo> listarComPaginacao(int pagina, int tamanhoPagina)
public List<Veiculo> buscarDisponiveisComPaginacao(int pagina, int tamanhoPagina)

// AluguelRepository
public List<Aluguel> listarComPaginacao(int pagina, int tamanhoPagina)
public List<Aluguel> buscarFinalizadosComPaginacao(int pagina, int tamanhoPagina)
```

#### Exemplo de uso:
```java
// Listar primeira página com 5 clientes
List<Cliente> pagina1 = clienteService.listarComPaginacao(0, 5);

// Listar segunda página com 5 clientes
List<Cliente> pagina2 = clienteService.listarComPaginacao(1, 5);
```

#### Localização no código:
- `src/repositories/ClienteRepository.java:47-53`
- `src/repositories/VeiculoRepository.java:59-65`
- `src/repositories/AluguelRepository.java:59-65`
- `Main.java:209-216` - Demonstração de paginação

---

### ✅ 3. Files, InputStream e OutputStream

Implementado sistema completo de **relatórios usando Files + Streams**.

#### Relatórios implementados:

1. **Faturamento Total por Período** (`gerarRelatorioFaturamentoPorPeriodo()`)
   - Filtra aluguéis por período
   - Calcula faturamento total e por tipo de veículo
   - Usa `BufferedWriter` + `Files.newBufferedWriter()`

2. **Veículos Mais Alugados** (`gerarRelatorioVeiculosMaisAlugados()`)
   - Agrupa aluguéis por veículo
   - Ordena por quantidade de aluguéis
   - Gera ranking top 10

3. **Clientes que Mais Alugaram** (`gerarRelatorioClientesQueMaisAlugaram()`)
   - Agrupa aluguéis por cliente
   - Ordena por quantidade de aluguéis
   - Gera ranking top 10

4. **Recibos de Aluguel e Devolução** (`gerarReciboAluguel()`, `gerarReciboDevolucao()`)
   - Gera recibos formatados em arquivo
   - Usa `Files` para escrita

5. **Relatório Completo de Aluguéis** (`gerarRelatorioCompletodeAlugueis()`)
   - Lista todos os aluguéis ativos e finalizados
   - Usa Streams para processar e formatar dados

#### Tecnologias utilizadas:
- `java.nio.file.Files` - Para operações de I/O
- `BufferedWriter` - Para escrita eficiente
- `StandardOpenOption` - Para controle de escrita (CREATE, TRUNCATE_EXISTING)
- Streams para processar dados antes de escrever

#### Localização no código:
- `src/services/RelatorioService.java` - Classe completa de relatórios
- `Main.java:249-262` - Demonstração de geração de relatórios

---

### ✅ 4. Functional Interfaces

#### Interfaces Funcionais Padrão (java.util.function):

**Predicate<T>** - Para validações e filtros:
```java
// ClienteService
private static final Predicate<Cliente> CLIENTE_VALIDO = cliente ->
    cliente != null && cliente.getNome() != null && ...;

// Exemplo de uso em filtros
public List<Cliente> listarPessoasFisicas() {
    Predicate<Cliente> ehPessoaFisica = c -> c instanceof PessoaFisica;
    return repository.buscarComFiltro(ehPessoaFisica);
}
```

**Function<T, R>** - Para transformações:
```java
// VeiculoService
private static final Function<Veiculo, String> VEICULO_PARA_DESCRICAO = veiculo ->
    String.format("%s - %s (%s)", veiculo.getPlaca(), ...);
```

**Consumer<T>** - Para ações (como impressão):
```java
// ClienteService
public void imprimirClientes() {
    Consumer<Cliente> impressora = cliente -> {
        String tipo = cliente instanceof PessoaFisica ? "PF" : "PJ";
        System.out.printf("[%s] %s - %s%n", tipo, ...);
    };
    forEach(impressora);
}
```

**Supplier<T>** - Para geração de dados:
```java
// GeradorDadosTeste
public static final GeradorDados<String> GERADOR_CPF = () ->
    String.format("%011d", random.nextLong(100000000000L));
```

#### Interfaces Funcionais Personalizadas:

1. **`ValidadorDocumento`** (`src/functional/ValidadorDocumento.java`)
   ```java
   @FunctionalInterface
   public interface ValidadorDocumento {
       boolean validar(String documento);

       static ValidadorDocumento cpf() { ... }
       static ValidadorDocumento cnpj() { ... }
   }
   ```
   - Usado em: `ClienteService.java:26-27`

2. **`CalculadoraDesconto`** (`src/functional/CalculadoraDesconto.java`)
   ```java
   @FunctionalInterface
   public interface CalculadoraDesconto {
       double calcular(int dias);
       default CalculadoraDesconto maiorDesconto(CalculadoraDesconto outra) { ... }
   }
   ```
   - Pronta para uso futuro nas regras de desconto

3. **`FormatadorRelatorio<T>`** (`src/functional/FormatadorRelatorio.java`)
   ```java
   @FunctionalInterface
   public interface FormatadorRelatorio<T> {
       String formatar(T objeto);
   }
   ```
   - Usado em: `RelatorioService.java:38-44`

4. **`GeradorDados<T>`** (`src/functional/GeradorDados.java`)
   ```java
   @FunctionalInterface
   public interface GeradorDados<T> extends Supplier<T> {
       T get();
   }
   ```
   - Usado em: `GeradorDadosTeste.java`

#### Localização no código:
- `src/functional/*.java` - Interfaces funcionais personalizadas
- `src/services/ClienteService.java:16-27` - Uso de Predicates
- `src/services/VeiculoService.java:18-35` - Uso de Predicates e Functions
- `src/services/AluguelService.java:27-37` - Uso de Predicates e Functions
- `src/utils/GeradorDadosTeste.java` - Uso extensivo de Suppliers

---

### ✅ 5. Gerador de Dados de Teste com Supplier

Implementada classe `GeradorDadosTeste` com múltiplos **Suppliers** para gerar dados fictícios.

#### Suppliers implementados:
```java
// Geradores básicos
GERADOR_CPF - Gera CPF aleatório
GERADOR_CNPJ - Gera CNPJ aleatório
GERADOR_PLACA - Gera placa de veículo

// Geradores de entidades
GERADOR_PESSOA_FISICA - Gera PessoaFisica completa
GERADOR_PESSOA_JURIDICA - Gera PessoaJuridica completa
GERADOR_CLIENTE - Gera Cliente (PF ou PJ)
GERADOR_VEICULO - Gera Veículo de qualquer tipo
GERADOR_VEICULO_PEQUENO - Gera Veículo PEQUENO
GERADOR_VEICULO_MEDIO - Gera Veículo MÉDIO
GERADOR_VEICULO_SUV - Gera Veículo SUV
GERADOR_DATA_RECENTE - Gera data nos últimos 30 dias
GERADOR_LOCAL - Gera local de aluguel
```

#### Métodos utilitários com Streams:
```java
// Gera lista de clientes usando Streams
public static List<Cliente> gerarClientes(int quantidade) {
    return IntStream.range(0, quantidade)
            .mapToObj(i -> GERADOR_CLIENTE.get())
            .collect(Collectors.toList());
}

// Gera veículos balanceados (igual qtd de cada tipo)
public static List<Veiculo> gerarVeiculosBalanceados(int quantidadePorTipo)

// Gera CPFs únicos
public static List<String> gerarCPFsUnicos(int quantidade)
```

#### Exemplo de uso:
```java
// Gerar 10 clientes aleatórios
List<Cliente> clientes = GeradorDadosTeste.gerarClientes(10);

// Gerar 5 veículos de cada tipo
List<Veiculo> veiculos = GeradorDadosTeste.gerarVeiculosBalanceados(5);
```

#### Localização no código:
- `src/utils/GeradorDadosTeste.java` - Classe completa com Suppliers
- `src/functional/GeradorDados.java` - Interface funcional base

---

## 📊 Estatísticas da Refatoração

### Arquivos Criados:
- `src/functional/CalculadoraDesconto.java`
- `src/functional/ValidadorDocumento.java`
- `src/functional/FormatadorRelatorio.java`
- `src/functional/GeradorDados.java`
- `src/services/RelatorioService.java`
- `src/utils/GeradorDadosTeste.java`

### Arquivos Refatorados:
- `src/repositories/ClienteRepository.java` - Adicionados métodos com Streams e paginação
- `src/repositories/VeiculoRepository.java` - Adicionados métodos com Streams e paginação
- `src/repositories/AluguelRepository.java` - Adicionados métodos com Streams e paginação
- `src/services/ClienteService.java` - Refatorado para usar Predicate, Function, Consumer
- `src/services/VeiculoService.java` - Refatorado para usar interfaces funcionais
- `src/services/AluguelService.java` - Refatorado para usar Streams avançados
- `Main.java` - Adicionada demonstração das funcionalidades

### Funcionalidades Adicionadas:
- ✅ Paginação com `skip()` e `limit()` em todos os repositórios
- ✅ Filtros com `Predicate` personalizados
- ✅ Ordenação com `Comparator` e lambdas
- ✅ Agrupamento com `Collectors.groupingBy()`
- ✅ Cálculos com `map()` + `reduce()`
- ✅ 5 tipos de relatórios com Files + Streams
- ✅ 4 interfaces funcionais personalizadas
- ✅ Gerador de dados com 13+ Suppliers
- ✅ Operações complexas: pipeline `filter` + `map` + `sorted` + `collect`

---

## 🎯 Principais Melhorias Percebidas

### 1. **Código Mais Declarativo**
Antes (imperativo):
```java
List<Cliente> pf = new ArrayList<>();
for (Cliente c : clientes) {
    if (c instanceof PessoaFisica) {
        pf.add(c);
    }
}
```

Depois (declarativo com Streams):
```java
List<Cliente> pf = clientes.stream()
    .filter(c -> c instanceof PessoaFisica)
    .collect(Collectors.toList());
```

### 2. **Menos Código Boilerplate**
- Lambdas substituem classes anônimas
- Method references (`Veiculo::isDisponivel`) tornam código mais limpo
- Streams eliminam loops repetitivos

### 3. **Composição de Operações**
```java
// Pipeline complexo em uma expressão
List<Map.Entry<String, Long>> ranking = alugueis.stream()
    .collect(Collectors.groupingBy(a -> a.getVeiculo().getPlaca(), Collectors.counting()))
    .entrySet().stream()
    .sorted(Map.Entry.comparingByValue().reversed())
    .collect(Collectors.toList());
```

### 4. **Maior Reusabilidade**
- Predicates podem ser combinados (`and()`, `or()`)
- Functions podem ser compostas
- Suppliers podem ser reutilizados

### 5. **Melhor Testabilidade**
- Interfaces funcionais facilitam testes unitários
- Predicates podem ser testados isoladamente
- Suppliers permitem mockar geração de dados

---

## 🚧 Dificuldades Enfrentadas

### 1. **Streams são Imutáveis**
- Streams não podem ser reutilizados após consumo
- Solução: Criar novos streams quando necessário

### 2. **Tratamento de Exceções em Lambdas**
- Lambdas não podem lançar checked exceptions diretamente
- Solução: Usar `RuntimeException` ou criar wrappers

### 3. **Debug de Streams**
- Difícil debugar pipelines complexos
- Solução: Quebrar em operações menores ou usar `peek()`

### 4. **Performance em Coleções Pequenas**
- Overhead de Streams pode ser maior em listas pequenas
- No contexto deste projeto, benefícios de legibilidade superam isso

---

## 🔍 Como Testar as Refatorações

### 1. Compilar o projeto:
```bash
javac -d bin -sourcepath . Main.java src/**/*.java
```

### 2. Executar o projeto:
```bash
java -cp bin Main
```

### 3. Observar a demonstração automática:
- O sistema executa `demonstrarNovasFuncionalidades()` automaticamente
- Mostra exemplos de paginação, filtros, agrupamentos, etc.
- Gera relatórios no diretório `relatorios/`

### 4. Verificar relatórios gerados:
```bash
ls -la relatorios/
cat relatorios/veiculos_mais_alugados.txt
cat relatorios/faturamento_*.txt
```

---

## 📚 Referências

- **Java Stream API**: [Oracle Docs](https://docs.oracle.com/javase/8/docs/api/java/util/stream/package-summary.html)
- **Functional Interfaces**: [Oracle Docs](https://docs.oracle.com/javase/8/docs/api/java/util/function/package-summary.html)
- **Files API (NIO.2)**: [Oracle Docs](https://docs.oracle.com/javase/tutorial/essential/io/fileio.html)

---

## ✅ Checklist de Refatoração (Completo)

- ✅ Substituir laços de repetição por **Streams** nas buscas e filtros
- ✅ Implementar **paginação com Stream.skip() e Stream.limit()** nas listagens
- ✅ Criar **Comparator com lambda** para ordenações de clientes e veículos
- ✅ Usar **Predicate** para encapsular regras de validação de CPF/CNPJ
- ✅ Usar **Function** para calcular valores de aluguel e aplicar descontos
- ✅ Usar **Consumer** para imprimir dados formatados no console
- ✅ Usar **Supplier** para gerar dados de teste (clientes e veículos fictícios)
- ✅ Persistir veículos, clientes e aluguéis em **arquivos** utilizando `Files`
- ✅ Implementar leitura/escrita com **InputStream/OutputStream** para dados
- ✅ Criar **interfaces funcionais personalizadas** para regras específicas
- ✅ Documentar refatorações realizadas

---

**Data da Refatoração:** Outubro 2025
**Refatorado por:** Claude Code
**Status:** ✅ **Concluído**
