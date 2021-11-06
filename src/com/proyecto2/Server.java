package com.proyecto2;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Represents the server
 * @author Diana Mejías Hernández
 * @version 1
 * @since 1
 */
public class Server {
    public static ServerSocket server;
    public static Socket client;
    public static final int port = 9090;
    public static ArrayList<ClientHandler> clients = new ArrayList<>();
    public static ArrayList<Integer> clientNumber = new ArrayList<>();
    public static int contClients = 1;
    private static ExecutorService limit = Executors.newFixedThreadPool(20);

    /**
     * Runs the server that assigns a client to a ClientHandler
     * @param args
     * @throws IOException
     */
    public static void main(String[] args) throws IOException {
        server = new ServerSocket(port);
        try {
            while(true) {
                System.out.println("Server waiting for connection...");
                client = server.accept();
                System.out.println("Client connected");
                clientNumber.add(contClients);
                System.out.println("NOMBRE DE CLIENTE: " + contClients);
                DataInputStream in = new DataInputStream(client.getInputStream());
                DataOutputStream out = new DataOutputStream(client.getOutputStream());
                contClients++;
                ClientHandler clientThread = new ClientHandler(client, contClients, in, out);
                clients.add(clientThread);
                limit.execute(clientThread);
            }
        }catch (Exception e){
            server.close();
            client.close();
            System.out.println("Closing connection...");
        }
    }

}
