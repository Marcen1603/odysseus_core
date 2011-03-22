package de.uniol.inf.is.odysseus.markov.markovql;

import java.util.ArrayList;
import java.util.List;

import de.uniol.inf.is.odysseus.datadictionary.IDataDictionary;
import de.uniol.inf.is.odysseus.markov.markovql.parser.ASTEmissions;
import de.uniol.inf.is.odysseus.markov.markovql.parser.ASTHiddenMarkovModel;
import de.uniol.inf.is.odysseus.markov.markovql.parser.ASTIdentifier;
import de.uniol.inf.is.odysseus.markov.markovql.parser.ASTObservations;
import de.uniol.inf.is.odysseus.markov.markovql.parser.ASTProbability;
import de.uniol.inf.is.odysseus.markov.markovql.parser.ASTProbabilityTransition;
import de.uniol.inf.is.odysseus.markov.markovql.parser.ASTProbabilityTransitionList;
import de.uniol.inf.is.odysseus.markov.markovql.parser.ASTProbabiltyList;
import de.uniol.inf.is.odysseus.markov.markovql.parser.ASTQuery;
import de.uniol.inf.is.odysseus.markov.markovql.parser.ASTQuotedIdentifier;
import de.uniol.inf.is.odysseus.markov.markovql.parser.ASTStartProbability;
import de.uniol.inf.is.odysseus.markov.markovql.parser.ASTStateList;
import de.uniol.inf.is.odysseus.markov.markovql.parser.ASTStates;
import de.uniol.inf.is.odysseus.markov.markovql.parser.ASTTransitions;
import de.uniol.inf.is.odysseus.markov.model.HiddenMarkovModel;
import de.uniol.inf.is.odysseus.markov.model.HiddenMarkovModelDictionary;
import de.uniol.inf.is.odysseus.markov.model.statemachine.Observation;
import de.uniol.inf.is.odysseus.markov.model.statemachine.State;
import de.uniol.inf.is.odysseus.markov.model.statemachine.Transition;
import de.uniol.inf.is.odysseus.planmanagement.QueryParseException;
import de.uniol.inf.is.odysseus.planmanagement.query.IQuery;
import de.uniol.inf.is.odysseus.planmanagement.query.Query;
import de.uniol.inf.is.odysseus.usermanagement.User;

public class MarkovQLVisitor extends AbstractMarkovQLVisitor{

	private List<IQuery> plans = new ArrayList<IQuery>();

	public MarkovQLVisitor(User user, IDataDictionary dd) {
		
	}	

	@Override
	public Object visit(ASTQuery node, Object data) {
		for(int i=0;i<node.jjtGetNumChildren();i++){			
			HiddenMarkovModel hmm = (HiddenMarkovModel)node.jjtGetChild(i).jjtAccept(this, data);								
			HiddenMarkovModelDictionary.getInstance().addHMM(hmm.getName(), hmm);
			// pack into query
			//IQuery query = new Query();					
			// and remember it
			//plans.add(query);
		}		
		return plans;
	}

	@Override
	public Object visit(ASTHiddenMarkovModel node, Object data) {
		String name = ((ASTIdentifier)node.jjtGetChild(0)).jjtGetValue().toString();		
		HiddenMarkovModel hmm = new HiddenMarkovModel(name);
		hmm = (HiddenMarkovModel)node.childrenAccept(this, hmm);
		
		return hmm;
	}	

	public List<IQuery> getPlans() {		
		return plans;
	}

	@Override
	public Object visit(ASTStates node, Object data) {
		HiddenMarkovModel hmm = (HiddenMarkovModel)data;	
		ASTStateList list = (ASTStateList) node.jjtGetChild(0);	
		
		@SuppressWarnings("unchecked")
		List<String> names = (List<String>)visit(list, null);		
		for(String name : names){
			hmm.addState(new State(name));
		}				
		return hmm; 
	}
	
	@Override
	public Object visit(ASTObservations node, Object data) {		
		HiddenMarkovModel hmm = (HiddenMarkovModel)data;
		ASTStateList list = (ASTStateList) node.jjtGetChild(0);
				
		@SuppressWarnings("unchecked")
		List<String> names = (List<String>)visit(list, null);		
		
		for(String name : names){
			hmm.addObservation(new Observation(name));
		}				
		return hmm; 
	}

	@Override
	public Object visit(ASTTransitions node, Object data) {
		HiddenMarkovModel hmm = (HiddenMarkovModel)data;
		ASTProbabilityTransitionList ptl = (ASTProbabilityTransitionList) node.jjtGetChild(0);						
		@SuppressWarnings("unchecked")
		List<State> dummyStates = (List<State>)ptl.jjtAccept(this, hmm);
		for(State dummy: dummyStates){			
			hmm.getState(dummy.getName()).addTransitions(dummy.getOutgoingTransitions());
		}
		return hmm;
	}

	
	@Override
	public Object visit(ASTEmissions node, Object data) {
		HiddenMarkovModel hmm = (HiddenMarkovModel)data;
		ASTProbabilityTransitionList ptl = (ASTProbabilityTransitionList) node.jjtGetChild(0);						
		@SuppressWarnings("unchecked")
		List<State> dummyStates = (List<State>)ptl.jjtAccept(this, hmm);
		for(State dummy: dummyStates){			
			hmm.getState(dummy.getName()).addEmissions(dummy.getOutgoingTransitions());
		}
		return hmm;
	}

	@Override
	public Object visit(ASTStartProbability node, Object data) {
		HiddenMarkovModel hmm = (HiddenMarkovModel)data;
		State startState = new State("StartState");
		ASTProbabiltyList liste = (ASTProbabiltyList)node.jjtGetChild(0);
		@SuppressWarnings("unchecked")
		List<Transition> transitionen = (List<Transition>) liste.jjtAccept(this, data);		
		startState.addTransitions(transitionen);
		hmm.setStartState(startState);
		return hmm;
	}	
	
	@Override
	public Object visit(ASTQuotedIdentifier node, Object data) {
		ASTIdentifier ident = (ASTIdentifier)node.jjtGetChild(0);		
		if(data instanceof List){
			@SuppressWarnings("unchecked")
			List<String> liste = (List<String>)data;
			liste.add((String)ident.jjtGetValue());
			return liste;
		}
			
		return ident.jjtGetValue();
	}
	
	
	@Override
	public Object visit(ASTProbabilityTransition node, Object data) {
		HiddenMarkovModel hmm = (HiddenMarkovModel)data;
		String from = (String) visit((ASTQuotedIdentifier) node.jjtGetChild(0), null);
		if(hmm.isStateExisting(from)){
			ASTProbability prob = (ASTProbability) node.jjtGetChild(1);
			Transition transition = (Transition) visit(prob, hmm);
			State dummyState = new State(from);
			dummyState.addTransition(transition);			
			return dummyState;
		}else{
			throw new RuntimeException(new QueryParseException("The State "+from+" has not been specified before!"));
		}		
	}
	
	@Override
	public Object visit(ASTProbabilityTransitionList node, Object data) {
		HiddenMarkovModel hmm = (HiddenMarkovModel)data;
		List<State> states = new ArrayList<State>();
		for(int i=0;i<node.jjtGetNumChildren();i++){
			states.add((State) visit((ASTProbabilityTransition)node.jjtGetChild(i), hmm));			
		}
		return states;
	}
	
	@Override
	public Object visit(ASTProbability node, Object data) {
		HiddenMarkovModel hmm = (HiddenMarkovModel)data;
		double probability = Double.parseDouble(node.jjtGetValue().toString());
		
		ASTQuotedIdentifier qi = (ASTQuotedIdentifier)node.jjtGetChild(0);
		Transition transition;
		String destination = (String) visit(qi, null);
		if(hmm.isStateExisting(destination)){
			State to = hmm.getState(destination);
			transition = new Transition(to, probability);
		}else{			
			if(hmm.isObservationExisting(destination)){
				Observation o = hmm.getObservation(destination);
				transition = new Transition(o, probability);				
			}else{			
				throw new RuntimeException(new QueryParseException("State or observation named "+destination+" for probability "+probability+" does not exist!"));
			}
		}				
		return transition;
	}
	
	@Override
	public Object visit(ASTStateList node, Object data) {
		List<String> identifiers = new ArrayList<String>();
		node.childrenAccept(this, identifiers);
		return identifiers;
	}
	
	
	@Override
	public Object visit(ASTProbabiltyList node, Object data) {	
		List<Transition> probabilities = new ArrayList<Transition>();
		for(int i=0;i<node.jjtGetNumChildren();i++){
			probabilities.add((Transition) node.jjtGetChild(i).jjtAccept(this, data));
		}
		return probabilities;
	}	
}
