package de.uniol.inf.is.odysseus.rcp.editor.parameters.editing;

import org.eclipse.jface.viewers.TableViewer;

public abstract class ComboEditingColumnDefinition<T> extends EditableColumnDefinition<T> {

	private String[] items;
	
	public ComboEditingColumnDefinition(String title, String[] items) {
		super(title);
		setItems(items);
	}

	@Override
	protected AbstractEditingSupport createEditingSupport(TableViewer viewer) {
		return new ComboEditingSupport(viewer, getItems()) {

			@SuppressWarnings("unchecked")
			@Override
			protected void doSetValue(Object element, Object value) {
				ComboEditingColumnDefinition.this.setValue((T)element, value.toString());
			}

			@SuppressWarnings("unchecked")
			@Override
			protected Object doGetValue(Object element) {
				return getStringValue((T)element);
			}
			
		};
	}
	
	public String[] getItems() {
		return items;
	}
	
	private void setItems( String[] items ) {
		if( items == null || items.length == 0)
			throw new IllegalArgumentException("items is invalid");
		this.items = items;
	}
}
