package com.proyecto2;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Server {
    public static ServerSocket server;
    public static Socket client;
    public static final int port = 9090;
    public static ArrayList<ClientHandler> clients = new ArrayList<>();
    public static ArrayList<Integer> clientNumber = new ArrayList<>();
    public static int contClients = 1;
    private static ExecutorService limit = Executors.newFixedThreadPool(20);



    public static void main(String[] args) throws IOException {
        server = new ServerSocket(port);
//        while (true){
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
//                while(true){
//                    String command = in.readUTF();
//                    System.out.println("Command: "+command);
//                    if (command.equals("exit")){
//                        out.writeUTF("Exit");
//                        server.close();
//                        client.close();
//                        in.close();
//                        out.close();
//                        System.out.println("Closing connection");
//                        break;
//                    }
//                    String response = "Hola";
//                    out.writeUTF(response);
//                }
                ClientHandler clientThread = new ClientHandler(client, contClients, in, out);
                clients.add(clientThread);
                limit.execute(clientThread);
//                Thread threadNew = new Thread(clientThread);
//                threadNew.start();
            }
        }catch (Exception e){
            server.close();
            client.close();
            System.out.println("Closing connection...");
        }
    }

}
