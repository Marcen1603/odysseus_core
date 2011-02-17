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
package de.uniol.inf.is.odysseus.rcp.editor.parameter;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

public abstract class SimpleParameterEditor<T> extends AbstractParameterEditor {
	
	private Control control;
	
	@Override
	public void createControl(Composite parent) {
		GridLayout gl = new GridLayout();
		gl.numColumns = 2;
		parent.setLayout(gl);
		
		// Label
		createLabel(parent);
		
		// Eingabe
		control = createInputControl(parent);
		control.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
	}
	
	public abstract T convertFromString( String txt );
	public abstract String convertToString( T object );
	
	protected String getLabelTitle() {
		return getParameter().getName();
	}

	protected Label createLabel(Composite parent) {
		Label label = new Label( parent, SWT.CENTER );
		label.setText(getLabelTitle());
		return label;
	}
	
	protected Control createInputControl(Composite parent) {
		final Text text = new Text(parent, SWT.SINGLE | SWT.BORDER);
		text.addModifyListener(new ModifyListener() {

			@Override
			public void modifyText(ModifyEvent e) {
				try {
					if( text.getText().length() == 0 )
						setValue(null);
					else 
						setValue(convertFromString(text.getText()));
										
				} catch( Exception ex ) {
					ex.printStackTrace();
				}
			}
			
		});
		
		// Anfangstext setzen
		try {
			@SuppressWarnings("unchecked")
			String txt = convertToString((T)getValue());
			text.setText(txt == null ? "" : txt );
		} catch( Exception ex ) {
			text.setText("");
		}

		return text;
	}
	
	@Override
	public void close() {

	}

	public Control getInputControl() {
		return control;
	}
}
