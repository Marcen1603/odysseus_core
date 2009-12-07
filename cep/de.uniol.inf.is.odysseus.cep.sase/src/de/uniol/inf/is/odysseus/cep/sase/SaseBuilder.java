package de.uniol.inf.is.odysseus.cep.sase;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import org.antlr.runtime.ANTLRStringStream;
import org.antlr.runtime.RecognitionException;
import org.antlr.runtime.Token;
import org.antlr.runtime.TokenRewriteStream;
import org.antlr.runtime.tree.CommonTree;
import org.antlr.runtime.tree.CommonTreeAdaptor;
import org.antlr.runtime.tree.TreeAdaptor;

import de.uniol.inf.is.odysseus.base.DataDictionary;
import de.uniol.inf.is.odysseus.base.ILogicalOperator;
import de.uniol.inf.is.odysseus.cep.CepAO;
import de.uniol.inf.is.odysseus.cep.metamodel.EAction;
import de.uniol.inf.is.odysseus.cep.metamodel.EEventSelectionStrategy;
import de.uniol.inf.is.odysseus.cep.metamodel.State;
import de.uniol.inf.is.odysseus.cep.metamodel.StateMachine;
import de.uniol.inf.is.odysseus.cep.metamodel.Transition;
import de.uniol.inf.is.odysseus.cep.metamodel.jep.JEPCondition;
import de.uniol.inf.is.odysseus.cep.metamodel.symboltable.Write;
import de.uniol.inf.is.odysseus.logicaloperator.base.AccessAO;
import de.uniol.inf.is.odysseus.logicaloperator.base.AlgebraPlanToStringVisitor;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.description.SDFSource;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFAttributeList;
import de.uniol.inf.is.odysseus.util.AbstractTreeWalker;

public class SaseBuilder {

	class CompareExpression {
		public List<PathAttribute> attributes = new LinkedList<PathAttribute>();
		private String fullExpression;

		@Override
		public String toString() {
			return attributes + " " + fullExpression;
		}

		boolean contains(String attrib) {
			for (PathAttribute a : attributes) {
				if (attrib.startsWith(a.getStatename())) {
					return true;
				}
			}
			return false;
		}

		public String getFullExpression() {
			return fullExpression;
		}

		public void setFullExpression(String fullExpression) {
			this.fullExpression = fullExpression;
		}
	}

	class PathAttribute {
		private String aggregation;
		private String attribute;
		private String kleenePart;
		private String path;
		private String fullAttribute;
		private String statename;

		public PathAttribute(String attribute, String kleenePart, String path,
				String aggregation) {
			init(attribute, kleenePart, path, aggregation);
		}

		private void init(String attribute, String kleenePart, String path,
				String aggregation) {
			this.attribute = attribute;
			this.kleenePart = kleenePart;
			this.path = path;
			this.aggregation = aggregation;
			this.fullAttribute = ((kleenePart == null) ? attribute : attribute
					+ kleenePart)
					+ "." + path;
			this.statename = attribute;
			if (kleenePart != null) {
				this.statename += (kleenePart.equals("[1]") ? "[1]" : "[i]");
			}
		}
		
		/**
		 * Constructor used for Creation of Id-Expressions
		 * @param statename
		 * @param path
		 */
		public PathAttribute(String statename, String path){
			int kIndex = statename.indexOf("[");
			if (kIndex > 0){
				init(statename.substring(0,kIndex),statename.substring(kIndex), path, Write.class.getSimpleName());
			}else{
				init(statename,null, path, Write.class.getSimpleName());
			}	
		}

		public String getFullAttributeName() {
			return fullAttribute;
		}

		public String getRealAttributeName() {
			return attribute + "." + path;
		}

		public String getAggregation() {
			return aggregation;
		}

		public String getAttribute() {
			return attribute;
		}

		public String getKleenePart() {
			return kleenePart;
		}

		boolean isKleeneAttribute() {
			return kleenePart != null;
		}

		public String getStatename() {
			return statename;
		}

		public String getPath() {
			return path;
		}

		@Override
		public String toString() {
			return attribute + "." + path;
		}
	}

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
		Set<String> sourceNames = new TreeSet<String>();
		List<State> states = null;
		if (tree != null) {
			for (Object child : tree.getChildren()) {
				String cStr = child.toString();
				if (cStr.equalsIgnoreCase("PATTERN")) {
					states = processPattern((CommonTree) child, cepAo, sourceNames);
				} else if (cStr.equalsIgnoreCase("WHERE")) {
					processWhere((CommonTree) child, cepAo, states);
				} else if (cStr.equalsIgnoreCase("WITHIN")) {
					processWithin((CommonTree) child, cepAo);
				} else {
					throw new RuntimeException(cStr + " no valid Syntax");
				}

			}
			System.out.println("Final SM "
					+ cepAo.getStateMachine().prettyPrint());
		}
		// Set Inputs for cepAO;
		int i = 0;
		for (String sn : sourceNames) {
			ILogicalOperator ao = DataDictionary.getInstance().getView(sn);
			if (ao != null) {
				cepAo.subscribeTo(ao, i++, 0, ao.getOutputSchema());
			} else {
				throw new RuntimeException("Source " + sn + " not found");
			}
		}
		return cepAo;
	}

	private List<State> processPattern(CommonTree patternTree, CepAO cepAo,
			Set<String> sourceNames) {
		List<State> states = null;
		for (Object child : patternTree.getChildren()) {
			String cStr = child.toString();
			if (cStr.equalsIgnoreCase("SEQ")) {
				states = processStates(getChildren(child), sourceNames);
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
									+ "_proceed", dest, new JEPCondition(""),
									EAction.consume));
							source.addTransition(new Transition(source.getId()
									+ "_take", dest, new JEPCondition(""),
									EAction.consume));
						} else {
							source.addTransition(new Transition(source.getId()
									+ "_begin", dest, new JEPCondition(""),
									EAction.consume));
						}
						if (i > 0 && i < states.size() - 1) {
							// Ignore auf sich selbst!
							source.addTransition(new Transition(source.getId()
									+ "_ignore", source, new JEPCondition(""),
									EAction.consume));
						}
					}
				} else {
					throw new RuntimeException("Illegal Sequence");
				}

			}
		}
		return states;

	}

	private List<State> processStates(List<CommonTree> states,
			Set<String> sourceNames) {
		List<State> recStates = new LinkedList<State>();
		for (CommonTree state : states) {
			if (state.getChildCount() > 2){
				throw new RuntimeException("Negative States not supported yet!");
			}
			String str = state.getChild(0).toString();
			sourceNames.add(getChild(state, 0).getChild(0).getText());
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

	private void processWhere(CommonTree whereTree, CepAO cepAo, List<State> states) {
		for (Object child : whereTree.getChildren()) {
			String cStr = child.toString();
			if (cStr.equalsIgnoreCase("WHERESTRAT")) {
				processWhereStrat(getChildren(child), cepAo);
			} else if (cStr.equalsIgnoreCase("WHEREEXPRESSION")) {
				processWhereExpression(getChildren(child), cepAo, states);
			}
		}
	}

	private void processWhereStrat(List<CommonTree> children, CepAO cepAo) {
		for (CommonTree t : children) {
			if ("skip_till_next_match".equals(t.toString())) {
				cepAo.getStateMachine().setEventSelectionStrategy(
						EEventSelectionStrategy.SKIP_TILL_NEXT_MATCH);
			} else if ("skip_till_any_match".equals(t.toString())) {
				cepAo.getStateMachine().setEventSelectionStrategy(
						EEventSelectionStrategy.SKIP_TILL_ANY_MATCH);
			} else if ("partition_contiguity".equals(t.toString())) {
				cepAo.getStateMachine().setEventSelectionStrategy(
						EEventSelectionStrategy.PARTITION_CONTIGUITY);
			} else if ("strict_contiguity".equals(t.toString())) {
				cepAo.getStateMachine().setEventSelectionStrategy(
						EEventSelectionStrategy.STRICT_CONTIGUITY);
			} else if ("PARAMLIST".equals(t.toString())) {
				// TODO: Process Param List
				// e.g. Map with AttributeSet and Strategy?
			}
		}
	}

	private void processWhereExpression(List<CommonTree> children, CepAO cepAo, List<State> states) {
		List<CompareExpression> compareExpressions = new ArrayList<CompareExpression>();

		for (CommonTree elem : children) {
			if (elem.toString().equalsIgnoreCase("and")) {
				// Zunächst (?) übergehen
			} else if (elem.toString().equalsIgnoreCase("IDEXPRESSION")) {
				// Achtung. Letzer Zustand ist der Final-Zustand 
				// TODO: Sicherstellen, dass die Reihenfolge der Zustände im Automaten auch der 
				// Reihenfolge im Ausdruck entspricht
				for (int i=0;i<states.size()-2;i++){
					CompareExpression ce = new CompareExpression();
					PathAttribute a = new PathAttribute(states.get(i).getId(), elem.getChild(0).toString());
					ce.attributes.add(a);
					PathAttribute b = new PathAttribute(states.get(i+1).getId(), elem.getChild(0).toString());
					ce.attributes.add(b);
					ce.fullExpression=a.getFullAttributeName()+" == "+b.getFullAttributeName();
					compareExpressions.add(ce);
				}
			} else if (elem.toString().equalsIgnoreCase("COMPAREEXPRESSION")) {
				compareExpressions.add(processCompareExpression(getChildren(elem)));
			}
		}
		StateMachine stmachine = cepAo.getStateMachine();
		List<State> finalStates = stmachine.getFinalStates();
		for (State finalState : finalStates) {
			List<State> pathes = stmachine.getAllPathesToStart(finalState);
			for (State s : pathes) {
				Iterator<CompareExpression> compareIter = compareExpressions
						.iterator();
				while (compareIter.hasNext()) {
					CompareExpression ce = compareIter.next();
					if (ce.contains(s.getId())) {
						Transition t = s.getTransition(s.getId() + "_begin");
						if (t == null) {
							t = s.getTransition(s.getId() + "_take");
						}
						if (t == null) {
							t = s.getTransition(s.getId() + "_proceed");
						}
						// Anpassen der Variablennamen für das Metamodell:
						String fullExpression = tranformAttributesToMetamodell(
								ce, s);
						// String fullExpression = ce.getFullExpression();
						t.append(fullExpression);
						t = s.getTransition(s.getId() + "_ignore");
						if (t != null) {
							switch (stmachine.getEventSelectionStrategy()) {
							case PARTITION_CONTIGUITY:
								t.append(fullExpression);
								// negation later!
								break;
							case SKIP_TILL_NEXT_MATCH:
								t.append(fullExpression);
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
				|| stmachine.getEventSelectionStrategy() == EEventSelectionStrategy.SKIP_TILL_NEXT_MATCH) {
			for (State s : stmachine.getStates()) {
				Transition t = s.getTransition(s.getId() + "_ignore");
				if (t != null) {
					t.negateExpression();
				}
			}
		}
	}

	private String tranformAttributesToMetamodell(CompareExpression ce, State s) {
		// Abhängig vom aktuellen Zustand müssen die Variablen entweder
		// aus der Symboltabelle des ensprechenden Zustand gelesen werden oder
		// sind aktuelle Belegungen
		String ret = new String(ce.getFullExpression());
		for (PathAttribute attrib : ce.attributes) {

			// Attribute des aktuellen Zustands
			if (attrib.getStatename().equals(s.getId())) {
				// Unterscheiden ob aktuell oder historisch
				if (attrib.isKleeneAttribute()) {
					if (attrib.getKleenePart().equals("[1]")) {
						// aktueller Zustand ist z.B. a[1]
						// --> Attribut ist das aktuell gelesene
						ret = replaceStrings(ret,
								attrib.getFullAttributeName(), "###"
										+ attrib.getRealAttributeName());
					} else { // Historisch
						String index = "-1"; // getKleenePart.equals("[i]")
						// a[..i-1] ?
						// a[a.LEN] ?
						if (attrib.getKleenePart().equals("[1]")) {
							index = "0";
						}
						ret = replaceStrings(ret,
								attrib.getFullAttributeName(), attrib
										.getAggregation()
										+ "#"
										+ attrib.getStatename()
										+ "#"
										+ index
										+ "#"
										+ attrib.getRealAttributeName());
					}
				} else {// Kein KleeneAttribute, aber aktuelle Zustand -->
						// aktuelles Event
					ret = replaceStrings(ret, attrib.getFullAttributeName(),
							"###" + attrib.getRealAttributeName());
				}

			} else { // Attribute eines anderen (bereits durchlaufenen) Zustands
				String index = "-1"; // getKleenePart.equals("[i]")
				// a[..i-1] ?
				// a[a.LEN] ?
				if (attrib.isKleeneAttribute() && attrib.getKleenePart().equals("[1]")) {
					index = "0";
				}
				ret = replaceStrings(ret, attrib.getFullAttributeName(), attrib
						.getAggregation()
						+ "#"
						+ attrib.getStatename()
						+ "#"
						+ index
						+ "#"
						+ attrib.getRealAttributeName());
			}
		}

		return ret;
	}

	private String replaceStrings(String ret, String oldString, String newString) {
		// System.out.println("replace "+oldString+" in "+ret+" with "+newString);
		ret = ret.replace(oldString, newString);
		// System.out.println("--> "+ret);
		return ret;
	}

	private CompareExpression processCompareExpression(
			List<CommonTree> children) {
		CompareExpression expr = new CompareExpression();
		expr.setFullExpression(buildStringForExpression(children));
		for (CommonTree elem : children) {
			if (elem.toString().equalsIgnoreCase("KMEMBER")) {
				PathAttribute attr = new PathAttribute(elem.getChild(0)
						.toString(), elem.getChild(1).toString(), elem
						.getChild(2).toString(), Write.class.getSimpleName());
				expr.attributes.add(attr);
			} else if (elem.toString().equalsIgnoreCase("MEMBER")) {
				PathAttribute attr = new PathAttribute(elem.getChild(0)
						.toString(), null, elem.getChild(1).toString(), Write.class.getSimpleName());
				expr.attributes.add(attr);
			} else if (elem.toString().equalsIgnoreCase("AGGREGATION")) {
				PathAttribute attr = new PathAttribute(elem.getChild(1)
						.toString(), elem.getChild(2).toString(), elem
						.getChild(3).toString(), elem.getChild(0).toString());
				expr.attributes.add(attr);
			}
		}
		return expr;
	}

	private String buildStringForExpression(List<CommonTree> children) {
		StringBuffer ret = new StringBuffer();
		for (CommonTree elem : children) {
			if (elem.toString().equalsIgnoreCase("KMEMBER")) {
				ret.append(elem.getChild(0).toString()).append(
						elem.getChild(1).toString()).append(".").append(
						elem.getChild(2).toString());
			} else if (elem.toString().equalsIgnoreCase("MEMBER")) {
				ret.append(elem.getChild(0).toString()).append(".").append(
						elem.getChild(1).toString());
			} else if (elem.toString().equalsIgnoreCase("AGGREGATION")) {
				ret.append(elem.getChild(1).toString()).append(
						elem.getChild(2).toString()).append(".").append(
						elem.getChild(3).toString());
			} else if (elem.toString().equalsIgnoreCase("EQUALS")) {
				ret.append("==");
			} else {
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
		// Zum Testen
		AccessAO source = new AccessAO(new SDFSource("Stock",
				"RelationalStreaming"));
		source.setOutputSchema(new SDFAttributeList());
		DataDictionary.getInstance().setView("Stock", source);

		source = new AccessAO(new SDFSource("Shelf", "RelationalStreaming"));
		source.setOutputSchema(new SDFAttributeList());
		DataDictionary.getInstance().setView("Shelf", source);

		source = new AccessAO(new SDFSource("Register", "RelationalStreaming"));
		source.setOutputSchema(new SDFAttributeList());
		DataDictionary.getInstance().setView("Register", source);

		source = new AccessAO(new SDFSource("Exit", "RelationalStreaming"));
		source.setOutputSchema(new SDFAttributeList());
		DataDictionary.getInstance().setView("Exit", source);

		source = new AccessAO(new SDFSource("Alert", "RelationalStreaming"));
		source.setOutputSchema(new SDFAttributeList());
		DataDictionary.getInstance().setView("Alert", source);

		source = new AccessAO(new SDFSource("Shipment", "RelationalStreaming"));
		source.setOutputSchema(new SDFAttributeList());
		DataDictionary.getInstance().setView("Shipment", source);

		String[] toParse = new String[] {
				"PATTERN SEQ(Stock+ a[], Stock b) WHERE skip_till_next_match(a[],b){ a[1].symbol = a[i].symbol and a[1].symbol=b.symbol and a[1].volume > 1000 and a[i].price > avg(a[..i-1].price) and b.volume < a[a.LEN].volume} WITHIN 1 hour",
				"PATTERN SEQ(Stock+ a[], Stock b) WHERE skip_till_next_match(a[],b){ symbol and a[1].volume > 1000 and a[i].price > avg(a[..i-1].price) and b.volume < a[a.LEN].volume} WITHIN 1 hour",
//				"PATTERN SEQ(Shelf a, ~(Register+ b[]), Exit c) "
//						+ "WHERE skip_till_next_match(a,b,c){ " + "tag_id"
//						+ "}" + "WITHIN 12 hours",
//				"PATTERN SEQ(Shelf a, ~(Register+ b[]), Exit c) "
//						+ "WHERE skip_till_next_match(a,b,c){ "
//						+ "a.tag_id = b.tag_id AND " + "a.tag_id = c.tag_id }"
//						+ "WITHIN 12 hours",
				"PATTERN SEQ(Alert a, Shipment+ b[]) "
						+ "WHERE skip_till_any_match(a,b[]){"
						+ "a.type = \"contaminated\" AND "
						+ "b[1].from = a.site AND " + "b[i].from = b[i-1].to} "
						+ "WITHIN 3 hours" };

		try {
			for (String q : toParse) {
				System.out.println(q);
				ILogicalOperator top = exec.parse(q);
				System.out.println(AbstractTreeWalker.prefixWalk(top,
						new AlgebraPlanToStringVisitor()));

			}
		} catch (RecognitionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
