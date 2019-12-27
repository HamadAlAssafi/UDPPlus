import java.util.Scanner;

public class Linkedlist {

    node head;
    node current;

    
public void add(String name,String num)
{
     node news = new node( name,num);
     news.next = head; 
     head = news; 
      
}

public  String search(String name) 
{ 
       
    node current = head;    //Initialize current 
    while (current != null) 
    { 
       if (current.name.equalsIgnoreCase(name.trim()) ) 
       {
                return current.num;
       }    //data found 
       
            current = current.next; 
    } 
        return "0000";    //data not found 
} 
        
}

class node 
{
    String name;
    String num;
    node next;
   
    public node(String name,String num )
    {
        this.name=name;
        this.num=num;
    }
}
