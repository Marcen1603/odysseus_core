package de.uniol.inf.is.odysseus.visualquerylanguage.swt;

import java.awt.Toolkit;
import java.util.ArrayList;
import java.util.Collection;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;

import de.uniol.inf.is.odysseus.logicaloperator.base.WindowType;

public class SWTWindowEditor {

	private Shell shell;

	private Collection<ISWTParameterListener> listeners = new ArrayList<ISWTParameterListener>();

	public SWTWindowEditor(Shell baseWindow) {
		shell = new Shell(baseWindow, SWT.RESIZE | SWT.CLOSE
				| SWT.APPLICATION_MODAL);
		shell.setText("WindowTypeEditor");
		GridLayout gl = new GridLayout();
		gl.numColumns = 1;
		shell.setLayout(gl);

		Composite comp = new org.eclipse.swt.widgets.Composite(shell,
				SWT.BORDER);
		GridLayout compLayout = new GridLayout();
		compLayout.numColumns = 2;
		compLayout.makeColumnsEqualWidth = true;
		comp.setLayout(compLayout);
		GridData compData = new GridData(GridData.FILL_BOTH);
		comp.setLayoutData(compData);

		Label label = new Label(comp, SWT.LEFT);
		label.setText("Fenstertyp auswählen: ");

		final Combo combo = new Combo(comp, SWT.DROP_DOWN | SWT.READ_ONLY);
		combo.setData(WindowType.values());

		for (WindowType type : WindowType.values()) {
			combo.add(type.toString());
		}
		if(combo.getItemCount() > 0) {
			combo.select(0);
		}

		Button applyButton = new Button(shell, SWT.PUSH);
		applyButton.setText("Fenstertyp setzen");
		applyButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				for (ISWTParameterListener listener : listeners) {
					Object windowType = null;
					for (WindowType type : WindowType.values()) {
						if(type.toString().equals(combo.getItem(combo.getSelectionIndex()))) {
							windowType = type; 
						}
					}
					listener.setValue(windowType);
				}
				shell.dispose();
			}
		});

		Button cancelButton = new Button(shell, SWT.PUSH);
		cancelButton.setText("Abbrechen");
		cancelButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				shell.close();
				shell.dispose();
			}
		});
		
		shell.pack();
		shell.setLocation(Toolkit.getDefaultToolkit().getScreenSize().width / 2
				- shell.getSize().x / 2, Toolkit.getDefaultToolkit()
				.getScreenSize().height
				/ 2 - shell.getSize().y / 2);
		shell.open();
	}

	public void addSWTParameterListener(ISWTParameterListener listener) {
		this.listeners.add(listener);
	}

}
