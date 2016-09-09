package PlanetCrash.ui.Scenes;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.swing.BorderFactory;
import javax.swing.JLayeredPane;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;












import com.mysql.jdbc.StringUtils;

import PlanetCrash.core.Exceptions.NotFoundException;
import PlanetCrash.core.Game.Game;
import PlanetCrash.core.Game.GameUtils;
import PlanetCrash.core.config.Config;
import PlanetCrash.db.DatabaseHandler;
import PlanetCrash.db.Updates.ManualUpdater;
import PlanetCrash.ui.GameGUI;
import PlanetCrash.ui.Objects.JImage;
import PlanetCrash.ui.Objects.JRoundedButton;
import PlanetCrash.ui.Objects.JRoundedEditText;
import PlanetCrash.ui.Objects.StarryBackground;

public class HallOfFameScene extends Scene{
	
	public HallOfFameScene(GameGUI gameGUI, Game game) {
		super(gameGUI, game);
	}

	JRoundedEditText nameField;
	JRoundedEditText continetField;
	JRoundedEditText currencyField;
	JRoundedEditText languageField;
	JRoundedEditText capitalField;
	JRoundedEditText populationField;

	JRoundedButton backBtn = new JRoundedButton("Back", 100, 60, 2);
	JRoundedButton addBtn = new JRoundedButton("Add", 220, 60, 2);
	
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
		int loginy = 200;
		
		//init user/pass font
		Font userpass = new Font(null, Font.BOLD, 14);
		
		
		LinkedHashMap<String,Integer> hof = GameUtils.getHighScores(gameGUI.mConnPool, new Config());
		
		
	    Iterator it = hof.entrySet().iterator();
	    int i = 200;
	    while (it.hasNext()) {
	    	
	        Map.Entry pair = (Map.Entry)it.next();
	        
			//add welcome message
			Font font = new Font(null, Font.HANGING_BASELINE, 20);
			
					
			JTextField username = new JTextField(pair.getKey().toString());
			username.setFont(font);
			username.setForeground(Color.CYAN);
			username.setBounds(250, i,
					(int)username.getPreferredSize().getWidth(), (int)username.getPreferredSize().getHeight());
			username.setOpaque(false);
			username.setBorder(BorderFactory.createEmptyBorder());
			
			JTextField score = new JTextField(pair.getValue().toString());
			score.setFont(font);
			score.setForeground(Color.CYAN);
			score.setBounds(550, i,
					(int)score.getPreferredSize().getWidth(), (int)score.getPreferredSize().getHeight());
			score.setOpaque(false);
			score.setBorder(BorderFactory.createEmptyBorder());
			
			panel.add(username, new Integer(3),0);
			panel.add(score, new Integer(3),0);
			
	
			i += 70;
	        
	        it.remove(); 
	    }
			
		backBtn.setBorderColor(Color.green);
		backBtn.setBounds(30, 500, backBtn.getWidth(), backBtn.getHeight());
		panel.add(backBtn, new Integer(2), 2);

		backBtn.addMouseListener(new MainListener(MainListener.BACK));
		return panel;
	}
	
	
	class MainListener implements MouseListener {

		public static final int BACK=0,ADD_COUNTRY=1;
		int mode;
		public MainListener(int mode) {
			this.mode=mode;
		}		
		
		
		@Override
		public void mouseClicked(MouseEvent arg0) {
			switch(mode) {
			case BACK:
				MainMenuScene mms = new MainMenuScene(gameGUI,game);	
				gameGUI.fadeSwitchScene(mms);		
				break;
			}
			
			
		}
		
		public boolean check_not_empty_fields(){
			if (continetField.getText().isEmpty() || currencyField.getText().isEmpty() || 
				languageField.getText().isEmpty() || capitalField.getText().isEmpty() ||
				populationField.getText().isEmpty() || nameField.getText().isEmpty())
			{
				JOptionPane.showMessageDialog(gameGUI.mainFrame, "Don't leave fields empty!");
				return false;
			}
			return true;
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
