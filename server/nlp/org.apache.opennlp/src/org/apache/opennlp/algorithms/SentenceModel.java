package org.apache.opennlp.algorithms;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;

import org.apache.opennlp.OpenNLPModel;

import de.uniol.inf.is.odysseus.core.collection.Option;
import de.uniol.inf.is.odysseus.nlp.datastructure.annotations.Annotated;
import de.uniol.inf.is.odysseus.nlp.datastructure.annotations.IAnnotation;
import de.uniol.inf.is.odysseus.nlp.datastructure.annotations.Span;
import de.uniol.inf.is.odysseus.nlp.datastructure.annotations.implementations.Sentences;
import de.uniol.inf.is.odysseus.nlp.datastructure.exception.InvalidSpanException;
import de.uniol.inf.is.odysseus.nlp.datastructure.exception.NLPException;
import de.uniol.inf.is.odysseus.nlp.datastructure.exception.NLPModelNotFoundException;
import opennlp.tools.sentdetect.SentenceDetector;
import opennlp.tools.sentdetect.SentenceDetectorME;

public class SentenceModel extends OpenNLPModel<Sentences>{
	private static final long serialVersionUID = 269539119735625215L;
	
	private SentenceDetector detector;
	
	public SentenceModel(){
		super();
	}

	public SentenceModel(HashMap<String, Option> configuration) throws NLPModelNotFoundException, NLPException{
		super(configuration);
	}
	
	@Override
	public Class<? extends IAnnotation> type() {
		return Sentences.class;
	}

	@Override
	public void annotate(Annotated annotated) {
		opennlp.tools.util.Span[] sentences = detector.sentPosDetect(annotated.getText());
		Span[] spans = new Span[sentences.length];
		for(int i = 0; i < sentences.length; i++){
			try {
				spans[i] = new Span(sentences[i].getStart(), sentences[i].getEnd());
			} catch (InvalidSpanException ignored) {
				//never thrown in OpenNLP sentence detection
			}
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
	protected void addRequirements() {
	}
	
}
