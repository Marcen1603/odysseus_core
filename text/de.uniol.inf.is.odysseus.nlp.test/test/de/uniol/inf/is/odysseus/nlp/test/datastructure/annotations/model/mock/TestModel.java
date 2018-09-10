package de.uniol.inf.is.odysseus.nlp.test.datastructure.annotations.model.mock;

import java.util.HashMap;
import java.util.Map;

import de.uniol.inf.is.odysseus.core.collection.Option;
import de.uniol.inf.is.odysseus.nlp.datastructure.annotations.Annotated;
import de.uniol.inf.is.odysseus.nlp.datastructure.annotations.model.AnnotationModel;
import de.uniol.inf.is.odysseus.nlp.datastructure.exception.NLPException;
import de.uniol.inf.is.odysseus.nlp.datastructure.exception.NLPModelNotFoundException;
import de.uniol.inf.is.odysseus.nlp.test.datastructure.annotations.mock.TestAnnotation;

public class TestModel extends AnnotationModel<TestAnnotation>{
	private boolean exchanged = false;

	public TestModel(){
		super();
	}

	public TestModel(Map<String, Option> configuration) throws NLPModelNotFoundException, NLPException{
		super(configuration);
	}

	public TestModel(boolean exchanged, HashMap<String, Option> configuration) throws NLPModelNotFoundException, NLPException{
		super(configuration);
		this.exchanged = exchanged;
	}
	
	@Override
	public void annotate(Annotated annotated) {
		annotated.put(new TestAnnotation());
	}

	@Override
	public String identifier() {
		return TestAnnotation.class.getSimpleName().toLowerCase();
	}

	@Override
	protected void addRequirements() {
		
	}

	public boolean isExchanged() {
		return exchanged;
	}

}