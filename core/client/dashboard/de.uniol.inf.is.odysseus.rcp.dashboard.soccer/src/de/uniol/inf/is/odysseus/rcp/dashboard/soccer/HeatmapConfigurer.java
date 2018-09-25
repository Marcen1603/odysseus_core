package de.uniol.inf.is.odysseus.rcp.dashboard.soccer;

import java.util.Collection;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;

import de.uniol.inf.is.odysseus.core.physicaloperator.IPhysicalOperator;
import de.uniol.inf.is.odysseus.rcp.dashboard.AbstractDashboardPartConfigurer;
import de.uniol.inf.is.odysseus.rcp.dashboard.DashboardPartUtil;

public class HeatmapConfigurer extends AbstractDashboardPartConfigurer<HeatmapDashboardPart> {

	private static final String[] SELECTABLE_PLAYER_IDS = new String[] { "1", "2", "3", "4", "5", "6", "7", "8", "11", "12", "13", "14", "15", "16", "17", "18"};
	
	private HeatmapDashboardPart dashboardPart;
	
	@Override
	public void init(HeatmapDashboardPart dashboardPartToConfigure, Collection<IPhysicalOperator> roots) {
		dashboardPart = dashboardPartToConfigure;
		
	}

	@Override
	public void createPartControl(Composite parent) {
		Composite rootComposite = new Composite(parent, SWT.NONE);
		rootComposite.setLayoutData(new GridData(GridData.FILL_BOTH));
		rootComposite.setLayout(new GridLayout(2,false));
		
		DashboardPartUtil.createLabel(rootComposite, "Player ID");
		Combo playerIDCombo = DashboardPartUtil.createCombo(rootComposite, SELECTABLE_PLAYER_IDS, dashboardPart.getSelectedPlayerID());
		playerIDCombo.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				Combo combo = (Combo)e.widget;
				dashboardPart.setSelectedPlayerID(SELECTABLE_PLAYER_IDS[combo.getSelectionIndex()]);
			}
		});
	}

	@Override
	public void dispose() {
		
	}
	
}
