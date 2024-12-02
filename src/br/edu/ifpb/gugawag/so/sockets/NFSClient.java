package br.edu.ifpb.gugawag.so.sockets;

import java.io.*;
import java.net.*;

public class NFSClient {

    private static final String SERVER_ADDRESS = "localhost";
    private static final int SERVER_PORT = 12345;

    public static void main(String[] args) {
        try (Socket socket = new Socket(SERVER_ADDRESS, SERVER_PORT);
             BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
             PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
             BufferedReader console = new BufferedReader(new InputStreamReader(System.in))) {

            System.out.println("Cliente conectado ao servidor NFS.");
            System.out.println("Comandos disponíveis: readdir, create <nome>, rename <antigo> <novo>, remove <nome>");

            String command;
            while (true) {
                System.out.print("Digite um comando: ");
                command = console.readLine();
                if ("exit".equalsIgnoreCase(command)) {
                    System.out.println("Encerrando cliente...");
                    break;
                }

                out.println(command);
                String response = in.readLine();
                System.out.println("Resposta do servidor: " + response);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}