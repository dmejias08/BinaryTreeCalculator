package com.proyecto2;

import javax.swing.*;
import javax.swing.text.BadLocationException;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class Interface extends JFrame implements KeyListener {
    int openParen=0, closeParen=0;
    String frozenText, request="null";
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
        operation.addKeyListener(this);
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

    @Override
    public void keyTyped(KeyEvent e) {
        char key = e.getKeyChar();
        char last = 0;
        if (key>=37 && key<=57 && key!=44 && key!=46 && key!=38 && key!=39) {
            frozenText=operation.getText();
            if (!operation.getText().equals("")) {
                last = operation.getText().charAt(operation.getCaretPosition()-1);
            }
            if (key>=42 && key<=47 && key!=45 && (last>=48 || last==41)) {

            } else if (key==41 && openParen>closeParen && last>=48) {
                closeParen++;
            } else if (key==40 && ((last<=47 && last>41) || last==0 || last==40)){
                openParen++;
            } else if (key==45 && (last>=48 || last==41 || last==0) ) {
            } else if (key==37 && last>=48) {
            } else if (key>=48 && last!=41 && last!=37){
            } else {
                if (key>=42 && key<=47 && last<=47 && last>=42){
                    try {
                        operation.getDocument().remove(operation.getCaretPosition()-1, 1);
                        operation.getDocument().insertString(operation.getCaretPosition(), ""+key,null);
                    } catch (BadLocationException ex) {
                        ex.printStackTrace();
                    }
                }
                e.consume();
            }
        } else {
            e.consume();
        }
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == 8){
            char last = 0;
            if (!operation.getText().equals("")) {
                last = frozenText.charAt(frozenText.length() - 1);
            }
            if (last==40){
                openParen--;
            } else if (last==41) {
                closeParen--;
            }
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
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
               cInterface.frozenText = cInterface.operation.getText();
               char key = opText.charAt(0);
               char last = 0;
               if (!cInterface.operation.getText().equals("")) {
                   try {
                       last = cInterface.operation.getText().charAt(cInterface.operation.getCaretPosition() - 1);
                   } catch (Exception n){
                   }
               }
               if (obj.getText()=="DEL"){
                   if (last==40){
                       cInterface.openParen--;
                   } else if (last==41) {
                       cInterface.closeParen--;
                   }
                   try {
                       cInterface.operation.getDocument().remove(cInterface.operation.getCaretPosition()-1, 1);
                   } catch (BadLocationException ex) {
                       ex.printStackTrace();
                   }
               } else if (obj.getText()=="AC") {
                   cInterface.operation.setText(null);
               } else if (obj.getText()=="="){
                   if (cInterface.openParen==cInterface.closeParen){
                       cInterface.request= cInterface.operation.getText();
                   } else {
                       JOptionPane.showMessageDialog(null, "La cantidad de parentesis abiertos no calza con los cerrados");
                   }
               } else {
                   if (key>=37 && key<=57 && key!=44 && key!=46 && key!=38 && key!=39) {
                       try {
                           if (key >= 42 && key <= 47 && key != 45 && (last >= 48 || last == 41)) {
                               cInterface.operation.getDocument().insertString(cInterface.operation.getCaretPosition(), ""+key,null);
                           } else if (key == 41 && cInterface.openParen > cInterface.closeParen && last >= 48) {
                               cInterface.operation.getDocument().insertString(cInterface.operation.getCaretPosition(), ""+key,null);
                               cInterface.closeParen++;
                           } else if (key == 40 && ((last <= 47 && last > 41) || last == 0 || last == 40)) {
                               cInterface.operation.getDocument().insertString(cInterface.operation.getCaretPosition(), ""+key,null);
                               cInterface.openParen++;
                           } else if (key == 45 && (last >= 48 || last == 41 || last == 0)) {
                               cInterface.operation.getDocument().insertString(cInterface.operation.getCaretPosition(), ""+key,null);
                           } else if (key == 37 && last >= 48) {
                               cInterface.operation.getDocument().insertString(cInterface.operation.getCaretPosition(), ""+key,null);
                           } else if (key >= 48 && last != 41 && last != 37) {
                               cInterface.operation.getDocument().insertString(cInterface.operation.getCaretPosition(), ""+key,null);
                           } else {
                               if (key >= 42 && key <= 47 && last <= 47 && last >= 42) {
                                   cInterface.operation.getDocument().remove(cInterface.operation.getCaretPosition()-1, 1);
                               }
                           }
                       } catch (BadLocationException ex){
                           ex.printStackTrace();
                       }
                   }
               }
           }
    }
}
