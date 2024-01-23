package minesweeper;

import java.util.Random;

/*
 * Az adott palyahoz tartozo aknak generalasaert felelos. Ez az osztaly tartalmazza az aknak pontos poziciojat.
 */
public class AknaTerkep {
	int aknaDb;
	boolean[][] aknaHely = new boolean[10][10];

	/*
	 * Konstruktor, minden mezo inicializalasa
	 */
	public AknaTerkep() {
		for (int i = 0; i < 10; ++i) {
			for (int j = 0; j < 10; ++j) {
				aknaHely[i][j] = false;
			}
		}
	}

	/*
	 * A palyan az aknak random elhelyezese.
	 * 
	 * @param aknakSzama - hany akna legyen a palyan, egesz szam
	 * 
	 * @param sor_oszlop - mekkora a palya, egesz szam
	 */
	public void ujAknaTerkep(int aknakSzama, int sor_oszlop) {
		if (sor_oszlop == 1) {
			aknaHely[0][0] = true;
		} else {
			Random rnd = new Random(System.currentTimeMillis());
			aknaDb = aknakSzama;
			int iteracioSzam = aknaDb;
			int maxIndex = sor_oszlop - 1;
			while (iteracioSzam > 0) {
				int sor = rnd.nextInt(maxIndex + 1);
				int oszlop = rnd.nextInt(maxIndex + 1);
				if (aknaHely[sor][oszlop] == false) {
					aknaHely[sor][oszlop] = true;
					iteracioSzam--;
				}
			}

		}

	}

}
