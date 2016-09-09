package PlanetCrash.ui.Scenes;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.lang.reflect.InvocationTargetException;
import java.sql.SQLException;











import javax.swing.JLayeredPane;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.Timer;

import PlanetCrash.core.Game.Game;
import PlanetCrash.core.Game.GameUtils;
import PlanetCrash.core.config.Config;
import PlanetCrash.db.DatabaseHandler;
import PlanetCrash.db.Updates.ReloadYago;
import PlanetCrash.db.Yago.Uploaders.Importer;
import PlanetCrash.ui.GameGUI;
import PlanetCrash.ui.Objects.JImage;
import PlanetCrash.ui.Objects.JRoundedButton;
import PlanetCrash.ui.Objects.JRoundedEditText;
import PlanetCrash.ui.Objects.StarryBackground;

public class LoadFromYagoScene extends Scene{
	
	public static boolean backDisabled=false;
	JRoundedButton backBtn = new JRoundedButton("Back", 100, 60, 2);
	Config config = new Config();
	
	public LoadFromYagoScene(GameGUI gameGUI, Game game) {
		super(gameGUI, game);
	}
	
	Font userpass = new Font(null, Font.BOLD, 14);
	JRoundedEditText passwordField;
	
	int per = 0;
	
	boolean isFirstLoad = true;

	
	JRoundedButton importBtn = new JRoundedButton("Import from Yago", 500, 60, 2);
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
		importBtn.setBorderColor(Color.green);
		importBtn.setBounds((GameGUI.WINDOW_WIDTH-importBtn.getWidth())/2, 375, importBtn.getWidth(), importBtn.getHeight());
		panel.add(importBtn, new Integer(2), 2);
		
		if (Importer.dbReady == true){
			backBtn.setBorderColor(Color.green);
			backBtn.setBounds(30, 500, backBtn.getWidth(), backBtn.getHeight());
			panel.add(backBtn, new Integer(2), 2);
			isFirstLoad = false;
		}

								
		//Register action listeners		
		importBtn.addMouseListener(new MainListener(MainListener.IMPORT));
		backBtn.addMouseListener(new MainListener(MainListener.BACK));		
		return panel;
	}
	
	class MainListener implements MouseListener {
		
		public static final int BACK=0,IMPORT=1;

		int mode;
		public MainListener(int mode) {
			this.mode=mode;
		}	

		@Override
		public void mouseClicked(MouseEvent arg0) {
			switch(mode) {
			case BACK:
				if (backDisabled){
					break;
				}
				SettingsScene mms = new SettingsScene(gameGUI,game);	
				gameGUI.fadeSwitchScene(mms);		
				break;
			case IMPORT:
				backDisabled=true;
				final DatabaseHandler dbh = new DatabaseHandler(gameGUI.mConnPool);
				final Config config = new Config();
				
				importBtn.removeMouseListener(this);
				importBtn.isButton(false);
				backBtn.removeMouseListener(this);
				backBtn.isButton(false);
				
		
				
				new Thread(new Runnable(){
					public void run(){
						ReloadYago.deleteLiteFiles(config);
						if (isFirstLoad){
							try{
								ReloadYago.deleteAllData(config.get_db_name(), dbh);
								Importer i = new Importer(dbh, config);
							} catch (FileNotFoundException | SQLException e) {
								try {
									EventQueue.invokeAndWait(new Runnable() {

										@Override
										public void run() {
											JOptionPane.showMessageDialog(gameGUI.mainFrame, "Can't find Yago Files!");
											
										}
										
									});
								} catch (InvocationTargetException
										| InterruptedException e1) {
									e1.printStackTrace();
								}
								gameGUI.quit();
								
							}
						}
						else{
							try {
								ReloadYago.updateFromYago(gameGUI.mConnPool, config);							
							} catch (FileNotFoundException e) {
								// TODO Auto-generated catch block
								JOptionPane.showMessageDialog(gameGUI.mainFrame, "Can't find Yago Files!");
								MainMenuScene mms = new MainMenuScene(gameGUI,game);		
								gameGUI.fadeSwitchScene(mms);
								return;
							}
						}
	
						JOptionPane.showMessageDialog(gameGUI.mainFrame, "Database loaded successfully!");
						Importer.parsing_finished = 0;
						Importer.uploading_finished = 0;
						if (isFirstLoad){
							LoginScene mms = new LoginScene(gameGUI,game);		
							gameGUI.fadeSwitchScene(mms);
						}
						else{
							MainMenuScene mms = new MainMenuScene(gameGUI,game);		
							gameGUI.fadeSwitchScene(mms);						
						}
					}
					
					}).start();
				
				per = 0;			
				final Timer t2 = new Timer(1000, new ActionListener() {

					@Override
					public void actionPerformed(ActionEvent e) {
										
						if (Importer.uploading_finished != -1){
						   
						    int temp = Math.round((Importer.uploading_finished * 100.0f) / 169000);		
						    if (temp!= per){
						    	per = temp;
						    	System.out.println(String.format("finished %d percenteges", (per)));
						    	importBtn.setText(String.format("Uploading... %d%%", (per)));
						    }												    												   
						}
						else if(Importer.uploading_finished==-1){
						    	((Timer)e.getSource()).stop();
						    	importBtn.setText("Uploading... 100%");
						}
					}
				});
				
							
				Timer t = new Timer(1000, new ActionListener() {

					@Override
					public void actionPerformed(ActionEvent e) {
										
						if (Importer.parsing_finished != -1){
						   importBtn.isButton(false);
						    int temp = Math.round((Importer.parsing_finished * 100.0f) / config.get_files_size());		
						    if (temp!= per){
						    	per = temp;
						    	System.out.println(String.format("finished %d percenteges", (per)));
						    	importBtn.setText(String.format("Parsing... %d%%", (per)));
						    	//perBtn.setText(String.format("%d%%", (per)));
						    }												    												   
						}
						else if(Importer.parsing_finished==-1){
						    	((Timer)e.getSource()).stop();
						    	per = 0;
						    	importBtn.setText("Uploading...");
						    	t2.start();
						}
					}
				});
				
				importBtn.setText("Parsing...");
				t.start();
				
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
