package views;

import services.ClienteService;

import java.awt.*;
import java.util.Scanner;

public class MenuPrincipal{
    private ClienteService clienteService;
    //private VeiculoService veiculoService;
    //private AluguelService aluguelService;


    public MenuPrincipal(ClienteService clienteService) {
        this.clienteService = clienteService;
    }

    public void start(){

        int resp = 0;

        do {

            Scanner input = new Scanner(System.in);
            System.out.println("""
                    
                    Selecione a opcao: 
                    
                    1 - Clientes
                    
                    """);

            int opcao = input.nextInt();

            switch (opcao) {
                case 1:
                    new MenuCliente(this.clienteService).init();


            }

            System.out.println("Deseja continuar 1 - Sim, 0 - Nao");
            resp = input.nextInt();

        } while(resp != 0);

    }

}

class MenuCliente {

    private ClienteService clienteService;

    public MenuCliente(ClienteService clienteService) {
        this.clienteService = clienteService;
    }

    public void init() {


        int resp = 0;

        do {

            Scanner input = new Scanner(System.in);
            System.out.println(" CLIENTES ");
            this.listar();
            System.out.println("""
                
                1 - Cadastrar
                2 - Remover
                3 - Alterar
                4 - Voltar
                
                """);

            int opcao = input.nextInt();

            switch (opcao) {
                case 1:
                    new MenuCliente(this.clienteService).init();


            }

            System.out.println("Deseja continuar 1 - Sim, 0 - Nao");
            resp = input.nextInt();

        } while(resp != 0);




    }


    public void listar() {
        clienteService.listarTodos().forEach(cliente -> {
            System.out.println(cliente.getNome());
            System.out.println(cliente.getDocumento());

        } );
    }

}


