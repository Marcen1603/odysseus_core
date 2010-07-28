package de.uniol.inf.is.odysseus.rcp.editor.operator.impl;

import java.util.ArrayList;
import java.util.Collection;

import de.uniol.inf.is.odysseus.rcp.editor.operator.IOperatorExtensionDescriptor;
import de.uniol.inf.is.odysseus.rcp.editor.operator.IOperatorExtensionDescriptorList;

public class OperatorExtensionDescriptorList extends ArrayList<IOperatorExtensionDescriptor>implements IOperatorExtensionDescriptorList {

	private static final long serialVersionUID = 2453870854116575087L;

	@Override
	public Collection<IOperatorExtensionDescriptor> getGroup( String groupName ) {
		Collection<IOperatorExtensionDescriptor> result = new ArrayList<IOperatorExtensionDescriptor>();
		
		for( IOperatorExtensionDescriptor d : this ) {
			if( d.getGroup().equals(groupName))
				result.add(d);
		}
		return result;
	}

}
