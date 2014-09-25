package windscadaanwendung.views.dashboard;

import org.eclipse.swt.widgets.Composite;

public class AEListPart extends WindDashboardPartView {

	@Override
	public void createPartControl(Composite parent) {
		this.valueType = "AEList";
		this.farmPart = true;
		super.createPartControl(parent);
		this.loadDashboardPartFile(this.getValueType() + this.getFileEnding());
	}
}
