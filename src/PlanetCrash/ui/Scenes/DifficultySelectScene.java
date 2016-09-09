package PlanetCrash.ui.Scenes;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.io.FileFilter;
import java.io.FileNotFoundException;
import java.sql.SQLException;

import javax.swing.BorderFactory;
import javax.swing.JLayeredPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import PlanetCrash.core.Game.Game;
import PlanetCrash.core.Game.GameUtils;
import PlanetCrash.core.Game.QuestionsGenerator;
import PlanetCrash.core.config.Config;
import PlanetCrash.db.DatabaseHandler;
import PlanetCrash.ui.GameGUI;
import PlanetCrash.ui.Objects.JImage;
import PlanetCrash.ui.Objects.JRoundedButton;
import PlanetCrash.ui.Objects.StarryBackground;

public class DifficultySelectScene extends Scene{
	

	public DifficultySelectScene(GameGUI gameGUI, Game game) {
		super(gameGUI, game);
	}

	@Override
	public Component create() {
		JLayeredPane panel = new JLayeredPane();
		panel.setPreferredSize(new Dimension(GameGUI.WINDOW_WIDTH,GameGUI.WINDOW_HEIGHT));

		JPanel s = emptyMainJPanel();
		panel.add(s, new Integer(0),0);

		//define font
		Font font = new Font(null, Font.PLAIN, 20);

		//Add background
		StarryBackground sb = new StarryBackground();
		s.add(sb);

		//Add logo
		JImage logo = new JImage(new File(GameGUI.ASSETS+"logo.png"));
		logo.setBounds((GameGUI.WINDOW_WIDTH-logo.getWidth())/2,80,logo.getWidth(),logo.getHeight());
		logo.setOpaque(false);
		panel.add(logo, new Integer(1), 0);

		//Add easy button
		JRoundedButton easyBtn = new JRoundedButton("Land", 220,60, 2);
		easyBtn.setBounds((GameGUI.WINDOW_WIDTH-easyBtn.getWidth())/2, 280, easyBtn.getWidth(), easyBtn.getHeight());
		easyBtn.setBorderColor(Color.green);
		//		easyBtn.setLabelFont(font);
		panel.add(easyBtn, new Integer(2), 0);

		//Add medium button
		JRoundedButton mediumBtn = new JRoundedButton("Bump", 220,60, 2);
		mediumBtn.setBounds((GameGUI.WINDOW_WIDTH-mediumBtn.getWidth())/2, 370, mediumBtn.getWidth(), mediumBtn.getHeight());
		mediumBtn.setBorderColor(Color.yellow);
		//		mediumBtn.setLabelFont(font);
		panel.add(mediumBtn, new Integer(3), 0);

		//Add hard button
		JRoundedButton hardBtn = new JRoundedButton("Crash", 220,60, 2);
		hardBtn.setBounds((GameGUI.WINDOW_WIDTH-hardBtn.getWidth())/2, 460, hardBtn.getWidth(), hardBtn.getHeight());
		hardBtn.setBorderColor(Color.red);
		//		hardBtn.setLabelFont(font);
		panel.add(hardBtn, new Integer(3), 0);

		//Add difficulty message
		JTextField welcomeMsg = new JTextField("Please choose difficulty:");
		welcomeMsg.setFont(font);
		welcomeMsg.setForeground(Color.CYAN);
		welcomeMsg.setBounds((GameGUI.WINDOW_WIDTH-(int)welcomeMsg.getPreferredSize().getWidth())/2, 240,
				(int)welcomeMsg.getPreferredSize().getWidth(), (int)welcomeMsg.getPreferredSize().getHeight());
		welcomeMsg.setOpaque(false);
		welcomeMsg.setBorder(BorderFactory.createEmptyBorder());
		panel.add(welcomeMsg, new Integer(3),0);
		
		//Add back button
		JRoundedButton backBtn = new JRoundedButton("Back", 100, 60, 2);
		backBtn.setBorderColor(Color.green);
		backBtn.setBounds(30, 500, backBtn.getWidth(), backBtn.getHeight());
		panel.add(backBtn, new Integer(2), 2);

		//Register the mouse listeners
		easyBtn.addMouseListener(new DifficultyListener(5));
		mediumBtn.addMouseListener(new DifficultyListener(10));
		hardBtn.addMouseListener(new DifficultyListener(15));
		backBtn.addMouseListener(new BackListener());

		return panel;
	}

	public class DifficultyListener implements MouseListener {
		int difficulty;

		public DifficultyListener(int difficulty) {
			this.difficulty=difficulty;
		}

		@Override
		public void mouseClicked(MouseEvent arg0) {
			//Set game difficuty
			game.setDifficulty(difficulty);

			//Set game country+questions
			new Thread(new Runnable() {

				@Override
				public void run() {
					// TODO Auto-generated method stub
					QuestionsGenerator qg = null;
					while(qg==null) {
						Config cfg = new Config();
						DatabaseHandler dbh = new DatabaseHandler(gameGUI.mConnPool);
						qg = GameUtils.generateCountry(game.getUser().getId(), cfg.get_db_name(), dbh, game.getDifficulty());
						if (qg==null)
							continue;
						game.setCountryId(qg.getCountryId());
						game.setCountry(qg.getCountry());
						game.setQuestions(qg.getPossibleQuestions());
						game.setPlanetName(GameUtils.funkName(game.getCountry()));

//						try {
							dbh.close();
//						} catch (SQLException e) {
//							// TODO Auto-generated catch block
//							e.printStackTrace();
//						}
					}
				}
			}).start();

			//Set planet variables
			game.setBackdrop(Game.BACKDROPS[(int)(Math.random()*Game.BACKDROPS.length)]);
			game.setLand(Game.LANDS[(int)(Math.random()*Game.LANDS.length)]);

			//Set soldiers
			try{
				File soldiersDir = new File(GameGUI.ASSETS+"soldiers");
				if(!soldiersDir.isDirectory() || !soldiersDir.exists())
					throw new FileNotFoundException("Could not find soldiers directory.");
				File[] soldiers = soldiersDir.listFiles(new FileFilter() {
					
					@Override
					public boolean accept(File pathname) {
						if(!pathname.getName().endsWith(".png"))
							return false;
						return true;
					}
				});
				File soldierImg = soldiers[(int)(Math.random()*soldiers.length)];
				game.setSoldier(soldierImg);

			} catch(Exception e) {
				//Show dialog here
				return;
			}

			//Set lives
			game.setLives(4);

			LoadingScene ls = new LoadingScene(gameGUI, game);
			gameGUI.fadeSwitchScene(ls);

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

	public class BackListener implements MouseListener {

		public static final int BACK=0,ADD_COUNTRY=1;
		int mode;
		public BackListener() {
		}		
		
		
		@Override
		public void mouseClicked(MouseEvent arg0) {
				MainMenuScene mms = new MainMenuScene(gameGUI,game);	
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
