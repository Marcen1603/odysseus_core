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
package de.uniol.inf.is.odysseus.rcp.windows;

import org.eclipse.core.runtime.Assert;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.KeyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

import de.uniol.inf.is.odysseus.rcp.Connect;
import de.uniol.inf.is.odysseus.rcp.l10n.OdysseusNLS;
import de.uniol.inf.is.odysseus.rcp.util.ConnectPreferencesManager;

/**
 * 
 * @author Merlin Wasmann
 *
 */

@SuppressWarnings("unused")
public class ClientConnectionWindow {

	private static final String TITLE_TEXT = "Odysseus RCP Client Connection";

	private static final String WSDLLOCATION_TEXT = "WSDL Location";
	private static final String SERVICE_TEXT = "Service";
	private static final String NAMESPACE_TEXT = "Service Namespace";

	private static final String OK_BUTTON_TEXT = "OK";
	private static final String CANCEL_BUTTON_TEXT = "Cancel";

	private Shell wnd;

	private Label wsdlLocationLabel;
	private Label serviceLabel;
	private Label serviceNamespaceLabel;
	private Text wsdlLocationInput;
	private Text serviceInput;
	private Text serviceNamespaceInput;
	private Button okButton;
	private Button cancelButton;

	private final String startWsdlLocation;
	private final boolean cancelOK;
	private boolean connected = false;
	private final Display display;

	public ClientConnectionWindow(Display parent, boolean cancelOK) {
		this(parent, "", cancelOK);
	}

	public ClientConnectionWindow(Display parent, String wsdlLocation,
			boolean cancelOK) {
		Assert.isNotNull(wsdlLocation);
		Assert.isNotNull(parent);
		startWsdlLocation = wsdlLocation;
		this.cancelOK = cancelOK;
		this.display = parent;
	}

	public void show() {
		wnd = createWindow();
		wnd.setVisible(true);

		while (!wnd.isDisposed())
			if (!display.readAndDispatch())
				display.sleep();
	}

	private Shell createWindow() {
		Shell wnd = new Shell(display, SWT.APPLICATION_MODAL | SWT.DIALOG_TRIM);

		wnd.setLayout(new GridLayout());
		wnd.setText(TITLE_TEXT);
		wnd.setSize(400, 180);

		wnd.setLayout(new GridLayout());

		wnd.addDisposeListener(new DisposeListener() {

			@Override
			public void widgetDisposed(DisposeEvent e) {
				if (connected)
					return;

				if (!cancelOK)
					System.exit(0);
			}

		});

		createInput(wnd);

		createButtons(wnd);

		wnd.layout();
		wsdlLocationInput.setFocus();

		return wnd;
	}

	private void createInput(Composite parent) {
		Composite comp = new Composite(parent, SWT.NONE);
		comp.setLayoutData(new GridData(GridData.FILL_BOTH));

		GridLayout layout = new GridLayout();
		layout.numColumns = 2;
		comp.setLayout(layout);

		wsdlLocationLabel = new Label(comp, SWT.NONE);
		wsdlLocationLabel.setText(OdysseusNLS.WsdlLocation + ":");
		wsdlLocationInput = new Text(comp, SWT.BORDER | SWT.SINGLE);
		wsdlLocationInput.setText(startWsdlLocation);
		wsdlLocationInput.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));

		serviceNamespaceLabel = new Label(comp, SWT.NONE);
		serviceNamespaceLabel.setText(OdysseusNLS.ServiceNamespace + ":");
		serviceNamespaceInput = new Text(comp, SWT.BORDER | SWT.SINGLE);
		serviceNamespaceInput.setLayoutData(new GridData(
				GridData.FILL_HORIZONTAL));

		serviceLabel = new Label(comp, SWT.NONE);
		serviceLabel.setText(OdysseusNLS.WebService + ":");
		serviceInput = new Text(comp, SWT.BORDER | SWT.SINGLE);
		serviceInput.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		serviceInput.addKeyListener(new KeyListener() {

			@Override
			public void keyPressed(KeyEvent e) {
				if (e.keyCode == 13) {
					tryToConnect();
				}
			}

			@Override
			public void keyReleased(KeyEvent e) {
			}

		});
	}

	private void markRed() {
		wsdlLocationLabel.setForeground(Display.getCurrent().getSystemColor(
				SWT.COLOR_RED));
		serviceNamespaceLabel.setForeground(Display.getCurrent()
				.getSystemColor(SWT.COLOR_RED));
		serviceLabel.setForeground(Display.getCurrent().getSystemColor(
				SWT.COLOR_RED));
	}

	private void createButtons(Composite parent) {
		Composite comp = new Composite(parent, SWT.NONE);
		comp.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));

		GridLayout layout = new GridLayout();
		layout.numColumns = 2;
		comp.setLayout(layout);

		okButton = new Button(comp, SWT.PUSH);
		okButton.setText(OdysseusNLS.OK);
		okButton.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		okButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				tryToConnect();
			}
		});

		cancelButton = new Button(comp, SWT.PUSH);
		cancelButton.setText(OdysseusNLS.Cancel);
		cancelButton.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		cancelButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				System.exit(0);
			}
		});
	}

	// TODO: trying to connect to server
	public void tryToConnect() {
		if (Connect.realConnect(wsdlLocationInput.getText(),
				serviceInput.getText(), serviceNamespaceInput.getText())) {
			this.connected = true;

			// TODO: autologinkram...
			// if(autoConnectCheck.getSelection()) {
			// ConnectPreferencesManager.getInstance().setWdslLocation(wsdlLocationInput.getText());
			// ConnectPreferencesManager.getInstance().setService(serviceInput.getText());
			// }
			// ConnectPreferencesManager.getInstance().setAutoConnect(autoConnectCheck.getSelection());
			// ConnectPreferencesManager.getInstance().save();

			wnd.dispose();
		} else {
			this.connected = false;
			markRed();
		}
	}
}
