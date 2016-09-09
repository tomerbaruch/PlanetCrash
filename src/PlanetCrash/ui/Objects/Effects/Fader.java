package PlanetCrash.ui.Objects.Effects;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;

import javax.swing.JPanel;

import PlanetCrash.ui.GameGUI;

public class Fader  extends JPanel implements Runnable{
	public static final int DELAY=25;
	int width=GameGUI.WINDOW_WIDTH,height=GameGUI.WINDOW_HEIGHT;
	int R,G,B;
	boolean outFader;
	
	long duration;

	double percent=0;

	/**
	 * @param duration fade out duration in miliseconds
	 */
	public Fader(long duration, boolean outFader, int R, int G, int B) {
		this.duration=duration;
		this.outFader=outFader;
		this.setOpaque(false);
	}
	
	public Fader(long duration, boolean outFader) {
		this(duration,outFader,0,0,0);
	}

	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);

		Color c = g.getColor();
		g.setColor(new Color(R,G,B,(int)(Math.max(Math.min((outFader?255*percent:255-255*percent),255),0))));
		g.fillRect(0, 0, width, height);
		g.setColor(c);
	}

	@Override
	public Dimension getPreferredSize() {
		return new Dimension(width,height);
	}

	@Override
	public void run() {
		long start=System.currentTimeMillis();
		long end = start+duration;

		while(System.currentTimeMillis()<end) {
			percent=((double)System.currentTimeMillis()-start)/((double)end-start);
			this.repaint();
		}

	}
}

