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
        +setTipoVeiculo() TipoVeiculo
        +getPlaca() String
        +getNome() String
        +getTipo() TipoVeiculo
        +isDisponivel() boolean
        +setDisponivel(boolean) void
    }

    class Cliente {
        <<abstract>>
        -String documento
        -String nome
        +setDocumento() string
        +getDocumento() String
        +getNome() String
        +setNome(String) void
        +calcularDesconto(int dias) double
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
        -String documento
        -LocalDateTime dataEHoraAlugada
        -LocalDateTime dataHoraDevolucao
        -String localAluguel
        -String localDevolucao
        -Veiculo veiculo
        -Cliente cliente
        -double valorTotal
        +getDocumento() String
        +getdataEHoraAlugada() LocalDateTime
        +getDataHoraDevolucao() LocalDateTime
        +getLocalAluguel() String
        +getLocalDevolucao() String
        +getVeiculo() Veiculo
        +getCliente() Cliente
        +getValorTotal() double
        +devolver(LocalDateTime DataHoraDevolucao, String localAluguel) void
        -calcularValorTotal() void
    }

    class Locadora {
        -String nome
        -Set~Veiculo~ veiculos
        -Set~Cliente~ clientes
        -List~Aluguel~ alugueis
        +cadastrarVeiculo(Veiculo) void
        +alterarVeiculo(String, Veiculo) void
        +buscarVeiculo(String) Set~document~
        +cadastrarCliente(Cliente) void
        +alterarCliente(String, Cliente) void
        +alugarVeiculo(String, String, LocalDateTime, String) Aluguel
        +devolverVeiculo(String, LocalDateTime, String) Aluguel
    }

    class Gravar {
        -Veiculo[] veiculo
        -Cliente[] cliente
        +setGravarVeiculo() Veiculo
        +setGravarCliente(int dias) Cliente
        +setGravarAluguel(int dias) double
        +getGravarVeiculo() Veiculo
        +getGravarCliente(int dias) Cliente
        +getGravarAluguel(int dias) double
    }

    %% RELACIONAMENTOS
    Cliente <|-- PessoaFisica
    Cliente <|-- PessoaJuridica
    
    Veiculo "1" -- "1" TipoVeiculo : possui
    Veiculo "1" -- "*" Aluguel : está em
    Cliente "1" -- "*" Aluguel : realiza

    Locadora "1" -- "*" Veiculo : gerencia
    Locadora "1" -- "*" Cliente : possui
    Locadora "1" -- "*" Aluguel : controla

```
