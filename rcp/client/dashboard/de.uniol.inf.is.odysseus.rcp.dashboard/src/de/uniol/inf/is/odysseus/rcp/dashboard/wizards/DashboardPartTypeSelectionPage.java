package de.uniol.inf.is.odysseus.rcp.dashboard.wizards;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;

import de.uniol.inf.is.odysseus.rcp.dashboard.DashboardPartRegistry;
import de.uniol.inf.is.odysseus.rcp.dashboard.IDashboardPart;

public class DashboardPartTypeSelectionPage extends WizardPage {

	private final List<String> dashboardPartNames;

	private Composite rootComposite;
	private Combo choosePartNameCombo;
	private int selectedIndex = -1;

	protected DashboardPartTypeSelectionPage(String pageName) {
		super(pageName);

		setTitle("Choose type of DashboardPart");
		setDescription("Choose one type of DashboardPart.");

		dashboardPartNames = determineDashboardPartNames();
	}

	@Override
	public void createControl(Composite parent) {
		initializeDialogUnits(parent);

		rootComposite = new Composite(parent, SWT.NONE);
		rootComposite.setLayoutData(new GridData((GridData.FILL_HORIZONTAL | GridData.FILL_VERTICAL)));
		rootComposite.setLayout(new GridLayout(1, true));

		final Composite choosePartNameComposite = new Composite(rootComposite, SWT.NONE);
		choosePartNameComposite.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		choosePartNameComposite.setLayout(new GridLayout(2, false));

		final Label choosePartNameLabel = new Label(choosePartNameComposite, SWT.NONE);
		choosePartNameLabel.setText("Type");

		choosePartNameCombo = new Combo(choosePartNameComposite, SWT.BORDER | SWT.READ_ONLY);
		choosePartNameCombo.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		choosePartNameCombo.setItems(dashboardPartNames.toArray(new String[0]));
		choosePartNameCombo.setText("");
		choosePartNameCombo.addSelectionListener(new SelectionAdapter() {

			@Override
			public void widgetSelected(SelectionEvent e) {
				selectedIndex = choosePartNameCombo.getSelectionIndex();
				setPageComplete(true);
			}
		});

		finishCreation(rootComposite);

	}

	public String getSelectedDashboardPartName() {
		if (selectedIndex > -1)
			return dashboardPartNames.get(selectedIndex);
		return null;
	}

	public IDashboardPart getSelectedDashboardPart() {
		try {
			IDashboardPart newDashboardPart = DashboardPartRegistry
					.createDashboardPart(getSelectedDashboardPartName());
			return newDashboardPart;
		} catch (InstantiationException e) {
			return null;
		}
	}

	private static List<String> determineDashboardPartNames() {
		List<String> names = new ArrayList<String>(DashboardPartRegistry.getDashboardPartNames());
		Collections.sort(names);
		return names;
	}

	private void finishCreation(Composite rootComposite) {
		setErrorMessage(null);
		setMessage(null);
		setControl(rootComposite);
		setPageComplete(false);
	}

}
