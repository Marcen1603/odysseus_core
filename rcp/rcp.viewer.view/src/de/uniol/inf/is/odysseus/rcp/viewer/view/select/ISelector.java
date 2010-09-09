package de.uniol.inf.is.odysseus.rcp.viewer.view.select;

import java.util.Collection;


public interface ISelector<T> {

	public void select( T node );
	public void select( Collection<? extends T> nodes );
	
	public void unselect( T node );
	public void unselect( Collection<? extends T> nodes );
	public void unselectAll();
	
	public Collection<T> getSelected();
	public int getSelectionCount();
	
	public boolean isSelected( T node );

	public void addSelectListener( ISelectListener<T> listener );
	public void removeSelectListener( ISelectListener<T> listener );
	public void notifySelectListener( ISelector<T> sender, Collection<? extends T> selected );
	public void notifyUnselectListener( ISelector<T> sender, Collection<? extends T> unselected );
}
