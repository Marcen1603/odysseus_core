package de.uniol.inf.is.odysseus.sensormanagement.application.view.utilities;

import javax.swing.JTextField;

public class TextFieldToString 
{
	private JTextField textField;

	public TextFieldToString(JTextField textField) 
	{
		this.textField = textField;
	}

	@Override
	public String toString()
	{
		String s = textField.getText(); 
		return s;
	}
}
