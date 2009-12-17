package de.uniol.inf.is.odysseus.cep.sase;

import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import org.antlr.runtime.ANTLRReaderStream;
import org.antlr.runtime.ANTLRStringStream;
import org.antlr.runtime.RecognitionException;
import org.antlr.runtime.Token;
import org.antlr.runtime.TokenRewriteStream;
import org.antlr.runtime.tree.CommonTree;
import org.antlr.runtime.tree.CommonTreeAdaptor;
import org.antlr.runtime.tree.TreeAdaptor;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

import de.uniol.inf.is.odysseus.base.DataDictionary;
import de.uniol.inf.is.odysseus.base.ILogicalOperator;
import de.uniol.inf.is.odysseus.base.IQueryParser;
import de.uniol.inf.is.odysseus.base.QueryParseException;
import de.uniol.inf.is.odysseus.cep.CepAO;
import de.uniol.inf.is.odysseus.cep.metamodel.CepVariable;
import de.uniol.inf.is.odysseus.cep.metamodel.EAction;
import de.uniol.inf.is.odysseus.cep.metamodel.EEventSelectionStrategy;
import de.uniol.inf.is.odysseus.cep.metamodel.OutputScheme;
import de.uniol.inf.is.odysseus.cep.metamodel.State;
import de.uniol.inf.is.odysseus.cep.metamodel.StateMachine;
import de.uniol.inf.is.odysseus.cep.metamodel.Transition;
import de.uniol.inf.is.odysseus.cep.metamodel.jep.JEPCondition;
import de.uniol.inf.is.odysseus.cep.metamodel.jep.JEPOutputSchemeEntry;
import de.uniol.inf.is.odysseus.cep.metamodel.symboltable.SymbolTableOperationFactory;
import de.uniol.inf.is.odysseus.cep.metamodel.symboltable.Write;
import de.uniol.inf.is.odysseus.logicaloperator.base.AccessAO;
import de.uniol.inf.is.odysseus.logicaloperator.base.AlgebraPlanToStringVisitor;
import de.uniol.inf.is.odysseus.relational.base.RelationalTuple;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.description.SDFSource;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFAttributeList;
import de.uniol.inf.is.odysseus.util.AbstractTreeWalker;

public class SaseBuilder implements IQueryParser, BundleActivator {

	static final TreeAdaptor adaptor = new CommonTreeAdaptor() {
		public Object create(Token payload) {
			return new CommonTree(payload);
		}
	};

	@Override
	public String getLanguage() {
		return "SASE";
	}

	@Override
	public List<ILogicalOperator> parse(Reader reader)
			throws QueryParseException {
		SaseLexer lex = null;
		try {
			lex = new SaseLexer(new ANTLRReaderStream(reader));
		} catch (IOException e) {
			throw new QueryParseException(e);
		}
		return processParse(lex);
	}

	public List<ILogicalOperator> parse(String text) throws QueryParseException {
		SaseLexer lex = new SaseLexer(new ANTLRStringStream(text));
		return processParse(lex);
	}

	private List<ILogicalOperator> processParse(SaseLexer lex)
			throws QueryParseException {
		List<ILogicalOperator> retList = new ArrayList<ILogicalOperator>();
		TokenRewriteStream tokens = new TokenRewriteStream(lex);
		SaseParser grammar = new SaseParser(tokens);
		grammar.setTreeAdaptor(adaptor);
		SaseParser.query_return ret;
		try {
			ret = grammar.query();
		} catch (RecognitionException e) {
			throw new QueryParseException(e);
		}
		CommonTree tree = (CommonTree) ret.getTree();
		// printTree(tree, 2);
		retList.add(createLogicalPlan(tree));
		return retList;
	}

	private ILogicalOperator createLogicalPlan(CommonTree tree) {
		CepAO<RelationalTuple<?>> cepAo = new CepAO<RelationalTuple<?>>();
		Set<String> sourceNames = new TreeSet<String>();
		List<State> states = null;
		if (tree != null) {
			for (Object child : tree.getChildren()) {
				String cStr = child.toString();
				if (cStr.equalsIgnoreCase("PATTERN")) {
					states = processPattern((CommonTree) child, cepAo,
							sourceNames);
				} else if (cStr.equalsIgnoreCase("WHERE")) {
					processWhere((CommonTree) child, cepAo, states);
				} else if (cStr.equalsIgnoreCase("WITHIN")) {
					processWithin((CommonTree) child, cepAo);
				} else if (cStr.equalsIgnoreCase("RETURN")) {
					processReturn((CommonTree) child, cepAo);
				} else {
					throw new RuntimeException(cStr + " no valid Syntax");
				}

			}
			cepAo.getStateMachine().getSymTabScheme(true);
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

	private void processReturn(CommonTree child, CepAO<RelationalTuple<?>> cepAo) {
		List<PathAttribute> retAttr = processAttributes(getChildren(child));
		JEPOutputSchemeEntry e = null;
		OutputScheme scheme = new OutputScheme();
		for (PathAttribute p : retAttr) {
			String op = p.getAggregation();
			String a = p.getAttribute();
			String i = p.getIndex();
			String path = p.getPath();
			e = new JEPOutputSchemeEntry(CepVariable.getStringFor(op, a, i, a
					+ "." + path));
			scheme.append(e);
		}
		cepAo.getStateMachine().setOutputScheme(scheme);
	}

	private List<State> processPattern(CommonTree patternTree,
			CepAO<RelationalTuple<?>> cepAo, Set<String> sourceNames) {
		List<State> states = null;
		for (Object child : patternTree.getChildren()) {
			String cStr = child.toString();
			if (cStr.equalsIgnoreCase("SEQ")) {
				states = processStates(getChildren(child), sourceNames);
				// Append Accepting State
				if (states != null && states.size() > 0) {
					states.add(new State("<ACCEPTING>", "", null, true));
					StateMachine<RelationalTuple<?>> sm = cepAo
							.getStateMachine();
					sm.setStates(states);
					sm.setInitialState(states.get(0));
					// Calculate transistions
					for (int i = 0; i < states.size() - 1; i++) {
						State source = states.get(i);
						State dest = states.get(i + 1);
						if (source.getId().endsWith("[i]")) {
							JEPCondition con = new JEPCondition("");
							con.setEventType(source.getType());
							source.addTransition(new Transition(source.getId()
									+ "_proceed", dest, con, 
									EAction.consumeNoBufferWrite));
							con = new JEPCondition("");
							con.setEventType(source.getType());
							source
									.addTransition(new Transition(source
											.getId()
											+ "_take", source,con,
											EAction.consumeBufferWrite));
						} else {
							JEPCondition con = new JEPCondition("");
							con.setEventType(source.getType());
							source
									.addTransition(new Transition(source
											.getId()
											+ "_begin", dest, con,
											EAction.consumeBufferWrite));
						}
						if (i > 0 && i < states.size() - 1) {
							JEPCondition con = new JEPCondition("");
							// Achtung! Ignore hat keinen Typ!
							//con.setEventType(source.getType());
							// Ignore auf sich selbst!
							source.addTransition(new Transition(source.getId()
									+ "_ignore", source, con,
									EAction.discard));
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
			if (state.getChildCount() > 2) {
				throw new RuntimeException("Negative States not supported yet!");
			}
			String str = state.getChild(0).toString();
			sourceNames.add(getChild(state, 0).getChild(0).getText());
			if (str.equals("KTYPE")) {
				String type = getChild(state, 0).getChild(0).getText();
				String id = getChild(state, 1).getChild(0).getText();
				recStates.add(new State(id + "[1]", id, type, false));
				recStates.add(new State(id + "[i]", id, type, false));
			} else if (str.equals("TYPE")) {
				String type = getChild(state, 0).getChild(0).getText();
				String id = getChild(state, 1).getChild(0).getText();
				recStates.add(new State(id, id, type, false));
			}
		}
		return recStates;
	}

	private void processWhere(CommonTree whereTree,
			CepAO<RelationalTuple<?>> cepAo, List<State> states) {
		for (Object child : whereTree.getChildren()) {
			String cStr = child.toString();
			if (cStr.equalsIgnoreCase("WHERESTRAT")) {
				processWhereStrat(getChildren(child), cepAo);
			} else if (cStr.equalsIgnoreCase("WHEREEXPRESSION")) {
				processWhereExpression(getChildren(child), cepAo, states);
			}
		}
	}

	private void processWhereStrat(List<CommonTree> children,
			CepAO<RelationalTuple<?>> cepAo) {
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

	private void processWhereExpression(List<CommonTree> children,
			CepAO<RelationalTuple<?>> cepAo, List<State> states) {
		List<CompareExpression> compareExpressions = new ArrayList<CompareExpression>();

		for (CommonTree elem : children) {
			if (elem.toString().equalsIgnoreCase("and")) {
				// Zunächst (?) übergehen
			} else if (elem.toString().equalsIgnoreCase("IDEXPRESSION")) {
				// Achtung. Letzer Zustand ist der Final-Zustand
				// TODO: Sicherstellen, dass die Reihenfolge der Zustände im
				// Automaten auch der
				// Reihenfolge im Ausdruck entspricht
				for (int i = 0; i < states.size() - 2; i++) {
					List<PathAttribute> attr = new ArrayList<PathAttribute>();
					PathAttribute a = new PathAttribute(states.get(i).getId(),
							elem.getChild(0).toString());
					attr.add(a);
					PathAttribute b = new PathAttribute(states.get(i + 1)
							.getId(), elem.getChild(0).toString());
					attr.add(b);
					CompareExpression ce = new CompareExpression(attr, a
							.getFullAttributeName()
							+ " == " + b.getFullAttributeName());
					compareExpressions.add(ce);
				}
			} else if (elem.toString().equalsIgnoreCase("COMPAREEXPRESSION")) {
				compareExpressions
						.add(processCompareExpression(getChildren(elem)));
			}
		}
		StateMachine<RelationalTuple<?>> stmachine = cepAo.getStateMachine();
		List<State> finalStates = stmachine.getFinalStates();
		for (State finalState : finalStates) {
			List<State> pathes = stmachine.getAllPathesToStart(finalState);
			for (State s : pathes) {
				Iterator<CompareExpression> compareIter = compareExpressions
						.iterator();
				while (compareIter.hasNext()) {
					CompareExpression ce = compareIter.next();
					// System.out.println(ce);
					PathAttribute attr = ce.get(s.getId());
					if (attr != null) {
						Transition t = s.getTransition(s.getId() + "_begin");

						if (t == null && attr.isKleeneAttribute()) {
							if (attr.getKleenePart().equals("[i]")) {
								t = s.getTransition(s.getId() + "_take");
							} else {
								t = s.getTransition(s.getId() + "_proceed");
							}
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
		// In die Syntax des Metamodells umwandeln
		String ret = new String(ce.getFullExpression());
		for (PathAttribute attrib : ce.getAttributes()) {
			String index = ""; // getKleenePart.equals("[i]")
			String fullAttribName = attrib.getFullAttributeName();
			String aggregation = attrib.getAggregation();
			String stateIdentifier = attrib.getAttribute();
			String realAttributeName = attrib.getRealAttributeName();

			// UNterschiedliche Fälle
			// 1. Das Attribut gehört zum aktuellen Zustand und ist kein
			// KleeneAttribut --> aktuell
			// 2. Das Attribut gehört zum aktuellen Zustand und ist ein
			// Kleenattribut --> historisch
			// 2.a mit dem Kleenepart identisch zum Zustand --> aktuell
			// 2.b. mit einem anderen Kleenpart --> historisch

			if (attrib.getStatename().equals(s.getId())) {
				if (!attrib.isKleeneAttribute()) {
					aggregation = "";
					stateIdentifier = "";
					index = "";
				} else {
					if (s.getId().endsWith(attrib.getKleenePart())) {
						aggregation = "";
						stateIdentifier = "";
						index = "";
					}
				}
			} else {
				if (attrib.isKleeneAttribute()) {
					if (attrib.getKleenePart().equals("[1]")) {
						index = "0";
					} else if (attrib.getKleenePart().equals("[i-1]")) {
						index = "-1";
					} else if (attrib.getKleenePart().equals("[i]")) {
						index = "";
					}
				}
			}

			ret = replaceStrings(ret, fullAttribName, aggregation
					+ CepVariable.getSeperator() + stateIdentifier
					+ CepVariable.getSeperator() + index
					+ CepVariable.getSeperator() + realAttributeName);
		}

		return ret;
	}

	private String replaceStrings(String ret, String oldString, String newString) {
		// System.out.println("replace " + oldString + " in " + ret + " with "
		// + newString);
		ret = ret.replace(oldString, newString);
		// System.out.println("--> " + ret);
		return ret;
	}

	private CompareExpression processCompareExpression(List<CommonTree> children) {
		List<PathAttribute> attribs = processAttributes(children);
		CompareExpression expr = new CompareExpression(attribs,
				buildStringForExpression(children));
		return expr;
	}

	private List<PathAttribute> processAttributes(List<CommonTree> children) {
		List<PathAttribute> attribs = new ArrayList<PathAttribute>();

		for (CommonTree elem : children) {
			if (elem.toString().equalsIgnoreCase("KMEMBER")) {
				PathAttribute attr = new PathAttribute(elem.getChild(0)
						.toString(), elem.getChild(1).toString(), elem
						.getChild(2).toString(), Write.class.getSimpleName());
				attribs.add(attr);
			} else if (elem.toString().equalsIgnoreCase("MEMBER")) {
				PathAttribute attr = new PathAttribute(elem.getChild(0)
						.toString(), null, elem.getChild(1).toString(),
						Write.class.getSimpleName());
				attribs.add(attr);
			} else if (elem.toString().equalsIgnoreCase("AGGREGATION")) {
				PathAttribute attr = new PathAttribute(elem.getChild(1)
						.toString(), elem.getChild(2).toString(), elem
						.getChild(3).toString(), SymbolTableOperationFactory
						.getOperation(elem.getChild(0).toString()).getName());
				// System.out.println("found aggregation " + attr + " "
				// + attr.getAggregation());
				attribs.add(attr);
			}
		}
		return attribs;
	}

	private String buildStringForExpression(List<CommonTree> children) {
		StringBuffer ret = new StringBuffer();
		for (CommonTree elem : children) {
			if (elem.toString().equalsIgnoreCase("KMEMBER")) {
				ret.append(Write.class.getSimpleName()).append("#").append(
						elem.getChild(0).toString()).append(
						elem.getChild(1).toString()).append(".").append(
						elem.getChild(2).toString());
			} else if (elem.toString().equalsIgnoreCase("MEMBER")) {
				ret.append(Write.class.getSimpleName()).append("#").append(
						elem.getChild(0).toString()).append(".").append(
						elem.getChild(1).toString());
			} else if (elem.toString().equalsIgnoreCase("AGGREGATION")) {
				ret.append(elem.getChild(0).toString()).append("#").append(
						elem.getChild(1).toString()).append(
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

	private void processWithin(CommonTree child, CepAO<RelationalTuple<?>> cepAo) {
		String value = getChild(child, 0).getText();
		String unit = getChild(child,1).getText();
		Long time = getTime(value,unit);
		cepAo.getStateMachine().setWindowSize(time);

		// check time explicit, no need to parse Expression at runtime!
//		String var = cepAo.getStateMachine().getInitialState().getId();
//		PathAttribute startVar = new PathAttribute(var, "time");
//		for (State s:cepAo.getStateMachine().getStates()){
//			if (s.getId() != var && !s.isAccepting()){
//				PathAttribute eventVar = new PathAttribute(s.getId(),"time");
//				ArrayList<PathAttribute> attributes = new ArrayList<PathAttribute>();
//				attributes.add(eventVar);
//				attributes.add(startVar);
//				CompareExpression ce = new CompareExpression(attributes, Write.class.getSimpleName()+"#"+s.getId()+".time < "+Write.class.getSimpleName()+"#"+var+".time + "+time);
//				for (Transition t: s.getTransitions()){
//					String timeExpression = tranformAttributesToMetamodell(ce, s);
//					System.out.println(t.getId()+" add: "+timeExpression);
//					t.append(timeExpression);
//				}
//			}
//			
//		}
		
	}
	
	private long getTime(String value, String unit){
		long time = Long.parseLong(value);
		
		if ("day".equals(unit) || "days".equals(unit)){
			time *=24*60*60*1000;
		}else if ("hour".equals(unit)|| "hours".equals(unit)){
			time *=60*60*1000;
		}else if ("minute".equals(unit) || "minutes".equals(unit)){
			time *=60*1000;
		}else if ("second".equals(unit) || "seconds".equals(unit)){
			time *=1000;
		}
		return time;		
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

	@Override
	public void start(BundleContext arg0) throws Exception {

	}

	@Override
	public void stop(BundleContext arg0) throws Exception {

	}

	public static void main(String[] args) {
		SaseBuilder exec = new SaseBuilder();
		// Zum Testen
		createDummySource("Stock");
		createDummySource("Shelf");
		createDummySource("Register");
		createDummySource("Exit");
		createDummySource("Alert");
		createDummySource("Shipment");
		createDummySource("nexmark:bid2");
		createDummySource("nexmark:person2");
		createDummySource("nexmark:auction2");
		createDummySource("nexmark:category2");
		createDummySource("nexmark:bid");
		createDummySource("nexmark:person");
		createDummySource("nexmark:auction");
		createDummySource("nexmark:category");

		String[] toParse = new String[] {
		 "PATTERN SEQ(Stock+ a[], Stock b) WHERE skip_till_next_match(a[],b){ a[1].symbol = a[i].symbol and a[1].symbol=b.symbol and a[1].volume > 1000 and a[i].price > Sum(a[..i-1].price)/Count(a[..i-1].price) and b.volume < a[a.LEN].volume} WITHIN 1 hour",
		 "PATTERN SEQ(Stock+ a[], Stock b) WHERE skip_till_next_match(a[],b){ symbol and a[1].volume > 1000 and a[i].price > Sum(a[..i-1].price)/Count(a[..i-1].price) and b.volume < a[a.LEN].volume} WITHIN 1 hour",
		// "PATTERN SEQ(Shelf a, ~(Register+ b[]), Exit c) "+
//		  "WHERE skip_till_next_match(a,b,c){ " + "tag_id"
//		 + "}" + "WITHIN 12 hours",
//		 //"PATTERN SEQ(Shelf a, ~(Register+ b[]), Exit c) "+
//		  "WHERE skip_till_next_match(a,b,c){ "
//		 + "a.tag_id = b.tag_id AND " + "a.tag_id = c.tag_id }"
//		 + "WITHIN 12 hours",
		 "PATTERN SEQ(Alert a, Shipment+ b[]) "
		 + "WHERE skip_till_any_match(a,b[]){"
		 + "a.type = \"contaminated\" AND " + "b[1].from = a.site AND "
		 + "b[i].from = b[i-1].to} " + "WITHIN 3 hours",
		 "PATTERN SEQ(Stock+ a[], Stock b) WHERE skip_till_next_match(a[],b){ symbol and a[1].volume > 1000 and a[i].price > Sum(a[..i-1].price)/Count(a[..i-1].price) and b.volume < 0.8 * a[a.LEN].volume} WITHIN 1 hour",
		 "PATTERN SEQ(nexmark:person2 person, nexmark:auction2+ auction[]) WHERE skip_till_any_match(person, auction){person.id = auction.seller}",
		 "PATTERN SEQ(nexmark:person2 person, nexmark:auction2 auction) WHERE skip_till_any_match(person, auction){person.id = auction.seller}",
		"PATTERN SEQ(nexmark:person2 person, nexmark:auction2 auction, nexmark:bid2+ bid[]) WHERE skip_till_any_match(person, auction, bid){person.id = auction.seller and auction.id = bid[1].auction and bid[i].auction = bid[i-1].auction and Count(bid[..i-1].bidder)>2} WITHIN 1 hour RETURN person.id, person.name, autcion.id" };
		try {
			for (String q : toParse) {
				System.out.println(q);
				List<ILogicalOperator> top = exec.parse(q);
				CepAO<RelationalTuple<?>> cepAo = (CepAO<RelationalTuple<?>>) top
						.get(0);
				System.out.println("Final SM "
						+ cepAo.getStateMachine().prettyPrint());
				System.out.println(AbstractTreeWalker.prefixWalk(top.get(0),
						new AlgebraPlanToStringVisitor()));
			}
		} catch (QueryParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private static void createDummySource(String sourcename) {
		AccessAO source;
		source = new AccessAO(new SDFSource(sourcename, "RelationalStreaming"));
		source.setOutputSchema(new SDFAttributeList());
		DataDictionary.getInstance().setView(sourcename, source);
	}

}

class CompareExpression {
	private List<PathAttribute> _attributes = null;
	private String _fullExpression = null;

	public CompareExpression(List<PathAttribute> attributes,
			String fullExpression) {
		super();
		_attributes = attributes;
		_fullExpression = fullExpression;
	}

	@Override
	public String toString() {

		return _attributes + " " + _fullExpression;
	}

	boolean contains(String attrib) {
		return get(attrib) != null;
	}

	PathAttribute get(String attrib) {
		for (PathAttribute a : _attributes) {
			if (attrib.startsWith(a.getStatename())) {
				return a;
			}
		}
		return null;
	}

	public String getFullExpression() {
		return _fullExpression;
	}

	public void setFullExpression(String fullExpression) {
		_fullExpression = fullExpression;
	}

	public List<PathAttribute> getAttributes() {
		return _attributes;
	}

}

class PathAttribute {
	private String _aggregation;
	private String _attribute;
	private String _kleenePart;
	private String _path;
	private String _fullAttribute;
	private String _statename;
	private String _index;

	public PathAttribute(String attribute, String kleenePart, String path,
			String aggregation) {
		init(attribute, kleenePart, path, aggregation);
	}

	public String getIndex() {
		return _index;
	}

	private void init(String attribute, String kleenePart, String path,
			String aggregation) {
		_attribute = attribute;
		_kleenePart = kleenePart;
		_path = path;
		_aggregation = aggregation;
		_fullAttribute = aggregation + "#"
				+ ((kleenePart == null) ? attribute : attribute + kleenePart)
				+ "." + path;
		_statename = attribute;
		if (kleenePart != null) {
			_statename += (kleenePart.equals("[1]") ? "[1]" : "[i]");
			_index = kleenePart.substring(kleenePart.indexOf("[") + 1,
					kleenePart.indexOf("]"));
			try{
				// Test if _index is a Number
				Integer.parseInt(_index);
			}catch(NumberFormatException e){
				_index=null;
			}
		}
	}

	/**
	 * Constructor used for Creation of Id-Expressions
	 * 
	 * @param statename
	 * @param path
	 */
	public PathAttribute(String statename, String path) {
		int kIndex = statename.indexOf("[");
		if (kIndex > 0) {
			init(statename.substring(0, kIndex), statename.substring(kIndex),
					path, Write.class.getSimpleName());
		} else {
			init(statename, null, path, Write.class.getSimpleName());
		}
	}

	public String getFullAttributeName() {
		return _fullAttribute;
	}

	public String getRealAttributeName() {
		return _attribute + "." + _path;
	}

	public String getAggregation() {
		return _aggregation;
	}

	public String getAttribute() {
		return _attribute;
	}

	public String getKleenePart() {
		return _kleenePart;
	}

	boolean isKleeneAttribute() {
		return _kleenePart != null;
	}

	public String getStatename() {
		return _statename;
	}

	public String getPath() {
		return _path;
	}

	@Override
	public String toString() {
		return _attribute + "." + _path;
	}
}