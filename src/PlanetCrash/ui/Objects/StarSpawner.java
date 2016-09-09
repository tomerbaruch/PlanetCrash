package PlanetCrash.ui.Objects;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JPanel;

import PlanetCrash.ui.GameGUI;

public class StarSpawner extends JPanel implements Runnable{

	List<Star> toPaint,stars, dump, newStars;
	int starLimit;
	int x,y,startVelocity=3;
	float acceleration;
	final int DELAY = 25;

	static long allowedThread;
	
	final Color colors[] = {Color.CYAN, Color.RED, Color.YELLOW, Color.GREEN, Color.PINK, Color.ORANGE};

	//A star spawner to which all stars will relate
	public StarSpawner(int x, int y, int starLimit, float acceleration) {
		setBorder(BorderFactory.createLineBorder(Color.black));
		setBackground(Color.BLACK);
		setDoubleBuffered(true);

		this.x=x;
		this.y=y;

		this.starLimit=starLimit;
		this.acceleration=acceleration;

		this.stars = new ArrayList<Star>();
		this.dump = new ArrayList<Star>();
		this.newStars = new ArrayList<Star>();

		//		while(newStars.size()<starLimit)
		//			spawnStar();
		//		stars = newStars;
		//		newStars = new ArrayList<Star>();

	}


	private void spawnStar() {
		double angle = Math.random()*Math.PI*2;
		int startX = (int)Math.ceil(this.x+5*Math.cos(angle));
		int startY = (int)Math.ceil(this.y+5*Math.sin(angle));


		Star s = new Star(angle, startX, startY, startVelocity, acceleration);
		if(Math.random()>0.8)
			s.setColor(colors[(int)(Math.random()*colors.length)]);
		newStars.add(s);
	}

	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		
		for (Star s : toPaint)
			s.paint(g);
	}

	private void cycle(long timeDelay) {
		if(stars.size()<starLimit)
			spawnStar();
		stars.addAll(newStars);
		newStars = new ArrayList<Star>();

		double delay = timeDelay/1000d;
		for (Star s : stars) {
			int dist = (int) (s.getVelocity()*delay+0.5*acceleration*delay*delay);
			int newX = s.getX()+(int)(Math.cos(s.getAngle())*dist);
			int newY = s.getY()+(int) (Math.sin(s.getAngle())*dist);
			if(newX < 0 || newY < 0 || newX > GameGUI.WINDOW_WIDTH || newY > GameGUI.WINDOW_HEIGHT) {
				dump.add(s);
				spawnStar();
			} else {
				s.setX(newX);
				s.setY(newY);
				s.setVelocity((int)(s.getVelocity()+timeDelay*acceleration));
			}

		}
		for (Star s : dump)
			stars.remove(s);
		dump = new ArrayList<Star>();

		toPaint = new ArrayList<Star>(stars);
	}


	@Override
	public void run() {
		long sleep,timediff,beforetime = System.currentTimeMillis();
		
		StarSpawner.allowedThread=Thread.currentThread().getId();
		
		while(Thread.currentThread().getId()==StarSpawner.allowedThread) {
			timediff = System.currentTimeMillis()-beforetime;
			sleep = DELAY - timediff;
			cycle(sleep>0?sleep:2);
			this.repaint();

			try {
				Thread.sleep(sleep>0?sleep:2);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}


			beforetime = System.currentTimeMillis();
		}

	}

	public Dimension getPreferredSize() {
		return new Dimension(GameGUI.WINDOW_WIDTH,GameGUI.WINDOW_HEIGHT);
	}
}
