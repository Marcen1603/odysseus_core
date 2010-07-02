package de.uniol.inf.is.odysseus.rcp.editor.operatorview.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

import de.uniol.inf.is.odysseus.rcp.editor.operator.IOperatorExtensionDescriptor;

public class OperatorGroup {

	private String name;
	private Collection<IOperatorExtensionDescriptor> extensions = new ArrayList<IOperatorExtensionDescriptor>();
	
	public OperatorGroup( String name ) {
		this.name = name;
	}
	
	public String getName() {
		return name;
	}
	
	public void addExtension( IOperatorExtensionDescriptor desc ) {
		extensions.add(desc);
	}
	
	public Collection<IOperatorExtensionDescriptor> getExtensions() {
		return Collections.unmodifiableCollection(extensions);
	}
}
