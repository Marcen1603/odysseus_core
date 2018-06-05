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
package cc.kuka.odysseus.ontology.rcp.dialogs;

import java.net.URI;
import java.util.HashSet;
import java.util.Set;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Monitor;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

import cc.kuka.odysseus.ontology.common.ODYSSEUS;
import cc.kuka.odysseus.ontology.common.SensorOntologyService;
import cc.kuka.odysseus.ontology.common.model.Property;
import cc.kuka.odysseus.ontology.common.model.condition.Condition;
import cc.kuka.odysseus.ontology.rcp.SensorRegistryPlugIn;
import cc.kuka.odysseus.ontology.rcp.l10n.OdysseusNLS;

/**
 * @author Christian Kuka <christian@kuka.cc>
 * @version $Id$
 *
 */
public class ConditionDialog extends Dialog {
    Text txtName;
    Text txtURI;
    private Text txtExpression;
    private Combo cmbProperty;
    private Condition condition;

    /**
     *
     * Class constructor.
     *
     * @param parent
     */
    public ConditionDialog(final Shell parent) {
        super(parent);
    }

    @Override
    protected Control createDialogArea(final Composite parent) {
        final Composite container = (Composite) super.createDialogArea(parent);

        final Monitor monitor = parent.getMonitor();
        final int maxWidth = (monitor.getBounds().width * 2) / 3;

        final Label lblName = new Label(container, SWT.WRAP);
        lblName.setText(OdysseusNLS.Name);
        GridData gd = new GridData();
        int attributeWidth = lblName.computeSize(SWT.DEFAULT, SWT.DEFAULT).x;
        gd.widthHint = Math.min(attributeWidth, maxWidth);
        gd.horizontalAlignment = GridData.FILL;
        gd.grabExcessHorizontalSpace = true;
        lblName.setLayoutData(gd);

        this.txtName = new Text(container, SWT.BORDER);
        gd = new GridData();
        attributeWidth = this.txtName.computeSize(SWT.DEFAULT, SWT.DEFAULT).x;
        if (attributeWidth > maxWidth) {
            gd.widthHint = maxWidth;
        }
        gd.horizontalAlignment = GridData.FILL;
        gd.grabExcessHorizontalSpace = true;
        this.txtName.setLayoutData(gd);
        this.txtName.addModifyListener(new ModifyListener() {

            @Override
            public void modifyText(final ModifyEvent e) {
                final int fragmentPos = ConditionDialog.this.txtURI.getText().indexOf("#");
                if (fragmentPos > 0) {
                    ConditionDialog.this.txtURI.setText(ConditionDialog.this.txtURI.getText().substring(0, fragmentPos + 1) + ConditionDialog.this.txtName.getText());
                }
                else {
                    ConditionDialog.this.txtURI.setText(ODYSSEUS.NS + ConditionDialog.this.txtName.getText());
                }
            }

        });

        final Label lblURI = new Label(container, SWT.WRAP);
        lblURI.setText(OdysseusNLS.URI);
        gd = new GridData();
        int uriWidth = lblURI.computeSize(SWT.DEFAULT, SWT.DEFAULT).x;
        gd.widthHint = Math.min(uriWidth, maxWidth);
        gd.horizontalAlignment = GridData.FILL;
        gd.grabExcessHorizontalSpace = true;
        lblURI.setLayoutData(gd);

        this.txtURI = new Text(container, SWT.BORDER);
        this.txtURI.setText(ODYSSEUS.NS);
        gd = new GridData();
        uriWidth = this.txtURI.computeSize(SWT.DEFAULT, SWT.DEFAULT).x;
        if (uriWidth > maxWidth) {
            gd.widthHint = maxWidth;
        }
        gd.horizontalAlignment = GridData.FILL;
        gd.grabExcessHorizontalSpace = true;
        this.txtURI.setLayoutData(gd);

        final Label lblProperty = new Label(container, SWT.WRAP);
        lblProperty.setText(OdysseusNLS.Property);
        gd = new GridData();
        final int lblPropertyWidth = lblProperty.computeSize(SWT.DEFAULT, SWT.DEFAULT).x;
        gd.horizontalSpan = 2;
        gd.widthHint = Math.min(lblPropertyWidth, maxWidth);
        gd.horizontalAlignment = GridData.FILL;
        gd.grabExcessHorizontalSpace = true;
        lblProperty.setLayoutData(gd);

        this.cmbProperty = new Combo(container, SWT.VERTICAL | SWT.DROP_DOWN | SWT.BORDER | SWT.READ_ONLY);
        this.cmbProperty.select(0);
        gd = new GridData();
        final int propertyWidth = this.cmbProperty.computeSize(SWT.DEFAULT, SWT.DEFAULT).x;
        if (propertyWidth > maxWidth) {
            gd.widthHint = maxWidth;
        }
        gd.horizontalAlignment = GridData.FILL;
        gd.grabExcessHorizontalSpace = true;
        this.cmbProperty.setLayoutData(gd);

        fillPropertyCombo(this.cmbProperty);

        this.txtExpression = new Text(container, SWT.BORDER);
        gd = new GridData();
        final int txtExpressionMinWidth = this.txtExpression.computeSize(SWT.DEFAULT, SWT.DEFAULT).x;
        gd.horizontalSpan = 2;
        gd.widthHint = Math.min(txtExpressionMinWidth, maxWidth);
        gd.horizontalAlignment = GridData.FILL;
        gd.grabExcessHorizontalSpace = true;
        this.txtExpression.setLayoutData(gd);

        return container;
    }

    @Override
    protected void configureShell(final Shell newShell) {
        super.configureShell(newShell);
        newShell.setText("Selection dialog");
    }

    private void saveInput() {
        final String propertyItem = this.cmbProperty.getItem(this.cmbProperty.getSelectionIndex());
        final String name = this.txtName.getText();
        final URI uri = URI.create(this.txtURI.getText());
        Property property = (Property) this.cmbProperty.getData(propertyItem);
        final String expression = this.txtExpression.getText();

        this.condition = new Condition(uri, name, property, expression);
    }

    @Override
    protected void okPressed() {
        this.saveInput();
        super.okPressed();
    }

    public Condition getCondition() {
        return this.condition;
    }

    private static void fillPropertyCombo(final Combo combo) {
        final SensorOntologyService ontology = SensorRegistryPlugIn.getSensorOntologyService();
        combo.removeAll();
        final Set<Property> properties = new HashSet<>(ontology.allProperties());

        for (final Property property : properties) {
            combo.add(property.name());
            combo.setData(property.name(), property);
        }
        combo.select(0);
    }
}
