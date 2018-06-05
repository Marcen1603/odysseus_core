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
import de.uniol.inf.is.odysseus.nlp.datastructure.annotations.implementations.Chunks;
import de.uniol.inf.is.odysseus.nlp.datastructure.annotations.implementations.PartsOfSpeech;
import de.uniol.inf.is.odysseus.nlp.datastructure.annotations.implementations.Tokens;
import de.uniol.inf.is.odysseus.nlp.datastructure.exception.NLPException;
import de.uniol.inf.is.odysseus.nlp.datastructure.exception.NLPModelNotFoundException;
import de.uniol.inf.is.odysseus.nlp.datastructure.exception.NLPTrainingFailedException;
import opennlp.tools.chunker.ChunkSample;
import opennlp.tools.chunker.ChunkSampleStream;
import opennlp.tools.chunker.Chunker;
import opennlp.tools.chunker.ChunkerFactory;
import opennlp.tools.chunker.ChunkerME;
import opennlp.tools.chunker.ChunkerModel;
import opennlp.tools.util.MarkableFileInputStreamFactory;
import opennlp.tools.util.ObjectStream;
import opennlp.tools.util.PlainTextByLineStream;
import opennlp.tools.util.TrainingParameters;

public class ChunksModel extends OpenNLPTrainableModel<Chunks> {
	private ChunkerModel model;
	private Chunker chunker;
	
	public ChunksModel() {
		super();
	}

	public ChunksModel(Map<String, Option> configuration) throws NLPModelNotFoundException, NLPException {
		super(configuration);
	}

	@Override
	public void train(String languageCode, File file, String charSet, OptionMap options) {
		try(ObjectStream<String> lineStream = new PlainTextByLineStream(new MarkableFileInputStreamFactory(file), charSet);
				ObjectStream<ChunkSample> sampleStream = new ChunkSampleStream(lineStream)) {
				ChunkerFactory factory = new ChunkerFactory();
				model = ChunkerME.train(languageCode, sampleStream, TrainingParameters.defaultParams(), factory);
				this.chunker = new ChunkerME(model);
			} catch (IOException e) {
				throw new NLPTrainingFailedException(e.getMessage());
			}
	}

	@Override
	public void annotate(Annotated annotated) {
		Tokens tokens = (Tokens) annotated.getAnnotations().get(Tokens.NAME);
		PartsOfSpeech pos = (PartsOfSpeech) annotated.getAnnotations().get(PartsOfSpeech.NAME);
		Chunks chunks = new Chunks(chunker.chunk(tokens.getTokens(), pos.getTags()));
		annotated.put(chunks);
	}

	@Override
	public String identifier() {
		return Chunks.NAME;
	}

	@Override
	protected void load(InputStream... stream) throws IOException {
		model = new ChunkerModel(stream[0]);
		getChunker();
	}

	private Chunker getChunker() {
		if(chunker == null){
			chunker = new ChunkerME(model);
		}
		return chunker;
	}

	@Override
	protected void addRequirements() {
		prerequisites.add(PartsOfSpeechModel.class);
	}

	@Override
	protected void store(OutputStream modelOut) throws IOException {
		model.serialize(modelOut);			
	}

}
