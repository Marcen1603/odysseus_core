package de.uniol.inf.is.odysseus.visualquerylanguage.model.query;

import de.uniol.inf.is.odysseus.visualquerylanguage.model.operators.INodeContent;

public interface IQueryStartListener<C extends INodeContent>{
	
	public void queryStarted(IQuery<C> query);
	
}
