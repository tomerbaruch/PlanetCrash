package PlanetCrash.ui.Objects;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;

import javax.swing.JLabel;
import javax.swing.JPanel;

public abstract class JRounded extends JPanel{
	protected int width,height,borderThickness,arc=30;
	protected Color borderColor,backgroundColor;
	
	public JRounded(int width, int height, int borderThickness) {
		this(Color.orange, Color.decode("#3B5998"), width,height,borderThickness);
	}
	
	public JRounded(Color borderColor, Color backgroundColor, int width, int height, int borderThickness) {
		super();
		
		this.borderColor=borderColor;
		this.backgroundColor=backgroundColor;
		this.width=width;
		this.height=height;
		this.borderThickness=borderThickness;
				
		setOpaque(false);
		setDoubleBuffered(true);
	}
	
	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		Color pastC = g.getColor();
		g.setColor(borderColor);
		g.fillRoundRect(0, 0, width, height, arc, arc);
		g.setColor(backgroundColor);
		g.fillRoundRect(borderThickness, borderThickness, width-(borderThickness*2), height-(borderThickness*2), arc, arc);
		g.setColor(borderColor);
		//label.paint(g);
		g.setColor(pastC);
	}
	
	@Override
	public Dimension getPreferredSize() {
		return new Dimension(width,height);
	}
	
	public int getWidth() {
		return width;
	}
	
	public int getHeight() {
		return height;
	}
	
	public void setBorderColor(Color c) {
		this.borderColor=c;
	}
	
	public void setBackgroundColor(Color c) {
		this.backgroundColor=c;
	}
	
	public Color getBorderColor() {
		return this.borderColor;
	}
	
	public Color getBackgroundColor() {
		return this.backgroundColor;
	}
}
