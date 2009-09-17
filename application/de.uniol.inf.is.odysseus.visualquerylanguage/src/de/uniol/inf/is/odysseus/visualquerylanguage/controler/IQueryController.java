package de.uniol.inf.is.odysseus.visualquerylanguage.controler;

import java.lang.reflect.InvocationTargetException;
import java.util.Collection;

import org.eclipse.swt.widgets.Control;

import de.uniol.inf.is.odysseus.planmanagement.executor.IExecutor;
import de.uniol.inf.is.odysseus.visualquerylanguage.model.query.IQuery;

public interface IQueryController<L extends IQuery<?>> {
	
	public void addQuery(L query);
	public void removeQuery(L query);
	public L getQueryById(int id);
	public Collection<L> getAllQueries();
	public void launchQuery(Control control, IExecutor executor) throws SecurityException, IllegalArgumentException, ClassNotFoundException, NoSuchMethodException, InstantiationException, IllegalAccessException, InvocationTargetException;
}
