package repositories;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import model.Cliente;
import utils.ArquivoUtil;

public class ClienteRepository {

    private List<Cliente> clienteList;
    private static final String ARQUIVO_CLIENTES = "clientes.dat";

    public ClienteRepository(List<Cliente> clienteList) {
        this.clienteList = clienteList;
    }

    public String getIdentificador(Cliente cliente) {
        return cliente.getDocumento();
    }

    public void salvarEmArquivo() {
        ArquivoUtil.salvarLista(ARQUIVO_CLIENTES, clienteList);
    }

    public void carregarDeArquivo() {
        this.clienteList = ArquivoUtil.lerLista(ARQUIVO_CLIENTES);
    }

    public void salvar(Cliente cliente){
        clienteList.add(cliente);
    }

    public List<Cliente> listarTodos() {
        return clienteList;
    }

    /**
     * Lista clientes com paginação usando Stream.skip() e limit().
     *
     * @param pagina número da página (começa em 0)
     * @param tamanhoPagina quantidade de itens por página
     * @return lista de clientes da página solicitada
     */
    public List<Cliente> listarComPaginacao(int pagina, int tamanhoPagina) {
        return clienteList.stream()
                .sorted(Comparator.comparing(Cliente::getNome))
                .skip((long) pagina * tamanhoPagina)
                .limit(tamanhoPagina)
                .collect(Collectors.toList());
    }

    /**
     * Busca clientes com filtro personalizado usando Predicate.
     *
     * @param filtro predicado para filtrar clientes
     * @return lista de clientes que atendem ao filtro
     */
    public List<Cliente> buscarComFiltro(Predicate<Cliente> filtro) {
        return clienteList.stream()
                .filter(filtro)
                .collect(Collectors.toList());
    }

    /**
     * Busca clientes com filtro e paginação.
     */
    public List<Cliente> buscarComFiltroPaginado(Predicate<Cliente> filtro, int pagina, int tamanhoPagina) {
        return clienteList.stream()
                .filter(filtro)
                .sorted(Comparator.comparing(Cliente::getNome))
                .skip((long) pagina * tamanhoPagina)
                .limit(tamanhoPagina)
                .collect(Collectors.toList());
    }

    public List<Cliente> buscarPorNomeParcial(String termo) {
        if (termo == null) termo = "";
        final String t = termo.toLowerCase();
        return clienteList.stream()
                .filter(c -> c.getNome() != null && c.getNome().toLowerCase().contains(t))
                .collect(Collectors.toList());
    }

    public Optional<Cliente> buscarPorDocumento(String documento) {
        return clienteList.stream()
                .filter(c -> c.getDocumento().equals(documento))
                .findFirst();
    }

    /**
     * Conta o total de clientes que atendem a um filtro.
     */
    public long contarComFiltro(Predicate<Cliente> filtro) {
        return clienteList.stream()
                .filter(filtro)
                .count();
    }
}
