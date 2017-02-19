package org.apache.opennlp.algorithms;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

import org.apache.opennlp.OpenNLPModel;

import de.uniol.inf.is.odysseus.core.collection.Option;
import de.uniol.inf.is.odysseus.nlp.datastructure.annotations.Annotated;
import de.uniol.inf.is.odysseus.nlp.datastructure.annotations.implementations.Chunks;
import de.uniol.inf.is.odysseus.nlp.datastructure.annotations.implementations.PartsOfSpeech;
import de.uniol.inf.is.odysseus.nlp.datastructure.annotations.implementations.Tokens;
import de.uniol.inf.is.odysseus.nlp.datastructure.exception.NLPException;
import de.uniol.inf.is.odysseus.nlp.datastructure.exception.NLPModelNotFoundException;
import opennlp.tools.chunker.Chunker;
import opennlp.tools.chunker.ChunkerME;
import opennlp.tools.chunker.ChunkerModel;

public class ChunksModel extends OpenNLPModel<Chunks> {
	private static final long serialVersionUID = 1712373769774616803L;
	private ChunkerModel model;
	private Chunker chunker;
	
	public ChunksModel() {
		super();
	}

	public ChunksModel(Map<String, Option> configuration) throws NLPModelNotFoundException, NLPException {
		super(configuration);
	}

	@Override
	public void train(String languageCode, File file, String charSet) {
		// TODO Auto-generated method stub
		
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

}
