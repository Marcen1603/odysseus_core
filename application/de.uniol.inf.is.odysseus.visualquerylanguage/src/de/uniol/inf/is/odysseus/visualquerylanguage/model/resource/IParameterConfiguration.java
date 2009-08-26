package de.uniol.inf.is.odysseus.visualquerylanguage.model.resource;

import java.util.Collection;

import de.uniol.inf.is.odysseus.visualquerylanguage.model.operators.DefaultPipeContent;
import de.uniol.inf.is.odysseus.visualquerylanguage.model.operators.DefaultSinkContent;
import de.uniol.inf.is.odysseus.visualquerylanguage.model.operators.DefaultSourceContent;

public interface IParameterConfiguration {
	
	public Collection<DefaultSourceContent> getSources();
	public Collection<DefaultSinkContent> getSinks();
	public Collection<DefaultPipeContent> getPipes();
	
}
