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
import java.util.Set;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Monitor;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

import de.uniol.inf.is.odysseus.core.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.ontology.SensorOntologyService;
import de.uniol.inf.is.odysseus.ontology.model.Property;
import de.uniol.inf.is.odysseus.ontology.model.SSNMeasurementProperty;
import de.uniol.inf.is.odysseus.ontology.model.condition.Condition;
import de.uniol.inf.is.odysseus.ontology.model.condition.ExpressionCondition;
import de.uniol.inf.is.odysseus.ontology.model.property.MeasurementProperty;
import de.uniol.inf.is.odysseus.ontology.rcp.SensorRegistryPlugIn;
import de.uniol.inf.is.odysseus.ontology.rcp.l10n.OdysseusNLS;

/**
 * @author Christian Kuka <christian@kuka.cc>
 * 
 */
public class MeasurementCapabiltyDialog extends Dialog {
	private Combo cmbForProperty;
	private Combo cmbConditionProperty;

	// private Text txtConditionValueMin;
	// private Text txtConditionValueMax;
	private Text txtConditionExpression;

	private Combo cmbMeasurementProperty;
	private Text txtMeasurementPropertyExpression;

	private Condition condition;
	private MeasurementProperty measurementPropery;
	private Property property;
	private SDFAttribute attribute;
	private final URI uri;

	/**
	 * Class constructor.
	 * 
	 * @param parentShell
	 */
	public MeasurementCapabiltyDialog(final Shell parent, URI uri,
			final SDFAttribute attribute) {
		super(parent);
		this.attribute = attribute;
		this.uri = uri;
	}

	@Override
	protected Control createDialogArea(final Composite parent) {
		final Composite container = (Composite) super.createDialogArea(parent);

		final Monitor monitor = parent.getMonitor();
		final int maxWidth = (monitor.getBounds().width * 2) / 3;

		container.setLayout(new GridLayout(2, false));
		GridData gd = new GridData();

		final Label lblForCondition = new Label(container, SWT.WRAP);
		lblForCondition.setText(OdysseusNLS.Property);
		gd = new GridData();
		final int lblForConditionWidth = lblForCondition.computeSize(
				SWT.DEFAULT, SWT.DEFAULT).x;
		gd.horizontalSpan = 2;
		gd.widthHint = Math.min(lblForConditionWidth, maxWidth);
		gd.horizontalAlignment = GridData.FILL;
		gd.grabExcessHorizontalSpace = true;
		lblForCondition.setLayoutData(gd);

		this.cmbForProperty = new Combo(container, SWT.VERTICAL | SWT.DROP_DOWN
				| SWT.BORDER | SWT.READ_ONLY);
		gd = new GridData();
		final int cmbForPropertyWidth = cmbForProperty.computeSize(SWT.DEFAULT,
				SWT.DEFAULT).x;
		gd.horizontalSpan = 2;
		gd.widthHint = Math.min(cmbForPropertyWidth, maxWidth);
		gd.horizontalAlignment = GridData.FILL;
		gd.grabExcessHorizontalSpace = true;
		cmbForProperty.setLayoutData(gd);

		this.fillPropertyCombo(this.cmbForProperty);

		final Label lblCondition = new Label(container, SWT.WRAP);
		lblCondition.setText(OdysseusNLS.Condition);
		gd = new GridData();
		final int lblConditionWidth = lblCondition.computeSize(SWT.DEFAULT,
				SWT.DEFAULT).x;
		gd.horizontalSpan = 2;
		gd.widthHint = Math.min(lblConditionWidth, maxWidth);
		gd.horizontalAlignment = GridData.FILL;
		gd.grabExcessHorizontalSpace = true;
		lblCondition.setLayoutData(gd);

		this.cmbConditionProperty = new Combo(container, SWT.VERTICAL
				| SWT.DROP_DOWN | SWT.BORDER | SWT.READ_ONLY);
		gd = new GridData();
		final int cmbConditionPropertyWidth = cmbConditionProperty.computeSize(
				SWT.DEFAULT, SWT.DEFAULT).x;
		gd.horizontalSpan = 2;
		gd.widthHint = Math.min(cmbConditionPropertyWidth, maxWidth);
		gd.horizontalAlignment = GridData.FILL;
		gd.grabExcessHorizontalSpace = true;
		cmbConditionProperty.setLayoutData(gd);

		this.fillPropertyCombo(this.cmbConditionProperty);

		// Button[] radios = new Button[2];
		//
		// radios[0] = new Button(container, SWT.RADIO);
		// radios[0].setText(OdysseusNLS.Interval);
		// radios[0].addListener(SWT.Selection, new Listener() {
		//
		// @Override
		// public void handleEvent(Event event) {
		// txtConditionExpression.setText("");
		// txtConditionExpression.setEnabled(false);
		// txtConditionExpression.setVisible(false);
		// txtConditionValueMin.setEnabled(true);
		// txtConditionValueMax.setEnabled(true);
		// txtConditionValueMin.setVisible(true);
		// txtConditionValueMax.setVisible(true);
		//
		// }
		// });
		// radios[1] = new Button(container, SWT.RADIO);
		// radios[1].setText(OdysseusNLS.Function);
		// radios[1].addListener(SWT.Selection, new Listener() {
		//
		// @Override
		// public void handleEvent(Event event) {
		// txtConditionValueMin.setText("");
		// txtConditionValueMax.setText("");
		// txtConditionValueMin.setEnabled(false);
		// txtConditionValueMax.setEnabled(false);
		// txtConditionValueMin.setVisible(false);
		// txtConditionValueMax.setVisible(false);
		// txtConditionExpression.setEditable(true);
		// txtConditionExpression.setEnabled(true);
		// txtConditionExpression.setVisible(true);
		//
		// }
		// });

		this.txtConditionExpression = new Text(container, SWT.BORDER);
		gd = new GridData();
		final int txtAttributeFunctionWidth = txtConditionExpression
				.computeSize(SWT.DEFAULT, SWT.DEFAULT).x;
		gd.horizontalSpan = 2;
		gd.widthHint = Math.min(txtAttributeFunctionWidth, maxWidth);
		gd.horizontalAlignment = GridData.FILL;
		gd.grabExcessHorizontalSpace = true;
		txtConditionExpression.setLayoutData(gd);
		// txtConditionExpression.setVisible(false);
		// txtConditionExpression.setEnabled(false);

		// this.txtConditionValueMin = new Text(container, SWT.BORDER);
		// gd = new GridData();
		// final int txtAttributeValueMinWidth =
		// txtConditionValueMin.computeSize(
		// SWT.DEFAULT, SWT.DEFAULT).x;
		// gd.horizontalSpan = 1;
		// gd.widthHint = Math.min(txtAttributeValueMinWidth, maxWidth / 2);
		// gd.horizontalAlignment = GridData.FILL;
		// gd.grabExcessHorizontalSpace = true;
		// txtConditionValueMin.setLayoutData(gd);
		//
		// this.txtConditionValueMax = new Text(container, SWT.BORDER);
		// gd = new GridData();
		// final int txtAttributeValueMaxWidth =
		// txtConditionValueMax.computeSize(
		// SWT.DEFAULT, SWT.DEFAULT).x;
		// gd.horizontalSpan = 1;
		// gd.widthHint = Math.min(txtAttributeValueMaxWidth, maxWidth / 2);
		// gd.horizontalAlignment = GridData.FILL;
		// gd.grabExcessHorizontalSpace = true;
		// txtConditionValueMax.setLayoutData(gd);
		// new Label(container, SWT.NONE);

		final Label lblProperty = new Label(container, SWT.WRAP);
		lblProperty.setText(OdysseusNLS.MeasurementProperty);
		gd = new GridData();
		final int lblPropertyWidth = lblProperty.computeSize(SWT.DEFAULT,
				SWT.DEFAULT).x;
		gd.horizontalSpan = 2;
		gd.widthHint = Math.min(lblPropertyWidth, maxWidth);
		gd.horizontalAlignment = GridData.FILL;
		gd.grabExcessHorizontalSpace = true;
		lblProperty.setLayoutData(gd);

		this.cmbMeasurementProperty = new Combo(container, SWT.VERTICAL
				| SWT.DROP_DOWN | SWT.BORDER | SWT.READ_ONLY);
		gd = new GridData();
		final int cmbMeasurementPropertyWidth = cmbMeasurementProperty
				.computeSize(SWT.DEFAULT, SWT.DEFAULT).x;
		gd.horizontalSpan = 2;
		gd.widthHint = Math.min(cmbMeasurementPropertyWidth, maxWidth);
		gd.horizontalAlignment = GridData.FILL;
		gd.grabExcessHorizontalSpace = true;
		cmbMeasurementProperty.setLayoutData(gd);

		this.fillMeasurementPropertyCombo(this.cmbMeasurementProperty);

		this.txtMeasurementPropertyExpression = new Text(container, SWT.BORDER);
		gd = new GridData();
		final int txtMeasurementPropertyValueMinWidth = txtMeasurementPropertyExpression
				.computeSize(SWT.DEFAULT, SWT.DEFAULT).x;
		gd.horizontalSpan = 2;
		gd.widthHint = Math.min(txtMeasurementPropertyValueMinWidth, maxWidth);
		gd.horizontalAlignment = GridData.FILL;
		gd.grabExcessHorizontalSpace = true;
		txtMeasurementPropertyExpression.setLayoutData(gd);

		// radios[0].setSelection(true);

		return container;
	}

	@Override
	protected void configureShell(final Shell newShell) {
		super.configureShell(newShell);
		newShell.setText("Selection dialog");
	}

	private void saveInput() {
		this.property = (Property) this.cmbForProperty
				.getData(this.cmbForProperty.getItem(this.cmbForProperty
						.getSelectionIndex()));

		Property conditionProperty = (Property) this.cmbConditionProperty
				.getData(this.cmbConditionProperty
						.getItem(this.cmbConditionProperty.getSelectionIndex()));
		// if (this.txtConditionExpression.isEnabled()) {
		final String attributeFunction = this.txtConditionExpression.getText();
		this.condition = new ExpressionCondition(URI.create(this.uri + "/"
				+ conditionProperty.getName()), conditionProperty,
				attributeFunction);
		// } else {
		// final double attributeMinValue = Double
		// .parseDouble(this.txtConditionValueMin.getText());
		// final double attributeMaxValue = Double
		// .parseDouble(this.txtConditionValueMax.getText());
		// final Interval attributeInterval = new Interval(attributeMinValue,
		// attributeMaxValue);
		// this.condition = new IntervalCondition(URI.create(this.uri + "/"
		// + conditionProperty.getName()), conditionProperty,
		// attributeInterval);
		// }
		final SSNMeasurementProperty measurementProperty = (SSNMeasurementProperty) this.cmbMeasurementProperty
				.getData(this.cmbMeasurementProperty
						.getItem(this.cmbMeasurementProperty
								.getSelectionIndex()));
		final String measurementPropertyExpression = this.txtMeasurementPropertyExpression
				.getText();

		this.measurementPropery = new MeasurementProperty(URI.create(this.uri
				+ "/" + conditionProperty.getName() + "/"
				+ measurementProperty.name()),
				measurementProperty.getResource(),
				measurementPropertyExpression);
	}

	public Condition getCondition() {
		return this.condition;
	}

	public MeasurementProperty getMeasurementProperty() {
		return this.measurementPropery;
	}

	public SDFAttribute getAttribute() {
		return attribute;
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

	private void fillMeasurementPropertyCombo(final Combo combo) {
		combo.removeAll();
		for (SSNMeasurementProperty measurementProperty : SSNMeasurementProperty
				.values()) {
			combo.add(measurementProperty.toString());
			combo.setData(measurementProperty.toString(), measurementProperty);
		}
		combo.select(0);
	}

	private void fillPropertyCombo(final Combo combo) {
		final SensorOntologyService ontology = SensorRegistryPlugIn
				.getSensorOntologyService();
		combo.removeAll();
		final Set<Property> properties = new HashSet<Property>(
				ontology.getAllProperties());

		for (Property property : properties) {
			combo.add(property.getName());
			combo.setData(property.getName(), property);
		}
		combo.select(0);
	}

}
