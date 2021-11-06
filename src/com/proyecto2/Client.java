package com.proyecto2;


import java.io.*;
import java.net.Socket;

/**
 * Represents the clients
 * @author Diana Mejías Hernández
 * @version 1
 * @since 1
 */
public class Client  {
    private static Interface interfac;
    private static DataInputStream in;
    private static DataOutputStream out;
    private static Socket server;
    public static final int port = 9090;

    /**
     * Runs the client and send its request, also read the server's response
     * @param args
     * @throws IOException
     */
    public static void main(String[] args) throws IOException {
        interfac = new Interface();
        interfac.repaint();
        server = new Socket("localhost",port);
        in = new DataInputStream(server.getInputStream());
        out = new DataOutputStream(server.getOutputStream());

        try {
            while (true) {
                String request = interfac.request;
                if (request.equals("null")) {
                    System.out.print("");
                } else {
                    if (request.charAt(0)=='-'){
                        request="0"+request;
                    }
                    out.writeUTF(request);
                    String response = in.readUTF();
                    interfac.operation.setText(response);
                    interfac.frozenText="";
                    interfac.operation.setCaretPosition(response.length());
                    interfac.request="null";
                }

            }
        }catch (Exception e) {
            in.close();
            out.close();
            e.printStackTrace();
        }

    }
}