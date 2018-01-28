package projekt;


import java.net.*;


public class Klient {
    private String host;
    private int port;
    private Socket socket;
    private Połączenie polaczenie = null;
    
    public Klient(String host, int port){
        this.host=host;
        this.port=port;
    }
    
    /**
     * metoda negocjująca połączenie TCP z serwem, tworząca nowe gniazdo
     * i nowey wątek nasłuchujący
     * @return zwraca wynik próby połączenia z serwerem
     */
    public boolean start(){
        try{
            socket = new Socket(host,port);
        }catch(Exception ex){
            return false;
        }
        polaczenie = new Połączenie(socket);
        polaczenie.start();
        return true;
    }
    
    /**
     * metoda wysyłająca komunikat do serwera
     * @param k komunikat do wysłania
     */
    public void wyslijWiadomosc(Komunikat k){
        polaczenie.wyslij(k.wiadomosc());
    }
    
    /*
     * metoda po odebraniu komunikatu od serwera tworzy nowy komunikat
     */
    public Komunikat odbierzWiadomosc(){
        if(polaczenie.message.isEmpty())
            return null;
        else {
            Komunikat k = new Komunikat((String)polaczenie.message.getFirst());
            polaczenie.message.removeFirst();
            return k;
        }
    }
    
}
