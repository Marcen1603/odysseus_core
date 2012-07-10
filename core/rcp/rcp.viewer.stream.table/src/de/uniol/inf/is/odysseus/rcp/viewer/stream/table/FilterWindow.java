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
package de.uniol.inf.is.odysseus.rcp.viewer.stream.table;

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
import org.eclipse.swt.widgets.Shell;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;

import de.uniol.inf.is.odysseus.core.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;

public class FilterWindow {

	private final SDFSchema schema;
	private final Display display;
	
	private List<Integer> selectedAttributeIndices;
	private List<CheckBoxIndexPair> checkBoxes;
	private Shell window;
	private boolean canceled;

	public FilterWindow(Display display, SDFSchema schema, List<Integer> selectedAttributeIndices ) {
		this.schema = Preconditions.checkNotNull(schema, "Schema must not be null.");
		this.display = Preconditions.checkNotNull(display, "Display must not be null.");
		this.selectedAttributeIndices = Preconditions.checkNotNull(selectedAttributeIndices, "List of indices of selected attributes must not be null!");
	}

	public void show() {
		window = createWindow(display);
		window.setVisible(true);
		window.open();
		
		window.addDisposeListener(new DisposeListener() {
			@Override
			public void widgetDisposed(DisposeEvent e) {
				if( !isCanceled() ) {
					selectedAttributeIndices = determineSelection(checkBoxes);
				}
			}

			private List<Integer> determineSelection(List<CheckBoxIndexPair> checkBoxes) {
				List<Integer> selection = Lists.newArrayList();
				for( CheckBoxIndexPair pair : checkBoxes ) {
					if( pair.checkBox.getSelection()) {
						selection.add(pair.index);
					}
				}
				return selection;
			}
		});
		
		processWindow();
	}
	
	public List<Integer> getSelectedAttributeIndices() {
		return selectedAttributeIndices;
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

		wnd.setText("Filter");
		wnd.setLayout(new GridLayout());

		checkBoxes = createCheckboxes(wnd, schema, selectedAttributeIndices);
		createButtons(wnd);

		wnd.pack();
		return wnd;
	}
	
	private static List<CheckBoxIndexPair> createCheckboxes(Shell shell, SDFSchema schema, List<Integer> selectedIndices ) {
		Composite attributesComposite = new Composite(shell, SWT.NONE);
		attributesComposite.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, true, true));
		attributesComposite.setLayout(new GridLayout(3, true));
		List<CheckBoxIndexPair> checkBoxes = Lists.newArrayList();
		
		for( SDFAttribute attribute : schema ) {
			Button checkBox = createCheckBox(attributesComposite, attribute.getURI());
			Integer index = schema.indexOf(attribute);
			checkBox.setSelection(selectedIndices.contains(index));
			checkBoxes.add(new CheckBoxIndexPair(index, checkBox));
		}
		
		return checkBoxes;
	}
	
	private static Button createCheckBox( Composite composite, String title ) {
		Button checkBox = new Button(composite, SWT.CHECK);
		checkBox.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, true, true));
		checkBox.setText(title);
		return checkBox;
	}

	private void createButtons(final Shell wnd) {
		Composite composite = new Composite(wnd, SWT.NONE);
		composite.setLayoutData(new GridData());
		composite.setLayout(new GridLayout(4, true));
		
		Button selectAllButton = createButton(composite, "Select All");
		selectAllButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				setSelectionOfAllCheckBoxes(true);
			}
		});
		
		Button deselectAllButton = createButton(composite, "Deselect All");
		deselectAllButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				setSelectionOfAllCheckBoxes(false);
			}
		});

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
	
	private void setSelectionOfAllCheckBoxes( boolean selection ) {
		for( CheckBoxIndexPair pair : checkBoxes ) {
			pair.checkBox.setSelection(selection);
		}
	}
	
	private static Button createButton( Composite composite, String title ) {
		Button button = new Button(composite, SWT.PUSH);
		button.setText(title);
		button.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));
		return button;
	}
}

class CheckBoxIndexPair {
	
	public int index;
	public Button checkBox;
	
	public CheckBoxIndexPair( int index, Button checkBox) {
		this.index = index;
		this.checkBox = Preconditions.checkNotNull(checkBox, "CheckBox must not be null.");
	}
	
}

