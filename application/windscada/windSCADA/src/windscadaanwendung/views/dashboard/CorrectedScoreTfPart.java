package windscadaanwendung.views.dashboard;

import org.eclipse.swt.widgets.Composite;

/**
 * 
 * @author MarkMilster
 *
 */
public class CorrectedScoreTfPart extends WindDashboardPartView {

	public void createPartControl(Composite parent, int wka_id) {
		this.setValueType("corrected_score_tf");
		super.createPartControl(parent);
		super.loadDashboardPartFile(wka_id + this.getValueType() + this.getFileEnding());
		// not in Observer, because every instance of this class shows a specific WKA 
	}
}
