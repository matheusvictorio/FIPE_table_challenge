package com.praticando.TabelaFipe.principal;

import com.praticando.TabelaFipe.model.Dados;
import com.praticando.TabelaFipe.model.Modelos;
import com.praticando.TabelaFipe.model.Veiculo;
import com.praticando.TabelaFipe.service.ConsumoApi;
import com.praticando.TabelaFipe.service.ConverteDados;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;


public class Principal {
    private Scanner sc = new Scanner(System.in);
    private ConsumoApi consumoApi = new ConsumoApi();

    private ConverteDados conversor = new ConverteDados();

    private final String urlBase = "https://parallelum.com.br/fipe/api/v1";

    public void exibeMenu() {
        var menu = """
                *** Opções ***
                - Carros
                - Motos
                - Caminhões
                
                Digite a opção:
              
                """;
        System.out.println(menu);

        var opcao = sc.nextLine();
        String endereco;

        if (opcao.toLowerCase().contains("carros")) {
            endereco = urlBase + "/carros/marcas";
        } else if (opcao.toLowerCase().contains("mot")) {
            endereco = urlBase + "/motos/marcas";
        } else if (opcao.toLowerCase().contains("caminh")) {
            endereco = urlBase + "/caminhoes/marcas";
        } else {
            System.out.println("Opção inválida");
            exibeMenu();
            return;
        }

        var json = consumoApi.obterDados(endereco);
        System.out.println(json);

        var marcas = conversor.obterLista(json, Dados.class);

        marcas.stream()
                .sorted(Comparator.comparing(Dados::nome))
                .forEach(System.out::println);

        System.out.println("Informe o código da marca que deseja consultar:");
        var codigo = sc.nextLine();

        endereco = endereco + "/" + codigo + "/modelos";
        json = consumoApi.obterDados(endereco);

        var modelosLista = conversor.obterDados(json, Modelos.class);

        System.out.println("\nModelos da marca: ");
        modelosLista.modelos().stream()
                .sorted(Comparator.comparing(Dados::codigo))
                .forEach(System.out::println);

        System.out.println("Digite um trecho do veículo que deseja consultar:");
        var nomeVeiculo = sc.nextLine();
        List<Dados> modelosBuscados = modelosLista.modelos().stream()
                .filter(m -> m.nome().toLowerCase().contains(nomeVeiculo.toLowerCase()))
                .collect(Collectors.toList());

        System.out.println("Modelos encontrados: ");
        modelosBuscados.stream()
                .sorted(Comparator.comparing(Dados::nome))
                .forEach(System.out::println);

        System.out.println("Digite o código do modelo para buscar avaliações:");
        var codigoModelo = sc.nextLine();

        endereco = endereco + "/" + codigoModelo + "/anos";

        json = consumoApi.obterDados(endereco);
        List<Dados> anos = conversor.obterLista(json, Dados.class);
        List<Veiculo> veiculos = new ArrayList<>();

        for (int i = 0; i < anos.size(); i++) {
            var enderecoAnos = endereco + "/" + anos.get(i).codigo();
            json = consumoApi.obterDados(enderecoAnos);
            Veiculo veiculo = conversor.obterDados(json, Veiculo.class);
            veiculos.add(veiculo);
        }

        veiculos.forEach(System.out::println);
    }
}
