package org.apache.opennlp;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

import de.uniol.inf.is.odysseus.core.collection.Option;
import de.uniol.inf.is.odysseus.nlp.toolkits.Annotation;
import de.uniol.inf.is.odysseus.nlp.toolkits.NLPToolkit;
import de.uniol.inf.is.odysseus.nlp.toolkits.annotations.IAnnotation;
import de.uniol.inf.is.odysseus.nlp.toolkits.annotations.NamedEntityAnnotation;
import de.uniol.inf.is.odysseus.nlp.toolkits.annotations.SentenceAnnotation;
import de.uniol.inf.is.odysseus.nlp.toolkits.annotations.TokenAnnotation;
import de.uniol.inf.is.odysseus.nlp.toolkits.exception.NLPInformationNotSupportedException;
import de.uniol.inf.is.odysseus.nlp.toolkits.exception.NLPModelNotFoundException;
import opennlp.tools.namefind.NameFinderME;
import opennlp.tools.namefind.TokenNameFinder;
import opennlp.tools.namefind.TokenNameFinderModel;
import opennlp.tools.sentdetect.SentenceDetector;
import opennlp.tools.sentdetect.SentenceDetectorME;
import opennlp.tools.sentdetect.SentenceModel;
import opennlp.tools.tokenize.Tokenizer;
import opennlp.tools.tokenize.TokenizerME;
import opennlp.tools.tokenize.TokenizerModel;
import opennlp.tools.util.Span;


public class OpenNLPToolkit extends NLPToolkit{

	public OpenNLPToolkit(Set<String> information, HashMap<String, Option> configuration) throws NLPInformationNotSupportedException, NLPModelNotFoundException {
		super(information, configuration, Arrays.asList(
				SentenceAnnotation.class, 
				TokenAnnotation.class,
				NamedEntityAnnotation.class
				));
	}
	
	@Override
	protected void annotateSentences(Annotation annotation){
		SentenceDetector detector = (SentenceDetector) models.get(SentenceAnnotation.class);
		String[] sentences = detector.sentDetect(annotation.getText());
		annotation.setAnnotation(SentenceAnnotation.class, new SentenceAnnotation(sentences));
	
	}

	
	@Override
	protected void annotateTokens(Annotation annotation){
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
	protected void annotateNamedEntities(Annotation annotation){
		TokenNameFinder nameFinder = (TokenNameFinder) models.get(NamedEntityAnnotation.class);
		List<String> namedEntities = new ArrayList<>();
		TokenAnnotation tokenAnnotation = (TokenAnnotation)annotation.getAnnotation(TokenAnnotation.class);;
		List<String[]> sentenceTokens = tokenAnnotation.getTokens();
		for(String[] tokens : sentenceTokens){
			Span[] neSpan = nameFinder.find(tokens);
			for(Span span : neSpan){
				String namedEntity = "";
				for(int i = span.getStart(); i <= span.getEnd() && i < tokens.length; i++){
					namedEntity += tokens[i];
				}
				namedEntities.add(namedEntity);
			}
		}
		NamedEntityAnnotation neAnnotation = new NamedEntityAnnotation(namedEntities);
		annotation.setAnnotation(NamedEntityAnnotation.class, neAnnotation);
	}


	@Override
	public void init() throws NLPInformationNotSupportedException, NLPModelNotFoundException {
		Class<? extends IAnnotation> highestAnnotation = getHighestAnnotation();
		
		if(highestAnnotation != null){
			try {
				switch((String)highestAnnotation.getField("NAME").get(null)){
					case NamedEntityAnnotation.NAME:
						loadModel(NamedEntityAnnotation.class);
					case TokenAnnotation.NAME:
						loadModel(TokenAnnotation.class);
					case SentenceAnnotation.NAME:
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
				case TokenAnnotation.NAME:
					models.put(clazz, new TokenizerME(new TokenizerModel(modelIn)));
					break;
				case SentenceAnnotation.NAME:
					models.put(clazz, new SentenceDetectorME(new SentenceModel(modelIn)));
					break;
				case NamedEntityAnnotation.NAME:
					models.put(clazz, new NameFinderME(new TokenNameFinderModel(modelIn)));
					break;
				default:
					break;
				}
			} catch (IllegalArgumentException | IllegalAccessException | NoSuchFieldException | SecurityException
					| IOException e) {
				throw new NLPModelNotFoundException(name);
			}
			
		
	}

	@Override
	public boolean equals(Object object) {
		// TODO Auto-generated method stub
		return false;
	}


}
