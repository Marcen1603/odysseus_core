package org.apache.opennlp.algorithms;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;

import org.apache.opennlp.OpenNLPModel;

import de.uniol.inf.is.odysseus.core.collection.Option;
import de.uniol.inf.is.odysseus.nlp.datastructure.Annotated;
import de.uniol.inf.is.odysseus.nlp.datastructure.IAnnotation;
import de.uniol.inf.is.odysseus.nlp.datastructure.Span;
import de.uniol.inf.is.odysseus.nlp.datastructure.annotations.Sentences;
import de.uniol.inf.is.odysseus.nlp.datastructure.exception.NLPException;
import de.uniol.inf.is.odysseus.nlp.datastructure.exception.NLPModelNotFoundException;
import opennlp.tools.sentdetect.SentenceDetector;
import opennlp.tools.sentdetect.SentenceDetectorME;

public class SentenceModel extends OpenNLPModel<Sentences>{
	private SentenceDetector detector;
	
	public SentenceModel(){
		super();
	}

	public SentenceModel(HashMap<String, Option> configuration) throws NLPModelNotFoundException, NLPException{
		super(configuration);
	}
	
	protected SentenceModel(InputStream stream) throws NLPException  {
		super(stream);
	}
	
	@Override
	public Class<? extends IAnnotation> type() {
		return Sentences.class;
	}

	@Override
	public void annotate(Annotated annotated) {
		opennlp.tools.util.Span[] sentences = detector.sentPosDetect(annotated.text);
		Span[] spans = new Span[sentences.length];
		for(int i = 0; i < sentences.length; i++){
			spans[i] = new Span(sentences[i].getStart(), sentences[i].getEnd());
		}
		Sentences annotation = new Sentences(spans);
		annotated.put(annotation);
	}

	@Override
	protected void load(InputStream stream) throws IOException {
		detector = new SentenceDetectorME(new opennlp.tools.sentdetect.SentenceModel(stream));
	}
	

	@Override
	public String identifier() {
		return Sentences.class.getSimpleName().toLowerCase();
	}

	@Override
	public void addRequirements() {
	}
	
}
