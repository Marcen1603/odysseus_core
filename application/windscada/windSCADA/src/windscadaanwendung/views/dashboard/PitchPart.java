package windscadaanwendung.views.dashboard;

import org.eclipse.swt.widgets.Composite;

public class PitchPart extends AbstractDashboardPartView {
	
	public PitchPart() {
		
	}
	
	@Override
	public void createPartControl(Composite parent) {
		super.createPartControl(parent);
		// TODO
		super.loadDashboardPartFile("4155pitch_angle.prt");
		DashboardPartViewObserver.addDashboardPartView(this);
	}

}
