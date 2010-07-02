package de.uniol.inf.is.odysseus.rcp.editor.operator;

public interface IOperatorExtensionDescriptor {
	
	public String getID();
	public String getLabel();
	public String getGroup();
	public ILogicalOperatorExtension getExtensionClass();
}
