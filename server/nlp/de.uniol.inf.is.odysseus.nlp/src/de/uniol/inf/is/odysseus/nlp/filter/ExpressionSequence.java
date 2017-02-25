package de.uniol.inf.is.odysseus.nlp.filter;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import de.uniol.inf.is.odysseus.nlp.datastructure.annotations.Annotated;

public class ExpressionSequence implements IExpressionElement{
	private List<IExpressionElement> elements = new ArrayList<>();
	
	public ExpressionSequence(){
		
	}
	
	public void add(IExpressionElement element){
		this.elements.add(element);
	}
	
	public List<IExpressionElement> getElements(){
		return elements;
	}

	@Override
	public boolean validate(Annotated annotated, AtomicInteger token) {
		boolean validate = true;
		for(IExpressionElement element : elements){
			validate = element.validate(annotated, token);
			if(!validate)
				return false;
		}
		return validate;
	}

}
