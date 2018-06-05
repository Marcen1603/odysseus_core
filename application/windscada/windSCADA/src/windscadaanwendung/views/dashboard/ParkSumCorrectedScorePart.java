package windscadaanwendung.views.dashboard;

import org.eclipse.swt.widgets.Composite;

/**
 * This class should be used as a view of the Dashboardpart which shows the
 * corrected_score_sum (the performance) of a whole windFarm
 * 
 * @author MarkMilster
 * 
 */
public class ParkSumCorrectedScorePart extends WindDashboardPartView {

	@Override
	public void createPartControl(Composite parent) {
		this.valueType = "corrected_score_sum";
		this.farmPart = true;
		super.createPartControl(parent);
		DashboardPartViewObserver.addDashboardPartView(this);
	}

}
