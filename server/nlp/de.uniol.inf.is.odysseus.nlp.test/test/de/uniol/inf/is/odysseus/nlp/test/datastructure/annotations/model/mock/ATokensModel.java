package de.uniol.inf.is.odysseus.nlp.test.datastructure.annotations.model.mock;

import java.util.Map;

import de.uniol.inf.is.odysseus.core.collection.Option;
import de.uniol.inf.is.odysseus.nlp.datastructure.annotations.Annotated;
import de.uniol.inf.is.odysseus.nlp.datastructure.annotations.implementations.Tokens;
import de.uniol.inf.is.odysseus.nlp.datastructure.annotations.model.AnnotationModel;

public class ATokensModel extends AnnotationModel<Tokens>{
	public ATokensModel(){
		super();
	}
	
	public ATokensModel(Map<String, Option> configuration){
		super(configuration);
	}
	
	@Override
	public void annotate(Annotated annotated) {
		
	}

	@Override
	public String identifier() {
		return Tokens.class.getSimpleName().toLowerCase();
	}

	@Override
	protected void addRequirements() {
		
	}

}
