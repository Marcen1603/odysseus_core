package de.uniol.inf.is.odysseus.rcp.editor.parameters.editing;

public abstract class EditableColumnDefinition<T> extends SimpleColumnDefinition<T> {

	public EditableColumnDefinition(String title) {
		super(title);
	}

	protected abstract void setValue( T element, String value );
}
