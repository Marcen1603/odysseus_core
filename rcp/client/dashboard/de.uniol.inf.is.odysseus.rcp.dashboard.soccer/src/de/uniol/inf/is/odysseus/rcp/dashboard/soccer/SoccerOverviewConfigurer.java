package de.uniol.inf.is.odysseus.rcp.dashboard.soccer;

import java.util.Collection;
import java.util.List;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;

import com.google.common.collect.Lists;

import de.uniol.inf.is.odysseus.core.physicaloperator.IPhysicalOperator;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;
import de.uniol.inf.is.odysseus.rcp.dashboard.AbstractDashboardPartConfigurer;

public class SoccerOverviewConfigurer extends AbstractDashboardPartConfigurer<SoccerOverviewDashboardPart> {

	private SoccerOverviewDashboardPart dashboardPart;
	private List<String> attributeNames;

	@Override
	public void init(SoccerOverviewDashboardPart dashboardPartToConfigure, Collection<IPhysicalOperator> roots) {
		dashboardPart = dashboardPartToConfigure;
		attributeNames = determineAttributes(roots);
	}

	private static List<String> determineAttributes(Collection<IPhysicalOperator> roots) {
		List<String> attributeNAmes = Lists.newArrayList();

		IPhysicalOperator firstRoot = roots.iterator().next();
		SDFSchema outputSchema = firstRoot.getOutputSchema();

		for (SDFAttribute outputAttribute : outputSchema) {
			attributeNAmes.add(outputAttribute.getAttributeName());
		}

		return attributeNAmes;
	}

	@Override
	public void createPartControl(Composite parent) {
		parent.setLayout(new GridLayout());

		Composite showComposite = createComposite(parent);
		createShowControls(showComposite);

		Composite attributeComposite = createComposite(parent);
		createAttributeControls(attributeComposite);
	}

	private static Composite createComposite(Composite parent) {
		Composite composite = new Composite(parent, SWT.NONE);
		composite.setLayoutData(new GridData(GridData.FILL_BOTH));
		return composite;
	}

	private void createShowControls(Composite parent) {
		parent.setLayout(new GridLayout());
		
		createCheckBox(parent, "Show Numbers", dashboardPart.isShowNumbers(), new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				Button b = (Button)e.widget;
				dashboardPart.setShowNumbers( b.getSelection());
				
				fireListener();
			}
		});
		
		createCheckBox(parent, "Show players team A", dashboardPart.isShowPlayersA(), new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				Button b = (Button)e.widget;
				dashboardPart.setShowPlayersA( b.getSelection());
				
				fireListener();
			}
		});
		
		createCheckBox(parent, "Show players team B", dashboardPart.isShowPlayersB(), new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				Button b = (Button)e.widget;
				dashboardPart.setShowPlayersB( b.getSelection());
				
				fireListener();
			}
		});

		createCheckBox(parent, "Show Ball", dashboardPart.isShowBall(), new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				Button b = (Button)e.widget;
				dashboardPart.setShowBall( b.getSelection());
				
				fireListener();
			}
		});
		
		createCheckBox(parent, "Show Referee", dashboardPart.isShowReferee(), new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				Button b = (Button)e.widget;
				dashboardPart.setShowReferee( b.getSelection());
				
				fireListener();
			}
		});
		
		createCheckBox(parent, "Show Field", dashboardPart.isShowField(), new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				Button b = (Button)e.widget;
				dashboardPart.setShowField( b.getSelection());
				
				fireListener();
			}
		});
		
		createCheckBox(parent, "Show Time", dashboardPart.isShowTime(), new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				Button b = (Button)e.widget;
				dashboardPart.setShowTime( b.getSelection());
				
				fireListener();
			}
		});
	}

	private static Button createCheckBox(Composite parent, String text, boolean isSelected, SelectionAdapter selectionAdapter) {
		Button checkBox = new Button(parent, SWT.CHECK );
		checkBox.setText(text);
		checkBox.setSelection(isSelected);
		checkBox.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		checkBox.addSelectionListener(selectionAdapter);
		return checkBox;
	}

	private void createAttributeControls(Composite parent) {
		parent.setLayout( new GridLayout(2, false));
		
		createLabel(parent, "Attribute for x-Position");
		createAttributeSelector(parent, dashboardPart.getXPosAttributeName(), attributeNames).addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				Combo c = (Combo)e.widget;
				String selectedAttribute = c.getItem(c.getSelectionIndex());
				
				dashboardPart.setXPosAttributeName(selectedAttribute);
			}
		});
		
		createLabel(parent, "Attribute for y-Position");
		createAttributeSelector(parent, dashboardPart.getYPosAttributeName(), attributeNames).addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				Combo c = (Combo)e.widget;
				String selectedAttribute = c.getItem(c.getSelectionIndex());
				
				dashboardPart.setYPosAttributeName(selectedAttribute);
			}
		});
		
		createLabel(parent, "Attribute for Timestamp");
		createAttributeSelector(parent, dashboardPart.getTimestampAttributeName(), attributeNames).addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				Combo c = (Combo)e.widget;
				String selectedAttribute = c.getItem(c.getSelectionIndex());
				
				dashboardPart.setTimestampAttributeName(selectedAttribute);
			}
		});
		
		createLabel(parent, "Attribute for SensorID");
		createAttributeSelector(parent, dashboardPart.getSIDAttributeName(), attributeNames).addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				Combo c = (Combo)e.widget;
				String selectedAttribute = c.getItem(c.getSelectionIndex());
				
				dashboardPart.setSIDAttributeName(selectedAttribute);
			}
		});
	}

	private static Combo createAttributeSelector(Composite parent, String selectedItem, List<String> attributeNames) {
		Combo combo = new Combo(parent, SWT.DROP_DOWN | SWT.BORDER | SWT.READ_ONLY);
		
		if( !attributeNames.isEmpty() ) {
			int selectedIndex = 0;
			for( String attributeName : attributeNames ) {
				combo.add(attributeName);
				if( attributeName.equals(selectedItem)) {
					selectedIndex = combo.getItemCount() - 1;
				}
			}
			combo.select(selectedIndex);
		} else {
			combo.add("<none>");
			combo.setEnabled(false);
			combo.select(0);
		}
		return combo;
	}

	private static Label createLabel(Composite parent, String text) {
		Label label = new Label(parent, SWT.NONE);
		label.setText(text);
		label.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		return label;
	}

	@Override
	public void dispose() {

	}

}
