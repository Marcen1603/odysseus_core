package de.uniol.inf.is.odysseus.rcp.viewer.view.select;

import java.util.Collection;


public interface ISelectListener<T> {

	public void selectObject( ISelector<T> sender, Collection<? extends T> selected );
	public void unselectObject( ISelector<T> sender, Collection<? extends T> unselected );
	
}
