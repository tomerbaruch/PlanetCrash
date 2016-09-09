package PlanetCrash.ui.Objects;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;

import javax.swing.BorderFactory;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.text.JTextComponent;

public class JRoundedEditText extends JRounded{
	JTextComponent textField;
	Font font; 

	public JRoundedEditText(Font font, String text, int width, int height, int borderThickness,boolean isPassword) {
		super(width,height,borderThickness);

		this.font=font;

		initTextField(text,isPassword);
	}

	public JRoundedEditText(String text, int width, int height, int borderThickness,boolean isPassword) {
		super(width,height,borderThickness);

		//init font
		font = new Font(null, Font.BOLD, 36);

		initTextField(text,isPassword);
	}

	public String getText() {
		return textField.getText();
	}

	public void setText(String text) {
		textField.setText(text);
	}

	private void initTextField(String text, boolean isPassword) {
		//Init textField
		textField = (isPassword?new JPasswordField(text):new JTextField(text));
		textField.setFont(font);
		textField.setBackground(backgroundColor);
		textField.setForeground(borderColor);
		textField.setSize(width-20, height-10);
		textField.setPreferredSize(new Dimension(width-20,height-10));
		if(textField instanceof JTextField)
			((JTextField)textField).setHorizontalAlignment(JTextField.CENTER);
		else if(textField instanceof JPasswordField)
			((JPasswordField)textField).setHorizontalAlignment(JTextField.CENTER);
		if(textField instanceof JPasswordField) 
			((JPasswordField) textField).setEchoChar('*');
		textField.setBounds(0, 0, width, height);
		textField.setBorder(BorderFactory.createEmptyBorder());


		add(textField);
	}
	
	public void setBorderColor(Color c) {
		super.setBorderColor(c);
		
		textField.setForeground(c);
	}
}
