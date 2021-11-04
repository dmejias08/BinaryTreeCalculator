package com.proyecto2;

import java.io.*;
import java.net.Socket;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Stack;

/**
 * Represents the handler of each client
 * @author Diana Mejías Hernández
 * @author Geovanny García Downing
 * @version 1
 * @since 1
 */

public class ClientHandler implements Runnable {
    public String register="";
    private Socket client;
    private DataInputStream in;
    private DataOutputStream out;
    private int name;

    /**
     * Creates the ClientHandler with a specific client
     * @param clientSocket, the client it needs to handle
     * @param name, the client's name
     * @param in, information the ClientHandle receive
     * @param out, information the ClientHandle send
     * @throws IOException
     */
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

    /**
     * Creates the csv archive that registers each request
     * @param cliente, client's name
     * @param operation, the registered operation
     * @param result, the registered result
     * @throws IOException
     */
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

    /**
     * It turns an infix expression to postfix expression
     * @param exp, the infix expression
     * @return A string representing the postfix expression
     */
    static String infixToPostfix(String exp) {
        // empty String for result
        String result = new String("");

        // empty stack
        Stack<Character> operators = new Stack<>();

        for (int i = 0; i < exp.length(); i++) {
            char c = exp.charAt(i);

            // found operand
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
                // found  '(' push it to the stack.
            } else if (c == '(') {
                operators.push(c);

                //  Found  ')', pop stack
                // until an '(' is encountered.
            } else if (c == ')') {
                while (!operators.isEmpty() && operators.peek() != '(') {
                    result += operators.pop() + " ";
                }
                operators.pop();
            } else {
                while (!operators.isEmpty() && Priotity(c) <= Priotity(operators.peek())) {
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

    /**
     * It returns the priority of the operator
     * @param operator, the operator to analyze
     * @return An integer representing the operator's priority
     */
    static int Priotity(char operator) {
        if (operator == '+' || operator == '-') {
            return 1;
        } else if (operator == '*' || operator == '/' || operator == '%') {
            return 2;
        }
        return -1;
    }

    /**
     * Represents a node, contains information
     * @author Diana Mejías Hernández
     * @version 1
     * @since 1
     */
    //Node class BinaryTree
    public class Node {
        String data;
        Node left;
        Node right;

        /**
         * Creates the node adding the information and its references
         * @param value, the data added to the node
         */
        Node(String value) {
            this.data = value;
            this.left = this.right = null;
        }
    }

    /**
     * Represents the expression binary tree
     * @author Geovanny García Downing
     * @author Diana Mejías Hernández
     * @version 1
     * @since 1
     */
    //Creating BinaryTree
    public class BinaryTree {

        boolean foundOperator(char op) {
            if (op == '+' || op == '-' || op == '*' || op == '/' || op == '%') {
                return true;
            } else {
                return false;
            }
        }

        /**
         * Created the expression binary tree
         * @param exp, A string representing the expression used to create the binary tree
         * @return A Node that represents the tree's root
         */
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

    /**
     * Evaluates the expression binary tree
     * @param root, A Node representing the tree's root
     * @return An integer that represents the result of the tree's evaluation
     */
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
