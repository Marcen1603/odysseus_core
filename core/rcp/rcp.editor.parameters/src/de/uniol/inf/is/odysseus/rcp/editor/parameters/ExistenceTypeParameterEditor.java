package de.uniol.inf.is.odysseus.rcp.editor.parameters;


public class ExistenceTypeParameterEditor extends ReadonlyComboParameterEditor {
	
	@Override
	protected String[] getList() {
		// es gibt nur zwei Einträge für den Existence-Operator
		return new String[] { "EXISTS", "NOT EXISTS"};
	}
}
