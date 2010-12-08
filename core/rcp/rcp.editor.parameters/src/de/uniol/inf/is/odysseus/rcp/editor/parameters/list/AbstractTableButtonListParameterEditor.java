package de.uniol.inf.is.odysseus.rcp.editor.parameters.list;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.TableViewer;
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
		button.setImage( Activator.getDefault().getImageRegistry().get("addIcon"));
		button.addSelectionListener( new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				addDataItem( createNewDataRow() );
				refresh();
			}
		});
		
		return button;
	}

	protected Button createRemoveButton(Composite parent) {
		Button button = new Button(parent, SWT.PUSH);
		button.setToolTipText("Remove");
		button.setImage( Activator.getDefault().getImageRegistry().get("removeIcon"));
		button.addSelectionListener( new SelectionAdapter() {
			@SuppressWarnings("unchecked")
			@Override
			public void widgetSelected(SelectionEvent e) {
				TableViewer viewer = getTableViewer();
				ISelection selection = viewer.getSelection();
				if( selection != null ) {
					U selectedItem = (U)((IStructuredSelection)selection).getFirstElement();
					if( selectedItem != null ) {
						removeDataItem(selectedItem);
						refresh();
					}
				}
			}
		});
		
		return button;
	}

	protected Button createShiftUpButton(Composite parent) {
		Button button = new Button(parent, SWT.PUSH);
		button.setToolTipText("Shift Up");
		button.setImage( Activator.getDefault().getImageRegistry().get("upIcon"));
		button.addSelectionListener( new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
			}
		});
		
		return button;
	}

	protected Button createShiftDownButton(Composite parent) {
		Button button = new Button(parent, SWT.PUSH);
		button.setToolTipText("Shift Down");
		button.setImage( Activator.getDefault().getImageRegistry().get("downIcon"));
		button.addSelectionListener( new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
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
