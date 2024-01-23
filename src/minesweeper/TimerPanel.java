package minesweeper;

import java.awt.Font;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.Timer;

import javax.swing.JLabel;
import javax.swing.JPanel;

/*
 * Az ido szamontartasaert felelos osztaly, JPanel leszarmazottja.
 */

public class TimerPanel extends JPanel {

	private static final long serialVersionUID = 1L;
	JLabel felirat;
	Timer timer;
	int ido;
	int elteltido;
	Aknakereso megjelenito;
	boolean ido_nelkul;

	/*
	 * Konstruktor arra az esetre, ha van idokorlat.
	 * 
	 * @param i - idokorlat, egesz szamkent
	 * 
	 * @param ak - maga a timert meghivo Aknakereso peldany
	 */
	public TimerPanel(int i, Aknakereso ak) {
		ido_nelkul = false;
		elteltido = 0;
		ido = i;
		megjelenito = ak;
		String idoString = String.valueOf(ido);
		felirat = new JLabel(idoString);
		felirat.setFont(new Font("Arial", Font.BOLD, 20));
		setLayout(new GridBagLayout());
		this.add(felirat);
		timer = new Timer(1000, new TimerListener());
		timer.setInitialDelay(1000);
		timer.start();
	}

	/*
	 * Konstruktor arra az esetre, ha nincs idokorlat.
	 * 
	 * @param ak - maga a timert meghivo Aknakereso peldany
	 */
	public TimerPanel(Aknakereso ak) {
		ido_nelkul = true;
		elteltido = 0;
		megjelenito = ak;
		timer = new Timer(1000, new TimerListener());
		timer.setInitialDelay(1000);
		timer.start();
		felirat = new JLabel("0");
		felirat.setFont(new Font("Arial", Font.BOLD, 20));
		setLayout(new GridBagLayout());
		this.add(felirat);
	}

	/*
	 * Ez a belso osztaly vegzi lenyegeben az ido szamontartasat. Frissiti
	 * folyamatosan a jatek soran hatralevo idot, valamint az eltelt idot is. Ha
	 * lejar az ido meghivja az Aknakereso osztaly megfelelo metodusat.
	 */
	private class TimerListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent ae) {
			elteltido++;
			if (ido_nelkul == false) {
				ido--;
				if (ido >= 0) {
					felirat.setText(Integer.toString(ido));
				} else {
					((Timer) (ae.getSource())).stop();
					if (megjelenito != null) {
						megjelenito.GameOverIdo();
					}

				}
			} else {
				felirat.setText(Integer.toString(elteltido));
			}

		}

	}

}
