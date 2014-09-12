/*******************************************************************************
 * Copyright (C) 2014  Christian Kuka <christian@kuka.cc>
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 2 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *******************************************************************************/
package de.uniol.inf.is.odysseus.rcp.exception;

import org.eclipse.jface.resource.JFaceResources;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

import de.uniol.inf.is.odysseus.rcp.l10n.OdysseusNLS;

/**
 * @author Christian Kuka <christian@kuka.cc>
 *
 */
public class BugReportEditor extends Window {
    private final BugReport reportManager;
    private Text textArea;

    /**
     * Class constructor.
     *
     * @param parentShell
     * @param bugReport
     */
    protected BugReportEditor(Shell parentShell, BugReport reportManager) {
        super(parentShell);
        this.reportManager = reportManager;
        setShellStyle(SWT.DIALOG_TRIM | SWT.APPLICATION_MODAL | getDefaultOrientation());
    }

    protected Control createButtonBar(Composite parent) {
        Composite composite = new Composite(parent, SWT.NONE);
        GridLayout layout = new GridLayout();
        layout.numColumns = 2;
        layout.makeColumnsEqualWidth = true;
        composite.setLayout(layout);
        GridData data = new GridData(GridData.HORIZONTAL_ALIGN_END | GridData.VERTICAL_ALIGN_CENTER);
        composite.setLayoutData(data);
        composite.setFont(parent.getFont());
        createButtonsForButtonBar(composite);
        return composite;
    }

    private void createButtonsForButtonBar(final Composite parent) {
        Button cancelButton = new Button(parent, SWT.PUSH);
        cancelButton.setText(OdysseusNLS.Cancel);
        cancelButton.setFont(JFaceResources.getDialogFont());
        GridData data = new GridData(GridData.HORIZONTAL_ALIGN_FILL);
        Point minSize = cancelButton.computeSize(SWT.DEFAULT, SWT.DEFAULT, true);
        data.widthHint = minSize.x;
        cancelButton.setLayoutData(data);
        cancelButton.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent event) {
                setReturnCode(CANCEL);
                close();
            }
        });

        Button sendButton = new Button(parent, SWT.PUSH);
        sendButton.setText(OdysseusNLS.SendBugReport);
        sendButton.setFont(JFaceResources.getDialogFont());
        data = new GridData(GridData.HORIZONTAL_ALIGN_FILL);
        minSize = cancelButton.computeSize(SWT.DEFAULT, SWT.DEFAULT, true);
        data.widthHint = minSize.x;
        cancelButton.setLayoutData(data);
        sendButton.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent event) {
                reportManager.send(textArea.getText());
                setReturnCode(OK);
                close();
            }
        });
        Shell shell = parent.getShell();
        if (shell != null) {
            shell.setDefaultButton(sendButton);
        }
    }

    protected Control createDialogArea(Composite parent) {
        Composite composite = new Composite(parent, SWT.NONE);
        GridLayout layout = new GridLayout();
        composite.setLayout(layout);
        composite.setLayoutData(new GridData(GridData.FILL_BOTH));
        createTextArea(parent);
        return composite;
    }

    protected void createTextArea(Composite parent) {
        textArea = new Text(parent, SWT.BORDER | SWT.V_SCROLL | SWT.MULTI);
        textArea.setEditable(true);
        GridData data = new GridData(GridData.HORIZONTAL_ALIGN_FILL);
        Point minSize = textArea.computeSize(SWT.DEFAULT, SWT.DEFAULT, true);
        data.widthHint = 600;
        data.heightHint = 300;
        textArea.setLayoutData(data);
    }

    public void setReport(String report) {
        textArea.setText(report);
    }

    @Override
    protected Control createContents(Composite parent) {
        Composite composite = new Composite(parent, 0);
        GridLayout layout = new GridLayout();
        layout.marginHeight = 0;
        layout.marginWidth = 0;
        layout.verticalSpacing = 0;

        composite.setLayout(layout);
        composite.setLayoutData(new GridData(GridData.FILL_BOTH));
        Control dialogArea = createDialogArea(composite);
        Control buttonBar = createButtonBar(composite);

        return composite;
    }

}
