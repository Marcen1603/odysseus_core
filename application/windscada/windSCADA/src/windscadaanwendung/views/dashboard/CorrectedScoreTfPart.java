package windscadaanwendung.views.dashboard;

import org.eclipse.swt.widgets.Composite;

/**
 * This class should be used as a view of the Dashboardpart which shows the
 * corrected_score (the performance) of a single WKA in a textfield
 * 
 * @author MarkMilster
 * 
 */
public class CorrectedScoreTfPart extends WindDashboardPartView {

	/**
	 * Creates the view
	 * 
	 * @param parent
	 *            The parent composite which can be a normal other swt-composite
	 * @param wka_id
	 *            The id of the wka which corrected_score should be shown in the
	 *            textfield
	 */
	public void createPartControl(Composite parent, int wka_id) {
		this.setValueType("corrected_score_tf");
		super.createPartControl(parent);
		super.loadDashboardPartFile(wka_id + this.getValueType()
				+ this.getFileEnding());
		// not in Observer, because every instance of this class shows a
		// specific WKA
	}
}
