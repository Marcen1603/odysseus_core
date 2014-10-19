package de.uniol.inf.is.odysseus.rcp.dashboard.windpower;

import java.util.Collection;
import java.util.List;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Text;

import com.google.common.collect.Lists;

import de.uniol.inf.is.odysseus.core.physicaloperator.IPhysicalOperator;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;
import de.uniol.inf.is.odysseus.rcp.dashboard.AbstractDashboardPartConfigurer;
import de.uniol.inf.is.odysseus.rcp.dashboard.DashboardPartUtil;

/**
 * Configurer of the WindTurbineDashBoardPart
 * 
 * @author Dennis Nowak
 * 
 */
public class WindTurbineConfigurer extends
		AbstractDashboardPartConfigurer<WindTurbineDashboardPart> {

	private WindTurbineDashboardPart dashboardPart;
	private List<String> attributeNames;

	@Override
	public void init(WindTurbineDashboardPart dashboardPartToConfigure,
			Collection<IPhysicalOperator> roots) {
		this.dashboardPart = dashboardPartToConfigure;
		attributeNames = determineAttributes(roots);
	}

	private static List<String> determineAttributes(
			Collection<IPhysicalOperator> roots) {
		List<String> attributeNames = Lists.newArrayList();

		IPhysicalOperator firstRoot = roots.iterator().next();
		SDFSchema outputSchema = firstRoot.getOutputSchema();

		for (SDFAttribute outputAttribute : outputSchema) {
			attributeNames.add(outputAttribute.getAttributeName());
		}

		return attributeNames;
	}

	@Override
	public void createPartControl(Composite parent) {
		Composite topComposite = new Composite(parent, SWT.NONE);
		topComposite.setLayout(new GridLayout(2, false));
		topComposite.setLayoutData(new GridData(GridData.FILL_BOTH));

		createUpdateIntervalControls(topComposite);
		createShowTextControls(topComposite);
		if (attributeNames.contains("warning")) {
			createShowWarningsControls(topComposite);
		}
		if (attributeNames.contains("error")) {
			createShowErrorsControls(topComposite);
		}
		createSetRotorImageControls(topComposite);

	}

	private void createSetRotorImageControls(Composite topComposite) {
		DashboardPartUtil.createLabel(topComposite, "Path to rotor Image");
		final Text attributesInput = DashboardPartUtil.createText(topComposite,
				String.valueOf(dashboardPart.getRotorImagePath()));
		attributesInput.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		attributesInput.addModifyListener(new ModifyListener() {
			@Override
			public void modifyText(ModifyEvent e) {
				dashboardPart.setRotorImagePath(attributesInput.getText());
				fireListener();
			}
		});
	}

	private void createShowErrorsControls(Composite topComposite) {
		DashboardPartUtil.createLabel(topComposite, "Show Errors");
		final Combo comboLocked = createBooleanComboDropDown(topComposite,
				dashboardPart.isShowError());
		comboLocked.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				dashboardPart.setShowError(comboLocked.getSelectionIndex() == 0);
				fireListener();
			}
		});
	}

	private void createShowWarningsControls(Composite topComposite) {
		DashboardPartUtil.createLabel(topComposite, "Show Warnings");
		final Combo comboLocked = createBooleanComboDropDown(topComposite,
				dashboardPart.isShowWarning());
		comboLocked.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				dashboardPart.setShowWarning(comboLocked.getSelectionIndex() == 0);
				fireListener();
			}
		});
	}

	private void createShowTextControls(Composite topComposite) {
		DashboardPartUtil.createLabel(topComposite, "Print measurement");
		final Combo comboLocked = createBooleanComboDropDown(topComposite,
				dashboardPart.isShowText());
		comboLocked.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				dashboardPart.setShowText(comboLocked.getSelectionIndex() == 0);
				fireListener();
			}
		});
	}

	private void createUpdateIntervalControls(Composite topComposite) {
		DashboardPartUtil.createLabel(topComposite, "Update interval (ms)");
		final Text attributesInput = DashboardPartUtil.createText(topComposite,
				String.valueOf(dashboardPart.getUpdateInterval()));
		attributesInput.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		attributesInput.addModifyListener(new ModifyListener() {
			@Override
			public void modifyText(ModifyEvent e) {
				dashboardPart.setUpdateInterval(Integer.valueOf(attributesInput
						.getText()));
				fireListener();
			}
		});
	}

	private static Combo createBooleanComboDropDown(Composite tableComposite,
			boolean isSetToTrue) {
		Combo comboDropDown = new Combo(tableComposite, SWT.DROP_DOWN
				| SWT.BORDER | SWT.READ_ONLY);
		comboDropDown.add("true");
		comboDropDown.add("false");
		comboDropDown.select(isSetToTrue ? 0 : 1);
		comboDropDown.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		return comboDropDown;
	}

	@Override
	public void dispose() {

	}

}
