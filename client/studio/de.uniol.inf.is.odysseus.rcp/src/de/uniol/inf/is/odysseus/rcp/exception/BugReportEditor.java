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

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.resource.JFaceResources;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.SWT;
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

import de.uniol.inf.is.odysseus.rcp.config.OdysseusRCPConfiguration;
import de.uniol.inf.is.odysseus.rcp.l10n.OdysseusNLS;

/**
 * @author Christian Kuka <christian@kuka.cc>
 *
 */
public class BugReportEditor extends Window {
	
	private static final String DIALOG_TITLE = "Send bug report";
	private static final Logger LOG = LoggerFactory.getLogger(BugReportEditor.class);

    private StyledText generatedTextArea;
    private StyledText userTextArea;

    protected BugReportEditor(final Shell parentShell) {
        super(parentShell);
        this.setShellStyle(SWT.DIALOG_TRIM | SWT.APPLICATION_MODAL | Window.getDefaultOrientation());
    }
    
    @Override
    protected void configureShell(Shell newShell) {
    	newShell.setText(DIALOG_TITLE);
    	
    	super.configureShell(newShell);
    }

    @Override
    protected Control createContents(final Composite parent) {
        Composite composite = new Composite(parent, 0);
        GridLayout layout = new GridLayout();
        layout.marginHeight = 0;
        layout.marginWidth = 0;
        layout.verticalSpacing = 0;

        composite.setLayout(layout);
        composite.setLayoutData(new GridData(GridData.FILL_BOTH));
        createDialogArea(composite);
        createButtonBar(composite);

        return composite;
    }
    
    private Control createButtonBar(final Composite parent) {
        Composite composite = new Composite(parent, SWT.NONE);
        GridLayout layout = new GridLayout();
        layout.numColumns = 5;
        layout.makeColumnsEqualWidth = true;
        composite.setLayout(layout);
        composite.setFont(parent.getFont());
        
        GridData data = new GridData(GridData.HORIZONTAL_ALIGN_END | GridData.VERTICAL_ALIGN_CENTER);
        composite.setLayoutData(data);
        
        createButtonsForButtonBar(composite);
        
        return composite;
    }

    private void createButtonsForButtonBar(final Composite parent) {
		Button jiraButton = createButton(parent, "Set JIRA account...");
        jiraButton.addSelectionListener(new SelectionAdapter() {
        	@Override
        	public void widgetSelected(SelectionEvent e) {
        		String savedUsername = OdysseusRCPConfiguration.get(BugReport.BUGREPORT_USER, "");
        		String savedPassword = OdysseusRCPConfiguration.get(BugReport.BUGREPORT_PASSWORD, "");
        		
        		UsernameAndPasswordDialog dlg = new UsernameAndPasswordDialog(getParentShell(), savedUsername, savedPassword);
        		if( dlg.open() == Dialog.OK ) {
        			OdysseusRCPConfiguration.set(BugReport.BUGREPORT_USER, dlg.getUsername());
        			OdysseusRCPConfiguration.set(BugReport.BUGREPORT_PASSWORD, dlg.getPassword());
        			OdysseusRCPConfiguration.save();
        		}
        	}
        });

        final Label errorLabel = createEmptyLabel(parent);

        Button cancelButton = createButton(parent, OdysseusNLS.Cancel);
        cancelButton.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(final SelectionEvent event) {
                setReturnCode(Window.CANCEL);
                close();
            }
        });

        Button sendButton = createButton(parent, OdysseusNLS.SendBugReport);
        sendButton.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(final SelectionEvent event) {
            	Button button = (Button)event.getSource();
				button.setText(OdysseusNLS.Sending);
                try {
                    boolean result = BugReport.send(BugReportEditor.this.userTextArea.getText(), BugReportEditor.this.generatedTextArea.getText());
                    setReturnCode( result ? Window.OK : Window.CANCEL);
                    close();
                } catch (IOException e) {
                    errorLabel.setText(e.getMessage());
                    LOG.error(e.getMessage(), e);
                    button.setText(OdysseusNLS.SendBugReport);
                } 
            }
        });
        
        Shell shell = parent.getShell();
        if (shell != null) {
            shell.setDefaultButton(sendButton);
        }
    }

	private static Label createEmptyLabel(final Composite parent) {
		Label label = new Label(parent, SWT.NONE);
        label.setText("");
        GridData errorLabelData = new GridData(GridData.HORIZONTAL_ALIGN_FILL);
        errorLabelData.horizontalSpan = 2;
        label.setLayoutData(errorLabelData);
		return label;
	}

	private static Button createButton(Composite parent, String title) {
		Button button = new Button(parent, SWT.PUSH);
        button.setText(title);
        button.setFont(JFaceResources.getDialogFont());
        GridData data = new GridData(GridData.HORIZONTAL_ALIGN_FILL);
        Point minSize = button.computeSize(SWT.DEFAULT, SWT.DEFAULT, true);
        data.widthHint = minSize.x;
        button.setLayoutData(data);
		return button;
	}

    private Control createDialogArea(final Composite parent) {
        Composite composite = new Composite(parent, SWT.NONE);
        
        GridLayout layout = new GridLayout();
        composite.setLayout(layout);
        composite.setLayoutData(new GridData(GridData.FILL_BOTH));
        
        createTextArea(parent);
        
        return composite;
    }

    private void createTextArea(final Composite parent) {
        createStyledLabel(parent, "Reporter, please consider answering these questions, where appropriate");

        userTextArea = createStyledText(parent);
        userTextArea.addLineStyleListener(new SegmentLineStyleListener());

        createStyledLabel(parent, "\nDebug Information:");

        generatedTextArea = createStyledText(parent);
    }
    
    private static StyledText createStyledText( Composite parent ) {
        StyledText styledText = new StyledText(parent, SWT.BORDER | SWT.V_SCROLL | SWT.MULTI);
        styledText.setEditable(true);
        
        GridData data = new GridData(GridData.HORIZONTAL_ALIGN_FILL);
        data.widthHint = 600;
        data.heightHint = 300;
        styledText.setLayoutData(data);
        
        return styledText;
    }

	private static Label createStyledLabel(Composite parent, String text) {
		Label label = new Label(parent, SWT.NONE);
        label.setText(text);

        GridData data = new GridData(GridData.HORIZONTAL_ALIGN_FILL);
        data.widthHint = 600;
        label.setLayoutData(data);
        
		return label;
	}

    public final void setGeneratedReport(final String report) {
        generatedTextArea.setText(report);
    }

    public final void setUserReport(final String report) {
        userTextArea.setText(report);
    }
}
