package utils;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Random;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import functional.GeradorDados;
import model.*;

/**
 * Classe para geração de dados de teste usando Supplier e interfaces funcionais.
 * Implementa Suppliers para criar clientes, veículos e aluguéis fictícios.
 */
public class GeradorDadosTeste {
    private static final Random random = new Random();

    // Arrays de dados para gerar nomes e modelos aleatórios
    private static final String[] NOMES_PF = {
        "Ana Silva", "João Santos", "Maria Oliveira", "Carlos Ferreira",
        "Fernanda Costa", "Roberto Lima", "Juliana Souza", "Pedro Alves",
        "Camila Rodrigues", "Lucas Martins", "Patricia Barbosa", "Rafael Dias",
        "Beatriz Cardoso", "Gustavo Pereira", "Larissa Fernandes"
    };

    private static final String[] NOMES_PJ = {
        "Tech Solutions Ltda", "Empresa ABC Ltda", "Inovação Digital Ltda",
        "Logística Express S.A.", "Consultoria Prime Ltda", "Desenvolvimento Web Inc",
        "Sistemas Integrados SA", "Marketing Digital Ltda", "Construção Forte Ltda",
        "Comércio Global S.A.", "Serviços Premium Ltda", "Indústria Nacional SA"
    };

    private static final String[] MODELOS_PEQUENO = {
        "Gol 1.0", "Onix 1.4", "Palio 1.0", "Ka 1.0", "Up 1.0",
        "Kwid 1.0", "Mobi 1.0", "Celta 1.0", "Sandero 1.0"
    };

    private static final String[] MODELOS_MEDIO = {
        "Civic 2.0", "Corolla 2.0", "Jetta 2.0", "Sentra 2.0",
        "Cruze 1.4", "Focus 2.0", "Virtus 1.6", "Prisma 1.4"
    };

    private static final String[] MODELOS_SUV = {
        "Hilux 2.8", "Amarok 2.0", "Ranger 3.2", "S10 2.8",
        "Frontier 2.5", "Trailblazer 2.8", "SW4 2.8", "Tiguan 2.0"
    };

    private static final String[] LOCAIS = {
        "Filial Centro", "Filial Shopping", "Filial Norte", "Filial Sul",
        "Filial Leste", "Filial Oeste", "Aeroporto Internacional"
    };

    // Suppliers usando interface funcional GeradorDados

    /**
     * Supplier para gerar CPF válido (apenas formato, não validação de dígitos).
     */
    public static final GeradorDados<String> GERADOR_CPF = () -> {
        return String.format("%011d", random.nextLong(100000000000L));
    };

    /**
     * Supplier para gerar CNPJ válido (apenas formato, não validação de dígitos).
     */
    public static final GeradorDados<String> GERADOR_CNPJ = () -> {
        return String.format("%014d", random.nextLong(100000000000000L));
    };

    /**
     * Supplier para gerar placa de veículo (formato antigo brasileiro).
     */
    public static final GeradorDados<String> GERADOR_PLACA = () -> {
        char letra1 = (char) ('A' + random.nextInt(26));
        char letra2 = (char) ('A' + random.nextInt(26));
        char letra3 = (char) ('A' + random.nextInt(26));
        int numero = random.nextInt(10000);
        return String.format("%c%c%c-%04d", letra1, letra2, letra3, numero);
    };

    /**
     * Supplier para gerar Pessoa Física.
     */
    public static final GeradorDados<PessoaFisica> GERADOR_PESSOA_FISICA = () -> {
        String nome = NOMES_PF[random.nextInt(NOMES_PF.length)];
        String cpf = GERADOR_CPF.get();
        return new PessoaFisica(cpf, nome);
    };

    /**
     * Supplier para gerar Pessoa Jurídica.
     */
    public static final GeradorDados<PessoaJuridica> GERADOR_PESSOA_JURIDICA = () -> {
        String nome = NOMES_PJ[random.nextInt(NOMES_PJ.length)];
        String cnpj = GERADOR_CNPJ.get();
        return new PessoaJuridica(cnpj, nome);
    };

    /**
     * Supplier para gerar Cliente (pode ser PF ou PJ).
     */
    public static final GeradorDados<Cliente> GERADOR_CLIENTE = () -> {
        return random.nextBoolean() ? GERADOR_PESSOA_FISICA.get() : GERADOR_PESSOA_JURIDICA.get();
    };

    /**
     * Supplier para gerar Veículo PEQUENO.
     */
    public static final GeradorDados<Veiculo> GERADOR_VEICULO_PEQUENO = () -> {
        String placa = GERADOR_PLACA.get();
        String modelo = MODELOS_PEQUENO[random.nextInt(MODELOS_PEQUENO.length)];
        return new Veiculo(placa, modelo, TipoVeiculo.PEQUENO);
    };

    /**
     * Supplier para gerar Veículo MÉDIO.
     */
    public static final GeradorDados<Veiculo> GERADOR_VEICULO_MEDIO = () -> {
        String placa = GERADOR_PLACA.get();
        String modelo = MODELOS_MEDIO[random.nextInt(MODELOS_MEDIO.length)];
        return new Veiculo(placa, modelo, TipoVeiculo.MEDIO);
    };

    /**
     * Supplier para gerar Veículo SUV.
     */
    public static final GeradorDados<Veiculo> GERADOR_VEICULO_SUV = () -> {
        String placa = GERADOR_PLACA.get();
        String modelo = MODELOS_SUV[random.nextInt(MODELOS_SUV.length)];
        return new Veiculo(placa, modelo, TipoVeiculo.SUV);
    };

    /**
     * Supplier para gerar Veículo de qualquer tipo.
     */
    public static final GeradorDados<Veiculo> GERADOR_VEICULO = () -> {
        int tipo = random.nextInt(3);
        return switch (tipo) {
            case 0 -> GERADOR_VEICULO_PEQUENO.get();
            case 1 -> GERADOR_VEICULO_MEDIO.get();
            default -> GERADOR_VEICULO_SUV.get();
        };
    };

    /**
     * Supplier para gerar data/hora aleatória nos últimos 30 dias.
     */
    public static final GeradorDados<LocalDateTime> GERADOR_DATA_RECENTE = () -> {
        int diasAtras = random.nextInt(30);
        int horas = random.nextInt(24);
        int minutos = random.nextInt(60);
        return LocalDateTime.now().minusDays(diasAtras).withHour(horas).withMinute(minutos);
    };

    /**
     * Supplier para gerar local de aluguel.
     */
    public static final GeradorDados<String> GERADOR_LOCAL = () -> {
        return LOCAIS[random.nextInt(LOCAIS.length)];
    };

    // Métodos utilitários para gerar listas usando Streams

    /**
     * Gera uma lista de clientes usando Supplier e Streams.
     *
     * @param quantidade número de clientes a gerar
     * @return lista de clientes gerados
     */
    public static List<Cliente> gerarClientes(int quantidade) {
        return IntStream.range(0, quantidade)
                .mapToObj(i -> GERADOR_CLIENTE.get())
                .collect(Collectors.toList());
    }

    /**
     * Gera uma lista de Pessoas Físicas usando Supplier e Streams.
     */
    public static List<PessoaFisica> gerarPessoasFisicas(int quantidade) {
        return IntStream.range(0, quantidade)
                .mapToObj(i -> GERADOR_PESSOA_FISICA.get())
                .collect(Collectors.toList());
    }

    /**
     * Gera uma lista de Pessoas Jurídicas usando Supplier e Streams.
     */
    public static List<PessoaJuridica> gerarPessoasJuridicas(int quantidade) {
        return IntStream.range(0, quantidade)
                .mapToObj(i -> GERADOR_PESSOA_JURIDICA.get())
                .collect(Collectors.toList());
    }

    /**
     * Gera uma lista de veículos usando Supplier e Streams.
     *
     * @param quantidade número de veículos a gerar
     * @return lista de veículos gerados
     */
    public static List<Veiculo> gerarVeiculos(int quantidade) {
        return IntStream.range(0, quantidade)
                .mapToObj(i -> GERADOR_VEICULO.get())
                .collect(Collectors.toList());
    }

    /**
     * Gera uma lista de veículos por tipo usando Supplier e Streams.
     */
    public static List<Veiculo> gerarVeiculosPorTipo(TipoVeiculo tipo, int quantidade) {
        GeradorDados<Veiculo> gerador = switch (tipo) {
            case PEQUENO -> GERADOR_VEICULO_PEQUENO;
            case MEDIO -> GERADOR_VEICULO_MEDIO;
            case SUV -> GERADOR_VEICULO_SUV;
        };

        return IntStream.range(0, quantidade)
                .mapToObj(i -> gerador.get())
                .collect(Collectors.toList());
    }

    /**
     * Gera lista balanceada de veículos (igual quantidade de cada tipo).
     */
    public static List<Veiculo> gerarVeiculosBalanceados(int quantidadePorTipo) {
        return IntStream.range(0, quantidadePorTipo * 3)
                .mapToObj(i -> {
                    int tipo = i % 3;
                    return switch (tipo) {
                        case 0 -> GERADOR_VEICULO_PEQUENO.get();
                        case 1 -> GERADOR_VEICULO_MEDIO.get();
                        default -> GERADOR_VEICULO_SUV.get();
                    };
                })
                .collect(Collectors.toList());
    }

    /**
     * Gera lista balanceada de clientes (igual quantidade PF e PJ).
     */
    public static List<Cliente> gerarClientesBalanceados(int quantidadePorTipo) {
        List<Cliente> clientes = IntStream.range(0, quantidadePorTipo)
                .mapToObj(i -> (Cliente) GERADOR_PESSOA_FISICA.get())
                .collect(Collectors.toList());

        clientes.addAll(
                IntStream.range(0, quantidadePorTipo)
                        .mapToObj(i -> (Cliente) GERADOR_PESSOA_JURIDICA.get())
                        .collect(Collectors.toList())
        );

        return clientes;
    }

    /**
     * Gera CPFs únicos em uma lista.
     */
    public static List<String> gerarCPFsUnicos(int quantidade) {
        return IntStream.range(0, quantidade)
                .mapToObj(i -> GERADOR_CPF.get())
                .distinct() // Garante unicidade
                .limit(quantidade)
                .collect(Collectors.toList());
    }

    /**
     * Gera CNPJs únicos em uma lista.
     */
    public static List<String> gerarCNPJsUnicos(int quantidade) {
        return IntStream.range(0, quantidade)
                .mapToObj(i -> GERADOR_CNPJ.get())
                .distinct() // Garante unicidade
                .limit(quantidade)
                .collect(Collectors.toList());
    }

    /**
     * Gera placas únicas em uma lista.
     */
    public static List<String> gerarPlacasUnicas(int quantidade) {
        return IntStream.range(0, quantidade * 2) // Gera mais para compensar duplicatas
                .mapToObj(i -> GERADOR_PLACA.get())
                .distinct() // Garante unicidade
                .limit(quantidade)
                .collect(Collectors.toList());
    }
}
