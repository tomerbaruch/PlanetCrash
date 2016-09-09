package PlanetCrash.ui;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JFrame;
import javax.swing.JLayeredPane;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.Timer;

import PlanetCrash.core.Game.Game;
import PlanetCrash.db.ConnectionPool;
import PlanetCrash.db.DatabaseException;
import PlanetCrash.db.DatabaseHandler;
import PlanetCrash.db.Yago.Uploaders.Importer;
import PlanetCrash.ui.Objects.Effects.Fader;
import PlanetCrash.ui.Scenes.LoadFromYagoScene;
import PlanetCrash.ui.Scenes.LoginScene;
import PlanetCrash.ui.Scenes.Scene;
import PlanetCrash.ui.Scenes.WaitingScene;

public class GameGUI {

	public static final int WINDOW_WIDTH=800;
	public static final int WINDOW_HEIGHT=600;

	public static final String ASSETS = "assets/";
	
	
	public static ConnectionPool mConnPool;

	protected Game game;

	public JFrame mainFrame;
	
	private JLayeredPane mJLPane;

	public GameGUI(ConnectionPool cpool) {
		this.mConnPool=cpool;
		this.game=new Game();
	}

	public void start() {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				createAndShowGUI();
			}
		});
	}

	private void createAndShowGUI() {
		mainFrame = new JFrame("Planet Crash");
		mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		mainFrame.setResizable(false);
		
		switchScene(createConnectionScene());

		mainFrame.pack();

		mainFrame.setLocationRelativeTo(null); //middle of screen
		mainFrame.setVisible(true);
	}

	public void switchScene(Scene scene) {
		mJLPane = new JLayeredPane();
		mJLPane.setPreferredSize(new Dimension(WINDOW_WIDTH,WINDOW_HEIGHT));
		
		JPanel jp = Scene.emptyMainJPanel();
		jp.add(scene.create());
		
		mJLPane.add(jp, new Integer(0),0);
		
		mainFrame.setContentPane(Scene.emptyMainJPanel());
		mainFrame.add(mJLPane);
		
		mainFrame.pack();
	}

	//Switches scenes with a fade effect
	public void fadeSwitchScene(final Scene scene) {
		final long duration = 500;
		
		final Fader of = new Fader(duration, true);
		final Fader inf = new Fader(duration,false);
		
		final JPanel container = Scene.emptyMainJPanel();
		container.setOpaque(false);
		container.add(of);
		mJLPane.add(container,new Integer(1),0);
		
		new Thread(of).start();
		
		Timer t1 =new Timer((int)duration, new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				switchScene(scene);
				mJLPane.remove(container);

				final JPanel containerr = Scene.emptyMainJPanel();
				containerr.setOpaque(false);
				containerr.add(inf);
				mJLPane.add(containerr, new Integer(2),0);
				new Thread(inf).start();
				
				Timer t2 = new Timer((int)duration, new ActionListener() {

					@Override
					public void actionPerformed(ActionEvent e) {
						mJLPane.remove(containerr);
					}
					
				});
				
				t2.setRepeats(false);
				t2.start();
			}
		});
		
		t1.setRepeats(false);
		t1.start();
	}
	
	/**
	 * 
	 * @return A Scene that initialized the database and connection pool.
	 */
	public WaitingScene createConnectionScene() {
		final GameGUI gameGUI = this;
		
		Runnable waitConnection = new Runnable() {

			@Override
			public void run() {
				final Timer timer = new Timer(0, new ActionListener() {
					
					@Override
					public void actionPerformed(ActionEvent e) {
						if (Importer.dbReady == false){
							LoadFromYagoScene mms = new LoadFromYagoScene(gameGUI,game);//MainMenuScene();
							fadeSwitchScene(mms);	
						}
						else{
							//Show main screen
							LoginScene mms = new LoginScene(gameGUI,game);//MainMenuScene();
							fadeSwitchScene(mms);		
						}	
					}
				});
				
				//Initialize connection pool and db state
				try {
					mConnPool.init();
					DatabaseHandler dbh = new DatabaseHandler(mConnPool);
					dbh.set_db_state();
					dbh.close();
				} catch (DatabaseException e1) {
					JOptionPane.showMessageDialog(gameGUI.mainFrame, e1.getMessage());
					gameGUI.quit();
				}
				
				timer.setRepeats(false);
				timer.start();
			}
			
		};
		
		return new WaitingScene(gameGUI, game, waitConnection, "Connecting to database...");
	}
	
	public void quit() {
		mConnPool.destroyPool();
		System.exit(0);
	}

}
