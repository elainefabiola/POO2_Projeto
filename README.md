# Sistema de Locação de Veículos

Sistema de gerenciamento de locação de veículos desenvolvido em Java seguindo os princípios SOLID e arquitetura em camadas.

## 📁 Estrutura do Projeto

O projeto está organizado em camadas bem definidas:

```
src/
├── Main.java                 # Ponto de entrada da aplicação
├── database/                 # Camada de dados (entidades)
│   ├── Cliente.java          # Classe abstrata base para clientes
│   ├── PessoaFisica.java     # Cliente pessoa física com CPF
│   ├── PessoaJuridica.java   # Cliente pessoa jurídica com CNPJ
│   ├── Veiculo.java          # Entidade que representa um veículo
│   ├── TipoVeiculo.java      # Enum com tipos de veículo e valores de diária
│   └── Aluguel.java          # Entidade que representa um aluguel (em desenvolvimento)
├── repositories/             # Camada de acesso a dados
│   ├── ClienteRepository.java    # Repositório específico para clientes
│   ├── VeiculoRepository.java    # Repositório específico para veículos
│   └── AluguelRepository.java    # Repositório específico para aluguéis (em desenvolvimento)
├── services/                 # Camada de regras de negócio
│   ├── ClienteService.java       # Serviços para gerenciamento de clientes
│   ├── VeiculoService.java       # Serviços para gerenciamento de veículos (em desenvolvimento)
│   └── AluguelService.java       # Serviços para gerenciamento de aluguéis (em desenvolvimento)
└── views/                    # Camada de interface do usuário
    └── MenuPrincipal.java    # Interface de console para interação com o usuário
```

## 🏗️ Arquitetura

### Database (Entidades)
- **Cliente**: Classe abstrata base para clientes com documento e nome
- **PessoaFisica**: Cliente pessoa física com CPF e regras de desconto específicas
- **PessoaJuridica**: Cliente pessoa jurídica com CNPJ e regras de desconto específicas
- **Veiculo**: Entidade que representa um veículo com placa, nome, tipo e disponibilidade
- **TipoVeiculo**: Enum com tipos de veículo (PEQUENO, MÉDIO, SUV) e valores de diária
- **Aluguel**: Entidade que representa um aluguel (em desenvolvimento)

### Repositories (Acesso a Dados)
- **ClienteRepository**: Repositório específico para clientes com operações CRUD
- **VeiculoRepository**: Repositório específico para veículos (em desenvolvimento)
- **AluguelRepository**: Repositório específico para aluguéis (em desenvolvimento)

### Services (Regras de Negócio)
- **ClienteService**: Serviços para gerenciamento de clientes com validações
- **VeiculoService**: Serviços para gerenciamento de veículos (em desenvolvimento)
- **AluguelService**: Serviços para gerenciamento de aluguéis (em desenvolvimento)

### Views (Interface do Usuário)
- **MenuPrincipal**: Interface de console com menus interativos para gerenciamento

## ⚙️ Funcionalidades Implementadas

### ✅ Gerenciamento de Clientes
- ✅ Cadastrar cliente (pessoa física ou jurídica)
- ✅ Listar todos os clientes
- ✅ Buscar cliente por nome (parcial)
- 🔄 Remover cliente (comentado)
- 🔄 Alterar cliente (comentado)

### 🔄 Gerenciamento de Veículos
- 🔄 Cadastrar veículo (em desenvolvimento)
- 🔄 Buscar veículo por placa ou nome (em desenvolvimento)
- 🔄 Listar todos os veículos (em desenvolvimento)
- 🔄 Listar veículos disponíveis (em desenvolvimento)
- 🔄 Remover veículo (em desenvolvimento)

### 🔄 Gerenciamento de Aluguéis
- 🔄 Realizar aluguel (em desenvolvimento)
- 🔄 Finalizar aluguel (em desenvolvimento)
- 🔄 Buscar aluguéis por cliente (em desenvolvimento)
- 🔄 Buscar aluguéis por veículo (em desenvolvimento)
- 🔄 Listar aluguéis ativos (em desenvolvimento)
- 🔄 Listar todos os aluguéis (em desenvolvimento)

## 🎯 Regras de Negócio Implementadas

### Descontos por Tipo de Cliente
- **Pessoa Física**: 5% de desconto para aluguéis com mais de 5 dias
- **Pessoa Jurídica**: 10% de desconto para aluguéis com mais de 3 dias

### Valores de Diária por Tipo de Veículo
- **PEQUENO**: R$ 100,00
- **MÉDIO**: R$ 150,00
- **SUV**: R$ 200,00

## 🚀 Como Executar

1. **Compile o projeto**:
```bash
javac src/Main.java src/database/*.java src/repositories/*.java src/services/*.java src/views/*.java
```

2. **Execute o programa**:
```bash
java -cp src Main
```

## 📋 Exemplo de Uso

Ao executar o programa, você verá um menu interativo:

```
Selecione a opcao: 

1 - Clientes

Deseja continuar 1 - Sim, 0 - Nao
```

### Menu de Clientes
```
CLIENTES
[lista de clientes cadastrados]

1 - Cadastrar
2 - Remover
3 - Alterar
4 - Voltar
```

## 🔧 Funcionalidades Técnicas

### ClienteRepository
- **Armazenamento**: Lista em memória
- **Identificação**: Documento (CPF/CNPJ)
- **Busca**: Por nome parcial (case-insensitive)
- **Operações**: Salvar e listar

### ClienteService
- **Validações**: Nome e documento obrigatórios
- **Regras de Negócio**: Aplicação de descontos por tipo de cliente
- **Operações**: Cadastrar, buscar por nome, listar todos

## 📊 Status do Desenvolvimento

| Módulo | Status | Funcionalidades |
|--------|--------|-----------------|
| Clientes | ✅ Completo | CRUD básico implementado |
| Veículos | 🔄 Em desenvolvimento | Estrutura criada |
| Aluguéis | 🔄 Em desenvolvimento | Estrutura criada |
| Interface | ✅ Funcional | Menu de clientes operacional |

## 🎨 Princípios SOLID Aplicados

- **S**: Single Responsibility Principle - Cada classe tem uma responsabilidade específica
- **O**: Open/Closed Principle - Classes abertas para extensão, fechadas para modificação
- **L**: Liskov Substitution Principle - Subclasses podem substituir suas classes base
- **I**: Interface Segregation Principle - Interfaces específicas para cada necessidade
- **D**: Dependency Inversion Principle - Dependências de abstrações, não de implementações

## 🛠️ Tecnologias Utilizadas

- **Java 11+**
- **Console I/O** para interface do usuário
- **Collections Framework** para gerenciamento de dados
- **Stream API** para operações de filtragem e busca
- **Date/Time API** (planejado para aluguéis)

## 🔄 Próximos Passos

1. **Completar módulo de Veículos**
2. **Implementar módulo de Aluguéis**
3. **Adicionar validações de CPF/CNPJ**
4. **Implementar persistência em arquivo**
5. **Adicionar relatórios e estatísticas**

## 👥 Desenvolvimento

Este projeto está em desenvolvimento ativo, com foco na implementação completa das funcionalidades de locação de veículos seguindo boas práticas de programação orientada a objetos.
