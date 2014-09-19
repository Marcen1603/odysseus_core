package windscadaanwendung.views.dashboard;

import org.eclipse.swt.widgets.Composite;

public class WindSpeedPart extends AbstractDashboardPartView {
	
	public WindSpeedPart(){
	}
	
	@Override
	public void createPartControl(Composite parent) {
		super.createPartControl(parent);
		// TODO
		super.loadDashboardPartFile("4155wind_speed.prt");
		DashboardPartViewObserver.addDashboardPartView(this);
	}

}
