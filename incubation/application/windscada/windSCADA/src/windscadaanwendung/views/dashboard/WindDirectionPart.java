package windscadaanwendung.views.dashboard;

import org.eclipse.swt.widgets.Composite;

public class WindDirectionPart extends WindDashboardPartView {
	
	@Override
	public void createPartControl(Composite parent) {
		this.valueType = "wind_direction";
		this.farmPart = false;
		super.createPartControl(parent);
		DashboardPartViewObserver.addDashboardPartView(this);
	}

}