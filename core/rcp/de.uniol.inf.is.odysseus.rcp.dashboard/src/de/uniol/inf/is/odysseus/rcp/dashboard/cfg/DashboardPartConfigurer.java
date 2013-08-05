package de.uniol.inf.is.odysseus.rcp.dashboard.cfg;

import com.google.common.base.Preconditions;

import de.uniol.inf.is.odysseus.rcp.dashboard.IDashboardPart;
import de.uniol.inf.is.odysseus.rcp.dashboard.editors.DashboardEditor;
import de.uniol.inf.is.odysseus.rcp.dashboard.util.EditorUtil;
import de.uniol.inf.is.odysseus.rcp.dashboard.windows.DashboardPartConfigWindow;

public class DashboardPartConfigurer {

	private final IDashboardPart dashboardPart;

	public DashboardPartConfigurer(IDashboardPart dashboardPart) {
		Preconditions.checkNotNull(dashboardPart, "DashboardPart must not be null!");

		this.dashboardPart = dashboardPart;
	}

	public void startConfigure() {
		DashboardPartConfigWindow window = new DashboardPartConfigWindow(EditorUtil.determineCurrentShell(), dashboardPart);
		window.open();

		if (applyNewSettings(window.getSelectedSinkName()) && EditorUtil.isActiveEditorDashboardEditor()) {
			DashboardEditor dashboardEditor = (DashboardEditor) EditorUtil.determineActiveEditor();
			dashboardEditor.setDirty(true);

			dashboardEditor.getDashboard().update();
		}
	}

	private boolean applyNewSettings(String selectedSinkNames) {
		dashboardPart.setSinkNames(selectedSinkNames);
		return true;
	}
}
