package windscadaanwendung.views.dashboard;

import org.eclipse.swt.widgets.Composite;

public class CorrectedScorePart extends WindDashboardPartView {
	
	@Override
	public void createPartControl(Composite parent) {
		this.valueType = "corrected_score";
		this.farmPart = false;
		super.createPartControl(parent);
		DashboardPartViewObserver.addDashboardPartView(this);
	}

}
