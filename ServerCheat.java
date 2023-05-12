import java.net.*;
import java.io.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
public class ServerCheat extends JFrame{


    //variable 
    ServerSocket server;
    Socket socket;
    BufferedReader br;    //for reading
    PrintWriter out;      //for writting

    //GUI Component
    private JLabel heading=new JLabel("Server Area");
    private JTextArea messageArea=new JTextArea();
    private JTextField messageInput = new JTextField();
    private Font font =new Font("Roboto",Font.PLAIN,20);



     //server constructor
     public ServerCheat(){

            try {
                server=new ServerSocket(4242);
                System.out.println("Server is ready to accept Connection");
                System.out.println("Watting....");
                socket=server.accept();
                br=new BufferedReader(new InputStreamReader(socket.getInputStream()));
                out=new PrintWriter(socket.getOutputStream());
                createGUI();
                handleEvent();
                startReading();
                //startWriting();

            } catch (Exception e) {
                e.printStackTrace();
            }

     }



     private void createGUI(){

        //window set
        this.setTitle("Client Messanger");
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
                System.out.println("Client terminate Cheat...!");
                JOptionPane.showMessageDialog(this, "CLIENT TERMINATE...");
                  //Server ternination textarea disable
                  messageInput.setEnabled(false);

                socket.close();
                break;
            }
             // print GUI Message area 
             messageArea.append("Server :"+ msg+"\n");
        }
        }
        catch(Exception e){
            System.out.println("connection is closed");
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
                out.flush();;
                
                if(content.equals("close")){
                    socket.close();
                    break;
                  }
            } 
         }catch (Exception e) {
            System.out.println("connection is closed...");
        }

        };
        //start thread
        new Thread(r2).start(); 
     }


    public static void main(String[] args) {
        System.out.println("i am server....going to start server");
                new ServerCheat();
    
    
    
    
    
    }
}