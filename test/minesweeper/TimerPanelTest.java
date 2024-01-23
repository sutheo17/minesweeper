package minesweeper;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/*
 * Az idozito megfelelo mukodeset ellenorzi.
 */
public class TimerPanelTest {

	Aknakereso ak;

	/*
	 * Egy aknakereso peldany letrehozasa a tesztesetek elott.
	 */
	@Before
	public void setUp() {
		ak = new Aknakereso();
		ak.setVisible(false);
	}

	/*
	 * Idovel letrehozott idozito alapbeallitasait ellenorzi.
	 */
	@Test
	public void InicializalasIdovel() {
		TimerPanel timerpanel = new TimerPanel(1000, ak);
		Assert.assertEquals(timerpanel.megjelenito, ak);
		Assert.assertEquals(timerpanel.ido, 1000);
		Assert.assertFalse(timerpanel.ido_nelkul);
		Assert.assertEquals(timerpanel.felirat.getText(), "1000");
		timerpanel.timer.stop();
	}

	/*
	 * Ido nelkül letrehozott idozito alapbeallitasait ellenorzi.
	 */
	@Test
	public void InicializalasIdoNelkul() {
		TimerPanel timerpanel = new TimerPanel(ak);
		Assert.assertEquals(timerpanel.megjelenito, ak);
		Assert.assertTrue(timerpanel.ido_nelkul);
		Assert.assertEquals(timerpanel.felirat.getText(), "0");
	}

	/*
	 * Az eltelidot ellenorzi idozitos esetben.
	 */
	@Test
	public void ElteltIdo_Idovel() throws InterruptedException {
		TimerPanel timerpanel = new TimerPanel(1000, ak);
		Thread.sleep(2000);
		Assert.assertEquals(timerpanel.elteltido, 1);
	}

	/*
	 * Az eltelidot ellenorzi idozito nelkuli esetben.
	 */
	@Test
	public void ElteltIdo_Idonelkul() throws InterruptedException {
		TimerPanel timerpanel = new TimerPanel(ak);
		Thread.sleep(2000);
		Assert.assertEquals(timerpanel.elteltido, 1);
	}

}
