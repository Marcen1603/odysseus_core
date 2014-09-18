package windscadaanwendung.views.dashboard;

import org.eclipse.swt.widgets.Composite;

public class PitchPart extends AbstractDashboardPartView {
	
	public PitchPart() {
		
	}
	
	@Override
	public void createPartControl(Composite parent) {
		super.createPartControl(parent);
		this.valueType = "pitch_angle";
		super.loadDashboardPartFile("4155.pitch_angle.prt");
		DashboardPartViewObserver.addDashboardPartView(this);
	}

}
