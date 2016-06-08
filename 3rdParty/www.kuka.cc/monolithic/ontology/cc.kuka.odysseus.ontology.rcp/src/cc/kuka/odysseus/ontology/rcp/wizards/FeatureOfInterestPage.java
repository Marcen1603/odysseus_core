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
package cc.kuka.odysseus.ontology.rcp.wizards;

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
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Text;

import cc.kuka.odysseus.ontology.common.ODYSSEUS;
import cc.kuka.odysseus.ontology.common.model.Property;
import cc.kuka.odysseus.ontology.rcp.dialogs.PropertyDialog;
import cc.kuka.odysseus.ontology.rcp.l10n.OdysseusNLS;

/**
 * @author Christian Kuka <christian@kuka.cc>
 * @version $Id$
 *
 */
public class FeatureOfInterestPage extends WizardPage {
    Text txtName;
    Text txtURI;
    Table tblProperties;

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
            public void modifyText(final ModifyEvent e) {
                final int fragmentPos = FeatureOfInterestPage.this.txtURI.getText().indexOf("#");
                if (fragmentPos > 0) {
                    FeatureOfInterestPage.this.txtURI.setText(FeatureOfInterestPage.this.txtURI.getText().substring(0, fragmentPos + 1) + FeatureOfInterestPage.this.txtName.getText());
                }
                else {
                    FeatureOfInterestPage.this.txtURI.setText(ODYSSEUS.NS + FeatureOfInterestPage.this.txtName.getText());
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
        this.tblProperties.setLinesVisible(true);
        this.tblProperties.setHeaderVisible(true);
        final String[] titles = { OdysseusNLS.Property };
        for (final String title2 : titles) {
            final TableColumn column = new TableColumn(this.tblProperties, SWT.NONE);
            column.setText(title2);
        }
        for (int i = 0; i < titles.length; i++) {
            this.tblProperties.getColumn(i).pack();
        }

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
                    item.setText(0, propertyDialog.getProperty().name());
                    item.setText(1, propertyDialog.getProperty().uri().toString());
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
        final List<Property> properties = new ArrayList<>(this.tblProperties.getItems().length);
        for (int i = 0; i < this.tblProperties.getItems().length; i++) {
            properties.add((Property) this.tblProperties.getItem(i).getData());
        }
        return properties;
    }

}
