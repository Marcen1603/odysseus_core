package de.uniol.inf.is.odysseus.rcp.dashboard.editors;

import org.eclipse.core.resources.IFile;
import org.eclipse.jface.util.LocalSelectionTransfer;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.dnd.DND;
import org.eclipse.swt.dnd.DropTarget;
import org.eclipse.swt.dnd.DropTargetAdapter;
import org.eclipse.swt.dnd.DropTargetEvent;
import org.eclipse.swt.dnd.Transfer;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Control;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Optional;
import com.google.common.base.Preconditions;

import de.uniol.inf.is.odysseus.rcp.dashboard.DashboardPlugIn;
import de.uniol.inf.is.odysseus.rcp.dashboard.IDashboardPart;
import de.uniol.inf.is.odysseus.rcp.dashboard.IDashboardPartHandler;
import de.uniol.inf.is.odysseus.rcp.dashboard.handler.XMLDashboardPartHandler;

public abstract class DashboardDropTarget {

	private static final Logger LOG = LoggerFactory.getLogger(DashboardDropTarget.class);
	private static final IDashboardPartHandler DASHBOARD_PART_HANDLER = new XMLDashboardPartHandler();
	private static final int DEFAULT_PART_WIDTH = 500;
	private static final int DEFAULT_PART_HEIGHT = 300;

	private final DropTarget dropTarget;
	private final Control composite;
	
	public DashboardDropTarget(Control composite) {
		Preconditions.checkNotNull(composite, "Composite as drop target must not be null!");
		
		this.composite = composite;
		
		dropTarget = new DropTarget(composite, DND.DROP_MOVE | DND.DROP_COPY);
		dropTarget.setTransfer(new Transfer[] { LocalSelectionTransfer.getTransfer() });
		dropTarget.addDropListener(new DropTargetAdapter() {
			
			@Override
			public void drop(DropTargetEvent event) {
				if( isDropAllowed() ) {
					processDropEvent(event);
				}
			}
			
		});		
	}
	
	public void dispose() {
		dropTarget.dispose();
	}
	
	private void processDropEvent(DropTargetEvent event) {
		Optional<IFile> optDashboardPartFile = getDashboardPartFile(event);
		if (optDashboardPartFile.isPresent()) {
			IFile dashboardPartFile = optDashboardPartFile.get();
			try {
				final IDashboardPart part = DASHBOARD_PART_HANDLER.load(dashboardPartFile);
				final Point position = composite.toControl(event.x, event.y);
				final DashboardPartPlacement place = new DashboardPartPlacement(part, dashboardPartFile.getFullPath().toString(), position.x, position.y, DEFAULT_PART_WIDTH, DEFAULT_PART_HEIGHT);
	
				dropDashboardPartPlacement(place);
			} catch( Throwable t ) {
				LOG.error("Exception during dropping dashboard part placement", t);
			}
		}
	}
	
	protected abstract boolean isDropAllowed();
	protected abstract void dropDashboardPartPlacement( DashboardPartPlacement place );
	
	private Optional<IFile> getDashboardPartFile( DropTargetEvent event ) {
		if (event.data instanceof IStructuredSelection) {
			final IStructuredSelection selection = (IStructuredSelection) event.data;
			if (selection.size() == 1) {
				final Object selectedObject = selection.getFirstElement();
				if (selectedObject instanceof IFile) {
					final IFile selectedFile = (IFile) selectedObject;
					if (selectedFile.getFileExtension().equals(DashboardPlugIn.DASHBOARD_PART_EXTENSION)) {
						return Optional.of(selectedFile);
					}
				}
			}
		}
		return Optional.absent();
	}
}
