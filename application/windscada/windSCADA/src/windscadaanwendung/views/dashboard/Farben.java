package windscadaanwendung.views.dashboard;

import org.eclipse.swt.widgets.Composite;

public class Farben extends WindDashboardPartView {

	public Farben() {
	}
	
	@Override
	public void createPartControl(Composite parent) {
		super.createPartControl(parent);
		super.loadDashboardPartFile("Farben.prt");
	}

}