package PlanetCrash.ui.Objects;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JLabel;

public class JRoundedButton extends JRounded {
	String text;
	JLabel label;
	Font font;
	Color origBGC, origBRDC;

	MouseListener ml;
	
	List<MouseListener> mls = new ArrayList<MouseListener>();

	public JRoundedButton(Font font, String text, int width, int height, int borderThickness) {
		super(width, height,borderThickness);

		//init font
		this.font = font;

		//init label
		label = new JLabel(text);
		label.setFont(font);
		label.setForeground(borderColor);
		//				label.setBounds(0, 0, 10, 10);
		add(label);

		this.text=text;

		label.setText(text);

		registerGraphicListener();
	}

	public JRoundedButton(String text,int width, int height, int borderThickness) {
		super(width,height,borderThickness);

		//init font
		font = new Font(null, Font.BOLD, 36);

		//init label
		label = new JLabel(text);
		label.setFont(font);
		label.setForeground(borderColor);
		//				label.setBounds(0, 0, 10, 10);
		add(label);

		this.text=text;

		label.setText(text);

		registerGraphicListener();
	}


	public void isButton(boolean isButton) {
		if(ml!=null)
			removeMouseListener(ml);
		if(isButton)
			registerGraphicListener();
	}

	public void setText(String text) {
		this.text=text;
		label.setText(text);
		this.repaint();
	}

	public void setBorderColor(Color c) {
		super.setBorderColor(c);
		label.setForeground(borderColor);
	}	

	@Override
	public void setBackgroundColor(Color c) {
		super.setBackgroundColor(c);
	}

	public void setLabelFont(Font font) {
		this.label.setFont(font);
	}

	private void registerGraphicListener() {
		origBGC=getBackgroundColor();
		//origBRDC=getBorderColor();

		ml = new MouseListener() {

			@Override
			public void mouseReleased(MouseEvent e) {
				// TODO Auto-generated method stub

			}

			@Override
			public void mousePressed(MouseEvent e) {
				// TODO Auto-generated method stub

			}

			@Override
			public void mouseExited(MouseEvent e) {
				setBackgroundColor(origBGC);
				if(label != null)
					label.setForeground(getBorderColor());
				repaint();
			}

			@Override
			public void mouseEntered(MouseEvent e) {
				setBackgroundColor(origBRDC);
				if(label != null)
					label.setForeground(origBGC);
				repaint();
			}

			@Override
			public void mouseClicked(MouseEvent e) {
				// TODO Auto-generated method stub

			}
		};

		this.addMouseListener(ml);
	}

	@Override
	public synchronized void addMouseListener(MouseListener l) {
		super.addMouseListener(l);
		mls.add(l);
	}
	
	public void removeAllMouseListeners() {
		for(MouseListener m : mls)
			removeMouseListener(m);
		mls=new ArrayList<MouseListener>();
	}
}
