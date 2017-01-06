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
import de.uniol.inf.is.odysseus.nlp.toolkits.bundle.classannotation.NLPToolkitWrapper;
import opennlp.tools.sentdetect.SentenceDetector;
import opennlp.tools.sentdetect.SentenceDetectorME;
import opennlp.tools.sentdetect.SentenceModel;
import opennlp.tools.tokenize.Tokenizer;
import opennlp.tools.tokenize.TokenizerME;
import opennlp.tools.tokenize.TokenizerModel;


@NLPToolkitWrapper(name="opennlp")
public class OpenNLPToolkit extends NLPToolkit{
	

	private List<Class<? extends IAnnotation>> internalPipeline = Arrays.asList(
			SentenceAnnotation.class, 
			TokenAnnotation.class
			);
	

	public OpenNLPToolkit(List<String> information, HashMap<String, Option> configuration) {
		super(information, configuration);
	}
	
	//TODO ERROR HANDLING (Model not found?)
	@Override
	public Annotation annotate(String text) {
		Annotation annotation = new Annotation(text);
		Class<? extends IAnnotation> highestAnnotation = getHighestAnnotation();
		
		if(highestAnnotation != null){
			try {
				switch((String)highestAnnotation.getField("NAME").get(null)){
				case "sentence":
					annotateSentences(annotation);
				case "token":
					annotateSentences(annotation);
					annotateTokens(annotation);
				}
			} catch (IllegalArgumentException | IllegalAccessException | NoSuchFieldException | SecurityException | IOException e) {
				e.printStackTrace();
			}
		}
		return annotation;
	}

	private void annotateSentences(Annotation annotation) throws IOException{
		try (
				 InputStream sentenceModelIn = getModelAsStream(SentenceAnnotation.class);
	         ){
			SentenceModel sentenceModel = new SentenceModel(sentenceModelIn);
			SentenceDetector sentenceDetector = new SentenceDetectorME(sentenceModel);
			
			String[] sentences = sentenceDetector.sentDetect(annotation.getText());
			annotation.setAnnotation(SentenceAnnotation.class, new SentenceAnnotation(sentences));
		 } catch (IOException exception) {
			 throw exception;
		 }
	}
	
	private void annotateTokens(Annotation annotation) throws IOException{
		try (
				 InputStream modelIn = getModelAsStream(TokenAnnotation.class);
	         ){
			TokenizerModel model = new TokenizerModel(modelIn);
			Tokenizer detector = new TokenizerME(model);
			detector.tokenize(annotation.getText());
			SentenceAnnotation sentences = (SentenceAnnotation) annotation.getAnnotation(SentenceAnnotation.class);
			List<String[]> tokens = new ArrayList<>();
			for(int sentence = 0; sentence < sentences.getSentences().length; sentence++){
				String[] detected = detector.tokenize(sentences.getSentences()[sentence]);
				tokens.add(detected);
			}
			TokenAnnotation tokenAnnotation = new TokenAnnotation(tokens);
			annotation.setAnnotation(TokenAnnotation.class, tokenAnnotation);
			//String[] sentences = detector.sentDetect(annotation.getText());
		 } catch (IOException exception) {
			 throw exception;
		 }
	}

	@Override
	Class<? extends IAnnotation> getHighestAnnotation() {
		List<Class<? extends IAnnotation>> pipeline = new ArrayList<>(internalPipeline);
		Collections.reverse(pipeline);
		for(Class<? extends IAnnotation> clazz : pipeline){
			try {
				String name = (String) clazz.getField("NAME").get(null);
				for(String inclName : information){
					if(inclName.equals(name)){
						return clazz;
					}
				}
			} catch (IllegalArgumentException | IllegalAccessException | NoSuchFieldException | SecurityException  e) {
				e.printStackTrace();
			}
		}
		return null;
	}


}
