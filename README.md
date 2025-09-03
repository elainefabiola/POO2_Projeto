# Sistema de Locação de Veículos

Sistema de gerenciamento de locação de veículos desenvolvido em Java seguindo os princípios SOLID e arquitetura em camadas.

## Estrutura do Projeto

O projeto está organizado em camadas bem definidas:

```
src/
├── Main.java                 # Ponto de entrada da aplicação
├── database/                 # Camada de dados (entidades)
│   ├── Cliente.java
│   ├── PessoaFisica.java
│   ├── PessoaJuridica.java
│   ├── Veiculo.java
│   ├── TipoVeiculo.java
│   └── Aluguel.java
├── repositories/             # Camada de acesso a dados
│   ├── RepositorioMemoria.java
│   ├── ClienteRepository.java
│   ├── VeiculoRepository.java
│   └── AluguelRepository.java
├── services/                 # Camada de regras de negócio
│   ├── ClienteService.java
│   ├── VeiculoService.java
│   └── AluguelService.java
└── views/                    # Camada de interface do usuário
    └── MenuPrincipal.java
```

## Arquitetura

### Database (Entidades)
- **Cliente**: Classe abstrata base para clientes
- **PessoaFisica**: Cliente pessoa física com CPF
- **PessoaJuridica**: Cliente pessoa jurídica com CNPJ
- **Veiculo**: Entidade que representa um veículo
- **TipoVeiculo**: Enum com tipos de veículo e valores de diária
- **Aluguel**: Entidade que representa um aluguel

### Repositories (Acesso a Dados)
- **RepositorioMemoria**: Classe base genérica para repositórios em memória
- **ClienteRepository**: Repositório específico para clientes
- **VeiculoRepository**: Repositório específico para veículos
- **AluguelRepository**: Repositório específico para aluguéis

### Services (Regras de Negócio)
- **ClienteService**: Serviços para gerenciamento de clientes
- **VeiculoService**: Serviços para gerenciamento de veículos
- **AluguelService**: Serviços para gerenciamento de aluguéis

### Views (Interface do Usuário)
- **MenuPrincipal**: Interface de console para interação com o usuário

## Funcionalidades

### Gerenciamento de Clientes
- Cadastrar cliente (pessoa física ou jurídica)
- Buscar cliente por documento ou nome
- Listar todos os clientes
- Remover cliente

### Gerenciamento de Veículos
- Cadastrar veículo (PEQUENO, MÉDIO, SUV)
- Buscar veículo por placa ou nome
- Listar todos os veículos
- Listar veículos disponíveis
- Remover veículo

### Gerenciamento de Aluguéis
- Realizar aluguel
- Finalizar aluguel
- Buscar aluguéis por cliente
- Buscar aluguéis por veículo
- Listar aluguéis ativos
- Listar todos os aluguéis

## Regras de Negócio

### Descontos
- **Pessoa Física**: 5% de desconto para aluguéis com mais de 5 dias
- **Pessoa Jurídica**: 10% de desconto para aluguéis com mais de 3 dias

### Valores de Diária
- **PEQUENO**: R$ 100,00
- **MÉDIO**: R$ 150,00
- **SUV**: R$ 200,00

## Como Executar

1. Compile o projeto:
```bash
javac src/Main.java src/database/*.java src/repositories/*.java src/services/*.java src/views/*.java
```

2. Execute o programa:
```bash
java -cp src Main
```

## Princípios SOLID Aplicados

- **S**: Single Responsibility Principle - Cada classe tem uma responsabilidade específica
- **O**: Open/Closed Principle - Classes abertas para extensão, fechadas para modificação
- **L**: Liskov Substitution Principle - Subclasses podem substituir suas classes base
- **I**: Interface Segregation Principle - Interfaces específicas para cada necessidade
- **D**: Dependency Inversion Principle - Dependências de abstrações, não de implementações

## Tecnologias Utilizadas

- Java 11+
- Console I/O
- Collections Framework
- Date/Time API
