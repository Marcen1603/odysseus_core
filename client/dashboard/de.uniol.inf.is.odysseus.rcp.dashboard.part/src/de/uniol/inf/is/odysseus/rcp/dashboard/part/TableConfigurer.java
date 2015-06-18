package de.uniol.inf.is.odysseus.rcp.dashboard.part;

import java.util.ArrayList;
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
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Text;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.core.physicaloperator.IPhysicalOperator;
import de.uniol.inf.is.odysseus.rcp.dashboard.AbstractDashboardPartConfigurer;
import de.uniol.inf.is.odysseus.rcp.dashboard.DashboardPartUtil;

public class TableConfigurer extends AbstractDashboardPartConfigurer<TableDashboardPart> {
	
	private static final Logger LOG = LoggerFactory.getLogger(TableConfigurer.class);

	private TableDashboardPart dashboardPart;
	private Collection<IPhysicalOperator> roots;
	
	@Override
	public void init(TableDashboardPart dashboardPartToConfigure, Collection<IPhysicalOperator> roots ) {
		this.dashboardPart = dashboardPartToConfigure;
		this.roots = roots;
	}

	@Override
	public void createPartControl(Composite parent) {
		Composite topComposite = new Composite(parent, SWT.NONE);
		topComposite.setLayout(new GridLayout(2, false));
		topComposite.setLayoutData(new GridData(GridData.FILL_BOTH));

		createTitleControls(topComposite);
		createOperatorControls(topComposite);
		createAttributesControls(topComposite);
		createMaxDataControls(topComposite);
	}

	private void createTitleControls(Composite topComposite) {
		DashboardPartUtil.createLabel( topComposite, "Title");
		final Text titleText = DashboardPartUtil.createText(topComposite, dashboardPart.getTitle());
		titleText.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		titleText.addModifyListener(new ModifyListener() {
			@Override
			public void modifyText(ModifyEvent e) {
				dashboardPart.setTitle(titleText.getText());
				fireListener();
			}
		});
	}

	private void createMaxDataControls(Composite topComposite) {
		DashboardPartUtil.createLabel( topComposite, "Max Data Count");
		final Text maxDataText = DashboardPartUtil.createText(topComposite, String.valueOf(dashboardPart.getMaxData()));
		maxDataText.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		maxDataText.addModifyListener(new ModifyListener() {
			@Override
			public void modifyText(ModifyEvent e) {
				dashboardPart.setMaxData(Integer.valueOf(maxDataText.getText()));
				fireListener();
			}
		});
	}

	private void createAttributesControls(Composite topComposite) {
		DashboardPartUtil.createLabel(topComposite, "Attributes");
		
		String[] selection = TableDashboardPart.determineAttributes(dashboardPart.getAttributeList());
		IPhysicalOperator operator = null;
		for(IPhysicalOperator op : roots) {
			if(op.getName().equals(dashboardPart.getOperatorName())) {
				operator = op;
				break;
			}
		}
		if(operator == null && roots.size() > 0) {
			operator = roots.iterator().next();
		} else if(operator == null) {
			LOG.warn("Root operator list is empty.");
			return;
		}
		
		final org.eclipse.swt.widgets.List attributeSelector = DashboardPartUtil.createAttributeMultiSelector(topComposite, operator.getOutputSchema(), selection);
		attributeSelector.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		attributeSelector.addSelectionListener(new SelectionAdapter() {
			/* (non-Javadoc)
			 * @see org.eclipse.swt.events.SelectionAdapter#widgetSelected(org.eclipse.swt.events.SelectionEvent)
			 */
			@Override
			public void widgetSelected(SelectionEvent e) {
				StringBuilder builder = new StringBuilder();
				for(String selection : attributeSelector.getSelection()) {
					builder.append(selection).append(",");
				}
				if(builder.length() > 0) {
					dashboardPart.setAttributeList(builder.substring(0, builder.length()-1));
				}
				fireListener();
			}
		});
	}
	
	private void createOperatorControls(final Composite topComposite) {
		DashboardPartUtil.createLabel(topComposite, "Operator");
		
		List<String> operatorNames = new ArrayList<>();
		for(IPhysicalOperator op : roots) {
			operatorNames.add(op.getName());
		}
		
		String selection = dashboardPart.getOperatorName();
		if(selection == null || selection.equals("")) {
			if(operatorNames.size() > 0) {
				selection = operatorNames.get(0);
			}
		}
		
		final Combo comboLocked = DashboardPartUtil.createCombo(topComposite, operatorNames.toArray(new String[operatorNames.size()]), selection); //createListComboDropDown(topComposite, operatorNames, selection);
		comboLocked.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				String operatorName = comboLocked.getItem(comboLocked.getSelectionIndex());
				dashboardPart.setOperatorName(operatorName);
				for(Control child : topComposite.getChildren()) {
					if(child instanceof org.eclipse.swt.widgets.List) {
						((org.eclipse.swt.widgets.List) child).removeAll();
						for(IPhysicalOperator op : roots) {
							if(operatorName.equals(op.getName())) {
								DashboardPartUtil.updateAttributeMultiSelector((org.eclipse.swt.widgets.List)child, op.getOutputSchema());
								
								StringBuilder builder = new StringBuilder();
								for(String selection : ((org.eclipse.swt.widgets.List)child).getSelection()) {
									builder.append(selection).append(",");
								}
								if(builder.length() > 0) {
									dashboardPart.setAttributeList(builder.substring(0, builder.length()-1));
								}
							}
						}
					}
				}
				fireListener();
			}
		});
	}

	@Override
	public void dispose() {

	}

}
