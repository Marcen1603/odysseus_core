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
import java.util.ArrayList;
import java.util.List;

import org.eclipse.jface.resource.JFaceResources;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.LineStyleEvent;
import org.eclipse.swt.custom.LineStyleListener;
import org.eclipse.swt.custom.StyleRange;
import org.eclipse.swt.custom.StyledText;
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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.rcp.l10n.OdysseusNLS;

/**
 * @author Christian Kuka <christian@kuka.cc>
 *
 */
public class BugReportEditor extends Window {
    private static final Logger LOG = LoggerFactory.getLogger(BugReportEditor.class);

    StyledText generatedTextArea;
    StyledText userTextArea;

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
                sendButton.setText(OdysseusNLS.Sending);
                try {
                    boolean result = BugReport.send(BugReportEditor.this.userTextArea.getText() + "\n" + BugReportEditor.this.generatedTextArea.getText());
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
                    sendButton.setText(OdysseusNLS.SendBugReport);
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
        Label userLabel = new Label(parent, SWT.NONE);
        userLabel.setText("Reporter, please consider answering these questions, where appropriate");
        GridData data = new GridData(GridData.HORIZONTAL_ALIGN_FILL);
        data.widthHint = 600;
        userLabel.setLayoutData(data);

        this.userTextArea = new StyledText(parent, SWT.BORDER | SWT.V_SCROLL | SWT.MULTI);
        this.userTextArea.setEditable(true);
        this.userTextArea.addLineStyleListener(new SegmentLineStyleListener());
        data = new GridData(GridData.HORIZONTAL_ALIGN_FILL);
        data.widthHint = 600;
        data.heightHint = 300;
        this.userTextArea.setLayoutData(data);

        Label generatedLabel = new Label(parent, SWT.NONE);
        generatedLabel.setText("\nDebug Information:");
        data = new GridData(GridData.HORIZONTAL_ALIGN_FILL);
        data.widthHint = 600;
        generatedLabel.setLayoutData(data);

        this.generatedTextArea = new StyledText(parent, SWT.BORDER | SWT.V_SCROLL | SWT.MULTI);
        this.generatedTextArea.setEditable(true);
        data = new GridData(GridData.HORIZONTAL_ALIGN_FILL);
        data.widthHint = 600;
        data.heightHint = 200;
        this.generatedTextArea.setLayoutData(data);
    }

    public void setGeneratedReport(final String report) {
        this.generatedTextArea.setText(report);
    }

    public void setUserReport(final String report) {
        this.userTextArea.setText(report);
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

    public class SegmentLineStyleListener implements LineStyleListener {

        public SegmentLineStyleListener() {
            super();
        }

        @Override
        public void lineGetStyle(LineStyleEvent event) {
            List<StyleRange> styles = new ArrayList<>();
            @SuppressWarnings("unused")
			int start = 0;
            int length = event.lineText.length();
            System.out.println("current line length:" + event.lineText.length());
            if (event.lineText.startsWith("* ")) {
                StyleRange style = new StyleRange();
                style.start = event.lineOffset;
                style.length = length;
                style.fontStyle = SWT.BOLD;
                styles.add(style);
            }

            event.styles = styles.toArray(new StyleRange[0]);
        }

    }
}
