package de.uniol.inf.is.odysseus.nlp.test.datastructure.pipeline.mock;

import java.util.List;
import java.util.Map;

import de.uniol.inf.is.odysseus.core.collection.Option;
import de.uniol.inf.is.odysseus.nlp.datastructure.annotations.IAnnotation;
import de.uniol.inf.is.odysseus.nlp.datastructure.annotations.model.AnnotationModel;
import de.uniol.inf.is.odysseus.nlp.datastructure.exception.NLPException;
import de.uniol.inf.is.odysseus.nlp.datastructure.pipeline.Pipeline;
import de.uniol.inf.is.odysseus.nlp.test.datastructure.annotations.model.mock.AnotherTestModel;
import de.uniol.inf.is.odysseus.nlp.test.datastructure.annotations.model.mock.TestModel;

public class TestPipeline extends Pipeline{

	public TestPipeline(List<String> models, Map<String, Option> configuration) throws NLPException {
		super(models, configuration);
	}


	public TestPipeline() {
		super();
	}


	@Override
	protected void configure() {	
		algorithms.add(TestModel.class);
		algorithms.add(AnotherTestModel.class);
	}

	@Override
	protected void configure(List<String> information, Map<String, Option> configuration) {	
	}
	
	
	public List<AnnotationModel<? extends IAnnotation>> getPipeline(){
		return this.pipeline;
	}
	
}
