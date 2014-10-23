package windscadaanwendung.views.dashboard;

import org.eclipse.swt.widgets.Composite;

/**
 * This class should be used as a view of the Dashboardpart which shows the
 * corrected_score (the performance) of a single WKA
 * 
 * @author MarkMilster
 * 
 */
public class CorrectedScorePart extends WindDashboardPartView {

	@Override
	public void createPartControl(Composite parent) {
		this.valueType = "corrected_score";
		this.farmPart = false;
		super.createPartControl(parent);
		DashboardPartViewObserver.addDashboardPartView(this);
	}

}
