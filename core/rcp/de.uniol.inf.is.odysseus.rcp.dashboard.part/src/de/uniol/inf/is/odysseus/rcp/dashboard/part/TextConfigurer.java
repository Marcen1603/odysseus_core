package de.uniol.inf.is.odysseus.rcp.dashboard.part;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

import de.uniol.inf.is.odysseus.rcp.dashboard.AbstractDashboardPartConfigurer;

public class TextConfigurer extends AbstractDashboardPartConfigurer<TextDashboardPart> {

	private TextDashboardPart dashboardPart;

	@Override
	public void init(TextDashboardPart dashboardPartToConfigure) {
		dashboardPart = dashboardPartToConfigure;
	}

	@Override
	public void createPartControl(Composite parent) {
		Composite topComposite = new Composite(parent, SWT.NONE);
		topComposite.setLayout(new GridLayout(2, false));
		topComposite.setLayoutData(new GridData(GridData.FILL_BOTH));

		createMaxDataControls(topComposite);
		createUpdateIntervalControls(topComposite);
		createShowHeartbeatsControls(topComposite);
	}

	@Override
	public void dispose() {

	}

	private void createMaxDataControls(Composite topComposite) {
		createLabel(topComposite, "Max Elements");
		final Text maxElementsText = createText(topComposite, String.valueOf(dashboardPart.getMaxElements()));
		maxElementsText.addModifyListener(new ModifyListener() {
			@Override
			public void modifyText(ModifyEvent e) {
				dashboardPart.setMaxElements(Integer.valueOf(maxElementsText.getText()));
			}
		});
	}

	private void createUpdateIntervalControls(Composite topComposite) {
		createLabel(topComposite, "Update interval (ms)");
		final Text attributesInput = createText(topComposite, String.valueOf(dashboardPart.getUpdateInterval()));
		attributesInput.addModifyListener(new ModifyListener() {
			@Override
			public void modifyText(ModifyEvent e) {
				dashboardPart.setUpdateInterval(Long.valueOf(attributesInput.getText()));
			}
		});
	}

	private void createShowHeartbeatsControls(Composite topComposite) {
		createLabel(topComposite, "Show Heartbeats");
		final Combo comboLocked = createBooleanComboDropDown(topComposite, dashboardPart.isShowHeartbeats());
		comboLocked.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				dashboardPart.setShowHeartbeats(comboLocked.getSelectionIndex() == 0);
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

	private static void createLabel(Composite parent, String text) {
		Label label = new Label(parent, SWT.NONE);
		label.setText(text);
	}

	private static Text createText(Composite topComposite, String txt) {
		Text text = new Text(topComposite, SWT.BORDER);
		text.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		text.setText(txt);
		return text;
	}

}
