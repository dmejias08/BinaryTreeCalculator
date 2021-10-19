package com.proyecto2;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Interface extends JFrame {
    JPanel pane;
    Button key0;
    Button key1;
    Button key2;
    Button key3;
    Button key4;
    Button key5;
    Button key6;
    Button key7;
    Button key8;
    Button key9;
    Button keyMin;
    Button keySum;
    Button keyDiv;
    Button keyMult;
    Button keyPer;
    Button keyEq;
    Button keyDelAll;
    Button keyDelLast;
    Button keyParent1;
    Button keyParent2;
    JTextField operation;
    JTextField result;
    JLabel title;

    public Interface(){
        setTitle("Calculator");
        setVisible(true);
        setSize(300,400 );
        pane = new JPanel();
        this.getContentPane().add(pane);
        pane.setLayout(null);

        operation = new JTextField(20);
        operation.setSize(270, 40);
        operation.setLocation(10, 10);
        pane.add(operation);

        //Definición de botones
        key1 = new Button(this,"1",10,60,0);
        key2 = new Button(this,"2",60,60,0);
        key3 = new Button(this,"3",110,60,0);
        key4 = new Button(this,"4",10,110,0);
        key5 = new Button(this,"5",60,110,0);
        key6 = new Button(this,"6",110,110,0);
        key7 = new Button(this,"7",10,160,0);
        key8 = new Button(this,"8",60,160,0);
        key9 = new Button(this,"9",110,160,0);
        keyParent1 = new Button(this,"(",10,210,0);
        key0 = new Button(this,"0",60,210,0);
        keyParent2 = new Button(this,")",110,210,0);
        keySum = new Button(this,"+",160,60,1);
        keyMin = new Button(this,"-",160,110,0);
        keyMult = new Button(this,"*",160,160,0);
        keyDiv = new Button(this,"/",160,210,0);
        keyPer = new Button(this,"%",210,60,20);
        keyDelLast = new Button(this,"DEL",210,110,20);
        keyDelAll = new Button(this,"AC",210,160,20);
        keyEq = new Button(this,"=",210,210,20);
    }
}

class Button implements ActionListener {
    JButton obj;
    Interface cInterface;
    String opText;
       public Button(Interface clientInterface, String text, int x, int y, int scale){
           cInterface=clientInterface;
           opText=text;
           obj = new JButton(text);
           obj.setSize(40+scale,40);
           obj.setLocation(x,y);
           obj.addActionListener( this::actionPerformed);
           cInterface.pane.add(obj);
       }

    @Override
    public void actionPerformed(ActionEvent e) {
           if (e.getSource()==obj){
               if (obj.getText()=="DEL"){
                   cInterface.operation.setText(cInterface.operation.getText().replaceFirst(".$",""));
               } else if (obj.getText()=="AC") {
                   cInterface.operation.setText(null);
               } else if (obj.getText()=="="){
                   //Prueba infix to posfix
               } else {
                   cInterface.operation.setText(cInterface.operation.getText()+opText);
               }
           }
    }
}
