
# Refatoração de Projeto: ADA LocateCar – Locadora de Veículos

## Contexto
Na primeira entrega do projeto da **Locadora de Veículos (ADA LocateCar)**, você implementou todas as funcionalidades básicas utilizando estruturas tradicionais do Java (loops, condicionais, coleções). Agora, nesta nova etapa, o objetivo é **refatorar o projeto existente** para aplicar conceitos mais avançados de Java, deixando a aplicação mais expressiva, enxuta e aderente às boas práticas.

---

## Objetivo da Refatoração
Você deverá **manter todas as funcionalidades já implementadas**, porém **reescrevendo partes do código** de forma a utilizar obrigatoriamente:

1. **Streams em Java**
    - Substituir buscas, filtros, ordenações e contagens que utilizavam loops por operações de `Stream`.
    - Implementar **paginação obrigatória** em todas as listagens, utilizando `skip()` e `limit()`.
    - Criar pipelines compostos (ex.: map + filter + sorted + collect).

2. **Files, InputStream e OutputStream**
    - Implementar relatórios com **Streams + arquivos**, como:
        - Faturamento total por período.
        - Veículos mais alugados.
        - Clientes que mais alugaram.
        - Recibos de aluguel e/ou devolução.

3. **Functional Interfaces**
    - Utilizar **Predicate, Consumer, Function, Supplier** nas operações com Streams.
    - Criar **interfaces funcionais personalizadas** (quando necessário) para encapsular regras de negócio (ex.: cálculo de desconto, validações de unicidade).
    - Substituir lógicas de filtragem, transformação ou cálculo que antes eram implementadas com classes anônimas.

---

## Regras do Projeto (mantidas da versão anterior)
- **RN1:** Veículos não podem ser repetidos (placa como identificador).
- **RN2:** Tipos de veículos válidos: `PEQUENO`, `MEDIO`, `SUV`.
- **RN3:** Aluguéis e devoluções devem registrar local, data e hora.
- **RN4:** Aluguel com horas fracionadas conta como diária cheia.
- **RN5:** Veículos alugados não podem estar disponíveis para novo aluguel.
- **RN6:** Clientes não podem estar duplicados (CPF ou CNPJ como identificadores).
- **RN7:** Descontos de devolução:
    - PF: acima de 5 diárias → 5% desconto.
    - PJ: acima de 3 diárias → 10% desconto.

---

## Checklist de Refatoração (Sugestão)

- ✅ Substituir laços de repetição por **Streams** nas buscas e filtros.
- ✅ Implementar **paginação com Stream.skip() e Stream.limit()** nas listagens.
- ✅ Criar **Comparator com lambda** para ordenações de clientes e veículos.
- ✅ Usar **Predicate** para encapsular regras de validação de CPF/CNPJ.
- ✅ Usar **Function** para calcular valores de aluguel e aplicar descontos.
- ✅ Usar **Consumer** para imprimir dados formatados no console.
- ✅ Usar **Supplier** para gerar dados de teste (clientes e veículos fictícios).
- ✅ Persistir veículos, clientes e aluguéis em **arquivos** utilizando `Files`.
- ✅ Implementar leitura/escrita com **InputStream/OutputStream** para dados.
- ✅ Criar **interfaces funcionais personalizadas** para regras específicas (ex.: cálculo de desconto).
- ✅ Atualizar **README.md** com descrição da refatoração.

---


## Entrega
- Repositório GitHub atualizado com a versão refatorada do projeto.
- README.md atualizado, destacando:
    - Quais partes foram refatoradas para Streams, Files, InputStream/OutputStream e Interfaces Funcionais.
    - Principais melhorias percebidas com a refatoração.
    - Dificuldades enfrentadas no processo.