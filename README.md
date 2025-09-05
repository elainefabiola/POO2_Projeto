# 🚗 Sistema ADA LocateCar

Sistema completo de gerenciamento de locação de veículos desenvolvido em Java, seguindo os princípios SOLID e arquitetura em camadas. Implementa todas as funcionalidades principais de uma locadora: gestão de clientes, veículos e aluguéis com interface de console intuitiva.

## 📁 Estrutura do Projeto

O projeto está organizado em camadas bem definidas:

```
src/
├── Main.java                     # Ponto de entrada - inicialização completa do sistema
├── database/                     # 🏗️ Camada de Domínio (Entidades)
│   ├── Cliente.java              # Classe abstrata base para clientes
│   ├── PessoaFisica.java         # Cliente pessoa física (CPF) - desconto 5% >5 dias
│   ├── PessoaJuridica.java       # Cliente pessoa jurídica (CNPJ) - desconto 10% >3 dias
│   ├── Veiculo.java              # Entidade veículo com controle de disponibilidade
│   ├── TipoVeiculo.java          # Enum: PEQUENO(R$100), MEDIO(R$150), SUV(R$200)
│   └── Aluguel.java              # Entidade aluguel com cálculo automático de valor
├── repositories/                 # 💾 Camada de Persistência (Repositórios)
│   ├── ClienteRepository.java    # Repositório de clientes (busca, CRUD)
│   ├── VeiculoRepository.java    # Repositório de veículos (disponibilidade)
│   ├── AluguelRepository.java    # Repositório de aluguéis (ativos/histórico)
│   └── RepositorioMemoria.java   # Base genérica (não utilizada)
├── services/                     # 🧠 Camada de Negócio (Serviços)
│   ├── ClienteService.java       # Validações e regras de cliente
│   ├── VeiculoService.java       # Validações e regras de veículo
│   └── AluguelService.java       # Regras de aluguel e devolução
└── views/                        # 🖥️ Camada de Apresentação (Interface)
    └── MenuPrincipal.java        # Interface completa: 4 menus integrados
```

## 🏗️ Arquitetura em Camadas

### 🏛️ Camada de Domínio (`database/`)
**Entidades de negócio com regras e validações:**

- **`Cliente`** (abstrata): Base para clientes com documento e nome
- **`PessoaFisica`**: CPF + regra de desconto 5% para >5 diárias  
- **`PessoaJuridica`**: CNPJ + regra de desconto 10% para >3 diárias
- **`Veiculo`**: Placa única, nome, tipo, controle de disponibilidade automático
- **`TipoVeiculo`** (enum): PEQUENO(R$100), MEDIO(R$150), SUV(R$200)
- **`Aluguel`**: ID único, cálculo automático de valor com descontos

### 💾 Camada de Repositório (`repositories/`)
**Persistência em memória com operações especializadas:**

- **`ClienteRepository`**: CRUD + busca por documento + busca por nome parcial
- **`VeiculoRepository`**: CRUD + busca por placa + filtro por disponibilidade
- **`AluguelRepository`**: CRUD + filtros por status/cliente/veículo

### 🧠 Camada de Serviço (`services/`)
**Regras de negócio e validações:**

- **`ClienteService`**: Validações de cadastro, unicidade de documento
- **`VeiculoService`**: Validações de placa única, controle de disponibilidade  
- **`AluguelService`**: Orchestração completa aluguel/devolução, cálculos

### 🖥️ Camada de Apresentação (`views/`)
**Interface de console completa e intuitiva:**

- **`MenuPrincipal`**: Hub central com 4 módulos integrados
- **`MenuCliente`**: Cadastro PF/PJ, listagem, busca por nome
- **`MenuVeiculo`**: Cadastro por tipo, listagem geral/disponíveis, busca
- **`MenuAluguel`**: Processo completo aluguel/devolução, relatórios

## ✅ Funcionalidades Implementadas

### 👥 **Gestão Completa de Clientes**
- ✅ **Cadastro PF/PJ**: Pessoa Física (CPF) e Jurídica (CNPJ) com validações
- ✅ **Listagem completa**: Exibição com identificação de tipo [PF]/[PJ]
- ✅ **Busca por nome**: Busca parcial case-insensitive
- ✅ **Validações**: Documento único, campos obrigatórios

### 🚗 **Gestão Completa de Veículos**
- ✅ **Cadastro por tipo**: PEQUENO, MEDIO, SUV com valores automáticos
- ✅ **Controle de disponibilidade**: Automático durante aluguel/devolução
- ✅ **Listagem completa**: Todos os veículos com status
- ✅ **Listagem disponíveis**: Apenas veículos disponíveis para locação
- ✅ **Busca por nome**: Busca parcial por modelo/marca
- ✅ **Validações**: Placa única, campos obrigatórios

### 💰 **Gestão Completa de Aluguéis** 
- ✅ **Processo de aluguel**: Vinculação cliente-veículo com data/local
- ✅ **Devolução avançada**: Input opcional de data/hora com validações
- ✅ **Cálculo automático**: Diárias + descontos por tipo de cliente
- ✅ **Relatórios**: Aluguéis ativos e histórico completo
- ✅ **Validações temporais**: Data não futura, não anterior à retirada
- ✅ **Controle de status**: Aluguéis ativos vs finalizados

## 💎 Regras de Negócio Implementadas

### 💸 **Sistema de Descontos Automático**
- **Pessoa Física**: 5% de desconto para aluguéis > 5 diárias
- **Pessoa Jurídica**: 10% de desconto para aluguéis > 3 diárias
- **Cálculo automático**: Aplicado na devolução com base no período real

### 🏷️ **Valores de Diária por Categoria**
- **PEQUENO**: R$ 100,00/dia (ex: Gol, Uno)
- **MEDIO**: R$ 150,00/dia (ex: Civic, Corolla) 
- **SUV**: R$ 200,00/dia (ex: Hilux, Tucson)

### ⏱️ **Cálculo de Diárias**
- **Regra**: Qualquer fração de hora = 1 diária completa
- **Exemplo**: Retirada 15h30 dia 25, devolução até 15h30 dia 26 = 1 diária
- **Automático**: Baseado em `LocalDateTime` preciso

### 🔐 **Validações de Integridade**
- **Documento único**: CPF/CNPJ não podem ser duplicados
- **Placa única**: Cada veículo tem identificação exclusiva  
- **Disponibilidade**: Veículo só pode ter 1 aluguel ativo
- **Consistência temporal**: Devolução sempre >= retirada

## 🚀 Como Executar

### ⚡ **Execução Rápida**

1. **Compile todos os arquivos**:
```bash
find src -name "*.java" -exec javac -d . {} +
```

2. **Execute o programa**:
```bash
java Main
```

### 🔧 **Execução Passo-a-Passo**

1. **Compilar manualmente**:
```bash
javac -d . src/database/*.java
javac -d . src/repositories/*.java  
javac -d . src/services/*.java
javac -d . src/views/*.java
javac -d . src/Main.java
```

2. **Executar**:
```bash
java Main
```

### 📋 **Pré-requisitos**
- **Java OpenJDK 21** (necessário para compatibilidade)
- **Terminal/Console** para interação
- **Sistema operacional**: Windows, macOS, Linux

## 🎮 Demonstração do Sistema

### 🏠 **Menu Principal**
```
==================================================
         ADA LOCATECAR - MENU PRINCIPAL
==================================================
1 - Gestão de Clientes
2 - Gestão de Veículos  
3 - Gestão de Aluguéis
0 - Sair
==================================================
Escolha uma opção: 
```

### 👥 **Menu de Clientes**
```
=== GESTÃO DE CLIENTES ===
1 - Cadastrar Cliente
2 - Listar Clientes
3 - Buscar por Nome
0 - Voltar
```

**Exemplo de cadastro:**
```
=== CADASTRAR CLIENTE ===
1 - Pessoa Física
2 - Pessoa Jurídica
Tipo: 1
Nome: João Silva
CPF: 12345678900
Cliente pessoa física cadastrado com sucesso!
```

### 🚗 **Menu de Veículos**
```
=== GESTÃO DE VEÍCULOS ===
1 - Cadastrar Veículo
2 - Listar Todos
3 - Listar Disponíveis
4 - Buscar por Nome
0 - Voltar
```

**Exemplo de listagem:**
```
=== LISTA DE VEÍCULOS ===
1. ABC-1234 - Gol 1.0 [PEQUENO] - R$ 100,00/dia - DISPONÍVEL
2. DEF-5678 - Civic 2.0 [MEDIO] - R$ 150,00/dia - ALUGADO
3. GHI-9012 - Hilux 2.8 [SUV] - R$ 200,00/dia - DISPONÍVEL
```

### 💰 **Menu de Aluguéis**
```
=== GESTÃO DE ALUGUÉIS ===
1 - Alugar Veículo
2 - Devolver Veículo
3 - Listar Aluguéis Ativos
4 - Listar Todos os Aluguéis
0 - Voltar
```

**Exemplo de devolução com data opcional:**
```
--- DATA/HORA DE DEVOLUÇÃO ---
Formato: dd/MM/yyyy HH:mm (exemplo: 25/12/2024 14:30)
Data de retirada: 05/09/2025 10:15
A devolução deve ser posterior à retirada.
Digite a data/hora de devolução ou ENTER para usar agora: 05/09/2025 18:30
✅ Data/hora aceita: 05/09/2025 18:30

=== DEVOLUÇÃO REALIZADA COM SUCESSO ===
Cliente: João Silva
Veículo: ABC-1234 - Gol 1.0
Retirada: 05/09/2025 10:15
Devolução: 05/09/2025 18:30
Local Devolução: Matriz Centro
VALOR TOTAL: R$ 100,00
```

## 🔧 Destaques Técnicos

### 🌟 **Funcionalidade Destaque: Entrada Opcional de Data/Hora**
```java
// Método lerDataHoraDevolucao() com validações robustas:
private LocalDateTime lerDataHoraDevolucao(LocalDateTime dataRetirada) {
    // ✅ Formato obrigatório: dd/MM/yyyy HH:mm  
    // ✅ Input opcional: ENTER = agora
    // ✅ Validação: não futura, não anterior à retirada
    // ✅ Tratamento de erro com retry
    // ✅ Exemplos claros para o usuário
}
```

### 💾 **Repositórios com Operações Especializadas**
- **`ClienteRepository`**: Busca por documento único + nome parcial
- **`VeiculoRepository`**: Filtro por disponibilidade + busca por nome
- **`AluguelRepository`**: Filtros por status/cliente/veículo + consultas especializadas

### 🧠 **Serviços com Validações Inteligentes**  
- **Unicidade**: Documentos e placas únicos no sistema
- **Integridade**: Veículo disponível antes do aluguel
- **Temporal**: Validações de data com `LocalDateTime`
- **Negócio**: Cálculo automático de descontos por tipo

### 🎯 **Uso de Padrões e Boas Práticas**
- **Optional**: Para métodos que podem não retornar resultado
- **Stream API**: Filtros e transformações funcionais  
- **Enum com comportamento**: `TipoVeiculo` com valores e cálculos
- **Tratamento de exceções**: Try-catch com mensagens informativas
- **Separação de responsabilidades**: Cada camada com propósito específico

## 📊 Status Atual do Projeto

| 🏗️ **Módulo** | 📈 **Status** | 🔧 **Funcionalidades** |
|---------------|---------------|-------------------------|
| **Clientes** | ✅ **100% Completo** | Cadastro PF/PJ, Listagem, Busca, Validações |
| **Veículos** | ✅ **100% Completo** | Cadastro por tipo, Controle disponibilidade, Busca |  
| **Aluguéis** | ✅ **100% Completo** | Aluguel, Devolução avançada, Cálculos, Relatórios |
| **Interface** | ✅ **100% Completa** | 4 menus integrados, Validações, UX intuitiva |
| **Arquitetura** | ✅ **Finalizada** | 4 camadas, Padrões SOLID, Injeção dependência |

## 🎨 Princípios SOLID Aplicados

- **S**: Single Responsibility - Cada classe tem responsabilidade única e bem definida
- **O**: Open/Closed - Extensível via herança (`Cliente` → `PessoaFisica`/`PessoaJuridica`)
- **L**: Liskov Substitution - Subclasses substituem perfeitamente as abstratas
- **I**: Interface Segregation - Repositórios específicos por domínio
- **D**: Dependency Inversion - Services dependem de abstrações (repositórios)

## 🛠️ Tecnologias e APIs Utilizadas

- **Java OpenJDK 21** com recursos modernos
- **LocalDateTime API** para manipulação precisa de data/hora
- **Stream API** para operações funcionais e filtros
- **Optional** para tratamento seguro de valores ausentes
- **BigDecimal** para cálculos monetários precisos
- **Collections Framework** para estruturas de dados eficientes
- **Scanner** para interface de console interativa

## ✨ Diferenciais do Projeto

### 🚀 **Funcionalidades Avançadas**
- **Input opcional de data/hora** com validações robustas
- **Cálculo automático** de diárias com frações de tempo  
- **Sistema de descontos** diferenciado por tipo de cliente
- **Controle de disponibilidade** automático de veículos

### 🏗️ **Arquitetura Robusta**
- **4 camadas bem definidas** com responsabilidades claras
- **Injeção de dependência** manual com baixo acoplamento
- **Tratamento de exceções** com mensagens user-friendly
- **Validações em múltiplas camadas** para integridade dos dados

### 🎯 **Experiência do Usuário**
- **Interface intuitiva** com navegação clara entre módulos
- **Mensagens informativas** com exemplos práticos
- **Dados pré-carregados** para facilitar testes
- **Validações com retry** em caso de erro de entrada

## 🎓 Objetivo Acadêmico

Projeto desenvolvido para a disciplina **POO2** demonstrando:
- ✅ **Programação Orientada a Objetos** avançada
- ✅ **Padrões de projeto** (Repository, Service Layer)
- ✅ **Arquitetura em camadas** bem estruturada
- ✅ **Tratamento de exceções** e validações
- ✅ **Interface de usuário** funcional e intuitiva
- ✅ **Documentação técnica** completa (UML + README)
