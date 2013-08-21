package de.uniol.inf.is.odysseus.rcp.dashboard.part;

import java.util.Collection;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

import de.uniol.inf.is.odysseus.core.physicaloperator.IPhysicalOperator;
import de.uniol.inf.is.odysseus.rcp.dashboard.AbstractDashboardPartConfigurer;

public class KeyedTableConfigurer extends AbstractDashboardPartConfigurer<KeyedTableDashboardPart> {

	private KeyedTableDashboardPart dashboardPart;

	@Override
	public void init(KeyedTableDashboardPart dashboardPartToConfigure, Collection<IPhysicalOperator> roots) {
		this.dashboardPart = dashboardPartToConfigure;
	}

	@Override
	public void createPartControl(Composite parent) {
		Composite topComposite = new Composite(parent, SWT.NONE);
		topComposite.setLayout(new GridLayout(2, false));
		topComposite.setLayoutData(new GridData(GridData.FILL_BOTH));

		createAttributesControls(topComposite);
		createKeyAttribbutesControls(topComposite);
		createMaxDataControls(topComposite);
		createTitleControls(topComposite);
		createAgeControls(topComposite);
	}

	private void createAgeControls(Composite topComposite) {
		Button ageCheckBox = createCheckBox(topComposite, "Show age", dashboardPart.isShowAge(), new SelectionAdapter(){
			@Override
			public void widgetSelected(SelectionEvent e) {
				Button b = (Button)e.widget;
				dashboardPart.setShowAge(b.getSelection());
			}
		});
		GridData gd = new GridData();
		gd.horizontalSpan = 2;
		ageCheckBox.setLayoutData(gd);
		
		createLabel(topComposite, "Maximum colored age (ms)");
		createText(topComposite, String.valueOf(dashboardPart.getMaxAgeMillis())).addModifyListener(new ModifyListener() {
			@Override
			public void modifyText(ModifyEvent e) {
				Text text = (Text)e.widget;
				try {
					dashboardPart.setMaxAgeMillis(Long.valueOf(text.getText()));
				} catch( Throwable ignored ) {
				}
			}
		});
	}

	private static Button createCheckBox(Composite topComposite, String text, boolean selected, SelectionAdapter selectionAdapter) {
		Button box = new Button(topComposite, SWT.CHECK);
		box.setText(text);
		box.setSelection(selected);
		box.addSelectionListener(selectionAdapter);
		return box;
	}

	private void createKeyAttribbutesControls(Composite topComposite) {
		createLabel(topComposite, "Key attribute");
		final Combo attributesInput = createCombo(topComposite, dashboardPart.getAttributes());
		attributesInput.addModifyListener(new ModifyListener() {
			@Override
			public void modifyText(ModifyEvent e) {
				dashboardPart.setKeyAttribute(attributesInput.getText());
				fireListener();
			}
		});
	}

	private void createTitleControls(Composite topComposite) {
		createLabel(topComposite, "Title");
		final Text titleText = createText(topComposite, dashboardPart.getTitle());
		titleText.addModifyListener(new ModifyListener() {
			@Override
			public void modifyText(ModifyEvent e) {
				dashboardPart.setTitle(titleText.getText());
				fireListener();
			}
		});
	}

	private void createMaxDataControls(Composite topComposite) {
		createLabel(topComposite, "Max Data Count");
		final Text maxDataText = createText(topComposite, String.valueOf(dashboardPart.getMaxData()));
		maxDataText.addModifyListener(new ModifyListener() {
			@Override
			public void modifyText(ModifyEvent e) {
				dashboardPart.setMaxData(Integer.valueOf(maxDataText.getText()));
				fireListener();
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
				fireListener();
			}
		});
	}

	private static Text createText(Composite topComposite, String txt) {
		Text text = new Text(topComposite, SWT.BORDER);
		text.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		text.setText(txt != null ? txt : "");
		return text;
	}
	
	private static Combo createCombo(Composite topComposite, String[] items){
		Combo combo = new Combo(topComposite, SWT.DROP_DOWN);
		combo.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		combo.setItems(items);
		return combo;
	}

	private static void createLabel(Composite parent, String text) {
		Label label = new Label(parent, SWT.NONE);
		label.setText(text);
	}

	@Override
	public void dispose() {

	}

}
