package windscadaanwendung.views.dashboard;

import org.eclipse.swt.widgets.Composite;

/**
 * This class should be used as a view of the Dashboardpart which shows the
 * windDirection at a single WKA
 * 
 * @author MarkMilster
 * 
 */
public class WindDirectionPart extends WindDashboardPartView {

	@Override
	public void createPartControl(Composite parent) {
		this.valueType = "wind_direction";
		this.farmPart = false;
		super.createPartControl(parent);
		DashboardPartViewObserver.addDashboardPartView(this);
	}

}