package de.uniol.inf.is.odysseus.rcp.editor.parameters.editing;

import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.EditingSupport;
import org.eclipse.jface.viewers.TableViewer;

public abstract class AbstractEditingSupport extends EditingSupport {
	private CellEditor editor;

	public AbstractEditingSupport(TableViewer viewer) {
		super(viewer);
		this.editor = createCellEditor(viewer);
	}
	
	@Override
	protected boolean canEdit(Object element) {
		return true;
	}

	@Override
	protected CellEditor getCellEditor(Object element) {
		return editor;
	}
	
	protected CellEditor getCellEditor() {
		return getCellEditor(null);
	}

	@Override
	protected void setValue(Object element, Object value) {
		doSetValue(element, value);
		getViewer().update(element, null);
	}
	
	@Override
	protected Object getValue(Object element) {
		return doGetValue(element);
	}
	
	protected abstract void doSetValue(Object element, Object value);
	protected abstract CellEditor createCellEditor( TableViewer viewer );
	protected abstract Object doGetValue( Object element );
}
