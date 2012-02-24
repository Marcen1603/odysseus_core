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
package de.uniol.inf.is.odysseus.rcp.editor.parameters.list;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;

import de.uniol.inf.is.odysseus.core.logicaloperator.ILogicalOperator;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.AbstractOperatorBuilder;
import de.uniol.inf.is.odysseus.rcp.editor.parameter.AbstractParameterEditor;
import de.uniol.inf.is.odysseus.rcp.editor.parameter.IParameterEditor;

public class AttributesParameterEditor extends AbstractParameterEditor implements IParameterEditor {

	private List<Button> checkBoxes = new ArrayList<Button>();
	private SDFSchema attributes;
	
	public AttributesParameterEditor() {
	}

	@SuppressWarnings("unchecked")
	@Override
	public void createControl(Composite parent) {
				
		GridLayout layout = new GridLayout();
		Composite container = new Composite(parent, SWT.NONE);
		container.setLayout(layout);
		
		// Schema des vorgängeroperators holen
		AbstractOperatorBuilder builder = (AbstractOperatorBuilder)getOperatorBuilder();
		
		// Label
		Label titleLabel = new Label(container, SWT.NONE);
		titleLabel.setText(getParameter().getName());

		if( builder.hasInputOperator(0) ) {
			ILogicalOperator op = builder.getInputOperator(0);
			attributes = op.getOutputSchema();
			
			// Checkboxes
			if( attributes != null && attributes.size() > 0) {
				for( int i = 0; i < attributes.size(); i++ ) {
					// Checkbox fÃ¼r jedes Attribut
					Button button = new Button( container, SWT.CHECK );
					button.setText(attributes.getAttribute(i).getSourceName() + "." + attributes.getAttribute(i).getAttributeName());
					button.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
					button.addSelectionListener(new SelectionAdapter() {
						@Override
						public void widgetSelected(SelectionEvent e) {
							save();
						}
					});
					checkBoxes.add(button);
					
				}
				
				// Hacken setzen, falls es schon ein Wert gibt
				List<SDFAttribute> checkedAttributes = (List<SDFAttribute>)getValue();
				if( checkedAttributes != null ) {
					for( int i = 0; i < checkedAttributes.size(); i++ ) {
						SDFAttribute att = checkedAttributes.get(i);
						int pos = attributes.indexOf(att);
						checkBoxes.get(pos).setSelection(true);
					}
				}
			} else {
				Label label = new Label(container, SWT.NONE);
				label.setText("Input-operator has no attribute-schema");
				label.setForeground(Display.getCurrent().getSystemColor(SWT.COLOR_RED));
			}
		} else {
			Label label = new Label(container, SWT.NONE);
			label.setText("No input-operator defined");
			label.setForeground(Display.getCurrent().getSystemColor(SWT.COLOR_RED));
		}
		
	}

	@Override
	public void close() {
		save();
	}

	private void save() {
		// Ausgewählte Attribute in Parameter speichern
		List<String> selectedAttributes = new ArrayList<String>();
		for( int i = 0; i < checkBoxes.size(); i++ ) {
			Button cb = checkBoxes.get(i);
			
			if( cb.getSelection() ) {
				// Im Titel des Buttons steht ja der vollstÃ¤ndige Attributname
				selectedAttributes.add(cb.getText()); 
			}
		}
		
		if( !selectedAttributes.isEmpty())
			setValue(selectedAttributes);
		else
			setValue(null);
	}
}
