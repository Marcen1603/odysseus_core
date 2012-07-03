package de.uniol.inf.is.odysseus.rcp.dashboard.wizards;

import java.util.List;

import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;

import com.google.common.base.Optional;

import de.uniol.inf.is.odysseus.rcp.dashboard.DashboardPartRegistry;
import de.uniol.inf.is.odysseus.rcp.dashboard.desc.DashboardPartDescriptor;

public class NewDashboardPartWizardPage2 extends WizardPage {

	private final List<String> dashboardPartNames;

	private Label descriptionLabel;
	private Combo choosePartNameCombo;

	public NewDashboardPartWizardPage2(String pageName) {
		super(pageName);

		setTitle("Choose type of DashboardPart");
		setDescription("Choose one type of DashboardPart and configure it.");

		dashboardPartNames = determineDashboardPartNames();
	}

	@Override
	public void createControl(Composite parent) {
		initializeDialogUnits(parent);

		Composite rootComposite = new Composite(parent, SWT.NONE);
		rootComposite.setLayoutData(new GridData((GridData.FILL_HORIZONTAL | GridData.FILL_VERTICAL)));
		rootComposite.setLayout(new GridLayout(1, true));

		Composite choosePartNameComposite = new Composite(rootComposite, SWT.NONE);
		choosePartNameComposite.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		choosePartNameComposite.setLayout(new GridLayout(2, false));

		Label choosePartNameLabel = new Label(choosePartNameComposite, SWT.NONE);
		choosePartNameLabel.setText("Type");

		choosePartNameCombo = new Combo(choosePartNameComposite, SWT.BORDER | SWT.READ_ONLY);
		choosePartNameCombo.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		choosePartNameCombo.setItems(dashboardPartNames.toArray(new String[0]));

		descriptionLabel = new Label(rootComposite, SWT.BORDER);
		GridData gd = new GridData(GridData.FILL_HORIZONTAL);
		gd.heightHint = 100;
		descriptionLabel.setLayoutData(gd);
		descriptionLabel.setBackground(Display.getCurrent().getSystemColor(SWT.COLOR_WHITE));
		descriptionLabel.setText("");
		choosePartNameCombo.addSelectionListener(new SelectionAdapter() {

			@Override
			public void widgetSelected(SelectionEvent e) {
				selectDashboardPart(choosePartNameCombo.getSelectionIndex());
			}

		});

		selectDashboardPart(0);
		finishCreation(rootComposite);
	}

	private void finishCreation(Composite rootComposite) {
		setErrorMessage(null);
		setMessage(null);
		setControl(rootComposite);
		setPageComplete(true);
	}

	private void selectDashboardPart(int index) {
		choosePartNameCombo.select(index);
		refreshDescriptionLabel(getDashboardPartName(index));
	}

	private void refreshDescriptionLabel(String dashboardPartName) {
		descriptionLabel.setText(getDescription(dashboardPartName));
	}

	private String getDashboardPartName(int index) {
		return dashboardPartNames.get(index);
	}

	private String getDescription(String dashboardPartName) {
		Optional<DashboardPartDescriptor> optDescriptor = DashboardPartRegistry.getDashboardPartDescriptor(dashboardPartName);
		if (optDescriptor.isPresent()) {
			DashboardPartDescriptor descriptor = optDescriptor.get();
			return descriptor.getDescription();
		} else {
			return "";
		}
	}

	private static List<String> determineDashboardPartNames() {
		return DashboardPartRegistry.getDashboardPartNames();
	}
}
