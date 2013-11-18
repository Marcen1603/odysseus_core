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
import de.uniol.inf.is.odysseus.ontology.model.Property;
import de.uniol.inf.is.odysseus.ontology.model.SSNMeasurementProperty;
import de.uniol.inf.is.odysseus.ontology.model.condition.Condition;
import de.uniol.inf.is.odysseus.ontology.model.condition.ExpressionCondition;
import de.uniol.inf.is.odysseus.ontology.model.condition.IntervalCondition;
import de.uniol.inf.is.odysseus.ontology.model.property.MeasurementProperty;
import de.uniol.inf.is.odysseus.ontology.rcp.SensorRegistryPlugIn;
import de.uniol.inf.is.odysseus.ontology.rcp.l10n.OdysseusNLS;
import de.uniol.inf.is.odysseus.probabilistic.math.Interval;

/**
 * @author Christian Kuka <christian@kuka.cc>
 * 
 */
public class MeasurementCapabiltyDialog extends Dialog {
    private Combo cmbProperty;
    private Combo cmbAttribute;
    private Text txtAttributeValueMin;
    private Text txtAttributeValueMax;
    private Text txtAttributeFunction;

    private Combo cmbMeasurementProperty;
    private Text txtMeasurementPropertyValueMin;
    private Text txtMeasurementPropertyValueMax;

    private Condition condition;
    private MeasurementProperty measurementPropery;
    private Property property;
    private final List<SDFAttribute> attributes;
    private final URI uri;

    /**
     * Class constructor.
     * 
     * @param parentShell
     */
    public MeasurementCapabiltyDialog(final Shell parent, URI uri, final List<SDFAttribute> attributes) {
        super(parent);
        this.attributes = attributes;
        this.uri = uri;
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

        this.cmbProperty = new Combo(container, SWT.VERTICAL | SWT.DROP_DOWN | SWT.BORDER | SWT.READ_ONLY);
        gd = new GridData();
        final int cmbPropertyWidth = cmbProperty.computeSize(SWT.DEFAULT, SWT.DEFAULT).x;
        gd.horizontalSpan = 2;
        gd.widthHint = Math.min(cmbPropertyWidth, maxWidth);
        gd.horizontalAlignment = GridData.FILL;
        gd.grabExcessHorizontalSpace = true;
        cmbProperty.setLayoutData(gd);

        this.fillPropertyCombo(this.cmbProperty);

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
                txtAttributeFunction.setEnabled(false);
                txtAttributeFunction.setVisible(false);
                txtAttributeValueMin.setEnabled(true);
                txtAttributeValueMax.setEnabled(true);
                txtAttributeValueMin.setVisible(true);
                txtAttributeValueMax.setVisible(true);

            }
        });
        radios[1] = new Button(container, SWT.RADIO);
        radios[1].setText(OdysseusNLS.Function);
        radios[1].addListener(SWT.Selection, new Listener() {

            @Override
            public void handleEvent(Event event) {
                txtAttributeValueMin.setText("");
                txtAttributeValueMax.setText("");
                txtAttributeValueMin.setEnabled(false);
                txtAttributeValueMax.setEnabled(false);
                txtAttributeValueMin.setVisible(false);
                txtAttributeValueMax.setVisible(false);
                txtAttributeFunction.setEditable(true);
                txtAttributeFunction.setEnabled(true);
                txtAttributeFunction.setVisible(true);

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
        txtAttributeFunction.setVisible(false);
        txtAttributeFunction.setEnabled(false);

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

        this.cmbMeasurementProperty = new Combo(container, SWT.VERTICAL | SWT.DROP_DOWN | SWT.BORDER | SWT.READ_ONLY);
        gd = new GridData();
        final int cmbMeasurementPropertyWidth = cmbMeasurementProperty.computeSize(SWT.DEFAULT, SWT.DEFAULT).x;
        gd.horizontalSpan = 2;
        gd.widthHint = Math.min(cmbMeasurementPropertyWidth, maxWidth);
        gd.horizontalAlignment = GridData.FILL;
        gd.grabExcessHorizontalSpace = true;
        cmbMeasurementProperty.setLayoutData(gd);

        this.fillMeasurementPropertyCombo(this.cmbMeasurementProperty);

        this.txtMeasurementPropertyValueMin = new Text(container, SWT.BORDER);
        gd = new GridData();
        final int txtMeasurementPropertyValueMinWidth = txtMeasurementPropertyValueMin.computeSize(SWT.DEFAULT, SWT.DEFAULT).x;
        gd.horizontalSpan = 1;
        gd.widthHint = Math.min(txtMeasurementPropertyValueMinWidth, maxWidth / 2);
        gd.horizontalAlignment = GridData.FILL;
        gd.grabExcessHorizontalSpace = true;
        txtMeasurementPropertyValueMin.setLayoutData(gd);

        this.txtMeasurementPropertyValueMax = new Text(container, SWT.BORDER);
        gd = new GridData();
        final int txtMeasurementPropertyValueMaxWidth = txtMeasurementPropertyValueMax.computeSize(SWT.DEFAULT, SWT.DEFAULT).x;
        gd.horizontalSpan = 1;
        gd.widthHint = Math.min(txtMeasurementPropertyValueMaxWidth, maxWidth / 2);
        gd.horizontalAlignment = GridData.FILL;
        gd.grabExcessHorizontalSpace = true;
        txtMeasurementPropertyValueMax.setLayoutData(gd);

        radios[0].setSelection(true);

        return container;
    }

    @Override
    protected void configureShell(final Shell newShell) {
        super.configureShell(newShell);
        newShell.setText("Selection dialog");
    }

    @SuppressWarnings("unused")
    private void saveInput() {
        final Property property = (Property) this.cmbProperty.getData(this.cmbProperty.getItem(this.cmbProperty.getSelectionIndex()));
        final SDFAttribute attribute = (SDFAttribute) this.cmbAttribute.getData(this.cmbAttribute.getItem(this.cmbAttribute.getSelectionIndex()));

        if (this.txtAttributeFunction.isEnabled()) {
            final String attributeFunction = this.txtAttributeFunction.getText();
            this.condition = new ExpressionCondition(URI.create(this.uri + "/" + attribute.getAttributeName()), this.property, attributeFunction);
        }
        else {
            final double attributeMinValue = Double.parseDouble(this.txtAttributeValueMin.getText());
            final double attributeMaxValue = Double.parseDouble(this.txtAttributeValueMax.getText());
            final Interval attributeInterval = new Interval(attributeMinValue, attributeMaxValue);
            this.condition = new IntervalCondition(URI.create(this.uri + "/" + attribute.getAttributeName()), this.property, attributeInterval);
        }
        final SSNMeasurementProperty measurementProperty = (SSNMeasurementProperty) this.cmbMeasurementProperty.getData(this.cmbMeasurementProperty.getItem(this.cmbMeasurementProperty
                .getSelectionIndex()));
        final double measurementPropertyMinValue = Double.parseDouble(this.txtMeasurementPropertyValueMin.getText());
        final double measurementPropertyMaxValue = Double.parseDouble(this.txtMeasurementPropertyValueMax.getText());

        final Interval measurementPropertyInterval = new Interval(measurementPropertyMinValue, measurementPropertyMaxValue);

        this.measurementPropery = new MeasurementProperty(URI.create(this.uri + "/" + attribute.getAttributeName() + "/" + measurementProperty.name()), measurementProperty.getResource());
    }

    public Condition getCondition() {
        return this.condition;
    }

    public MeasurementProperty getMeasurementProperty() {
        return this.measurementPropery;
    }

    /**
     * @return
     */
    public Property getProperty() {
        return this.property;
    }

    @Override
    protected void okPressed() {
        this.saveInput();
        super.okPressed();
    }

    private void fillAttributeCombo(final Combo combo) {
        combo.removeAll();
        for (final SDFAttribute attribute : this.attributes) {
            combo.add(attribute.getAttributeName());
            combo.setData(attribute.getAttributeName(), attribute);
        }
        combo.select(0);
    }

    private void fillMeasurementPropertyCombo(final Combo combo) {
        combo.removeAll();
        for (SSNMeasurementProperty property : SSNMeasurementProperty.values()) {
            combo.add(property.toString());
            combo.setData(property.toString(), property);
        }
        combo.select(0);
    }

    private void fillPropertyCombo(final Combo combo) {
        final SensorOntologyService ontology = SensorRegistryPlugIn.getSensorOntologyService();
        combo.removeAll();
        final Set<Property> properties = new HashSet<Property>(ontology.getAllProperties());

        for (Property property : properties) {
            combo.add(property.getName());
            combo.setData(property.getName(), property);
        }
        combo.select(0);
    }

}
