package de.uniol.inf.is.odysseus.rcp.viewer.nodeview;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.ISelectionListener;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.part.ViewPart;

import de.uniol.inf.is.odysseus.rcp.resource.ResourceManager;
import de.uniol.inf.is.odysseus.rcp.viewer.model.graph.IOdysseusNodeModel;

public class NodeViewPart extends ViewPart implements ISelectionListener {
	
	private static boolean imagesLoaded = false;
	private final Map<IOdysseusNodeModel, NodeInfoPanel> showInfoPanels = new HashMap<IOdysseusNodeModel, NodeInfoPanel>();
	private Composite parent;
	
	public NodeViewPart() {

	}

	@Override
	public void createPartControl(Composite parent) {
		this.parent = new Composite(parent, SWT.NONE);
		this.parent.setLayout(new GridLayout());

		getSite().getWorkbenchWindow().getSelectionService().addSelectionListener(this);

		// Bilder laden
		if( !imagesLoaded ) {
				ResourceManager.getInstance().load(parent.getDisplay(), new ResourceConfiguration(Activator.getDefault().getBundle()));
				imagesLoaded = true;
		}
	}

	@Override
	public void setFocus() {
	}

	@Override
	public void selectionChanged(IWorkbenchPart part, final ISelection selection) {
		PlatformUI.getWorkbench().getDisplay().asyncExec(new Runnable() {
			@Override
			public void run() {
				if (selection instanceof IStructuredSelection) {
					IStructuredSelection structSelection = (IStructuredSelection) selection;

					List<?> objList = structSelection.toList();
					for (Object obj : objList) {
						if( obj instanceof IOdysseusNodeModel ) {
							addInfoPanel((IOdysseusNodeModel)obj);
						}
					}
					
					for( IOdysseusNodeModel view : showInfoPanels.keySet().toArray( new IOdysseusNodeModel[0]) ) {
						if( !objList.contains(view)) {
							removeInfoPanel(view);
						}
					}
					
					parent.layout();
					
				} else {
				}
			}
		});
	}

	private void addInfoPanel(IOdysseusNodeModel node) {
		if (!showInfoPanels.containsKey(node)) {

			NodeInfoPanel p = new NodeInfoPanel(parent, node);
			showInfoPanels.put(node, p);
		}
	}

	private void removeInfoPanel(IOdysseusNodeModel unselected) {
		if (showInfoPanels.containsKey(unselected)) {
			NodeInfoPanel panel = showInfoPanels.get(unselected);
			if (panel.isPinned())
				return;

			panel.dispose();
			showInfoPanels.remove(unselected);

		}
	}
}
