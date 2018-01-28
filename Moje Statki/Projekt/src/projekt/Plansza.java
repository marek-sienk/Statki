package projekt;


import java.awt.Color;
import java.awt.Dimension;
import java.awt.Point;
import java.util.Random;
import javax.swing.JPanel;


public class Plansza extends JPanel{
    
    private enum kierunek{
        POZIOMO,
        PIONOWO
    }
    
    public enum WynikStrzalu{
        PUDLO,
	TRAFIONY,
	TRAFIONY_ZATOPIONY,
    }
    
    private ElementyPlanszy tablica[][] = new ElementyPlanszy[15][15];
    private JPanel[][] tab = new JPanel[15][15];
    
    private RodzajeGraczy wlasciciel;
    
    /*
     * metoda zwracająca właściciela planszy
     */
    public RodzajeGraczy getWlasciciel(){
        return wlasciciel;
    }
    
    /**
     * metoda zwracająca elenenty planszy
     * @return elementy planszy
     */
    public ElementyPlanszy[][] getTablica(){
        return tablica;
    }
    
    /**
     * metoda zerująca planszę
     */
    public void zerowanieTablicy(){
        for (int i = 0; i < tablica.length; i++) {
            for (int j = 0; j < tablica[i].length; j++) {
                tablica[i][j] = ElementyPlanszy.POLE_PUSTE;
                rysuj(i,j);
            }
        }
    }
    
    /**
     * metoda losowego rozstawienia statków
     */
    public void rozkladLosowy() {
		zerowanieTablicy();

		for (int i = 0; i < 15; i++) {
			int dlugosc;
			if (i < 1)
				dlugosc = 5;
			else if (i < 3)
				dlugosc = 4;
			else if (i < 6)
				dlugosc = 3;
			else if (i < 10)
				dlugosc = 2;
			else
				dlugosc = 1;

			boolean ok;
			Point goraLewo;
			Point dolPrawo;
			int x, y;
			kierunek k;

			do {

				Random r = new Random();
				k = (Math.abs(r.nextInt()) % 2 == 0) ? kierunek.PIONOWO
						: kierunek.POZIOMO;

				if (k == kierunek.POZIOMO) {
					x = Math.abs(r.nextInt()) % (15 - dlugosc);
					y = Math.abs(r.nextInt()) % 15;
					goraLewo = new Point(Math.max(x - 1, 0), Math.max(y - 1, 0));
					dolPrawo = new Point(Math.min(x + dlugosc + 1, 14), Math
							.min(y + 1, 14));
				} else {
					x = Math.abs(r.nextInt()) % 15;
					y = Math.abs(r.nextInt()) % (15 - dlugosc);
					goraLewo = new Point(Math.max(x - 1, 0), Math.max(y - 1, 0));
					dolPrawo = new Point(Math.min(x + 1, 14), Math.min(y
							+ dlugosc + 1, 14));
				}

				ok = true;

				for (int m = goraLewo.x; m <= dolPrawo.x; m++)
					for (int n = goraLewo.y; n <= dolPrawo.y; n++)
						if (tablica[m][n] != ElementyPlanszy.POLE_PUSTE) {
							ok = false;
						}
			} while (ok==false);

			for (int d = 0; d < dlugosc; d++)
				if (k == kierunek.POZIOMO) {
					tablica[x + d][y] = ElementyPlanszy.STATEK;
				} else {
					tablica[x][y + d] = ElementyPlanszy.STATEK;
				}
		}
                for (int i = 0; i < 15; i++) {
                    for (int j = 0; j < 15; j++) {
                    rysuj(i,j);
                }
            }
		
	}
    
    /**
     * metoda sprawdzająca czy wstazany statek jest zatopiony
     * @param x parametr położenia na osi x
     * @param y parametr położenia na osi y
     * @return zwraca odpowiedź na to czy statek został zatopiony
     */
    private boolean czyZatopiony(int x,int y){
        int t = x;
        while(--t >= 0 && (tablica[t][y]==ElementyPlanszy.STATEK_TRAFIONY || 
                tablica[t][y]==ElementyPlanszy.STATEK))
            if(tablica[t][y]==ElementyPlanszy.STATEK)
                return false;
        
        t = x;
        while(++t <= 14 && (tablica[t][y]==ElementyPlanszy.STATEK || 
                tablica[t][y]==ElementyPlanszy.STATEK_TRAFIONY))
            if(tablica[t][y]==ElementyPlanszy.STATEK)
                return false;
        
        t = y;
        while(--t >=0 && (tablica[x][t]==ElementyPlanszy.STATEK || 
                tablica[x][t]==ElementyPlanszy.STATEK_TRAFIONY))
            if(tablica[x][t]==ElementyPlanszy.STATEK)
                return false;
        
        t = y;
        while(++t <=14 && (tablica[x][t]==ElementyPlanszy.STATEK || 
                tablica[x][t]==ElementyPlanszy.STATEK_TRAFIONY))
            if(tablica[x][t]==ElementyPlanszy.STATEK)
                return false;
            
        return true;
    }
    
    /**
     * metoda zaznacza wskazay statek jako zatopiony
     * @param x położenie na osi x
     * @param y położenie na osi y
     */
    public void zaznaczZatopiony(int x,int y){
        int x1=x,x2=x,y1=y,y2=y;
        while(x1 > 0 && tablica[x1][y]==ElementyPlanszy.STATEK_TRAFIONY)
            x1--;
        while(x2 < 14 && tablica[x2][y]==ElementyPlanszy.STATEK_TRAFIONY)
            x2++;
        while(y1 > 0 && tablica[x][y1]==ElementyPlanszy.STATEK_TRAFIONY)
            y1--;
        while(y2 < 14 && tablica[x][y2]==ElementyPlanszy.STATEK_TRAFIONY)
            y2++;
        
        for (int i = x1; i <= x2; i++) {
            for (int j = y1; j <= y2; j++) {
                if(tablica[i][j]==ElementyPlanszy.STATEK_TRAFIONY)
                    tablica[i][j]=ElementyPlanszy.STATEK_ZATOPIONY;
                else
                tablica[i][j]=ElementyPlanszy.PUDŁO;
                rysuj(i,j);
            }
            
        }
    }
    
    /**
     * metoda sprawdza wynik strzału w dany element na planszy
     * @param x położenie na osi x
     * @param y położenie na osi y
     * @return zwraca wynik strzału
     */
    public WynikStrzalu sprawdzStrzal(int x,int y){
        if(tablica[x][y]==ElementyPlanszy.STATEK){
            if(czyZatopiony(x, y))
                return WynikStrzalu.TRAFIONY_ZATOPIONY;
            else
                return WynikStrzalu.TRAFIONY;
        }else if(tablica[x][y]==ElementyPlanszy.POLE_PUSTE)
        return WynikStrzalu.PUDLO;
        else if (tablica[x][y]==ElementyPlanszy.STATEK_TRAFIONY)
            return WynikStrzalu.TRAFIONY;
        else if(tablica[x][y]==ElementyPlanszy.STATEK_ZATOPIONY)
            return WynikStrzalu.TRAFIONY_ZATOPIONY;
        else
            return WynikStrzalu.PUDLO;
            
    }
    
    /**
     * metoda zaznacza wybrane pole po strzale
     * @param x parametr położenia x
     * @param y parametr położenia y
     * @param w wynik strzału
     */
    public void zaznaczStrzal(int x,int y,WynikStrzalu w){
        if(w == WynikStrzalu.PUDLO)
            tablica[x][y] = ElementyPlanszy.PUDŁO;
        else
            tablica[x][y]=ElementyPlanszy.STATEK_TRAFIONY;
        if(w==WynikStrzalu.TRAFIONY_ZATOPIONY)
            zaznaczZatopiony(x, y);
       rysuj(x,y);
    }
    
    /**
     * 
     * @param gracz właściciel planszy 
     */
    public Plansza(RodzajeGraczy gracz){
        Dimension rozmiar = new Dimension(200, 200);
        setSize(rozmiar);
	setMinimumSize(rozmiar);
	setMaximumSize(rozmiar);
	setPreferredSize(rozmiar);
        setLayout(null);
        
        this.wlasciciel = gracz;
        
        create();
        zerowanieTablicy();
        
        if(wlasciciel == RodzajeGraczy.GRACZ){
            rozkladLosowy();
            
        }
        
        
        
    }
    
    /**
     * metoda tworząca wizualne elementy planszy
     */
    public void create(){
        
        for (int i = 0; i < 15; i++) {
            for (int j = 0; j < 15; j++) {
                tab[i][j] = new JPanel();
                tab[i][j].setBounds(i*20, j*20, 19, 19);
                tab[i][j].setBackground(Color.getColor("kolor", 150));
                add(tab[i][j]);
            }
        }
    }
    
    /**
     * metoda zmieniająca kolor wybranego elementu planszy
     * @param x parametr położenia x
     * @param y parametr położenia y
     */
    public void rysuj(int x,int y){
        if(tablica[x][y]==ElementyPlanszy.POLE_PUSTE)
            tab[x][y].setBackground(Color.getColor("kolor", 564186));
        if(tablica[x][y]==ElementyPlanszy.STATEK)
            tab[x][y].setBackground(Color.DARK_GRAY);
        if(tablica[x][y]==ElementyPlanszy.STATEK_TRAFIONY)
            tab[x][y].setBackground(Color.red);
        if(tablica[x][y]==ElementyPlanszy.STATEK_ZATOPIONY)
            tab[x][y].setBackground(Color.BLACK);
        if(tablica[x][y]==ElementyPlanszy.PUDŁO)
            tab[x][y].setBackground(Color.BLUE);
    }

    
}
