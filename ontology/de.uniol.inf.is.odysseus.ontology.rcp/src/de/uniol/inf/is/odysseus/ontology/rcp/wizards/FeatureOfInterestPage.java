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

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.window.Window;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
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

import de.uniol.inf.is.odysseus.ontology.model.Property;
import de.uniol.inf.is.odysseus.ontology.ontology.vocabulary.ODYSSEUS;
import de.uniol.inf.is.odysseus.ontology.rcp.dialogs.PropertyDialog;
import de.uniol.inf.is.odysseus.ontology.rcp.l10n.OdysseusNLS;

/**
 * @author Christian Kuka <christian@kuka.cc>
 * 
 */
public class FeatureOfInterestPage extends WizardPage {
    private Text txtName;
    private Text txtURI;
    private Table tblProperties;

    public FeatureOfInterestPage(final String pageName, final IStructuredSelection selection) {
        super(pageName);

        this.setTitle("Define the feature of interest");
        this.setDescription("Set the feature of interest name and the properties.");

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
        this.txtName.addModifyListener(new ModifyListener() {

			@Override
			public void modifyText(ModifyEvent e) {
				int fragmentPos = txtURI.getText().indexOf("#");
				if (fragmentPos > 0) {
					txtURI.setText(txtURI.getText().substring(0,
							fragmentPos + 1)
							+ txtName.getText());
				} else {
					txtURI.setText(ODYSSEUS.NS + txtName.getText());
				}
			}

		});
        
        final Label lblURI = new Label(container, SWT.NONE);
        lblURI.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, false, false, 2, 1));
        lblURI.setText(OdysseusNLS.URI + ":");

        this.txtURI = new Text(container, SWT.BORDER);
        this.txtURI.setText(ODYSSEUS.NS);
        final GridData gd_txtURI = new GridData(SWT.FILL, SWT.CENTER, false, false, 2, 1);
        gd_txtURI.widthHint = 328;
        this.txtURI.setLayoutData(gd_txtURI);

        final Label lblAttributes = new Label(container, SWT.NONE);
        lblAttributes.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, false, false, 2, 1));
        lblAttributes.setText(OdysseusNLS.Properties + ":");

        this.tblProperties = new Table(container, SWT.BORDER | SWT.MULTI);

        final GridData gd_propertyTableViewer = new GridData(SWT.FILL, SWT.FILL, false, true, 1, 1);
        gd_propertyTableViewer.widthHint = 328;
        this.tblProperties.setLayoutData(gd_propertyTableViewer);

        final Composite btnComposite = new Composite(container, SWT.NULL);
        btnComposite.setLayout(new GridLayout(1, false));
        final Button btnAdd = new Button(btnComposite, SWT.NONE);
        final GridData gd_btnAdd = new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1);
        btnAdd.setLayoutData(gd_btnAdd);
        btnAdd.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(final SelectionEvent e) {
                final PropertyDialog propertyDialog = new PropertyDialog(btnAdd.getShell());

                propertyDialog.open();
                if (FeatureOfInterestPage.this.tblProperties.isDisposed()) {
                    return;
                }
                if (propertyDialog.getReturnCode() == Window.OK) {
                    final TableItem item = new TableItem(FeatureOfInterestPage.this.tblProperties, SWT.NONE);
                    item.setText(0, propertyDialog.getProperty().getName());
                    item.setText(1, propertyDialog.getProperty().getUri().toString());
                    item.setData(propertyDialog.getProperty());
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
                final int index = FeatureOfInterestPage.this.tblProperties.getSelectionIndex();
                FeatureOfInterestPage.this.tblProperties.remove(index);
            }
        });
        btnRemove.setText(OdysseusNLS.Remove);
    }

    public String getFeatureOfInterestName() {
        return this.txtName.getText();
    }

    public URI getFeatureOfInterestURI() {
        return URI.create(this.txtURI.getText());
    }

    public List<Property> getProperties() {
        final List<Property> properties = new ArrayList<Property>(this.tblProperties.getItems().length);
        for (int i = 0; i < this.tblProperties.getItems().length; i++) {
            properties.add((Property) this.tblProperties.getItem(i).getData());
        }
        return properties;
    }

}
