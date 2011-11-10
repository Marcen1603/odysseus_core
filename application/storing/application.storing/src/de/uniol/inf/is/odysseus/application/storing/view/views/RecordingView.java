package de.uniol.inf.is.odysseus.application.storing.view.views;

import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.part.ViewPart;

import de.uniol.inf.is.odysseus.application.storing.controller.IRecordingListener;
import de.uniol.inf.is.odysseus.application.storing.controller.RecordingController;


public class RecordingView extends ViewPart implements IRecordingListener {

	/**
	 * The ID of the view as specified by the extension.
	 */
	public static final String ID = "de.uniol.inf.is.odysseus.application.storing.view.views.RecordingView";

	private TableViewer viewer;

	/**
	 * The constructor.
	 */
	public RecordingView() {
	}

	/**
	 * This is a callback that will allow us to create the viewer and initialize
	 * it.
	 */
	public void createPartControl(Composite parent) {
		viewer = new TableViewer(parent, SWT.MULTI | SWT.H_SCROLL | SWT.V_SCROLL);
		viewer.setContentProvider(new ViewContentProvider());
		viewer.setLabelProvider(new ViewLabelProvider());
		viewer.setInput(getViewSite());

		// Contextmenu
		MenuManager menuManager = new MenuManager();
		Menu contextMenu = menuManager.createContextMenu(getTreeViewer().getControl());
		// Set the MenuManager
		getTreeViewer().getControl().setMenu(contextMenu);
		getSite().registerContextMenu(menuManager, getTreeViewer());
		getSite().setSelectionProvider(getTreeViewer());
		RecordingController.getInstance().addListener(this);
	}

	private TableViewer getTreeViewer() {
		return this.viewer;
	}

	/**
	 * Passing the focus request to the viewer's control.
	 */
	public void setFocus() {
		viewer.getControl().setFocus();
	}
	
	@Override
	public void dispose() {
		RecordingController.getInstance().removeListener(this);
		super.dispose();
	}
	
	
	private void refresh() {
		PlatformUI.getWorkbench().getDisplay().asyncExec(new Runnable() {
			@Override
			public void run() {
				try {
					viewer.refresh(); 
				} catch (Exception e) {
					viewer.setInput("Refresh failed");					
				}
			}

		});
		
	}
	@Override
	public void recordingChanged() {
		refresh();		
	}
}