package PlanetCrash.ui.Scenes;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;

import javax.swing.JLayeredPane;
import javax.swing.JPanel;

import PlanetCrash.core.Game.Game;
import PlanetCrash.ui.GameGUI;
import PlanetCrash.ui.Objects.JImage;
import PlanetCrash.ui.Objects.JRoundedButton;
import PlanetCrash.ui.Objects.JRoundedEditText;
import PlanetCrash.ui.Objects.StarryBackground;

public class SettingsScene extends Scene{
	
	public SettingsScene(GameGUI gameGUI, Game game) {
		super(gameGUI, game);
	}
	
	Font userpass = new Font(null, Font.BOLD, 14);
	JRoundedEditText passwordField;
	
	int per = 0;

	
	JRoundedButton reloadBtn = new JRoundedButton("Reload entire YAGO", 500, 60, 2);
	JRoundedButton addBtn = new JRoundedButton("Add country", 500, 60, 2);
	JRoundedButton editBtn = new JRoundedButton("Edit country", 500, 60, 2);
	JRoundedButton backBtn = new JRoundedButton("Back", 100, 60, 2);
	JRoundedEditText usernameField = new JRoundedEditText(userpass,"Test", 220, 60, 2, false);
	
	@Override
	public Component create() {
		JLayeredPane panel = new JLayeredPane();
		panel.setPreferredSize(new Dimension(GameGUI.WINDOW_WIDTH,GameGUI.WINDOW_HEIGHT));

		JPanel s = emptyMainJPanel();
		panel.add(s, new Integer(0),0);

		//Add background
		StarryBackground sb = new StarryBackground();
		s.add(sb);

		//Add logo
		JImage logo = new JImage(new File(GameGUI.ASSETS+"logo.png"));
		logo.setBounds((GameGUI.WINDOW_WIDTH-logo.getWidth())/2,80,logo.getWidth(),logo.getHeight());
		logo.setOpaque(false);
		panel.add(logo, new Integer(1), 0);
		
		//login x,y
		int loginy = 250;
		
		//init user/pass font
		
		

		
		//Add login button
		reloadBtn.setBorderColor(Color.green);
		reloadBtn.setBounds((GameGUI.WINDOW_WIDTH-reloadBtn.getWidth())/2, 250, reloadBtn.getWidth(), reloadBtn.getHeight());
		panel.add(reloadBtn, new Integer(2), 2);
		
		//Add login button
		addBtn.setBorderColor(Color.green);
		addBtn.setBounds((GameGUI.WINDOW_WIDTH-addBtn.getWidth())/2, 325, addBtn.getWidth(), addBtn.getHeight());
		panel.add(addBtn, new Integer(2), 2);
		
		//Add login button
		editBtn.setBorderColor(Color.green);
		editBtn.setBounds((GameGUI.WINDOW_WIDTH-editBtn.getWidth())/2, 400, editBtn.getWidth(), editBtn.getHeight());
		panel.add(editBtn, new Integer(2), 2);
		
		backBtn.setBorderColor(Color.green);
		backBtn.setBounds(30, 500, backBtn.getWidth(), backBtn.getHeight());
		panel.add(backBtn, new Integer(2), 2);
								
		//Register action listeners
		reloadBtn.addMouseListener(new MainListener(MainListener.ADD_ALL));
		addBtn.addMouseListener(new MainListener(MainListener.ADD_COUNTRY));
		editBtn.addMouseListener(new MainListener(MainListener.EDIT_COUNTRY));
		backBtn.addMouseListener(new MainListener(MainListener.BACK));

		return panel;
	}
	

	class LoadToYagoListener implements MouseListener {

		@Override
		public void mouseClicked(MouseEvent arg0) {
			LoadFromYagoScene mms = new LoadFromYagoScene(gameGUI,game);	
			gameGUI.fadeSwitchScene(mms);
		}		
		

		@Override
		public void mouseEntered(MouseEvent arg0) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void mouseExited(MouseEvent arg0) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void mousePressed(MouseEvent arg0) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void mouseReleased(MouseEvent arg0) {
			// TODO Auto-generated method stub
			
		}
		
	}
	
	
	class MainListener implements MouseListener {
		public static final int ADD_ALL=0,ADD_COUNTRY=1,EDIT_COUNTRY=2,BACK=3;
		int mode;
		public MainListener(int mode) {
			this.mode=mode;
		}
		@Override
		public void mouseClicked(MouseEvent arg0) {
			switch(mode) {
			case ADD_ALL:
				LoadFromYagoScene mms = new LoadFromYagoScene(gameGUI,game);	
				gameGUI.fadeSwitchScene(mms);		
				break;
			case ADD_COUNTRY:
				AddCountryScene mms2 = new AddCountryScene(gameGUI,game);	
				gameGUI.fadeSwitchScene(mms2);
				break;
			case EDIT_COUNTRY:
				EditCountryScene mms3 = new EditCountryScene(gameGUI,game);	
				gameGUI.fadeSwitchScene(mms3);
				break;
			case BACK:
				MainMenuScene mms4 = new MainMenuScene(gameGUI,game);	
				gameGUI.fadeSwitchScene(mms4);
				break;
			}	
			

		}		
		

		@Override
		public void mouseEntered(MouseEvent arg0) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void mouseExited(MouseEvent arg0) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void mousePressed(MouseEvent arg0) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void mouseReleased(MouseEvent arg0) {
			// TODO Auto-generated method stub
			
		}
		
	}
	
	class EditCountryListener implements MouseListener {

		@Override
		public void mouseClicked(MouseEvent arg0) {
			EditCountryScene mms = new EditCountryScene(gameGUI,game);	
			gameGUI.fadeSwitchScene(mms);
		}		
		

		@Override
		public void mouseEntered(MouseEvent arg0) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void mouseExited(MouseEvent arg0) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void mousePressed(MouseEvent arg0) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void mouseReleased(MouseEvent arg0) {
			// TODO Auto-generated method stub
			
		}
		
	}


}
