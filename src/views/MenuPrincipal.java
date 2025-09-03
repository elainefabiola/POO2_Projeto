package views;

import services.ClienteService;

public class MenuPrincipal{
    private ClienteService clienteService;
    //private VeiculoService veiculoService;
    //private AluguelService aluguelService;


    public MenuPrincipal(ClienteService clienteService) {
        this.clienteService = clienteService;
    }

    public void start(){

        clienteService.listarTodos().forEach(cliente -> {
            System.out.println(cliente.getNome());
            System.out.println(cliente.getDocumento());

        } );

    }
}