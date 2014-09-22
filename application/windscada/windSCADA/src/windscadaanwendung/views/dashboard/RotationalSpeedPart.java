package windscadaanwendung.views.dashboard;

import org.eclipse.swt.widgets.Composite;

public class RotationalSpeedPart extends AbstractDashboardPartView {

	@Override
	public void createPartControl(Composite parent) {
		this.valueType = "rotational_speed";
		this.farmPart = false;
		super.createPartControl(parent);
		DashboardPartViewObserver.addDashboardPartView(this);
	}

}
