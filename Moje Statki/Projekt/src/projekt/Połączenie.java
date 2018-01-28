package projekt;


import static java.awt.image.ImageObserver.ERROR;
import java.io.*;
import java.net.*;
import java.util.*;
import javax.swing.JOptionPane;

public class Połączenie extends Thread{
    private Socket socket;
    private OutputStream os;
    private InputStream is;
    public LinkedList<String> message;
    
    String[] lista;
   
    /**
     * 
     * @param socket gniazdo połączenia z serwerem
     */
    Połączenie(Socket socket) {
        try{
            os = socket.getOutputStream();
            is = socket.getInputStream();
        }catch(IOException e){
         JOptionPane.showMessageDialog(null, e, "Błąd", ERROR);   
        }
        message = new LinkedList<String>();
        this.socket=socket;
    }
    
    /**
     * metoda wysyłająca komunikat do serwera
     * @param wiadomosc treść komunikatu
     */
    public void wyslij(String wiadomosc){
        try{
        os.write((wiadomosc+(char)3).getBytes());
        System.out.println("WYSYŁAM> "+wiadomosc);
        }catch(IOException ex){
         JOptionPane.showMessageDialog(null, ex, "Błąd", ERROR);   
        }
    }
    
    /**
     * metoda obsługująca wątek do nasłuchiwania
     */
    public void run(){
        try{
        while(true){
            
            String dane = "";
            int k;
            while((k=is.read())!=3)
                dane = dane + (char)k;
            message.add(dane);
           }
        }catch(IOException e){
         JOptionPane.showMessageDialog(null, e, "Błąd", ERROR);  
        }
    }
   
}
