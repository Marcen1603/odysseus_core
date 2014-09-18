package windscadaanwendung.views.dashboard;

import org.eclipse.swt.widgets.Composite;

public class CorrectedScoreTfPart extends AbstractDashboardPartView {
	
	public CorrectedScoreTfPart(){
	}
	
	public void createPartControl(Composite parent, int wka_id) {
		super.createPartControl(parent);
		super.loadDashboardPartFile(wka_id + ".corrected_score_tf.prt");
	}
}
