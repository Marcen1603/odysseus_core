/*******************************************************************************
 * Copyright 2012 The Odysseus Team
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/
package de.uniol.inf.is.odysseus.rcp.viewer.editors;

import java.util.List;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;

import de.uniol.inf.is.odysseus.core.physicaloperator.IPhysicalOperator;

public class ChooseOperatorWindow {

	private final List<IPhysicalOperator> operators;
	private final Display display;
	
	private List<RadioButtonOperatorPair> radioOperatorPairs;
	private IPhysicalOperator selectedOperator;
	private Shell window;
	private boolean canceled;

	public ChooseOperatorWindow(Display display, List<IPhysicalOperator> operators ) {
		Preconditions.checkNotNull(operators, "List of physical operators must not be null!");
		Preconditions.checkArgument(!operators.isEmpty(), "List of physical operators must not be empty!");
		this.display = Preconditions.checkNotNull(display, "Display must not be null.");
		
		this.operators = operators;
	}

	public void show() {
		window = createWindow(display);
		window.setVisible(true);
		window.open();
		
		window.addDisposeListener(new DisposeListener() {
			@Override
			public void widgetDisposed(DisposeEvent e) {
				if( !isCanceled() ) {
					selectedOperator = determineSelection(radioOperatorPairs);
				}
			}

			private IPhysicalOperator determineSelection(List<RadioButtonOperatorPair> checkBoxes) {
				for( RadioButtonOperatorPair pair : checkBoxes ) {
					if( pair.radioButton.getSelection()) {
						return pair.operator;
					}
				}
				throw new IllegalStateException("No physical operator chosen!");
			}
		});
		
		processWindow();
	}
	
	public IPhysicalOperator getSelectedOperator() {
		return selectedOperator;
	}
	
	public boolean isCanceled() {
		return canceled;
	}

	private void processWindow() {
		while (!window.isDisposed())
			if (!display.readAndDispatch())
				display.sleep();
	}

	private Shell createWindow(Display display2) {
		Shell wnd = new Shell(display);
		wnd.setSize(500, 500);

		wnd.setText("Choose operator to show stream");
		wnd.setLayout(new GridLayout());

		Label label = new Label(wnd, SWT.NONE);
		label.setText("Query has more than one sink. Choose one operator\nto show the stream from.");
		
		radioOperatorPairs = createRadioButtons(wnd, operators);
		createRadioButtons(wnd);

		wnd.pack();
		return wnd;
	}
	
	private static List<RadioButtonOperatorPair> createRadioButtons(Shell shell, List<IPhysicalOperator> operators ) {
		Composite attributesComposite = new Composite(shell, SWT.NONE);
		attributesComposite.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, true, true));
		attributesComposite.setLayout(new GridLayout(1, true));
		List<RadioButtonOperatorPair> radioButtons = Lists.newArrayList();
		
		for( IPhysicalOperator operator : operators ) {
			Button radioButton = createRadioButton(attributesComposite, operator.getName());
			radioButtons.add(new RadioButtonOperatorPair(operator, radioButton));
		}
		
		return radioButtons;
	}
	
	private static Button createRadioButton( Composite composite, String title ) {
		Button checkBox = new Button(composite, SWT.RADIO);
		checkBox.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, true, true));
		checkBox.setText(title);
		return checkBox;
	}

	private void createRadioButtons(final Shell wnd) {
		Composite composite = new Composite(wnd, SWT.NONE);
		composite.setLayoutData(new GridData());
		composite.setLayout(new GridLayout(2, true));
			
		Button cancelButton = createButton(composite, "Cancel");
		cancelButton.addSelectionListener(new SelectionAdapter() {
			
			@Override
			public void widgetSelected(SelectionEvent e) {
				canceled = true;
				wnd.dispose();
			}
			
		});
		Button okButton = createButton(composite, "OK");
		okButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				canceled = false;
				wnd.dispose();
			}
		});
		
	}
		
	private static Button createButton( Composite composite, String title ) {
		Button button = new Button(composite, SWT.PUSH);
		button.setText(title);
		button.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));
		return button;
	}
}

class RadioButtonOperatorPair {
	
	public IPhysicalOperator operator;
	public Button radioButton;
	
	public RadioButtonOperatorPair( IPhysicalOperator operator, Button radioButton) {
		this.operator = operator;
		this.radioButton = radioButton;
	}
	
}

