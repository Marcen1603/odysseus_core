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

import org.eclipse.jface.dialogs.TitleAreaDialog;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;

import de.uniol.inf.is.odysseus.core.physicaloperator.IPhysicalOperator;

public class ChooseOperatorWindow extends TitleAreaDialog {

	private final List<IPhysicalOperator> operators = Lists.newArrayList();
	private final Collection<IPhysicalOperator> selectedOperator = Lists.newArrayList();
	
	private List<CheckBoxOperatorPair> checkBoxOperatorPairs;
	
	public ChooseOperatorWindow(Shell parentShell, Collection<IPhysicalOperator> availableOperators) {
		super(parentShell);
		
		// Preconditions.checkNotNull(availableOperators, "List of physical operators must not be null!");
		// Preconditions.checkArgument(!availableOperators.isEmpty(), "List of physical operators must not be empty!");
		
		operators.addAll(availableOperators);
	}
	
	@Override
	protected Control createContents(Composite parent) {
		Control contents = super.createContents(parent);
		
		setTitle("Operator selection");
		setMessage("Select the operator to show the result stream from");
		getShell().setText("Operator selection");

		return contents;
	}
	
	@Override
	protected Control createDialogArea(Composite parent) {
		parent.setLayout(new GridLayout());
		
		Composite rootComposite = new Composite(parent, SWT.NONE);
		rootComposite.setLayout(new GridLayout(1, true));
		rootComposite.setLayoutData(new GridData(GridData.FILL_BOTH));

		checkBoxOperatorPairs = createCheckBoxes(rootComposite, operators);
		
		Composite buttonComposite = new Composite(rootComposite, SWT.NONE);
		buttonComposite.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		buttonComposite.setLayout(new GridLayout(2, true));
		
		Button selectAllButton = new Button(buttonComposite, SWT.PUSH);
		selectAllButton.setText("Select all");
		setButtonLayoutData(selectAllButton);
		selectAllButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				for( CheckBoxOperatorPair pair : checkBoxOperatorPairs ) {
					pair.checkBox.setSelection(true);
					pair.selected = true;
				}
			}
		});
		
		Button selectNoneButton = new Button(buttonComposite, SWT.PUSH);
		selectNoneButton.setText("Select none");
		setButtonLayoutData(selectNoneButton);
		selectNoneButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				for( CheckBoxOperatorPair pair : checkBoxOperatorPairs ) {
					pair.checkBox.setSelection(false);
					pair.selected = false;
				}
			}
		});

		return rootComposite;
	}
	
	private static List<CheckBoxOperatorPair> createCheckBoxes(Composite parent, List<IPhysicalOperator> operators ) {
		Composite checkBoxComposite = new Composite(parent, SWT.NONE);
		checkBoxComposite.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, true, true));
		checkBoxComposite.setLayout(new GridLayout(1, true));
		
		List<CheckBoxOperatorPair> checkBoxes = Lists.newArrayList();
		
		for( IPhysicalOperator operator : operators ) {
			final Button checkButton = new Button(checkBoxComposite, SWT.CHECK);
			checkButton.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
			checkButton.setText(determineTextForOperator(operator));
			
			final CheckBoxOperatorPair pair = new CheckBoxOperatorPair(operator, checkButton);
			checkButton.addSelectionListener(new SelectionAdapter() {
				@Override
				public void widgetSelected(SelectionEvent e) {
					pair.selected = checkButton.getSelection();
				}
			});
			checkBoxes.add(pair);
		}
		
		return checkBoxes;
	}
	
	private static String determineTextForOperator(IPhysicalOperator operator) {
		StringBuilder sb = new StringBuilder();

		sb.append(operator.getName());
		sb.append(" (").append(operator.getClass().getSimpleName()).append(")");

		return sb.toString();
	}
	
	@Override
	protected void createButtonsForButtonBar(Composite parent) {
		Button cancelButton = createButton(parent, Window.CANCEL, "Cancel", false);
		setButtonLayoutData(cancelButton);
		cancelButton.addSelectionListener(new SelectionAdapter() {
			
			@Override
			public void widgetSelected(SelectionEvent e) {
				setReturnCode(Window.CANCEL);
				selectedOperator.clear();
				close();
			}
			
		});
		Button okButton = createButton(parent, Window.OK, "OK", true);
		setButtonLayoutData(okButton);
		okButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				setReturnCode(Window.OK);
				selectedOperator.clear();
				selectedOperator.addAll(determineSelection(checkBoxOperatorPairs));
				close();
			}
		});		
	}

	private static Collection<IPhysicalOperator> determineSelection(List<CheckBoxOperatorPair> checkBoxes) {
		Collection<IPhysicalOperator> selectedOperators = Lists.newArrayList();
		for( CheckBoxOperatorPair pair : checkBoxes ) {
			if( pair.selected) {
				selectedOperators.add(pair.operator);
			}
		}
		
		return selectedOperators;
	}	

	public Collection<IPhysicalOperator> getSelectedOperator() {
		return selectedOperator;
	}
}

class CheckBoxOperatorPair {
	
	public final IPhysicalOperator operator;
	public final Button checkBox;
	
	public boolean selected;
	
	public CheckBoxOperatorPair( IPhysicalOperator operator, Button checkBox) {
		this.operator = operator;
		this.checkBox = checkBox;
	}
}

