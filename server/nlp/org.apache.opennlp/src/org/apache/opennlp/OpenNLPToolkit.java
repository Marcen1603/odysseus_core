package org.apache.opennlp;

import java.util.List;
import java.util.Map;

import de.uniol.inf.is.odysseus.core.collection.Option;
import de.uniol.inf.is.odysseus.nlp.datastructure.annotations.Span;
import de.uniol.inf.is.odysseus.nlp.datastructure.annotations.parsetree.ParseLeaf;
import de.uniol.inf.is.odysseus.nlp.datastructure.annotations.parsetree.ParseNode;
import de.uniol.inf.is.odysseus.nlp.datastructure.annotations.parsetree.ParseTree;
import de.uniol.inf.is.odysseus.nlp.datastructure.exception.InvalidSpanException;
import de.uniol.inf.is.odysseus.nlp.datastructure.exception.NLPException;
import de.uniol.inf.is.odysseus.nlp.datastructure.toolkit.NLPToolkit;
import opennlp.tools.parser.Parse;



public class OpenNLPToolkit extends NLPToolkit{

	/**
	 * Instantiates Toolkit with empty Pipeline for algorithm access only.
	 */
	public OpenNLPToolkit(){
		super();
	}
	
	public OpenNLPToolkit(List<String> models, Map<String, Option> configuration) throws NLPException {
		super(models, configuration);
	}

	@Override
	protected void instantiatePipeline(List<String> models, Map<String, Option> configuration) throws NLPException {
		this.pipeline = new OpenNLPPipeline(models, configuration);
	}

	@Override
	protected void instantiateEmptyPipeline() {
		this.pipeline = new OpenNLPPipeline();
	}
	
	public static Span convertOpenNLPSpanToSpan(opennlp.tools.util.Span span) throws InvalidSpanException{
		return new Span(span.getStart(), span.getEnd(), span.toString());
	}

	public static ParseTree convertParseToParseTree(Parse parse) {
		ParseNode[] children = new ParseNode[parse.getChildCount()];
		String sentence = parse.getText();
		for(int i = 0; i < parse.getChildCount(); i++){
			Parse child = parse.getChildren()[i];
			children[i] = new ParseNode(child.getType(), getSpanOfText(sentence, child.getSpan()), getParseChildren(child, sentence));
		}
		ParseTree parseTree = new ParseTree(parse.getType(), getSpanOfText(sentence, parse.getSpan()), children);
		return parseTree;
	}
	
	public static ParseNode[] getParseChildren(Parse parse, String sentence){
		ParseNode[] children = new ParseNode[parse.getChildCount()];
		for(int i = 0; i < parse.getChildCount(); i++){
			Parse child = parse.getChildren()[i];
			if(child.getChildCount() > 0)
				children[i] = new ParseNode(child.getType(), getSpanOfText(sentence, child.getSpan()), getParseChildren(child, sentence));
			else
				children[i] = new ParseLeaf(child.getType(), getSpanOfText(sentence, child.getSpan()));
		}
		return children;
	}
	
	public static String getSpanOfText(String text, opennlp.tools.util.Span span){
		return text.substring(span.getStart(), span.getEnd());
	}

}
