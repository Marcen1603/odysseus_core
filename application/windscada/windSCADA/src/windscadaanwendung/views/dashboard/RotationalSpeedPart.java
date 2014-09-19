package windscadaanwendung.views.dashboard;

import org.eclipse.swt.widgets.Composite;

public class RotationalSpeedPart extends AbstractDashboardPartView {
	
	public RotationalSpeedPart(){
	}
	
	@Override
	public void createPartControl(Composite parent) {
		super.createPartControl(parent);
		// TODO
		super.loadDashboardPartFile("4155rotational_speed.prt");
		DashboardPartViewObserver.addDashboardPartView(this);
	}

}
