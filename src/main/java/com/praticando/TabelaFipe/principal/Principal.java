package com.praticando.TabelaFipe.principal;

import com.praticando.TabelaFipe.service.ConsumoApi;

import java.util.Scanner;


public class Principal {
    private Scanner sc = new Scanner(System.in);
    private ConsumoApi consumoApi = new ConsumoApi();

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

    }
}
