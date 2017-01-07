package de.uniol.inf.is.odysseus.nlp.toolkits;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import de.uniol.inf.is.odysseus.core.collection.Option;
import de.uniol.inf.is.odysseus.nlp.toolkits.annotations.IAnnotation;
import de.uniol.inf.is.odysseus.nlp.toolkits.annotations.SentenceAnnotation;
import de.uniol.inf.is.odysseus.nlp.toolkits.annotations.TokenAnnotation;
import opennlp.tools.sentdetect.SentenceDetector;
import opennlp.tools.sentdetect.SentenceDetectorME;
import opennlp.tools.sentdetect.SentenceModel;
import opennlp.tools.tokenize.Tokenizer;
import opennlp.tools.tokenize.TokenizerME;
import opennlp.tools.tokenize.TokenizerModel;


public class OpenNLPToolkit extends NLPToolkit{

	public OpenNLPToolkit(List<String> information, HashMap<String, Option> configuration) throws NLPInformationNotSupportedException, NLPModelNotFoundException {
		super(information, configuration, Arrays.asList(
				SentenceAnnotation.class, 
				TokenAnnotation.class
				));
	}
	
	@Override
	void annotateSentences(Annotation annotation){
		SentenceDetector detector = (SentenceDetector) models.get(SentenceAnnotation.class);
		String[] sentences = detector.sentDetect(annotation.getText());
		annotation.setAnnotation(SentenceAnnotation.class, new SentenceAnnotation(sentences));
	
	}

	
	@Override
	void annotateTokens(Annotation annotation){
		Tokenizer tokenizer = (Tokenizer) models.get(TokenAnnotation.class);
		tokenizer.tokenize(annotation.getText());
		SentenceAnnotation sentences = (SentenceAnnotation) annotation.getAnnotation(SentenceAnnotation.class);
		List<String[]> tokens = new ArrayList<>();
		for(int sentence = 0; sentence < sentences.getSentences().length; sentence++){
			String[] detected = tokenizer.tokenize(sentences.getSentences()[sentence]);
			tokens.add(detected);
		}
		TokenAnnotation tokenAnnotation = new TokenAnnotation(tokens);
		annotation.setAnnotation(TokenAnnotation.class, tokenAnnotation);
		//String[] sentences = detector.sentDetect(annotation.getText());
	}


	@Override
	public void init() throws NLPInformationNotSupportedException, NLPModelNotFoundException {
		Class<? extends IAnnotation> highestAnnotation = getHighestAnnotation();
		
		if(highestAnnotation != null){
			try {
				switch((String)highestAnnotation.getField("NAME").get(null)){
					case "token":
						loadModel(TokenAnnotation.class);
					case "sentence":
						loadModel(SentenceAnnotation.class);
						break;
					default:
						throw new NLPInformationNotSupportedException();
				}
			} catch (IllegalArgumentException | IllegalAccessException | NoSuchFieldException | SecurityException ignored) {
			}
		}
	}

	private void loadModel(Class<? extends IAnnotation> clazz) throws NLPModelNotFoundException {
		String name = "";
			try (InputStream modelIn = getModelAsStream(clazz)) {
				name = (String)clazz.getField("NAME").get(null);
				switch(name){
				case "token":
					models.put(clazz, new TokenizerME(new TokenizerModel(modelIn)));
					break;
				case "sentence":
					models.put(clazz, new SentenceDetectorME(new SentenceModel(modelIn)));
					break;
				}
			} catch (IllegalArgumentException | IllegalAccessException | NoSuchFieldException | SecurityException
					| IOException e) {
				throw new NLPModelNotFoundException(name);
			}
			
		
	}


}
