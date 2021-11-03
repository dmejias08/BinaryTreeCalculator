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
                String response = infixToPostfix(command);
                out.writeUTF(response);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }



    static String infixToPostfix(String exp) {
        // initializing empty String for result
        String result = new String("");

        // initializing empty stack
        Stack<Character> operators = new Stack<>();

        for (int i = 0; i < exp.length(); i++) {
            char c = exp.charAt(i);

            // If the scanned character is an
            // operand, add it to output.
            int newPosition = i;
            if (Character.isLetterOrDigit(c)) {
                for (int n = i; n < exp.length(); n++) {
                    char c2 = exp.charAt(n);
                    if (Character.isLetterOrDigit(c2)) {
                        result += c2;
                    } else {
                        newPosition = n;
                        break;
                    }
                }
                i = newPosition - 1;
                result += " ";
                // If the scanned character is an '(',
                // push it to the stack.
            } else if (c == '(') {
                operators.push(c);

                //  If the scanned character is an ')',
                // pop and output from the stack
                // until an '(' is encountered.
            } else if (c == ')') {
                while (!operators.isEmpty() && operators.peek() != '(') {
                    result += operators.pop() + " ";
                }
                operators.pop();
            } else {
                while (!operators.isEmpty() && Prec(c) <= Prec(operators.peek())) {
                    result += operators.pop();
                    result += " ";
                }
                operators.push(c);
            }
        }

        // pop all the operators from the stack
        while (!operators.isEmpty()) {
            if (operators.peek() == '(') {
                return "Invalid Expression";
            }
            result += operators.pop() + " ";
        }
        System.out.println(result);
        return result;
    }

    static int Prec(char operator) {
        if (operator == '+' || operator == '-') {
            return 1;
        } else if (operator == '*' || operator == '/') {
            return 2;
        }
        return -1;
    }


    //Node class BinaryTree
    public class Node {
        String data;
        Node left;
        Node right;

        //Constructor
        Node(String value) {
            this.data = value;
            this.left = this.right = null;
        }
    }

    //Creating BinaryTree
    public class BinaryTree {
        boolean foundOperator(char op) {
            if (op == '+' || op == '-' || op == '*' || op == '/') {
                return true;
            } else {
                return false;
            }
        }
        public Node BinaryTree(String exp){

            Stack <Node> nodeStack = new Stack<>();
            Node node, node1, node2;


            for (int j = 0; j < exp.length()-1; j++) {
                char ch = exp.charAt(j);

                // Found number add to the stack
                if (foundOperator(ch) == false) {
                    String actualChildren="";
                    int newPosition=0;
                    for (int n = j; n < exp.length()-1; n++) {
                        char c2 = exp.charAt(n);
                        if (Character.isLetterOrDigit(c2)) {
                            actualChildren += c2;
                        } else {
                            newPosition=n;
                            break;
                        }
                    }

                    j=newPosition;

                    node = new Node(actualChildren);
                    nodeStack.push(node);

                } else { //Found operator
                    node = new Node(""+ch);
                    j++;
                    //Add its children
                    node1 = nodeStack.pop();
                    node2 = nodeStack.pop();

                    node.left = node1;
                    node.right = node2;

                    //add father to the stack
                    nodeStack.push(node);
                }
            }
            node = nodeStack.peek();
            return node;
        }
    }
    // evaluate binary tress
    public static int evalBinaryTree(Node root){
        System.out.println("Root");
        System.out.println(root.data);

        if(root == null){
            return 0;
        }

        if (root.left == null && root.right == null){
            return Integer.valueOf(root.data);
        }

        int result=0;

        if (root.data.equals("+")){
            result = evalBinaryTree(root.right) + evalBinaryTree(root.left);
        } else if (root.data.equals("-")){
            result = evalBinaryTree(root.right) - evalBinaryTree(root.left); ;
        } else if (root.data.equals("*")){
            result = evalBinaryTree(root.right) * evalBinaryTree(root.left);;
        } else if (root.data.equals("/")) {
            result = evalBinaryTree(root.right) / evalBinaryTree(root.left);;
        } else {
            result = Integer.valueOf(root.data);
        }
        return result;
    }
}
