package services;

import java.io.BufferedWriter;
import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import functional.FormatadorRelatorio;
import model.Aluguel;
import model.Cliente;
import model.TipoVeiculo;
import model.Veiculo;

/**
 * Service para geração de relatórios utilizando Files, InputStream e OutputStream.
 * Implementa relatórios usando Streams e arquivos conforme especificado no Refact.md:
 * - Faturamento total por período
 * - Veículos mais alugados
 * - Clientes que mais alugaram
 * - Recibos de aluguel e devolução
 */
public class RelatorioService {
    private final AluguelService aluguelService;
    private final ClienteService clienteService;
    private final VeiculoService veiculoService;

    private static final DateTimeFormatter FORMATO_DATA = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
    private static final String DIRETORIO_RELATORIOS = "relatorios";

    // Formatadores usando interface funcional personalizada
    private static final FormatadorRelatorio<Aluguel> FORMATADOR_ALUGUEL = aluguel ->
            String.format("ID: %s | Cliente: %s | Veículo: %s - %s | Retirada: %s | Status: %s",
                    aluguel.getId().substring(0, 8),
                    aluguel.getCliente().getNome(),
                    aluguel.getVeiculo().getPlaca(),
                    aluguel.getVeiculo().getNome(),
                    aluguel.getDataHoraRetirada().format(FORMATO_DATA),
                    aluguel.isAtivo() ? "Ativo" : "Finalizado");

    public RelatorioService(AluguelService aluguelService,
                          ClienteService clienteService,
                          VeiculoService veiculoService) {
        this.aluguelService = aluguelService;
        this.clienteService = clienteService;
        this.veiculoService = veiculoService;
        criarDiretorioRelatorios();
    }

    private void criarDiretorioRelatorios() {
        try {
            Path path = Paths.get(DIRETORIO_RELATORIOS);
            if (!Files.exists(path)) {
                Files.createDirectory(path);
            }
        } catch (IOException e) {
            System.err.println("Erro ao criar diretório de relatórios: " + e.getMessage());
        }
    }

    /**
     * Gera relatório de faturamento total por período.
     * Usa Streams para filtrar e calcular, e Files para escrever.
     */
    public void gerarRelatorioFaturamentoPorPeriodo(LocalDateTime inicio, LocalDateTime fim) throws IOException {
        String nomeArquivo = String.format("%s/faturamento_%s_a_%s.txt",
                DIRETORIO_RELATORIOS,
                inicio.format(DateTimeFormatter.ofPattern("yyyy-MM-dd")),
                fim.format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));

        Path path = Paths.get(nomeArquivo);

        // Usando BufferedWriter com Files para escrita eficiente
        try (BufferedWriter writer = Files.newBufferedWriter(path,
                StandardOpenOption.CREATE,
                StandardOpenOption.TRUNCATE_EXISTING)) {

            writer.write("=".repeat(80));
            writer.newLine();
            writer.write("RELATÓRIO DE FATURAMENTO POR PERÍODO");
            writer.newLine();
            writer.write("=".repeat(80));
            writer.newLine();
            writer.write(String.format("Período: %s a %s",
                    inicio.format(FORMATO_DATA),
                    fim.format(FORMATO_DATA)));
            writer.newLine();
            writer.write("-".repeat(80));
            writer.newLine();
            writer.newLine();

            // Buscar aluguéis do período usando Streams
            List<Aluguel> alugueisPeriodo = aluguelService.buscarPorPeriodo(inicio, fim);

            // Calcular faturamento total
            BigDecimal faturamentoTotal = alugueisPeriodo.stream()
                    .map(Aluguel::getValorTotal)
                    .reduce(BigDecimal.ZERO, BigDecimal::add);

            // Agrupar por tipo de veículo
            Map<TipoVeiculo, BigDecimal> faturamentoPorTipo = alugueisPeriodo.stream()
                    .collect(Collectors.groupingBy(
                            a -> a.getVeiculo().getTipo(),
                            Collectors.reducing(
                                    BigDecimal.ZERO,
                                    Aluguel::getValorTotal,
                                    BigDecimal::add)));

            writer.write(String.format("Total de aluguéis no período: %d", alugueisPeriodo.size()));
            writer.newLine();
            writer.write(String.format("Faturamento total: R$ %.2f", faturamentoTotal));
            writer.newLine();
            writer.newLine();

            writer.write("FATURAMENTO POR TIPO DE VEÍCULO:");
            writer.newLine();
            writer.write("-".repeat(80));
            writer.newLine();

            // Escrever faturamento por tipo usando Stream
            faturamentoPorTipo.entrySet().stream()
                    .sorted(Map.Entry.<TipoVeiculo, BigDecimal>comparingByValue().reversed())
                    .forEach(entry -> {
                        try {
                            writer.write(String.format("  %s: R$ %.2f",
                                    entry.getKey(),
                                    entry.getValue()));
                            writer.newLine();
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                    });

            writer.newLine();
            writer.write("=".repeat(80));
            writer.newLine();
            writer.write("Relatório gerado em: " + LocalDateTime.now().format(FORMATO_DATA));
            writer.newLine();
        }

        System.out.println("✅ Relatório de faturamento gerado: " + nomeArquivo);
    }

    /**
     * Gera relatório de veículos mais alugados.
     * Usa Streams para agregação e Files para escrita.
     */
    public void gerarRelatorioVeiculosMaisAlugados() throws IOException {
        String nomeArquivo = String.format("%s/veiculos_mais_alugados.txt", DIRETORIO_RELATORIOS);
        Path path = Paths.get(nomeArquivo);

        try (BufferedWriter writer = Files.newBufferedWriter(path,
                StandardOpenOption.CREATE,
                StandardOpenOption.TRUNCATE_EXISTING)) {

            writer.write("=".repeat(80));
            writer.newLine();
            writer.write("RELATÓRIO DE VEÍCULOS MAIS ALUGADOS");
            writer.newLine();
            writer.write("=".repeat(80));
            writer.newLine();
            writer.newLine();

            // Obter estatísticas usando Streams
            List<Map.Entry<String, Long>> ranking = aluguelService.obterVeiculosMaisAlugados();

            writer.write(String.format("Total de veículos no sistema: %d", veiculoService.listarTodos().size()));
            writer.newLine();
            writer.write(String.format("Veículos que já foram alugados: %d", ranking.size()));
            writer.newLine();
            writer.newLine();

            writer.write("RANKING:");
            writer.newLine();
            writer.write("-".repeat(80));
            writer.newLine();

            // Escrever ranking usando Stream com índice
            ranking.stream()
                    .limit(10) // Top 10
                    .forEach(entry -> {
                        int posicao = ranking.indexOf(entry) + 1;
                        try {
                            writer.write(String.format("%2d. %-50s %3d aluguéis",
                                    posicao,
                                    entry.getKey(),
                                    entry.getValue()));
                            writer.newLine();
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                    });

            writer.newLine();
            writer.write("=".repeat(80));
            writer.newLine();
            writer.write("Relatório gerado em: " + LocalDateTime.now().format(FORMATO_DATA));
            writer.newLine();
        }

        System.out.println("✅ Relatório de veículos mais alugados gerado: " + nomeArquivo);
    }

    /**
     * Gera relatório de clientes que mais alugaram.
     * Usa Streams para agregação e Files para escrita.
     */
    public void gerarRelatorioClientesQueMaisAlugaram() throws IOException {
        String nomeArquivo = String.format("%s/clientes_que_mais_alugaram.txt", DIRETORIO_RELATORIOS);
        Path path = Paths.get(nomeArquivo);

        try (BufferedWriter writer = Files.newBufferedWriter(path,
                StandardOpenOption.CREATE,
                StandardOpenOption.TRUNCATE_EXISTING)) {

            writer.write("=".repeat(80));
            writer.newLine();
            writer.write("RELATÓRIO DE CLIENTES QUE MAIS ALUGARAM");
            writer.newLine();
            writer.write("=".repeat(80));
            writer.newLine();
            writer.newLine();

            // Obter estatísticas usando Streams
            List<Map.Entry<String, Long>> ranking = aluguelService.obterClientesQueMaisAlugaram();

            writer.write(String.format("Total de clientes no sistema: %d", clienteService.listarTodos().size()));
            writer.newLine();
            writer.write(String.format("Clientes que já alugaram: %d", ranking.size()));
            writer.newLine();
            writer.newLine();

            writer.write("RANKING:");
            writer.newLine();
            writer.write("-".repeat(80));
            writer.newLine();

            // Escrever ranking usando Stream
            ranking.stream()
                    .limit(10) // Top 10
                    .forEach(entry -> {
                        int posicao = ranking.indexOf(entry) + 1;
                        try {
                            writer.write(String.format("%2d. %-50s %3d aluguéis",
                                    posicao,
                                    entry.getKey(),
                                    entry.getValue()));
                            writer.newLine();
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                    });

            writer.newLine();
            writer.write("=".repeat(80));
            writer.newLine();
            writer.write("Relatório gerado em: " + LocalDateTime.now().format(FORMATO_DATA));
            writer.newLine();
        }

        System.out.println("✅ Relatório de clientes que mais alugaram gerado: " + nomeArquivo);
    }

    /**
     * Gera recibo de aluguel.
     * Usa Files para escrita de recibo formatado.
     */
    public void gerarReciboAluguel(String aluguelId) throws IOException {
        Aluguel aluguel = aluguelService.listarTodos().stream()
                .filter(a -> a.getId().equals(aluguelId))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Aluguel não encontrado"));

        String nomeArquivo = String.format("%s/recibo_aluguel_%s.txt",
                DIRETORIO_RELATORIOS,
                aluguel.getId().substring(0, 8));

        Path path = Paths.get(nomeArquivo);

        try (BufferedWriter writer = Files.newBufferedWriter(path,
                StandardOpenOption.CREATE,
                StandardOpenOption.TRUNCATE_EXISTING)) {

            writer.write("=".repeat(80));
            writer.newLine();
            writer.write("ADA LOCATECAR - RECIBO DE ALUGUEL");
            writer.newLine();
            writer.write("=".repeat(80));
            writer.newLine();
            writer.newLine();

            writer.write(String.format("ID do Aluguel: %s", aluguel.getId()));
            writer.newLine();
            writer.write(String.format("Data/Hora: %s", aluguel.getDataHoraRetirada().format(FORMATO_DATA)));
            writer.newLine();
            writer.newLine();

            writer.write("DADOS DO CLIENTE:");
            writer.newLine();
            writer.write("-".repeat(80));
            writer.newLine();
            Cliente cliente = aluguel.getCliente();
            writer.write(String.format("Nome: %s", cliente.getNome()));
            writer.newLine();
            writer.write(String.format("Documento: %s", cliente.getDocumento()));
            writer.newLine();
            writer.newLine();

            writer.write("DADOS DO VEÍCULO:");
            writer.newLine();
            writer.write("-".repeat(80));
            writer.newLine();
            Veiculo veiculo = aluguel.getVeiculo();
            writer.write(String.format("Placa: %s", veiculo.getPlaca()));
            writer.newLine();
            writer.write(String.format("Modelo: %s", veiculo.getNome()));
            writer.newLine();
            writer.write(String.format("Categoria: %s", veiculo.getTipo()));
            writer.newLine();
            writer.newLine();

            writer.write("DADOS DO ALUGUEL:");
            writer.newLine();
            writer.write("-".repeat(80));
            writer.newLine();
            writer.write(String.format("Local de Retirada: %s", aluguel.getLocalRetirada()));
            writer.newLine();
            writer.write(String.format("Status: %s", aluguel.isAtivo() ? "ATIVO" : "FINALIZADO"));
            writer.newLine();
            writer.newLine();

            writer.write("=".repeat(80));
            writer.newLine();
            writer.write("Recibo gerado em: " + LocalDateTime.now().format(FORMATO_DATA));
            writer.newLine();
        }

        System.out.println("✅ Recibo de aluguel gerado: " + nomeArquivo);
    }

    /**
     * Gera recibo de devolução.
     * Usa Files para escrita de recibo com cálculo de valores.
     */
    public void gerarReciboDevolucao(String aluguelId) throws IOException {
        Aluguel aluguel = aluguelService.listarTodos().stream()
                .filter(a -> a.getId().equals(aluguelId))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Aluguel não encontrado"));

        if (aluguel.isAtivo()) {
            throw new IllegalArgumentException("Aluguel ainda está ativo. Não é possível gerar recibo de devolução.");
        }

        String nomeArquivo = String.format("%s/recibo_devolucao_%s.txt",
                DIRETORIO_RELATORIOS,
                aluguel.getId().substring(0, 8));

        Path path = Paths.get(nomeArquivo);

        try (BufferedWriter writer = Files.newBufferedWriter(path,
                StandardOpenOption.CREATE,
                StandardOpenOption.TRUNCATE_EXISTING)) {

            writer.write("=".repeat(80));
            writer.newLine();
            writer.write("ADA LOCATECAR - RECIBO DE DEVOLUÇÃO");
            writer.newLine();
            writer.write("=".repeat(80));
            writer.newLine();
            writer.newLine();

            writer.write(String.format("ID do Aluguel: %s", aluguel.getId()));
            writer.newLine();
            writer.newLine();

            writer.write("DADOS DO CLIENTE:");
            writer.newLine();
            writer.write("-".repeat(80));
            writer.newLine();
            Cliente cliente = aluguel.getCliente();
            writer.write(String.format("Nome: %s", cliente.getNome()));
            writer.newLine();
            writer.write(String.format("Documento: %s", cliente.getDocumento()));
            writer.newLine();
            writer.newLine();

            writer.write("DADOS DO VEÍCULO:");
            writer.newLine();
            writer.write("-".repeat(80));
            writer.newLine();
            Veiculo veiculo = aluguel.getVeiculo();
            writer.write(String.format("Placa: %s", veiculo.getPlaca()));
            writer.newLine();
            writer.write(String.format("Modelo: %s", veiculo.getNome()));
            writer.newLine();
            writer.write(String.format("Categoria: %s", veiculo.getTipo()));
            writer.newLine();
            writer.newLine();

            writer.write("DADOS DA LOCAÇÃO:");
            writer.newLine();
            writer.write("-".repeat(80));
            writer.newLine();
            writer.write(String.format("Retirada: %s", aluguel.getDataHoraRetirada().format(FORMATO_DATA)));
            writer.newLine();
            writer.write(String.format("Local de Retirada: %s", aluguel.getLocalRetirada()));
            writer.newLine();
            writer.write(String.format("Devolução: %s", aluguel.getDataHoraDevolucao().format(FORMATO_DATA)));
            writer.newLine();
            writer.write(String.format("Local de Devolução: %s", aluguel.getLocalDevolucao()));
            writer.newLine();
            writer.newLine();

            writer.write("VALORES:");
            writer.newLine();
            writer.write("-".repeat(80));
            writer.newLine();
            writer.write(String.format("Valor Total: R$ %.2f", aluguel.getValorTotal()));
            writer.newLine();
            writer.newLine();

            writer.write("=".repeat(80));
            writer.newLine();
            writer.write("Recibo gerado em: " + LocalDateTime.now().format(FORMATO_DATA));
            writer.newLine();
        }

        System.out.println("✅ Recibo de devolução gerado: " + nomeArquivo);
    }

    /**
     * Gera relatório completo de todos os aluguéis.
     * Usa Streams + Files para processar e escrever dados.
     */
    public void gerarRelatorioCompletodeAlugueis() throws IOException {
        String nomeArquivo = String.format("%s/relatorio_completo_alugueis.txt", DIRETORIO_RELATORIOS);
        Path path = Paths.get(nomeArquivo);

        try (BufferedWriter writer = Files.newBufferedWriter(path,
                StandardOpenOption.CREATE,
                StandardOpenOption.TRUNCATE_EXISTING)) {

            writer.write("=".repeat(80));
            writer.newLine();
            writer.write("RELATÓRIO COMPLETO DE ALUGUÉIS");
            writer.newLine();
            writer.write("=".repeat(80));
            writer.newLine();
            writer.newLine();

            List<Aluguel> todosAlugueis = aluguelService.listarTodos();
            List<Aluguel> ativos = aluguelService.listarAtivos();
            List<Aluguel> finalizados = aluguelService.listarFinalizados();

            writer.write(String.format("Total de aluguéis: %d", todosAlugueis.size()));
            writer.newLine();
            writer.write(String.format("Aluguéis ativos: %d", ativos.size()));
            writer.newLine();
            writer.write(String.format("Aluguéis finalizados: %d", finalizados.size()));
            writer.newLine();
            writer.newLine();

            // Aluguéis ativos
            writer.write("ALUGUÉIS ATIVOS:");
            writer.newLine();
            writer.write("-".repeat(80));
            writer.newLine();

            ativos.stream()
                    .sorted((a1, a2) -> a2.getDataHoraRetirada().compareTo(a1.getDataHoraRetirada()))
                    .forEach(aluguel -> {
                        try {
                            writer.write(FORMATADOR_ALUGUEL.formatar(aluguel));
                            writer.newLine();
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                    });

            writer.newLine();

            // Aluguéis finalizados
            writer.write("ALUGUÉIS FINALIZADOS:");
            writer.newLine();
            writer.write("-".repeat(80));
            writer.newLine();

            finalizados.stream()
                    .sorted((a1, a2) -> a2.getDataHoraDevolucao().compareTo(a1.getDataHoraDevolucao()))
                    .limit(20) // Últimos 20
                    .forEach(aluguel -> {
                        try {
                            writer.write(String.format("%s | Devolução: %s | Valor: R$ %.2f",
                                    FORMATADOR_ALUGUEL.formatar(aluguel),
                                    aluguel.getDataHoraDevolucao().format(FORMATO_DATA),
                                    aluguel.getValorTotal()));
                            writer.newLine();
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                    });

            writer.newLine();
            writer.write("=".repeat(80));
            writer.newLine();
            writer.write("Relatório gerado em: " + LocalDateTime.now().format(FORMATO_DATA));
            writer.newLine();
        }

        System.out.println("✅ Relatório completo de aluguéis gerado: " + nomeArquivo);
    }
}
