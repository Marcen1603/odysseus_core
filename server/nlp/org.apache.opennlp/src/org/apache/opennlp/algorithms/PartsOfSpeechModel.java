package org.apache.opennlp.algorithms;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

import org.apache.opennlp.OpenNLPModel;

import de.uniol.inf.is.odysseus.core.collection.Option;
import de.uniol.inf.is.odysseus.nlp.datastructure.annotations.Annotated;
import de.uniol.inf.is.odysseus.nlp.datastructure.annotations.implementations.PartsOfSpeech;
import de.uniol.inf.is.odysseus.nlp.datastructure.annotations.implementations.Tokens;
import de.uniol.inf.is.odysseus.nlp.datastructure.exception.NLPException;
import de.uniol.inf.is.odysseus.nlp.datastructure.exception.NLPModelNotFoundException;
import opennlp.tools.postag.POSModel;
import opennlp.tools.postag.POSTagger;
import opennlp.tools.postag.POSTaggerME;

public class PartsOfSpeechModel extends OpenNLPModel<PartsOfSpeech> {
	private static final long serialVersionUID = 3816907420196986638L;
	private POSModel model;
	private POSTagger tagger;
	
	public PartsOfSpeechModel(){
		super();
	}

	public PartsOfSpeechModel(Map<String, Option> configuration) throws NLPModelNotFoundException, NLPException{
		super(configuration);
	}
	@Override
	public void train(String languageCode, File file, String charSet) {
		// TODO Auto-generated method stub
	}

	@Override
	public void annotate(Annotated annotated) {
		Tokens tokensAnnotation = (Tokens)annotated.getAnnotations().get(Tokens.class.getSimpleName().toLowerCase());
		String[] tokens = tokensAnnotation.getTokens();
		String[] tags = tagger.tag(tokens);
		PartsOfSpeech annotation = new PartsOfSpeech(tags);
		annotated.put(annotation);
	}

	@Override
	public String identifier() {
		return PartsOfSpeech.class.getSimpleName().toLowerCase();
	}
	
	private POSTagger getTagger(){
		if(tagger==null){
			tagger = new POSTaggerME(model);
		}
		return tagger;
	}

	@Override
	protected void load(InputStream... stream) throws IOException {
		model = new POSModel(stream[0]);
		getTagger();
	}

	@Override
	protected void addRequirements() {
		prerequisites.add(TokenModel.class);
	}

}
