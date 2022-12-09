package server;

import java.awt.Component;
import java.awt.Font;
import java.awt.*;
import java.io.*;
import java.lang.*;
import java.net.*;
import files.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;


public class Server{

    static ArrayList<MyFile> myFiles = new ArrayList<>();

    File[] filesToSend = new File[1];
    
    public static void main(String[] args) throws Exception{
        int fileId = 0;
        
        JFrame frame = new JFrame("Server");
        frame.setSize(500, 500);
        frame.setResizable(true);
        frame.setLocationRelativeTo(null);
        frame.setLayout(new BoxLayout(frame.getContentPane(), BoxLayout.Y_AXIS));
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel,BoxLayout.Y_AXIS));
    
        JScrollPane scrollPane = new JScrollPane(panel);
        scrollPane.setVerticalScrollBarPolicy(scrollPane.VERTICAL_SCROLLBAR_ALWAYS);
    
        JLabel title = new JLabel("File receiver");
        title.setFont(new Font("Arial",Font.BOLD, 25));
        title.setBorder(new EmptyBorder(20,0,10,0));
        title.setSize(450,450);
        title.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        frame.add(title);
        frame.add(scrollPane);
        frame.setVisible(true);
        
        ServerSocket serversocket = new ServerSocket(1234);
            
        while(true){
            try{
                Socket socket = serversocket.accept();

                DataInputStream dataInputStream = new DataInputStream(socket.getInputStream());

                int filenamelength = dataInputStream.readInt();

                if(filenamelength > 0){
                    byte[] filenameBytes = new byte[filenamelength];
                    dataInputStream.readFully(filenameBytes, 0, filenameBytes.length);
                    String fileName = new String(filenameBytes);

                    int fileContentlength = dataInputStream.readInt();

                    if(fileContentlength > 0){
                        byte[] fileContentBytes = new byte[fileContentlength];
                        dataInputStream.readFully(fileContentBytes, 0,fileContentlength);

                        JPanel fileRow = new JPanel();
                        fileRow.setLayout(new BoxLayout(fileRow, BoxLayout.Y_AXIS));

                        JLabel filename = new JLabel(fileName);
                        filename.setFont(new Font("Arial",Font.BOLD,20));
                        filename.setBorder(new EmptyBorder(10, 0, 10, 0));
                    
                        if(getFileExtenxion(fileName).equalsIgnoreCase("txt")){
                            fileRow.setName(String.valueOf(fileId)); 
                            fileRow.addMouseListener(getMyMouseListener());
                            
                            fileRow.add(filename);
                            panel.add(fileRow);
                            frame.validate();

                        }
                        else{
                            fileRow.setName(String.valueOf(fileId));
                            fileRow.addMouseListener(getMyMouseListener());
                            fileRow.add(filename);
                            panel.add(fileRow);
                            
                            frame.validate();
                        }

                        myFiles.add(new MyFile(fileId, fileName, fileContentBytes, getFileExtenxion(fileName)));
                    }
                }
                
            }
            catch(Exception error){
                System.out.print(error);
            }
        }
    }

    public static MouseListener getMyMouseListener(){

        return new MouseListener(){
            public void mouseClicked(MouseEvent e){
                JPanel panel = (JPanel) e.getSource();
        
                int fileId = Integer.parseInt(panel.getName());
        
                for(MyFile myfile: myFiles){
                    if(myfile.getId() == fileId){
                        JFrame preview = createFrame(myfile.getName(), myfile.getData(), myfile.getFileExtenxion());
                        preview.setVisible(true);
                    }
                    // else{
                    //     fileRow.setName(String.valueOf(fileId));
                    //     fileRow.addMouseListener(getMyMouseListener());
                    // }
                }
            }
            public void mousePressed(MouseEvent e){

            }
            public void mouseReleased(MouseEvent e){
                
            }
            public void mouseEntered(MouseEvent e){

            }
            public void mouseExited(MouseEvent e){

            }
        };
    }

    public static JFrame createFrame(String fileName, byte[] fileData, String fileExtension){
       JFrame frame = new JFrame("File Downloader"); 
        frame.setSize(400,400);

        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        JLabel title = new JLabel("File Downloader");
        title.setAlignmentX(Component.CENTER_ALIGNMENT);
        title.setFont(new Font("Arial",Font.BOLD,20));
        title.setBorder(new EmptyBorder(20,0,10,0));

        JLabel prompt = new JLabel("Are you sure you want to download"+fileName+"?");
        prompt.setFont(new Font("Arial",Font.BOLD,20));
        prompt.setBorder(new EmptyBorder(20,0,10,0));
        prompt.setAlignmentX(Component.CENTER_ALIGNMENT);

        JButton bYes = new JButton("Yes");
        bYes.setPreferredSize(new Dimension(150,75));
        bYes.setFont(new Font("Arial",Font.BOLD,20));

        JButton bNo = new JButton("No");
        bNo.setPreferredSize(new Dimension(150,75));
        bNo.setFont(new Font("Arial",Font.BOLD,20));

        JLabel fileContent = new JLabel();
        fileContent.setAlignmentX(Component.CENTER_ALIGNMENT);

        JPanel buttons = new JPanel();
        buttons.setBorder(new EmptyBorder(20,0,10,0));
        buttons.add(bYes);
        buttons.add(bNo);
        
        if(fileExtension.equalsIgnoreCase("txt")){
            fileContent.setText("<html>"+ new String(fileData) +"</html>");
        }
        else{
            fileContent.setIcon(new ImageIcon(fileData));
        }
        // else{
        //     fileContent.setIcon(new )
        // }

        bYes.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e){
                File fileToDownload = new File(fileName);

                try{
                    FileOutputStream fileOutputStream = new FileOutputStream(fileToDownload);
                    fileOutputStream.write(fileData); 
                    fileOutputStream.close();
                    

                    frame.dispose();
                }
                catch(Exception error){
                    System.out.print(error);
                }
            }
        });

        bNo.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e){
                frame.dispose();
            }
        });

        panel.add(title);
        panel.add(prompt);
        panel.add(fileContent);
        panel.add(buttons);

        frame.add(panel);

        return frame;
    }
    
    public static String getFileExtenxion(String fileName){
        int i = fileName.lastIndexOf('.');
        
        if(i > 0){
            return fileName.substring(i+1);
        }
        else{
            return "NO extension found";
        }
    }

}