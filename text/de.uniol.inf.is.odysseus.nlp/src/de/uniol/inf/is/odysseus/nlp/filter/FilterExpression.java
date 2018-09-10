package de.uniol.inf.is.odysseus.nlp.filter;

import java.util.concurrent.atomic.AtomicInteger;

import de.uniol.inf.is.odysseus.nlp.datastructure.annotations.Annotated;

public class FilterExpression {
	private String expression;
	private IExpressionElement object;
	private final char STRING_DELIMITER ='"';

	public FilterExpression(String expression) throws MalformedExpression{
		if(expression == null)
			throw new MalformedExpression();
		this.expression = expression;
		object = fromSequence(expression);
	}
	
	public String getExpression(){
		return expression;
	}
	
	public boolean validate(Annotated annotated) {
		boolean incrementing = true;
		AtomicInteger tokenId = new AtomicInteger(0);
		int last = -1;
		while(incrementing && !object.validate(annotated, tokenId)){
			incrementing = tokenId.get() > last;
			last = tokenId.get();
		}
		return incrementing;
	}
	
	
	/**
	 * Translates a sequence into the internal datastructure.
	 * Eg.  ["token"="get"]["lemma"="VB"] or  ["token"="get"](nand=["lemma"="VB"])
	 * @param expression string representation of expression
	 * @return new ExpressionSequence
	 * @throws MalformedExpression if there's anything wrong in the expression
	 */
	public ExpressionSequence fromSequence(String expression) throws MalformedExpression{
		ExpressionSequence sequence = new ExpressionSequence();	
		for(AtomicInteger i = new AtomicInteger(0); i.intValue() < expression.length(); i.addAndGet(1)){
			char c = expression.charAt(i.intValue());
			switch(c){
				case '[':
					sequence.add(fromAtom(expression, i));
					continue;
				case '(':
					sequence.add(fromGroup(expression, i));
					continue;
			}	
		}
		
		return sequence;
	}
	
	/**
	 * Translates a group (and=...) into the internal datastructure
	 * @param expression the expression that should be translated
	 * @param start start-index that will increase during translation
	 * @return new Group
	 * @throws MalformedExpression if the expression has errors
	 */
	public IExpressionElement fromGroup(String expression, AtomicInteger start) throws MalformedExpression{
		boolean escaped = false;// \( means ( is escaped so will not be investigated
		boolean awaitType = true;
		String type = "";
		String subexpression = "";
		int level = 1; //recursion-level
		int beginExpression = 0; //id of expression beginning
		
		for(; start.addAndGet(1) < expression.length(); ){
			char c = expression.charAt(start.intValue());

			if(awaitType){
				if(c == '='){
					awaitType = false;
					beginExpression=start.get()+1;
					continue;
				}else{
					type += c;
				}
			}else{
				if(c == STRING_DELIMITER){
					escaped = !escaped;
				}else if(!escaped && c == '('){
					level++;
				}else if(!escaped && c == ')' && level > 1){
					level--;
				}else if(!escaped && c == ')' && level == 1){
					subexpression = expression.substring(beginExpression, start.get());
					return new ExpressionGroup(type, fromSequence(subexpression));
				}
			}			
		}
		
		throw new MalformedExpression();
	}	
	
	/**
	 * Atoms or elementary Objects are eg. ["token"="change"].
	 * @param expression the expression to be translated
	 * @param start start-index that will increase during translation
	 * @return
	 * @throws MalformedExpression
	 */
	public IExpressionElement fromAtom(String expression, AtomicInteger start) throws MalformedExpression{
		boolean escaped = false;
		//'..'='..'
		String type = null;
		String needle = null;
		boolean awaitType = true;
		boolean awaitNeedle = false;
		int beginEscaped = 0;
		for(; start.addAndGet(1) < expression.length(); ){
			char c = expression.charAt(start.intValue());
			
			if(!escaped && c == STRING_DELIMITER){
				beginEscaped = start.get();
				escaped = true;
			}else{
				if(escaped && c == STRING_DELIMITER){
					String s = expression.substring(beginEscaped+1, start.get());
					if(awaitType){
						type = s;
						awaitType = false;
						awaitNeedle = true;
					}else if(awaitNeedle){
						needle = s;
						awaitNeedle = false;
					}else{
						throw new MalformedExpression();
					}
					escaped = false;
				}
			}
			
			if(c == '=' && !escaped){
				awaitNeedle = true;
			}
			
			if(c == ']' && !escaped){
				break;
			}
			
		}
		
		if(type == null || needle == null){
			throw new MalformedExpression();
		}
		
		return new ExpressionAtom(type, needle);
	}
}
