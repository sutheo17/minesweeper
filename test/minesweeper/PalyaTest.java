package minesweeper;

import java.awt.Color;
import java.util.Random;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/*
 * A palya alapveto funkcioinak letesztelese.
 */
public class PalyaTest {

	Palya palya;
	int sor;
	int oszlop;

	/*
	 * Egy palya letrehozasa a tesztesetek elott, valamint egy akna nelkuli mezo
	 * kivalasztasa.
	 */
	@Before
	public void setUp() {
		Aknakereso ak = new Aknakereso();
		ak.ZaszloTest(15);
		ak.setVisible(false);
		palya = new Palya(10, 150, 15, ak);
		palya.zaszlokSzama = 15;

		Random rnd = new Random(System.currentTimeMillis());
		boolean kilep = false;
		while (!kilep) {
			sor = rnd.nextInt(palya.sor_oszlop);
			oszlop = rnd.nextInt(palya.sor_oszlop);
			if (!palya.mezok[sor][oszlop].akna_e) {
				kilep = true;
			}
		}
	}

	/*
	 * A palya alapveto beallitasait ellenorzi.
	 */
	@Test
	public void UjPalyaLetrehozasa() {
		Aknakereso ak = new Aknakereso();
		ak.setVisible(false);
		Palya ujmap = new Palya(10, 150, 15, ak);
		Assert.assertEquals(ujmap.aknakSzama, 15);
		Assert.assertEquals(ujmap.sor_oszlop, 10);
		Assert.assertEquals(ujmap.ido, 150);
		Assert.assertEquals(ujmap.megjelenito, ak);
		for (int i = 0; i < 10; i++) {
			for (int j = 0; j < 10; j++) {
				Assert.assertEquals(ujmap.mezok[i][j].akna_e, ujmap.aknaterkep.aknaHely[i][j]);
			}
		}
	}

	/*
	 * Azt ellenorzi, hogy tenyleg megfeleloen mukodik-e a nyerest vizsgalo metodus.
	 */
	@Test
	public void NyeresiKondicioWin() {
		for (int i = 0; i < 10; i++) {
			for (int j = 0; j < 10; j++) {
				if (palya.aknaterkep.aknaHely[i][j]) {
					palya.mezok[i][j].zaszlozott_e = true;
				} else {
					palya.mezok[i][j].latszik_e = true;
				}
			}
		}
		Assert.assertTrue(palya.NyeresiKondicio());
		Assert.assertTrue(palya.GameOver);
	}

	/*
	 * Azt ellenorzi, hogy tenyleg megfeleloen mukodik-e a NyeresiKondicio metodus.
	 */
	@Test
	public void NyeresiKondicioNotYetWin() {
		Assert.assertFalse(palya.NyeresiKondicio());
		Assert.assertFalse(palya.GameOver);
	}

	/*
	 * Azt ellenorzi, hogy tenyleg megfeleloen mukodik-e a SzomszedAknaSzam metódus,
	 * amely a mezo kozvetlen kozelebe levo aknak szamat adja meg.
	 */
	@Test
	public void SzomszedAknaSzam() {
		Random rnd = new Random(System.currentTimeMillis());
		boolean kilep = false;
		int sor = 0;
		int oszlop = 0;
		while (!kilep) {
			sor = rnd.nextInt(palya.sor_oszlop);
			oszlop = rnd.nextInt(palya.sor_oszlop);
			if (!palya.mezok[sor][oszlop].akna_e) {
				kilep = true;
			}
		}

		int aknadb = 0;
		for (int i = sor - 1; i <= sor + 1; i++) {
			for (int j = oszlop - 1; j <= oszlop + 1; j++) {
				if (i >= 0 && i < palya.sor_oszlop && j >= 0 && j < palya.sor_oszlop) {
					if (palya.aknaterkep.aknaHely[i][j] == true) {
						aknadb++;
					}
				}
			}
		}

		Assert.assertEquals(palya.SzomszedAknaSzam(palya.mezok[sor][oszlop]), aknadb);
	}

	/*
	 * Az ellenorzi, hogy egy zaszlo nelkuli mezo megfelelo szamot mutat-e, ez
	 * fokent a SzomszedFelfed metodust vizsgalja.
	 */
	@Test
	public void SzomszedFelfedZaszloNelkul() {
		palya.SzomszedFelfed(palya.mezok[sor][oszlop]);
		String eredmeny = "";
		if (palya.SzomszedAknaSzam(palya.mezok[sor][oszlop]) != 0) {
			eredmeny = String.valueOf(palya.SzomszedAknaSzam(palya.mezok[sor][oszlop]));
		}
		Assert.assertEquals(palya.mezok[sor][oszlop].getText(), eredmeny);
		Assert.assertTrue(palya.mezok[sor][oszlop].latszik_e);
		Assert.assertEquals(palya.mezok[sor][oszlop].getBackground(), Color.white);
	}

	/*
	 * Az ellenorzi, hogy egy zaszlozott mezo megfelelo szamot mutat-e ez fokent a
	 * SzomszedFelfed metodust vizsgalja. Valamint, hogy a zaszlok szama megfeleloen
	 * valtozik-e.
	 */
	@Test
	public void SzomszedFelfedZaszloval() {
		palya.mezok[sor][oszlop].zaszlozott_e = true;
		palya.zaszlokSzama++;

		palya.SzomszedFelfed(palya.mezok[sor][oszlop]);
		String eredmeny = "";
		if (palya.SzomszedAknaSzam(palya.mezok[sor][oszlop]) != 0) {
			eredmeny = String.valueOf(palya.SzomszedAknaSzam(palya.mezok[sor][oszlop]));
		}
		Assert.assertEquals(palya.mezok[sor][oszlop].getText(), eredmeny);
		Assert.assertTrue(palya.mezok[sor][oszlop].latszik_e);
		Assert.assertEquals(palya.mezok[sor][oszlop].getBackground(), Color.white);
		Assert.assertEquals(palya.mezok[sor][oszlop].getIcon(), null);
		Assert.assertEquals(palya.zaszlokSzama, palya.aknakSzama);
	}

	/*
	 * Azt vizsgalja megfeleloen mukodik-e a jatek elvesztese soran lefuto
	 * AknakFelfedese metodus.
	 */
	@Test
	public void AknakFelfedese() {
		palya.AknakFelfedese(palya.mezok[sor][oszlop]);
		for (int i = 0; i < 10; i++) {
			for (int j = 0; j < 10; j++) {
				if (palya.mezok[i][j].akna_e) {
					Assert.assertEquals(palya.mezok[i][j].getForeground(), Color.black);
					Assert.assertEquals(palya.mezok[i][j].getBackground(), Color.red);
					Assert.assertNotEquals(palya.mezok[i][j].getIcon(), null);
				}
			}
		}
	}

}
