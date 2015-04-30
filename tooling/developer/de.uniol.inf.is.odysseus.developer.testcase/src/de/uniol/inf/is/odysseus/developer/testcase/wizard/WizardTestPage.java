/*******************************************************************************
 * Copyright 2015 The Odysseus Team
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
package de.uniol.inf.is.odysseus.developer.testcase.wizard;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.DirectoryDialog;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.dialogs.WizardNewFileCreationPage;

import de.uniol.inf.is.odysseus.core.logicaloperator.LogicalOperatorInformation;
import de.uniol.inf.is.odysseus.core.usermanagement.ISession;
import de.uniol.inf.is.odysseus.developer.testcase.Activator;

/**
 * @author Christian Kuka <christian@kuka.cc>
 * @version $Id$
 *
 */
public class WizardTestPage extends WizardPage {
    List<LogicalOperatorInformation> operators = new ArrayList<>();
    LogicalOperatorInformation operator = null;
    String directory;
    String name;

    protected WizardTestPage(final String pageName) {
        super(pageName);
        this.setPageComplete(false);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void createControl(final Composite parent) {
        final Composite composite = new Composite(parent, SWT.NONE);
        composite.setLayoutData(new GridData((GridData.FILL_BOTH)));
        composite.setLayout(new GridLayout(1, true));
        {// Test case name
            final Label label = new Label(composite, SWT.NONE);
            label.setText("Name:");
            final Text text = new Text(composite, SWT.BORDER);
            text.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
            text.addModifyListener(new ModifyListener() {

                @Override
                public void modifyText(final ModifyEvent e) {
                    WizardTestPage.this.name = text.getText();
                }
            });
        }
        {// Set operator
            final Label label = new Label(composite, SWT.NONE);
            label.setText("Operator:");
            final Combo combo = new Combo(composite, SWT.BORDER | SWT.READ_ONLY);
            combo.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
            final ISession session = Activator.getSession();
            this.operators = Activator.getExecutor().getOperatorInformations(session);
            Collections.sort(this.operators, new Comparator<LogicalOperatorInformation>() {

                @Override
                public int compare(final LogicalOperatorInformation o1, final LogicalOperatorInformation o2) {
                    return o1.getOperatorName().compareTo(o2.getOperatorName());
                }
            });
            for (final LogicalOperatorInformation operator : this.operators) {
                combo.add(operator.getOperatorName());
            }
            combo.select(0);
            combo.addSelectionListener(new SelectionAdapter() {
                @Override
                public void widgetSelected(final SelectionEvent e) {
                    final int index = combo.getSelectionIndex();
                    WizardTestPage.this.operator = WizardTestPage.this.operators.get(index);
                }
            });
        }
        {// Set output path
            final Label label = new Label(composite, SWT.NONE);
            label.setText("Path:");
            final Text outputPath = new Text(composite, SWT.BORDER);
            outputPath.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));

            final Button choosePathButton = new Button(composite, SWT.PUSH);
            choosePathButton.setText("Choose from project");
            choosePathButton.addSelectionListener(new SelectionAdapter() {

                @Override
                public void widgetSelected(final SelectionEvent e) {
                    final DirectoryDialog dialog = new DirectoryDialog(parent.getShell());
                    dialog.setText("Odysseus Testcase");
                    dialog.setMessage("Select a directory");
                    String directory = dialog.open();
                    if (directory != null) {
                        outputPath.setText(directory);
                        WizardTestPage.this.directory = directory;
                        WizardTestPage.this.setPageComplete(true);
                    }
                }

            });
        }
        this.setControl(composite);

    }

    /**
     * @return
     */
    public LogicalOperatorInformation getOperator() {
        return this.operator;
    }

    /**
     * @return
     */
    public String getDirectory() {
        return this.directory;
    }

    public String getTestcaseName() {
        return this.name;
    }

    @Override
    public WizardNewFileCreationPage getPreviousPage() {
        return (WizardNewFileCreationPage) super.getPreviousPage();
    }

}
