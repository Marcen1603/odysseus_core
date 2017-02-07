package org.apache.opennlp.algorithms;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;

import org.apache.opennlp.OpenNLPModel;

import de.uniol.inf.is.odysseus.core.collection.Option;
import de.uniol.inf.is.odysseus.nlp.datastructure.Annotated;
import de.uniol.inf.is.odysseus.nlp.datastructure.IAnnotation;
import de.uniol.inf.is.odysseus.nlp.datastructure.annotations.Tokens;
import de.uniol.inf.is.odysseus.nlp.datastructure.exception.NLPException;
import de.uniol.inf.is.odysseus.nlp.datastructure.exception.NLPModelNotFoundException;
import opennlp.tools.tokenize.Tokenizer;
import opennlp.tools.tokenize.TokenizerME;
import opennlp.tools.tokenize.TokenizerModel;


public class TokenModel extends OpenNLPModel<Tokens> {
	private Tokenizer tokenizer;
	
	public TokenModel(){
		super();
	}
	

	public TokenModel(HashMap<String, Option> configuration) throws NLPModelNotFoundException, NLPException{
		super(configuration);
	}
	

	@Override
	public Class<? extends IAnnotation> type() {
		return Tokens.class;
	}

	@Override
	public void annotate(Annotated annotated) {
		Tokens annotation = new Tokens(tokenizer.tokenize(annotated.getText()));
		annotated.put(annotation);
	}

	@Override
	protected void load(InputStream stream) throws IOException {
		this.tokenizer = new TokenizerME(new TokenizerModel(stream));
	}

	@Override
	public String identifier() {
		return Tokens.class.getSimpleName().toLowerCase();
	}
	
	@Override
	protected void addRequirements() {
		prerequisites.add(SentenceModel.class);
	}

}
