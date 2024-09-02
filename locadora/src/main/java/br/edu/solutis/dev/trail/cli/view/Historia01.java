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

        // Validação do CPF
        while (true) {
            System.out.println("=== Cadastro de Cliente ===");
            System.out.print("CPF: ");
            cpf = scanner.nextLine();

            if (cpf.isEmpty() || !cpf.matches("\\d{11}")) { // CPF deve ter 11 dígitos
                System.out.println("CPF inválido. Por favor, insira um CPF com 11 dígitos numéricos.");
                continue;
            }

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


        // Validação do nome
        String nome = null;
        while (true) {
            System.out.print("Nome completo: ");
            nome = scanner.nextLine();

            if (nome.isEmpty()) {
                System.out.println("Nome não pode ser vazio.");
            } else {
                break;
            }
        }

        // Validação da data de nascimento
        LocalDate aniversario = null;
        while (true) {
            System.out.print("Data de nascimento (YYYY-MM-DD): ");
            String aniversarioStr = scanner.nextLine();

            try {
                aniversario = LocalDate.parse(aniversarioStr);
                break;
            } catch (DateTimeParseException e) {
                System.out.println("Data de nascimento inválida. Por favor, insira no formato YYYY-MM-DD.");
            }
        }

        // Validação da CNH
        String cnh = null;
        while (true) {
            System.out.print("Número da CNH: ");
            cnh = scanner.nextLine();

            if (cnh.isEmpty() || !cnh.matches("\\d{11}")) { // CNH deve ter 11 dígitos numéricos
                System.out.println("CNH inválida. Por favor, insira um número de CNH com 11 dígitos numéricos.");
            } else {
                break;
            }
        }

        // Validação do email
        String email = null;
        while (true) {
            System.out.print("Email: ");
            email = scanner.nextLine();

            if (email.isEmpty() || !email.matches("^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$")) {
                System.out.println("Email inválido. Por favor, insira um email válido.");
                continue;
            }

            HttpRequest checkRequest = HttpRequest.newBuilder()
                    .uri(URI.create("http://localhost:8080/motoristas/email?email=" + email))
                    .GET()
                    .build();

            HttpResponse<String> checkResponse = client.send(checkRequest, HttpResponse.BodyHandlers.ofString());

            if (checkResponse.statusCode() == 200) {
                System.out.println("Email já cadastrado. Por favor, insira um email diferente.");
            } else if (checkResponse.statusCode() == 404) {
                break;
            } else {
                System.out.println("Erro ao verificar o email: " + checkResponse.body());
                return;
            }
        }

        // Validação do sexo
        SexoEnum sexo = null;
        while (true) {
            System.out.print("Sexo (MASCULINO/FEMININO): ");
            String sexoStr = scanner.nextLine().toUpperCase();

            try {
                sexo = SexoEnum.valueOf(sexoStr);
                break;
            } catch (IllegalArgumentException e) {
                System.out.println("Sexo inválido. Por favor, insira MASCULINO, FEMININO ou OUTRO.");
            }
        }

        // Criação do JSON e envio da requisição
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
