package org.apache.opennlp.algorithms;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.regex.Pattern;

import org.apache.opennlp.OpenNLPModel;

import de.uniol.inf.is.odysseus.core.collection.Option;
import de.uniol.inf.is.odysseus.nlp.datastructure.annotations.Annotated;
import de.uniol.inf.is.odysseus.nlp.datastructure.annotations.IAnnotation;
import de.uniol.inf.is.odysseus.nlp.datastructure.annotations.implementations.Tokens;
import de.uniol.inf.is.odysseus.nlp.datastructure.annotations.model.TrainableFileAnnotationModel;
import de.uniol.inf.is.odysseus.nlp.datastructure.exception.NLPException;
import de.uniol.inf.is.odysseus.nlp.datastructure.exception.NLPModelNotFoundException;
import de.uniol.inf.is.odysseus.nlp.datastructure.exception.NLPTrainingFailedException;
import opennlp.tools.dictionary.Dictionary;
import opennlp.tools.tokenize.TokenSample;
import opennlp.tools.tokenize.TokenSampleStream;
import opennlp.tools.tokenize.Tokenizer;
import opennlp.tools.tokenize.TokenizerFactory;
import opennlp.tools.tokenize.TokenizerME;
import opennlp.tools.tokenize.TokenizerModel;
import opennlp.tools.tokenize.lang.Factory;
import opennlp.tools.util.MarkableFileInputStreamFactory;
import opennlp.tools.util.ObjectStream;
import opennlp.tools.util.PlainTextByLineStream;
import opennlp.tools.util.TrainingParameters;


public class TokenModel extends OpenNLPModel<Tokens> {
	private static final long serialVersionUID = 8879738892852158758L;
	private TokenizerModel tokenizerModel;

	public TokenizerModel getTokenizerModel() {
		return tokenizerModel;
	}

	private Tokenizer tokenizer;
	
	public void makeSerializable(){
		tokenizer = null;
	}
	
	public Tokenizer getTokenizer(){
		if(tokenizer == null){
			tokenizer = new TokenizerME(tokenizerModel);
		}
		return tokenizer;
	}
	
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
		Tokens annotation = new Tokens(getTokenizer().tokenize(annotated.getText()));
		annotated.put(annotation);
	}

	@Override
	protected void load(InputStream stream) throws IOException {
		this.tokenizerModel = new TokenizerModel(stream);
		getTokenizer();
	}

	@Override
	public String identifier() {
		return Tokens.class.getSimpleName().toLowerCase();
	}
	
	@Override
	protected void addRequirements() {
		prerequisites.add(SentenceModel.class);
	}


	@Override
	public void train(String languageCode, File file, String charSet) {
		try(ObjectStream<String> lineStream = new PlainTextByLineStream(new MarkableFileInputStreamFactory(file), charSet);
			ObjectStream<TokenSample> sampleStream = new TokenSampleStream(lineStream)) {
			TokenizerFactory factory = new TokenizerFactory(languageCode, new Dictionary(), true, Pattern.compile(Factory.DEFAULT_ALPHANUMERIC));
			tokenizerModel = TokenizerME.train(sampleStream, factory,  TrainingParameters.defaultParams());
			this.tokenizer = new TokenizerME(tokenizerModel);
		} catch (IOException e) {
			throw new NLPTrainingFailedException(e.getMessage());
		}
	}

}
