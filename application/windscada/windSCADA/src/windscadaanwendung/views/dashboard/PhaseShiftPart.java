package windscadaanwendung.views.dashboard;

import org.eclipse.swt.widgets.Composite;

import windscadaanwendung.ca.WKA;

/**
 * This class should be used as a view of the Dashboardpart which shows the
 * phaseShift of a single WKA in a textfield
 * 
 * @author MarkMilster
 * 
 */
public class PhaseShiftPart extends WindDashboardPartView {

	/**
	 * Creates the view
	 * 
	 * @param parent
	 *            The parent composite which can be a normal other swt-composite
	 * @param wka_id
	 *            The id of the wka which phaseShift should be shown in the
	 *            textfield
	 */
	public void createPartControl(Composite parent, WKA wka) {
		this.valueType = "phase_shift";
		this.farmPart = false;
		super.createPartControl(parent);
		super.loadDashboardPartFile(String.valueOf(wka.getID())
				+ this.getValueType() + this.getFileEnding());
	}

}
