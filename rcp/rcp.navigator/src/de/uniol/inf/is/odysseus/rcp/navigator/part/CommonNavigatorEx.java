package de.uniol.inf.is.odysseus.rcp.navigator.part;

import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.navigator.CommonNavigator;

public class CommonNavigatorEx extends CommonNavigator {

	@Override
	public void createPartControl(Composite aParent) {
		super.createPartControl(aParent);
		
		getCommonViewer().setInput( ResourcesPlugin.getWorkspace().getRoot());
	}
}
