package de.uniol.inf.is.odysseus.rcp.dashboard.cfg;

import java.util.Map;

import com.google.common.base.Preconditions;

import de.uniol.inf.is.odysseus.rcp.dashboard.Configuration;
import de.uniol.inf.is.odysseus.rcp.dashboard.editors.DashboardEditor;
import de.uniol.inf.is.odysseus.rcp.dashboard.util.EditorUtil;
import de.uniol.inf.is.odysseus.rcp.dashboard.windows.DashboardPartConfigWindow;

public class DashboardPartConfigurer {

	private final Configuration configuration;
	
	public DashboardPartConfigurer(Configuration configuration) {
		Preconditions.checkNotNull(configuration, "Configuration must not be null!");
		
		this.configuration = configuration;
	}
	
	public void startConfigure() {
		DashboardPartConfigWindow window = new DashboardPartConfigWindow(EditorUtil.determineCurrentShell(), configuration);
		window.open();

		if( applyNewSettings(configuration, window.getSelectedSettings()) && EditorUtil.isActiveEditorDashboardEditor()) {
			DashboardEditor dashboardEditor = (DashboardEditor) EditorUtil.determineActiveEditor();
			dashboardEditor.setDirty(true);
		}		
	}
	
	private boolean applyNewSettings(Configuration config, Map<String, String> settings) {
		boolean changed = false;
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
