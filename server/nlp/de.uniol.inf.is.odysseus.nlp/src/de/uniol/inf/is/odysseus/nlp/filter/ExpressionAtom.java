package de.uniol.inf.is.odysseus.nlp.filter;

import java.util.concurrent.atomic.AtomicInteger;

import de.uniol.inf.is.odysseus.nlp.datastructure.annotations.Annotated;
import de.uniol.inf.is.odysseus.nlp.filter.validators.ElementsValidatorManager;

public class ExpressionAtom implements IExpressionElement{
	private String type;
	private String needle;
	
	public ExpressionAtom(String type, String needle){
		this.type = type;
		this.needle = needle;
	}
	
	
	public String getType(){
		return type;
	}
	
	public String getNeedle(){
		return needle;
	}


	@Override
	public boolean validate(Annotated annotated, AtomicInteger token) {
		return ElementsValidatorManager.get(type).validate(this, annotated, token);
	}
}
