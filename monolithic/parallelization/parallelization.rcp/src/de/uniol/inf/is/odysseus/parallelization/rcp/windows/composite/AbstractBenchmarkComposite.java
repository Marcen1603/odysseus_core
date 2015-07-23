/********************************************************************************** 
 * Copyright 2015 The Odysseus Team
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
package de.uniol.inf.is.odysseus.parallelization.rcp.windows.composite;

import org.eclipse.jface.resource.JFaceResources;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

public abstract class AbstractBenchmarkComposite extends Composite{

	public AbstractBenchmarkComposite(Composite parent, int style) {
		super(parent, style);
	}

	protected static Label createLabel(Composite generalComposite, String string) {
		Label label = new Label(generalComposite, SWT.WRAP | SWT.BORDER
				| SWT.LEFT);
		label.setText(string);
		return label;
	}
	
	protected static Label createBoldLabel(Composite generalComposite, String string) {
		Label label = new Label(generalComposite, SWT.WRAP | SWT.BORDER
				| SWT.LEFT);
		label.setFont(JFaceResources.getFontRegistry().getBold(JFaceResources.DEFAULT_FONT));
		label.setText(string);
		return label;
	}
	
	protected static Label createSeperator(Composite generalComposite){
		Label separator = new Label(generalComposite, SWT.HORIZONTAL | SWT.SEPARATOR);
		separator.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		return separator;
	}

	protected static void createLabelWithSeperator(Composite generalComposite,String string){
		createBoldLabel(generalComposite, string);
		createSeperator(generalComposite);
	}
	
	protected Text createTextWithLabel(Composite composite,
			GridData gridData, String labelString, String textString) {
		createLabel(composite, labelString);

		Text textBox = new Text(composite,
				SWT.SINGLE | SWT.BORDER);
		textBox.setText(textString);
		textBox.setLayoutData(gridData);
		
		return textBox;
	}
	
	protected Button createCheckButton(
			Composite composite,
			String buttonText) {
		Button button = new Button(this, SWT.CHECK);
		button.setText(buttonText);
		button.setSelection(true);
		return button;
	}
	
	protected Composite createDefaultComposite(){
		Composite composite = new Composite(this, SWT.NONE);
		GridLayout intraOperatorGridLayout = new GridLayout(2, false);
		composite.setLayout(intraOperatorGridLayout);
		return composite;
	}
	
	protected Combo createComboWithLabel(Composite composite, GridData gridData, String[] items, String labelString){
		createLabel(composite, labelString);
		
		Combo combo = new Combo(composite, SWT.READ_ONLY);
		combo.setItems(items);
		combo.select(0);
		combo.setLayoutData(gridData);
		return combo;
	}
}
