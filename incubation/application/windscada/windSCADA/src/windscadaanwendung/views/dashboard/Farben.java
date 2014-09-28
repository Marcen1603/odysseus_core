package windscadaanwendung.views.dashboard;

import org.eclipse.swt.widgets.Composite;

/**
 * This class should be used as a view of the Dashboardpart which shows the RGB-Colors of a Kohonen-Map
 * 
 * @author MarkMilster
 *
 */
public class Farben extends WindDashboardPartView {
	
	@Override
	public void createPartControl(Composite parent) {
		super.createPartControl(parent);
		super.loadDashboardPartFile("Farben.prt");
	}

}