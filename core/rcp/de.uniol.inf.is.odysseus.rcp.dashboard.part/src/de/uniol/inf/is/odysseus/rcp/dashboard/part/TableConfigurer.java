package de.uniol.inf.is.odysseus.rcp.dashboard.part;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

import de.uniol.inf.is.odysseus.rcp.dashboard.IDashboardPartConfigurer;

public class TableConfigurer implements IDashboardPartConfigurer<TableDashboardPart> {

	private TableDashboardPart dashboardPart;

	@Override
	public void init(TableDashboardPart dashboardPartToConfigure) {
		this.dashboardPart = dashboardPartToConfigure;
	}

	@Override
	public void createPartControl(Composite parent) {
		Composite topComposite = new Composite(parent, SWT.NONE);
		topComposite.setLayout(new GridLayout(2, false));
		topComposite.setLayoutData(new GridData(GridData.FILL_BOTH));

		createAttributesControls(topComposite);
		createMaxDataControls(topComposite);
		createTitleControls(topComposite);
	}

	private void createTitleControls(Composite topComposite) {
		createLabel( topComposite, "Title");
		final Text titleText = createText(topComposite, dashboardPart.getTitle());
		titleText.addModifyListener(new ModifyListener() {
			@Override
			public void modifyText(ModifyEvent e) {
				dashboardPart.setTitle(titleText.getText());
			}
		});
	}

	private void createMaxDataControls(Composite topComposite) {
		createLabel( topComposite, "Max Data Count");
		final Text maxDataText = createText(topComposite, String.valueOf(dashboardPart.getMaxData()));
		maxDataText.addModifyListener(new ModifyListener() {
			@Override
			public void modifyText(ModifyEvent e) {
				dashboardPart.setMaxData(Integer.valueOf(maxDataText.getText()));
			}
		});
	}

	private void createAttributesControls(Composite topComposite) {
		createLabel(topComposite, "Attributes");
		final Text attributesInput = createText(topComposite, dashboardPart.getAttributeList());
		attributesInput.addModifyListener(new ModifyListener() {
			@Override
			public void modifyText(ModifyEvent e) {
				dashboardPart.setAttributeList(attributesInput.getText());
			}
		});
	}

	private static Text createText(Composite topComposite, String txt) {
		Text text = new Text(topComposite, SWT.BORDER);
		text.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		text.setText(txt);
		return text;
	}

	private static void createLabel(Composite parent, String text) {
		Label label = new Label(parent, SWT.NONE);
		label.setText(text);
	}

	@Override
	public void dispose() {

	}

}
