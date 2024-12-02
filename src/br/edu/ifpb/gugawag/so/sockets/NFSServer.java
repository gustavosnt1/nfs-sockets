package br.edu.ifpb.gugawag.so.sockets;

import java.io.*;
import java.net.*;
import java.util.*;

public class NFSServer {

    private static final int PORT = 12345;
    private static List<String> arquivos = new ArrayList<>();

    public static void main(String[] args) {
        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            System.out.println("Servidor NFS iniciado. Aguardando conexões...");
            while (true) {
                Socket clientSocket = serverSocket.accept();
                System.out.println("Cliente conectado: " + clientSocket.getInetAddress());
                handleClient(clientSocket);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void handleClient(Socket clientSocket) {
        try (
                BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true)
        ) {
            String command;
            while ((command = in.readLine()) != null) {
                System.out.println("Comando recebido: " + command);
                String[] parts = command.split(" ", 2);
                String action = parts[0];
                String response = "";

                switch (action) {
                    case "readdir":
                        response = String.join(",", arquivos);
                        break;

                    case "create":
                        if (parts.length > 1) {
                            arquivos.add(parts[1]);
                            response = "Arquivo criado: " + parts[1];
                        } else {
                            response = "Erro: nome do arquivo não especificado.";
                        }
                        break;

                    case "rename":
                        if (parts.length > 1) {
                            String[] names = parts[1].split(" ");
                            if (names.length == 2 && arquivos.remove(names[0])) {
                                arquivos.add(names[1]);
                                response = "Arquivo renomeado: " + names[0] + " -> " + names[1];
                            } else {
                                response = "Erro: arquivo não encontrado ou nome inválido.";
                            }
                        } else {
                            response = "Erro: parâmetros inválidos.";
                        }
                        break;

                    case "remove":
                        if (parts.length > 1 && arquivos.remove(parts[1])) {
                            response = "Arquivo removido: " + parts[1];
                        } else {
                            response = "Erro: arquivo não encontrado.";
                        }
                        break;

                    default:
                        response = "Comando não reconhecido.";
                        break;
                }

                out.println(response);
            }
        } catch (IOException e) {
            System.out.println("Conexão com o cliente encerrada.");
        }
    }
}
