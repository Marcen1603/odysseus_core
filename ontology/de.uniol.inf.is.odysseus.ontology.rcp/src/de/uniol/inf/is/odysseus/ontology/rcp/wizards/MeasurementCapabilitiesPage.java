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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.window.Window;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;

import de.uniol.inf.is.odysseus.core.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.ontology.model.MeasurementCapability;
import de.uniol.inf.is.odysseus.ontology.model.condition.Condition;
import de.uniol.inf.is.odysseus.ontology.model.property.MeasurementProperty;
import de.uniol.inf.is.odysseus.ontology.rcp.dialogs.MeasurementCapabiltyDialog;
import de.uniol.inf.is.odysseus.ontology.rcp.l10n.OdysseusNLS;

/**
 * @author Christian Kuka <christian@kuka.cc>
 * 
 */
public class MeasurementCapabilitiesPage extends WizardPage {
	private final SensingDevicePage sensingDevicePage;
	private Combo cmbAttribute;
	private Table tblCapabilities;
	private final Map<SDFAttribute, List<MeasurementCapability>> attributeCapabilities = new HashMap<SDFAttribute, List<MeasurementCapability>>();

	public MeasurementCapabilitiesPage(final String pageName,
			final IStructuredSelection selection,
			final SensingDevicePage sensingDevicePage) {
		super(pageName);
		this.sensingDevicePage = sensingDevicePage;
		this.setTitle("Set measurement capabilities");
		this.setDescription("Set the attribute and the condition in which the property holds");
	}

	@Override
	public void createControl(final Composite parent) {
		this.initializeDialogUnits(parent);

		this.initializeDialogUnits(parent);

		final Composite container = new Composite(parent, SWT.NULL);
		container.setFont(parent.getFont());

		this.setControl(container);
		container.setLayout(new GridLayout(2, false));

		final Label lblAttribute = new Label(container, SWT.NONE);
		lblAttribute.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, false,
				false, 2, 1));
		lblAttribute.setText(OdysseusNLS.Attribute + ":");

		this.cmbAttribute = new Combo(container, SWT.VERTICAL | SWT.DROP_DOWN
				| SWT.BORDER | SWT.READ_ONLY);
		this.fillAttributes(this.cmbAttribute);
		this.cmbAttribute.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(final SelectionEvent e) {

				final SDFAttribute attribute = (SDFAttribute) MeasurementCapabilitiesPage.this.cmbAttribute.getData(MeasurementCapabilitiesPage.this.cmbAttribute
						.getItem(MeasurementCapabilitiesPage.this.cmbAttribute
								.getSelectionIndex()));
				MeasurementCapabilitiesPage.this.tblCapabilities.removeAll();
				if ((MeasurementCapabilitiesPage.this.attributeCapabilities != null)
						&& (attribute != null)) {
					for (final MeasurementCapability capability : MeasurementCapabilitiesPage.this.attributeCapabilities
							.get(attribute)) {
						final TableItem item = new TableItem(
								MeasurementCapabilitiesPage.this.tblCapabilities,
								SWT.NONE);
						for (Condition condition : capability.getInConditions()) {
							for (MeasurementProperty measurementProperty : capability
									.getHasMeasurementProperties()) {
								item.setText(0, capability.getForProperty()
										.getName());
								item.setText(1, condition.getOnProperty()
										.getName());
								item.setText(2, condition.toString());
								item.setText(3, measurementProperty
										.getResource().getLocalName());
								item.setText(4,
										measurementProperty.getExpression());
							}
						}
					}
				}
			}
		});
		final GridData gd_cmbAttribute = new GridData(SWT.FILL, SWT.CENTER,
				false, false, 2, 1);
		gd_cmbAttribute.widthHint = 328;
		this.cmbAttribute.setLayoutData(gd_cmbAttribute);

		final Label lblAttributes = new Label(container, SWT.NONE);
		lblAttributes.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, false,
				false, 2, 1));
		lblAttributes.setText(OdysseusNLS.MeasurementCapabilities + ":");

		this.tblCapabilities = new Table(container, SWT.BORDER | SWT.MULTI);
		this.tblCapabilities.setLinesVisible(true);
		this.tblCapabilities.setHeaderVisible(true);
		String[] titles = { OdysseusNLS.Property, OdysseusNLS.Condition,
				OdysseusNLS.Expression, OdysseusNLS.MeasurementProperty,
				OdysseusNLS.Expression };
		for (int i = 0; i < titles.length; i++) {
			TableColumn column = new TableColumn(this.tblCapabilities, SWT.NONE);
			column.setText(titles[i]);
		}
		final GridData gd_propertyTableViewer = new GridData(SWT.FILL,
				SWT.FILL, false, true, 1, 1);
		gd_propertyTableViewer.widthHint = 328;
		this.tblCapabilities.setLayoutData(gd_propertyTableViewer);
		for (int i = 0; i < titles.length; i++) {
			this.tblCapabilities.getColumn(i).pack();
		}

		final Composite btnComposite = new Composite(container, SWT.NULL);
		btnComposite.setLayout(new GridLayout(1, false));
		final Button btnAdd = new Button(btnComposite, SWT.NONE);
		final GridData gd_btnAdd = new GridData(SWT.FILL, SWT.CENTER, false,
				false, 1, 1);
		btnAdd.setLayoutData(gd_btnAdd);
		btnAdd.addSelectionListener(new SelectionAdapter() {

			@Override
			public void widgetSelected(final SelectionEvent e) {
				final SDFAttribute attribute = (SDFAttribute) MeasurementCapabilitiesPage.this.cmbAttribute.getData(MeasurementCapabilitiesPage.this.cmbAttribute
						.getItem(MeasurementCapabilitiesPage.this.cmbAttribute
								.getSelectionIndex()));

				final MeasurementCapabiltyDialog measurementCapabilityDialog = new MeasurementCapabiltyDialog(
						btnAdd.getShell(),
						URI.create(MeasurementCapabilitiesPage.this.sensingDevicePage
								.getSensingDeviceURI().toString()
								+ "/"
								+ attribute.getAttributeName()), attribute);

				measurementCapabilityDialog.open();
				if (MeasurementCapabilitiesPage.this.tblCapabilities
						.isDisposed()) {
					return;
				}
				if (measurementCapabilityDialog.getReturnCode() == Window.OK) {
					final TableItem item = new TableItem(
							MeasurementCapabilitiesPage.this.tblCapabilities,
							SWT.NONE);
					item.setText(0, measurementCapabilityDialog.getProperty()
							.getName());
					item.setText(1, measurementCapabilityDialog.getCondition()
							.getOnProperty().getName());
					item.setText(2, measurementCapabilityDialog.getCondition()
							.toString());
					item.setText(3, measurementCapabilityDialog
							.getMeasurementProperty().getResource()
							.getLocalName());
					item.setText(4, measurementCapabilityDialog
							.getMeasurementProperty().getExpression());

					final MeasurementCapability capability = new MeasurementCapability(
							URI.create(MeasurementCapabilitiesPage.this.sensingDevicePage
									.getSensingDeviceURI().toString()
									+ "/"
									+ attribute.getAttributeName()), attribute
									.getAttributeName(),
							measurementCapabilityDialog.getProperty());
					capability.addCondition(measurementCapabilityDialog
							.getCondition());
					capability
							.addMeasurementProperty(measurementCapabilityDialog
									.getMeasurementProperty());
					if (!MeasurementCapabilitiesPage.this.attributeCapabilities
							.containsKey(attribute)) {
						MeasurementCapabilitiesPage.this.attributeCapabilities
								.put(attribute,
										new ArrayList<MeasurementCapability>());
					}
					MeasurementCapabilitiesPage.this.attributeCapabilities.get(
							attribute).add(capability);
					item.setData(capability);
				}
			}
		});
		btnAdd.setText(OdysseusNLS.Add);
		final Button btnRemove = new Button(btnComposite, SWT.NONE);
		final GridData gd_btnRemove = new GridData(SWT.FILL, SWT.CENTER, false,
				false, 1, 1);
		btnRemove.setLayoutData(gd_btnRemove);
		btnRemove.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(final SelectionEvent e) {
				final int index = MeasurementCapabilitiesPage.this.tblCapabilities
						.getSelectionIndex();
				final MeasurementCapability capability = (MeasurementCapability) MeasurementCapabilitiesPage.this.tblCapabilities
						.getItem(index).getData();

				final SDFAttribute attribute = (SDFAttribute) MeasurementCapabilitiesPage.this.cmbAttribute.getData(MeasurementCapabilitiesPage.this.cmbAttribute
						.getItem(MeasurementCapabilitiesPage.this.cmbAttribute
								.getSelectionIndex()));
				MeasurementCapabilitiesPage.this.attributeCapabilities.get(
						attribute).remove(capability);

				MeasurementCapabilitiesPage.this.tblCapabilities.remove(index);
			}
		});
		btnRemove.setText(OdysseusNLS.Remove);

		// finishCreation(container);
	}

	public List<MeasurementCapability> getMeasurementCapabilities() {
		final List<MeasurementCapability> measurementCapabilities = new ArrayList<MeasurementCapability>();
		for (final SDFAttribute attribute : this.attributeCapabilities.keySet()) {
			measurementCapabilities.addAll(this.attributeCapabilities
					.get(attribute));
		}
		return measurementCapabilities;
	}

	private void fillAttributes(final Combo combo) {
		combo.removeAll();
		for (final SDFAttribute attribute : this.sensingDevicePage
				.getAttributes()) {
			combo.add(attribute.getAttributeName());
			combo.setData(attribute.getAttributeName(), attribute);
		}
		combo.select(0);
	}

	@Override
	public void setVisible(final boolean visible) {
		super.setVisible(visible);

		if (visible == true) {
			this.fillAttributes(this.cmbAttribute);
		}
	}

	@SuppressWarnings("unused")
	private void finishCreation(final Composite rootComposite) {
		this.setErrorMessage(null);
		this.setMessage(null);
		this.setControl(rootComposite);
		this.setPageComplete(false);
	}

}