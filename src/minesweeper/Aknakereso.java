package minesweeper;

import javax.swing.JFrame;
import javax.swing.ImageIcon;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import javax.swing.JPanel;
import javax.swing.JButton;
import javax.swing.JScrollPane;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.JComboBox;
import javax.swing.BoxLayout;
import javax.swing.JTable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Vector;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;

/**
 * Az aknakereso jatek letrehozasara szolgal. Letrehozza az alapveto swing
 * objektumokat. Valamint meghivja a toplista letrehozasara szolgalo fuggvenyt,
 * valamint az ablak mereteket legeneralo fuggvenyt. A megjelenitesert felelos
 * osztaly, ez tartalmazza a main programot is.
 */
public class Aknakereso extends JFrame {

	private static final long serialVersionUID = 1L;
	private final JMenu jmenu, jsubmenu;
	private final JMenuBar jmb;
	private final JMenuItem ji1, ji2, ji3, ji4;
	private final JLabel jl1, jl2;
	private final JButton jb;
	private JPanel jp;
	private Palya ezelotti_palya;
	private TimerPanel ezelotti_timer;
	private JLabel ezelotti_zaszlojl, zaszlojl;
	private JTable jt;
	private JFrame Toplista;
	public int sor_oszlop, aknakSzama, ido;
	private HashMap<Integer, Integer> meretek;

	/*
	 * Az osztaly konstruktora. Az aknakereso jatek alapveto beallitasait vegzi el.
	 */
	public Aknakereso() {
		ImageIcon logo = new ImageIcon("icon.png");

		this.setTitle("Aknakereső");
		this.setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);

		this.setIconImage(logo.getImage());

		jmb = new JMenuBar();
		jmenu = new JMenu("Menü");
		jsubmenu = new JMenu("Új játék létrehozása");
		ji1 = new JMenuItem("Időkerettel");
		ji1.addActionListener(new UjJatekIdoLimittel());
		ji2 = new JMenuItem("Időkeret nélkül");
		ji2.addActionListener(new UjJatekIdoNelkul());
		ji3 = new JMenuItem("Toplista megtekintése");
		ji3.addActionListener(new TopLista());
		ji4 = new JMenuItem("Kilépés");
		ji4.addActionListener(new Kilep());
		jsubmenu.add(ji1);
		jsubmenu.add(ji2);
		jmenu.add(jsubmenu);
		jmenu.add(ji3);
		jmenu.add(ji4);
		jmb.add(jmenu);
		this.setJMenuBar(jmb);
		this.addWindowListener(new WindowClosing());
		jp = new JPanel();
		jp.setLayout(new BorderLayout(10, 10));
		jl1 = new JLabel("Aknakereső", JLabel.CENTER);
		jl1.setFont(new Font("Arial", Font.PLAIN, 100));
		jl1.setVerticalAlignment(JLabel.BOTTOM);
		jp.add(jl1, BorderLayout.NORTH);
		jl2 = new JLabel("By: Sütheö István (XOBJYX)", JLabel.CENTER);
		jl2.setFont(new Font("Arial", Font.PLAIN, 40));
		jp.add(jl2, BorderLayout.SOUTH);
		jb = new JButton();
		ImageIcon smiley = new ImageIcon("smiley.png");
		jb.setIcon(smiley);
		jb.setBackground(Color.black);
		jb.setBorderPainted(false);
		jb.setFocusPainted(false);
		jb.addActionListener(new FoMenuGomb());
		jp.add(jb, BorderLayout.CENTER);
		ezelotti_palya = new Palya(1, 0, 0, null);
		ezelotti_timer = new TimerPanel(1, null);
		ezelotti_zaszlojl = new JLabel("");

		TopListaLetrehoz();
		AblakBeallitasok();

		this.add(jp);
		this.setSize(1000, 1000);
		this.setResizable(false);
		this.setLocationRelativeTo(null);
		this.setVisible(true);
	}

	/*
	 * Ez a belso osztaly azert felelos, hogy a felhasznalotol egy megerositest
	 * kapjon kilepes eseten. Igen valasz eseten bezarja az adott Aknakereso
	 * peldanyt (ezaltal a programot is).
	 */

	private class WindowClosing extends WindowAdapter {
		public void windowClosing(WindowEvent e) {
			int kilepes = JOptionPane.showOptionDialog(null, "Biztos ki akarsz lépni?", "Kilépés a játékból",
					JOptionPane.OK_CANCEL_OPTION, JOptionPane.INFORMATION_MESSAGE, null, new String[] { "Igen", "Nem" },
					"default");

			if (kilepes == JOptionPane.YES_OPTION) {
				dispose();
				if (Toplista != null) {
					Toplista.dispose();
				}
				System.out.println("Toplistak sikeresen mentve.");
			}
		}
	}

	/*
	 * A fomenuben lathato nagy gombra nyomas eseten megjelenit egy udvozlo
	 * uzenetet.
	 */

	private class FoMenuGomb implements ActionListener {
		public void actionPerformed(ActionEvent ae) {
			JOptionPane.showMessageDialog(null,
					"<html><h1>Üdvözöllek az Aknakereső játékban!</h1>Új játék indításához, valamint a toplista megtekintéséhez használd a <font color = red>bal felső</font> sarokban található menüt.",
					"", JOptionPane.INFORMATION_MESSAGE);
		}
	}

	/*
	 * Ha a menubol az idokorlatos jatekra nyomunk, akkor ez a metodus fog lefutni.
	 * A felhasznalotol JComboBox segitsegevel megkonfiguralja a jatekot, bekeri a
	 * palyameretet, idot, aknaszamot. A palyameretet az UjJatekPalyaMeret metodus
	 * segitsegevel kapja meg. Ha a felhasznalo megadott mindent egy uj jatekot
	 * indit.
	 */

	private class UjJatekIdoLimittel implements ActionListener {
		public void actionPerformed(ActionEvent ae) {

			int meret = UjJatekPalyaMeret();
			if (meret != 0) {
				JPanel panelTop = new JPanel();
				panelTop.setLayout(new BoxLayout(panelTop, BoxLayout.PAGE_AXIS));
				int idoPlusz = meret * 60 / 20;
				Object idoOpciok[] = new Object[20];
				for (int i = 0; i < 20; i++) {
					idoOpciok[i] = idoPlusz * (i + 1);
				}
				JComboBox<Object> jcbIdo = new JComboBox<Object>(idoOpciok);
				JComboBox<Object> jcbAkna;
				if (meret != 1) {
					int indexAkna = meret * meret - 1;
					Object aknaOpciok[] = new Object[indexAkna];
					for (int i = 0; i < indexAkna; i++) {
						aknaOpciok[i] = i + 1;
					}
					jcbAkna = new JComboBox<Object>(aknaOpciok);
				} else {
					Object aknaOpciok[] = new Object[1];
					aknaOpciok[0] = 1;
					jcbAkna = new JComboBox<Object>(aknaOpciok);
				}

				panelTop.add(new JLabel("Hány akna legyen a pályán?"));
				panelTop.add(jcbAkna);
				panelTop.add(new JLabel("Mennyi idő legyen a pálya teljesítésére? (másodpercben)"));
				panelTop.add(jcbIdo);

				JFrame belso = new JFrame();
				belso.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
				belso.setAlwaysOnTop(true);
				belso.add(panelTop);
				belso.setSize(300, 150);
				int valasztas = JOptionPane.showOptionDialog(belso, panelTop, "Pályabeállítások",
						JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE, null,
						new String[] { "<html><font color=green>OK", "<html><font color=red>Mégse" }, null);
				if (valasztas == JOptionPane.YES_OPTION) {
					aknakSzama = (int) jcbAkna.getSelectedItem();
					ido = (int) jcbIdo.getSelectedItem();
					sor_oszlop = meret;
					UjJatek();
					belso.dispose();
				} else {
					belso.dispose();

				}
			}
		}
	}

	/*
	 * Ha a menubol az idokorlat nelkuli jatekra nyomunk, akkor ez a metodus fog
	 * lefutni. A felhasznalotol JComboBox segitsegevel megkonfiguralja a jatekot,
	 * bekeri a palyameretet, aknaszamot. A palyameretet az UjJatekPalyaMeret
	 * metodus segitsegevel kapja meg. Ha a felhasznalo megadott mindent egy uj
	 * jatekot indit.
	 */

	private class UjJatekIdoNelkul implements ActionListener {
		public void actionPerformed(ActionEvent ae) {
			int meret = UjJatekPalyaMeret();
			if (meret != 0) {
				JPanel panelTop = new JPanel();
				panelTop.setLayout(new BoxLayout(panelTop, BoxLayout.PAGE_AXIS));
				JComboBox<Object> jcbAkna;
				if (meret != 1) {
					int indexAkna = meret * meret - 1;
					Object aknaOpciok[] = new Object[indexAkna];
					for (int i = 0; i < indexAkna; i++) {
						aknaOpciok[i] = i + 1;
					}
					jcbAkna = new JComboBox<Object>(aknaOpciok);
				} else {
					Object aknaOpciok[] = new Object[1];
					aknaOpciok[0] = 1;
					jcbAkna = new JComboBox<Object>(aknaOpciok);
				}
				panelTop.add(new JLabel("Hány akna legyen a pályán?"));
				panelTop.add(jcbAkna);
				JFrame belso = new JFrame();
				belso.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
				belso.setAlwaysOnTop(true);
				belso.add(panelTop);
				belso.setSize(300, 150);
				int valasztas = JOptionPane.showOptionDialog(belso, panelTop, "Pályabeállítások",
						JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE, null,
						new String[] { "<html><font color=green>OK", "<html><font color=red>Mégse" }, null);
				if (valasztas == JOptionPane.YES_OPTION) {
					aknakSzama = (int) jcbAkna.getSelectedItem();
					ido = 0;
					sor_oszlop = meret;
					UjJatek();
					belso.dispose();
				} else {
					belso.dispose();
				}
			}

		}

	}

	/*
	 * Ha egy uj jatekot inditunk, akar idokorlattal, akar anelkul, akkor ez a
	 * metodus fog lefutni. A felhasznalotol ker egy megfelelo palyameretet (1-10
	 * kozotti egesz szam), majd ez visszaadja. Figyel arra, hogy a felhasznalo
	 * megfelelo palyameretet adjon meg, valamint a formatumra.
	 * 
	 * @return int - a kivalasztott palyameret
	 */

	private int UjJatekPalyaMeret() {
		JPanel panel = new JPanel();
		panel.add(new JLabel(
				"<html>Hány sorból és hány oszlopból álljon a pálya?<br><font color=red>1 és 10 közé eső egész számot adj meg!"));
		JTextField mezo = new JTextField(10);
		panel.add(mezo);
		boolean kilepes = false;
		int palyaMeret = 0;
		while (!kilepes) {
			int valasztas = JOptionPane.showOptionDialog(null, panel, "Pályaméret", JOptionPane.YES_NO_OPTION,
					JOptionPane.QUESTION_MESSAGE, null, new String[] { "OK", "Mégse" }, null);
			if (valasztas == JOptionPane.YES_OPTION) {
				String meret = mezo.getText();
				try {
					palyaMeret = Integer.parseInt(meret);
				} catch (NumberFormatException e) {
				}
				if (palyaMeret > 10 || palyaMeret < 1) {
					JOptionPane.showMessageDialog(null,
							"Nem megfelelő pályaméretet adtál meg!\nA pálya méret egy 1 és 10 közötti egész szám lehet!",
							"Nem megfelelő input", JOptionPane.WARNING_MESSAGE);
				} else {
					kilepes = true;
				}
			} else {
				palyaMeret = 0;
				kilepes = true;
			}

		}
		return palyaMeret;

	}

	/*
	 * A program inditasakor fut le, a toplista letrehozasaert felelos.
	 */
	private void TopListaLetrehoz() {
		Toplista = new JFrame();
		Toplista.setResizable(false);
		String oszlopNevek[] = { "", "Idő", "Aknák száma", "Név" };
		DefaultTableModel tableModell = new DefaultTableModel(oszlopNevek, 0) {
			private static final long serialVersionUID = 1L;

			@Override
			public boolean isCellEditable(int row, int column) {
				return false;
			}
		};

		jt = new JTable(15, 4);
		jt.setModel(tableModell);
		jt.setBounds(30, 40, 200, 250);
		JScrollPane sp = new JScrollPane(jt);
		jt.getColumnModel().getColumn(0).setMaxWidth(20);
		jt.getColumnModel().getColumn(1).setMaxWidth(50);
		jt.getColumnModel().getColumn(2).setMaxWidth(100);
		jt.getColumnModel().getColumn(2).setPreferredWidth(100);
		jt.setAutoResizeMode(JTable.AUTO_RESIZE_LAST_COLUMN);
		jt.getTableHeader().setResizingAllowed(false);
		jt.getTableHeader().setReorderingAllowed(false);
		jt.getTableHeader().getColumnModel().getColumn(0).setHeaderValue("");
		jt.getTableHeader().getColumnModel().getColumn(1).setHeaderValue("Idő");
		jt.getTableHeader().getColumnModel().getColumn(2).setHeaderValue("Aknák száma");
		jt.getTableHeader().getColumnModel().getColumn(3).setHeaderValue("Név");

		DefaultTableCellRenderer kozepreIgazitva = new DefaultTableCellRenderer();
		kozepreIgazitva.setHorizontalAlignment(JLabel.CENTER);
		jt.getColumnModel().getColumn(1).setCellRenderer(kozepreIgazitva);
		jt.getColumnModel().getColumn(2).setCellRenderer(kozepreIgazitva);

		Toplista.setTitle("Toplista");

		Toplista.add(sp);
		Toplista.setSize(500, 303);
	}

	/*
	 * Ez a belso osztaly akkor hivodik meg, amikor a menubol a toplista
	 * megtekinteset valasztja a felhasznalo. A felhasznalotol megkerdezi melyik
	 * kategoriat szeretne megtekinteni. Ha valamelyik kategoriat kivalasztja akkor
	 * a toplista betolteseert felelos metodust meghivja.
	 */

	private class TopLista implements ActionListener {
		public void actionPerformed(ActionEvent ae) {
			JPanel panelTop = new JPanel();
			panelTop.setLayout(new BoxLayout(panelTop, BoxLayout.PAGE_AXIS));
			JComboBox<Object> jcbToplista;
			Object toplistaOpciok[] = new Object[10];
			for (int i = 0; i < 10; i++) {
				toplistaOpciok[i] = (i + 1) + "x" + (i + 1);
			}
			jcbToplista = new JComboBox<Object>(toplistaOpciok);
			panelTop.add(new JLabel("Melyik kategóriához tartozó toplistát szeretnéd megtekinteni?"));
			panelTop.add(jcbToplista);
			JFrame belso = new JFrame();
			belso.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
			belso.setAlwaysOnTop(true);
			belso.add(panelTop);
			belso.setSize(300, 150);
			int valasztas = JOptionPane.showOptionDialog(belso, panelTop, "Toplista megtekintése",
					JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE, null,
					new String[] { "<html><font color=green>OK", "<html><font color=red>Mégse" }, null);
			if (valasztas == JOptionPane.YES_OPTION) {
				DefaultTableModel tabla = (DefaultTableModel) jt.getModel();
				tabla.setRowCount(0);
				String fajlnev = (jcbToplista.getSelectedIndex() + 1) + ".txt";
				try {
					TopListaFajlbolBetolt(fajlnev);
				} catch (Exception e) {
					System.out.println("Ehhez a kategoriahoz meg nincsenek rekordok!");
				}
				belso.dispose();
				ImageIcon logo = new ImageIcon("cup.png");
				Toplista.setIconImage(logo.getImage());
				Toplista.setLocationRelativeTo(null);
				Toplista.setVisible(true);
				Toplista.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
				Toplista.setAlwaysOnTop(true);
			} else {
				belso.dispose();

			}

		}
	}

	/*
	 * Ez a belso osztaly felelos a menubol valo kilepesert. Itt azonban nincsen
	 * megerosito kerdes.
	 */

	private class Kilep implements ActionListener {
		public void actionPerformed(ActionEvent ae) {
			dispose();
			if (Toplista != null) {
				Toplista.dispose();
			}
			System.out.println("Toplistak sikeresen mentve.");
		}
	}

	/*
	 * Az ablakmereteket inicializalja a megfelelo meretu palyak megjelenitesehez.
	 */
	private void AblakBeallitasok() {
		int[] meret = { 250, 400, 400, 500, 600, 700, 800, 900, 1000, 1000 };
		meretek = new HashMap<Integer, Integer>();
		for (int i = 0; i < 10; i++) {
			meretek.put(i + 1, meret[i]);
		}
	}

	/*
	 * A Palya osztaly teszteleset segito metodus. Csak a tesztesetekben van
	 * meghivva.
	 */

	public void ZaszloTest(int aknaDb) {
		String zaszloString = String.valueOf(aknaDb);
		zaszlojl = new JLabel(zaszloString, SwingConstants.CENTER);
	}

	/*
	 * Uj jatek inditasa. Miutan a felhasznalotol bekertuk a megfelelo
	 * palyabeallitasokat, ez a metodus fut le. Felveszi a megfelelo komponenseket a
	 * JFrame-be, valamint meg is jeleniti azokat. Legeneral egy uj palyat, valamint
	 * egy uj idozitot is.
	 */
	public void UjJatek() {
		System.out.println("\nA kivalasztott palyameret: " + sor_oszlop + "x" + sor_oszlop);
		System.out.println("Aknak szama: " + aknakSzama + "\n");
		this.remove(ezelotti_zaszlojl);
		String zaszloString = String.valueOf(aknakSzama);
		zaszlojl = new JLabel(zaszloString, SwingConstants.CENTER);
		zaszlojl.setFont(new Font("Arial", Font.BOLD, 20));
		ezelotti_zaszlojl = zaszlojl;
		Palya palya = new Palya(sor_oszlop, ido, aknakSzama, this);
		this.setSize(meretek.get(sor_oszlop), meretek.get(sor_oszlop) + 50);
		this.setLocationRelativeTo(null);
		this.remove(ezelotti_palya);
		this.remove(ezelotti_timer);
		ezelotti_timer.timer.stop();
		ezelotti_palya = palya;
		this.remove(jp);
		this.revalidate();
		this.repaint();
		if (ido == 0) {
			TimerPanel timer = new TimerPanel(this);
			ezelotti_timer = timer;
			this.add(palya);
			this.add(zaszlojl, BorderLayout.SOUTH);
			this.add(timer, BorderLayout.NORTH);
			this.revalidate();
			this.repaint();
		} else {
			this.add(palya);
			TimerPanel timer = new TimerPanel(ido, this);
			ezelotti_timer = timer;
			this.add(timer, BorderLayout.NORTH);
			this.add(zaszlojl, BorderLayout.SOUTH);
			this.revalidate();
			this.repaint();
		}
	}

	/*
	 * A toplista egyes kategoriainak a mentesere szolgalo metodus.
	 * 
	 * @param fajlnev - az adott kategoriahoz tartozo .txt kiterjesztesu fajl neve
	 * Stringkent
	 */
	private void TopListaFajlbaMent(String fajlnev) throws Exception {
		DefaultTableModel tabla = (DefaultTableModel) jt.getModel();
		ObjectOutputStream ki = new ObjectOutputStream(new FileOutputStream(fajlnev));
		int sor = tabla.getRowCount();
		for (int i = 0; i < sor; i++) {
			String helyezes = (i + 1) + ".";
			tabla.setValueAt(helyezes, i, 0);
		}

		ki.writeObject(tabla.getDataVector());
		ki.close();
	}

	/*
	 * A toplista egyes kategoriainak beolvasasara szolgalo metodus.
	 * 
	 * @param fajlnev - az adott kategoriahoz tartozo .txt kiterjesztesu fajl neve
	 * Stringkent
	 */
	private void TopListaFajlbolBetolt(String fajlnev) throws Exception {
		DefaultTableModel tabla = (DefaultTableModel) jt.getModel();
		ObjectInputStream be = new ObjectInputStream(new FileInputStream(fajlnev));
		Vector<?> sorAdatok = (Vector<?>) be.readObject();
		Iterator<?> it = sorAdatok.iterator();
		while (it.hasNext()) {
			tabla.addRow((Vector<?>) it.next());
		}
		be.close();
	}

	/*
	 * Jatek kozben a zaszloszam modositasaert felelos metodus.
	 */
	public void ZaszloUpdate(int db) {
		String zaszloString = String.valueOf(db);
		zaszlojl.setText(zaszloString);
	}

	/*
	 * Ha lejar az ido, ez a metodus fut le. Leallitja az idozitot, valamint jelez a
	 * felhasznalonak. Kikapcsolja az osszes mezohozzaferest (MouseListenert).
	 */
	public void GameOverIdo() {
		ezelotti_timer.timer.stop();
		JOptionPane.showMessageDialog(null, "A játék véget ért. Letelt az idő!", "GAME OVER",
				JOptionPane.WARNING_MESSAGE);
		ezelotti_palya.GameOver = true;
	}

	/*
	 * Ha egy aknara nyom a felhasznalo, ez a metodus fut le. Leallitja az idozitot,
	 * valamint jelez a felhasznalonak. Kikapcsolja az osszes mezohozzaferest.
	 */
	public void GameOverAkna() {
		ezelotti_timer.timer.stop();
		JOptionPane.showMessageDialog(null, "A játék véget ért. Egy aknát fedtél fel!", "GAME OVER",
				JOptionPane.WARNING_MESSAGE);
		ezelotti_palya.GameOver = true;
	}

	/*
	 * Jatekos hozzaadasa a megfelelo kategoriaju toplistahoz.
	 * 
	 * @param ido - mennyi ido alatt teljesitette a palyat
	 * 
	 * @param nev - a jatekos neve
	 */
	private void ToplistaHozzaad(String ido, String nev) throws FileNotFoundException, IOException {
		ArrayList<String> fajlok = new ArrayList<String>();
		for (int i = 0; i < 10; i++) {
			int szam = i + 1;
			fajlok.add(szam + ".txt");
		}
		String fajlnev = fajlok.get(sor_oszlop - 1);
		DefaultTableModel tabla = (DefaultTableModel) jt.getModel();
		tabla.setRowCount(0);
		try {
			TopListaFajlbolBetolt(fajlnev);
			boolean bekerult = false;
			kulso_ciklus: for (int i = 0; i < tabla.getRowCount(); i++) {
				String maxIdo = (String) tabla.getValueAt(i, 1);
				if (Integer.valueOf(maxIdo) == Integer.valueOf(ido)) {
					for (int j = i; j < tabla.getRowCount(); j++) {
						String ujido = (String) tabla.getValueAt(j, 1);
						if (Integer.valueOf(maxIdo) < Integer.valueOf(ujido)) {
							tabla.insertRow(j, new Object[] { "", ido, String.valueOf(aknakSzama), nev });
							bekerult = true;
							break kulso_ciklus;
						}
					}

				} else if (Integer.valueOf(maxIdo) > Integer.valueOf(ido)) {
					tabla.insertRow(i, new Object[] { "", ido, String.valueOf(aknakSzama), nev });
					bekerult = true;
					break;
				}
			}
			if (!bekerult && tabla.getRowCount() < 15) {
				tabla.addRow(new Object[] { "", ido, String.valueOf(aknakSzama), nev });
			}
			if (tabla.getRowCount() > 15) {
				tabla.setRowCount(15);
			}

			TopListaFajlbaMent(fajlnev);
		} catch (Exception e) {
			tabla.addRow(new Object[] { "", ido, String.valueOf(aknakSzama), nev });
			try {
				TopListaFajlbaMent(fajlnev);
			} catch (Exception f) {
				System.out.println("Sikertelen fajlbamentes!");
			}
		}
	}

	/*
	 * Akkor hivodik meg ez a metodus ha a felhasznalo sikeresen felfedte az osszes
	 * akna nelkuli mezot, es ezzel egyidoben megjelolte az osszes aknat tartalmazo
	 * mezot zaszloval.
	 */
	public void GameWin() {
		ezelotti_timer.timer.stop();
		JPanel belso = new JPanel();
		belso.setLayout(new BoxLayout(belso, BoxLayout.PAGE_AXIS));
		JLabel felirat = new JLabel("Sikeresen megtaláltad az összes aknát. Kérlek add meg a nevedet!");
		JTextField mezo = new JTextField(30);
		belso.add(felirat);
		belso.add(mezo);
		String[] options = { "OK" };

		int valasztas = JOptionPane.showOptionDialog(null, belso, "CONGRATULATIONS", JOptionPane.NO_OPTION,
				JOptionPane.QUESTION_MESSAGE, null, options, options[0]);
		if (valasztas == JOptionPane.YES_OPTION) {
			try {
				if (mezo.getText().equals("")) {
					String nev = "Anonymous";
					ToplistaHozzaad(String.valueOf(ezelotti_timer.elteltido), nev);
				} else {
					ToplistaHozzaad(String.valueOf(ezelotti_timer.elteltido), mezo.getText());
				}

			} catch (Exception e) {
				System.out.println("Hiba fajlbeolvasaskor!");
			}
		}
	}

	/*
	 * A main fuggveny, egy Aknakereso peldanyt hoz letre. Innen indul a program.
	 */

	public static void main(String[] args) {
		new Aknakereso();

	}

}
