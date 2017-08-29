package org.apache.opennlp.algorithms;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.opennlp.OpenNLPToolkit;
import org.apache.opennlp.OpenNLPTrainableModel;

import de.uniol.inf.is.odysseus.core.collection.Option;
import de.uniol.inf.is.odysseus.core.collection.OptionMap;
import de.uniol.inf.is.odysseus.nlp.datastructure.annotations.Annotated;
import de.uniol.inf.is.odysseus.nlp.datastructure.annotations.implementations.NamedEntities;
import de.uniol.inf.is.odysseus.nlp.datastructure.annotations.implementations.NamedEntity;
import de.uniol.inf.is.odysseus.nlp.datastructure.annotations.implementations.Tokens;
import de.uniol.inf.is.odysseus.nlp.datastructure.annotations.model.AnnotationModel;
import de.uniol.inf.is.odysseus.nlp.datastructure.annotations.model.IJoinable;
import de.uniol.inf.is.odysseus.nlp.datastructure.exception.InvalidSpanException;
import de.uniol.inf.is.odysseus.nlp.datastructure.exception.NLPException;
import de.uniol.inf.is.odysseus.nlp.datastructure.exception.NLPModelNotFoundException;
import de.uniol.inf.is.odysseus.nlp.datastructure.exception.NLPModelStoringFailed;
import de.uniol.inf.is.odysseus.nlp.datastructure.exception.NLPTrainingFailedException;
import opennlp.tools.namefind.NameFinderME;
import opennlp.tools.namefind.NameSample;
import opennlp.tools.namefind.NameSampleDataStream;
import opennlp.tools.namefind.TokenNameFinder;
import opennlp.tools.namefind.TokenNameFinderFactory;
import opennlp.tools.namefind.TokenNameFinderModel;
import opennlp.tools.util.MarkableFileInputStreamFactory;
import opennlp.tools.util.ObjectStream;
import opennlp.tools.util.PlainTextByLineStream;
import opennlp.tools.util.TrainingParameters;

public class NamedEntitiesModel extends OpenNLPTrainableModel<NamedEntities> implements IJoinable {
	private TokenNameFinderModel[] namedEntitiesModels;
	private NameFinderME[] nameFinders;

	public NamedEntitiesModel(){
		super();
	}

	public NamedEntitiesModel(Map<String, Option> configuration) throws NLPModelNotFoundException, NLPException{
		super(configuration);
	}
	
	@Override
	public void train(String languageCode, File file, String charSet, OptionMap trainOptions) {
		String type = trainOptions.get("type");
		if(type == null)
			throw new NLPTrainingFailedException("Error: option type (e.g. person, location,...) should be set.");
		String overwrite = trainOptions.get("overwrite");
		if(overwrite == null)
			throw new NLPTrainingFailedException("Error: overwrite type should be set. Sets filename of models which will be overriden if train is connected to annotate");
		//the filename of the model that will be overwritten during join
		filenames = new String[]{overwrite};
		namedEntitiesModels = new TokenNameFinderModel[1];
		try(ObjectStream<String> lineStream = new PlainTextByLineStream(new MarkableFileInputStreamFactory(file), charSet);
				ObjectStream<NameSample> sampleStream = new NameSampleDataStream(lineStream)) {
				TokenNameFinderFactory factory = new TokenNameFinderFactory();
				namedEntitiesModels[0] = NameFinderME.train(languageCode, type, sampleStream, TrainingParameters.defaultParams(), factory);
				nameFinders = null;
				getNameFinders();
			} catch (IOException e) {
				throw new NLPTrainingFailedException(e.getMessage());
			}
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

	@Override
	public AnnotationModel<?> join(AnnotationModel<?> join, boolean overwrite) {
		if (join instanceof NamedEntitiesModel){
			NamedEntitiesModel model = (NamedEntitiesModel) join;
			TokenNameFinderModel[] findersA = namedEntitiesModels;
			TokenNameFinderModel[] findersB = model.namedEntitiesModels;
			NamedEntitiesModel joined = new NamedEntitiesModel();
			Map<String, TokenNameFinderModel> finders = new HashMap<>();
			for(int i = 0; i < findersA.length; i++){
				String filename = filenames[i];
				finders.put(filename, findersA[i]);		
			}
			
			for(int i = 0; i < findersB.length; i++){
				String filename = model.filenames[i];
				if(finders.containsKey(filename)){
					finders.put(filename, findersB[i]);
				}
			}
			
			joined.namedEntitiesModels = finders.values().toArray(new TokenNameFinderModel[0]);
			joined.getNameFinders();
			return joined;
			
		}
		
		return null;
	}

	@Override
	protected void store(OutputStream modelOut) throws IOException {
		if(namedEntitiesModels.length != 1){
			throw new NLPModelStoringFailed("Cannot store NamedEntitiesModel, if more than one named-entities-models are used");
		}
		namedEntitiesModels[0].serialize(modelOut);				
	}
}
