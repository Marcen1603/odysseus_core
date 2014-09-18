package windscadaanwendung.views.dashboard;

import org.eclipse.swt.widgets.Composite;

public class RotationalSpeedPart extends AbstractDashboardPartView {
	
	public RotationalSpeedPart(){
	}
	
	@Override
	public void createPartControl(Composite parent) {
		super.createPartControl(parent);
		super.loadDashboardPartFile("4155.rotational_speed.prt");
		DashboardPartViewObserver.addDashboardPartView(this);
	}

}
