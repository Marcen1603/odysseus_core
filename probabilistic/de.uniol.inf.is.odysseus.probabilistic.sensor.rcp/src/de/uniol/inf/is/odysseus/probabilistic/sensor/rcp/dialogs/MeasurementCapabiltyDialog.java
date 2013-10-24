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
package de.uniol.inf.is.odysseus.probabilistic.sensor.rcp.dialogs;

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
import de.uniol.inf.is.odysseus.probabilistic.math.Interval;
import de.uniol.inf.is.odysseus.probabilistic.sensor.SensorOntologyService;
import de.uniol.inf.is.odysseus.probabilistic.sensor.model.Condition;
import de.uniol.inf.is.odysseus.probabilistic.sensor.model.MeasurementProperty;
import de.uniol.inf.is.odysseus.probabilistic.sensor.rcp.SensorRegistryPlugIn;
import de.uniol.inf.is.odysseus.probabilistic.sensor.rcp.l10n.OdysseusNLS;

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
    private List<SDFAttribute> attributes;

    /**
     * Class constructor.
     * 
     * @param parentShell
     */
    public MeasurementCapabiltyDialog(Shell parent, List<SDFAttribute> attributes) {
        super(parent);
        this.attributes = attributes;
    }

    @Override
    protected Control createDialogArea(Composite parent) {
        final Composite container = (Composite) super.createDialogArea(parent);

        Monitor monitor = parent.getMonitor();
        int maxWidth = monitor.getBounds().width * 2 / 3;

        Label lblName = new Label(container, SWT.WRAP);
        lblName.setText(OdysseusNLS.Name);
        GridData gd = new GridData();
        int lblNameWidth = lblName.computeSize(SWT.DEFAULT, SWT.DEFAULT).x;
        gd.widthHint = Math.min(lblNameWidth, maxWidth);
        gd.horizontalAlignment = GridData.FILL;
        gd.grabExcessHorizontalSpace = true;
        lblName.setLayoutData(gd);

        txtName = new Text(container, SWT.BORDER);
        gd = new GridData();
        lblNameWidth = txtName.computeSize(SWT.DEFAULT, SWT.DEFAULT).x;
        if (lblNameWidth > maxWidth)
            gd.widthHint = maxWidth;
        gd.horizontalAlignment = GridData.FILL;
        gd.grabExcessHorizontalSpace = true;
        txtName.setLayoutData(gd);

        Label lblCondition = new Label(container, SWT.WRAP);
        lblCondition.setText(OdysseusNLS.Condition);
        gd = new GridData();
        int lblConditionWidth = lblCondition.computeSize(SWT.DEFAULT, SWT.DEFAULT).x;
        gd.widthHint = Math.min(lblConditionWidth, maxWidth);
        gd.horizontalAlignment = GridData.FILL;
        gd.grabExcessHorizontalSpace = true;
        lblCondition.setLayoutData(gd);

        cmbAttribute = new Combo(container, SWT.BORDER);
        fillAttributeCombo(cmbAttribute);
        txtAttributeValueMin = new Text(container, SWT.BORDER);
        txtAttributeValueMax = new Text(container, SWT.BORDER);

        Label lblProperty = new Label(container, SWT.WRAP);
        lblProperty.setText(OdysseusNLS.MeasurementProperty);
        gd = new GridData();
        int lblPropertyWidth = lblProperty.computeSize(SWT.DEFAULT, SWT.DEFAULT).x;
        gd.widthHint = Math.min(lblPropertyWidth, maxWidth);
        gd.horizontalAlignment = GridData.FILL;
        gd.grabExcessHorizontalSpace = true;
        lblProperty.setLayoutData(gd);

        cmbProperty = new Combo(container, SWT.BORDER);
        fillPropertyCombo(cmbProperty);
        txtPropertyValueMin = new Text(container, SWT.BORDER);
        txtPropertyValueMax = new Text(container, SWT.BORDER);

        return container;
    }

    @Override
    protected void configureShell(Shell newShell) {
        super.configureShell(newShell);
        newShell.setText("Selection dialog");
    }

    private void saveInput() {
        String name = txtName.getText();
        SDFAttribute attribute = (SDFAttribute) cmbAttribute.getData(cmbAttribute.getItem(cmbAttribute.getSelectionIndex()));
        double attributeMinValue = Double.parseDouble(txtAttributeValueMin.getText());
        double attributeMaxValue = Double.parseDouble(txtAttributeValueMax.getText());
        Interval attributeInterval = new Interval(attributeMinValue, attributeMaxValue);

        condition = new Condition(name, attribute, attributeInterval);

        MeasurementProperty.Property property = (MeasurementProperty.Property) cmbProperty.getData(cmbProperty.getItem(cmbProperty.getSelectionIndex()));
        double propertyMinValue = Double.parseDouble(txtPropertyValueMin.getText());
        double propertyMaxValue = Double.parseDouble(txtPropertyValueMax.getText());

        Interval propertyInterval = new Interval(propertyMinValue, propertyMaxValue);

        measurementPropery = new MeasurementProperty(property, propertyInterval);

    }

    public Condition getCondition() {
        return this.condition;
    }

    public MeasurementProperty getMeasurementProperty() {
        return this.measurementPropery;
    }

    @Override
    protected void okPressed() {
        saveInput();
        super.okPressed();
    }

    private void fillAttributeCombo(Combo combo) {
        SensorOntologyService ontology = SensorRegistryPlugIn.getSensorOntologyService();
        combo.removeAll();
        Set<SDFAttribute> attributes = new HashSet<SDFAttribute>(ontology.getAllProperties());
        attributes.addAll(this.attributes);
        for (SDFAttribute attribute : attributes) {
            combo.add(attribute.getAttributeName());
            combo.setData(attribute.getAttributeName(), attribute);
        }
        combo.select(0);
    }

    private void fillPropertyCombo(Combo combo) {
        combo.removeAll();
        combo.add(MeasurementProperty.Property.Accurancy.toString());
        combo.setData(MeasurementProperty.Property.Accurancy.toString(), MeasurementProperty.Property.Accurancy);
        combo.select(0);
    }
}
