package com.proyecto2;


import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Scanner;

public class Client  {
    private static DataInputStream in;
    private static DataOutputStream out;
    private static Socket server;
    public static final int port = 9090;

    public static void main(String[] args) throws IOException {
        Scanner sn = new Scanner(System.in);
        sn.useDelimiter("\n");
        server = new Socket("localhost",port);
        in = new DataInputStream(server.getInputStream());
        out = new DataOutputStream(server.getOutputStream());

        try {
            while (true) {
                System.out.println(">");
                String request = sn.nextLine();
//                out.writeUTF(request);
                System.out.println("Request: " + request);
                if (request.equals(null)) {
                    System.out.println("Request is null");
                    continue;
                } else {
                    out.writeUTF(request);
                    String response = in.readUTF();
                    if(response.equals("Exit")){
                        server.close();
                        in.close();
                        out.close();
                        System.out.println("Closing connection");
                        break;
                    }
                    System.out.println("Response: " + response);
                }

            }
        }catch (Exception e) {
            sn.close();
            in.close();
            out.close();
            e.printStackTrace();
        }

    }
}