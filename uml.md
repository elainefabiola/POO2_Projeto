# Diagrama UML Completo

Este diagrama mostra as camadas do sistema após as alterações recentes (BigDecimal, derivado, services, repositórios).

```mermaid
classDiagram
    %% =========================
    %% CAMADA DE DOMÍNIO
    %% =========================
    class TipoVeiculo {
        <<enumeration>>
        PEQUENO
        MEDIO
        SUV
        -valorDiaria : BigDecimal
        +getValorDiaria() BigDecimal
    }

    class Veiculo {
        -placa : String
        -nome : String
        -tipo : TipoVeiculo
        -disponivel : boolean
        %% {derived} → calculado a partir de Aluguéis ativos
        +getPlaca() String
        +setPlaca(String) void
        +getNome() String
        +setNome(String) void
        +getTipo() TipoVeiculo
        +setTipo(TipoVeiculo) void
        +isDisponivel() boolean
    }

    class Cliente {
        <<abstract>>
        -documento : String
        -nome : String
        +getDocumento() String
        +setDocumento(String) void
        +getNome() String
        +setNome(String) void
        +calcularDesconto(dias:int, bruto:BigDecimal) BigDecimal
    }

    class PessoaFisica {
        -cpf : String
        +getCpf() String
        +calcularDesconto(dias:int, bruto:BigDecimal) BigDecimal
    }

    class PessoaJuridica {
        -cnpj : String
        +getCnpj() String
        +calcularDesconto(dias:int, bruto:BigDecimal) BigDecimal
    }

    class Aluguel {
        -id : String
        -cliente : Cliente
        -veiculo : Veiculo
        -dataHoraRetirada : LocalDateTime
        -dataHoraDevolucao : LocalDateTime
        -localRetirada : String
        -localDevolucao : String
        -valorTotal : BigDecimal
        -calcularValorTotal() BigDecimal
        +getId() String
        +getCliente() Cliente
        +getVeiculo() Veiculo
        +getDataHoraRetirada() LocalDateTime
        +getDataHoraDevolucao() LocalDateTime
        +getLocalRetirada() String
        +getLocalDevolucao() String
        +getValorTotal() BigDecimal
        +finalizar(devolucao:LocalDateTime, localDevolucao:String) void
    }

    %% Herança
    Cliente <|-- PessoaFisica
    Cliente <|-- PessoaJuridica

    %% Associações de domínio
    Aluguel o-- Veiculo : refere-se a
    Aluguel o-- Cliente : refere-se a
    Veiculo "1" -- "1" TipoVeiculo : possui

    %% =========================
    %% CAMADA DE REPOSITÓRIOS (CONTRATOS)
    %% =========================
    class ClienteRepository {
        <<interface>>
        +save(Cliente) void
        +findByDocumento(String) Optional~Cliente~
        +findByNomeContains(String) List~Cliente~
        +findAll() List~Cliente~
    }

    class VeiculoRepository {
        <<interface>>
        +save(Veiculo) void
        +findByPlaca(String) Optional~Veiculo~
        +findAll() List~Veiculo~
        +findDisponiveis() List~Veiculo~
    }

    class AluguelRepository {
        <<interface>>
        +save(Aluguel) void
        +findById(String) Optional~Aluguel~
        +findAtivos() List~Aluguel~
        +findByCliente(String) List~Aluguel~
        +findByVeiculo(String) List~Aluguel~
    }

    %% =========================
    %% CAMADA DE SERVIÇOS (CASOS DE USO)
    %% =========================
    class ClienteService {
        +cadastrar(Cliente) void
        +buscarPorDocumento(String) Optional~Cliente~
        +listar() List~Cliente~
        +buscarPorNome(String) List~Cliente~
    }

    class VeiculoService {
        +cadastrar(Veiculo) void
        +buscarPorPlaca(String) Optional~Veiculo~
        +listar() List~Veiculo~
        +listarDisponiveis() List~Veiculo~
    }

    class AluguelService {
        +alugar(documento:String, placa:String, retirada:LocalDateTime, localRetirada:String) Aluguel
        +devolver(aluguelId:String, devolucao:LocalDateTime, localDevolucao:String) void
        +listarAtivos() List~Aluguel~
        +listarPorCliente(String) List~Aluguel~
        +listarPorVeiculo(String) List~Aluguel~
    }

    %% Dependências: serviços usam repositórios
    ClienteService ..> ClienteRepository : usa
    VeiculoService ..> VeiculoRepository : usa
    AluguelService ..> AluguelRepository : usa
    AluguelService ..> ClienteRepository : valida/busca
    AluguelService ..> VeiculoRepository : valida/busca
