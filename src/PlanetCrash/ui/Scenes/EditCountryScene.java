package PlanetCrash.ui.Scenes;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.sql.SQLException;

import javax.swing.JLayeredPane;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import PlanetCrash.core.Exceptions.NotFoundException;
import PlanetCrash.core.Game.Game;
import PlanetCrash.db.DatabaseHandler;
import PlanetCrash.db.Updates.ManualUpdater;
import PlanetCrash.parsing.entities.entity_country;
import PlanetCrash.ui.GameGUI;
import PlanetCrash.ui.Objects.JImage;
import PlanetCrash.ui.Objects.JRoundedButton;
import PlanetCrash.ui.Objects.JRoundedEditText;
import PlanetCrash.ui.Objects.StarryBackground;

public class EditCountryScene extends Scene{

	public EditCountryScene(GameGUI gameGUI, Game game) {
		super(gameGUI, game);
	}

	JRoundedEditText nameField;
	JRoundedEditText continetField;
	JRoundedEditText currencyField;
	JRoundedEditText languageField;
	JRoundedEditText capitalField;
	JRoundedEditText populationField;

	JRoundedButton backBtn = new JRoundedButton("Back", 100, 60, 2);

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

		//Add country name field
		JPanel namePanel = new JPanel();
		JRoundedButton usernameLabel = new JRoundedButton(userpass,"Enter Country:", 180, userpass.getSize()+20, 2);
		nameField = new JRoundedEditText(userpass,"", 220, userpass.getSize()+20, 2, false);

		usernameLabel.isButton(false);
		usernameLabel.setBorderColor(Color.pink);
		nameField.setBorderColor(Color.pink);

		namePanel.setPreferredSize(new Dimension(usernameLabel.getWidth()+nameField.getWidth()+50, nameField.getHeight()));
		namePanel.setBounds((GameGUI.WINDOW_WIDTH-(int)namePanel.getPreferredSize().getWidth())/2 -38,
				loginy, usernameLabel.getWidth()+nameField.getWidth()+50, nameField.getHeight());
		namePanel.setOpaque(false);
		namePanel.setBorder(new EmptyBorder(-5, -25, -5, -5));
		namePanel.add(usernameLabel);
		namePanel.add(nameField);
		panel.add(namePanel, new Integer(2),0);



		//Add load button
		JRoundedButton loadBtn = new JRoundedButton(new Font(null, Font.BOLD, 20),"Load", 80, 40, 2);
		loadBtn.setBorderColor(Color.green);
		loadBtn.setBounds((GameGUI.WINDOW_WIDTH-loadBtn.getWidth())/2+200, loginy, loadBtn.getWidth(), loadBtn.getHeight());
		panel.add(loadBtn, new Integer(2), 2);
		//Register action listeners
		LoadMouseListener lml = new LoadMouseListener();
		loadBtn.addMouseListener(lml);
		loadBtn.setBorderColor(Color.pink);


		//Add Continent field
		JPanel continentPanel = new JPanel();
		JRoundedButton passwordLabel = new JRoundedButton(userpass,"Continent:", 100, userpass.getSize()+20, 2);
		continetField = new JRoundedEditText(userpass,"", 220, userpass.getSize()+20, 2, false);

		passwordLabel.isButton(false);
		passwordLabel.setBorderColor(Color.cyan);
		continetField.setBorderColor(Color.cyan);

		continentPanel.setPreferredSize(new Dimension(usernameLabel.getWidth()+continetField.getWidth()+50, continetField.getHeight()));
		continentPanel.setBounds((GameGUI.WINDOW_WIDTH-(int)continentPanel.getPreferredSize().getWidth())/2,
				loginy+50, usernameLabel.getWidth()+continetField.getWidth()+50, continetField.getHeight());
		continentPanel.setOpaque(false);
		continentPanel.setBorder(new EmptyBorder(-5, -25, -5, -5));
		continentPanel.add(passwordLabel);
		continentPanel.add(continetField);
		panel.add(continentPanel, new Integer(2),1);

		//Add currency field
		JPanel currencyPanel = new JPanel();
		JRoundedButton currencyLabel = new JRoundedButton(userpass,"Currency:", 100, userpass.getSize()+20, 2);
		currencyField = new JRoundedEditText(userpass,"", 220, userpass.getSize()+20, 2, false);

		currencyLabel.isButton(false);
		currencyLabel.setBorderColor(Color.cyan);
		currencyField.setBorderColor(Color.cyan);

		currencyPanel.setPreferredSize(new Dimension(usernameLabel.getWidth()+currencyField.getWidth()+50, currencyField.getHeight()));
		currencyPanel.setBounds((GameGUI.WINDOW_WIDTH-(int)currencyPanel.getPreferredSize().getWidth())/2,
				loginy+100, usernameLabel.getWidth()+currencyField.getWidth()+50, currencyField.getHeight());
		currencyPanel.setOpaque(false);
		currencyPanel.setBorder(new EmptyBorder(-5, -25, -5, -5));
		currencyPanel.add(currencyLabel);
		currencyPanel.add(currencyField);
		panel.add(currencyPanel, new Integer(2),1);

		//Add language field
		JPanel languagePanel = new JPanel();
		JRoundedButton languageLabel = new JRoundedButton(userpass,"Language:", 100, userpass.getSize()+20, 2);
		languageField = new JRoundedEditText(userpass,"", 220, userpass.getSize()+20, 2, false);

		languageLabel.isButton(false);
		languageLabel.setBorderColor(Color.cyan);
		languageField.setBorderColor(Color.cyan);

		languagePanel.setPreferredSize(new Dimension(usernameLabel.getWidth()+languageField.getWidth()+50, languageField.getHeight()));
		languagePanel.setBounds((GameGUI.WINDOW_WIDTH-(int)languagePanel.getPreferredSize().getWidth())/2,
				loginy+150, usernameLabel.getWidth()+languageField.getWidth()+50, languageField.getHeight());
		languagePanel.setOpaque(false);
		languagePanel.setBorder(new EmptyBorder(-5, -25, -5, -5));
		languagePanel.add(languageLabel);
		languagePanel.add(languageField);
		panel.add(languagePanel, new Integer(2),1);

		//Add capital field
		JPanel capitalPanel = new JPanel();
		JRoundedButton capitalLabel = new JRoundedButton(userpass,"Capital:", 100, userpass.getSize()+20, 2);
		capitalField = new JRoundedEditText(userpass,"", 220, userpass.getSize()+20, 2, false);

		capitalLabel.isButton(false);
		capitalLabel.setBorderColor(Color.cyan);
		capitalField.setBorderColor(Color.cyan);

		capitalPanel.setPreferredSize(new Dimension(capitalLabel.getWidth()+capitalField.getWidth()+50, capitalField.getHeight()));
		capitalPanel.setBounds((GameGUI.WINDOW_WIDTH-(int)capitalPanel.getPreferredSize().getWidth())/2,
				loginy+200, capitalLabel.getWidth()+capitalField.getWidth()+50, capitalField.getHeight());
		capitalPanel.setOpaque(false);
		capitalPanel.setBorder(new EmptyBorder(-5, -25, -5, -5));
		capitalPanel.add(capitalLabel);
		capitalPanel.add(capitalField);
		panel.add(capitalPanel, new Integer(2),1);

		//Add population field
		JPanel populationPanel = new JPanel();
		JRoundedButton populationLabel = new JRoundedButton(userpass,"Population:", 100, userpass.getSize()+20, 2);
		populationField = new JRoundedEditText(userpass,"", 220, userpass.getSize()+20, 2, false);

		populationLabel.isButton(false);
		populationLabel.setBorderColor(Color.cyan);
		populationField.setBorderColor(Color.cyan);

		populationPanel.setPreferredSize(new Dimension(populationLabel.getWidth()+populationField.getWidth()+50, populationField.getHeight()));
		populationPanel.setBounds((GameGUI.WINDOW_WIDTH-(int)populationPanel.getPreferredSize().getWidth())/2,
				loginy+250, populationLabel.getWidth()+populationField.getWidth()+50, populationField.getHeight());
		populationPanel.setOpaque(false);
		populationPanel.setBorder(new EmptyBorder(-5, -25, -5, -5));
		populationPanel.add(populationLabel);
		populationPanel.add(populationField);
		panel.add(populationPanel, new Integer(2),1);



		//Add update button
		JRoundedButton addBtn = new JRoundedButton("Update", 220, 60, 2);
		addBtn.setBorderColor(Color.green);
		addBtn.setBounds((GameGUI.WINDOW_WIDTH-addBtn.getWidth())/2, 500, addBtn.getWidth(), addBtn.getHeight());
		panel.add(addBtn, new Integer(2), 2);

		backBtn.setBorderColor(Color.green);
		backBtn.setBounds(30, 500, backBtn.getWidth(), backBtn.getHeight());
		panel.add(backBtn, new Integer(2), 2);
		//		
		//		//Add create user button
		//		JRoundedButton createBtn = new JRoundedButton("Register", 220, 60, 2);
		//		createBtn.setBounds((GameGUI.WINDOW_WIDTH-loginBtn.getWidth())/2, 375+60+20, createBtn.getWidth(), createBtn.getHeight());
		//		panel.add(createBtn, new Integer(2), 3);


		//Register action listeners		
		backBtn.addMouseListener(new BackListener());

		//Register action listeners
		UpdateMouseListener eml = new UpdateMouseListener();
		addBtn.addMouseListener(eml);


		return panel;
	}


	class UpdateMouseListener implements MouseListener {

		@Override
		public void mouseClicked(MouseEvent arg0) {
			DatabaseHandler dbh = new DatabaseHandler(gameGUI.mConnPool);
			try{
				if (check_not_empty_fields()){
					ManualUpdater.update_country(nameField.getText(), continetField.getText(), currencyField.getText(), languageField.getText(), capitalField.getText(), Integer.parseInt(populationField.getText()), dbh);
					JOptionPane.showMessageDialog(gameGUI.mainFrame, "Update Succeeded :)");
					nameField.setText("");
					continetField.setText("");
					currencyField.setText("");
					languageField.setText("");
					capitalField.setText("");
					populationField.setText("");
				}

			}catch(NotFoundException | SQLException ex){
				JOptionPane.showMessageDialog(gameGUI.mainFrame, ex.getMessage());
			}catch(NumberFormatException e){
				JOptionPane.showMessageDialog(gameGUI.mainFrame, "Population can be number only");
			}

			dbh.close();

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

	}
	class LoadMouseListener implements MouseListener {
		@Override
		public void mouseClicked(MouseEvent arg0) {
			DatabaseHandler dbh = new DatabaseHandler(gameGUI.mConnPool);
			try{
				entity_country country_res = ManualUpdater.get_country_details(nameField.getText(), dbh);

				continetField.setText(country_res.getContinent());
				currencyField.setText(country_res.getCurrency());
				languageField.setText(country_res.getLanguage());
				capitalField.setText(country_res.getCapital());
				populationField.setText(Integer.toString(country_res.getPopulation_size()));

			}catch(NotFoundException | SQLException ex){
				JOptionPane.showMessageDialog(gameGUI.mainFrame, ex.getMessage());
			}catch(NumberFormatException e){
				JOptionPane.showMessageDialog(gameGUI.mainFrame, "Population can be number only");
			}

			//			try {
			dbh.close();
			//			} catch (SQLException e) {
			//				// TODO Auto-generated catch block
			//				e.printStackTrace();
			//			}


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


	class BackListener implements MouseListener {

		public BackListener(){

		}
		@Override
		public void mouseClicked(MouseEvent arg0) {
			SettingsScene mms = new SettingsScene(gameGUI,game);	
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
