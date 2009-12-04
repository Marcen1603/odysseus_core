package de.uniol.inf.is.odysseus.cep.sase;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import javax.management.RuntimeErrorException;

import org.antlr.runtime.ANTLRStringStream;
import org.antlr.runtime.RecognitionException;
import org.antlr.runtime.Token;
import org.antlr.runtime.TokenRewriteStream;
import org.antlr.runtime.tree.CommonTree;
import org.antlr.runtime.tree.CommonTreeAdaptor;
import org.antlr.runtime.tree.TreeAdaptor;

import de.uniol.inf.is.odysseus.base.ILogicalOperator;
import de.uniol.inf.is.odysseus.cep.CepAO;
import de.uniol.inf.is.odysseus.cep.metamodel.EAction;
import de.uniol.inf.is.odysseus.cep.metamodel.EEventSelectionStrategy;
import de.uniol.inf.is.odysseus.cep.metamodel.State;
import de.uniol.inf.is.odysseus.cep.metamodel.StateMachine;
import de.uniol.inf.is.odysseus.cep.metamodel.Transition;
import de.uniol.inf.is.odysseus.cep.metamodel.jep.JEPCondition;

public class SaseBuilder {

	static final TreeAdaptor adaptor = new CommonTreeAdaptor() {
		public Object create(Token payload) {
			return new CommonTree(payload);
		}
	};

	public ILogicalOperator parse(String text) throws RecognitionException {
		SaseLexer lex = new SaseLexer(new ANTLRStringStream(text));
		TokenRewriteStream tokens = new TokenRewriteStream(lex);
		SaseParser grammar = new SaseParser(tokens);
		grammar.setTreeAdaptor(adaptor);
		SaseParser.query_return ret = grammar.query();
		CommonTree tree = (CommonTree) ret.getTree();
		// printTree(tree, 2);
		return createLogicalPlan(tree);
	}

	private ILogicalOperator createLogicalPlan(CommonTree tree) {
		CepAO cepAo = new CepAO();
		if (tree != null) {
			for (Object child : tree.getChildren()) {
				String cStr = child.toString();
				if (cStr.equalsIgnoreCase("PATTERN")) {
					processPattern((CommonTree) child, cepAo);
				} else if (cStr.equalsIgnoreCase("WHERE")) {
					processWhere((CommonTree) child, cepAo);
				} else if (cStr.equalsIgnoreCase("WITHIN")) {
					processWithin((CommonTree) child, cepAo);
				} else {
					throw new RuntimeException(cStr + " no valid Syntax");
				}

			}
			System.out.println("Final SM "+cepAo.getStateMachine().prettyPrint());
		}
		return cepAo;
	}

	private void processPattern(CommonTree patternTree, CepAO cepAo) {
		for (Object child : patternTree.getChildren()) {
			String cStr = child.toString();
			if (cStr.equalsIgnoreCase("SEQ")) {
				List<State> states = processStates(getChildren(child));
				// Append Accepting State
				if (states != null && states.size() > 0) {
					states.add(new State("<ACCEPTING>", true));
					StateMachine sm = cepAo.getStateMachine();
					sm.setStates(states);
					sm.setInitialState(states.get(0));
					// Calculate transistions
					for (int i = 0; i < states.size() - 1; i++) {
						State source = states.get(i);
						State dest = states.get(i + 1);
						if (source.getId().endsWith("[i]")) {
							source.addTransition(new Transition(source.getId()
									+ "_proceed", dest, new JEPCondition(""),EAction.consume));
							source.addTransition(new Transition(source.getId()
									+ "_take", dest, new JEPCondition(""),EAction.consume));
						} else {
							source.addTransition(new Transition(source.getId()
									+ "_begin", dest, new JEPCondition(""),EAction.consume));
						}
						if (i > 0 && i<states.size()-1) {
							// Ignore auf sich selbst!
							source.addTransition(new Transition(source.getId()
									+ "_ignore", source, new JEPCondition(""),EAction.consume));
						}
					}
				} else {
					throw new RuntimeException("Illegal Sequence");
				}

			}
		}

	}

	private List<State> processStates(List<CommonTree> states) {
		List<State> recStates = new LinkedList<State>();
		for (CommonTree state : states) {
			String str = state.getChild(0).toString();
			if (str.equals("KTYPE")) {
				String id = getChild(state, 1).getChild(0).getText();
				recStates.add(new State(id + "[1]"));
				recStates.add(new State(id + "[i]"));
			} else if (str.equals("TYPE")) {
				String id = getChild(state, 1).getChild(0).getText();
				recStates.add(new State(id));
			}
		}
		return recStates;
	}


	private void processWhere(CommonTree whereTree, CepAO cepAo) {
		for (Object child : whereTree.getChildren()) {
			String cStr = child.toString();
			if (cStr.equalsIgnoreCase("WHERESTRAT")) {
				processWhereStrat(getChildren(child), cepAo);
			}else if (cStr.equalsIgnoreCase("WHEREEXPRESSION")){
				processWhereExpression(getChildren(child), cepAo);
			}
		}
	}

		
	private void processWhereStrat(List<CommonTree> children, CepAO cepAo) {
		for (CommonTree t:children){
			if ("skip_till_next_match".equals(t.toString())){
				cepAo.getStateMachine().setEventSelectionStrategy(EEventSelectionStrategy.SKIP_TILL_NEXT_MATCH);
			}else if ("skip_till_any_match".equals(t.toString())){
				cepAo.getStateMachine().setEventSelectionStrategy(EEventSelectionStrategy.SKIP_TILL_ANY_MATCH);
			}else if ("partition_contiguity".equals(t.toString())){
				cepAo.getStateMachine().setEventSelectionStrategy(EEventSelectionStrategy.PARTITION_CONTIGUITY);
			}else if ("strict_contiguity".equals(t.toString())){
				cepAo.getStateMachine().setEventSelectionStrategy(EEventSelectionStrategy.STRICT_CONTIGUITY);
			}else if ("PARAMLIST".equals(t.toString())){
				// TODO: Process Param List
				// e.g. Map with AttributeSet and Strategy?
			}
		}
	}
	
	
	private void processWhereExpression(List<CommonTree> children, CepAO cepAo) {
		List<CompareExpression> compareExpressions = new ArrayList<CompareExpression>();
		
		for (CommonTree elem:children){
			if (elem.toString().equalsIgnoreCase("and")){
				// Zunächst (?) übergehen
			}else if (elem.toString().equalsIgnoreCase("IDEXPRESSION")){
				// TODO
				System.err.println("ID Expression not avaiable yet!!");
			}else if (elem.toString().equalsIgnoreCase("COMPAREEXPRESSION")){
				compareExpressions.add(processCompareExpression(getChildren(elem), cepAo));
			}
		}
		StateMachine stmachine = cepAo.getStateMachine();
		List<State> finalStates = stmachine.getFinalStates();
		for (State finalState:finalStates){
			List<State> pathes = stmachine.getAllPathesToStart(finalState);
			for (State s:pathes){
				Iterator<CompareExpression> compareIter = compareExpressions.iterator();
				while(compareIter.hasNext()){
					CompareExpression ce = compareIter.next();
					if (ce.contains(s.getId())){
						Transition t = s.getTransition(s.getId()+"_begin");
						if (t==null){
							t = s.getTransition(s.getId()+"_take");
						}
						if (t==null){
							t = s.getTransition(s.getId()+"_proceed");
						}
						t.append(ce.fullExpression);
						t = s.getTransition(s.getId()+"_ignore");
						if (t != null){
							switch (stmachine.getEventSelectionStrategy()){
							case PARTITION_CONTIGUITY:
								t.append(ce.fullExpression);
								// negation later!
							break;
							case SKIP_TILL_NEXT_MATCH:
								t.append(ce.fullExpression);
								// negation later!
							break;
							case STRICT_CONTIGUITY:
								t.append("false");
							break;
							case SKIP_TILL_ANY_MATCH:
							default:
								// let _ignore be true
							}
						}
						compareIter.remove();
					}
				}
			}
		}
		// SET NEGATION on whole expression!
		if (stmachine.getEventSelectionStrategy() == EEventSelectionStrategy.PARTITION_CONTIGUITY 
				|| stmachine.getEventSelectionStrategy() == EEventSelectionStrategy.SKIP_TILL_NEXT_MATCH){
			for(State s: stmachine.getStates()){
				Transition t = s.getTransition(s.getId()+"_ignore");
				if (t!= null){
					t.negateExpression();
				}
			}
		}
	}
	

	private CompareExpression processCompareExpression(List<CommonTree> children, CepAO cepAo) {
		CompareExpression expr = new CompareExpression();
		expr.fullExpression = buildStringForExpression(children);
		for (CommonTree elem:children){
			if (elem.toString().equalsIgnoreCase("KMEMBER")){			
				PathAttribute attr = new PathAttribute(elem.getChild(0).toString()+elem.getChild(1).toString(),true, elem.getChild(2).toString());
				expr.attributes.add(attr);
			}else if (elem.toString().equalsIgnoreCase("MEMBER")){
				PathAttribute attr = new PathAttribute(elem.getChild(0).toString(),false, elem.getChild(1).toString());
				expr.attributes.add(attr);		
			}else if (elem.toString().equalsIgnoreCase("AGGREGATION")){
				PathAttribute attr = new PathAttribute(elem.getChild(1).toString()+elem.getChild(2).toString(), true, elem.getChild(3).toString());
				expr.attributes.add(attr);
			}						
		}
		return expr;
	}

	
	private String buildStringForExpression(List<CommonTree> children) {
		StringBuffer ret = new StringBuffer();
		for (CommonTree elem:children){
			if (elem.toString().equalsIgnoreCase("KMEMBER")){
				ret.append(elem.getChild(0).toString())
					.append(elem.getChild(1).toString())
					.append(".")
					.append(elem.getChild(2).toString());
			}else if (elem.toString().equalsIgnoreCase("MEMBER")){
				ret.append(elem.getChild(0).toString())
					.append(".")
					.append(elem.getChild(1).toString());
			}else if (elem.toString().equalsIgnoreCase("AGGREGATION")){
				ret.append(elem.getChild(0).toString()).
					append("(")
					.append(elem.getChild(1).toString())
					.append(elem.getChild(2).toString()).append(")")
					.append(".").append(elem.getChild(3).toString());
			}else if (elem.toString().equalsIgnoreCase("EQUALS")){
				ret.append("==");
			}else{
				ret.append(elem.toString());
			}
		}
		return ret.toString();
	}

	private void processWithin(CommonTree child, CepAO cepAo) {
		// TODO Auto-generated method stub

	}

	
	private CommonTree getChild(Object o, int i) {
		return (CommonTree) ((CommonTree) o).getChild(i);
	}

	private List<CommonTree> getChildren(Object o) {
		List<CommonTree> ret = new ArrayList<CommonTree>();
		for (Object t : ((CommonTree) o).getChildren()) {
			ret.add((CommonTree) t);
		}
		return ret;
	}
	
	public void printTree(CommonTree t, int indent) {
		if (t != null) {
			StringBuffer sb = new StringBuffer(indent);
			for (int i = 0; i < indent; i++)
				sb = sb.append("   ");
			for (int i = 0; i < t.getChildCount(); i++) {
				System.out.println(sb.toString() + t.getChild(i).toString());
				printTree((CommonTree) t.getChild(i), indent + 1);
			}
		}
	}

	public static void main(String[] args) {
		SaseBuilder exec = new SaseBuilder();
			
		
//		String[] toParse = new String[] {
//						"PATTERN SEQ(Stock+ a[], Stock b) WHERE skip_till_next_match(a[],b){ [symbol] and a[1].volume > 1000 and a[i].price > avg(a[..i-1].price) and b.a = 10 and a[i].from = a[i-1].to} WITHIN 1 hour"};

		String[] toParse = new String[] {
		"PATTERN SEQ(Stock+ a[], Stock b) WHERE skip_till_next_match(a[],b){ a[1].symbol = a[i].symbol and a[1].symbol=b.symbol and a[1].volume > 1000 and a[i].price > avg(a[..i-1].price) and b.volume < a[a.LEN].volume} WITHIN 1 hour"};

		try {
			for (String q : toParse) {
				System.out.println(q);
				exec.parse(q);
			}
		} catch (RecognitionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
	
class CompareExpression{
	public List<PathAttribute> attributes = new LinkedList<PathAttribute>();
	String fullExpression;
	
	@Override
	public String toString() {
		return attributes+" "+fullExpression;
	}
	
	boolean contains(String attrib){
		for (PathAttribute a: attributes){
			if (attrib.equals(a.attribute)){
				return true;
			}
		}
		return false;
	}
}	

class PathAttribute{
	public String attribute;
	public boolean isKleenAttribute;
	public String path;
	
	public PathAttribute(String attribute, boolean isKleenAttribute, String path) {
		this.attribute = attribute;
		this.isKleenAttribute = isKleenAttribute;
		this.path = path;
	}
	
	@Override
	public String toString() {
		return attribute+"."+path;
	}
}	
