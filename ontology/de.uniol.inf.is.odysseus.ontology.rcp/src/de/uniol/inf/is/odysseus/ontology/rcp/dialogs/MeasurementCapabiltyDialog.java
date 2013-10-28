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
package de.uniol.inf.is.odysseus.ontology.rcp.dialogs;

import java.net.URI;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Monitor;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

import de.uniol.inf.is.odysseus.core.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.ontology.SensorOntologyService;
import de.uniol.inf.is.odysseus.ontology.model.Condition;
import de.uniol.inf.is.odysseus.ontology.model.MeasurementProperty;
import de.uniol.inf.is.odysseus.ontology.ontology.vocabulary.ODYSSEUS;
import de.uniol.inf.is.odysseus.ontology.rcp.SensorRegistryPlugIn;
import de.uniol.inf.is.odysseus.ontology.rcp.l10n.OdysseusNLS;
import de.uniol.inf.is.odysseus.probabilistic.math.Interval;

/**
 * @author Christian Kuka <christian@kuka.cc>
 * 
 */
public class MeasurementCapabiltyDialog extends Dialog {
    private Text txtName;
    private Combo cmbAttribute;
    private Text txtAttributeValueMin;
    private Text txtAttributeValueMax;

    private Combo cmbProperty;
    private Text txtPropertyValueMin;
    private Text txtPropertyValueMax;

    private Condition condition;
    private MeasurementProperty measurementPropery;
    private final List<SDFAttribute> attributes;

    /**
     * Class constructor.
     * 
     * @param parentShell
     */
    public MeasurementCapabiltyDialog(final Shell parent, final List<SDFAttribute> attributes) {
        super(parent);
        this.attributes = attributes;
    }

    @Override
    protected Control createDialogArea(final Composite parent) {
        final Composite container = (Composite) super.createDialogArea(parent);

        final Monitor monitor = parent.getMonitor();
        final int maxWidth = (monitor.getBounds().width * 2) / 3;

        final Label lblName = new Label(container, SWT.WRAP);
        lblName.setText(OdysseusNLS.Name);
        GridData gd = new GridData();
        int lblNameWidth = lblName.computeSize(SWT.DEFAULT, SWT.DEFAULT).x;
        gd.widthHint = Math.min(lblNameWidth, maxWidth);
        gd.horizontalAlignment = GridData.FILL;
        gd.grabExcessHorizontalSpace = true;
        lblName.setLayoutData(gd);

        this.txtName = new Text(container, SWT.BORDER);
        gd = new GridData();
        lblNameWidth = this.txtName.computeSize(SWT.DEFAULT, SWT.DEFAULT).x;
        if (lblNameWidth > maxWidth) {
            gd.widthHint = maxWidth;
        }
        gd.horizontalAlignment = GridData.FILL;
        gd.grabExcessHorizontalSpace = true;
        this.txtName.setLayoutData(gd);

        final Label lblCondition = new Label(container, SWT.WRAP);
        lblCondition.setText(OdysseusNLS.Condition);
        gd = new GridData();
        final int lblConditionWidth = lblCondition.computeSize(SWT.DEFAULT, SWT.DEFAULT).x;
        gd.widthHint = Math.min(lblConditionWidth, maxWidth);
        gd.horizontalAlignment = GridData.FILL;
        gd.grabExcessHorizontalSpace = true;
        lblCondition.setLayoutData(gd);

        this.cmbAttribute = new Combo(container, SWT.BORDER);
        this.fillAttributeCombo(this.cmbAttribute);
        this.txtAttributeValueMin = new Text(container, SWT.BORDER);
        this.txtAttributeValueMax = new Text(container, SWT.BORDER);

        final Label lblProperty = new Label(container, SWT.WRAP);
        lblProperty.setText(OdysseusNLS.MeasurementProperty);
        gd = new GridData();
        final int lblPropertyWidth = lblProperty.computeSize(SWT.DEFAULT, SWT.DEFAULT).x;
        gd.widthHint = Math.min(lblPropertyWidth, maxWidth);
        gd.horizontalAlignment = GridData.FILL;
        gd.grabExcessHorizontalSpace = true;
        lblProperty.setLayoutData(gd);

        this.cmbProperty = new Combo(container, SWT.BORDER);
        this.fillPropertyCombo(this.cmbProperty);
        this.txtPropertyValueMin = new Text(container, SWT.BORDER);
        this.txtPropertyValueMax = new Text(container, SWT.BORDER);

        return container;
    }

    @Override
    protected void configureShell(final Shell newShell) {
        super.configureShell(newShell);
        newShell.setText("Selection dialog");
    }

    private void saveInput() {
        final String name = this.txtName.getText();
        final SDFAttribute attribute = (SDFAttribute) this.cmbAttribute.getData(this.cmbAttribute.getItem(this.cmbAttribute.getSelectionIndex()));
        final double attributeMinValue = Double.parseDouble(this.txtAttributeValueMin.getText());
        final double attributeMaxValue = Double.parseDouble(this.txtAttributeValueMax.getText());
        final Interval attributeInterval = new Interval(attributeMinValue, attributeMaxValue);

        this.condition = new Condition(URI.create(ODYSSEUS.NS + name), attribute, attributeInterval);

        final MeasurementProperty.Property property = (MeasurementProperty.Property) this.cmbProperty.getData(this.cmbProperty.getItem(this.cmbProperty.getSelectionIndex()));
        final double propertyMinValue = Double.parseDouble(this.txtPropertyValueMin.getText());
        final double propertyMaxValue = Double.parseDouble(this.txtPropertyValueMax.getText());

        final Interval propertyInterval = new Interval(propertyMinValue, propertyMaxValue);

        this.measurementPropery = new MeasurementProperty(property, propertyInterval);

    }

    public Condition getCondition() {
        return this.condition;
    }

    public MeasurementProperty getMeasurementProperty() {
        return this.measurementPropery;
    }

    @Override
    protected void okPressed() {
        this.saveInput();
        super.okPressed();
    }

    private void fillAttributeCombo(final Combo combo) {
        final SensorOntologyService ontology = SensorRegistryPlugIn.getSensorOntologyService();
        combo.removeAll();
        final Set<SDFAttribute> attributes = new HashSet<SDFAttribute>(ontology.getAllProperties());
        attributes.addAll(this.attributes);
        for (final SDFAttribute attribute : attributes) {
            combo.add(attribute.getAttributeName());
            combo.setData(attribute.getAttributeName(), attribute);
        }
        combo.select(0);
    }

    private void fillPropertyCombo(final Combo combo) {
        combo.removeAll();
        combo.add(MeasurementProperty.Property.Accurancy.toString());
        combo.setData(MeasurementProperty.Property.Accurancy.toString(), MeasurementProperty.Property.Accurancy);
        combo.select(0);
    }
}
