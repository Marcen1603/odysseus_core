package de.uniol.inf.is.odysseus.rcp.dashboard.textfield.parts;

import java.util.Collection;

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

import de.uniol.inf.is.odysseus.core.physicaloperator.IPhysicalOperator;
import de.uniol.inf.is.odysseus.rcp.dashboard.AbstractDashboardPartConfigurer;
import de.uniol.inf.is.odysseus.rcp.dashboard.DashboardPartUtil;

public class TextfieldConfigurer extends AbstractDashboardPartConfigurer<TextfieldDashboardPart> {

	private TextfieldDashboardPart dashboardPart;

	@Override
	public void init(TextfieldDashboardPart dashboardPartToConfigure, Collection<IPhysicalOperator> roots ) {
		dashboardPart = dashboardPartToConfigure;
	}

	@Override
	public void createPartControl(Composite parent) {
		Composite topComposite = new Composite(parent, SWT.NONE);
		topComposite.setLayout(new GridLayout(2, false));
		topComposite.setLayoutData(new GridData(GridData.FILL_BOTH));

		createAttributeToShowControls(topComposite);
		createUpdateIntervalControls(topComposite);
		createShowHeartbeatsControls(topComposite);
	}

	private void createAttributeToShowControls(Composite topComposite) {
		DashboardPartUtil.createLabel(topComposite, "Attributeindex to show");
		final Text attributeIndex = DashboardPartUtil.createText(topComposite, String.valueOf(dashboardPart.getAtributeToShow()));
		attributeIndex.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		attributeIndex.addModifyListener(new ModifyListener() {
			@Override
			public void modifyText(ModifyEvent e) {
				dashboardPart.setAtributeToShow(Integer.parseInt(attributeIndex.getText()));
				fireListener();
			}
		});
	}

	@Override
	public void dispose() {

	}

	

	private void createUpdateIntervalControls(Composite topComposite) {
		DashboardPartUtil.createLabel(topComposite, "Update interval (ms)");
		final Text attributesInput = DashboardPartUtil.createText(topComposite, String.valueOf(dashboardPart.getUpdateInterval()));
		attributesInput.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		attributesInput.addModifyListener(new ModifyListener() {
			@Override
			public void modifyText(ModifyEvent e) {
				dashboardPart.setUpdateInterval(Long.valueOf(attributesInput.getText()));
				fireListener();
			}
		});
	}

	private void createShowHeartbeatsControls(Composite topComposite) {
		DashboardPartUtil.createLabel(topComposite, "Show Heartbeats");
		final Combo comboLocked = createBooleanComboDropDown(topComposite, dashboardPart.isShowHeartbeats());
		comboLocked.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				dashboardPart.setShowHeartbeats(comboLocked.getSelectionIndex() == 0);
				fireListener();
			}
		});
	}

	private static Combo createBooleanComboDropDown(Composite tableComposite, boolean isSetToTrue) {
		Combo comboDropDown = new Combo(tableComposite, SWT.DROP_DOWN | SWT.BORDER | SWT.READ_ONLY);
		comboDropDown.add("true");
		comboDropDown.add("false");
		comboDropDown.select(isSetToTrue ? 0 : 1);
		comboDropDown.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		return comboDropDown;
	}
}
