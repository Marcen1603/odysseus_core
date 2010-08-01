package de.uniol.inf.is.odysseus.rcp.editor.parameter;

import org.eclipse.swt.widgets.Composite;

import de.uniol.inf.is.odysseus.logicaloperator.builder.IOperatorBuilder;
import de.uniol.inf.is.odysseus.logicaloperator.builder.IParameter;

public interface IParameterEditor {

	public void init( IOperatorBuilder builder, IParameter<?> parameter );
	public void createControl( Composite parent );
	public void close();
	
}
