package de.uniol.inf.is.odysseus.nlp.test.datastructure.annotations.model.mock;

import java.util.Map;

import de.uniol.inf.is.odysseus.core.collection.Option;
import de.uniol.inf.is.odysseus.nlp.datastructure.annotations.Annotated;
import de.uniol.inf.is.odysseus.nlp.datastructure.annotations.model.AnnotationModel;
import de.uniol.inf.is.odysseus.nlp.datastructure.exception.NLPException;
import de.uniol.inf.is.odysseus.nlp.datastructure.exception.NLPModelNotFoundException;
import de.uniol.inf.is.odysseus.nlp.test.datastructure.annotations.mock.AnotherTestAnnotation;

public class AnotherTestModel extends AnnotationModel<AnotherTestAnnotation>{
	public AnotherTestModel(){
		super();
	}

	public AnotherTestModel(Map<String, Option> configuration) throws NLPModelNotFoundException, NLPException{
		super(configuration);
	}


	@Override
	public void annotate(Annotated annotated) {
		annotated.put(new AnotherTestAnnotation());
	}

	@Override
	public String identifier() {
		return AnotherTestAnnotation.class.getSimpleName().toLowerCase();
	}

	@Override
	protected void addRequirements() {
		prerequisites.add(TestModel.class);
	}		
}
