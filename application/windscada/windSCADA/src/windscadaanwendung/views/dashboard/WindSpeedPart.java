package windscadaanwendung.views.dashboard;

import org.eclipse.swt.widgets.Composite;

public class WindSpeedPart extends AbstractDashboardPartView {
	
	public WindSpeedPart(){
	}
	
	@Override
	public void createPartControl(Composite parent) {
		super.createPartControl(parent);
		super.loadDashboardPartFile("4155.wind_speed.prt");
		DashboardPartViewObserver.addDashboardPartView(this);
	}

}
