package de.uniol.inf.is.odysseus.visualquerylanguage.controler;

import java.util.Collection;

import de.uniol.inf.is.odysseus.visualquerylanguage.model.query.IQuery;

public interface IQueryController<C extends IQuery<?>> {
	
	public void addQuery(C query);
	public void removeQuery(C query);
	public C getQueryById(int id);
	public Collection<C> getAllQueries();
	
}
