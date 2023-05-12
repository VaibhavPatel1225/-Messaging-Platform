import java.net.*;

import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.*;;
public class ClientCheat  extends JFrame{
       
       Socket socket;
       BufferedReader br;    //for reading
       PrintWriter out;      //for writting

      //GUI Component
      private JLabel heading=new JLabel("Client Area");
      private JTextArea messageArea=new JTextArea();
      private JTextField messageInput = new JTextField();
      private Font font =new Font("Roboto",Font.PLAIN,20);



       // make constructor
       public ClientCheat(){
        try {
            System.out.println("sending request to server....");
            InetAddress ip = InetAddress.getLocalHost();
            socket=new Socket(ip,4242);
            System.out.println("connection done");
            
            br=new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out=new PrintWriter(socket.getOutputStream());
             createGUI();
             handleEvent();
             startReading();
            //startWriting();
 
            
        } catch (Exception e) {
        
        }
       }

      private void createGUI(){

        //window set
        this.setTitle("Server Messanger");
        this.setSize(600, 600);
        this.setLocationRelativeTo(null); //Center Window
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); //CLOSE IF EXIT PRESS
        this.setVisible(true); //window visible

        //each component design
        heading.setFont(font);
        messageInput.setFont(font);
        messageArea.setFont(font);
    
       // heading.setIcon(new ImageIcon("applogo.png")); //set logo
        heading.setHorizontalTextPosition(SwingConstants.CENTER);//text center
        heading.setVerticalTextPosition(SwingConstants.BOTTOM);//bottom of logo text
        heading.setHorizontalAlignment(SwingConstants.CENTER);  //center heading in window
        heading.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));//create empty space in text
         
        //no chage in text area 
        messageArea.setEditable(false);

        messageInput.setHorizontalAlignment(SwingConstants.CENTER);

        // window layout set
        this.setLayout(new BorderLayout());
        //set border each component
        this.add(heading,BorderLayout.NORTH);
        JScrollPane jScrollPane = new JScrollPane(messageArea); //provide scrollber for big cheat
        this.add(jScrollPane,BorderLayout.CENTER);
        this.add(messageInput,BorderLayout.SOUTH);
      }

      private void handleEvent() {
        
         //taget message input 
         messageInput.addKeyListener(new KeyListener() {
       
          @Override
          public void keyTyped(KeyEvent e){
          }
          @Override
          public void keyPressed(KeyEvent e){  
          }

          @Override
          public void keyReleased(KeyEvent e){
            //System.out.println("key Released ="+e.getKeyCode()); key relesed it key code
            if(e.getKeyCode()==10){  //Enter key keycode 10
              String contentToSend = messageInput.getText();  //textfield whatever write to get 
              messageArea.append("ME : "+contentToSend+"\n");
              out.println(contentToSend); //send message
              out.flush();
              messageInput.setText("");
              messageInput.requestFocus();
            }
          }
         });
      }






       public void startReading()
       {
          // thread - read continue data
          Runnable r1=()->{
            System.out.println("reader startted...");
            try{
            while(true){
             
              String msg= br.readLine();
              if(msg.equals("close")){
                  System.out.println("Server terminate Cheat...!");
                  JOptionPane.showMessageDialog(this, "SERVER TERMINATE...");
                  //Server ternination textarea disable
                  messageInput.setEnabled(false);
                  socket.close();
                  break;
              }
              // print GUI Message area 
              messageArea.append("Client :"+ msg+"\n");
          }
          }catch(Exception e){
        }
          };
          new Thread(r1).start(); 
  
       }
       public void startWriting()
       {
          // thread - write continue data
          Runnable r2=()->{
              System.out.println("writer started..");
              try {
              while(!socket.isClosed()){
         
                  //tacke input
                  BufferedReader br1 = new BufferedReader(new InputStreamReader(System.in));
                  String content = br1.readLine();
                  out.println(content);
                  out.flush();

                  if(content.equals("close")){
                    socket.close();
                    break;
                  }
                  
              } 
           }catch (Exception e) {
            System.out.println("connection is closed....");
        }
  
          };
          //start thread
          new Thread(r2).start(); 
       }
  

       public static void main(String[] args) {
        System.out.println("this is client...");
        new ClientCheat();
       }
}
