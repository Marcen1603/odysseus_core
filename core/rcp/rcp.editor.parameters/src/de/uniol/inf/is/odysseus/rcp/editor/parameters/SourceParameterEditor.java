package de.uniol.inf.is.odysseus.rcp.editor.parameters;

import java.util.Map.Entry;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;

import de.uniol.inf.is.odysseus.datadictionary.DataDictionary;
import de.uniol.inf.is.odysseus.logicaloperator.ILogicalOperator;
import de.uniol.inf.is.odysseus.rcp.editor.parameter.IParameterEditor;
import de.uniol.inf.is.odysseus.rcp.editor.parameter.SimpleParameterEditor;
import de.uniol.inf.is.odysseus.rcp.user.ActiveUser;

public class SourceParameterEditor extends SimpleParameterEditor<String> implements IParameterEditor {

	@Override
	protected Control createInput(Composite parent) {
		// Combobox anstatt Texteingabe
		final Combo combo = new Combo(parent, SWT.BORDER);

		// Liste der Quellen
		for( Entry<String, ILogicalOperator> e : DataDictionary.getInstance().getStreamsAndViews(ActiveUser.getActiveUser())) {
			combo.add(e.getKey());
		}
		
		combo.addModifyListener(new ModifyListener() {

			@Override
			public void modifyText(ModifyEvent e) {
				try {
					if( combo.getText().length() == 0 )
						setValue(null);
					else 
						setValue(convertFromString(combo.getText()));
										
				} catch( Exception ex ) {
					ex.printStackTrace();
				}
			}
			
		});
		
		// Anfangstext setzen
		try {
			String txt = convertToString((String)getValue());
			combo.setText(txt == null ? "" : txt );
		} catch( Exception ex ) {
			combo.setText("");
		}

		return combo;
	}
	
	@Override
	public String convertFromString(String txt) {
		return txt;
	}

	@Override
	public String convertToString(String object) {
		return object;
	}

}
