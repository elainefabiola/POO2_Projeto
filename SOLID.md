## 🎨 Princípios SOLID Aplicados

O projeto implementa rigorosamente os cinco princípios SOLID, demonstrando boas práticas de programação orientada a objetos:

### 🔹 **S - Single Responsibility Principle (SRP)**

Cada classe possui uma única responsabilidade bem definida:

**Exemplos práticos:**
- **`ClienteService`**: Apenas validações e regras de negócio para clientes
- **`VeiculoRepository`**: Exclusivamente operações de persistência de veículos
- **`Aluguel`**: Cálculo de valores e controle de estado do aluguel
- **`TipoVeiculo`**: Enum responsável apenas por valores e cálculos de diária

```java
// ClienteService - Responsabilidade única: validações de cliente
public class ClienteService {
    private final ClienteRepository repository;
    
    public void cadastrarCliente(Cliente cliente) {
        // Apenas validações e regras de negócio
        if (cliente == null) {
            throw new IllegalArgumentException("Cliente não pode ser nulo");
        }
        // ... outras validações
        repository.salvar(cliente);
    }
}
```

### 🔹 **O - Open/Closed Principle (OCP)**

O sistema é aberto para extensão, fechado para modificação:

**Exemplo principal:**
- **`Cliente`** (classe abstrata) permite extensão via herança
- **`PessoaFisica`** e **`PessoaJuridica`** estendem sem modificar a base
- Novos tipos de cliente podem ser adicionados sem alterar código existente

```java
// Cliente abstrato - aberto para extensão
public abstract class Cliente {
    protected String documento;
    protected String nome;
    
    // Método abstrato - cada subclasse implementa sua regra
    public abstract double calcularDesconto(int dias);
}

// PessoaFisica - extensão sem modificação da base
public class PessoaFisica extends Cliente {
    @Override
    public double calcularDesconto(int dias) {
        return dias > 5 ? 0.05 : 0.0; // 5% para >5 dias
    }
}

// PessoaJuridica - outra extensão
public class PessoaJuridica extends Cliente {
    @Override
    public double calcularDesconto(int dias) {
        return dias > 3 ? 0.10 : 0.0; // 10% para >3 dias
    }
}
```

### 🔹 **L - Liskov Substitution Principle (LSP)**

Subclasses podem substituir suas classes base sem quebrar funcionalidade:

**Demonstração:**
- **`PessoaFisica`** e **`PessoaJuridica`** substituem **`Cliente`** perfeitamente
- Todas mantêm o contrato da classe base
- Comportamento polimórfico funciona corretamente

```java
// No Aluguel.java - uso polimórfico
private BigDecimal calcularValorTotal() {
    int dias = calcularDias();
    double valorBruto = veiculo.getTipo().calcularAluguel(dias);
    // Cliente pode ser PessoaFisica ou PessoaJuridica - ambas funcionam
    double desconto = cliente.calcularDesconto(dias);
    double valorComDesconto = valorBruto * (1 - desconto);
    return BigDecimal.valueOf(valorComDesconto);
}
```

### 🔹 **I - Interface Segregation Principle (ISP)**

Repositórios específicos evitam dependências desnecessárias:

**Implementação:**
- **`ClienteRepository`**: Operações específicas para clientes
- **`VeiculoRepository`**: Operações específicas para veículos  
- **`AluguelRepository`**: Operações específicas para aluguéis
- Cada service depende apenas do repositório que precisa

```java
// AluguelService usa apenas os repositórios necessários
public class AluguelService {
    private final AluguelRepository aluguelRepository;
    private final ClienteRepository clienteRepository;  // Só para consultas
    private final VeiculoRepository veiculoRepository;  // Só para consultas
    
    // Não depende de operações desnecessárias de outros domínios
}
```

### 🔹 **D - Dependency Inversion Principle (DIP)**

Services dependem de abstrações (repositórios), não de implementações concretas:

**Arquitetura aplicada:**
- **Services** dependem de **Repositories** (abstrações)
- **Main** injeta dependências manualmente
- Baixo acoplamento entre camadas

```java
// Main.java - Injeção de dependência manual
public static void main(String[] args) {
    // Criação das abstrações
    ClienteRepository clienteRepo = new ClienteRepository(clientes);
    VeiculoRepository veiculoRepo = new VeiculoRepository(veiculos);
    
    // Services dependem das abstrações, não das implementações
    ClienteService clienteService = new ClienteService(clienteRepo);
    VeiculoService veiculoService = new VeiculoService(veiculoRepo);
    
    // Fácil substituição: trocar implementação sem afetar service
}
```
