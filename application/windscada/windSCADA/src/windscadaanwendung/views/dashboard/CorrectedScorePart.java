package windscadaanwendung.views.dashboard;

import org.eclipse.swt.widgets.Composite;

public class CorrectedScorePart extends AbstractDashboardPartView {
	
	public CorrectedScorePart(){
	}
	
	@Override
	public void createPartControl(Composite parent) {
		super.createPartControl(parent);
		this.valueType = "corrected_score";
		super.loadDashboardPartFile("4155.corrected_score.prt");
		DashboardPartViewObserver.addDashboardPartView(this);
	}

}
