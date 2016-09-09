package PlanetCrash.ui.Objects;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JPanel;

import PlanetCrash.ui.GameGUI;


public class JImage extends JPanel{
	
	private BufferedImage image;
	
	public JImage(File imgFile) {
		if(imgFile == null) {
			System.out.println("Null image file");
			return;
		}
			
		try {
			this.image = ImageIO.read(imgFile);
		} catch (IOException e) {
			System.out.println("Could not load image "+imgFile.getAbsolutePath());
			e.printStackTrace();
		}
	}
	
	@Override
	public Dimension getPreferredSize() {
		return new Dimension(image.getWidth(),image.getHeight());
	}
	
	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		g.drawImage(image,0,0,null);
	}

	public int getWidth() {
		return image.getWidth();
	}
	
	public int getHeight() {
		return image.getHeight();
	}
}
