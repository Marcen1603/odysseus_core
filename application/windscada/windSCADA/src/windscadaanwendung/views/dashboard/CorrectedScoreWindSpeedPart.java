package windscadaanwendung.views.dashboard;

import org.eclipse.swt.widgets.Composite;

public class CorrectedScoreWindSpeedPart extends AbstractDashboardPartView {

	@Override
	public void createPartControl(Composite parent) {
		this.valueType = "corrected_score_wind_speed";
		this.farmPart = false;
		super.createPartControl(parent);
		// TODO
		super.loadDashboardPartFile("4155corrected_score_wind_speed.prt");
		DashboardPartViewObserver.addDashboardPartView(this);
	}
}
