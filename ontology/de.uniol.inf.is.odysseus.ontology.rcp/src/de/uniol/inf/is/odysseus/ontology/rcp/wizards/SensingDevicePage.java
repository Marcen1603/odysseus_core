/**
 * Copyright 2013 The Odysseus Team
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
package de.uniol.inf.is.odysseus.ontology.rcp.wizards;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.window.Window;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Text;

import de.uniol.inf.is.odysseus.core.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.ontology.rcp.dialogs.AttributeDialog;
import de.uniol.inf.is.odysseus.ontology.rcp.l10n.OdysseusNLS;

/**
 * @author Christian Kuka <christian@kuka.cc>
 * 
 */
public class SensingDevicePage extends WizardPage {
    private Text txtName;
    private Table tblAttributes;

    public SensingDevicePage(final String pageName, final IStructuredSelection selection) {
        super(pageName);

        this.setTitle("Define the sensing device");
        this.setDescription("Set the sensing device name and observed properties.");

    }

    @Override
    public void createControl(final Composite parent) {
        this.initializeDialogUnits(parent);

        final Composite container = new Composite(parent, SWT.NULL);
        container.setFont(parent.getFont());

        this.setControl(container);
        container.setLayout(new GridLayout(2, false));

        final Label lblName = new Label(container, SWT.NONE);
        lblName.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, false, false, 2, 1));
        lblName.setText(OdysseusNLS.Name + ":");

        this.txtName = new Text(container, SWT.BORDER);
        this.txtName.setText(OdysseusNLS.Name);
        final GridData gd_txtName = new GridData(SWT.FILL, SWT.CENTER, false, false, 2, 1);
        gd_txtName.widthHint = 328;
        this.txtName.setLayoutData(gd_txtName);

        final Label lblAttributes = new Label(container, SWT.NONE);
        lblAttributes.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, false, false, 2, 1));
        lblAttributes.setText(OdysseusNLS.Attributes + ":");

        this.tblAttributes = new Table(container, SWT.BORDER | SWT.MULTI);

        final GridData gd_propertyTableViewer = new GridData(SWT.FILL, SWT.FILL, false, true, 1, 1);
        gd_propertyTableViewer.widthHint = 328;
        this.tblAttributes.setLayoutData(gd_propertyTableViewer);

        final Composite btnComposite = new Composite(container, SWT.NULL);
        btnComposite.setLayout(new GridLayout(1, false));
        final Button btnAdd = new Button(btnComposite, SWT.NONE);
        final GridData gd_btnAdd = new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1);
        btnAdd.setLayoutData(gd_btnAdd);
        btnAdd.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(final SelectionEvent e) {
                final AttributeDialog attributeDialog = new AttributeDialog(btnAdd.getShell());

                attributeDialog.open();
                if (SensingDevicePage.this.tblAttributes.isDisposed()) {
                    return;
                }
                if (attributeDialog.getReturnCode() == Window.OK) {
                    final TableItem item = new TableItem(SensingDevicePage.this.tblAttributes, SWT.NONE);
                    item.setText(0, attributeDialog.getAttribute().getAttributeName());
                    item.setText(1, attributeDialog.getAttribute().getDatatype().getQualName());
                    item.setData(attributeDialog.getAttribute());
                }
            }
        });
        btnAdd.setText(OdysseusNLS.Add);
        final Button btnRemove = new Button(btnComposite, SWT.NONE);
        final GridData gd_btnRemove = new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1);
        btnRemove.setLayoutData(gd_btnRemove);
        btnRemove.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(final SelectionEvent e) {
                final int index = SensingDevicePage.this.tblAttributes.getSelectionIndex();
                SensingDevicePage.this.tblAttributes.remove(index);
            }
        });
        btnRemove.setText(OdysseusNLS.Remove);
    }

    public String getSensingDeviceName() {
        return this.txtName.getText();
    }

    public List<SDFAttribute> getAttributes() {
        final List<SDFAttribute> attributes = new ArrayList<SDFAttribute>(this.tblAttributes.getItemCount());
        for (int i = 0; i < this.tblAttributes.getItemCount(); i++) {
            attributes.add((SDFAttribute) this.tblAttributes.getItem(i).getData());
        }
        return attributes;
    }

}
