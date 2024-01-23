package minesweeper;

import java.awt.Color;
import java.awt.Image;
import java.util.HashMap;

import javax.swing.ImageIcon;
import javax.swing.JButton;

/*
 * A jatek mezoinek a megjelenese, a JButton osztaly leszarmazottja, ez szinezi, valamint latja el ikonnal
 * a kulonbozo tipusu mezoket, ha a felhasznalo rajuk kattint bal vagy jobb klikkel.
 */
public class Cella extends JButton {

	private static final long serialVersionUID = 1L;
	public static final Color MEZO_NEM_LATSZIK = Color.DARK_GRAY;
	public static final Color MEZO_LATSZIK = Color.WHITE;

	int sor, oszlop;
	boolean akna_e;
	boolean latszik_e;
	boolean zaszlozott_e;
	private HashMap<Integer, Integer> ikonMeretek;

	/*
	 * Cella letrehozasa, soranak, oszlopanak beallitasa.
	 * 
	 * @param s - sor pozicio egesz szamkent
	 * 
	 * @param o - oszlop pozicio egesz szamkent
	 */
	public Cella(int s, int o) {
		super();
		sor = s;
		oszlop = o;
		super.setFocusPainted(false);
		super.revalidate();
		super.repaint();
	}

	/*
	 * Uj jatek eseten mindegyik mezore lefut, tovabbi alapbeallitasokat tartalmaz.
	 * 
	 * @param akna - az adott mezo akna-e, boolean tipusu
	 */
	public void ujJatek(boolean akna) {
		akna_e = akna;
		latszik_e = false;
		zaszlozott_e = false;
		super.setEnabled(true);
		super.setText("");
		mezoSzinez();

	}

	/*
	 * Az adott palyamerettol fuggo ikonmeretek inicializalasa.
	 */
	public void ikonMeret() {
		int[] ikonMeret = { 150, 120, 90, 90, 80, 80, 80, 80, 80, 80 };
		ikonMeretek = new HashMap<Integer, Integer>();
		for (int i = 0; i < 10; i++) {
			ikonMeretek.put(i + 1, ikonMeret[i]);
		}
	}

	/*
	 * Egy adott mezo szinenek beallitasa attol fuggoen, hogy latszik-e.
	 */
	public void mezoSzinez() {
		super.setBackground(latszik_e ? MEZO_LATSZIK : MEZO_NEM_LATSZIK);
	}

	/*
	 * Az aknat tartalmazo mezok kulsejenek beallitasaert felelos.
	 * 
	 * @param sor_oszlop - palyameret, hogy megfelelo nagysagu ikonok legyenek,
	 * egesz szam formatumu
	 */
	public void aknaSzinez(int sor_oszlop) {
		super.setForeground(Color.black);
		super.setBackground(Color.red);
		ImageIcon akna = new ImageIcon("mine.png");
		Image kep = akna.getImage();
		Image kisebbKep = kep.getScaledInstance(ikonMeretek.get(sor_oszlop), ikonMeretek.get(sor_oszlop),
				java.awt.Image.SCALE_SMOOTH);
		ImageIcon kisebbAkna = new ImageIcon(kisebbKep);
		super.setIcon(kisebbAkna);
	}

	/*
	 * A zaszlozott mezok kulsejenek beallitasaert felelos.
	 * 
	 * @param sor_oszlop - palyameret, hogy megfelelo nagysagu ikonok legyenek,
	 * egesz szam formatumu
	 */
	public void zaszloSzinez(int sor_oszlop) {
		super.setForeground(Color.black);
		super.setBackground(Color.blue);
		ImageIcon zaszlo = new ImageIcon("flag.png");
		Image kep = zaszlo.getImage();
		Image kisebbKep = kep.getScaledInstance(ikonMeretek.get(sor_oszlop), ikonMeretek.get(sor_oszlop),
				java.awt.Image.SCALE_SMOOTH);
		ImageIcon kisebbZaszlo = new ImageIcon(kisebbKep);
		super.setIcon(kisebbZaszlo);
	}

}
