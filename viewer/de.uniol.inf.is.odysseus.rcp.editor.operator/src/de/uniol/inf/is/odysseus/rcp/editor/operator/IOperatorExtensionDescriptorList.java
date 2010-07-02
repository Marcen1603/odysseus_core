package de.uniol.inf.is.odysseus.rcp.editor.operator;

import java.util.Collection;
import java.util.List;

public interface IOperatorExtensionDescriptorList extends List<IOperatorExtensionDescriptor>{

	public Collection<IOperatorExtensionDescriptor> getGroup( String groupName ); 
}
