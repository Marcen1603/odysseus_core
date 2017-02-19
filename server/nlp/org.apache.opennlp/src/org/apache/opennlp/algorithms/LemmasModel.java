package org.apache.opennlp.algorithms;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

import org.apache.opennlp.OpenNLPModel;

import de.uniol.inf.is.odysseus.core.collection.Option;
import de.uniol.inf.is.odysseus.nlp.datastructure.annotations.Annotated;
import de.uniol.inf.is.odysseus.nlp.datastructure.annotations.implementations.Lemmas;
import de.uniol.inf.is.odysseus.nlp.datastructure.annotations.implementations.PartsOfSpeech;
import de.uniol.inf.is.odysseus.nlp.datastructure.annotations.implementations.Tokens;
import de.uniol.inf.is.odysseus.nlp.datastructure.exception.NLPException;
import de.uniol.inf.is.odysseus.nlp.datastructure.exception.NLPModelNotFoundException;
import opennlp.tools.lemmatizer.DictionaryLemmatizer;
import opennlp.tools.lemmatizer.Lemmatizer;
import opennlp.tools.lemmatizer.LemmatizerME;
import opennlp.tools.lemmatizer.LemmatizerModel;

public class LemmasModel extends OpenNLPModel<Lemmas> {
	private static final long serialVersionUID = -1520950014473462548L;
	private LemmatizerModel model;
	private Lemmatizer lemmatizer;
	private boolean isStatistical = false;

	public LemmasModel(){
		super();
	}
	
	public LemmasModel(Map<String, Option> configuration) throws NLPModelNotFoundException, NLPException {
		super(configuration);
		if(configuration.containsKey("model."+identifier()+".type")){
			String type = (String)configuration.get("model."+identifier()+".type").getValue();
			if(type.equals("statistical"))
				isStatistical = true;
		}
	}



	@Override
	public void train(String languageCode, File file, String charSet) {
		// TODO Auto-generated method stub
	}

	@Override
	public void annotate(Annotated annotated) {
		Tokens tokensAnnotation = (Tokens)annotated.getAnnotations().get(Tokens.class.getSimpleName().toLowerCase());
		String[] tokens = tokensAnnotation.getTokens();
		PartsOfSpeech posAnnotation = (PartsOfSpeech)annotated.getAnnotations().get(PartsOfSpeech.class.getSimpleName().toLowerCase());
		String[] tags = posAnnotation.getTags();
		String[] lemmas = getLemmatizer().lemmatize(tokens, tags);
		annotated.put(new Lemmas(lemmas));
	}

	@Override
	public String identifier() {
		return Lemmas.class.getSimpleName().toLowerCase();
	}

	@Override
	protected void load(InputStream... stream) throws IOException {
		if(isStatistical){
			model = new LemmatizerModel(stream[0]);		
			getLemmatizer();
		}else{
			lemmatizer = new DictionaryLemmatizer(stream[0]);
		}
	}

	@Override
	protected void addRequirements() {
		prerequisites.add(PartsOfSpeechModel.class);
	}

	private Lemmatizer getLemmatizer(){
		if(lemmatizer==null){
			lemmatizer = new LemmatizerME(model);
		}
		return lemmatizer;
	}
	
}
