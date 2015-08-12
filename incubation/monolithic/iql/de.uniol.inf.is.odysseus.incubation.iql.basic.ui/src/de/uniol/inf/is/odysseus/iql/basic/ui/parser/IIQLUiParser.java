package de.uniol.inf.is.odysseus.iql.basic.ui.parser;

import org.eclipse.core.resources.IProject;
import de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLModel;



public interface IIQLUiParser {	
	public void parse(IQLModel model, IProject project);

}
