package windscadaanwendung.views.dashboard;


import org.eclipse.swt.widgets.Composite;

public class DPVTest extends AbstractDashboardPartView {

	public DPVTest() {
	}
	
	@Override
	public void createPartControl(Composite parent) {
		super.createPartControl(parent);
		super.loadDashboardPartFile("BidAgg.prt");
	}

}
