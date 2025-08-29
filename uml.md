```mermaid
classDiagram
    %% ENUMS
    class TipoVeiculo {
        <<enumeration>>
        PEQUENO
        MEDIO
        SUV
        +getValorDiaria() double
    }

    %% CLASSES PRINCIPAIS
    class Veiculo {
        -String placa
        -String nome
        -TipoVeiculo tipo
        -boolean disponivel
        +getPlaca() String
        +getNome() String
        +getTipo() TipoVeiculo
        +isDisponivel() boolean
        +setDisponivel(boolean) void
        +equals(Object) boolean
        +hashCode() int
    }

    class Cliente {
        <<abstract>>
        -String id
        -String nome
        +getId() String
        +getNome() String
        +setNome(String) void
        +calcularDesconto(int dias) double
        +equals(Object) boolean
        +hashCode() int
    }

    class PessoaFisica {
        -String cpf
        +getCpf() String
        +calcularDesconto(int dias) double
    }

    class PessoaJuridica {
        -String cnpj
        +getCnpj() String
        +calcularDesconto(int dias) double
    }

    class Aluguel {
        -String id
        -LocalDateTime dataHoraAluguel
        -LocalDateTime dataHoraDevolucao
        -String localAluguel
        -String localDevolucao
        -Veiculo veiculo
        -Cliente cliente
        -double valorTotal
        +getId() String
        +getDataHoraAluguel() LocalDateTime
        +getDataHoraDevolucao() LocalDateTime
        +getLocalAluguel() String
        +getLocalDevolucao() String
        +getVeiculo() Veiculo
        +getCliente() Cliente
        +getValorTotal() double
        +devolver(LocalDateTime, String) void
        -calcularValorTotal() void
    }

    %% RELACIONAMENTOS
    Cliente <|-- PessoaFisica
    Cliente <|-- PessoaJuridica
    
    Veiculo "1" -- "1" TipoVeiculo : possui
    Veiculo "1" -- "*" Aluguel : está em
    Cliente "1" -- "*" Aluguel : realiza

```
