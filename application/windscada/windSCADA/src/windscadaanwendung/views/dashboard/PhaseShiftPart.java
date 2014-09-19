package windscadaanwendung.views.dashboard;

import org.eclipse.swt.widgets.Composite;

public class PhaseShiftPart extends AbstractDashboardPartView {
	
	public PhaseShiftPart(){
	}
	
	@Override
	public void createPartControl(Composite parent) {
		super.createPartControl(parent);
		// TODO
		super.loadDashboardPartFile("4155phase_shift.prt");
		DashboardPartViewObserver.addDashboardPartView(this);
	}

}
