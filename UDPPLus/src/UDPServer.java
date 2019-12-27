import java.net.*;
import java.security.SecureRandom;


public class UDPServer {

     public static void main(String[] args)throws Exception {
         
       
        SecureRandom sr = new SecureRandom();

        DatagramSocket serverSocket = new DatagramSocket(9152);
        byte[] receiveData = new byte[1024];
        byte[] sendData = new byte[1024];

        Linkedlist list = new Linkedlist();
        list.add("Abdullah Ali", "15324");
        list.add("Manal Abdullah", "90781");
        list.add("Henry Markos", "88125");
        list.add("Hisham Mansoor", "62044");
        list.add("Asma Awal", "71825");
        list.add("Osama Ahmed", "12818");
        list.add("Alice Tarkood", "29502");
        list.add("Mohammed Khalid", "19012");
        
       // Start for  Connection oriented feature , two way handshaking
        DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
        
            serverSocket.receive(receivePacket);
            String SYNC = new String(receivePacket.getData());
            InetAddress IPAddress = receivePacket.getAddress();
            int port = receivePacket.getPort();
            if (SYNC.contains("SYN")) 
            {

                //ack from server to client
                //maximum number of messages the server can handle
                
                int maxNum = 1+sr.nextInt(10); // we supposed that our server can only handle up to 8 messages at the same time 
                double busy = maxNum * 0.20;
                int msgAvailable = (int) (maxNum - busy);
                if (msgAvailable <= busy) {
                    //server is busy
                    String failed = "-1";
                    sendData = failed.getBytes();

                } else {
                    //Connection accepted
                    String sucsess = Integer.toString(msgAvailable); // converted from integer to string so that we can convert it to bytes
                    sendData = sucsess.getBytes();
                }
                           
                //Accept connection or Reject
              
               
               DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, IPAddress, port);
                serverSocket.send(sendPacket);
            }
        while (true) {
            receiveData = new byte[1024];
            receivePacket = new DatagramPacket(receiveData, receiveData.length);
            serverSocket.receive(receivePacket);
            String name = new String(receivePacket.getData());
            //Security :
            //Decryption
            name = reverse(name);

            //search inside the linkedList

            String number=list.search(name);

            //Enryption for Message
            String encryptedNum = reverse(number);

            sendData = encryptedNum.getBytes();
            
          IPAddress = receivePacket.getAddress();
          port = receivePacket.getPort();
           DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, IPAddress, port);
            
            //send ACK or NACK
                int x= 1+ sr.nextInt(100);

                  if (x % 10 != 0) 
                  {
                     serverSocket.send(sendPacket);
                  }
                  else
                  {
                      System.out.println("Ack lost");
                  }
                  
        }

    }
     
    public static String reverse(String a) 
    {
        String b = "";
        char[] x = a.toCharArray();
        for (int i = x.length - 1; i >= 0; i--) 
        {
            b += x[i];
        }
        return b;
    }



}
