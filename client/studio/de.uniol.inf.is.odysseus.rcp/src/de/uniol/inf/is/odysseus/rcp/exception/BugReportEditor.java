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

import java.io.IOException;

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
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.rcp.l10n.OdysseusNLS;

/**
 * @author Christian Kuka <christian@kuka.cc>
 *
 */
public class BugReportEditor extends Window {
    private static final Logger LOG = LoggerFactory.getLogger(BugReportEditor.class);

    Text textArea;

    /**
     * Class constructor.
     *
     * @param parentShell
     * @param bugReport
     */
    protected BugReportEditor(final Shell parentShell) {
        super(parentShell);
        this.setShellStyle(SWT.DIALOG_TRIM | SWT.APPLICATION_MODAL | Window.getDefaultOrientation());
    }

    protected Control createButtonBar(final Composite parent) {
        final Composite composite = new Composite(parent, SWT.NONE);
        final GridLayout layout = new GridLayout();
        layout.numColumns = 4;
        layout.makeColumnsEqualWidth = true;
        composite.setLayout(layout);
        final GridData data = new GridData(GridData.HORIZONTAL_ALIGN_END | GridData.VERTICAL_ALIGN_CENTER);
        composite.setLayoutData(data);
        composite.setFont(parent.getFont());
        this.createButtonsForButtonBar(composite);
        return composite;
    }

    private void createButtonsForButtonBar(final Composite parent) {
        final Label errorLabel = new Label(parent, SWT.NONE);
        errorLabel.setText("");
        GridData data = new GridData(GridData.HORIZONTAL_ALIGN_FILL);
        data.horizontalSpan = 2;
        errorLabel.setLayoutData(data);

        final Button cancelButton = new Button(parent, SWT.PUSH);
        cancelButton.setText(OdysseusNLS.Cancel);
        cancelButton.setFont(JFaceResources.getDialogFont());
        data = new GridData(GridData.HORIZONTAL_ALIGN_FILL);
        Point minSize = cancelButton.computeSize(SWT.DEFAULT, SWT.DEFAULT, true);
        data.widthHint = minSize.x;
        cancelButton.setLayoutData(data);
        cancelButton.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(final SelectionEvent event) {
                BugReportEditor.this.setReturnCode(Window.CANCEL);
                BugReportEditor.this.close();
            }
        });

        final Button sendButton = new Button(parent, SWT.PUSH);
        sendButton.setText(OdysseusNLS.SendBugReport);
        sendButton.setFont(JFaceResources.getDialogFont());
        data = new GridData(GridData.HORIZONTAL_ALIGN_FILL);
        minSize = cancelButton.computeSize(SWT.DEFAULT, SWT.DEFAULT, true);
        data.widthHint = minSize.x;
        cancelButton.setLayoutData(data);
        sendButton.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(final SelectionEvent event) {
                try {
                    sendButton.setText(OdysseusNLS.Sending);
                    boolean result = BugReport.send(BugReportEditor.this.textArea.getText());
                    if (result) {
                        BugReportEditor.this.setReturnCode(Window.OK);
                    }
                    else {
                        BugReportEditor.this.setReturnCode(Window.CANCEL);
                    }
                    BugReportEditor.this.close();
                }
                catch (IOException e) {
                    errorLabel.setText(e.getMessage());
                    LOG.error(e.getMessage(), e);

                }

            }
        });
        final Shell shell = parent.getShell();
        if (shell != null) {
            shell.setDefaultButton(sendButton);
        }
    }

    protected Control createDialogArea(final Composite parent) {
        final Composite composite = new Composite(parent, SWT.NONE);
        final GridLayout layout = new GridLayout();
        composite.setLayout(layout);
        composite.setLayoutData(new GridData(GridData.FILL_BOTH));
        this.createTextArea(parent);
        return composite;
    }

    protected void createTextArea(final Composite parent) {
        this.textArea = new Text(parent, SWT.BORDER | SWT.V_SCROLL | SWT.MULTI);
        this.textArea.setEditable(true);
        final GridData data = new GridData(GridData.HORIZONTAL_ALIGN_FILL);
        data.widthHint = 600;
        data.heightHint = 300;
        this.textArea.setLayoutData(data);
    }

    public void setReport(final String report) {
        this.textArea.setText(report);
    }

    @Override
    protected Control createContents(final Composite parent) {
        final Composite composite = new Composite(parent, 0);
        final GridLayout layout = new GridLayout();
        layout.marginHeight = 0;
        layout.marginWidth = 0;
        layout.verticalSpacing = 0;

        composite.setLayout(layout);
        composite.setLayoutData(new GridData(GridData.FILL_BOTH));
        this.createDialogArea(composite);
        this.createButtonBar(composite);

        return composite;
    }

}
