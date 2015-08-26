package de.uniol.inf.is.odysseus.query.codegenerator.rcp.composite;

import java.util.Set;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

public abstract class AbstractParamterComposite extends Composite{
	
	AbstractParamterComposite(Composite parent, int style) {
		super(parent, style);
	}

	protected static Label createLabel(Composite composite, String text) {
		Label label = new Label(composite,  SWT.FILL);
		label.setText(text);
		return label;
	}
	
	
	protected static Text createTextField(Composite composite,String defaultText){
		Text textField = new Text(composite, SWT.BORDER);
		textField.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		textField.setText(defaultText);
		
		return textField;
	}
	
	
	protected static Label createLabelWithBounds(Composite composite, String text){
		Label label  = createLabel(composite, text);
		label.setBounds(5, 130, 45, 15);
		return label;
		
	}
	
	
	protected static Combo createComboWithLabel(Composite composite, String labelText, Set<String> comboValues){
		createLabelWithBounds(composite,labelText);
		
		Combo combo =new Combo(composite, SWT.DROP_DOWN | SWT.READ_ONLY);
		combo.setBounds(99, 127, 68, 23);
		
		for(String value : comboValues){
		    	 combo.add(value.toUpperCase());
		  }
		combo.select(0);
		
		return combo;
	}
	
	protected static Text createTextFieldWithLabel(Composite composite,String defaultText, String labelText){
		createLabel(composite, labelText);
		Text text = createTextField(composite,defaultText);
		return text;
	}
	

	
	
	


}
