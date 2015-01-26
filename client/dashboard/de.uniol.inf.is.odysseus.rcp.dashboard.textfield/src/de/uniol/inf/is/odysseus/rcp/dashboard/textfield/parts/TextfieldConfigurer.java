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

/**
 * The Configurer builds the GUI to configure a specific Dashboard-Part
 * 
 * @author MarkMilster
 * 
 */
public class TextfieldConfigurer extends
		AbstractDashboardPartConfigurer<TextfieldDashboardPart> {

	private TextfieldDashboardPart dashboardPart;

	@Override
	public void init(TextfieldDashboardPart dashboardPartToConfigure,
			Collection<IPhysicalOperator> roots) {
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
		createWarningAndErrorControls(topComposite);
	}

	/**
	 * Creates the specified controls on the topComposite
	 * 
	 * @param topComposite
	 */
	private void createWarningAndErrorControls(Composite topComposite) {
		DashboardPartUtil.createLabel(topComposite, "Show Warnings");
		final Combo comboWarnings = createBooleanComboDropDown(topComposite,
				dashboardPart.isShowWarning());
		comboWarnings.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				dashboardPart.setShowWarning(comboWarnings.getSelectionIndex() == 0);
				fireListener();
			}
		});
		DashboardPartUtil.createLabel(topComposite, "Show Errors");
		final Combo comboErrors = createBooleanComboDropDown(topComposite,
				dashboardPart.isShowError());
		comboErrors.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				dashboardPart.setShowError(comboErrors.getSelectionIndex() == 0);
				fireListener();
			}
		});

		DashboardPartUtil.createLabel(topComposite, "Warningindex");
		final Text warningIndex = DashboardPartUtil.createText(topComposite,
				String.valueOf(dashboardPart.getWarningIndex()));
		warningIndex.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		warningIndex.addModifyListener(new ModifyListener() {
			@Override
			public void modifyText(ModifyEvent e) {
				dashboardPart.setWarningIndex(Integer.parseInt(warningIndex
						.getText()));
				fireListener();
			}
		});
		DashboardPartUtil.createLabel(topComposite, "Errorindex");
		final Text errorIndex = DashboardPartUtil.createText(topComposite,
				String.valueOf(dashboardPart.getErrorIndex()));
		errorIndex.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		errorIndex.addModifyListener(new ModifyListener() {
			@Override
			public void modifyText(ModifyEvent e) {
				dashboardPart.setErrorIndex(Integer.parseInt(errorIndex
						.getText()));
				fireListener();
			}
		});
	}

	/**
	 * Creates the specified controls on the topComposite
	 * 
	 * @param topComposite
	 */
	private void createAttributeToShowControls(Composite topComposite) {
		DashboardPartUtil.createLabel(topComposite, "Attributeindex to show");
		final Text attributeIndex = DashboardPartUtil.createText(topComposite,
				String.valueOf(dashboardPart.getAtributeToShow()));
		attributeIndex.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		attributeIndex.addModifyListener(new ModifyListener() {
			@Override
			public void modifyText(ModifyEvent e) {
				dashboardPart.setAtributeToShow(Integer.parseInt(attributeIndex
						.getText()));
				fireListener();
			}
		});
	}

	@Override
	public void dispose() {

	}

	/**
	 * Creates the specified controls on the topComposite
	 * 
	 * @param topComposite
	 */
	private void createUpdateIntervalControls(Composite topComposite) {
		DashboardPartUtil.createLabel(topComposite, "Update interval (ms)");
		final Text attributesInput = DashboardPartUtil.createText(topComposite,
				String.valueOf(dashboardPart.getUpdateInterval()));
		attributesInput.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		attributesInput.addModifyListener(new ModifyListener() {
			@Override
			public void modifyText(ModifyEvent e) {
				dashboardPart.setUpdateInterval(Long.valueOf(attributesInput
						.getText()));
				fireListener();
			}
		});
	}

	/**
	 * Creates the specified controls on the topComposite
	 * 
	 * @param topComposite
	 */
	private void createShowHeartbeatsControls(Composite topComposite) {
		DashboardPartUtil.createLabel(topComposite, "Show Heartbeats");
		final Combo comboLocked = createBooleanComboDropDown(topComposite,
				dashboardPart.isShowHeartbeats());
		comboLocked.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				dashboardPart.setShowHeartbeats(comboLocked.getSelectionIndex() == 0);
				fireListener();
			}
		});
	}

	/**
	 * Creates a Combo with boolean values as used in other Configurers
	 * 
	 * @param tableComposite
	 * @param isSetToTrue
	 * @return
	 */
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
}
