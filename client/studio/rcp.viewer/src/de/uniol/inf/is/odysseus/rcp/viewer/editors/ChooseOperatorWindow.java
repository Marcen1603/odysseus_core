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

import java.util.Collection;
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
	private final Collection<IPhysicalOperator> selectedOperator = Lists.newArrayList();
	
	private List<CheckBoxOperatorPair> checkBoxOperatorPairs;
	
	private Shell window;
	private boolean canceled;

	public ChooseOperatorWindow(Display display, List<IPhysicalOperator> operators ) {
		Preconditions.checkNotNull(operators, "List of physical operators must not be null!");
		Preconditions.checkArgument(!operators.isEmpty(), "List of physical operators must not be empty!");
		this.display = Preconditions.checkNotNull(display, "Display must not be null.");
		
		this.operators = operators;
	}

	public void show() {
		window = createWindow();
		window.setVisible(true);
		window.open();
		
		window.addDisposeListener(new DisposeListener() {
			@Override
			public void widgetDisposed(DisposeEvent e) {
				if( !isCanceled() ) {
					selectedOperator.clear();
					selectedOperator.addAll(determineSelection(checkBoxOperatorPairs));
				}
			}

		});
		
		processWindow();
	}
	
	public Collection<IPhysicalOperator> getSelectedOperator() {
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

	private Shell createWindow() {
		Shell wnd = new Shell(display);
		wnd.setSize(500, 500);

		wnd.setText("Choose operator to show stream");
		wnd.setLayout(new GridLayout());

		Label label = new Label(wnd, SWT.WRAP);
		label.setText("Query has more than one sink. Choose one or more operators\n to show the streams from.");
		
		checkBoxOperatorPairs = createCheckBoxes(wnd, operators);
		createButtons(wnd);

		wnd.pack();
		return wnd;
	}
	
	private static List<CheckBoxOperatorPair> createCheckBoxes(Shell shell, List<IPhysicalOperator> operators ) {
		Composite attributesComposite = new Composite(shell, SWT.NONE);
		attributesComposite.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, true, true));
		attributesComposite.setLayout(new GridLayout(1, true));
		
		List<CheckBoxOperatorPair> checkBoxes = Lists.newArrayList();
		
		for( IPhysicalOperator operator : operators ) {
			Button checkButton = createCheckBox(attributesComposite, operator);
			checkBoxes.add(new CheckBoxOperatorPair(operator, checkButton));
		}
		
		return checkBoxes;
	}
	
	private static Button createCheckBox( Composite composite, IPhysicalOperator operator) {
		Button checkBox = new Button(composite, SWT.CHECK);
		checkBox.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		checkBox.setText(determineTextForOperator(operator));
		return checkBox;
	}

	private static String determineTextForOperator(IPhysicalOperator operator) {
		StringBuilder sb = new StringBuilder();
		
		sb.append(operator.getName());
		sb.append(" (").append(operator.getClass().getSimpleName()).append(")");
		
		return sb.toString();
	}

	private void createButtons(final Shell wnd) {
		Composite composite = new Composite(wnd, SWT.NONE);
		composite.setLayoutData(new GridData());
		composite.setLayout(new GridLayout(2, true));
		
		Button selectAllButton = createButton(composite, "Select all");
		selectAllButton.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		selectAllButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				for( CheckBoxOperatorPair pair : checkBoxOperatorPairs ) {
					pair.checkBox.setSelection(true);
				}
			}
		});
		
		Button selectNoneButton = createButton(composite, "Select none");
		selectNoneButton.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		selectNoneButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				for( CheckBoxOperatorPair pair : checkBoxOperatorPairs ) {
					pair.checkBox.setSelection(false);
				}
			}
		});

		Button cancelButton = createButton(composite, "Cancel");
		cancelButton.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		cancelButton.addSelectionListener(new SelectionAdapter() {
			
			@Override
			public void widgetSelected(SelectionEvent e) {
				canceled = true;
				wnd.dispose();
			}
			
		});
		Button okButton = createButton(composite, "OK");
		okButton.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
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
		return button;
	}
	
	private static Collection<IPhysicalOperator> determineSelection(List<CheckBoxOperatorPair> checkBoxes) {
		Collection<IPhysicalOperator> selectedOperators = Lists.newArrayList();
		for( CheckBoxOperatorPair pair : checkBoxes ) {
			if( pair.checkBox.getSelection()) {
				selectedOperators.add(pair.operator);
			}
		}
		
		return selectedOperators;
	}
}

class CheckBoxOperatorPair {
	
	public IPhysicalOperator operator;
	public Button checkBox;
	
	public CheckBoxOperatorPair( IPhysicalOperator operator, Button radioButton) {
		this.operator = operator;
		this.checkBox = radioButton;
	}
	
}

