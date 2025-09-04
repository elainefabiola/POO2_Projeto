package repositories;

import database.Veiculo;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Repositório específico de Veiculo sobre a infraestrutura genérica em memória.
 * - Identificador: PLACA (String)
 * - Consultas de apoio ao caso de uso: por nome (parcial, case-insensitive) e disponíveis.
 *
 * Requisitos esperados pelo projeto:
 *  - Cadastrar, alterar, buscar por parte do nome
 *  - Alugar/Devolver (usa flag disponivel)
 */
//public class VeiculoRepository  {
//
//    @Override
//    public String getIdentificador(Veiculo veiculo) {
//        return veiculo.getIdentificador(); // placa
//    }
//
//    // Busca por parte do nome (case-insensitive).
//    public List<Veiculo> buscarPorNomeParcial(String termo) {
//        if (termo == null) termo = "";
//        final String t = termo.toLowerCase();
//        return listarTodos().stream()
//                .filter(v -> v.getNome() != null && v.getNome().toLowerCase().contains(t))
//                .collect(Collectors.toList());
//    }
//
//    // Lista apenas os veículos atualmente disponíveis.
//    public List<Veiculo> buscarDisponiveis() {
//        return listarTodos().stream()
//                .filter(Veiculo::isDisponivel)
//                .collect(Collectors.toList());
//    }
//}
