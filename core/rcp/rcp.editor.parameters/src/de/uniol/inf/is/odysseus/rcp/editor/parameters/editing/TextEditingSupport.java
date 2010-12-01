package de.uniol.inf.is.odysseus.rcp.editor.parameters.editing;

import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TextCellEditor;

public abstract class TextEditingSupport extends AbstractEditingSupport{

	public TextEditingSupport(TableViewer viewer) {
		super(viewer);
	}

	@Override
	protected CellEditor createCellEditor( TableViewer viewer ) {
		return new TextCellEditor(viewer.getTable());
	}
}
