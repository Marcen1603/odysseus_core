package de.uniol.inf.is.odysseus.rcp.dashboard.editors;

import org.eclipse.core.resources.IFile;
import org.eclipse.jface.util.LocalSelectionTransfer;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.dnd.DND;
import org.eclipse.swt.dnd.DropTarget;
import org.eclipse.swt.dnd.DropTargetAdapter;
import org.eclipse.swt.dnd.DropTargetEvent;
import org.eclipse.swt.dnd.Transfer;
import org.eclipse.swt.widgets.Composite;

import com.google.common.base.Optional;
import java.util.Objects;

import de.uniol.inf.is.odysseus.rcp.dashboard.DashboardPlugIn;

public abstract class DashboardDropTarget {

	private final DropTarget dropTarget;
	
	public DashboardDropTarget(Composite composite) {
		Objects.requireNonNull(composite, "Composite as drop target must not be null!");
		
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
			dropDashboardPartPlacement(optDashboardPartFile.get(), event);
		}
	}
	
	protected abstract boolean isDropAllowed();
	protected abstract void dropDashboardPartPlacement( IFile dashboardPartFile, DropTargetEvent event);
	
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
