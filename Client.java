package client;

import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

import java.io.*;
import java.io.DataOutputStream;
import java.util.*;
import java.net.*;
import java.lang.*;
import java.lang.Byte;

import java.awt.Component;
import java.awt.Font;
import java.awt.*;

public class Client{
    public static void main(String[] args){
  
        File[] filesToSend = new File[1];
        JFileChooser filechooser;

        JFrame frame = new JFrame();
        frame.setTitle("Client");
        frame.setSize(500, 500);
        frame.setResizable(true);
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BoxLayout(frame.getContentPane(), BoxLayout.Y_AXIS));
        frame.setVisible(true);

        
        JLabel title = new JLabel("File sender");
        title.setFont(new Font("Arial",Font.BOLD, 25));
        title.setBorder(new EmptyBorder(20,0,10,0));;
        title.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        JLabel fileName = new JLabel("Choose a file to send");
        fileName.setFont(new Font("Arial",Font.BOLD, 20));
        fileName.setAlignmentX(Component.CENTER_ALIGNMENT);

        JPanel panel = new JPanel();
        panel.setBorder(new EmptyBorder(75,0,10,0));
        
        JButton boutonSend =  new JButton("Send File");
        boutonSend.setSize(150, 75);
        boutonSend.setFont(new Font("Arial",Font.BOLD, 20));
    
        JButton boutonChoose = new JButton("Choose File");
        boutonChoose.setSize(150, 75);
        boutonChoose.setFont(new Font("Arial",Font.BOLD,20));

        panel.add(boutonChoose);
        panel.add(boutonSend);
        
        boutonChoose.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e){
            JFileChooser filechooser = new JFileChooser();
            filechooser.setDialogTitle("Choose a File To Send");
            
            // System.out.print("ok");
            if(filechooser.showOpenDialog(boutonChoose) == JFileChooser.APPROVE_OPTION){
                filesToSend[0] = filechooser.getSelectedFile();
                // path = filechooser.getSelectedFile().getName();
                // repertoire = filechooser.getSelectedFile().getPath();
                    fileName.setText("The file you want to send is: " + filesToSend[0].getName());
                }
            }
        });
        
        boutonSend.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e){
            if(filesToSend[0] == null){
                fileName.setText("Please choose a file first");
            }
            else{
                try{
                    FileInputStream fileInputStream = new FileInputStream(filesToSend[0].getAbsolutePath());
                    Socket socket = new Socket("localhost",1234);
        
                    DataOutputStream dataOutputStream = new DataOutputStream(socket.getOutputStream());
    
                    String fileName = filesToSend[0].getName();
                    byte[] filenameBytes = fileName.getBytes();
                    
                    byte[] fileContentBytes = new byte[(int)filesToSend[0].length()];
                    fileInputStream.read(fileContentBytes);
        
                    dataOutputStream.writeInt(filenameBytes.length);
                    dataOutputStream.write(filenameBytes);
        
                    dataOutputStream.writeInt(fileContentBytes.length);
                    dataOutputStream.write(fileContentBytes);
                    // dataOutputStream.close();
                }
                    catch(Exception error){
                        System.out.print(error);
                    }
                }
            }
        });

        frame.add(title);
        frame.add(fileName);
        frame.add(panel);
        frame.setVisible(true);
    
        }
    }