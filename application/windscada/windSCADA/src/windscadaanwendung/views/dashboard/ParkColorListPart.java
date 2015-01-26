package windscadaanwendung.views.dashboard;

import org.eclipse.swt.widgets.Composite;

/**
 * This class should be used as a view of the Dashboardpart which shows the
 * colorList visualization of a kohonen-map of a whole windFarm
 * 
 * @author MarkMilster
 * 
 */
public class ParkColorListPart extends WindDashboardPartView {

	@Override
	public void createPartControl(Composite parent) {
		this.valueType = "ColorList";
		this.farmPart = true;
		super.createPartControl(parent);
		DashboardPartViewObserver.addDashboardPartView(this);
	}

}
