package de.uniol.inf.is.odysseus.viewer.swt.select;

import java.util.Collection;


public interface ISelectListener<T> {

	public void selectObject( ISelector<T> sender, Collection<? extends T> selected );
	public void unselectObject( ISelector<T> sender, Collection<? extends T> unselected );
	
}
