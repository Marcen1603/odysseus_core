package de.uniol.inf.is.odysseus.codegenerator.rcp.composite;

import java.util.Set;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

/**
 * abstract composite for codegeneration gui 
 * @author Marc
 *
 */
public abstract class AbstractParameterComposite extends Composite{
	
	AbstractParameterComposite(Composite parent, int style) {
		super(parent, style);
	}

	/**
	 * create a label with a given text
	 * @param composite
	 * @param text
	 * @return
	 */
	protected static Label createLabel(Composite composite, String text) {
		Label label = new Label(composite,  SWT.FILL);
		label.setText(text);
		return label;
	}
	
	/**
	 * create text field with a given text
	 * 
	 * @param composite
	 * @param defaultText
	 * @return
	 */
	protected static Text createTextField(Composite composite,String defaultText){
		Text textField = new Text(composite, SWT.BORDER);
		textField.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		textField.setText(defaultText);
		
		return textField;
	}
	
	
	/**
	 * create a label with default bounds for a given text 
	 * @param composite
	 * @param text
	 * @return
	 */
	protected static Label createLabelWithBounds(Composite composite, String text){
		Label label  = createLabel(composite, text);
		label.setBounds(5, 130, 45, 15);
		return label;
		
	}
	
	/**
	 * create a comboBox and a label
	 * 
	 * @param composite
	 * @param labelText
	 * @param comboValues
	 * @param selectItem
	 * @return
	 */
	protected static Combo createComboWithLabel(Composite composite, String labelText, Set<String> comboValues, int selectItem){
		createLabelWithBounds(composite,labelText);
		
		Combo combo = new Combo(composite, SWT.DROP_DOWN | SWT.READ_ONLY);
		combo.setBounds(99, 127, 68, 23);
		
		for(String value : comboValues){
		    	 combo.add(value.toUpperCase());
		  }
		
		if(combo.getItemCount()>= selectItem){
			combo.select(selectItem);
		}else{
			combo.select(0);
		}
	
		
		return combo;
	}
	
	/**
	 * create a textField and a label
	 * 
	 * @param composite
	 * @param defaultText
	 * @param labelText
	 * @return
	 */
	protected static Text createTextFieldWithLabel(Composite composite,String defaultText, String labelText){
		createLabel(composite, labelText);
		Text text = createTextField(composite,defaultText);
		return text;
	}
	

}
