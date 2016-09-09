package PlanetCrash.ui.Scenes;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.io.FileFilter;
import java.util.Collections;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JLayeredPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import PlanetCrash.core.Game.Answer;
import PlanetCrash.core.Game.Game;
import PlanetCrash.ui.GameGUI;
import PlanetCrash.ui.Objects.JImage;
import PlanetCrash.ui.Objects.JRounded;
import PlanetCrash.ui.Objects.JRoundedButton;

public class GameScene extends Scene{
	JRoundedButton[] answersButtons;
	List<Answer> answers;
	JLayeredPane panel;

	public GameScene(GameGUI gameGUI, Game game) {
		super(gameGUI, game);
		this.answersButtons = new JRoundedButton[4];
	}

	@Override
	public Component create() {
		while(game.getQuestions()==null)
			try {
				Thread.sleep(50);
			} catch (InterruptedException e) {
			}
		this.answers = game.getQuestion(game.getCurrentQuestion()).getPossibleAnswers();
		Collections.shuffle(this.answers);

		panel = new JLayeredPane();
		panel.setPreferredSize(new Dimension(GameGUI.WINDOW_WIDTH,GameGUI.WINDOW_HEIGHT));

		panel.setBackground(Color.GREEN);

		//Add background
		JPanel backdrop = emptyMainJPanel();
		backdrop.setBackground(game.getBackdrop());
		panel.add(backdrop, new Integer(0),0);
		//Background overlay
		JImage bdOverlay = new JImage(randomBackdropOverlay());
		bdOverlay.setOpaque(false);
		backdrop.add(bdOverlay);

		//Add land
		JPanel land = emptyMainJPanel();
		land.setBackground(game.getLand());
		land.setBounds(0, GameGUI.WINDOW_HEIGHT-200, GameGUI.WINDOW_WIDTH, GameGUI.WINDOW_HEIGHT);
		panel.add(land, new Integer(1),0);
		//Land overlay
		JImage landOverlay = new JImage(randomLandOverlay());
		landOverlay.setOpaque(false);
		land.add(landOverlay);

		//Add question label
		Font ansfont = new Font(null, Font.PLAIN, 18);
		JPanel questionP = new JPanel();
		questionP.setLayout(null);
		questionP.setPreferredSize(new Dimension(3*GameGUI.WINDOW_WIDTH/4,100));
		panel.add(questionP, new Integer(4),0);
		questionP.setOpaque(false);
		questionP.setBounds(GameGUI.WINDOW_WIDTH/8,150,3*GameGUI.WINDOW_WIDTH/4,100);

		JRoundedButton qlabel = new JRoundedButton(ansfont, "The aliens challenge you:", 3*GameGUI.WINDOW_WIDTH/8, 40, 1);
		qlabel.isButton(false);
		qlabel.setBounds(0,0,(int)qlabel.getPreferredSize().getWidth(),(int)qlabel.getPreferredSize().getHeight());
		questionP.add(qlabel);

		//Add question
		Font qfont = (game.getQuestion(game.getCurrentQuestion()).getQuestion().length()>65?new Font(null, Font.PLAIN,11):ansfont);
		JRoundedButton qst = new JRoundedButton(qfont, game.getQuestion(game.getCurrentQuestion()).getQuestion(),3*GameGUI.WINDOW_WIDTH/4, 40, 1);
		qst.setBounds(0,40,3*GameGUI.WINDOW_WIDTH/4,40);
		qst.isButton(false);
		questionP.add(qst);

		//Add answers
		int ah = 80;
		JPanel answersP = emptyMainJPanel();
		answersP.setBounds(0, GameGUI.WINDOW_HEIGHT-ah, GameGUI.WINDOW_WIDTH, GameGUI.WINDOW_HEIGHT);
		panel.add(answersP,new Integer(3),0);
		answersP.setOpaque(false);
		answersP.setLayout(null);

		int ax=0,ay=0;
		for(int i=0; i<answersButtons.length; i++) {
			qfont = (answers.get(i).getAnswer().length()>60?new Font(null, Font.PLAIN,11):ansfont);
			JRoundedButton ans = new JRoundedButton(qfont,answers.get(i).getAnswer(), GameGUI.WINDOW_WIDTH/2, ah/2, 1);
			ans.setOpaque(false);
			ans.setBounds(ax, ay, GameGUI.WINDOW_WIDTH/2, ah/2);

			//register listener
			ans.addMouseListener(new AnswerListener(answers.get(i)));

			answersButtons[i]=ans;
			answersP.add(ans);

			ax=((ax+GameGUI.WINDOW_WIDTH/2)%GameGUI.WINDOW_WIDTH)*((i+1)%2);
			if(i==1) ay=(ay+ah/2);
		}


		//Add soldiers
		JPanel soldiers = emptyMainJPanel();
		soldiers.setBounds(0, GameGUI.WINDOW_HEIGHT-200-20, GameGUI.WINDOW_WIDTH, GameGUI.WINDOW_HEIGHT);
		panel.add(soldiers,new Integer(2),0);
		soldiers.setOpaque(false);
		soldiers.setLayout(null);

		int sx=50,sy=20;
		for(int i=0; i<game.getLives(); i++) {
			JImage s = new JImage(game.getSoldier());
			s.setOpaque(false);
			s.setBounds(sx,sy,s.getWidth(),s.getHeight());
			soldiers.add(s);

			sx+=s.getWidth()/2;
			sy=(sy+s.getHeight()/2)%s.getHeight();
		}

		//Add aliens
		JPanel aliens = emptyMainJPanel();
		File alienFile = randomAlien(); //the alien type for this screen
		aliens.setBounds(0, GameGUI.WINDOW_HEIGHT-200-2*(new JImage(alienFile)).getHeight()/3, GameGUI.WINDOW_WIDTH, GameGUI.WINDOW_HEIGHT);
		panel.add(aliens,new Integer(2),0);
		aliens.setOpaque(false);
		aliens.setLayout(null);

		int alx=GameGUI.WINDOW_WIDTH-(int)((new JImage(alienFile)).getWidth()*1.4),aly=20,inc=0;
		for(int i=0; i<game.getDifficulty()-game.getCurrentQuestion(); i++) {
			JImage s = new JImage(alienFile);
			s.setOpaque(false);
			s.setBounds(alx,aly,s.getWidth(),s.getHeight());
			aliens.add(s);

			
			if(alx<GameGUI.WINDOW_WIDTH/2) {
				alx = GameGUI.WINDOW_WIDTH-(int)((new JImage(alienFile)).getWidth()*1.4);
				aly+=s.getHeight()/2;
				inc+=s.getHeight()/2;
			} else {
				alx-=s.getWidth()/3;
			}
			aly=(aly+s.getHeight()/2)%/*(GameGUI.WINDOW_HEIGHT-160);*/(s.getHeight()+inc);
		}

		//Add status bar
		JPanel statusbar = new JPanel();
		statusbar.setPreferredSize(new Dimension(GameGUI.WINDOW_WIDTH,30));
		statusbar.setBounds(0,-1,GameGUI.WINDOW_WIDTH,30);
		statusbar.setBorder(BorderFactory.createEmptyBorder(-5, -5, -5, -5));
		panel.add(statusbar, new Integer(4),2);
		JRoundedButton sbbg = new JRoundedButton("", (int)(GameGUI.WINDOW_WIDTH*1.3), 30, 1);
		sbbg.setPreferredSize(new Dimension((int)(GameGUI.WINDOW_WIDTH*1.3),30));
		sbbg.setBackground(Color.decode("#3B5998"));
		sbbg.setBounds((int)(-GameGUI.WINDOW_WIDTH*0.15), 0, (int)(GameGUI.WINDOW_WIDTH*1.3), 30);
		sbbg.isButton(false);
		statusbar.add(sbbg);
		JTextField stext = new JTextField();
		stext.setText("Lives: "+game.getLives() +"\t\t  Questions: "+(game.getCurrentQuestion()+1)+"\\"+game.getDifficulty()
				+"\t\t  Planet: "+game.getPlanetName());
		stext.setEditable(false);
		stext.setBackground(sbbg.getBackgroundColor());
		stext.setBorder(BorderFactory.createEmptyBorder());
		stext.setForeground(Color.cyan);
		sbbg.add(stext);


		return panel;
	}

	/**
	 * @param dirp
	 * @return a random .png file from the directory given
	 */
	public static File randomFile(String dirp) {
		File dir = new File(dirp);
		if(!dir.exists() || !dir.isDirectory())
			return null;
		File[] flist = dir.listFiles(new FileFilter() {

			@Override
			public boolean accept(File pathname) {
				if(!pathname.getName().endsWith(".png"))
					return false;
				return true;
			}
		});
		return flist[(int)(Math.random()*flist.length)];

	}

	private File randomBackdropOverlay() {
		return randomFile(GameGUI.ASSETS+"Overlays/Backdrop");
	}

	private File randomLandOverlay() {
		return randomFile(GameGUI.ASSETS+"Overlays/Land");
	}

	private File randomAlien() {
		return randomFile(GameGUI.ASSETS+"Aliens");
	}

	class AnswerListener implements MouseListener {
		Answer answer;

		public AnswerListener(Answer answer) {
			this.answer=answer;
		}

		@Override
		public void mouseClicked(MouseEvent e) {
			//disable answer button listers
			for(int i=0;i<answersButtons.length;i++) {
//				for(int j=0; j<answersButtons[i].getMouseListeners().length;j++)
//					answersButtons[i].removeMouseListener(answersButtons[i].getMouseListeners()[j]);
				answersButtons[i].removeAllMouseListeners();
			}

			JLayeredPane dlg = new JLayeredPane();
			JLayeredPane mask = emptyMainJLayeredPane();

			//mask.setLayout(null);
			mask.setOpaque(false);
			JPanel bg = emptyMainJPanel();

			if (game.getQuestion(game.getCurrentQuestion()).isCorrectAnswer(answer)) {
				//Correct answer
				bg.setBackground(new Color(6,186,17,100));
				dlg = createCorrectDialog(true);

			} else {
				//Wrong answer
				bg.setBackground(new Color(255,0,0,50));
				dlg = createCorrectDialog(false);
				game.decLives();
			}
			mask.add(bg, new Integer(0),0);
			dlg.setBounds((int)(GameGUI.WINDOW_WIDTH-dlg.getPreferredSize().getWidth())/2,
					(int)(GameGUI.WINDOW_HEIGHT-dlg.getPreferredSize().getHeight())/2,
					(int)dlg.getPreferredSize().getWidth(),
					(int)dlg.getPreferredSize().getHeight());
			mask.add(dlg,new Integer(1),0);

			mask.setOpaque(false);

			//Add next/quit buttons
			JPanel buttons = new JPanel();
			buttons.setLayout(null);
			buttons.setOpaque(false);

			int bw=90,bh=60;
			Rectangle dlgb= dlg.getComponent(1).getBounds();
			buttons.setPreferredSize(new Dimension(bw,bh));
			buttons.setBounds(0,Math.min((dlgb.y+dlgb.height-bh/2)+20,GameGUI.WINDOW_HEIGHT-bh),GameGUI.WINDOW_WIDTH,bh);

			JRoundedButton next = new JRoundedButton("Next", bw, bh, 2);
			next.setBounds(Math.min((dlgb.x+dlgb.width-bw/2),GameGUI.WINDOW_WIDTH-bw),0,bw,bh);
			next.setBorderColor(Color.GREEN);
			buttons.add(next);

			JRoundedButton quit = new JRoundedButton("Quit", bw, bh, 2);
			quit.setBounds(Math.max(0,(dlgb.x-bw/2)),0,bw,bh);
			quit.setBorderColor(Color.red);
			buttons.add(quit);

			mask.add(buttons, new Integer(2),0);

			panel.add(mask, new Integer(5),0);

			//Register listeners
			next.addMouseListener(new NextListener(NextListener.NEXT));
			quit.addMouseListener(new NextListener(NextListener.QUIT));
		}

		private JLayeredPane createCorrectDialog(boolean isCorrect) {
			JLayeredPane master = emptyMainJLayeredPane();
			JPanel containerT = emptyMainJPanel();
			containerT.setLayout(null);
			containerT.setOpaque(false);

			//JTextField title = new JTextField("Correct!");
			JRoundedButton title = new JRoundedButton(new Font(null, Font.BOLD, 30), (isCorrect?"Correct!":"Wrong!"), 150, 50, 2);
			JTextField msg = new JTextField("The answer is:\n"+game.getQuestion(game.getCurrentQuestion()).getCorrectAnswer().getAnswer());
			msg.setBackground(Color.decode("#3B5998"));
			msg.setEditable(false);
			msg.setBorder(BorderFactory.createEmptyBorder());
			title.setBorderColor(isCorrect?Color.green:Color.red);
			title.isButton(false);
			msg.setFont(new Font(Font.SANS_SERIF, Font.BOLD,18));
			msg.setForeground(isCorrect?Color.green:Color.red);

			title.setBounds((int)(GameGUI.WINDOW_WIDTH-title.getPreferredSize().getWidth())/2,
					220, (int)title.getPreferredSize().getWidth(), (int)title.getPreferredSize().getHeight());
			containerT.add(title);
			containerT.setBounds(0,0,GameGUI.WINDOW_WIDTH,GameGUI.WINDOW_HEIGHT);

			JRounded containerM = new JRounded((int)msg.getPreferredSize().getWidth()+50,
					(int)msg.getPreferredSize().getHeight()+50,2) {

				/**
				 * 
				 */
				private static final long serialVersionUID = 1L;};

				containerM.add(msg);
				containerM.setLayout(null);
				containerM.setBounds((int)(GameGUI.WINDOW_WIDTH-containerM.getPreferredSize().getWidth())/2,
						250, (int)containerM.getPreferredSize().getWidth(), (int)containerM.getPreferredSize().getHeight());
				containerM.setBorderColor(isCorrect?Color.green:Color.red);

				msg.setBounds((int)(containerM.getPreferredSize().getWidth()-msg.getPreferredSize().getWidth())/2,
						(int)(containerM.getPreferredSize().getHeight()-msg.getPreferredSize().getHeight())/2, 
						(int)msg.getPreferredSize().getWidth(), (int)msg.getPreferredSize().getHeight());

				master.add(containerM,new Integer(0),0);
				master.add(containerT,new Integer(1),0);

				return master;
		}


		@Override
		public void mouseEntered(MouseEvent e) {
			// TODO Auto-generated method stub

		}

		@Override
		public void mouseExited(MouseEvent e) {
			// TODO Auto-generated method stub

		}

		@Override
		public void mousePressed(MouseEvent e) {
			// TODO Auto-generated method stub

		}

		@Override
		public void mouseReleased(MouseEvent e) {
			// TODO Auto-generated method stub

		}

	}

	class NextListener implements MouseListener {
		static final int NEXT=0,QUIT=1;
		int mode;

		public NextListener(int MODE) {
			this.mode=MODE;
		}

		@Override
		public void mouseClicked(MouseEvent arg0) {
			switch(mode) {
			case NEXT:
				if(game.getLives()<=0 ||game.getCurrentQuestion()>=game.getDifficulty()-1) { //loss or win
					EndGameScene es = new EndGameScene(gameGUI, game);
					gameGUI.fadeSwitchScene(es);
				} else { //next question
					game.advanceQuestion();
					GameScene ns = new GameScene(gameGUI,game);
					gameGUI.fadeSwitchScene(ns);
				}
				break;
			case QUIT:
				MainMenuScene mms = new MainMenuScene(gameGUI, game);
				gameGUI.fadeSwitchScene(mms);
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
