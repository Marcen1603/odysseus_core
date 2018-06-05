package windscadaanwendung.views.dashboard;

import org.eclipse.swt.widgets.Composite;

/**
 * This class should be used as a view of the Dashboardpart which shows the
 * gierAngle of a single WKA
 * 
 * @author MarkMilster
 * 
 */
public class GierAnglePart extends WindDashboardPartView {

	@Override
	public void createPartControl(Composite parent) {
		this.valueType = "gier_angle";
		this.farmPart = false;
		super.createPartControl(parent);
		DashboardPartViewObserver.addDashboardPartView(this);
	}

}