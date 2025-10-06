package functional;

import java.util.function.Function;
import java.util.function.Predicate;
import model.Cliente;
import model.PessoaFisica;
import model.PessoaJuridica;

/**
 * Interface funcional para estratégias de tipo de cliente
 * Aplica o padrão Strategy usando interfaces funcionais
 */
@FunctionalInterface
public interface TipoClienteStrategy extends Function<Cliente, String> {
    
    // Estratégias pré-definidas usando Function
    TipoClienteStrategy TIPO_ABREVIADO = cliente -> 
        cliente instanceof PessoaFisica ? "PF" : "PJ";
    
    TipoClienteStrategy TIPO_COMPLETO = cliente -> 
        cliente instanceof PessoaFisica ? "Pessoa Física" : "Pessoa Jurídica";
    
    TipoClienteStrategy TIPO_COM_DOCUMENTO = cliente -> {
        if (cliente instanceof PessoaFisica) {
            return "PF - CPF: " + cliente.getDocumento();
        } else {
            return "PJ - CNPJ: " + cliente.getDocumento();
        }
    };
    
    // Predicates para filtros
    Predicate<Cliente> EH_PESSOA_FISICA = cliente -> cliente instanceof PessoaFisica;
    Predicate<Cliente> EH_PESSOA_JURIDICA = cliente -> cliente instanceof PessoaJuridica;
    
    // Método default para compor com outras estratégias
    default TipoClienteStrategy comPrefixo(String prefixo) {
        return cliente -> prefixo + this.apply(cliente);
    }
}