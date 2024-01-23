package minesweeper;

import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.HashMap;

import javax.swing.JPanel;

/*
 * Az aknakereso jatek agya. Ez az osztaly felelos a palya mezoinek legeneralasaert, valamint ezek inicializalasaert.
 * Valamint itt kezeljuk a felhasznalo egyes kattintasait az adott mezokre.
 */
public class Palya extends JPanel {
	private static final long serialVersionUID = 1L;
	int sor_oszlop;
	int ido;
	int aknakSzama;
	int zaszlokSzama;
	Cella mezok[][];
	Aknakereso megjelenito;
	boolean GameOver;
	AknaTerkep aknaterkep;
	HashMap<Integer, Integer> szamMeretek;

	/*
	 * Az osztaly konstruktora, itt tortenik meg a palya inicializalasa, valamint a
	 * mezokhoz egyes Listener-ek hozzaadasa.
	 * 
	 * @param s - sor, valamint oszlopszam egesz szamkent
	 * 
	 * @param t - idokorlat, egesz szamkent
	 * 
	 * @param a - aknak szama, egesz szamkent
	 * 
	 * @param ak - maga a Palyat meghivo Aknakereso peldany
	 */
	public Palya(int s, int t, int a, Aknakereso ak) {
		GameOver = false;
		zaszlokSzama = 0;
		sor_oszlop = s;
		ido = t;
		aknakSzama = a;
		megjelenito = ak;
		mezok = new Cella[sor_oszlop][sor_oszlop];
		super.setLayout(new GridLayout(sor_oszlop, sor_oszlop, 2, 2));

		for (int i = 0; i < sor_oszlop; ++i) {
			for (int j = 0; j < sor_oszlop; ++j) {
				mezok[i][j] = new Cella(i, j);
				mezok[i][j].ikonMeret();
				super.add(mezok[i][j]);
			}
		}

		MezoKattintas egerHallgato = new MezoKattintas();

		for (int i = 0; i < sor_oszlop; ++i) {
			for (int j = 0; j < sor_oszlop; ++j) {
				mezok[i][j].addMouseListener(egerHallgato);
			}
		}

		aknaterkep = new AknaTerkep();
		aknaterkep.ujAknaTerkep(aknakSzama, sor_oszlop);

		for (int i = 0; i < sor_oszlop; i++) {
			for (int j = 0; j < sor_oszlop; j++) {
				mezok[i][j].ujJatek(aknaterkep.aknaHely[i][j]);
			}
		}

		szamMeretek();

	}

	/*
	 * Az adott mezokon olvashato szamok mereteinek legeneralasa a kulonbozo meretu
	 * palyakhoz.
	 */
	private void szamMeretek() {
		int[] szamMeret = { 10, 140, 90, 90, 85, 80, 75, 75, 70, 70 };
		szamMeretek = new HashMap<Integer, Integer>();
		for (int i = 0; i < 10; i++) {
			szamMeretek.put(i + 1, szamMeret[i]);
		}
	}

	/*
	 * Ellenorzi, hogy a jatekot megnyerte-e a jatekos.
	 * 
	 * @return boolean - nyert-e a jatekos mar vagy meg nem
	 */
	public boolean NyeresiKondicio() {
		int felfedett = 0;
		int megtalalt = 0;
		for (int i = 0; i < sor_oszlop; ++i) {
			for (int j = 0; j < sor_oszlop; ++j) {
				if (mezok[i][j].zaszlozott_e && mezok[i][j].akna_e) {
					megtalalt++;
				} else if (mezok[i][j].latszik_e) {
					felfedett++;
				}
			}
		}

		int akna_nelkuli_mezok = (sor_oszlop * sor_oszlop) - aknakSzama;

		if (megtalalt == aknakSzama && felfedett == akna_nelkuli_mezok) {
			for (int i = 0; i < sor_oszlop; ++i) {
				for (int j = 0; j < sor_oszlop; ++j) {
					if (mezok[i][j].zaszlozott_e) {
						mezok[i][j].setBackground(Color.GREEN);
					}
				}
			}
			GameOver = true;
			return true;
		} else {
			return false;
		}
	}

	/*
	 * Egy adott mezo, valamint annak szomszedjainak felfedese, ha azok nem
	 * tartalmaznak aknat. Ha nincs egyeltalan a kozvetlen kozelebe akna akkor
	 * rekurzivan meghivodik megint ez a metodus.
	 */
	public void SzomszedFelfed(Cella mezo) {
		int aknaDb = SzomszedAknaSzam(mezo);
		String aknaSzam = String.valueOf(aknaDb);
		if (Integer.valueOf(aknaSzam) > 0) {
			mezo.setText(aknaSzam);
		}
		if (mezo.zaszlozott_e) {
			mezo.setIcon(null);
			mezo.zaszlozott_e = false;
			zaszlokSzama--;
			megjelenito.ZaszloUpdate(aknakSzama - zaszlokSzama);
		}
		mezo.setFont(new Font("Arial", Font.BOLD, szamMeretek.get(sor_oszlop)));
		mezo.latszik_e = true;
		mezo.mezoSzinez();
		if (aknaDb == 0) {
			for (int i = mezo.sor - 1; i <= mezo.sor + 1; i++) {
				for (int j = mezo.oszlop - 1; j <= mezo.oszlop + 1; j++) {
					if (i >= 0 && i < sor_oszlop && j >= 0 && j < sor_oszlop) {
						if (!mezok[i][j].latszik_e) {
							SzomszedFelfed(mezok[i][j]);
						}
					}
				}
			}
		}
	}

	/*
	 * Ha a jatek veget er, akkor ez a metodus felfedi az aknat helyzetet.
	 */
	public void AknakFelfedese(Cella mezo) {
		for (int i = 0; i < sor_oszlop; i++) {
			for (int j = 0; j < sor_oszlop; j++) {
				if (mezok[i][j].akna_e && mezok[i][j] != mezo) {
					mezok[i][j].aknaSzinez(sor_oszlop);
				}
			}
		}
	}

	/*
	 * Egy adott mezo kozvetlen kozeleben (szomszedsagaban) levo aknak szama
	 * 
	 * @return int - szomszédos aknák száma
	 */
	public int SzomszedAknaSzam(Cella mezo) {
		int akna = 0;
		for (int i = mezo.sor - 1; i <= mezo.sor + 1; i++) {
			for (int j = mezo.oszlop - 1; j <= mezo.oszlop + 1; j++) {
				if (i >= 0 && i < sor_oszlop && j >= 0 && j < sor_oszlop) {
					if (mezok[i][j].akna_e) {
						akna++;
					}
				}
			}
		}
		return akna;
	}

	/*
	 * Ez a belso osztaly kezeli a mezokon levo kattintasokat. Folyamatosan
	 * ellenorzi, hogy a nyeresi kondicio megvan-e mar, illetve jatek vege eseten
	 * meghivja az Aknakereso osztaly megfelelo metodusat.
	 */
	private class MezoKattintas extends MouseAdapter {
		@Override
		public void mouseClicked(MouseEvent e) {
			if (!GameOver) {
				Cella forrasMezo = (Cella) e.getSource();

				System.out.println("Erre a mezore klikkeltel -> sor: " + (forrasMezo.sor + 1) + " oszlop: "
						+ (forrasMezo.oszlop + 1));

				if (e.getButton() == MouseEvent.BUTTON1) {
					if (forrasMezo.akna_e && !forrasMezo.zaszlozott_e) {
						forrasMezo.aknaSzinez(sor_oszlop);
						Color ELTALT_AKNA = new Color(153, 0, 0);
						forrasMezo.setBackground(ELTALT_AKNA);
						AknakFelfedese(forrasMezo);
						megjelenito.GameOverAkna();
					} else if (forrasMezo.zaszlozott_e) {
						System.out.println("Ez a mezo mar zaszlozva van !");
					} else {
						SzomszedFelfed(forrasMezo);
						if (NyeresiKondicio()) {
							megjelenito.GameWin();
						}
					}
				} else if (e.getButton() == MouseEvent.BUTTON3) {
					if (!forrasMezo.zaszlozott_e && zaszlokSzama < aknakSzama && !forrasMezo.latszik_e) {
						forrasMezo.zaszlozott_e = true;
						forrasMezo.zaszloSzinez(sor_oszlop);
						zaszlokSzama++;
						megjelenito.ZaszloUpdate(aknakSzama - zaszlokSzama);
						if (NyeresiKondicio()) {
							megjelenito.GameWin();
						}

					} else if (forrasMezo.zaszlozott_e) {
						forrasMezo.zaszlozott_e = false;
						forrasMezo.mezoSzinez();
						forrasMezo.setIcon(null);
						zaszlokSzama--;
						megjelenito.ZaszloUpdate(aknakSzama - zaszlokSzama);
					}
				}
			}

		}
	}

}
