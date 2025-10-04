# 🧪 Onde Testar Cada Item do Refact.md

## 📍 Mapa Completo de Testes das Refatorações

Este documento mostra **exatamente onde** você pode testar cada item exigido no `Refact.md` através do sistema interativo.

---

## 🎯 Como Acessar os Menus de Teste

### 1. Compilar e Executar
```bash
./compilar.sh
java -cp bin Main
```

### 2. Navegar no Menu Principal
```
=============================================================
         ADA LOCATECAR - MENU PRINCIPAL
=============================================================
1 - Gestão de Clientes
2 - Gestão de Veículos
3 - Gestão de Aluguéis
-------------------------------------------------------------
4 - 🔍 DEMONSTRAÇÃO DAS REFATORAÇÕES (Streams, Predicates...)  ⬅️ TESTAR AQUI!
5 - 📊 RELATÓRIOS (Files + Streams)                             ⬅️ TESTAR AQUI!
-------------------------------------------------------------
0 - Sair
```

---

## ✅ CHECKLIST REFACT.MD - Onde Testar Cada Item

### 1. ✅ **Streams em Java**
   **Exigência:** Substituir buscas, filtros, ordenações e contagens por Streams

   **Onde Testar:**
   - **Menu Principal → Opção 4 (Demonstração)**
     - Todas as opções (1-7) usam Streams
     - Opção 8 executa todas de uma vez

   **Código-fonte:**
   - `ClienteRepository.java:47-100` - Métodos com Streams
   - `VeiculoRepository.java:59-130` - Métodos com Streams
   - `AluguelRepository.java:59-165` - Métodos com Streams

   **Exemplos testáveis:**
   ```
   Menu 4 → Opção 2: Filtros com Predicate
   Menu 4 → Opção 3: Agrupamento com groupingBy
   Menu 4 → Opção 7: Ordenação com Comparator
   ```

---

### 2. ✅ **Paginação com skip() e limit()**
   **Exigência:** Implementar paginação **obrigatória** em todas as listagens

   **Onde Testar:**
   - **Menu Principal → Opção 4 → Opção 1**
     - Digite tamanho da página (ex: 5)
     - Digite número da página (ex: 0, 1, 2...)
     - Sistema mostra clientes e veículos paginados

   **Código-fonte:**
   - `ClienteRepository.java:47` - `listarComPaginacao()`
   - `VeiculoRepository.java:59` - `listarComPaginacao()`
   - `AluguelRepository.java:59` - `listarComPaginacao()`

   **Implementação:**
   ```java
   .skip((long) pagina * tamanhoPagina)
   .limit(tamanhoPagina)
   ```

---

### 3. ✅ **Predicate<T> para Validações**
   **Exigência:** Usar Predicate para encapsular regras de validação

   **Onde Testar:**
   - **Menu Principal → Opção 4 → Opção 2**
     - Filtra apenas Pessoas Físicas
     - Filtra apenas Pessoas Jurídicas
     - Filtra veículos disponíveis

   **Código-fonte:**
   - `ClienteService.java:17-27` - Predicates para validação
   - `VeiculoService.java:19-27` - Predicates para validação
   - `ClienteService.java:88-98` - Uso de Predicates

   **Exemplo de código:**
   ```java
   Predicate<Cliente> ehPessoaFisica = c -> c instanceof PessoaFisica;
   repository.buscarComFiltro(ehPessoaFisica)
   ```

---

### 4. ✅ **Function<T,R> para Transformações**
   **Exigência:** Usar Function para calcular valores e transformações

   **Onde Testar:**
   - **Menu Principal → Opção 4 → Opção 4**
     - Rankings de veículos mais alugados
     - Rankings de clientes que mais alugaram
     - (usa Function para transformar e agregar dados)

   **Código-fonte:**
   - `VeiculoService.java:30-35` - Function para descrição
   - `AluguelService.java:32-37` - Function para descrição
   - `AluguelService.java:195-203` - Function em pipeline complexo

   **Exemplo de código:**
   ```java
   Function<Veiculo, String> paraDescricao = veiculo ->
       String.format("%s - %s", veiculo.getPlaca(), veiculo.getNome());
   ```

---

### 5. ✅ **Consumer<T> para Ações**
   **Exigência:** Usar Consumer para imprimir dados formatados

   **Onde Testar:**
   - **Menu Principal → Opção 4 → Opção 6**
     - Imprime clientes usando Consumer
     - Imprime veículos usando Consumer

   **Código-fonte:**
   - `ClienteService.java:111-118` - Método `imprimirClientes()`
   - `VeiculoService.java:118-122` - Método `imprimirVeiculos()`

   **Exemplo de código:**
   ```java
   Consumer<Cliente> impressora = cliente -> {
       System.out.printf("[%s] %s%n", tipo, cliente.getNome());
   };
   clientes.forEach(impressora);
   ```

---

### 6. ✅ **Supplier<T> para Geração de Dados**
   **Exigência:** Usar Supplier para gerar dados de teste

   **Onde Ver:**
   - **Não tem menu interativo** (usados internamente)
   - Ver código em `GeradorDadosTeste.java`

   **Código-fonte:**
   - `GeradorDadosTeste.java:45-111` - 13+ Suppliers implementados
   - `GeradorDadosTeste.java:116-223` - Métodos que usam Suppliers

   **Suppliers disponíveis:**
   ```java
   GERADOR_CPF
   GERADOR_CNPJ
   GERADOR_PLACA
   GERADOR_PESSOA_FISICA
   GERADOR_PESSOA_JURIDICA
   GERADOR_VEICULO
   GERADOR_DATA_RECENTE
   GERADOR_LOCAL
   ```

   **Como testar manualmente:**
   ```java
   // No código ou em teste
   String cpf = GeradorDadosTeste.GERADOR_CPF.get();
   List<Cliente> clientes = GeradorDadosTeste.gerarClientes(10);
   ```

---

### 7. ✅ **Comparator com Lambda**
   **Exigência:** Criar Comparator com lambda para ordenações

   **Onde Testar:**
   - **Menu Principal → Opção 4 → Opção 7**
     - Clientes ordenados por nome (A-Z)
     - Veículos ordenados por nome (A-Z)
     - Aluguéis ordenados por data (mais recentes primeiro)

   **Código-fonte:**
   - `ClienteService.java:123-127` - `listarOrdenadosPorNome()`
   - `VeiculoService.java:143-147` - `listarOrdenadosPorNome()`
   - `AluguelService.java:234-238` - `listarOrdenadosPorDataRetirada()`

   **Exemplo de código:**
   ```java
   .sorted(Comparator.comparing(Cliente::getNome))
   .sorted(Comparator.comparing(Aluguel::getData).reversed())
   ```

---

### 8. ✅ **Files + Streams para Relatórios**
   **Exigência:** Implementar relatórios com Streams + arquivos

   **Onde Testar:**
   - **Menu Principal → Opção 5 (Relatórios)**
     - Opção 1: Faturamento por período
     - Opção 2: Veículos mais alugados
     - Opção 3: Clientes que mais alugaram
     - Opção 4: Recibo de aluguel
     - Opção 5: Recibo de devolução
     - Opção 6: Relatório completo
     - Opção 7: Gerar TODOS os relatórios

   **Código-fonte:**
   - `RelatorioService.java` - Classe completa (640 linhas)
   - Todos os métodos usam `Files.newBufferedWriter()` + Streams

   **Arquivos gerados:**
   ```
   relatorios/
   ├── faturamento_2025-10-03_a_2025-11-02.txt
   ├── veiculos_mais_alugados.txt
   ├── clientes_que_mais_alugaram.txt
   ├── recibo_aluguel_12345678.txt
   ├── recibo_devolucao_12345678.txt
   └── relatorio_completo_alugueis.txt
   ```

   **Como verificar:**
   ```bash
   ls -la relatorios/
   cat relatorios/veiculos_mais_alugados.txt
   ```

---

### 9. ✅ **Interfaces Funcionais Personalizadas**
   **Exigência:** Criar interfaces funcionais personalizadas para regras específicas

   **Interfaces Criadas:**
   1. **`ValidadorDocumento`** - Validação de CPF/CNPJ
   2. **`CalculadoraDesconto`** - Cálculo de descontos
   3. **`FormatadorRelatorio<T>`** - Formatação de relatórios
   4. **`GeradorDados<T>`** - Geração de dados de teste

   **Onde Ver o Uso:**
   - **ValidadorDocumento:** `ClienteService.java:26-27`
   - **FormatadorRelatorio:** `RelatorioService.java:38-44`
   - **GeradorDados:** `GeradorDadosTeste.java` (toda classe)
   - **CalculadoraDesconto:** Criada e pronta para uso futuro

   **Código-fonte:**
   - `src/functional/ValidadorDocumento.java`
   - `src/functional/CalculadoraDesconto.java`
   - `src/functional/FormatadorRelatorio.java`
   - `src/functional/GeradorDados.java`

---

### 10. ✅ **Pipeline Complexo: map + filter + sorted + collect**
   **Exigência:** Criar pipelines compostos

   **Onde Testar:**
   - **Menu Principal → Opção 4 → Opção 4**
     - Rankings (veículos/clientes mais ativos)
     - Usa pipeline: `groupingBy` + `counting` + `sorted` + `collect`

   **Código-fonte:**
   - `AluguelService.java:195-203` - Pipeline para veículos
   - `AluguelService.java:208-216` - Pipeline para clientes
   - `AluguelService.java:221-229` - Pipeline com reducing

   **Exemplo de pipeline:**
   ```java
   aluguelRepository.listarTodos().stream()
       .collect(Collectors.groupingBy(
           a -> a.getVeiculo().getPlaca(),
           Collectors.counting()))
       .entrySet().stream()
       .sorted(Map.Entry.comparingByValue().reversed())
       .collect(Collectors.toList());
   ```

---

## 📊 Tabela Resumo: Item do Refact.md → Onde Testar

| Item Refact.md | Menu Interativo | Arquivo de Código |
|----------------|-----------------|-------------------|
| Streams | Menu 4 → Todas opções | `*Repository.java` |
| Paginação skip/limit | Menu 4 → Opção 1 | `*Repository.java:47-53` |
| Predicate | Menu 4 → Opção 2 | `*Service.java:17-27` |
| Function | Menu 4 → Opção 4 | `*Service.java:30-37` |
| Consumer | Menu 4 → Opção 6 | `*Service.java:111-118` |
| Supplier | Ver código | `GeradorDadosTeste.java` |
| Comparator | Menu 4 → Opção 7 | `*Service.java:123-127` |
| Files + Streams | Menu 5 → Todas opções | `RelatorioService.java` |
| Interfaces Personalizadas | Ver código | `src/functional/*.java` |
| Pipeline Complexo | Menu 4 → Opção 4 | `AluguelService.java:195-229` |

---

## 🎮 Roteiro de Teste Completo

### Teste Rápido (5 minutos)
```
1. Execute: java -cp bin Main
2. Menu Principal → Opção 4 → Opção 8 (executa TODAS demonstrações)
3. Menu Principal → Opção 5 → Opção 7 (gera TODOS relatórios)
4. Verificar: ls -la relatorios/
```

### Teste Detalhado (15 minutos)
```
1. Menu 4 → Opção 1: Testar paginação (tamanho=5, página=0)
2. Menu 4 → Opção 2: Ver filtros com Predicate
3. Menu 4 → Opção 3: Ver agrupamentos
4. Menu 4 → Opção 4: Ver rankings (TOP 5)
5. Menu 4 → Opção 5: Ver cálculo de faturamento
6. Menu 4 → Opção 6: Ver impressão com Consumer
7. Menu 4 → Opção 7: Ver ordenação com Comparator
8. Menu 5 → Opção 1: Gerar relatório de faturamento
9. Menu 5 → Opção 2: Gerar relatório de veículos
10. Menu 5 → Opção 6: Gerar relatório completo
11. Verificar arquivos: cat relatorios/*.txt
```

---

## 🔍 Como Verificar o Código-Fonte

### Buscar por Streams
```bash
grep -r "\.stream()" src/ | grep -v "\.class"
```

### Buscar por Predicate
```bash
grep -r "Predicate<" src/
```

### Buscar por skip/limit
```bash
grep -r "\.skip\|\.limit" src/
```

### Ver todas as interfaces funcionais
```bash
ls -la src/functional/
```

---

## 📝 Notas Importantes

### ✅ Tudo está Implementado
- **Todos** os 10 itens do checklist do Refact.md estão implementados
- **Todos** podem ser testados via menu interativo ou código
- **Todos** têm localização documentada neste arquivo

### 📁 Arquivos Principais para Avaliação
1. **Streams + Paginação:** `src/repositories/*Repository.java`
2. **Interfaces Funcionais:** `src/services/*Service.java`
3. **Files + Relatórios:** `src/services/RelatorioService.java`
4. **Interfaces Personalizadas:** `src/functional/*.java`
5. **Supplier:** `src/utils/GeradorDadosTeste.java`
6. **Menu de Testes:** `src/views/MenuPrincipal.java` (linhas 613-1115)

### 🎯 Demonstração Automática
- O `Main.java` já executa uma demonstração automática na primeira execução
- Você pode acessar demonstrações interativas a qualquer momento no Menu 4 e 5

---

## ✅ Conclusão

**Todos os itens exigidos no Refact.md foram implementados e podem ser testados:**

1. ✅ Streams → Menu 4 (todas opções)
2. ✅ Paginação → Menu 4 → Opção 1
3. ✅ Predicate → Menu 4 → Opção 2
4. ✅ Function → Menu 4 → Opção 4
5. ✅ Consumer → Menu 4 → Opção 6
6. ✅ Supplier → Ver `GeradorDadosTeste.java`
7. ✅ Comparator → Menu 4 → Opção 7
8. ✅ Files + Streams → Menu 5 (todas opções)
9. ✅ Interfaces Personalizadas → Ver `src/functional/`
10. ✅ Pipeline Complexo → Menu 4 → Opção 4

**Para testar tudo de uma vez:**
```bash
./compilar.sh
java -cp bin Main
# Depois: Menu 4 → Opção 8 e Menu 5 → Opção 7
```

---

**Versão:** 2.0 - Refatorada com Menu Interativo de Testes
**Data:** Outubro 2025
**Status:** ✅ Pronto para teste e avaliação
