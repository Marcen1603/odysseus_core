package org.apache.opennlp.algorithms;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.opennlp.OpenNLPModel;
import org.apache.opennlp.OpenNLPToolkit;

import de.uniol.inf.is.odysseus.core.collection.Option;
import de.uniol.inf.is.odysseus.nlp.datastructure.annotations.Annotated;
import de.uniol.inf.is.odysseus.nlp.datastructure.annotations.implementations.NamedEntities;
import de.uniol.inf.is.odysseus.nlp.datastructure.annotations.implementations.NamedEntity;
import de.uniol.inf.is.odysseus.nlp.datastructure.annotations.implementations.Tokens;
import de.uniol.inf.is.odysseus.nlp.datastructure.exception.InvalidSpanException;
import de.uniol.inf.is.odysseus.nlp.datastructure.exception.NLPException;
import de.uniol.inf.is.odysseus.nlp.datastructure.exception.NLPModelNotFoundException;
import opennlp.tools.namefind.NameFinderME;
import opennlp.tools.namefind.TokenNameFinder;
import opennlp.tools.namefind.TokenNameFinderModel;

public class NamedEntitiesModel extends OpenNLPModel<NamedEntities> {
	private static final long serialVersionUID = 958282844283877757L;
	private TokenNameFinderModel[] namedEntitiesModels;
	private TokenNameFinder[] nameFinders;

	public NamedEntitiesModel(){
		super();
	}

	public NamedEntitiesModel(Map<String, Option> configuration) throws NLPModelNotFoundException, NLPException{
		super(configuration);
	}
	
	@Override
	public void train(String languageCode, File file, String charSet) {
		// TODO Auto-generated method stub
	}

	@Override
	public void annotate(Annotated annotated) {
		List<NamedEntity> list = new ArrayList<>();
		Tokens tokensAnnotation = (Tokens)annotated.getAnnotations().get(Tokens.NAME);
		String[] tokens = tokensAnnotation.getTokens();
		for(TokenNameFinder nameFinder : nameFinders){
			opennlp.tools.util.Span[] tokenSpans = nameFinder.find(tokens);
			for(opennlp.tools.util.Span tokenSpan : tokenSpans){
				NamedEntity entity = new NamedEntity(tokenSpan.getType());
				list.add(entity);
				try {
					entity.add(OpenNLPToolkit.convertOpenNLPSpanToSpan(tokenSpan));
				} catch (InvalidSpanException e) {
					e.printStackTrace();
				}
			}
		}
		NamedEntities namedEntities = new NamedEntities(list);
		annotated.put(namedEntities);
	}

	@Override
	public String identifier() {
		return NamedEntities.NAME;
	}

	@Override
	protected void load(InputStream... streams) throws IOException {
		this.namedEntitiesModels = new TokenNameFinderModel[streams.length];
		for(int i = 0; i < streams.length; i++){
			this.namedEntitiesModels[i] = new TokenNameFinderModel(streams[i]);
		}
		getNameFinders();
		
	}

	private TokenNameFinder[] getNameFinders() {
		if(nameFinders == null){
			nameFinders = new NameFinderME[namedEntitiesModels.length];
			for(int i = 0 ; i < namedEntitiesModels.length; i++){
				nameFinders[i] = new NameFinderME(namedEntitiesModels[i]);
			}
		}
		return nameFinders;
	}

	@Override
	protected void addRequirements() {
		prerequisites.add(TokenModel.class);
	}

}
