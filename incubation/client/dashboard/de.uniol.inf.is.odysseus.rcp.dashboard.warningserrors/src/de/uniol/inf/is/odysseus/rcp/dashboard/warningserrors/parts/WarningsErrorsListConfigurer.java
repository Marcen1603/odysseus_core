package de.uniol.inf.is.odysseus.rcp.dashboard.warningserrors.parts;

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

/**
 * 
 * @author MarkMilster
 *
 */
public class WarningsErrorsListConfigurer extends AbstractDashboardPartConfigurer<WarningsErrorsListDashboardPart> {

	private WarningsErrorsListDashboardPart dashboardPart;

	@Override
	public void init(WarningsErrorsListDashboardPart dashboardPartToConfigure, Collection<IPhysicalOperator> roots ) {
		dashboardPart = dashboardPartToConfigure;
	}

	@Override
	public void createPartControl(Composite parent) {
		Composite topComposite = new Composite(parent, SWT.NONE);
		topComposite.setLayout(new GridLayout(2, false));
		topComposite.setLayoutData(new GridData(GridData.FILL_BOTH));
		
		createWarningAndErrorControls(topComposite);
		createIndexesControls(topComposite);
	}
	
	private void createIndexesControls(Composite topComposite) {
		DashboardPartUtil.createLabel(topComposite, "wka_id Index:");
		final Text wkaIdIndex = DashboardPartUtil.createText(topComposite, String.valueOf(dashboardPart.getWkaIdIndex()));
		wkaIdIndex.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		wkaIdIndex.addModifyListener(new ModifyListener() {
			@Override
			public void modifyText(ModifyEvent e) {
				dashboardPart.setWkaIdIndex(Integer.parseInt(wkaIdIndex.getText()));
				fireListener();
			}
		});
		
		DashboardPartUtil.createLabel(topComposite, "windfarm_id Index: ");
		final Text farmIdIndex = DashboardPartUtil.createText(topComposite, String.valueOf(dashboardPart.getFarmIdIndex()));
		farmIdIndex.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		farmIdIndex.addModifyListener(new ModifyListener() {
			@Override
			public void modifyText(ModifyEvent e) {
				dashboardPart.setFarmIdIndex(Integer.parseInt(farmIdIndex.getText()));
				fireListener();
			}
		});
		
		DashboardPartUtil.createLabel(topComposite, "value_type index:");
		final Text valueTypeIndex = DashboardPartUtil.createText(topComposite, String.valueOf(dashboardPart.getValueTypeIndex()));
		valueTypeIndex.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		valueTypeIndex.addModifyListener(new ModifyListener() {
			@Override
			public void modifyText(ModifyEvent e) {
				dashboardPart.setValueTypeIndex(Integer.parseInt(valueTypeIndex.getText()));
				fireListener();
			}
		});
	}

	private void createWarningAndErrorControls(Composite topComposite) {
		DashboardPartUtil.createLabel(topComposite, "Show Warnings");
		final Combo comboWarnings = createBooleanComboDropDown(topComposite, dashboardPart.isShowWarning());
		comboWarnings.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				dashboardPart.setShowWarning(comboWarnings.getSelectionIndex() == 0);
				fireListener();
			}
		});
		DashboardPartUtil.createLabel(topComposite, "Show Errors");
		final Combo comboErrors = createBooleanComboDropDown(topComposite, dashboardPart.isShowError());
		comboErrors.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				dashboardPart.setShowError(comboErrors.getSelectionIndex() == 0);
				fireListener();
			}
		});
		
		DashboardPartUtil.createLabel(topComposite, "Warningindex");
		final Text warningIndex = DashboardPartUtil.createText(topComposite, String.valueOf(dashboardPart.getWarningIndex()));
		warningIndex.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		warningIndex.addModifyListener(new ModifyListener() {
			@Override
			public void modifyText(ModifyEvent e) {
				dashboardPart.setWarningIndex(Integer.parseInt(warningIndex.getText()));
				fireListener();
			}
		});
		DashboardPartUtil.createLabel(topComposite, "Errorindex");
		final Text errorIndex = DashboardPartUtil.createText(topComposite, String.valueOf(dashboardPart.getErrorIndex()));
		errorIndex.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		errorIndex.addModifyListener(new ModifyListener() {
			@Override
			public void modifyText(ModifyEvent e) {
				dashboardPart.setErrorIndex(Integer.parseInt(errorIndex.getText()));
				fireListener();
			}
		});
	}

	@Override
	public void dispose() {

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
