package projekt;


import java.awt.*;
import java.awt.event.*;
import java.util.concurrent.TimeUnit;
import javax.swing.*;


public class Statki extends JFrame{
    
    public String serverID = null;
    public int port = 0;
    public String nazwa = null;
    String[] gracze = null;
    boolean czyZajety = false;
    boolean mojaTura = false;
    String nazwaPrzeciwnika = null;
    int statki = 15;
    
    Klient klient;
    
    private JDialog daneSerwera = null;
    private JLabel L_adresSerwera = null;
    private JLabel L_port = null;
    private JTextField T_adresSerwera = null;
    private JTextField T_port = null;
    private JButton okej = null;
    private JButton anuluj = null;
    private JButton polacz = null;
    private JButton wyjscie = null;
    private JLabel L_nazwa = null;
    private JTextField T_nazwa = null;
    private JButton ustawNazwe = null;
    private JDialog nowaNazwa = null;
    private JTextField T_nowaNazwa = null;
    private JButton Lista = null;
    private JDialog listaGraczy = null;
    private List list = null;
    private JButton connect = null;
    private JButton disconnect = null;
    private Plansza planszaGracza = null;
    private Plansza planszaPrzeciwnika = null;
    private JLabel comments = null;
    private JLabel name = null;
    private JDialog czyPolaczyc = null;
    private JButton tak = null;
    private JButton nie = null;
    private JLabel L_czyPolaczyc = null;
    private JButton losuj = null;
    private JLabel L_stMoje = null;
    
    public Statki(){
        setSize(700,500);
        setLocationRelativeTo(null);
        setTitle("Statki");
        setLayout(null);
        
        add(getpolacz(),null);
        add(getwyjscie(),null);
        add(getustawNazwe(),null);
        add(getLista(),null);
        add(getdisconnect(),null);
        add(getplanszaGracza(),null);
        add(getplanszaPrzeciwnika(),null);
        add(getcomments(),null);
        add(getname(),null);
        add(getlosuj(),null);
        add(getL_stMoje(),null);
        
       
       setVisible(true);
       setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
       getdaneSerwera();
       }
    
    /**
     * metoda wysyłająca komunikat do serwera
     * @param k komunikat do wysłania
     */
    
    public void send(Komunikat k){
        klient.wyslijWiadomosc(k);
    }
    
    /**
     * metoda tworząca etykietę wyswietlajacą liczbę statków użytkownika
     * @return etykieta
     */
    private JLabel getL_stMoje(){
        if(L_stMoje==null){
            L_stMoje = new JLabel("Twoje statki "+statki+"/15");
            L_stMoje.setBounds(30, 10, 120, 20);
        }
        return L_stMoje;
    }
    
    /**
     * metoda tworząca przycisk do losowego ustawiania statków
     * @return przycisk
     */
    private JButton getlosuj(){
        if(losuj==null){
            losuj = new JButton("Losuj");
            losuj.setBounds(30, 400, 100, 40);
            
            losuj.addActionListener(new ActionListener() {

                @Override
                public void actionPerformed(ActionEvent e) {
                    planszaGracza.rozkladLosowy();
                    }
            });
        }
        return losuj;
    }
    
    /**
     * metoda tworząca etykietę wyświetlającą nazwe
     * @return etykieta
     */
    private JLabel getname(){
        if(name==null){
            name = new JLabel();
            name.setBounds(560, 410, 80, 40);
        }
        return name;
    }
    
    /**
     * metoda tworząca planszę gracza
     * @return plansza gracza
     */
    private Plansza getplanszaGracza(){
        if(planszaGracza==null){
            planszaGracza = new Plansza(RodzajeGraczy.GRACZ);
            planszaGracza.setBounds(30, 30, 300, 300);
        }
        return planszaGracza;
    }
    
    /**
     * metoda tworząca planszę przeciwnika zawierająca obsługę myszki
     * @return plansza przeciwnika
     */
    private Plansza getplanszaPrzeciwnika(){
        if(planszaPrzeciwnika==null){
            planszaPrzeciwnika = new Plansza(RodzajeGraczy.PRZECIWNIK);
            planszaPrzeciwnika.setBounds(360, 30, 300, 300);
            planszaPrzeciwnika.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
                            if(e.getButton() == MouseEvent.BUTTON1 &&
                                    planszaPrzeciwnika.getWlasciciel()==RodzajeGraczy.PRZECIWNIK && mojaTura == true){
                                Point p = e.getPoint();
                                int x = p.x / 20;
                                int y = p.y / 20;
                                if(planszaPrzeciwnika.getTablica()[x][y] == ElementyPlanszy.POLE_PUSTE && 
                                        klient!=null && czyZajety == true){
                                    send(new Komunikat(Integer.toString(Komunikat.shot)+x+Komunikat.separator+y));
                                }
                                    
                            }
                        }
        });
    }
        return planszaPrzeciwnika;
    }
    
    /**
     * metoda tworząca okno dialogowe umożliwiające nawiązanie
     * połączenia z przeciwnikiem lub jego odrzucenie
     * @return okno akceptacji połączenia
     */
    private JDialog getczyPolaczyc(){
        if(czyPolaczyc==null){
            czyPolaczyc = new JDialog(this,"CZY POŁĄCZYĆ",true);
            czyPolaczyc.setSize(300, 200);
            czyPolaczyc.setLayout(null);
            czyPolaczyc.setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
            czyPolaczyc.setLocationRelativeTo(this);
            czyPolaczyc.add(getL_czyPolaczyc(),null);
            czyPolaczyc.add(gettak(),null);
            czyPolaczyc.add(getnie(),null);
        }
        return czyPolaczyc;
    }
    
    /**
     * metoda tworząca etykietę w oknie akceptacji połączenia
     * @return etykieta
     */
    private JLabel getL_czyPolaczyc(){
        if(L_czyPolaczyc==null){
            L_czyPolaczyc = new JLabel("",JLabel.CENTER);
            L_czyPolaczyc.setBounds(30, 30, 240, 20);
        }
        return L_czyPolaczyc;
    }
    
    /**
     * metoda tworząca przycisk do akceptacji połączenia z przeciwnikiem
     * @return przycisk tak
     */
    private JButton gettak(){
        if(tak==null){
            tak = new JButton("Tak");
            tak.setBounds(30, 90, 100, 40);
            
            tak.addActionListener(new ActionListener() {

                @Override
                public void actionPerformed(ActionEvent e) {
                    if(klient!=null){
                        send(new Komunikat(Integer.toString(Komunikat.tak)+nazwaPrzeciwnika));
                        czyZajety = true;
                        if(connect!=null)
                        connect.setEnabled(false);
                        disconnect.setEnabled(true);
                        planszaPrzeciwnika.zerowanieTablicy();
                        losuj.setEnabled(false);
                    }
                    czyPolaczyc.dispose();
                }
            });
        }
        return tak;
    }
    
    /**
     * metoda tworząca przycisk do odrzucenia połączenia z przeciwnikiem
     * @return przycisk nie
     */
    private JButton getnie(){
        if(nie==null){
            nie = new JButton("Nie");
            nie.setBounds(150, 90, 100, 40);
            
            nie.addActionListener(new ActionListener() {

                @Override
                public void actionPerformed(ActionEvent e) {
                    if(klient!=null){
                        send(new Komunikat(Integer.toString(Komunikat.nie)+nazwaPrzeciwnika));
                    }
                    nazwaPrzeciwnika = null;
                    czyPolaczyc.dispose();
                }
            });
        }
        return nie;
    }
   
    /**
     * metoda tworząca przycisk do połączenia z serwerem
     * @return przycisk połącz
     */
    private JButton getpolacz(){
        if(polacz==null){
            polacz = new JButton("Połącz");
            polacz.setBounds(30, 350, 100, 40);
            polacz.addActionListener(new ActionListener() {

                @Override
                public void actionPerformed(ActionEvent e) {
                        try{
                        klient = new Klient(serverID,port);
                        klient.start();
                        nasluch();
                        send(new Komunikat(Komunikat.name+nazwa));
                        polacz.setEnabled(false);
                        }catch(Exception ex){
                            klient = null;
                            polacz.setEnabled(true);
                            getdaneSerwera();
                        }
                }
            });
        }
        return polacz;
    }
    
    /**
     * metoda tworząca przycisk wyjścia z aplikacji
     * @return przycisk wyjście
     */
    private JButton getwyjscie(){
        if(wyjscie==null){
            wyjscie = new JButton("Wyjście");
            wyjscie.setBounds(550, 350, 100, 40);
            wyjscie.addActionListener(new ActionListener() {

                @Override
                public void actionPerformed(ActionEvent e) {
                    if(klient!=null)
                    send(new Komunikat(Integer.toString(Komunikat.exit)));
                    System.exit(0);
                }
            });
        }
        return wyjscie;
        
    }
    
    /**
     * metoda tworząca przycisk do ustawienia lub zmiany nazwy
     * @return przycisk zmien nazwe
     */
    private JButton getustawNazwe(){
        if(ustawNazwe == null){
            ustawNazwe = new JButton("Zm. nazwę");
            ustawNazwe.setBounds(420, 350, 100, 40);
            ustawNazwe.addActionListener(new ActionListener() {

                public void actionPerformed(ActionEvent e) {
                    getnowaNazwa();
                }
            });
        }
        return ustawNazwe;
    }
    
    /**
     * metoda tworząca okno dialogowe w którym ustawiane są dane serwera
     * i nazwa użytkownika
     * @return okno dialogowe
     */
    private JDialog getdaneSerwera(){
        if(daneSerwera==null){
            daneSerwera = new JDialog(this,"Podaj dane serwera",true);
      
        daneSerwera.setSize(300,250);
        daneSerwera.setLocationRelativeTo(this);
        daneSerwera.setLayout(null);
        daneSerwera.setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        
        daneSerwera.add(getL_adresSerwera(),null);
        daneSerwera.add(getT_adresSerwera(),null);
        daneSerwera.add(getL_port(),null);
        daneSerwera.add(getT_port(),null);
        daneSerwera.add(getL_nazwa(),null);
        daneSerwera.add(getT_nazwa(),null);
        daneSerwera.add(getokej(),null);
        daneSerwera.add(getanuluj(),null);
        
        daneSerwera.setVisible(true);
        }else
        daneSerwera.setVisible(true);
        return daneSerwera;
    }
    
    /**
     * metoda tworząca etykietę w której wyswietlane są komentarze
     * @return etykieta
     */
    private JLabel getcomments(){
        if(comments==null){
            comments = new JLabel();
            comments.setBounds(160, 410, 540, 40);
        }
        return comments;
    }
    
    /**
     * metoda tworząca okno dialogowe do ustawienia lub zmiany nazwy
     * @return okno dialogowe zmiany nazwy
     */
    private JDialog getnowaNazwa(){
            nowaNazwa = new JDialog(this,"PODAJ NAZWĘ",true);
            nowaNazwa.setLayout(null);
            nowaNazwa.setSize(200,100);
            nowaNazwa.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
            nowaNazwa.setLocationRelativeTo(this);
            nowaNazwa.add(getT_nowaNazwa(),null);
            nowaNazwa.setVisible(true);
        return nowaNazwa;
    }
    
    /**
     * metoda tworząca pole tekstowe do wpisania nowej nazwy
     * @return pole tekstowe
     */
    private JTextField getT_nowaNazwa(){
        if(T_nowaNazwa==null){
            T_nowaNazwa = new JTextField();
            T_nowaNazwa.setBounds(15, 20, 160, 20);
            T_nowaNazwa.addActionListener(new ActionListener() {

                @Override
                public void actionPerformed(ActionEvent e) {
                    if(T_nowaNazwa.getText().length()>0){
                        if(klient!=null)
                        send(new Komunikat(Komunikat.name+T_nowaNazwa.getText()));
                        T_nowaNazwa.setText(null);
                        nowaNazwa.dispose();
                    }
                }
            });
        }
        return T_nowaNazwa;
    }
    
    /**
     * metoda tworząca przycisk do pobrania listy użytkowników z serwera
     * @return przycisk lista
     */
    private JButton getLista(){
        if(Lista==null){
            Lista = new JButton("Lista");
            Lista.setBounds(290, 350, 100, 40);
            Lista.addActionListener(new ActionListener() {

                @Override
                public void actionPerformed(ActionEvent e) {
                    if(klient!=null)
                        send(new Komunikat(Integer.toString(Komunikat.userList)));
                    getlistaGraczy();
                }
            });
        }
        return Lista;
    }
    
    /**
     * metoda tworząca okno dialogowe z listągraczy
     * @return okno dialogowe z listą
     */
    private JDialog getlistaGraczy(){
            listaGraczy = new JDialog(this,"LISTA GRACZY",true);
            listaGraczy.setLayout(null);
            listaGraczy.setSize(200, 275);
            listaGraczy.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
            listaGraczy.setLocationRelativeTo(this);
            listaGraczy.add(getList(),null);
            listaGraczy.add(getconnect(),null);
        return listaGraczy;
    }
    
    /**
     * metoda tworząca listę w której wyświetlana jest lista graczy
     * @return lista graczy
     */
    private List getList(){
        if(list==null){
            list = new List();
            list.setBounds(15, 15, 160, 150);
        }
        return list;
    }
    
    /**
     * metoda tworząca przycisk powodujący połączenie z wybranym graczem
     * @return przycisk połącz
     */
    private JButton getconnect(){
        if(connect==null){
            connect = new JButton("POŁĄCZ");
            connect.setBounds(15,180,160,40);
            if(czyZajety == true)
                connect.setEnabled(false);
            connect.addActionListener(new ActionListener() {

                @Override
                public void actionPerformed(ActionEvent e) {
                    String s = list.getSelectedItem();
                    if(klient!=null && s!=null){
                        send(new Komunikat(Komunikat.connect+s));
                    listaGraczy.dispose();
                    }
                }
            });
        }
        return connect;
    }
    
    /**
     * metoda tworząca przycisk pozwalający na rozłączenie z graczem
     * i przerwanie gry
     * @return przycisk rozłącz
     */
    private JButton getdisconnect(){
        if(disconnect==null){
            disconnect = new JButton("Rzłącz");
            disconnect.setEnabled(false);
            disconnect.setBounds(160, 350, 100, 40);
            disconnect.addActionListener(new ActionListener() {

                @Override
                public void actionPerformed(ActionEvent e) {
                    if(klient!=null){
                        send(new Komunikat(Integer.toString(Komunikat.disconnect)));
                        if(connect!=null)
                        connect.setEnabled(true);
                        disconnect.setEnabled(false);
                        planszaGracza.rozkladLosowy();
                        losuj.setEnabled(true);
                        statki=15;
                        L_stMoje.setText("Twoje statki "+statki+"/15");
                        czyZajety=false;
                    }
                }
            });
        }
        return disconnect;
    }
  
    /**
     * metoda tworząca etykietę w oknie danych serwera
     * @return etykieta 
     */
     private JLabel getL_nazwa(){
        if(L_nazwa==null){
            L_nazwa = new JLabel("Twoja nazwa",JLabel.RIGHT);
            L_nazwa.setBounds(5, 100, 105, 20);
        }
        return L_nazwa;
    }
    
     /**
      * metoda tworząca pole tekstowe w oknie danych serwera
      * @return pole tekstowe 
      */
    private JTextField getT_nazwa(){
        if(T_nazwa==null){
            T_nazwa = new JTextField();
            T_nazwa.setBounds(120, 100, 120, 20);
        }
        return T_nazwa;
    }
    
    /**
     * metoda tworząca etykietę w oknie danych serwera
     * @return etykieta
     */
    private JLabel getL_adresSerwera(){
        if(L_adresSerwera == null){
            L_adresSerwera = new JLabel("Adres serwera",JLabel.RIGHT);
            L_adresSerwera.setBounds(5, 20, 105, 20);
        }
        return L_adresSerwera;
    }
    
    /**
     * metoda tworząca pole tekstowe w oknie danych serwera
     * @return pole tekstowe
     */
    private JTextField getT_adresSerwera(){
        if(T_adresSerwera == null){
            T_adresSerwera = new JTextField(10);
            T_adresSerwera.setBounds(120, 20, 120, 20);
            T_adresSerwera.setText("localhost");
        }
        return T_adresSerwera;
    }
    
    /**
     * metoda tworząca etykietę w oknie danych serwera
     * @return etykieta
     */
    private JLabel getL_port(){
        if(L_port == null){
            L_port = new JLabel("Port",JLabel.RIGHT);
            L_port.setBounds(5, 60, 105, 20);
        }
        return L_port;
    }
    
    /**
     * metoda tworząca pole tekstowe w oknie danych serwera
     * @return pole tekstowe
     */
    private JTextField getT_port(){
        if(T_port == null){
            T_port = new JTextField(10);
            T_port.setBounds(120, 60, 120, 20);
            T_port.setText("4444");
        }
        return T_port;
    }
    
    /**
     * metoda tworząca przycisk ustawiający dane hosta i nazwę użytkownika
     * @return przycisk okej
     */
    private JButton getokej(){
        if(okej == null){
            okej = new JButton("OK");
            okej.setBounds(30, 140, 100, 40);
            
            okej.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                if(T_adresSerwera.getText().length()>0 && T_port.getText().length()>0 && T_nazwa.getText().length()>0){
                nazwa = T_nazwa.getText();
                serverID = T_adresSerwera.getText();
                String tmp = "";
                for(int i=0;i<T_port.getText().length();i++){
                    if(Character.isDigit(T_port.getText().charAt(i)))
                        tmp+=T_port.getText().charAt(i);
                }
                port = Integer.parseInt(tmp); 
                        
                daneSerwera.dispose();
                }
                }
            });
        }
        return okej;
    }
    
    /**
     * metoda tworząca przycisk do anulowania wprowadzenia danych serwera
     * i kończąca działanie aplikacji
     * @return przycisk anuluj
     */
    private JButton getanuluj(){
        if(anuluj == null){
            anuluj = new JButton("ANULUJ");
            anuluj.setBounds(140, 140, 100, 40);
            
            anuluj.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                System.exit(0);   
                }
            });
        }
        return anuluj;
    }
    
    /**
     * metoda tworząca wątek który ma za zadanie odbierać komunikaty od serwera
     * i wykonywać różne operacje zależnie od typu komunikatu
     */
    public void nasluch(){
        new Thread(){
            public void run(){
                Komunikat k;
                while(true){
                    try {
                        TimeUnit.MILLISECONDS.sleep(100);
                    } catch (InterruptedException ex) {
                      JOptionPane.showMessageDialog(Statki.this, ex, "Błąd", ERROR);
                    }
        while(klient != null && (k = klient.odbierzWiadomosc()) != null){
            switch(k.getTyp()){
                case Komunikat.comment:
                    comments.setText(k.getKomunikat());
                    break;
                case Komunikat.name:
                    nazwa = k.getKomunikat();
                    name.setText(nazwa);
                    comments.setText("Nazwa "+nazwa+" zaakceptowana");
                    break;
                case Komunikat.userList:
                    if(list!=null)
                    list.clear();
                    gracze = null;
                    gracze = k.getKomunikat().split(Komunikat.separator);
                    int j=0;
                    for(int i=0;i<gracze.length;i++){
                        if(!gracze[i].equals(nazwa)){
                        list.add(gracze[i], j);
                        j++;
                        }
                    }
                    listaGraczy.setVisible(true);
                    break;
                case Komunikat.shot:
                    String[] strzal = k.getKomunikat().split(Komunikat.separator);
                    int x = Integer.parseInt(strzal[0]);
                    int y = Integer.parseInt(strzal[1]);
                    if(planszaGracza.getTablica()[x][y]==ElementyPlanszy.POLE_PUSTE
                            || planszaGracza.getTablica()[x][y]==ElementyPlanszy.STATEK){
                    planszaGracza.zaznaczStrzal(x,y, planszaGracza.sprawdzStrzal(x,y));
                    if(planszaGracza.sprawdzStrzal(x, y)==Plansza.WynikStrzalu.PUDLO){
                        mojaTura = true;
                        comments.setText("Twoja tura");
                    }
                    if(klient!=null)
                        send(new Komunikat(Integer.toString(Komunikat.wynikStrzalu)+
                                x+"#"+y+"#"+planszaGracza.sprawdzStrzal(x, y).toString()));
                    }
                    
                    if(planszaGracza.sprawdzStrzal(x, y)==Plansza.WynikStrzalu.TRAFIONY_ZATOPIONY){
                        statki--;
                        L_stMoje.setText("Twoje statki "+statki+"/15");
                        if(statki==0)
                            if(klient!=null){
                                send(new Komunikat(Integer.toString(Komunikat.disconnect)+Integer.toString(statki)));
                                comments.setText(null);
                                JOptionPane.showMessageDialog(Statki.this,"              PRZEGRAŁEŚ");
                                planszaGracza.rozkladLosowy();
                             disconnect.setEnabled(false);
                             losuj.setEnabled(true);
                             if(connect!=null)
                                 connect.setEnabled(true);
                             statki=15;
                             L_stMoje.setText("Twoje statki "+statki+"/15");
                            }
                    }
                    break;
                case Komunikat.wynikStrzalu:
                    String [] wynik = k.getKomunikat().split(Komunikat.separator);
                    int xx = Integer.parseInt(wynik[0]);
                    int yy = Integer.parseInt(wynik[1]);
                    String s = wynik[2];
                    if(s.equals("PUDLO")){
                        planszaPrzeciwnika.zaznaczStrzal(xx, yy, Plansza.WynikStrzalu.PUDLO);
                        mojaTura = false;
                        comments.setText(null);
                    }
                    else if(s.equals("TRAFIONY"))
                        planszaPrzeciwnika.zaznaczStrzal(xx, yy, Plansza.WynikStrzalu.TRAFIONY);
                    else if(s.equals("TRAFIONY_ZATOPIONY"))
                        planszaPrzeciwnika.zaznaczStrzal(xx, yy, Plansza.WynikStrzalu.TRAFIONY_ZATOPIONY);
                    break;
                case Komunikat.connect:
                    nazwaPrzeciwnika = k.getKomunikat();
                    getczyPolaczyc();
                    L_czyPolaczyc.setText(nazwaPrzeciwnika+" chce z tobą zagrać. Połączyć?");
                    czyPolaczyc.setVisible(true);
                    break;
                case Komunikat.disconnect:
                    if(k.getKomunikat().equals("0")){
                        comments.setText(null);
                        JOptionPane.showMessageDialog(Statki.this,"              WYGRAŁEŚ");
                    }
                    else{
                        comments.setText(null);
                    JOptionPane.showMessageDialog(Statki.this,"     Przeciwnik się rozłączył");
                    }
                    czyZajety = false;
                    if(connect!=null)
                    connect.setEnabled(true);
                    disconnect.setEnabled(false);
                    losuj.setEnabled(true);
                    planszaGracza.rozkladLosowy();
                    statki=15;
                    L_stMoje.setText("Twoje statki "+statki+"/15");
                    break;
                case Komunikat.tak:
                    mojaTura = true;
                    czyZajety = true;
                    if(connect!=null)
                    connect.setEnabled(false);
                    disconnect.setEnabled(true);
                    comments.setText("Połączono pomyślnie z "+k.getKomunikat()+". Twój ruch");
                    losuj.setEnabled(false);
                    planszaPrzeciwnika.zerowanieTablicy();
                    break;
                case Komunikat.nie:
                    comments.setText("Odmowa połączenia");
                    break;
                default:
                    comments.setText("Nieznany komunikat : "+k.getKomunikat()+" ## typu : "+k.getTyp());
            }
            }
            }
            }
        }.start();
       
    }
    
    /**
     * metoda główna programu
     * @param args 
     */
    public static void main(String[] args) {
       new Statki();
    }
    
}
