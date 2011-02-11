package de.uniol.inf.is.odysseus.rcp.benchmarker.gui.view;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IPersistableElement;

/**
 * Diese Klasse beinhaltet den Input für den Sucheditor
 * 
 * @author Stefanie Witzke
 *
 */
public class SearchEditorInput implements IEditorInput {

	private final int id;

	public SearchEditorInput(int id) {
		this.id = id;
	}

	public SearchEditorInput() {
		this.id = 0;
	}

	public int getId() {
		return id;
	}

	@SuppressWarnings("rawtypes")
	@Override
	public Object getAdapter(Class adapter) {
		return null;
	}

	@Override
	public boolean exists() {
		return true;
	}

	@Override
	public ImageDescriptor getImageDescriptor() {
		return null;
	}

	@Override
	public String getName() {
		return String.valueOf(id);
	}

	@Override
	public IPersistableElement getPersistable() {
		return null;
	}

	@Override
	public String getToolTipText() {
		return " ";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + id;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		SearchEditorInput other = (SearchEditorInput) obj;
		if (id != other.id)
			return false;
		return true;
	}

}
