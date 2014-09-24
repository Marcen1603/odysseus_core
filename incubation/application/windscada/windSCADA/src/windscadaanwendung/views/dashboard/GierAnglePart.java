package windscadaanwendung.views.dashboard;

import org.eclipse.swt.widgets.Composite;

public class GierAnglePart extends WindDashboardPartView {
	
	@Override
	public void createPartControl(Composite parent) {
		this.valueType = "gier_angle";
		this.farmPart = false;
		super.createPartControl(parent);
		DashboardPartViewObserver.addDashboardPartView(this);
	}

}