package minesweeper;

import org.junit.Assert;
import org.junit.Test;

/*
 * Az aknak generalasanak ellenorzese.
 */
public class AknaTerkepTest {

	/*
	 * Ellenorzi, hogy mindegyik mezo alapbol nem tartalmaz aknat.
	 */
	@Test
	public void Inicializalas() {
		AknaTerkep uj = new AknaTerkep();
		for (int i = 0; i < 10; ++i) {
			for (int j = 0; j < 10; ++j) {
				Assert.assertFalse(uj.aknaHely[i][j]);
			}
		}
	}

	/*
	 * Ha 1x1 palya van, akkor mindenkepp aknat kell a mezonek tartalmaznia.
	 */
	@Test
	public void AknaGeneralasKisPalya() {
		AknaTerkep uj = new AknaTerkep();
		uj.ujAknaTerkep(1, 1);
		Assert.assertTrue(uj.aknaHely[0][0]);
	}

	/*
	 * Nem 1x1-es palya eseten az akna szamot ellenorzi.
	 */
	@Test
	public void AknakGeneralasaNagyPalya() {
		AknaTerkep uj = new AknaTerkep();
		uj.ujAknaTerkep(4, 5);
		int aknadb = 0;
		for (int i = 0; i < 10; ++i) {
			for (int j = 0; j < 10; ++j) {
				if (uj.aknaHely[i][j] == true) {
					aknadb++;
				}
			}
		}
		Assert.assertEquals(aknadb, 4);
	}

}
