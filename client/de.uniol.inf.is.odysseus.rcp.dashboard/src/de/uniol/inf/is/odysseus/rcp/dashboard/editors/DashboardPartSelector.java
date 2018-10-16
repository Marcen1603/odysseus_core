package de.uniol.inf.is.odysseus.rcp.dashboard.editors;

import java.util.List;

import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.ISelectionProvider;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.StructuredSelection;

import com.google.common.base.Optional;
import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;


public class DashboardPartSelector implements ISelectionProvider {

	private final List<ISelectionChangedListener> listeners = Lists.newArrayList();
	
	private IStructuredSelection selection = StructuredSelection.EMPTY;
	private DashboardPartPlacement selectedDashboardPartPlacement = null;
	
	private boolean isLocked = false;
	
	public void setLock( boolean locked ) {
		isLocked = locked;
	}
	
	@Override
	public IStructuredSelection getSelection() {
		return !isLocked ? selection : StructuredSelection.EMPTY;
	}
	
	public Optional<DashboardPartPlacement> getSelectedDashboardPartPlacement() {
		return Optional.fromNullable(selectedDashboardPartPlacement);
	}
	
	public boolean hasSelection() {
		return !isLocked && !selection.isEmpty();
	}
	
	public boolean isSelected( DashboardPartPlacement placement ) {
		return placement == selectedDashboardPartPlacement;
	}

	@Override
	public void addSelectionChangedListener(ISelectionChangedListener listener) {
		// Preconditions.checkNotNull(listener, "Selection changed listener must not be null!");
		
		listeners.add(listener);
	}

	@Override
	public void removeSelectionChangedListener(ISelectionChangedListener listener) {
		listeners.remove(listener);
	}

	public void setSelection(ISelection selection, boolean fireListeners ) {
		Optional<DashboardPartPlacement> optDashboardPart = getSelectedDashboardPartPlacement(selection);
		if( optDashboardPart.isPresent() ) {
			selectedDashboardPartPlacement = optDashboardPart.get();
			this.selection = (IStructuredSelection)selection;

			if( fireListeners ) {
				fireListeners(selection);
			}
		}
	}
	
	private Optional<DashboardPartPlacement> getSelectedDashboardPartPlacement(ISelection selection) {
		if (selection instanceof IStructuredSelection && !selection.isEmpty()) {
			final IStructuredSelection structSelection = (IStructuredSelection) selection;
			final Object selectedObject = structSelection.getFirstElement();
			if (selectedObject instanceof DashboardPartPlacement) {
				return Optional.of((DashboardPartPlacement) selectedObject);
			}
		}
		return Optional.absent();
	}
	
	@Override
	public void setSelection(ISelection selection) {
		setSelection(selection, true);
	}

	private void fireListeners(ISelection selection) {
		for (final ISelectionChangedListener listener : listeners) {
			if (listener != null) {
				listener.selectionChanged(new SelectionChangedEvent(this, selection));
			}
		}
	}
	
	public void setSelection( DashboardPartPlacement placement ) {
		if( placement == null ) {
			clearSelection();
		} else {
			selectedDashboardPartPlacement = placement;
			selection = new StructuredSelection(placement);
			fireListeners(selection);
		}
	}
	
	public void clearSelection() {
		selectedDashboardPartPlacement = null;
		selection = StructuredSelection.EMPTY;
		
		fireListeners(selection);
	}
}
