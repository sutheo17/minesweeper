package minesweeper;

import java.util.ArrayList;
import java.util.List;

import java.awt.Color;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

/*
 * A mezok alapveto funkcioinak tesztelese parameteres formaban, vegigjarja az osszes lehetseges mezo poziciot.
*/
@RunWith(Parameterized.class)
public class CellaTest {

	Cella mezo;
	int sor;
	int oszlop;

	/*
	 * CellaTest konstruktora.
	 */
	public CellaTest(int s, int o) {
		this.sor = s;
		this.oszlop = o;
	}

	/*
	 * Cella letrehozasa megfelelo parameterekkel.
	 */
	@Before
	public void setUp() {
		mezo = new Cella(sor, oszlop);
		mezo.ikonMeret();
	}

	/*
	 * A cella sor, valamint oszlopszamanak ellenorzese.
	 */
	@Test
	public void CellaLetrehoz() {
		Cella ujmezo = new Cella(sor, oszlop);
		Assert.assertEquals(ujmezo.sor, sor);
		Assert.assertEquals(ujmezo.oszlop, oszlop);
	}

	/*
	 * A cella alapveto beallitasait ellenorzi.
	 */
	@Test
	public void CellaAlapBeallitasok() {
		mezo.ujJatek(true);
		Assert.assertTrue(mezo.akna_e);
		Assert.assertFalse(mezo.latszik_e);
		Assert.assertFalse(mezo.zaszlozott_e);
		Assert.assertEquals(mezo.getBackground(), Color.DARK_GRAY);
	}

	/*
	 * Nem latszo mezo szinenek ellenorzese.
	 */
	@Test
	public void MezoSzinez() {
		mezo.ujJatek(false);
		mezo.latszik_e = true;
		mezo.mezoSzinez();
		Assert.assertEquals(mezo.getBackground(), Color.WHITE);
	}

	/*
	 * Akna szinenek, ikonjanak ellenorzese.
	 */
	@Test
	public void AknaSzinez() {
		mezo.ujJatek(true);
		mezo.aknaSzinez(sor + 1);
		Assert.assertEquals(mezo.getBackground(), Color.red);
		Assert.assertEquals(mezo.getForeground(), Color.black);
		Assert.assertNotEquals(mezo.getIcon(), null);
	}

	/*
	 * Zaszlozott mezo szinenek, ikonjanak ellenorzese.
	 */
	@Test
	public void ZaszloSzinez() {
		mezo.ujJatek(false);
		mezo.zaszloSzinez(sor + 1);
		Assert.assertEquals(mezo.getBackground(), Color.blue);
		Assert.assertEquals(mezo.getForeground(), Color.black);
		Assert.assertNotEquals(mezo.getIcon(), null);
	}

	/*
	 * Megfelelo parameterek letrehozasa, az osszes lehetseges mezovel.
	 */
	@Parameters
	public static List<Object[]> parameters() {
		List<Object[]> params = new ArrayList<Object[]>();
		for (int i = 0; i < 10; i++) {
			for (int j = 0; j < 10; j++) {
				params.add(new Object[] { i, j });
			}
		}
		return params;
	}
}
