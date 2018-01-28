package multiserwer;


import java.io.*;
import java.net.*;
import java.util.*;

public class MultiSerwer {
    
    Vector<WatekKlienta> v = new Vector<WatekKlienta>();
    
    /**
     * metoda tworząca gniazdo serwera i nasłuchująca nowe połączenia
     * @throws IOException 
     */
    void startSerwer() throws IOException{
        ServerSocket server = new ServerSocket(4444);
        System.out.println("Serwer wystartował");
        while(true){
            Socket socket = server.accept();
            System.out.println("Jest klient");
            WatekKlienta watek = new WatekKlienta(socket);
            v.addElement(watek);
        }
    }
    
    /**
     * metoda sprawdzająca czy istnieje użytkownik o podanej nazwie
     * @param name nazwa do sprawdzenia
     * @return czy istnieje
     */
    public boolean czyIstnieje(String name){
            for(int i=0;i<v.size();i++){
                WatekKlienta tmp = v.elementAt(i);
                if(name.equals(tmp.getMyName()))
                    return true;
            }
            return false;
        }
    
    /**
     * metoda wyszukująca użytkownika o podanej nazwie
     * @param name nazwa do wyszukania
     * @return poszukiwany użytkownik
     */
    public WatekKlienta znajdzPoNazwie(String name){
        WatekKlienta t;
        for(int i=0;i<v.size();i++){
            t = v.elementAt(i);
            if(t.getMyName().equals(name))
            return t;
        }
        return null;
    }
    
    /**
     * główna metoda programu
     * @param args
     * @throws IOException 
     */
    public static void main(String[] args) throws IOException {
        new MultiSerwer().startSerwer();
    }
    
    /**
     * klasa wewnętrzna wątku klienta
     */
    class WatekKlienta extends Thread{
        
        Socket socket;
        
        InputStream is;
        OutputStream os;
        
        WatekKlienta przeciwnik = null;
        
        String mojaNazwa = null;
        boolean wolny = true;
        
        /**
         * metoda ustawiająca nezwe użutkownikowi
         * @param nazwa nazwa do ustawienia
         */
        public void ustawNazwe(String nazwa){
            mojaNazwa = nazwa;
        }
        
        /**
         * metoda zwracająca nazwę użytkownika
         * @return nazwa użytkownika
         */
        public String getMyName(){
            return mojaNazwa;
        }
        
        /**
         * metoda ustawiająca status użytkownikowi
         * @param status status do ustawienia
         */
        public void ustawStatus(boolean status){
            wolny = status;
        }
        
        /**
         * metoda sprawdzająca czy jest zdolny rozpocząć grę
         * @return wynik sprawdzenia
         */
        public boolean czyWolny(){
            return wolny;
        }
        
        /**
         * metoda wysyłająca komunikat do użytkownika
         * @param typ wyp komunikatu
         * @param dane treść lub dane
         * @throws IOException 
         */
        public void send(int typ, String dane) throws IOException{
            os.write((typ+dane+(char)3).getBytes());  //3 w ascii to End Of Text
            //out.println(typ+dane);
        }
        
        /**
         * metoda sprawdzająca czy użytkownik ma przeciwnika
         * @return wynik sprawdzenia
         */
        public boolean maPrzeciwnika(){
            if(przeciwnik != null)
                return true;
            else
                return false;
        }
        
        /**
         * metoda ustawiająca przeciwnika użytkownikowi
         * @param przeciwnik przeciwnik do ustawienia
         */
        public void setPrzeciwnik(WatekKlienta przeciwnik){
            this.przeciwnik = przeciwnik;
        }
        
        /**
         * metoda usuwająca wątek użytkownika z wektora
         * @throws IOException 
         */
        public void usunMnie() throws IOException{
            synchronized(v){
                v.remove(WatekKlienta.this);
            }
        }
        
        /**
         * metoda tworząca listę użytkowników zdolnych do rozpoczęciagry
         * @return lista
         */
        public StringBuffer tworzListe(){
        StringBuffer lista = new StringBuffer();
        for (int i = 0; i < v.size(); i++) {
            WatekKlienta w = v.elementAt(i);
            if(w.getMyName() != null && w.wolny == true){
                if(lista.length()>0)
                    lista.append(Komunikat.separator);
                lista.append(w.getMyName());
            }
        }
        return lista;
    }
        
        /**
         * 
         * @param socket nowe gniazdo dla wątku klienta
         * @throws IOException 
         */
        WatekKlienta(Socket socket) throws IOException{
            this.socket = socket;
            
            is = socket.getInputStream();
            os = socket.getOutputStream();
            
            ustawNazwe(null);
            ustawStatus(true);
            start();
        }
        
        /**
         * metoda obsługująca działanie wątku klienta
         */
        public void run(){
            try{
            while(true){
                String dane="";
                int k;
                while((k=is.read())!=3)  // (char)3 = End Of Text
                   dane = dane + (char)k;
                int typ = (int)dane.charAt(0)-48; //48 to 0 w ASCI
                dane = dane.substring(1);
                
                if(typ == Komunikat.name){
                    if(czyIstnieje(dane))
                        send(Komunikat.comment,"Nazwa jest zajeta");
                    else{
                        ustawNazwe(dane);
                        System.out.println("nazwa : "+mojaNazwa);
                        send(Komunikat.name,getMyName());
                    }
                }else if(typ == Komunikat.connect){
                WatekKlienta p = znajdzPoNazwie(dane);
                if(p!=null && !p.maPrzeciwnika()){
                    p.send(Komunikat.connect, getMyName());
                }
                else
                    send(Komunikat.comment,"Nie ma gracza o takiej nazwie lub jest zajety");
            }else if(typ == Komunikat.shot){
                if(maPrzeciwnika()==true)
                przeciwnik.send(Komunikat.shot,dane);
            }else if(typ == Komunikat.wynikStrzalu){
                if(maPrzeciwnika()==true)
                    przeciwnik.send(Komunikat.wynikStrzalu, dane);
            }else if(typ == Komunikat.userList){
                send(Komunikat.userList,tworzListe().toString());
            }else if(typ == Komunikat.disconnect){
                przeciwnik.send(Komunikat.disconnect,dane);
                przeciwnik.ustawStatus(true);
                przeciwnik.setPrzeciwnik(null);
                ustawStatus(true);
                setPrzeciwnik(null);
            }else if(typ == Komunikat.tak){
                WatekKlienta p = znajdzPoNazwie(dane);
                if(p!=null && !p.maPrzeciwnika()){
                    setPrzeciwnik(p);
                    ustawStatus(false);
                    przeciwnik.setPrzeciwnik(this);
                    przeciwnik.ustawStatus(false);
                    p.send(Komunikat.tak,getMyName());
                }
            }else if(typ == Komunikat.nie){
                WatekKlienta p = znajdzPoNazwie(dane);
                if(p!=null)
                    p.send(Komunikat.nie,"");
            }else if(typ == Komunikat.exit){
                if(maPrzeciwnika()){
                przeciwnik.send(Komunikat.disconnect,"");
                przeciwnik.ustawStatus(true);
                przeciwnik.setPrzeciwnik(null);
                }
                ustawStatus(true);
                setPrzeciwnik(null);
                is.close();
                os.close();
                socket.close();
                usunMnie();
            }else
                send(Komunikat.comment,"Bledny typ wiadomosci");
            }
            }catch(IOException e){
                try {
                    usunMnie();
                    is.close();
                    os.close();
                    socket.close();
                } catch (IOException ex) {
                    System.out.println(ex); 
                }
            }
        }
        
        
    }
}
