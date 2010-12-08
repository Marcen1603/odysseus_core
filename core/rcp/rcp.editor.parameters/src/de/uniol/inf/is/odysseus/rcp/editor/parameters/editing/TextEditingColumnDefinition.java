package de.uniol.inf.is.odysseus.rcp.editor.parameters.editing;

import org.eclipse.jface.viewers.TableViewer;

public abstract class TextEditingColumnDefinition<T> extends EditableColumnDefinition<T> {

	public TextEditingColumnDefinition(String title) {
		super(title);
	}

	@Override
	protected AbstractEditingSupport createEditingSupport(TableViewer viewer) {
		return new TextEditingSupport(viewer) {

			@SuppressWarnings("unchecked")
			@Override
			protected void doSetValue(Object element, Object value) {
				TextEditingColumnDefinition.this.setValue( (T)element, value.toString());
			}

			@SuppressWarnings("unchecked")
			@Override
			protected Object doGetValue(Object element) {
				return getStringValue((T)element);
			}
			
		};
	}

}
