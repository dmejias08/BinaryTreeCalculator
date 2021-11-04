package com.proyecto2;

import java.io.*;
import java.net.Socket;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Stack;

public class ClientHandler implements Runnable {
    public String register="";
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
                BinaryTree tree = new BinaryTree();
                int result = evalBinaryTree(tree.BinaryTree(infixToPostfix("("+command+")")));
                String response = String.valueOf(result);
                makeCSV(String.valueOf(name-1),command,response);
                out.writeUTF(response);
            }
        } catch (Exception e) {
            try {
                in.close();
                out.close();
            } catch (IOException ex) {
            }
        }
    }

    public void makeCSV(String cliente, String operation, String result) {
        try {
            String currentPath = Paths.get("").toAbsolutePath().normalize().toString();
            File newFolder = new File(currentPath);
            boolean dirCreated = newFolder.mkdir();

            // get current time
            DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-M-dd_HH-mm-ss");
            LocalDateTime now = LocalDateTime.now();
            String fileName = "Reporte_Cliente_"+ cliente + ".csv";

            // Whatever the file path is.
            File statText = new File(currentPath + "/" + fileName);
            FileOutputStream os = new FileOutputStream(statText);
            OutputStreamWriter osw = new OutputStreamWriter(os);
            Writer w = new BufferedWriter(osw);

            register+=operation+","+result+","+dtf.format(now) +"\n";

            w.write(register);

            w.close();
        } catch (IOException e) {
            System.err.println("Problem writing to the file " + e);
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
        return result;
    }

    static int Prec(char operator) {
        if (operator == '+' || operator == '-') {
            return 1;
        } else if (operator == '*' || operator == '/' || operator == '%') {
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
            if (op == '+' || op == '-' || op == '*' || op == '/' || op == '%') {
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
            result = evalBinaryTree(root.right) - evalBinaryTree(root.left);
        } else if (root.data.equals("*")){
            result = evalBinaryTree(root.right) * evalBinaryTree(root.left);
        } else if (root.data.equals("/")) {
            result = evalBinaryTree(root.right) / evalBinaryTree(root.left);
        } else if (root.data.equals("%")) {
            result = evalBinaryTree(root.right) % evalBinaryTree(root.left);
        } else {
            result = Integer.valueOf(root.data);
        }
        return result;
    }
}
