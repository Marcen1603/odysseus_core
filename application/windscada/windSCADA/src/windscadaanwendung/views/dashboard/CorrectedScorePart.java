package windscadaanwendung.views.dashboard;

import org.eclipse.swt.widgets.Composite;

public class CorrectedScorePart extends AbstractDashboardPartView {

	public CorrectedScorePart(){
	}
	
	@Override
	public void createPartControl(Composite parent) {
		this.valueType = "corrected_score";
		this.farmPart = false;
		super.createPartControl(parent);
		// TODO
		super.loadDashboardPartFile("4155corrected_score.prt");
		DashboardPartViewObserver.addDashboardPartView(this);
	}

}
