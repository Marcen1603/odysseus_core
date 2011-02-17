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

import de.uniol.inf.is.odysseus.rcp.editor.parameters.activator.Activator;

public abstract class AbstractTableButtonListParameterEditor<T, U, V> extends AbstractTableListParameterEditor<T, U, V> {

	private Composite buttonComposite;
	private Button addButton;
	private Button removeButton;
	private Button shiftUpButton;
	private Button shiftDownButton;

	protected Composite getButtonComposite() {
		return buttonComposite;
	}

	protected Button getAddButton() {
		return addButton;
	}

	protected Button getRemoveButton() {
		return removeButton;
	}

	protected Button getShiftUpButton() {
		return shiftUpButton;
	}

	protected Button getShiftDownButton() {
		return shiftDownButton;
	}

	@Override
	protected List<Button> createButtons(Composite parent) {

		setButtonComposite(createButtonComposite(parent));

		List<Button> buttons = new ArrayList<Button>();

		setAddButton(createAddButton(getButtonComposite()));
		setRemoveButton(createRemoveButton(getButtonComposite()));
		setShiftUpButton(createShiftUpButton(getButtonComposite()));
		setShiftDownButton(createShiftDownButton(getButtonComposite()));

		buttons.add(getAddButton());
		buttons.add(getRemoveButton());
		buttons.add(getShiftUpButton());
		buttons.add(getShiftDownButton());
		return buttons;
	}

	protected Composite createButtonComposite(Composite parent) {
		Composite buttonComposite = new Composite(parent, SWT.NONE);
		GridLayout gridLayout = new GridLayout();
		gridLayout.numColumns = 4;
		buttonComposite.setLayout(gridLayout);
		buttonComposite.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		return buttonComposite;
	}

	protected Button createAddButton(Composite parent) {
		Button button = new Button(parent, SWT.PUSH);
		button.setToolTipText("Add");
		button.setImage(Activator.getDefault().getImageRegistry().get("addIcon"));
		button.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				addDataItem(createNewDataRow());
				refresh();
			}
		});

		return button;
	}

	protected Button createRemoveButton(Composite parent) {
		Button button = new Button(parent, SWT.PUSH);
		button.setToolTipText("Remove");
		button.setImage(Activator.getDefault().getImageRegistry().get("removeIcon"));
		button.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				U selectedItem = getSelection();
				if (selectedItem != null) {
					removeDataItem(selectedItem);
					refresh();
				}
			}
		});

		return button;
	}

	protected Button createShiftUpButton(Composite parent) {
		Button button = new Button(parent, SWT.PUSH);
		button.setToolTipText("Shift Up");
		button.setImage(Activator.getDefault().getImageRegistry().get("upIcon"));
		button.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				U selectedItem = getSelection();
				if (selectedItem != null) {
					int index = getData().indexOf(selectedItem);
					if( index > 0 ) {
						U upperItem = getData().get(index - 1);
						getData().set(index-1, selectedItem);
						getData().set(index, upperItem);
						
						refresh();
					}
				}
			}
		});

		return button;
	}

	protected Button createShiftDownButton(Composite parent) {
		Button button = new Button(parent, SWT.PUSH);
		button.setToolTipText("Shift Down");
		button.setImage(Activator.getDefault().getImageRegistry().get("downIcon"));
		button.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				U selectedItem = getSelection();
				if (selectedItem != null) {
					int index = getData().indexOf(selectedItem);
					if( index < getData().size() - 1 ) {
						U upperItem = getData().get(index + 1);
						getData().set(index+1, selectedItem);
						getData().set(index, upperItem);
						
						refresh();
					}
				}
			}
		});

		return button;
	}

	private void setShiftDownButton(Button shiftDownButton) {
		if (shiftDownButton == null)
			throw new IllegalArgumentException("shiftDownButton is null");

		this.shiftDownButton = shiftDownButton;
	}

	private void setShiftUpButton(Button shiftUpButton) {
		if (shiftUpButton == null)
			throw new IllegalArgumentException("shiftUpButton is null");
		this.shiftUpButton = shiftUpButton;
	}

	private void setRemoveButton(Button removeButton) {
		if (removeButton == null)
			throw new IllegalArgumentException("removeButton is null");

		this.removeButton = removeButton;
	}

	private void setAddButton(Button addButton) {
		if (addButton == null)
			throw new IllegalArgumentException("addButton is null");

		this.addButton = addButton;
	}

	private void setButtonComposite(Composite buttonComposite) {
		if (buttonComposite == null)
			throw new IllegalArgumentException("buttonComposite is null");

		this.buttonComposite = buttonComposite;
	}

	protected abstract U createNewDataRow();
}
