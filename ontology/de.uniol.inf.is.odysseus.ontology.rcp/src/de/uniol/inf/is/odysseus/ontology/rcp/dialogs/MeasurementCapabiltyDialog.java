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

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Monitor;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

import de.uniol.inf.is.odysseus.core.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.ontology.SensorOntologyService;
import de.uniol.inf.is.odysseus.ontology.model.SSNMeasurementProperty;
import de.uniol.inf.is.odysseus.ontology.model.condition.Condition;
import de.uniol.inf.is.odysseus.ontology.model.property.MeasurementProperty;
import de.uniol.inf.is.odysseus.ontology.rcp.SensorRegistryPlugIn;
import de.uniol.inf.is.odysseus.ontology.rcp.l10n.OdysseusNLS;
import de.uniol.inf.is.odysseus.probabilistic.math.Interval;

/**
 * @author Christian Kuka <christian@kuka.cc>
 * 
 */
public class MeasurementCapabiltyDialog extends Dialog {
    private Combo cmbAttribute;
    private Text txtAttributeValueMin;
    private Text txtAttributeValueMax;
    private Text txtAttributeFunction;

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

        container.setLayout(new GridLayout(2, false));
        GridData gd = new GridData();

        final Label lblCondition = new Label(container, SWT.WRAP);
        lblCondition.setText(OdysseusNLS.Condition);
        gd = new GridData();
        final int lblConditionWidth = lblCondition.computeSize(SWT.DEFAULT, SWT.DEFAULT).x;
        gd.horizontalSpan = 2;
        gd.widthHint = Math.min(lblConditionWidth, maxWidth);
        gd.horizontalAlignment = GridData.FILL;
        gd.grabExcessHorizontalSpace = true;
        lblCondition.setLayoutData(gd);

        this.cmbAttribute = new Combo(container, SWT.VERTICAL | SWT.DROP_DOWN | SWT.BORDER | SWT.READ_ONLY);
        gd = new GridData();
        final int cmbAttributeWidth = cmbAttribute.computeSize(SWT.DEFAULT, SWT.DEFAULT).x;
        gd.horizontalSpan = 2;
        gd.widthHint = Math.min(cmbAttributeWidth, maxWidth);
        gd.horizontalAlignment = GridData.FILL;
        gd.grabExcessHorizontalSpace = true;
        cmbAttribute.setLayoutData(gd);

        this.fillAttributeCombo(this.cmbAttribute);

        Button[] radios = new Button[2];

        radios[0] = new Button(container, SWT.RADIO);
        radios[0].setText(OdysseusNLS.Interval);
        radios[0].addListener(SWT.Selection, new Listener() {

            @Override
            public void handleEvent(Event event) {
                txtAttributeFunction.setText("");
                txtAttributeFunction.setEditable(false);
                txtAttributeFunction.setEnabled(false);
                txtAttributeValueMin.setEnabled(true);
                txtAttributeValueMax.setEnabled(true);
                txtAttributeValueMin.setEditable(true);
                txtAttributeValueMax.setEditable(true);

            }
        });
        radios[1] = new Button(container, SWT.RADIO);
        radios[1].setText(OdysseusNLS.Function);
        radios[1].addListener(SWT.Selection, new Listener() {

            @Override
            public void handleEvent(Event event) {
                txtAttributeValueMin.setText("");
                txtAttributeValueMax.setText("");
                txtAttributeValueMin.setEditable(false);
                txtAttributeValueMax.setEditable(false);
                txtAttributeValueMin.setEnabled(false);
                txtAttributeValueMax.setEnabled(false);
                txtAttributeFunction.setEditable(true);
                txtAttributeFunction.setEnabled(true);

            }
        });

        this.txtAttributeFunction = new Text(container, SWT.BORDER);
        gd = new GridData();
        final int txtAttributeFunctionWidth = txtAttributeFunction.computeSize(SWT.DEFAULT, SWT.DEFAULT).x;
        gd.horizontalSpan = 2;
        gd.widthHint = Math.min(txtAttributeFunctionWidth, maxWidth);
        gd.horizontalAlignment = GridData.FILL;
        gd.grabExcessHorizontalSpace = true;
        txtAttributeFunction.setLayoutData(gd);

        this.txtAttributeValueMin = new Text(container, SWT.BORDER);
        gd = new GridData();
        final int txtAttributeValueMinWidth = txtAttributeValueMin.computeSize(SWT.DEFAULT, SWT.DEFAULT).x;
        gd.horizontalSpan = 1;
        gd.widthHint = Math.min(txtAttributeValueMinWidth, maxWidth / 2);
        gd.horizontalAlignment = GridData.FILL;
        gd.grabExcessHorizontalSpace = true;
        txtAttributeValueMin.setLayoutData(gd);

        this.txtAttributeValueMax = new Text(container, SWT.BORDER);
        gd = new GridData();
        final int txtAttributeValueMaxWidth = txtAttributeValueMax.computeSize(SWT.DEFAULT, SWT.DEFAULT).x;
        gd.horizontalSpan = 1;
        gd.widthHint = Math.min(txtAttributeValueMaxWidth, maxWidth / 2);
        gd.horizontalAlignment = GridData.FILL;
        gd.grabExcessHorizontalSpace = true;
        txtAttributeValueMax.setLayoutData(gd);
        new Label(container, SWT.NONE);

        final Label lblProperty = new Label(container, SWT.WRAP);
        lblProperty.setText(OdysseusNLS.MeasurementProperty);
        gd = new GridData();
        final int lblPropertyWidth = lblProperty.computeSize(SWT.DEFAULT, SWT.DEFAULT).x;
        gd.horizontalSpan = 2;
        gd.widthHint = Math.min(lblPropertyWidth, maxWidth);
        gd.horizontalAlignment = GridData.FILL;
        gd.grabExcessHorizontalSpace = true;
        lblProperty.setLayoutData(gd);

        this.cmbProperty = new Combo(container, SWT.VERTICAL | SWT.DROP_DOWN | SWT.BORDER | SWT.READ_ONLY);
        gd = new GridData();
        final int cmbPropertyWidth = cmbProperty.computeSize(SWT.DEFAULT, SWT.DEFAULT).x;
        gd.horizontalSpan = 2;
        gd.widthHint = Math.min(cmbPropertyWidth, maxWidth);
        gd.horizontalAlignment = GridData.FILL;
        gd.grabExcessHorizontalSpace = true;
        cmbProperty.setLayoutData(gd);

        this.fillPropertyCombo(this.cmbProperty);

        this.txtPropertyValueMin = new Text(container, SWT.BORDER);
        gd = new GridData();
        final int txtPropertyValueMinWidth = txtPropertyValueMin.computeSize(SWT.DEFAULT, SWT.DEFAULT).x;
        gd.horizontalSpan = 1;
        gd.widthHint = Math.min(txtPropertyValueMinWidth, maxWidth / 2);
        gd.horizontalAlignment = GridData.FILL;
        gd.grabExcessHorizontalSpace = true;
        txtPropertyValueMin.setLayoutData(gd);

        this.txtPropertyValueMax = new Text(container, SWT.BORDER);
        gd = new GridData();
        final int txtPropertyValueMaxWidth = txtPropertyValueMax.computeSize(SWT.DEFAULT, SWT.DEFAULT).x;
        gd.horizontalSpan = 1;
        gd.widthHint = Math.min(txtPropertyValueMaxWidth, maxWidth / 2);
        gd.horizontalAlignment = GridData.FILL;
        gd.grabExcessHorizontalSpace = true;
        txtPropertyValueMax.setLayoutData(gd);

        radios[0].setSelection(true);
        txtAttributeFunction.setEditable(false);
        txtAttributeFunction.setEnabled(false);

        return container;
    }

    @Override
    protected void configureShell(final Shell newShell) {
        super.configureShell(newShell);
        newShell.setText("Selection dialog");
    }

    private void saveInput() {
        final SDFAttribute attribute = (SDFAttribute) this.cmbAttribute.getData(this.cmbAttribute.getItem(this.cmbAttribute.getSelectionIndex()));
        final String attributeFunction = this.txtAttributeFunction.getText();
        final double attributeMinValue = Double.parseDouble(this.txtAttributeValueMin.getText());
        final double attributeMaxValue = Double.parseDouble(this.txtAttributeValueMax.getText());
        final Interval attributeInterval = new Interval(attributeMinValue, attributeMaxValue);
        if (this.txtAttributeFunction.isEnabled()) {
            // this.condition = new ExpressionCondition(attribute,
            // attributeFunction);
        }
        else {
            // this.condition = new IntervalCondition(attribute,
            // attributeInterval);
        }
        final SSNMeasurementProperty property = (SSNMeasurementProperty) this.cmbProperty.getData(this.cmbProperty.getItem(this.cmbProperty.getSelectionIndex()));
        final double propertyMinValue = Double.parseDouble(this.txtPropertyValueMin.getText());
        final double propertyMaxValue = Double.parseDouble(this.txtPropertyValueMax.getText());

        final Interval propertyInterval = new Interval(propertyMinValue, propertyMaxValue);

        // this.measurementPropery = new MeasurementProperty(property,
        // propertyInterval);
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
        for (SSNMeasurementProperty property : SSNMeasurementProperty.values()) {
            combo.add(property.toString());
            combo.setData(property.toString(), property);
        }
        combo.select(0);
    }
}
