package PlanetCrash.ui.Scenes;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.sql.SQLException;

import javax.swing.BorderFactory;
import javax.swing.JLayeredPane;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.text.DefaultStyledDocument;
import javax.swing.text.Style;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyleContext;
import javax.swing.text.StyledDocument;

import PlanetCrash.core.Game.Game;
import PlanetCrash.db.DatabaseHandler;
import PlanetCrash.ui.GameGUI;
import PlanetCrash.ui.Objects.JImage;
import PlanetCrash.ui.Objects.JRoundedButton;

public class EndGameScene extends Scene{



	private String[] goodNews = {"you make a nice stew.", "you go well with bread.", "being eaten alive kinda tickles.",
			"at least you're not a disgusting alien.", "you will get a hot bath in a cauldron."};


	public EndGameScene(GameGUI gameGUI, Game game) {
		super(gameGUI, game);
	}

	@Override
	public Component create() {
		boolean victory = game.getLives()>0 && game.getCurrentQuestion()>=game.getDifficulty()-1;

		if (victory) { //Update country victory in db
			new Thread(new Runnable() {

				@Override
				public void run() {
					DatabaseHandler dbh = new DatabaseHandler(gameGUI.mConnPool);
					try {
						dbh.singleInsert("user_country_completed",
								new String[]{"idUser","idCountry","isManual","Level"},
								new Object[]{game.getUser().getId(), game.getCountryId(), 0, game.getDifficulty()});
					} catch (SQLException e) {
						e.printStackTrace();
					}

					dbh.close();


				}
			}).start();
		}


		JLayeredPane panel = new JLayeredPane();
		panel.setPreferredSize(new Dimension(GameGUI.WINDOW_WIDTH,GameGUI.WINDOW_HEIGHT));

		JPanel s = emptyMainJPanel();
		s.setLayout(null);
		panel.add(s, new Integer(0),0);

		StyledDocument document = new DefaultStyledDocument();
		Style defaultStyle = document.getStyle(StyleContext.DEFAULT_STYLE);
		StyleConstants.setAlignment(defaultStyle, StyleConstants.ALIGN_CENTER);

		JImage bg;
		JTextField title;
		JTextPane message = new JTextPane(document);
		if(victory) {
			bg = new JImage(new File(GameGUI.ASSETS+"victory.png"));
			title = new JTextField("Victory!");
			title.setForeground(Color.green);
			message.setText("Congratulations!\nYou have successfully survived the aliens'\nchallenges and have reached the power core!");
			message.setForeground(Color.green);
		} else {
			bg = new JImage(new File(GameGUI.ASSETS+"loss.png"));
			title = new JTextField("Defeat!");
			title.setForeground(Color.red);
			message.setText("You have failed to rise up to the aliens' challenges.\nGood news: "+goodNews[(int)(Math.random()*goodNews.length)]);
			message.setForeground(Color.red);
		}
		title.setFont(new Font(Font.SANS_SERIF,Font.BOLD,70));
		title.setEditable(false);
		title.setBackground(new Color(0,0,0,120));
		//		title.setOpaque(false);
		title.setBorder(BorderFactory.createEmptyBorder());
		int tw=(int)title.getPreferredSize().getWidth(),th=(int)title.getPreferredSize().getHeight();
		title.setBounds((GameGUI.WINDOW_WIDTH-tw)/2,(GameGUI.WINDOW_HEIGHT-th)/5,tw,th);
		s.add(title);

		message.setFont(new Font(Font.SANS_SERIF,Font.PLAIN,30));
		message.setEditable(false);
		message.setBackground(new Color(0,0,0,120));
		message.setBorder(BorderFactory.createEmptyBorder());
		int mw=(int)message.getPreferredSize().getWidth(), mh=(int)message.getPreferredSize().getHeight();
		message.setBounds((GameGUI.WINDOW_WIDTH-mw)/2, (GameGUI.WINDOW_HEIGHT-mh)/2, mw, mh);
		s.add(message);

		s.add(bg);

		//Add main menu button
		JRoundedButton mainmenuBtn = new JRoundedButton("Main Menu", 220,60, 2);
		mainmenuBtn.setBounds((GameGUI.WINDOW_WIDTH-mainmenuBtn.getWidth())/2, 430, mainmenuBtn.getWidth(), mainmenuBtn.getHeight());
		panel.add(mainmenuBtn, new Integer(2), 0);

		//Add quit button
		JRoundedButton quitBtn = new JRoundedButton("Quit", 220,60, 2);
		quitBtn.setBounds((GameGUI.WINDOW_WIDTH-quitBtn.getWidth())/2, 500, quitBtn.getWidth(), quitBtn.getHeight());
		quitBtn.setBorderColor(Color.decode("#bf00af"));
		panel.add(quitBtn, new Integer(3), 0);

		//Register mouse listeners
		mainmenuBtn.addMouseListener(new EndSceneMouseListener(EndSceneMouseListener.MAIN_MENU));
		quitBtn.addMouseListener(new EndSceneMouseListener(EndSceneMouseListener.QUIT));

		return panel;
	}

	class EndSceneMouseListener implements MouseListener {
		public static final int MAIN_MENU=0,QUIT=1;
		int mode;

		public EndSceneMouseListener(int mode) {
			this.mode=mode;
		}

		@Override
		public void mouseClicked(MouseEvent arg0) {
			switch(mode) {
			case MAIN_MENU:
				gameGUI.fadeSwitchScene(new MainMenuScene(gameGUI, game));
				break;
			case QUIT:
				gameGUI.quit();
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

}
