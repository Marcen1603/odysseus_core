package windscadaanwendung.views.dashboard;

import org.eclipse.swt.widgets.Composite;

import windscadaanwendung.ca.WKA;

public class PhaseShiftPart extends WindDashboardPartView {

	public void createPartControl(Composite parent, WKA wka) {
		this.valueType = "phase_shift";
		this.farmPart = false;
		super.createPartControl(parent);
		super.loadDashboardPartFile(String.valueOf(wka.getID()) + this.getValueType() + this.getFileEnding());
	}

}
