package PlanetCrash.ui.Objects;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;

public class Star {
	double angle;
	int startX,startY,endX,endY,velocity;
	float acceleration;
	int x,y;
	long starttime;
	Color color = Color.WHITE;
	
	public Star(double angle,int startX,int startY, int startVelocity, float acceleration) {
		this.angle=angle;
		this.x=this.startX=startX;
		this.y=this.startY=startY;
		this.velocity=startVelocity;
		this.acceleration=acceleration;
		this.starttime=System.currentTimeMillis();
	}
	
	public void paint(Graphics g) {
		Color c = g.getColor();
		g.setColor(color);
		((Graphics2D)g).fillOval(x, y, 3, 3);
		g.setColor(c);
	}
	
	private float percentLifetime() {
		return 0;
	}
	
	public int getX() {
		return this.x;
	}
	
	public int getY() {
		return this.y;
	}
	
	public void setX(int x) {
		this.x=x;
	}
	
	public void setY(int y) {
		this.y=y;
	}
	
	public double getAngle() {
		return this.angle;
	}
	
	public void setVelocity(int v) {
		this.velocity=v;
	}
	
	public int getVelocity() {
		return this.velocity;
	}
	
	public void setColor(Color color) {
		this.color=color;
	}
}
