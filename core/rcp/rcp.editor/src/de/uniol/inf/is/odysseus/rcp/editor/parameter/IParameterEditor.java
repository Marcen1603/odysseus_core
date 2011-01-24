package de.uniol.inf.is.odysseus.rcp.editor.parameter;

import org.eclipse.swt.widgets.Composite;

import de.uniol.inf.is.odysseus.logicaloperator.builder.IOperatorBuilder;
import de.uniol.inf.is.odysseus.logicaloperator.builder.IParameter;
import de.uniol.inf.is.odysseus.rcp.editor.views.IParameterView;

public interface IParameterEditor {

	public void init( IOperatorBuilder builder, IParameter<?> parameter, IParameterView view );
	public void createControl( Composite parent );
	public void close();
	
}
