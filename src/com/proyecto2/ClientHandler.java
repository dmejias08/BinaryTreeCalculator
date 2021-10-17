package com.proyecto2;

import java.io.*;
import java.net.Socket;
import java.util.Stack;

public class ClientHandler implements Runnable {
    private Socket client;
    private DataInputStream in;
    private DataOutputStream out;
    private int name;


    public ClientHandler(Socket clientSocket, int name, DataInputStream in, DataOutputStream out) throws IOException {
        this.client = clientSocket;
        this.name = name;
        this.in = new DataInputStream(client.getInputStream());
        this.out = new DataOutputStream(client.getOutputStream());
    }

    @Override
    public void run() {
        try {
            while (true) {
                String command = in.readUTF();
                System.out.println("Command: " + command);
                if (command.equals("exit")) {
                    out.writeUTF("Exit");
                    client.close();
                    in.close();
                    out.close();
                    System.out.println("Closing connection");
                    break;
                }
                String response = infixToPosfix(command);
                out.writeUTF(response);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }



    public static String infixToPosfix (String exp){

        String result = "";
        // empty stack for operators
        Stack<Character> operators = new Stack<>();

        for (int i = 0; i < exp.length(); i++){
            char ch = exp.charAt(i);

            if (Character.isDigit(ch)){
                result += ch;

            }else if (ch == '('){
                operators.push(ch);

                // pop until find "("
            }else if (ch == ')'){
                while (!operators.isEmpty() &&
                        operators.peek() != '('){
                    result += operators.pop();

                }
            }else { // found an operator
                while (!operators.isEmpty() &&
                        Prec(ch) <= Prec(operators.peek())) {
                    result += operators.pop();
                }
                operators.push(ch);
            }
        }
        //pop all
        while (!operators.isEmpty()){
            if(operators.peek() == '(') {
                break;
            }
            else if(operators.peek()!= '(') {
                result += operators.pop();
            }
        }
        return result;
    }
    static int Prec (char  operator){
        if (operator == '+' || operator == '-'){
            return 1;
        }
        else if (operator == '*' || operator == '/'){
            return 2;
        }
        return -1;
    }
}