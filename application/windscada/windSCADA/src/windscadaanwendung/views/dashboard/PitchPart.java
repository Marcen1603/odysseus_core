package windscadaanwendung.views.dashboard;

import org.eclipse.swt.widgets.Composite;

import windscadaanwendung.ca.WKA;

/**
 * This class should be used as a view of the Dashboardpart which shows the pith
 * angle of a single WKA in a textfield
 * 
 * @author MarkMilster
 * 
 */
public class PitchPart extends WindDashboardPartView {

	/**
	 * Creates the view
	 * 
	 * @param parent
	 *            The parent composite which can be a normal other swt-composite
	 * @param wka_id
	 *            The id of the wka which pitch angle should be shown in the
	 *            textfield
	 */
	public void createPartControl(Composite parent, WKA wka) {
		this.valueType = "pitch_angle";
		this.farmPart = false;
		super.createPartControl(parent);
		super.loadDashboardPartFile(String.valueOf(wka.getID())
				+ this.getValueType() + this.getFileEnding());
	}

}
