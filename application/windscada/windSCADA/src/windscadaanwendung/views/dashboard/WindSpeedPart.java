package windscadaanwendung.views.dashboard;

import org.eclipse.swt.widgets.Composite;

public class WindSpeedPart extends WindDashboardPartView {
	
	@Override
	public void createPartControl(Composite parent) {
		this.valueType = "wind_speed";
		this.farmPart = false;
		super.createPartControl(parent);
		DashboardPartViewObserver.addDashboardPartView(this);
	}

}
