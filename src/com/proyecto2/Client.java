package com.proyecto2;


import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Scanner;

public class Client  {
    private static Interface interfac;
    private static DataInputStream in;
    private static DataOutputStream out;
    private static Socket server;
    public static final int port = 9090;

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
                    out.writeUTF("("+request+")");
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