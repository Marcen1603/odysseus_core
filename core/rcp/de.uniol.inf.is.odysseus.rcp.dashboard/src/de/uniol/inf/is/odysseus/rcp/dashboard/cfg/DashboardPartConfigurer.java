package de.uniol.inf.is.odysseus.rcp.dashboard.cfg;

import java.util.Map;

import com.google.common.base.Preconditions;

import de.uniol.inf.is.odysseus.rcp.dashboard.Configuration;
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
		DashboardPartConfigWindow window = new DashboardPartConfigWindow(EditorUtil.determineCurrentShell(), dashboardPart.getConfiguration());
		window.open();

		if( applyNewSettings(window.getSelectedSettings(), window.getSelectedSinkName() ) && EditorUtil.isActiveEditorDashboardEditor()) {
			DashboardEditor dashboardEditor = (DashboardEditor) EditorUtil.determineActiveEditor();
			dashboardEditor.setDirty(true);
		}		
	}
	
	private boolean applyNewSettings(Map<String, String> settings, String selectedSinkName) {
		Configuration config = dashboardPart.getConfiguration();
		boolean changed = false;
		
		if( !config.get(Configuration.SINK_NAME_CFG).equals(selectedSinkName)) {
			config.set(Configuration.SINK_NAME_CFG, selectedSinkName);
			changed = true;
		}
		
		for (final String key : settings.keySet()) {
			String newValue = settings.get(key);
			String oldValue = (config.get(key) != null) ? config.get(key).toString() : "";
			
			if( !oldValue.equals(newValue)) {
				config.setAsString(key, settings.get(key));
				changed = true;
			}
		}
		return changed;
	}
}
