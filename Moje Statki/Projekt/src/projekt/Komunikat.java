package projekt;


public class Komunikat {
    
    public final static int name = 0;
    public final static int shot = 1;
    public final static int comment = 2;	
    public final static int userList = 3;
    public final static int wynikStrzalu = 4;
    public final static int tak = 5;
    public final static int nie = 6;		
    public final static int connect = 7;
    public final static int disconnect = 8;
    public final static int exit = 9;	
    public final static String  separator= "#";
    
    private int typ;
    private String komunikat;
    
    /**
     * 
     * @param odebranaWiadomosc cała trześc odebranego komunikatu
     */
    public Komunikat(String odebranaWiadomosc){
        int t = (int)odebranaWiadomosc.charAt(0)-48; //48 to 0 w ASCI
        String tresc = odebranaWiadomosc.substring(1);
        
        setTyp(t);
        setKomunikat(tresc);
    }
    
    /**
     * metoda tworząca komunikat gotowy do wysłania
     * @return komunikat do wysłania
     */
    public String wiadomosc(){
        String wiadomosc = typ+getKomunikat();
        return wiadomosc;
    }
    
    /**
     * metoda zwracająca tresc komunikatu (bez typu)
     * @return treść komunikatu
     */
    public String getKomunikat(){
        return komunikat;
    }
    
    /**
     * metoda ustawiająca treść komunikatu (bez typu)
     * @param kom komunikat 
     */
    public void setKomunikat(String kom){
        komunikat = kom;
    }
    
    /**
     * metoda ustawiajaca typ komunikatu
     * @param t typ komunikatu
     */
    public void setTyp(int t){
        typ = t;
    }
    
    /**
     * metoda pobierająca typ komunikatu
     * @return typ komunikatu
     */
    public int getTyp(){
        return typ;
    }
    
}
