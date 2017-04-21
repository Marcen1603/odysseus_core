package org.apache.opennlp.algorithms;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Map;

import org.apache.opennlp.OpenNLPTrainableModel;

import de.uniol.inf.is.odysseus.core.collection.Option;
import de.uniol.inf.is.odysseus.core.collection.OptionMap;
import de.uniol.inf.is.odysseus.nlp.datastructure.annotations.Annotated;
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

public class SentenceModel extends OpenNLPTrainableModel<Sentences>{
	private SentenceDetector detector;

	private opennlp.tools.sentdetect.SentenceModel sentenceModel;
	
	public SentenceModel(){
		super();
	}

	public SentenceModel(Map<String, Option> configuration) throws NLPModelNotFoundException, NLPException{
		super(configuration);
	}
	

	@Override
	public void annotate(Annotated annotated) {
		opennlp.tools.util.Span[] sentences = getDetector().sentPosDetect(annotated.getText());
		Span[] spans = new Span[sentences.length];
		for(int i = 0; i < sentences.length; i++){
			try {
				String text = annotated.getText().substring(sentences[i].getStart(), sentences[i].getEnd());
				spans[i] = new Span(sentences[i].getStart(), sentences[i].getEnd(), text);
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
		return Sentences.NAME;
	}

	@Override
	protected void addRequirements() {
	}

	@Override
	public void train(String languageCode, File file, String charSet, OptionMap options) {
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


	@Override
	protected void store(OutputStream modelOut) throws IOException {
		sentenceModel.serialize(modelOut);
	}
	/*@Override
	public void makeSerializable() {
		detector = null;
	}*/
	
}
