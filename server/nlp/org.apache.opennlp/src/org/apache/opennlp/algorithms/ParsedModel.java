package org.apache.opennlp.algorithms;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

import org.apache.opennlp.OpenNLPModel;
import org.apache.opennlp.OpenNLPToolkit;

import de.uniol.inf.is.odysseus.core.collection.Option;
import de.uniol.inf.is.odysseus.nlp.datastructure.annotations.Annotated;
import de.uniol.inf.is.odysseus.nlp.datastructure.annotations.Span;
import de.uniol.inf.is.odysseus.nlp.datastructure.annotations.implementations.Parsed;
import de.uniol.inf.is.odysseus.nlp.datastructure.annotations.implementations.Sentences;
import de.uniol.inf.is.odysseus.nlp.datastructure.annotations.parsetree.ParseTree;
import de.uniol.inf.is.odysseus.nlp.datastructure.exception.NLPException;
import de.uniol.inf.is.odysseus.nlp.datastructure.exception.NLPModelNotFoundException;
import opennlp.tools.cmdline.parser.ParserTool;
import opennlp.tools.parser.Parse;
import opennlp.tools.parser.Parser;
import opennlp.tools.parser.ParserFactory;
import opennlp.tools.parser.ParserModel;

public class ParsedModel extends OpenNLPModel<Parsed> {
	private ParserModel model;
	private Parser parser;

	public ParsedModel() {
		super();
	}

	public ParsedModel(Map<String, Option> configuration) throws NLPModelNotFoundException, NLPException {
		super(configuration);
	}
	


	@Override
	public void annotate(Annotated annotated) {
		Sentences sentAnnotation = (Sentences) annotated.getAnnotations().get(Sentences.NAME);
		String[] sentences = new String[sentAnnotation.getSentences().length];
		for(int i = 0; i < sentAnnotation.getSentences().length; i++){
			Span span = sentAnnotation.getSentences()[i];
			String sentence = annotated.getText().substring(span.getStart(), span.getEnd());
			sentences[i] = sentence;
		}
		ParseTree[] trees = new ParseTree[sentences.length];
		for(int i = 0; i < sentences.length; i++){
			Parse[] parses = ParserTool.parseLine(sentences[i], parser, 1);
			trees[i] = OpenNLPToolkit.convertParseToParseTree(parses[0]);
		}
		annotated.put(new Parsed(trees));		
	}

	@Override
	public String identifier() {
		return Parsed.NAME;
	}

	@Override
	protected void load(InputStream... stream) throws IOException {
		model = new ParserModel(stream[0]);
		getParser();
	}

	private Parser getParser() {
		if(parser == null){
			parser = ParserFactory.create(model);
		}
		return parser;
	}

	@Override
	protected void addRequirements() {
		prerequisites.add(SentenceModel.class);
		prerequisites.add(TokenModel.class);
	}

}
