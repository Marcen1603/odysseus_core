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
package de.uniol.inf.is.odysseus.rcp.viewer.stream.map;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

public class PropertyWindow {

	private final Display display;
	
	private Shell window;
	private boolean canceled;
	private String name;

	public PropertyWindow(final Display display, String name) {
		this.display = display;
		this.name = name;
	}

	public void show() {
		window = createWindow(display, name );
		window.setVisible(true);
		window.open();
		
		processWindow();
	}

	public boolean isCanceled() {
		return canceled;
	}

	private void processWindow() {
		while (!window.isDisposed())
			if (!display.readAndDispatch())
				display.sleep();
	}

	private Shell createWindow(Display display, String name) {
		Shell wnd = new Shell(display);
		wnd.setSize(500, 500);

		wnd.setText(name);
		wnd.setLayout(new GridLayout());

		createButtons(wnd);

		wnd.pack();
		return wnd;
	}

	private void createButtons(final Shell wnd) {
		Composite composite = new Composite(wnd, SWT.NONE);
		composite.setLayoutData(new GridData());
		composite.setLayout(new GridLayout(4, true));

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

