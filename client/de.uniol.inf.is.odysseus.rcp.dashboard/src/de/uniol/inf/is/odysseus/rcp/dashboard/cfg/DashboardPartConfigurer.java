package de.uniol.inf.is.odysseus.rcp.dashboard.cfg;

import java.util.Objects;

import de.uniol.inf.is.odysseus.rcp.dashboard.IDashboardPart;
import de.uniol.inf.is.odysseus.rcp.dashboard.controller.DashboardPartController;
import de.uniol.inf.is.odysseus.rcp.dashboard.editors.DashboardEditor;
import de.uniol.inf.is.odysseus.rcp.dashboard.util.EditorUtil;
import de.uniol.inf.is.odysseus.rcp.dashboard.windows.DashboardPartConfigWindow;

public class DashboardPartConfigurer {

	private final IDashboardPart dashboardPart;
	private final DashboardPartController dashboardPartController;

	public DashboardPartConfigurer(IDashboardPart dashboardPart, DashboardPartController controller) {
		Objects.requireNonNull(dashboardPart, "DashboardPart must not be null!");
		Objects.requireNonNull(controller, "DashboardPartController must not be null!");

		this.dashboardPart = dashboardPart;
		this.dashboardPartController = controller;
	}

	public void startConfigure() {
		DashboardPartConfigWindow window = new DashboardPartConfigWindow(EditorUtil.determineCurrentShell(), dashboardPart, dashboardPartController);
		window.open();

		if (applyNewSettings(window.getSelectedSinkName(), window.isSinkSynced()) && EditorUtil.isActiveEditorDashboardEditor()) {
			DashboardEditor dashboardEditor = (DashboardEditor) EditorUtil.determineActiveEditor();
			dashboardEditor.setDirty(true);

			dashboardEditor.getDashboard().update();
		}
	}

	private boolean applyNewSettings(String selectedSinkNames, boolean sinksSynced) {
		dashboardPart.setSinkNames(selectedSinkNames);
		dashboardPart.setSinkSynchronized(sinksSynced);
		return true;
	}
}
