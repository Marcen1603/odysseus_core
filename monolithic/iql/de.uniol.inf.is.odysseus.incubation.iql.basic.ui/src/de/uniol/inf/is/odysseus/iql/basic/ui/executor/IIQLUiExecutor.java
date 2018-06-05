package de.uniol.inf.is.odysseus.iql.basic.ui.executor;

import de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLModel;
import de.uniol.inf.is.odysseus.iql.basic.executor.IIQLExecutor;



public interface IIQLUiExecutor extends IIQLExecutor {	
	public void parse(IQLModel model);

}
