package br.edu.solutis.dev.trail.cli.view;

import br.edu.solutis.dev.trail.locadora.model.enums.SexoEnum;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.Scanner;

public class Historia01 {
    public static void main(String[] args) throws IOException, InterruptedException {
        Scanner scanner = new Scanner(System.in);
        HttpClient client = HttpClient.newHttpClient();
        String cpf = null;

        while (true) {
            System.out.println("=== Cadastro de Cliente ===");
            System.out.print("CPF: ");
            cpf = scanner.nextLine();

            HttpRequest checkRequest = HttpRequest.newBuilder()
                    .uri(URI.create("http://localhost:8080/motoristas/Cpf?cpf=" + cpf))
                    .GET()
                    .build();

            HttpResponse<String> checkResponse = client.send(checkRequest, HttpResponse.BodyHandlers.ofString());

            if (checkResponse.statusCode() == 200) {
                System.out.println("CPF já cadastrado. Por favor, insira um CPF diferente.");
            } else if (checkResponse.statusCode() == 404) {
                // CPF não cadastrado, saia do loop
                break;
            } else {
                System.out.println("Erro ao verificar o CPF: " + checkResponse.body());
                return;
            }
        }

        // Continuar com o cadastro se o CPF não estiver cadastrado
        System.out.print("Nome completo: ");
        String nome = scanner.nextLine();

        System.out.print("Data de nascimento (YYYY-MM-DD): ");
        String aniversarioStr = scanner.nextLine();
        LocalDate aniversario = null;
        try {
            aniversario = LocalDate.parse(aniversarioStr);
        } catch (DateTimeParseException e) {
            System.out.println("Data de nascimento inválida. Por favor, insira no formato YYYY-MM-DD.");
            return;
        }

        System.out.print("Número da CNH: ");
        String cnh = scanner.nextLine();

        System.out.print("Email: ");
        String email = scanner.nextLine();

        System.out.print("Sexo (MASCULINO/FEMININO): ");
        String sexoStr = scanner.nextLine().toUpperCase();
        SexoEnum sexo = null;
        try {
            sexo = SexoEnum.valueOf(sexoStr);
        } catch (IllegalArgumentException e) {
            System.out.println("Sexo inválido. Por favor, insira MASCULINO, FEMININO ou OUTRO.");
            return;
        }

        String json = String.format("""
                {
                    "id": 3,
                    "email": "%s",
                    "nome": "%s",
                    "cpf": "%s",
                    "aniversario": "%s",
                    "sexo": "%s",
                    "cnh": "%s"
                }
                """, email, nome, cpf, aniversario, sexo, cnh);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/motoristas"))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(json))
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        System.out.println("Status Code: " + response.statusCode());
        System.out.println("Response Body: " + response.body());
    }
}
