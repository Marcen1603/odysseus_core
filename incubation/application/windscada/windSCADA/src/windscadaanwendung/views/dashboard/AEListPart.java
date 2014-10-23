package windscadaanwendung.views.dashboard;

import org.eclipse.swt.widgets.Composite;

/**
 * This class should be used as a view of the Dashboardpart which shows a List
 * of all currently active Warnings and Errors
 * 
 * @author MarkMilster
 * 
 */
public class AEListPart extends WindDashboardPartView {

	@Override
	public void createPartControl(Composite parent) {
		this.valueType = "AEList";
		this.farmPart = true;
		super.createPartControl(parent);
		this.loadDashboardPartFile(this.getValueType() + this.getFileEnding());
	}
}
