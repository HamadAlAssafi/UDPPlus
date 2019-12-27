import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.*;
import java.security.SecureRandom;

public class client {
    


     public static void main(String[] args)throws Exception {
      
         SecureRandom sr = new SecureRandom();
        BufferedReader inFromUser= new BufferedReader(new InputStreamReader(System.in));

         
         int ClientPort = (1024 + sr.nextInt(9999) ); // Range from 1024 -> 9999
        DatagramSocket clientSocket = new DatagramSocket(ClientPort);
        InetAddress IPAddress = InetAddress.getByName("localhost");//returns IP address for the localhost
        byte[] sendData = new byte[1024];
        byte[] receiveData = new byte[1024];
        // Start for  Connection oriented feature , two way handshaking
        //Open connection and send for server
            String SYNC = "SYN";
            sendData = SYNC.getBytes();
           
            DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, IPAddress, 9152);
            clientSocket.send(sendPacket);

            DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
                    
            //receive 
            clientSocket.receive(receivePacket);
            
            String recieved = new String(receivePacket.getData());
            int maxRequests = Integer.parseInt(recieved.trim());
            //if server is busy
            if (recieved.trim().equals("-1")) {
                System.out.println(recieved + "\nServer is busy.");
                clientSocket.close();
            }
            //if server is available
            //send accont name
            else{
              System.out.println("Server is available.\ncan accepet " + recieved +" Message(s)\n");
            

        while (true ) {
            receiveData = new byte[1024];
            String accName;
            System.out.print("Account Name :");
            accName=inFromUser.readLine();
            System.out.println(accName);
            
            //Security Reverse
            accName=reverse(accName);
            sendData = accName.toString().getBytes();
            
            sendPacket = new DatagramPacket(sendData, sendData.length, IPAddress, 9152);
           
           //timeout for message
            clientSocket.setSoTimeout(30);
            clientSocket.send(sendPacket);

            receivePacket = new DatagramPacket(receiveData, receiveData.length);

            // Reliability 
            boolean recieve = true;
            while (recieve) 
            {
                try {
                    clientSocket.receive(receivePacket);
                    
                }catch (SocketTimeoutException e) {
                    System.out.println("Timeout\n Retransmitting message");
                    clientSocket.send(sendPacket);
                    continue;
                }
                recieve = false;
                System.out.println("Message recieved\n");
            }
            
            //recieve accont no
            recieved = new String(receivePacket.getData());
            // Decryption
            String number = reverse(recieved);
            System.out.println("Your accont No : " + number);
            
            maxRequests--;
            if( maxRequests == 0)
            {
                System.out.println("Disconnected \"You exceeded number of messages\"");
                break;
            }
                        //if user wants to close connection
            System.out.println("Type: \"Exit\" to close connection");
            SYNC = inFromUser.readLine();
            
            if (SYNC.equalsIgnoreCase("Exit")) 
            {
                System.out.println("Disconnected \"Terminated by user\"");
                clientSocket.close();
                break;
            }
            

        }
            }
    }

    //Method for Security 
    public static String reverse(String a) {
        String b = "";
        char[] x = a.toCharArray();
        for (int i = x.length - 1; i >= 0; i--) 
        {
            b += x[i];
        }
         
        return b;
    }

  
}
