//package de.uniol.inf.is.odysseus.core.server.sparql.parser;
//
//import java.io.Reader;
//import java.util.ArrayList;
//import java.util.List;
//
//import sun.reflect.generics.reflectiveObjects.NotImplementedException;
//
//import com.hp.hpl.jena.graph.Node;
//import com.hp.hpl.jena.graph.Node_Variable;
//import com.hp.hpl.jena.graph.Triple;
//import com.hp.hpl.jenaUpdated.query.Query;
//import com.hp.hpl.jenaUpdated.query.QueryException;
//import com.hp.hpl.jenaUpdated.query.QueryFactory;
//import com.hp.hpl.jenaUpdated.query.SortCondition;
//import com.hp.hpl.jenaUpdated.query.Syntax;
//import com.hp.hpl.jenaUpdated.sparql.core.BasicPattern;
//import com.hp.hpl.jenaUpdated.sparql.core.Var;
//import com.hp.hpl.jenaUpdated.sparql.expr.Expr;
//import com.hp.hpl.jenaUpdated.sparql.syntax.Element;
//import com.hp.hpl.jenaUpdated.sparql.syntax.ElementAggregation;
//import com.hp.hpl.jenaUpdated.sparql.syntax.ElementDatastream;
//import com.hp.hpl.jenaUpdated.sparql.syntax.ElementFilter;
//import com.hp.hpl.jenaUpdated.sparql.syntax.ElementGroup;
//import com.hp.hpl.jenaUpdated.sparql.syntax.ElementNamedGraph;
//import com.hp.hpl.jenaUpdated.sparql.syntax.ElementOptional;
//import com.hp.hpl.jenaUpdated.sparql.syntax.ElementSimpleWindow;
//import com.hp.hpl.jenaUpdated.sparql.syntax.ElementStartEndPredicate;
//import com.hp.hpl.jenaUpdated.sparql.syntax.ElementStartEndPredicateWindow;
//import com.hp.hpl.jenaUpdated.sparql.syntax.ElementTriplesBlock;
//import com.hp.hpl.jenaUpdated.sparql.syntax.ElementUnion;
//import com.hp.hpl.jenaUpdated.sparql.syntax.ElementWindow;
//import com.hp.hpl.jenaUpdated.sparql.syntax.TemplateGroup;
//import com.hp.hpl.jenaUpdated.sparql.syntax.TemplateTriple;
//
//import de.uniol.inf.is.odysseus.core.server.queryexecution.ExecutionMode;
//import de.uniol.inf.is.odysseus.core.server.querytranslation.IQueryParser;
//import de.uniol.inf.is.odysseus.core.server.querytranslation.logicalops.AbstractLogicalOperator;
//import de.uniol.inf.is.odysseus.core.server.querytranslation.logicalops.AccessAO;
//import de.uniol.inf.is.odysseus.core.server.querytranslation.logicalops.AggregateAO;
//import de.uniol.inf.is.odysseus.core.server.querytranslation.logicalops.ProjectAO;
//import de.uniol.inf.is.odysseus.core.server.querytranslation.logicalops.SortAO;
//import de.uniol.inf.is.odysseus.core.server.querytranslation.logicalops.sparql.NaturalJoinAO;
//import de.uniol.inf.is.odysseus.core.server.querytranslation.logicalops.sparql.SparqlFilterAO;
//import de.uniol.inf.is.odysseus.core.server.querytranslation.logicalops.streaming.WindowAO;
//import de.uniol.inf.is.odysseus.core.server.querytranslation.logicalops.streaming.WindowType;
//import de.uniol.inf.is.odysseus.core.server.querytranslation.logicalops.streaming.sparql.Ask;
//import de.uniol.inf.is.odysseus.core.server.querytranslation.logicalops.streaming.sparql.Construct;
//import de.uniol.inf.is.odysseus.core.server.querytranslation.logicalops.streaming.sparql.DuplicateElimination;
//import de.uniol.inf.is.odysseus.core.server.querytranslation.logicalops.streaming.sparql.LeftJoin;
//import de.uniol.inf.is.odysseus.core.server.querytranslation.logicalops.streaming.sparql.Slice;
//import de.uniol.inf.is.odysseus.core.server.querytranslation.logicalops.streaming.sparql.StartEndPredicate;
//import de.uniol.inf.is.odysseus.core.server.querytranslation.logicalops.streaming.sparql.StartEndPredicateWindow;
//import de.uniol.inf.is.odysseus.core.server.querytranslation.logicalops.streaming.sparql.TriplePatternMatching;
//import de.uniol.inf.is.odysseus.core.server.querytranslation.logicalops.streaming.sparql.Union;
//import de.uniol.inf.is.odysseus.core.server.querytranslation.parser.ParseException;
//import de.uniol.inf.is.odysseus.core.server.sourcedescription.sdf.description.SDFSource;
//import de.uniol.inf.is.odysseus.core.server.sourcedescription.sdf.schema.SDFAttribute;
//import de.uniol.inf.is.odysseus.core.server.sourcedescription.sdf.schema.SDFSchema;
//import de.uniol.inf.is.odysseus.wrapper.SourceType;
//
///**
// * This class translates a sparql query into a query object
// * and provides methods for creating a logical query plan.
// * 
// * @author Andre Bolles <andre.bolles@informatik.uni-oldenburg.de>
// *
// */
//public class SPARQLTranslator implements IQueryParser{
//	
//	private ExecutionMode mode;
//	
//	/**
//	 * creates a SPARQL-Translator with the specified execution mode
//	 * either interval based or positive negative
//	 * 
//	 * @param mode The mode in which every query translated by this SPARQLTranslator object
//	 * will be executed. If null, the interval based approach will be chosen.
//	 */
//	public SPARQLTranslator(ExecutionMode mode){
//		if(mode == null){
//			this.mode = ExecutionMode.INTERVAL_BASED;
//		}
//		else{
//			this.mode = mode;
//		}
//	}
//	
//	@Override
//	public List<AbstractLogicalOperator> parse(Reader reader)
//			throws ParseException {
//		throw new NotImplementedException();
//	}
//	
//	public List<AbstractLogicalOperator> parse(String queryString){
//		Query query = this.translate(queryString);
//		AbstractLogicalOperator plan = this.createQueryPlan(query);
//		List<AbstractLogicalOperator> plans = new ArrayList<AbstractLogicalOperator>();
//		plans.add(plan);
//		return plans;
//	}
//	
//	public Query translate(String queryString){
//		Query query = QueryFactory.create(queryString, Syntax.syntaxSPARQL);
//		return query;
//	}
//	
//	public AbstractLogicalOperator createQueryPlan(Query query){
//		ArrayList<AccessAO> accessOps = new ArrayList<AccessAO>();
//
//		// gebe alle default streams aus
//		for (ElementDatastream ds : query.getDefaultDatastreams()) {
//			SDFSource source = new SDFSource(ds.getGraphURI(),
//					SourceType.RDFStreaming);
//			AccessAO aop = new AccessAO(source);
//			aop.setMode(this.mode);
//			accessOps.add(aop);
//			System.out.println("default stream: " + ds.getGraphURI());
//		}
//
//		// gebe alle named streams aus
//		for (ElementDatastream ds : query.getNamedDatastreams()) {
//			SDFSource source = new SDFSource(ds.getGraphURI(),
//					SourceType.RDFStreaming);
//			AccessAO aop = new AccessAO(source);
//			aop.setMode(this.mode);
//			accessOps.add(aop);
//			System.out.println("named stream: " + ds.getGraphURI());
//		}
//
//		AbstractLogicalOperator root = null;
//
//		AbstractLogicalOperator whereClause = null;
//		if (query.getDefaultDatastreams().size() == 0) {
//			whereClause = createPlan(query.getQueryPattern(), null, query
//					.getNamedDatastreams(), null, null, accessOps);
//		} else if (query.getDefaultDatastreams().size() == 1) {
//			whereClause = createPlan(query.getQueryPattern(), query
//					.getDefaultDatastreams(), query.getNamedDatastreams(),
//					null, null, accessOps);
//		} else if (query.getDefaultDatastreams().size() > 1) {
//			Union union = new Union();
//			union.setMode(this.mode);
//
//			ArrayList<ElementDatastream> tmp1 = new ArrayList<ElementDatastream>();
//			tmp1.add(query.getDefaultDatastreams().get(0));
//			AbstractLogicalOperator child1 = createPlan(query.getQueryPattern(), tmp1, query
//					.getNamedDatastreams(), null, null, accessOps);
//
//			ArrayList<ElementDatastream> tmp2 = new ArrayList<ElementDatastream>();
//			tmp2.add(query.getDefaultDatastreams().get(1));
//			AbstractLogicalOperator child2 = createPlan(query.getQueryPattern(), tmp2, query
//					.getNamedDatastreams(), null, null, accessOps);
//
//			union.setLeftInput(child1);
//			union.setRightInput(child2);
//
//			union.calcOutElements();
//
//			// Process further default streams
//			for (int i = 2; i < query.getDefaultDatastreams().size(); i++) {
//				Union lpo = new Union();
//				lpo.setMode(this.mode);
//				lpo.setLeftInput(union);
//
//				ArrayList<ElementDatastream> tmp = new ArrayList<ElementDatastream>();
//				tmp.add(query.getDefaultDatastreams().get(i));
//
//				AbstractLogicalOperator child = createPlan(query.getQueryPattern(), tmp,
//						query.getNamedDatastreams(), null, null, accessOps);
//
//				lpo.setRightInput(child);
//				union = lpo;
//				union.calcOutElements();
//			}
//
//			whereClause = union;
//		}
//
//		root = whereClause;
//		
//		// if there are aggregations, add the aggregation operator
//		if(!query.getAggregations().isEmpty()){
//			AggregateAO aggAO = new AggregateAO();
//			aggAO.setMode(this.mode);
//			
//			// create the input schema
//			aggAO.setInputSchema(root.getOutputSchema());
//			
//			// add the aggregations
//			for(ElementAggregation ea : query.getAggregations()){
//				SDFAttribute inputAttribute = null;
//				// find the input attribute corresponding to this aggregation
//				for(SDFAttribute in : aggAO.getInputSchema()){
//					if(in.getQualName().equals(ea.getVarName())){
//						inputAttribute = in;
//					}
//				}
//				SDFAttribute outputAttribute = null;
//				// TODO noch nicht korrekt
//				switch(ea.getType()){
//					case AVG: outputAttribute = new SDFAttribute("");
//						outputAttribute.setURI(outputAttribute.hashCode() + "#" + ea.getVarName());
//						break;
//					case SUM: outputAttribute = new SDFAttribute("");
//					outputAttribute.setURI(outputAttribute.hashCode() + "#" + ea.getVarName());
//						break;
//					case MIN: outputAttribute = new SDFAttribute("");
//						outputAttribute.setURI(outputAttribute.hashCode() + "#" + ea.getVarName());
//						break;
//					case MAX: outputAttribute = new SDFAttribute("");
//						outputAttribute.setURI(outputAttribute.hashCode() + "#" + ea.getVarName());
//						break;
//					case COUNT: outputAttribute = new SDFAttribute("");
//						outputAttribute.setURI(outputAttribute.hashCode() + "#" + ea.getVarName());
//						break;
//				}
//				// TODO input und output attribute sollten nicht gleich sein
//				aggAO.addAggregation(inputAttribute, ea.getType(), outputAttribute);
//			}
//			
//			// add the groupings
//			for(String gbVar : query.getGroupByVars()){
//				SDFAttribute gbAtt = null;
//				for(SDFAttribute in: aggAO.getInputSchema()){
//					if(in.getQualName().equals(gbVar)){
//						gbAtt = in;
//					}
//				}
//				aggAO.addGroupingAttribute(gbAtt);
//			}
//			
//			System.out.println("AggregateAO erstellt");
//			for(SDFAttribute att : aggAO.getGroupingAttributes())
//				System.out.print(" " + att.getQualName());
//			
//			System.out.println("\n");
//			
//			aggAO.setInputAO(root);
//			root = aggAO;
//		}
//		
//		if (query.getLimit() != Query.NOLIMIT
//				|| query.getOffset() != Query.NOLIMIT) {
//			AbstractLogicalOperator oldRoot = root;
//			root = new Slice();
//			root.setMode(this.mode);
//			((Slice) root).setLimit(query.getLimit());
//			((Slice) root).setOffset(query.getOffset());
//
//			// find out which slice operator to use.
//			ArrayList<AbstractLogicalOperator> windows = new ArrayList<AbstractLogicalOperator>();
//			this.getWindowsInPlan(oldRoot, windows);
//			WindowType sliceType = this.findOutTypeToUse(windows);
//			((Slice) root).setType(sliceType);
//
//			if (sliceType == WindowType.FIXED_TIME_WINDOW) {
//				// it is not necessary, to check, whether there
//				// are more than one stream or not. If there
//				// are more than one stream it is not problematic
//				// because time is a stream independant value
//				// so it will work.
//				// there must be only fixed windows with the same
//				// width in the array list
//				// and there must be at least one, so get the first
//				// and take its width
//				((Slice) root).setFixedWidth(((WindowAO) windows.get(0)).getWindowSize());
//
//			}
//			// Dieser Fall kann momentan noch nicht eintreten,
//			// da es nur eingeschraenkte Start-/Ende-Praedikatfenster
//			// gibt und fuer dieser immer die Ask- und Slice-Operatoren
//			// fuer gleitende Zeitfenster verwendet werden muessen.
//			else if (sliceType == WindowType.START_END_PREDICATE_WINDOW) {
//				// check if there are more than one stream in
//				// the query plan. if yes, the type of slice operator
//				// to use is for sliding windows, if not
//				// you can use the slice operator for start end predicate
//				// windows, but in this case every window operator
//				// has to know the slice operator to tell him the
//				// intervals.
//				ArrayList<String> streamURIs = new ArrayList<String>();
//				this.getReferredStreams(oldRoot, streamURIs);
//
//				if (streamURIs.size() > 1) {
//					((Slice) root)
//							.setType(WindowType.PERIODIC_TIME_WINDOW);
//				} else if (streamURIs.size() == 1) {
//					// register this operator at all start end predicate
//					// windows.
//					// all the operators in windows must be instance of
//					// StartEndPredicateWindow
//					for (int i = 0; i < windows.size(); i++) {
//						((StartEndPredicateWindow) windows.get(i))
//								.setSlice(root);
//					}
//				} else {
//					throw new QueryException(
//							"No streams have been defined in query.");
//				}
//			}
//
//			root.setInputAO(0, oldRoot);
//			((Slice) root).calcOutElements();
//		}
//
//
//		// add the other operators: orderby, slice, projection, ask, construct
//		if (query.getOrderBy() != null && query.getOrderBy().size() > 0) {
//			AbstractLogicalOperator oldRoot = root;
//			List orderBy = query.getOrderBy();
//			SDFSchema sortVars = new SDFSchema();
//			boolean[] direction = new boolean[orderBy.size()];
//
//			for (int i = 0; i < orderBy.size(); i++) {
//				SortCondition sc = (SortCondition) orderBy.get(i);
//				
//				// only the variables are interesting
//				// you cannot process anything else meaningfully
//				Expr ex = sc.getExpression();
//				Var exVar = ex.asVar();
//				SDFAttribute sortVar = new SDFAttribute(this.hashCode()
//						+ "#"
//						+ exVar.getName());
//				sortVars.add(sortVar);
//				if(sc.direction == Query.ORDER_ASCENDING){
//					direction[i] = true;
//				}
//				else{
//					direction[i] = false;
//				}
//			}
//			
//			root = new SortAO(sortVars);
//			root.setMode(this.mode);
//			((SortAO) root).setAscending(direction);
//			
//			root.setInputAO(0, oldRoot);
//			((SortAO) root).calcOutElements();
//		}
//		if (query.getQueryType() == Query.QueryTypeAsk) {
//			AbstractLogicalOperator oldRoot = root;
//			root = new Ask();
//			root.setMode(this.mode);
//			// find out which slice operator to use.
//			ArrayList<AbstractLogicalOperator> windows = new ArrayList<AbstractLogicalOperator>();
//			this.getWindowsInPlan(oldRoot, windows);
//			WindowType askType = this.findOutTypeToUse(windows);
//			((Ask) root).setType(askType);
//
//			if (askType == WindowType.FIXED_TIME_WINDOW) {
//				// it is not necessary, to check, whether there
//				// are more than one stream or not. If there
//				// are more than one stream it is not problematic
//				// because time is a stream independant value
//				// so it will work.
//				// there must be only fixed windows with the same
//				// width in the array list
//				// and there must be at least one, so get the first
//				// and take its width
//				((Ask) root).setFixedWidth(((WindowAO) windows.get(0)).getWindowSize());
//
//			}
//			// Dieser Fall kann momentan noch nicht eintreten,
//			// da es nur eingeschraenkte Start-/Ende-Praedikatfenster
//			// gibt und fuer dieser immer die Ask- und Slice-Operatoren
//			// fuer gleitende Zeitfenster verwendet werden muessen.
//			else if (askType == WindowType.START_END_PREDICATE_WINDOW) {
//				// check if there are more than one stream in
//				// the query plan. if yes, the type of slice operator
//				// to use is for sliding windows, if not
//				// you can use the slice operator for start end predicate
//				// windows, but in this case every window operator
//				// has to know the slice operator to tell him the
//				// intervals.
//				ArrayList<String> streamURIs = new ArrayList<String>();
//				this.getReferredStreams(oldRoot, streamURIs);
//
//				if (streamURIs.size() > 1) {
//					((Slice) root)
//							.setType(WindowType.PERIODIC_TIME_WINDOW);
//				} else if (streamURIs.size() == 1) {
//					// register this operator at all start end predicate
//					// windows.
//					// all the operators in windows must be instance of
//					// StartEndPredicateWindow
//					for (int i = 0; i < windows.size(); i++) {
//						((StartEndPredicateWindow) windows.get(i)).setAsk(root);
//					}
//				} else {
//					throw new QueryException(
//							"No streams have been defined in query.");
//				}
//			}
//			root.setInputAO(0, oldRoot);
//		} else if (query.getQueryType() == Query.QueryTypeDescribe) {
//			throw new RuntimeException(
//					"Describe queries are not allowed in stream processing.");
//		} else if (query.getQueryType() == Query.QueryTypeConstruct) {
//			AbstractLogicalOperator oldRoot = root;
//
//			// get the triples from the query
//			TemplateGroup constructTemplate = (TemplateGroup) query
//					.getConstructTemplate();
//			List<Triple> triples = new ArrayList<Triple>();
//			List templateTriples = constructTemplate.getTemplates();
//			for (int i = 0; i < templateTriples.size(); i++) {
//				if (templateTriples.get(i) instanceof TemplateTriple) {
//					triples.add(((TemplateTriple) templateTriples.get(i))
//							.getTriple());
//				} else {
//					throw new QueryException(
//							"Undefined Construct Template found: "
//									+ templateTriples.get(i));
//				}
//			}
//			root = new Construct(triples);
//			root.setMode(this.mode);
//			root.setInputAO(0, oldRoot);
//		} else if (query.getQueryType() == Query.QueryTypeSelect) {
//			AbstractLogicalOperator oldRoot = root;
//
//			// get the variables mentioned in the select statement
//			List resultVars = query.getResultVars();
//			SDFSchema selectVars = new SDFSchema();
//			for (int i = 0; i < resultVars.size(); i++) {
//				// the variables are returned as strings.
//				SDFAttribute selectVar = new SDFAttribute(this.hashCode() + "#"
//						+ resultVars.get(i));
//				selectVars.add(selectVar);
//			}
//			// calcOutElements not necessary, because
//			// the out elements will be set in the constructor
//			root = new ProjectAO(selectVars);
//			root.setMode(this.mode);
//			root.setInputAO(0, oldRoot);
//			root.setInputIDSize(0, root.getInputAO(0).getOutputIDSize());
//			root.setInputSchema(0, oldRoot.getOutputSchema());
////			((ProjectAO)root).setRestrictList(SPARQL_Util.calcProjectRestrictList(root.getInputSchema(0), root.getOutputSchema()));
//			
//			
//			// if the query has a distinct term
//			// create a duplicate elimination operator
//			if (query.isDistinct() || query.isReduced()) {
//				AbstractLogicalOperator duplicateElimination = new DuplicateElimination();
//				duplicateElimination.setMode(this.mode);
//				duplicateElimination.setInputAO(0, root);
//				((DuplicateElimination)duplicateElimination).calcOutElements();
//				root = duplicateElimination;
//				if (query.isReduced()) {
//					System.out
//							.println("WARNING: REDUCED will be handled DISTINCT!");
//				}
//			}
//		}
//		return root;
//	}
//	
//	/**
//	 * @param elem
//	 *            The acutal elemt for which to create an operator
//	 * @param activeStream
//	 *            The stream to which to access in the triple pattern matching
//	 *            that are children of the created operator
//	 * @param namedStreams
//	 *            the named streams of the query. used to get the window
//	 *            operators for these streams
//	 * @param the
//	 *            window to set as parent or child of the triple pattern
//	 *            matching that are children of the created operator
//	 * @param streamVar
//	 *            If the element is a named graph pattern and the name of the
//	 *            graph will be stored in a variable, this variable has to be
//	 *            overgiven to the triple pattern matching or window operator.
//	 */
//	private AbstractLogicalOperator createPlan(Element elem,
//			List<ElementDatastream> activeStream,
//			List<ElementDatastream> namedStreams, ElementWindow window,
//			Node_Variable streamVar, ArrayList<AccessAO> accessOps) {
//		AbstractLogicalOperator op = null;
//		if (elem instanceof ElementGroup) {
//			ElementGroup elg = (ElementGroup) elem;
//			window = elg.getWindow();
//
//			// There may be filter, that have to be evaluated last
//			ArrayList<AbstractLogicalOperator> filters = new ArrayList<AbstractLogicalOperator>();
//			ArrayList oldElementFilters = new ArrayList();
//			for (int i = 0; i < elg.getElements().size(); i++) {
//				if (elg.getElements().get(i) instanceof ElementFilter) {
//					AbstractLogicalOperator filter = createPlan((Element) elg.getElements()
//							.get(i), activeStream, namedStreams, null,
//							streamVar, accessOps);
//					filters.add(filter);
//					oldElementFilters.add(elg.getElements().remove(i));
//					i--;
//				}
//			}
//
//			// then put the optional elements to the end of the list
//			// but take care of the order between these optional elements
//			List optionalElements = new ArrayList();
//			for (int i = 0; i < elg.getElements().size(); i++) {
//				if (elg.getElements().get(i) instanceof ElementOptional) {
//					optionalElements.add(elg.getElements().get(i));
//					elg.getElements().remove(i);
//					i--;
//				}
//			}
//			elg.getElements().addAll(optionalElements);
//
//			// if the first element is an optional element, this
//			// element will not be considered as optional, but
//			// as a normal group graph pattern.
//			if (elg.getElements().size() == 1) {
//				if (elg.getElements().get(0) instanceof ElementOptional) {
//					op = createPlan(
//							((ElementOptional) elg.getElements().get(0))
//									.getOptionalElement(), activeStream,
//							namedStreams, null, streamVar, accessOps);
//				}
//				// if the next element is a triples block, the window
//				// from this group graph pattern has to be used
//				else if (elg.getElements().get(0) instanceof ElementTriplesBlock) {
//					op = createPlan((Element) elg.getElements().get(0),
//							activeStream, namedStreams, window, streamVar,
//							accessOps);
//				}
//				// the next element is not a triples block, so the window
//				// cannot be determined at this point. Perhaps there is another
//				// window defined or the window of the corresponding stream
//				// will be taken, but not the window of this group graph
//				// pattern.
//				else {
//					op = createPlan((Element) elg.getElements().get(0),
//							activeStream, namedStreams, null, streamVar,
//							accessOps);
//				}
//			} else if (elg.getElements().size() > 1) {
//				// All the elements in a group graph pattern will be joined
//				// if there are optional elements the join will be a left join
//				op = new NaturalJoinAO();
//				op.setMode(this.mode);
//
//				// =============================================================
//				// first child of join
//				if (elg.getElements().get(0) instanceof ElementOptional) {
//					AbstractLogicalOperator child = createPlan(((ElementOptional) elg
//							.getElements().get(0)).getOptionalElement(),
//							activeStream, namedStreams, null, streamVar,
//							accessOps);
//					((NaturalJoinAO) op).setLeftInput(child);
//					((NaturalJoinAO) op).setInputSchema(0, child.getOutputSchema());
//				}
//				// if the next element is a triples block, the window
//				// from this group graph pattern has to be used
//				else if (elg.getElements().get(0) instanceof ElementTriplesBlock) {
//					AbstractLogicalOperator child = createPlan((Element) elg.getElements()
//							.get(0), activeStream, namedStreams, window,
//							streamVar, accessOps);
//					((NaturalJoinAO) op).setLeftInput(child);
//					((NaturalJoinAO) op).setLeftInputSchema(child.getOutputSchema());
//				}
//				// the next element is not a triples block, so the window
//				// cannot be determined at this point. Perhaps there is another
//				// window defined or the window of the corresponding stream
//				// will be taken, but not the window of this group graph
//				// pattern.
//				else {
//					AbstractLogicalOperator child = createPlan((Element) elg.getElements()
//							.get(0), activeStream, namedStreams, null,
//							streamVar, accessOps);
//					((NaturalJoinAO) op).setLeftInput(child);
//					((NaturalJoinAO) op).setLeftInputSchema(child.getOutputSchema());
//				}
//
//				// =============================================================
//				// second child of join
//				if (elg.getElements().get(1) instanceof ElementOptional) {
//					// we need a left join, so create one from the
//					// natural join
//					AbstractLogicalOperator tmp = op;
//					op = new LeftJoin((NaturalJoinAO) tmp);
//					op.setMode(this.mode);
//
//					// first get all the filters from the optional element
//					// and add the expression of these filters to the left join
//					List<ElementFilter> optfilters = this
//							.getOptionalFilters((ElementOptional) elg
//									.getElements().get(1));
//					for (ElementFilter f : optfilters) {
//						((LeftJoin) op).addFilterExpression(f.getExpr());
//					}
//
//					AbstractLogicalOperator child = createPlan(((ElementOptional) elg
//							.getElements().get(1)).getOptionalElement(),
//							activeStream, namedStreams, null, streamVar,
//							accessOps);
//					op.setInputAO(1, child);
//					op.setInputSchema(1, child.getOutputSchema());
//
//					// put the filters back, they will be needed
//					// if more than one default stream has been defined.
//					try {
//						this.setOptionalFilters((ElementOptional) elg
//								.getElements().get(1), optfilters);
//					} catch (Exception e) {
//						e.printStackTrace();
//					}
//				}
//				// if the next element is a triples block, the window
//				// from this group graph pattern has to be used
//				else if (elg.getElements().get(1) instanceof ElementTriplesBlock) {
//					AbstractLogicalOperator child = createPlan((Element) elg.getElements()
//							.get(0), activeStream, namedStreams, window,
//							streamVar, accessOps);
//					op.setInputAO(1, child);
//					op.setInputSchema(1, child.getOutputSchema());
//				}
//				// the next element is not a triples block, so the window
//				// cannot be determined at this point. Perhaps there is another
//				// window defined or the window of the corresponding stream
//				// will be taken, but not the window of this group graph
//				// pattern.
//				else {
//					AbstractLogicalOperator child = createPlan((Element) elg.getElements()
//							.get(1), activeStream, namedStreams, null,
//							streamVar, accessOps);
//					op.setInputAO(1, child);
//					op.setInputSchema(1, child.getOutputSchema());
//				}
//
//				if (op instanceof NaturalJoinAO) {
//					((NaturalJoinAO) op).calcOutElements();
//				} else if (op instanceof LeftJoin) {
//					((LeftJoin) op).calcOutElements();
//				}
//
//				// Process further elements in the group graph pattern
//				for (int i = 2; i < elg.getElements().size(); i++) {
//					AbstractLogicalOperator lpo = new NaturalJoinAO();
//					lpo.setMode(this.mode);
//					lpo.setInputAO(0, op);
//					if (elg.getElements().get(i) instanceof ElementOptional) {
//						AbstractLogicalOperator tmp = lpo;
//						lpo = new LeftJoin((NaturalJoinAO) tmp);
//						lpo.setMode(this.mode);
//						// first get all the filters, from the optional element
//						List<ElementFilter> optfilters = this
//								.getOptionalFilters((ElementOptional) elg
//										.getElements().get(i));
//						for (ElementFilter f : optfilters) {
//							((LeftJoin) lpo).addFilterExpression(f.getExpr());
//						}
//
//						AbstractLogicalOperator child = createPlan(((ElementOptional) elg
//								.getElements().get(i)).getOptionalElement(),
//								activeStream, namedStreams, null, streamVar,
//								accessOps);
//						lpo.setInputAO(1, child);
//						lpo.setInputSchema(1, child.getOutputSchema());
//						// put the filters back, they will be needed
//						// if more than one default stream has been defined.
//						try {
//							this.setOptionalFilters((ElementOptional) elg
//									.getElements().get(1), optfilters);
//						} catch (Exception e) {
//							e.printStackTrace();
//						}
//					} else if (elg.getElements().get(i) instanceof ElementTriplesBlock) {
//						AbstractLogicalOperator child = createPlan((Element) elg
//								.getElements().get(i), activeStream,
//								namedStreams, window, streamVar, accessOps);
//						lpo.setInputAO(1, child);
//						lpo.setInputSchema(1, child.getOutputSchema());
//					} else {
//						AbstractLogicalOperator child = createPlan((Element) elg
//								.getElements().get(i), activeStream,
//								namedStreams, null, streamVar, accessOps);
//						lpo.setInputAO(1, child);
//						lpo.setInputSchema(1, child.getOutputSchema());
//					}
//
//					op = lpo;
//
//					if (op instanceof NaturalJoinAO) {
//						((NaturalJoinAO) op).calcOutElements();
//					} else if (op instanceof LeftJoin) {
//						((LeftJoin) op).calcOutElements();
//					}
//				}
//			}
//
//			// Put the filters before the operator.
//			// Be aware of the ordering, so begin at the end
//			// of the list.
//			for (int i = filters.size() - 1; i >= 0; i--) {
//				AbstractLogicalOperator tmp = op;
//				op = filters.get(i);
//				op.setInputAO(0, tmp);
//				((SparqlFilterAO) op).calcOutElements();
//			}
//			// Put the filters back, because there can be more
//			// than one default streams and than creating an operator
//			// for this element group will be done more than once.
//			elg.getElements().addAll(oldElementFilters);
//		} else if (elem instanceof ElementTriplesBlock) {
//			BasicPattern elems = ((ElementTriplesBlock) elem).getTriples();
//			List<Triple> triples = elems.getList();
//			if (triples.size() == 1) {
//				op = createPlan(triples.get(0), activeStream, namedStreams,
//						window, streamVar, accessOps);
//			} else {
//				op = new NaturalJoinAO();
//				op.setMode(this.mode);
//				AbstractLogicalOperator child1 = createPlan(triples.get(0), activeStream,
//						namedStreams, window, streamVar, accessOps);
//				AbstractLogicalOperator child2 = createPlan(triples.get(1), activeStream,
//						namedStreams, window, streamVar, accessOps);
//				op.setInputAO(0, child1);
//				op.setInputAO(1, child2);
//				((NaturalJoinAO) op).calcOutElements();
//				for (int i = 2; i < triples.size(); i++) {
//					AbstractLogicalOperator lpo = new NaturalJoinAO();
//					lpo.setMode(this.mode);
//					lpo.setInputAO(0, op);
//					AbstractLogicalOperator child = createPlan(triples.get(i), activeStream,
//							namedStreams, window, streamVar, accessOps);
//					lpo.setInputAO(1, child);
//					op = lpo;
//					((NaturalJoinAO) op).calcOutElements();
//				}
//			}
//
//		} else if (elem instanceof ElementUnion) {
//			List<Element> elems = ((ElementUnion) elem).getElements();
//			if (elems.size() == 1) {
//				op = createPlan(elems.get(0), activeStream, namedStreams,
//						window, streamVar, accessOps);
//			} else {
//				op = new Union();
//				op.setMode(this.mode);
//				AbstractLogicalOperator child1 = createPlan(elems.get(0), activeStream,
//						namedStreams, window, streamVar, accessOps);
//				AbstractLogicalOperator child2 = createPlan(elems.get(1), activeStream,
//						namedStreams, window, streamVar, accessOps);
//				op.setInputAO(0, child1);
//				op.setInputAO(1, child2);
//				((Union) op).calcOutElements();
//				for (int i = 2; i < elems.size(); i++) {
//					AbstractLogicalOperator lpo = new Union();
//					lpo.setMode(this.mode);
//					lpo.setInputAO(0, op);
//					AbstractLogicalOperator child = createPlan(elems.get(i), activeStream,
//							namedStreams, window, streamVar, accessOps);
//					lpo.setInputAO(1, child);
//					op = lpo;
//					((Union) op).calcOutElements();
//				}
//			}
//		} else if (elem instanceof ElementFilter) {
//			ElementFilter filter = (ElementFilter) elem;
//			SparqlFilterAO fpo = new SparqlFilterAO();
//			fpo.setMode(this.mode);
//			fpo.setExpression(filter.getExpr());
//			// CalcOutElements kann hier noch nicht aufgerufen werden, da der InputOperator noch fehlt
//			// fpo.calcOutElements();
//			op = fpo;
//		} else if (elem instanceof ElementNamedGraph) {
//			ElementNamedGraph g = (ElementNamedGraph) elem;
//			Node n = g.getGraphNameNode();
//			if (n.isURI()) {
//				List<ElementDatastream> tmp = new ArrayList<ElementDatastream>();
//				// find the corresponding datastream
//				String uri = n.getURI();
//				for (ElementDatastream ds : namedStreams) {
//					if (ds.getGraphURI().equals(uri)) {
//						tmp.add(ds);
//					}
//				}
//				if (tmp.size() == 0) {
//					throw new RuntimeException("Named Graph with URI \"" + uri
//							+ "\" not found.");
//				}
//				op = createPlan(g.getElement(), tmp, namedStreams, window,
//						streamVar, accessOps);
//			} else if (n.isVariable()) {
//				if (namedStreams.size() == 0) {
//					throw new RuntimeException(
//							"No named graphs specified in query. Query would process"
//									+ " no results.");
//				}
//
//				// There is only one named stream, so you directly add the
//				// subplan to
//				// the query plan. The join will regard the nodevariable.
//				if (namedStreams.size() == 1) {
//					List<ElementDatastream> tmp = new ArrayList<ElementDatastream>();
//					tmp.add(namedStreams.get(0));
//					op = createPlan(g.getElement(), tmp, namedStreams, window,
//							(Node_Variable) n, accessOps);
//				} else if (namedStreams.size() >= 2) {
//					// for every possible datasource generate the subplan and
//					// tell
//					// triple pattern matching operators to set the variable n
//					op = new Union();
//					op.setMode(this.mode);
//
//					ArrayList<ElementDatastream> tmp = new ArrayList<ElementDatastream>();
//					tmp.add(namedStreams.get(0));
//					op
//							.setInputAO(0, createPlan(g.getElement(), tmp,
//									namedStreams, window, (Node_Variable) n,
//									accessOps));
//
//					tmp = new ArrayList<ElementDatastream>();
//					tmp.add(namedStreams.get(1));
//					op
//							.setInputAO(1, createPlan(g.getElement(), tmp,
//									namedStreams, window, (Node_Variable) n,
//									accessOps));
//
//					((Union) op).calcOutElements();
//					for (int i = 2; i < namedStreams.size(); i++) {
//						AbstractLogicalOperator lpo = new Union();
//						lpo.setMode(this.mode);
//						lpo.setInputAO(0, op);
//						tmp = new ArrayList<ElementDatastream>();
//						tmp.add(namedStreams.get(i));
//						lpo.setInputAO(1, createPlan(g.getElement(), tmp,
//								namedStreams, window, (Node_Variable) n,
//								accessOps));
//						op = lpo;
//						((Union) op).calcOutElements();
//					}
//				}
//			}
//		} else if (elem instanceof ElementOptional) {
//			throw new RuntimeException("Unhandled ElementOptional.");
//		} else {
//			throw new RuntimeException("Unknown Element: " + elem);
//		}
//		return op;
//	}
//	
//	/**
//	 * This method creates an operator for a triple
//	 */
//	private AbstractLogicalOperator createPlan(Triple triple,
//			List<ElementDatastream> activeStream,
//			List<ElementDatastream> namedStreams, ElementWindow window,
//			Node_Variable streamVar, ArrayList<AccessAO> accessOps) {
//		System.out.println("=========================");
//		System.out.println("triple.getSubject().isVariable(): "
//				+ triple.getSubject().isVariable());
//		System.out.println("as Variable: "
//				+ ((Var) triple.getSubject()).getVarName());
//		System.out.println("=========================");
//		AbstractLogicalOperator op = null;
//		AbstractLogicalOperator windowOperator = null;
//		// if the window is null, the one from the active graph
//		// has to be taken and set as child of the triple pattern matching.
//		// the active stream must not be null
//		if (activeStream == null || activeStream.size() < 1) {
//			throw new QueryException(
//					"There is no active stream. Cannot create Triple Pattern Matching.");
//		}
//		if (activeStream.size() > 1) {
//			throw new QueryException("Too many active graphs.");
//		}
//		if (window == null) {
//			ElementDatastream ds = (ElementDatastream) activeStream.get(0);
//			window = ds.getWindow();
//			if (window instanceof ElementSimpleWindow) {
//				ElementSimpleWindow stw = (ElementSimpleWindow) window;
//				windowOperator = new WindowAO(stw.getType());
//				windowOperator.setMode(this.mode);
//				((WindowAO)windowOperator).setWindowSize(stw.getRange());
//				((WindowAO)windowOperator).setWindowAdvance(stw.getDelta());
//			} else if (window instanceof ElementStartEndPredicateWindow) {
//				ElementStartEndPredicateWindow sepw = (ElementStartEndPredicateWindow) window;
//				ElementStartEndPredicate startElem = sepw.getStart();
//				ElementStartEndPredicate endElem = sepw.getEnd();
//
//				StartEndPredicate start = new StartEndPredicate();
//				// there has not to be a filter
//				if (startElem.getFilter() != null) {
//					start.setFilter(startElem.getFilter().getExpr());
//				}
//				start.setTriple(startElem.getTriple());
//
//				StartEndPredicate end = new StartEndPredicate();
//				// there has not to be a filter
//				if (endElem.getFilter() != null) {
//					end.setFilter(endElem.getFilter().getExpr());
//				}
//				end.setTriple(endElem.getTriple());
//
//				windowOperator = new StartEndPredicateWindow(start, end);
//				windowOperator.setMode(this.mode);
//				// nicht notwendig, da der Typ im Konstruktor gesetzt wird
//				//((WindowAO)windowOperator).setWindowType(WindowType.START_END_PREDICATE_WINDOW);
//
//				// and now the reducing window has to be set
//				Element redwin = sepw.getReducingWindow();
//				if (redwin instanceof ElementSimpleWindow) {
//					ElementSimpleWindow esdw = (ElementSimpleWindow) redwin;
//					WindowAO tw = new WindowAO(esdw.getType());
//					tw.setMode(this.mode);
//					tw.setWindowSize(esdw.getRange());
//					tw.setWindowAdvance(esdw.getDelta());
//					((StartEndPredicateWindow) windowOperator)
//							.setReducingWindow(tw);
//				}
//			}
//
//			// find the correct stream access operator
//			for (AccessAO access : accessOps) {
//				if (access.getSource().getURI(false).equals(
//						activeStream.get(0).getGraphURI())) {
//					windowOperator.setInputAO(0, access);
//				}
//			}
//
//			// the operator to return is a triple pattern matching
//			op = new TriplePatternMatching(triple);
//			op.setMode(this.mode);
//			op.setInputAO(0, windowOperator);
//			((TriplePatternMatching)op).calcOutIDSize();
//
//			if (streamVar != null) {
//				((TriplePatternMatching) op).setGraphVar(streamVar);
//				((TriplePatternMatching) op).setStream_name(activeStream.get(0)
//						.getGraphURI());
//			}
//			((TriplePatternMatching) op).calcOutElements();
//		}
//		// The window is not null, so it will be parent of the
//		// triple pattern matching.
//		else {
//			if (window instanceof ElementSimpleWindow) {
//				ElementSimpleWindow sdw = (ElementSimpleWindow) window;
//				windowOperator = new WindowAO(sdw.getType());
//				windowOperator.setMode(this.mode);
//				((WindowAO)windowOperator).setWindowSize(sdw.getRange());
//				((WindowAO)windowOperator).setWindowAdvance(sdw.getDelta());
//			} else if (window instanceof ElementStartEndPredicateWindow) {
//				ElementStartEndPredicateWindow sepw = (ElementStartEndPredicateWindow) window;
//				ElementStartEndPredicate startElem = sepw.getStart();
//				ElementStartEndPredicate endElem = sepw.getEnd();
//
//				StartEndPredicate start = new StartEndPredicate();
//				// there has not to be a filter
//				if (startElem.getFilter() != null) {
//					start.setFilter(startElem.getFilter().getExpr());
//				}
//				start.setTriple(startElem.getTriple());
//
//				StartEndPredicate end = new StartEndPredicate();
//				// there has not to be a filter
//				if (endElem.getFilter() != null) {
//					end.setFilter(endElem.getFilter().getExpr());
//				}
//				end.setTriple(endElem.getTriple());
//
//				windowOperator = new StartEndPredicateWindow(start, end);
//				windowOperator.setMode(this.mode);
//				// nicht notwendig, da der Typ im Konstruktor gesetzt wird
//				//((WindowAO)windowOperator).setWindowType(WindowType.START_END_PREDICATE_WINDOW);
//
//				// and now the reducing window has to be set
//				Element redwin = sepw.getReducingWindow();
//				if (redwin instanceof ElementSimpleWindow) {
//					ElementSimpleWindow esdw = (ElementSimpleWindow) redwin;
//					WindowAO tw = new WindowAO(esdw.getType());
//					tw.setMode(this.mode);
//					tw.setWindowSize(esdw.getRange());
//					tw.setWindowAdvance(esdw.getDelta());
//
//					((StartEndPredicateWindow) windowOperator)
//							.setReducingWindow(tw);
//					
//				}
//			}
//			TriplePatternMatching tpm = new TriplePatternMatching(triple);
//			tpm.setMode(this.mode);
//			if (streamVar != null) {
//				((TriplePatternMatching) op).setGraphVar(streamVar);
//				((TriplePatternMatching) op).setStream_name(activeStream.get(0)
//						.getGraphURI());
//			}
//			tpm.calcOutElements();
//			// find the correct stream access operator
//			for (AccessAO access : accessOps) {
//				if (access.getSource().getURI(false).equals(
//						activeStream.get(0).getGraphURI())) {
//					tpm.setInputAO(access);
//				}
//			}
//
//			windowOperator.setInputAO(0, tpm);
//			op = windowOperator;
//			((WindowAO)op).calcOutElements();
//		}
//		return op;
//	}
//	
//	/**
//	 * This method removes all Filters from an optional element an returns them.
//	 * This will be used to add the filters to the leftjoin operator
//	 * 
//	 * @param elem
//	 *            The optional element from which to remove the filters.
//	 * @return A list of ElementFilters that were in the optional elem, but not
//	 *         in subelements.
//	 */
//	private List<ElementFilter> getOptionalFilters(ElementOptional elem) {
//		List<ElementFilter> filters = new ArrayList<ElementFilter>();
//		Element elemOpt = elem.getOptionalElement();
//		if (elemOpt instanceof ElementGroup) {
//			ElementGroup elg = (ElementGroup) elemOpt;
//			for (int i = 0; i < elg.getElements().size(); i++) {
//				if (elg.getElements().get(i) instanceof ElementFilter) {
//					filters.add((ElementFilter) elg.getElements().remove(i));
//					i--;
//				}
//			}
//		}
//		return filters;
//	}
//	
//	/**
//	 * This method puts the filters back to the optional element, if it consists
//	 * of a ElementGroup. This will be used after processing the element in
//	 * createPlan() method.
//	 * 
//	 * @param elem
//	 *            The element to which the filters have to be put back.
//	 * @param filters
//	 *            The filters to put back.
//	 */
//	private void setOptionalFilters(ElementOptional elem,
//			List<ElementFilter> filters) throws Exception {
//		Element elemOpt = elem.getOptionalElement();
//		if (elemOpt instanceof ElementGroup) {
//			ElementGroup elg = (ElementGroup) elemOpt;
//			boolean found = false;
//			for (int i = 0; i < elg.getElements().size() && !found; i++) {
//				if (elg.getElements().get(i) instanceof ElementFilter) {
//					found = true;
//				}
//			}
//
//			if (!found) {
//				// the filters can be added to the end of the list,
//				// because the ordering is not important
//				elg.getElements().addAll(filters);
//			} else {
//				throw new Exception(
//						"Element already contains filters. Something is wrong.");
//			}
//		} else {
//			throw new Exception("Element is not instance of ElementOptional.");
//		}
//	}
//
//	/**
//	 * This method returns all window operators of query plan.
//	 * 
//	 * @param windows
//	 *            The array list, into which the windows will be stored.
//	 */
//	private void getWindowsInPlan(AbstractLogicalOperator plan, ArrayList<AbstractLogicalOperator> windows) {
//		for (int i = 0; i < plan.getNumberOfInputs(); i++) {
//			if (plan.getInputAO(i) instanceof WindowAO) {
//				windows.add(plan.getInputAO(i));
//			} else {
//				getWindowsInPlan(plan.getInputAO(i), windows);
//			}
//		}
//	}
//
//	/**
//	 * This method finds out, for which type of window an ask or slice operator
//	 * has to be used.
//	 * 
//	 * @return A WindowType, for which the operators can be used.
//	 */
//	private WindowType findOutTypeToUse(ArrayList<AbstractLogicalOperator> windows) {
//		// the values for a fixed window
//		long fixedWidth = Long.MIN_VALUE;
//		boolean foundPredicate = false;
//
//		WindowType type = WindowType.JUMPING_TIME_WINDOW;
//		for (int i = 0; i < windows.size(); i++) {
//			WindowAO wao = (WindowAO)windows.get(i);
//			// wenn es nicht feste Zeitfenster sind, dann muessen ask und slice fuer
//			// gleitende Zeitfenster verwendet werden
//			if (wao.getWindowType() == WindowType.JUMPING_TUPLE_WINDOW ||
//			wao.getWindowType() == WindowType.SLIDING_TIME_WINDOW ||
//			wao.getWindowType() == WindowType.SLIDING_TUPLE_WINDOW) {
//				return WindowType.JUMPING_TIME_WINDOW;
//			}
//			else if(wao.getWindowType() == WindowType.JUMPING_TIME_WINDOW){
//				if(wao.getWindowSlide() == wao.getWindowSize()){
//					long newRange = wao.getWindowSize();
//					// nur wenn alle festen Zeitfenster die gleiche Breite haben
//					// koennen ask und slice fuer feste Zeitfenster verwendet werden
//					if ((fixedWidth == newRange || fixedWidth == Long.MIN_VALUE)
//							&& !foundPredicate) {
//						fixedWidth = newRange;
//						type = WindowType.FIXED_TIME_WINDOW;
//					} else {
//						return WindowType.JUMPING_TIME_WINDOW;
//					}
//					return WindowType.FIXED_TIME_WINDOW;
//				}
//			}
//			// bei Start-/Ende-Praedikatfenstern muessen immer die Ask- und Slice-Operatoren
//			// fuer gleitende Zeitfenster verwendet werden.
//			else if(wao.getWindowType() == WindowType.START_END_PREDICATE_WINDOW){
//				return WindowType.JUMPING_TIME_WINDOW;
//			}
//		}
//		return type;
//	}
//
//	/**
//	 * This method returns all streams, that are really used in a query. This
//	 * means, it does not only look a the from clauses, but also to the triple
//	 * pattern matching operators and looks from which stream they get there
//	 * statements. This method only adds URIs to streamURIs, if they are not
//	 * already in this list.
//	 * 
//	 * @param the
//	 *            plan to analyse
//	 * @param streamURIs
//	 *            The arraylist in which the uris will be stored.
//	 */
//	private void getReferredStreams(AbstractLogicalOperator plan, ArrayList<String> streamURIs) {
//		for (int i = 0; i < plan.getNumberOfInputs(); i++) {
//			if (plan.getInputAO(i) instanceof TriplePatternMatching) {
//				TriplePatternMatching tpm = (TriplePatternMatching) plan
//						.getInputAO(i);
//				if (tpm.getStream_name() == null) {
//					if (!streamURIs.contains("default")) {
//						streamURIs.add("default");
//					}
//				} else if (!streamURIs.contains(tpm.getStream_name())) {
//					streamURIs.add(tpm.getStream_name());
//				}
//			}
//			// may be it's not a leaf, so go deeper in the tree
//			else {
//				getReferredStreams(plan.getInputAO(i), streamURIs);
//			}
//		}
//	}
//
//}
