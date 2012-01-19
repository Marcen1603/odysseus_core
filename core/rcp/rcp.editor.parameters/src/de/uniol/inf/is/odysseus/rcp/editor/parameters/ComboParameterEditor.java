/** Copyright [2011] [The Odysseus Team]
  *
  * Licensed under the Apache License, Version 2.0 (the "License");
  * you may not use this file except in compliance with the License.
  * You may obtain a copy of the License at
  *
  *     http://www.apache.org/licenses/LICENSE-2.0
  *
  * Unless required by applicable law or agreed to in writing, software
  * distributed under the License is distributed on an "AS IS" BASIS,
  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
  * See the License for the specific language governing permissions and
  * limitations under the License.
  */
package de.uniol.inf.is.odysseus.rcp.editor.parameters;

import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;

import de.uniol.inf.is.odysseus.logicaloperator.ILogicalOperator;
import de.uniol.inf.is.odysseus.rcp.OdysseusRCPPlugIn;
import de.uniol.inf.is.odysseus.rcp.editor.parameter.IParameterEditor;
import de.uniol.inf.is.odysseus.rcp.editor.parameter.SimpleParameterEditor;
import de.uniol.inf.is.odysseus.usermanagement.client.GlobalState;

public class ComboParameterEditor extends SimpleParameterEditor<String> implements IParameterEditor {

	@Override
	protected Control createInputControl(Composite parent) {
		// Combobox anstatt Texteingabe
		final Combo combo = createCombo(parent);
		if( combo == null ) 
			throw new IllegalArgumentException("combo is null");
		
		// Liste erstellen
		final String[] list = getList();
		for( String str: list )
			combo.add(str);
		
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
		setComboStartValue(combo);
		
		return combo;
	}
	
	protected Combo createCombo(Composite parent) {
		return new Combo(parent, SWT.BORDER);
	}
	
	protected void setComboStartValue( Combo combo ) {
		try {
			String txt = convertToString(String.valueOf(getValue()));
			if( txt == null || txt.equals("")) {
				combo.select(0);
			} else {
				for( int i = 0; i < combo.getItems().length; i++) {
					String item = combo.getItem(i);
					if( item.equalsIgnoreCase(txt)) {
						combo.select(i);
						return;
					}
				}
				combo.select(0);
			}
		} catch( Exception ex ) {
			if( combo.getItemCount() > 0 )
				combo.select(0);
			else 
				combo.setText("");
		}
	}
	
	// F�llt die Combo mit der Liste
	protected String[] getList() {
		// Liste der Quellen
		List<String> sources = new ArrayList<String>();
		for( Entry<String, ILogicalOperator> e : GlobalState.getActiveDatadictionary().getStreamsAndViews(GlobalState.getActiveSession(OdysseusRCPPlugIn.RCP_USER_TOKEN))) {
			sources.add(e.getKey());
		}
		return sources.toArray(new String[sources.size()]);
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
