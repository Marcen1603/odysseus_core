package windscadaanwendung.views.dashboard;

import org.eclipse.swt.widgets.Composite;

/**
 * This class should be used as a view of the Dashboardpart which shows the
 * corrected_score (the performance) and the windSpeed of a single WKA
 * 
 * @author MarkMilster
 * 
 */
public class CorrectedScoreWindSpeedPart extends WindDashboardPartView {

	@Override
	public void createPartControl(Composite parent) {
		this.valueType = "corrected_score_wind_speed";
		this.farmPart = false;
		super.createPartControl(parent);
		DashboardPartViewObserver.addDashboardPartView(this);
	}
}
