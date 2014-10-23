package windscadaanwendung.views.dashboard;

import org.eclipse.swt.widgets.Composite;

/**
 * This class should be used as a view of the Dashboardpart which shows the
 * rotationalSpeed of a single WKA
 * 
 * @author MarkMilster
 * 
 */
public class RotationalSpeedPart extends WindDashboardPartView {

	@Override
	public void createPartControl(Composite parent) {
		this.valueType = "rotational_speed";
		this.farmPart = false;
		super.createPartControl(parent);
		DashboardPartViewObserver.addDashboardPartView(this);
	}

}
