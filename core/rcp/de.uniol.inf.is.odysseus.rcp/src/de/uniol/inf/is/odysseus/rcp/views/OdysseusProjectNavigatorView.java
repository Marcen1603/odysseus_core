package de.uniol.inf.is.odysseus.rcp.views;

import org.eclipse.core.resources.IResourceChangeEvent;
import org.eclipse.core.resources.IResourceChangeListener;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.navigator.CommonNavigator;

public class OdysseusProjectNavigatorView extends CommonNavigator implements IResourceChangeListener {

	@Override
	public void createPartControl(Composite aParent) {
		super.createPartControl(aParent);
		
		getCommonViewer().setInput( ResourcesPlugin.getWorkspace().getRoot());
		
		ResourcesPlugin.getWorkspace().addResourceChangeListener(this);
	}
	
	@Override
	public void resourceChanged(IResourceChangeEvent event) {
		PlatformUI.getWorkbench().getDisplay().asyncExec(new Runnable() {
			
			@Override
			public void run() {
				if( !getCommonViewer().getControl().isDisposed())
					getCommonViewer().refresh();
			}
		});
	}
}
