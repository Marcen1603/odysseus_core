package org.apache.opennlp.algorithms;

import java.io.File;
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
import de.uniol.inf.is.odysseus.nlp.datastructure.exception.NLPTrainingFailedException;
import opennlp.tools.dictionary.Dictionary;
import opennlp.tools.sentdetect.SentenceDetector;
import opennlp.tools.sentdetect.SentenceDetectorFactory;
import opennlp.tools.sentdetect.SentenceDetectorME;
import opennlp.tools.sentdetect.SentenceSample;
import opennlp.tools.sentdetect.SentenceSampleStream;
import opennlp.tools.util.MarkableFileInputStreamFactory;
import opennlp.tools.util.ObjectStream;
import opennlp.tools.util.PlainTextByLineStream;
import opennlp.tools.util.TrainingParameters;

public class SentenceModel extends OpenNLPModel<Sentences>{
	private static final long serialVersionUID = 269539119735625215L;
	
	private SentenceDetector detector;

	private opennlp.tools.sentdetect.SentenceModel sentenceModel;
	
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
		opennlp.tools.util.Span[] sentences = getDetector().sentPosDetect(annotated.getText());
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

	private SentenceDetector getDetector() {
		if(detector == null)
			detector = new SentenceDetectorME(sentenceModel);
		return detector;
	}

	@Override
	protected void load(InputStream... stream) throws IOException {
		sentenceModel = new opennlp.tools.sentdetect.SentenceModel(stream[0]);
		getDetector();
	}
	

	@Override
	public String identifier() {
		return Sentences.class.getSimpleName().toLowerCase();
	}

	@Override
	protected void addRequirements() {
	}

	@Override
	public void train(String languageCode, File file, String charSet) {
		try(ObjectStream<String> lineStream = new PlainTextByLineStream(new MarkableFileInputStreamFactory(file), charSet);
				ObjectStream<SentenceSample> sampleStream = new SentenceSampleStream(lineStream)) {
				char[] eosCharacters = {'!', '?', '.', ':'};
				SentenceDetectorFactory factory = new SentenceDetectorFactory(languageCode, true, new Dictionary(), eosCharacters);
				sentenceModel = SentenceDetectorME.train(languageCode, sampleStream, factory,  TrainingParameters.defaultParams());
				detector = new SentenceDetectorME(sentenceModel);
			} catch (IOException e) {
				throw new NLPTrainingFailedException(e.getMessage());
			}
	}

	/*@Override
	public void makeSerializable() {
		detector = null;
	}*/
	
}
