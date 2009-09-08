package de.uniol.inf.is.odysseus.visualquerylanguage.controler;

import java.util.Collection;

import de.uniol.inf.is.odysseus.visualquerylanguage.model.query.IQuery;

public interface IQueryController<L extends IQuery<?>> {
	
	public void addQuery(L query);
	public void removeQuery(L query);
	public L getQueryById(int id);
	public Collection<L> getAllQueries();
}
