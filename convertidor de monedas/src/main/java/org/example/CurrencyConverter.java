package org.example;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Scanner;
import org.json.JSONObject;

public class CurrencyConverter {

    private static final String API_KEY = "f93c52a9c75403697aa6fdf3";  // Reemplaza con tu clave API
    private static final String BASE_URL = "https://v6.exchangerate-api.com/v6/";

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        // Mostrar opciones al usuario
        System.out.println("Convertidor de monedas");
        System.out.println("1. USD a EUR");
        System.out.println("2. USD a COP");
        System.out.println("3. EUR a USD");
        System.out.println("4. EUR a GBP");
        System.out.println("5. GBP a USD");
        System.out.println("6. GBP a EUR");
        System.out.print("Elige una opción (1-6): ");

        int opcion = scanner.nextInt();

        // Pedir al usuario que ingrese el monto a convertir
        System.out.print("Ingresa el monto a convertir: ");
        double monto = scanner.nextDouble();

        // Obtener la tasa de cambio actual desde la API
        double tasaCambio = obtenerTasaDeCambio(opcion);

        if (tasaCambio == 0) {
            System.out.println("Hubo un problema al obtener la tasa de cambio.");
        } else {
            // Realizar la conversión
            double resultado = monto * tasaCambio;
            System.out.printf("El resultado es: %.2f\n", resultado);
        }

        scanner.close();
    }

    private static double obtenerTasaDeCambio(int opcion) {
        String monedaOrigen = "", monedaDestino = "";

        // Determinar las monedas de origen y destino según la opción seleccionada
        switch (opcion) {
            case 1:
                monedaOrigen = "USD";
                monedaDestino = "EUR";
                break;
            case 2:
                monedaOrigen = "USD";
                monedaDestino = "COP";
                break;
            case 3:
                monedaOrigen = "EUR";
                monedaDestino = "USD";
                break;
            case 4:
                monedaOrigen = "EUR";
                monedaDestino = "GBP";
                break;
            case 5:
                monedaOrigen = "GBP";
                monedaDestino = "USD";
                break;
            case 6:
                monedaOrigen = "GBP";
                monedaDestino = "EUR";
                break;
            default:
                System.out.println("Opción no válida.");
                return 0;
        }

        try {
            // Crear la URL para obtener la tasa de cambio desde la API
            String url = BASE_URL + API_KEY + "/latest/" + monedaOrigen;

            // Crear una solicitud HTTP
            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(url))
                    .build();

            // Enviar la solicitud y obtener la respuesta
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            // Procesar la respuesta JSON
            org.json.JSONObject jsonResponse = new org.json.JSONObject(response.body());

            // Verificar si la respuesta es válida
            if (jsonResponse.getString("result").equals("success")) {
                // Obtener la tasa de cambio entre las dos monedas
                org.json.JSONObject conversionRates = jsonResponse.getJSONObject("conversion_rates");
                return conversionRates.getDouble(monedaDestino);
            } else {
                System.out.println("Error al obtener la tasa de cambio: " + jsonResponse.toString(Integer.parseInt("error-type")));
                return 0;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }
}


