package de.uniol.inf.is.odysseus.visualquerylanguage.controler;

import org.eclipse.swt.widgets.Control;

import de.uniol.inf.is.odysseus.planmanagement.executor.IAdvancedExecutor;
import de.uniol.inf.is.odysseus.visualquerylanguage.ReflectionException;

public interface IQueryController {
	
	public void launchQuery(Control control, IAdvancedExecutor executor) throws ReflectionException;
}
