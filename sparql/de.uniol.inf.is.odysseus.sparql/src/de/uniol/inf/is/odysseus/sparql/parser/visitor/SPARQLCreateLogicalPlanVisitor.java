package de.uniol.inf.is.odysseus.sparql.parser.visitor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import de.uniol.inf.is.odysseus.datadictionary.IDataDictionary;
import de.uniol.inf.is.odysseus.logicaloperator.AccessAO;
import de.uniol.inf.is.odysseus.logicaloperator.AggregateAO;
import de.uniol.inf.is.odysseus.logicaloperator.FileSinkAO;
import de.uniol.inf.is.odysseus.logicaloperator.ILogicalOperator;
import de.uniol.inf.is.odysseus.logicaloperator.JoinAO;
import de.uniol.inf.is.odysseus.logicaloperator.LeftJoinAO;
import de.uniol.inf.is.odysseus.logicaloperator.ProjectAO;
import de.uniol.inf.is.odysseus.logicaloperator.SelectAO;
import de.uniol.inf.is.odysseus.logicaloperator.TimestampAO;
import de.uniol.inf.is.odysseus.logicaloperator.UnionAO;
import de.uniol.inf.is.odysseus.logicaloperator.WindowAO;
import de.uniol.inf.is.odysseus.predicate.IPredicate;
import de.uniol.inf.is.odysseus.predicate.TruePredicate;
import de.uniol.inf.is.odysseus.relational.base.RelationalAccessSourceTypes;
import de.uniol.inf.is.odysseus.relational.base.predicate.TypeSafeRelationalPredicate;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.description.SDFSource;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.DirectAttributeResolver;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.IAttributeResolver;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFAttributeList;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFDatatype;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFExpression;
import de.uniol.inf.is.odysseus.sparql.logicaloperator.DuplicateElimination;
import de.uniol.inf.is.odysseus.sparql.logicaloperator.TriplePatternMatching;
import de.uniol.inf.is.odysseus.sparql.parser.ast.ASTAdditiveExpression;
import de.uniol.inf.is.odysseus.sparql.parser.ast.ASTAggregation;
import de.uniol.inf.is.odysseus.sparql.parser.ast.ASTArgList;
import de.uniol.inf.is.odysseus.sparql.parser.ast.ASTAskQuery;
import de.uniol.inf.is.odysseus.sparql.parser.ast.ASTBaseDecl;
import de.uniol.inf.is.odysseus.sparql.parser.ast.ASTBlankNode;
import de.uniol.inf.is.odysseus.sparql.parser.ast.ASTBlankNodePropertyList;
import de.uniol.inf.is.odysseus.sparql.parser.ast.ASTBooleanLiteral;
import de.uniol.inf.is.odysseus.sparql.parser.ast.ASTBrackettedExpression;
import de.uniol.inf.is.odysseus.sparql.parser.ast.ASTBuiltInCall;
import de.uniol.inf.is.odysseus.sparql.parser.ast.ASTCSVSource;
import de.uniol.inf.is.odysseus.sparql.parser.ast.ASTChannel;
import de.uniol.inf.is.odysseus.sparql.parser.ast.ASTCollection;
import de.uniol.inf.is.odysseus.sparql.parser.ast.ASTCompilationUnit;
import de.uniol.inf.is.odysseus.sparql.parser.ast.ASTConditionalAndExpression;
import de.uniol.inf.is.odysseus.sparql.parser.ast.ASTConditionalOrExpression;
import de.uniol.inf.is.odysseus.sparql.parser.ast.ASTConstraint;
import de.uniol.inf.is.odysseus.sparql.parser.ast.ASTConstructQuery;
import de.uniol.inf.is.odysseus.sparql.parser.ast.ASTConstructTemplate;
import de.uniol.inf.is.odysseus.sparql.parser.ast.ASTConstructTriples;
import de.uniol.inf.is.odysseus.sparql.parser.ast.ASTCreateStatement;
import de.uniol.inf.is.odysseus.sparql.parser.ast.ASTDatastreamClause;
import de.uniol.inf.is.odysseus.sparql.parser.ast.ASTDescribeQuery;
import de.uniol.inf.is.odysseus.sparql.parser.ast.ASTExpression;
import de.uniol.inf.is.odysseus.sparql.parser.ast.ASTFilter;
import de.uniol.inf.is.odysseus.sparql.parser.ast.ASTFromClause;
import de.uniol.inf.is.odysseus.sparql.parser.ast.ASTFunctionCall;
import de.uniol.inf.is.odysseus.sparql.parser.ast.ASTGraphGraphPattern;
import de.uniol.inf.is.odysseus.sparql.parser.ast.ASTGraphNode;
import de.uniol.inf.is.odysseus.sparql.parser.ast.ASTGraphPatternNotTriples;
import de.uniol.inf.is.odysseus.sparql.parser.ast.ASTGraphTerm;
import de.uniol.inf.is.odysseus.sparql.parser.ast.ASTGroupBy;
import de.uniol.inf.is.odysseus.sparql.parser.ast.ASTGroupGraphPattern;
import de.uniol.inf.is.odysseus.sparql.parser.ast.ASTGroupOrUnionGraphPattern;
import de.uniol.inf.is.odysseus.sparql.parser.ast.ASTHost;
import de.uniol.inf.is.odysseus.sparql.parser.ast.ASTIRI_REF;
import de.uniol.inf.is.odysseus.sparql.parser.ast.ASTIRIref;
import de.uniol.inf.is.odysseus.sparql.parser.ast.ASTIRIrefOrFunction;
import de.uniol.inf.is.odysseus.sparql.parser.ast.ASTIdentifier;
import de.uniol.inf.is.odysseus.sparql.parser.ast.ASTLimitClause;
import de.uniol.inf.is.odysseus.sparql.parser.ast.ASTLimitOffsetClauses;
import de.uniol.inf.is.odysseus.sparql.parser.ast.ASTMultiplicativeExpression;
import de.uniol.inf.is.odysseus.sparql.parser.ast.ASTNumericExpression;
import de.uniol.inf.is.odysseus.sparql.parser.ast.ASTNumericLiteral;
import de.uniol.inf.is.odysseus.sparql.parser.ast.ASTNumericLiteralNegative;
import de.uniol.inf.is.odysseus.sparql.parser.ast.ASTNumericLiteralPositive;
import de.uniol.inf.is.odysseus.sparql.parser.ast.ASTNumericLiteralUnsigned;
import de.uniol.inf.is.odysseus.sparql.parser.ast.ASTObject;
import de.uniol.inf.is.odysseus.sparql.parser.ast.ASTObjectList;
import de.uniol.inf.is.odysseus.sparql.parser.ast.ASTOffsetClause;
import de.uniol.inf.is.odysseus.sparql.parser.ast.ASTOptionalGraphPattern;
import de.uniol.inf.is.odysseus.sparql.parser.ast.ASTOrderClause;
import de.uniol.inf.is.odysseus.sparql.parser.ast.ASTOrderCondition;
import de.uniol.inf.is.odysseus.sparql.parser.ast.ASTPrefixDecl;
import de.uniol.inf.is.odysseus.sparql.parser.ast.ASTPrefixedName;
import de.uniol.inf.is.odysseus.sparql.parser.ast.ASTPrimaryExpression;
import de.uniol.inf.is.odysseus.sparql.parser.ast.ASTPrologue;
import de.uniol.inf.is.odysseus.sparql.parser.ast.ASTPropertyList;
import de.uniol.inf.is.odysseus.sparql.parser.ast.ASTPropertyListNotEmpty;
import de.uniol.inf.is.odysseus.sparql.parser.ast.ASTQuery;
import de.uniol.inf.is.odysseus.sparql.parser.ast.ASTRDFLiteral;
import de.uniol.inf.is.odysseus.sparql.parser.ast.ASTRegexExpression;
import de.uniol.inf.is.odysseus.sparql.parser.ast.ASTRelationalExpression;
import de.uniol.inf.is.odysseus.sparql.parser.ast.ASTSelectQuery;
import de.uniol.inf.is.odysseus.sparql.parser.ast.ASTSlidingTimeWindow;
import de.uniol.inf.is.odysseus.sparql.parser.ast.ASTSlidingTupelWindow;
import de.uniol.inf.is.odysseus.sparql.parser.ast.ASTSocket;
import de.uniol.inf.is.odysseus.sparql.parser.ast.ASTSolutionModifier;
import de.uniol.inf.is.odysseus.sparql.parser.ast.ASTSourceSelector;
import de.uniol.inf.is.odysseus.sparql.parser.ast.ASTStreamClause;
import de.uniol.inf.is.odysseus.sparql.parser.ast.ASTString;
import de.uniol.inf.is.odysseus.sparql.parser.ast.ASTTimeunit;
import de.uniol.inf.is.odysseus.sparql.parser.ast.ASTTriplesBlock;
import de.uniol.inf.is.odysseus.sparql.parser.ast.ASTTriplesNode;
import de.uniol.inf.is.odysseus.sparql.parser.ast.ASTTriplesSameSubject;
import de.uniol.inf.is.odysseus.sparql.parser.ast.ASTUnaryExpression;
import de.uniol.inf.is.odysseus.sparql.parser.ast.ASTValueLogical;
import de.uniol.inf.is.odysseus.sparql.parser.ast.ASTValueSpecification;
import de.uniol.inf.is.odysseus.sparql.parser.ast.ASTVar;
import de.uniol.inf.is.odysseus.sparql.parser.ast.ASTVarOrIRIref;
import de.uniol.inf.is.odysseus.sparql.parser.ast.ASTVarOrTerm;
import de.uniol.inf.is.odysseus.sparql.parser.ast.ASTVerb;
import de.uniol.inf.is.odysseus.sparql.parser.ast.ASTWhereClause;
import de.uniol.inf.is.odysseus.sparql.parser.ast.ASTWindow;
import de.uniol.inf.is.odysseus.sparql.parser.ast.ASTWindowNotPWindow;
import de.uniol.inf.is.odysseus.sparql.parser.ast.Node;
import de.uniol.inf.is.odysseus.sparql.parser.ast.SPARQLParserVisitor;
import de.uniol.inf.is.odysseus.sparql.parser.ast.SimpleNode;
import de.uniol.inf.is.odysseus.sparql.parser.helper.AggregateFunctionName;
import de.uniol.inf.is.odysseus.sparql.parser.helper.Aggregation;
import de.uniol.inf.is.odysseus.sparql.parser.helper.INode;
import de.uniol.inf.is.odysseus.sparql.parser.helper.SPARQLDirectAttributeResolver;
import de.uniol.inf.is.odysseus.sparql.parser.helper.SourceInfo;
import de.uniol.inf.is.odysseus.sparql.parser.helper.Triple;
import de.uniol.inf.is.odysseus.sparql.parser.helper.Variable;
import de.uniol.inf.is.odysseus.usermanagement.ISession;


/**
 * This visitor creates a logical SPARQL plan from a SPARQL query.
 * IMPORTANT: data[0] = the logical operator generated from child nodes
 * 
 * Andre Bolles <andre.bolles@uni-oldenburg.de>
 *
 */
@SuppressWarnings({"rawtypes","unchecked"})
public class SPARQLCreateLogicalPlanVisitor implements SPARQLParserVisitor{

	private ISession user;
	private IDataDictionary dd;
	
	
	private HashMap<String, String> prefixes;
	
	private List<SourceInfo> namedStreams;
	private List<SourceInfo> defaultStreams;
	
	private boolean isCreateStatement;
	@SuppressWarnings("unused")
	private String baseURI;
	
	public SPARQLCreateLogicalPlanVisitor(){
		this.prefixes = new HashMap<String, String>();
		this.namedStreams = new ArrayList<SourceInfo>();
		this.defaultStreams = new ArrayList<SourceInfo>();
		this.isCreateStatement = false;
	}
	
	
	public void setUser(ISession user) {
		this.user = user;
	}
	
	public void setDataDictionary(IDataDictionary dd){
		this.dd = dd;
	}
	
	/**
	 * 
	 * @return True, if the last AST was from a create statement. False otherwise
	 */
	public boolean isCreateStatement(){
		return this.isCreateStatement;
	}
	
	// ========================= VISIT METHODS =====================================
	
	@Override
	public Object visit(SimpleNode node, Object data) {
		return node.childrenAccept(this, data);
	}

	@Override
	public Object visit(ASTCompilationUnit node, Object data) {
		return node.childrenAccept(this, data);
	}

	@Override
	public Object visit(ASTQuery node, Object data) {
		// create statement
		if(node.jjtGetChild(0) instanceof ASTCreateStatement){
			return node.jjtGetChild(0).jjtAccept(this, data);
		}
		// prologue statement
		else{
			// prologue statement
			node.jjtGetChild(0).jjtAccept(this, data);
			
			// query statement
			return node.jjtGetChild(1).jjtAccept(this, data);
		}
	}

	@Override
	public Object visit(ASTPrologue node, Object data) {
		return node.childrenAccept(this, data);
	}

	@Override
	public Object visit(ASTBaseDecl node, Object data) {
		this.baseURI = node.getBaseURI();
		return null;
	}

	@Override
	public Object visit(ASTPrefixDecl node, Object data) {
		this.prefixes.put(node.getPrefix(), node.getIRI());
		return null;
	}

	@Override
	public Object visit(ASTSelectQuery node, Object data) {
		
		ILogicalOperator logOp = null;
		
		this.defaultStreams = node.getDefaultStreams();
		this.namedStreams = node.getNamedStreams();
		
		// check if all streams are available
		for(SourceInfo si: this.defaultStreams){
			boolean found = false;
			if(this.dd.containsViewOrStream(si.getStreamName(), this.user)){
				found = true;
				continue;
			}
			if(!found){
				throw new RuntimeException("No stream definition found for default stream: '" + si.getStreamName() + "'");
			}
		}
		
		for(SourceInfo si: this.namedStreams){
			boolean found = false;
			if(this.dd.containsViewOrStream(si.getStreamName(), this.user)){
				found = true;
				continue;
			}
			if(!found){
				throw new RuntimeException("No stream definition found for named stream: '" + si.getStreamName() + "'");
			}
		}
		
		ILogicalOperator inputForProjection = null;
		for(int i = 0; i<node.jjtGetNumChildren(); i++){
			Node curChild = node.jjtGetChild(i);
			if(curChild instanceof ASTWhereClause){
				inputForProjection = (ILogicalOperator)((LinkedList)curChild.jjtAccept(this, new LinkedList())).get(0);
				break;
			}
		}
		
		
		if(node.isResultStar()){
			logOp = new ProjectAO();
		}
		else{
			if(node.getAggregations().isEmpty()){
				ProjectAO projectAO = new ProjectAO();
				
				SDFAttributeList outputSchema = new SDFAttributeList("");
				
				/* In SPARQL Variables are used only for natural joins.
				 * Odysseus does not provide a natural join and therefore
				 * simply uses an equi join. However, in this case one
				 * or more attributes with the same name but different
				 * source information are available. Since all attributes
				 * with the same name must have the same value, at this place
				 * we can simply take the first attribute with the correct name.
				 */
				for(Variable curVar: node.getResultVars()){
					for(SDFAttribute curAttr: inputForProjection.getOutputSchema()){
						if(curAttr.getAttributeName().equals(curVar.getName())){
							outputSchema.add(curAttr);
							break;
						}
					}
				}
				
				projectAO.setOutputSchema(outputSchema);
				logOp = projectAO;
			}
			else{
				AggregateAO aggAO = new AggregateAO();
				
				// connect to the input operator
				aggAO.subscribeToSource(inputForProjection, 0, 0, inputForProjection.getOutputSchema());
				
				Iterator<Variable> resVarsIter = node.getResultVars().iterator();
				Iterator<Aggregation> resAggIter = node.getAggregations().iterator();
				
				for(int i = 0; i<node.getOutputSchemaSize(); i++){
					int varOrAgg = node.getVarOrAgg(i);
					// it's a grouping variable
					if(varOrAgg == 0){
						Variable gv = resVarsIter.next();
						
						/* In SPARQL Variables are used only for natural joins.
						 * Odysseus does not provide a natural join and therefore
						 * simply uses an equi join. However, in this case one
						 * or more attributes with the same name but different
						 * source information is available. Since all attributes
						 * with the same name must have the same value, at this place
						 * we can simply take the first attribute with the correct name.
						 */
						for(SDFAttribute curAttr: aggAO.getInputSchema()){
							if(curAttr.getAttributeName().equals(gv.getName())){
								aggAO.addGroupingAttribute(curAttr);
								break;
							}
						}
					}
					// it's an aggregation
					else{
						Aggregation agg = resAggIter.next();
						Variable aggVar = agg.getVariable();
						
						SDFAttribute aggAttr = null;
						// find the input attribute
						for(SDFAttribute curAttr: aggAO.getInputSchema()){
							if(curAttr.getAttributeName().equals(aggVar.getName())){
								aggAttr = curAttr;
								break;
							}
						}
						
						de.uniol.inf.is.odysseus.physicaloperator.AggregateFunction physAggFunc = 
							new de.uniol.inf.is.odysseus.physicaloperator.AggregateFunction(agg.getAggFunc().toString());
						aggAO.addAggregation(aggAttr, physAggFunc, outAttribute(aggAttr.getAttributeName(), agg.getAggFunc()));
					}
				}
				
				// now check the group by clause
				for(int i = 0; i<node.jjtGetNumChildren(); i++){
					Node curNode = node.jjtGetChild(i);
					if(curNode instanceof ASTGroupBy){
						ASTGroupBy gb = (ASTGroupBy)curNode;
						List<Variable> variables = gb.getVariables();
						
						List<SDFAttribute> groupingAttrs = aggAO.getGroupingAttributes();
						// check if each grouping variable is in the list of grouping attributes
						// of the aggregation. If not, add the current variable
						for(Variable curVar: variables){
							boolean found = false;
							for(SDFAttribute curAttr: groupingAttrs){
								if(curAttr.getAttributeName().equals(curVar.getName())){
									found = true;
									break;
								}
							}
							
							if(!found){
								/* In SPARQL Variables are used only for natural joins.
								 * Odysseus does not provide a natural join and therefore
								 * simply uses an equi join. However, in this case one
								 * or more attributes with the same name but different
								 * source information is available. Since all attributes
								 * with the same name must have the same value, at this place
								 * we can simply take the first attribute with the correct name.
								 */
								for(SDFAttribute curAttr: aggAO.getInputSchema()){
									if(curAttr.getAttributeName().equals(curVar.getName())){
										aggAO.addGroupingAttribute(curAttr);
										break;
									}
								}
							}
						}
						
						// vice versa: check, if each grouping attribute is in the grouping
						// clause. If not, throw an exception.
						for(SDFAttribute groupingAttr: aggAO.getGroupingAttributes()){
							boolean found = false;
							for(Variable curVar: variables){
								if(groupingAttr.getAttributeName().equals(curVar.getName())){
									found = true;
									break;
								}
							}
							
							if(!found){
								throw new RuntimeException("No grouping defined over variable '" + groupingAttr.getAttributeName() + "'.");
							}
						}
					}
				}
				
				logOp = aggAO;
			}
		}
		
		
		logOp.subscribeToSource(inputForProjection, 0, 0, inputForProjection.getOutputSchema());		
		
		if(node.isDistinct()){
			ILogicalOperator duplAO = new DuplicateElimination();
			duplAO.subscribeToSource(logOp, 0, 0, logOp.getOutputSchema());
			logOp=duplAO;
		}
		
		// if result has to be written into file
		if(node.getFileURL() != null){
			FileSinkAO fileSink = new FileSinkAO(node.getFileURL(), "CSV", 100, false);
			fileSink.subscribeToSource(logOp, 0, 0, logOp.getOutputSchema());
			logOp = fileSink;
		}
		
		((LinkedList)data).addFirst(logOp);
		
		return data;
	}

	@Override
	public Object visit(ASTConstructQuery node, Object data) {
		return node.childrenAccept(this, data);
	}

	@Override
	public Object visit(ASTDescribeQuery node, Object data) {
		return node.childrenAccept(this, data);
	}

	@Override
	public Object visit(ASTAskQuery node, Object data) {
		return node.childrenAccept(this, data);
	}

	@Override
	public Object visit(ASTFromClause node, Object data) {
		return node.childrenAccept(this, data);
	}


	@Override
	public Object visit(ASTDatastreamClause node, Object data) {
		return node.childrenAccept(this, data);
	}

	@Override
	public Object visit(ASTAggregation node, Object data) {
		return node.childrenAccept(this, data);
	}

	@Override
	public Object visit(ASTWindow node, Object data) {
		return node.childrenAccept(this, data);
	}

	@Override
	public Object visit(ASTSlidingTimeWindow node, Object data) {
		return node.childrenAccept(this, data);
	}

	@Override
	public Object visit(ASTSlidingTupelWindow node, Object data) {
		return node.childrenAccept(this, data);
	}

	@Override
	public Object visit(ASTWindowNotPWindow node, Object data) {
		return node.childrenAccept(this, data);
	}

	@Override
	public Object visit(ASTValueSpecification node, Object data) {
		return node.childrenAccept(this, data);
	}

	@Override
	public Object visit(ASTTimeunit node, Object data) {
		return node.childrenAccept(this, data);
	}

	@Override
	public Object visit(ASTSourceSelector node, Object data) {
		return node.childrenAccept(this, data);
	}

	@Override
	public Object visit(ASTWhereClause node, Object data) {		
		return node.childrenAccept(this, data);
	}

	@Override
	public Object visit(ASTGroupBy node, Object data) {
		return node.childrenAccept(this, data);
	}

	@Override
	public Object visit(ASTSolutionModifier node, Object data) {
		return node.childrenAccept(this, data);
	}

	@Override
	public Object visit(ASTLimitOffsetClauses node, Object data) {
		return node.childrenAccept(this, data);
	}

	@Override
	public Object visit(ASTOrderClause node, Object data) {
		return node.childrenAccept(this, data);
	}

	@Override
	public Object visit(ASTOrderCondition node, Object data) {
		return node.childrenAccept(this, data);
	}

	@Override
	public Object visit(ASTLimitClause node, Object data) {
		return node.childrenAccept(this, data);
	}

	@Override
	public Object visit(ASTOffsetClause node, Object data) {
		return node.childrenAccept(this, data);
	}

	@Override
	public Object visit(ASTGroupGraphPattern node, Object data) {
		// the data that can be passed to the child nodes
		LinkedList oldDataCopy = new LinkedList();
		LinkedList oldData = (LinkedList)data;
		for(int i = 0; i<oldData.size(); i++){
			oldDataCopy.add(oldData.get(i));
		}
		
		// this is the window that can be defined in a basic
		// graph pattern
		WindowAO explicitWindow = node.getWindowAO();
		
		// top logical operator, that results from this group graph pattern.
		ILogicalOperator topOfGroupGraphPattern = null;
		
		// the first child can be 
		// a TriplesBlock,
		// a GraphPatternNotTriples,
		// a Filter,
		// an window,
		// or nothing
		
		List<ILogicalOperator> topsOfNestedPatterns = new ArrayList<ILogicalOperator>();
		
		// in this list for each top operator of nested patterns
		// a value is stored if the corresponding operator in the
		// above list, results from an optional graph pattern.
		// This is necessary to distinguish between join and left join
		// of these operators.
		List<Boolean> isOptional = new ArrayList<Boolean>();
		
		// the filter must be set at last, since it can
		// be placed before other triples patterns
		ASTConstraint filterConstraint = null;
		
		for(int i = 0; i<node.jjtGetNumChildren(); i++){
			Node child = node.jjtGetChild(i);
			if(child instanceof ASTTriplesBlock){				
				// create a join from all the triples with the following
				// procedure:
				// join two triples with two default streams defined results
				// in the following plan
				//					join
				//				  /      \
				//              tpm      tpm
				//               |        |
				//			   union    union
				//            /     \  /     \
				//           w      w  w     w
				ASTTriplesBlock triplesBlock = (ASTTriplesBlock)child;
				List<List<Triple>> triples = triplesBlock.getTriples();
				

				// for each of the same subject triples collections store the
				// corresponding top operator.
				List<ILogicalOperator> topOfSameSubjects = new ArrayList<ILogicalOperator>();
				for(List<Triple> sameSubjTriples: triples){
					// each list of triples in the list "triples"
					// contains triples with the same subject
					// so first generate a join for each of these same
					// subject triples
					List<TriplePatternMatching> tpmAOs = new ArrayList<TriplePatternMatching>();
					for(Triple curTriple: sameSubjTriples){
						TriplePatternMatching tpm = new TriplePatternMatching(curTriple, this.prefixes);
						
						// check, if there is a variable
						LinkedList dataList = (LinkedList)data;
						if(dataList.size() > 0){
							// get the name of the named stream to build the plan for
							String namedStream = null;
							INode graphTerm = null;
							try{
								graphTerm = ((INode)dataList.getFirst());
							}catch(ClassCastException e){
								throw e;
							}
							if(graphTerm.isVariable()){
								namedStream = (String)dataList.get(1); 
								tpm.setGraphVar((Variable)graphTerm);
								tpm.setStream_name(namedStream);
							}
							else{
								namedStream = ((INode)dataList.getFirst()).getName();
								tpm.setStream_name(namedStream); // not necessary, but for future
							}
							
							// get the access ao for the named stream
							ILogicalOperator accessAO = this.dd.getViewOrStream(namedStream, this.user);
							
							// get the window for the named stream
							WindowAO win = null;
							if(explicitWindow != null){
								win = explicitWindow;
							}
							else{
								// there is no explicit window, so take the window
								// from the corresponding named stream.
								
								for(SourceInfo si: this.namedStreams){
									if(si.getStreamName().equals(namedStream)){
										win = si.getWindowOp();
										break;
									}
								}
							}
							
							// the window definitions can be null
							// since we allow non continuous sparql
							// queries
							if(win != null){
								win.subscribeToSource(accessAO, 0, 0, accessAO.getOutputSchema());
								tpm.subscribeToSource(win, 0, 0, win.getOutputSchema());
								tpm.initPredicate();
							}
							else{
								tpm.subscribeToSource(accessAO, 0, 0, accessAO.getOutputSchema());
								tpm.initPredicate();
							}
						}
						// do it for all default streams
						else if(dataList.size() == 0){
							// for each default stream get the access ao from the data dictionary
							// and add a window
							List<ILogicalOperator> topOps = new ArrayList<ILogicalOperator>();
							for(int di = 0; di<this.defaultStreams.size(); di++){
								ILogicalOperator top = null;
								ILogicalOperator accessAO = this.dd.getViewOrStream(this.defaultStreams.get(di).getStreamName(), this.user);
								if(explicitWindow != null){
									explicitWindow.subscribeTo(accessAO, accessAO.getOutputSchema());
									top = explicitWindow;
								}
								// there is no explicit window so use the default window
								else{
									if(this.defaultStreams.get(di).getWindowOp() != null){
										WindowAO defaultWindow = this.defaultStreams.get(di).getWindowOp().clone(); 
										defaultWindow.subscribeTo(accessAO, accessAO.getOutputSchema());
										top = defaultWindow;
									}
									else{
										// no window means we have a non continuous query
										top = accessAO;
									}
								}
								
								topOps.add(top);
							}
							
							// now union all window aos
							if(topOps.size() == 1){
								// the windowOps can be connected directly to the triple pattern matchin
								tpm.subscribeToSource(topOps.get(0), 0, 0, topOps.get(0).getOutputSchema());
								tpm.initPredicate();
							}
							else if(topOps.size() > 1){
								// a union operator is necessary
								UnionAO union = new UnionAO();
								union.subscribeToSource(topOps.get(0), 0, 0, topOps.get(0).getOutputSchema());
								union.subscribeToSource(topOps.get(1), 1, 0, topOps.get(1).getOutputSchema());
								
								// process further unions
								for(int ui = 2; ui<topOps.size(); ui++ ){
									UnionAO innerUnion = new UnionAO();
									innerUnion.subscribeToSource(union, 0, 0, union.getOutputSchema());
									innerUnion.subscribeToSource(topOps.get(ui), 1, 0, topOps.get(ui).getOutputSchema());
									union = innerUnion;
								}
								
								tpm.subscribeToSource(union, 0, 0, union.getOutputSchema());
								tpm.initPredicate();
							}
							else{
								throw new RuntimeException("No default streams defined in GroupGraphPattern.");
							}
						}
						tpmAOs.add(tpm);
					}
					
					// join all triple pattern matchings
					if(tpmAOs.size() == 1){
						topOfSameSubjects.add(tpmAOs.get(0));
					}
					else if(tpmAOs.size() > 1){
						JoinAO join = new JoinAO();
						join.subscribeToSource(tpmAOs.get(0), 0, 0, tpmAOs.get(0).getOutputSchema());
						join.subscribeToSource(tpmAOs.get(1), 1, 0, tpmAOs.get(1).getOutputSchema());
						
						// create join predicate
						// each variable that is in both schemas must be equal
						SDFAttributeList commonVars = getCommonVariables(join.getInputSchema(0), join.getInputSchema(1));
						IPredicate joinPred = createJoinPredicate(commonVars, join.getInputSchema(0), join.getInputSchema(1));
						join.setPredicate(joinPred);
						
						
						// process further joins
						for(int ji = 2; ji<tpmAOs.size(); ji++){
							JoinAO innerJoin = new JoinAO();
							innerJoin.subscribeToSource(join, 0, 0, join.getOutputSchema());
							innerJoin.subscribeToSource(tpmAOs.get(ji), 1, 0, tpmAOs.get(ji).getOutputSchema());
							
							// create join predicate
							// each variable that is in both schemas must be equal
							SDFAttributeList innerCommonVars = getCommonVariables(innerJoin.getInputSchema(0), innerJoin.getInputSchema(1));
							IPredicate innerJoinPred = createJoinPredicate(innerCommonVars, innerJoin.getInputSchema(0), innerJoin.getInputSchema(1));
							innerJoin.setPredicate(innerJoinPred);
							
							join = innerJoin;
						}
						
						
						topOfSameSubjects.add(join);
					}
				}				
				
				// create a join over the top operators of the
				// same subject triples.
				if(topOfSameSubjects.size()== 1){
					topsOfNestedPatterns.add(topOfSameSubjects.get(0));
				}
				else if(topOfSameSubjects.size() > 1){
					JoinAO join = new JoinAO();
					join.subscribeToSource(topOfSameSubjects.get(0), 0, 0, topOfSameSubjects.get(0).getOutputSchema());
					join.subscribeToSource(topOfSameSubjects.get(1), 1, 0, topOfSameSubjects.get(1).getOutputSchema());
					
					// create join predicate
					// each variable that is in both schemas must be equal
					SDFAttributeList commonVars = getCommonVariables(join.getInputSchema(0), join.getInputSchema(1));
					IPredicate joinPred = createJoinPredicate(commonVars, join.getInputSchema(0), join.getInputSchema(1));
					join.setPredicate(joinPred);
					
					
					// process further joins
					for(int ji = 2; ji<topOfSameSubjects.size(); ji++){
						JoinAO innerJoin = new JoinAO();
						innerJoin.subscribeToSource(join, 0, 0, join.getOutputSchema());
						innerJoin.subscribeToSource(topOfSameSubjects.get(ji), 1, 0, topOfSameSubjects.get(ji).getOutputSchema());
						
						// create join predicate
						// each variable that is in both schemas must be equal
						SDFAttributeList innerCommonVars = getCommonVariables(innerJoin.getInputSchema(0), innerJoin.getInputSchema(1));
						IPredicate innerJoinPred = createJoinPredicate(innerCommonVars, innerJoin.getInputSchema(0), innerJoin.getInputSchema(1));
						innerJoin.setPredicate(innerJoinPred);
						
						join = innerJoin;
					}
					
					topsOfNestedPatterns.add(join);
				}
				else{
					throw new RuntimeException("No triples in group graph pattern.");
				}
				
				isOptional.add(false);
				
			}
			else if(child instanceof ASTGraphPatternNotTriples){
				// get the child of this graph pattern, because
				// this is the relevant one
				// jjt: GraphPatternNotTriples:: (OptionalGraphPattern() | GroupOrUnionGraphPattern() | GraphGraphPattern())
				
				// newData has to be copied, since this code is in a loop and will therefore executed multiple times
				LinkedList newData = new LinkedList();
				for(Object old: oldDataCopy){
					newData.add(old);
				}
				
				ILogicalOperator topOp = (ILogicalOperator)((LinkedList)child.jjtAccept(this, newData)).getFirst();
				
				topsOfNestedPatterns.add(topOp);
				// if the child of GraphPatternNotTriples is an OptionalGraphPattern,
				// we need a left join, so set the field in isOptional to "true"
				isOptional.add(child.jjtGetChild(0) instanceof ASTOptionalGraphPattern);
			}
			else if(child instanceof ASTFilter){
				filterConstraint = (ASTConstraint)child.jjtGetChild(0);
				
				// we cannot set the filter here because we need the union
				// of schemas of all triple patterns, but not all triple
				// patterns have been read here. So first read the rest
				// of the triple patterns an then put the selection
				// as parent of the corresponding join of this
				// group graph pattern.
				// jjt: GroupGraphPattern:: TriplesBlock | Filter | TriplesBlock (example)
			}
		}
		
		// now join all top operators that have been developed from nested patterns
		// like triples block, nested group graph patterns and so on.
		if(topsOfNestedPatterns.size() == 1){
			topOfGroupGraphPattern = topsOfNestedPatterns.get(0);
		}
		else if(topsOfNestedPatterns.size() > 1){
			JoinAO join = null;
			if(isOptional.get(1)){
				join = new LeftJoinAO();
			}
			else{
				join = new JoinAO();
			}
			ILogicalOperator leftIn = topsOfNestedPatterns.get(0);
			ILogicalOperator rightIn = topsOfNestedPatterns.get(1);
			
			join.subscribeToSource(leftIn, 0, 0, leftIn.getOutputSchema());
			join.subscribeToSource(rightIn, 1, 0, rightIn.getOutputSchema());
			
			IPredicate joinPred = createJoinPredicate(
					getCommonVariables(leftIn.getOutputSchema(), rightIn.getOutputSchema()),
					leftIn.getOutputSchema(),
					rightIn.getOutputSchema());
			join.setPredicate(joinPred);
			
			// process further top operators
			for(int i = 2; i<topsOfNestedPatterns.size(); i++){
				JoinAO innerJoin = null;
				if(isOptional.get(i)){
					innerJoin = new LeftJoinAO();
				}
				else{
					innerJoin = new JoinAO();
				}
				
				innerJoin.subscribeToSource(join, 0, 0, join.getOutputSchema());
				
				ILogicalOperator innerRightIn = topsOfNestedPatterns.get(i);
				innerJoin.subscribeToSource(innerRightIn, 1, 0, innerRightIn.getOutputSchema());
				
				IPredicate innerJoinPred = createJoinPredicate(
						getCommonVariables(join.getOutputSchema(), innerRightIn.getOutputSchema()),
						join.getOutputSchema(),
						innerRightIn.getOutputSchema());
				innerJoin.setPredicate(innerJoinPred);
				join = innerJoin;
			}
			
			topOfGroupGraphPattern = join; 
		}
		else{
			throw new RuntimeException("Empty Group Graph Pattern found.");
		}
		
		// If there is a filter expression in this
		// group graph pattern, we need a select
		// as top operator of this group graph pattern
		if(filterConstraint != null){
			SelectAO select = new SelectAO();
			IAttributeResolver attrRes = new SPARQLDirectAttributeResolver(topOfGroupGraphPattern.getOutputSchema());
			SDFExpression expr = new SDFExpression(null, filterConstraint.toString(), attrRes);
			IPredicate selectPred = new TypeSafeRelationalPredicate(expr);
			select.setPredicate(selectPred);
			select.subscribeTo(topOfGroupGraphPattern, topOfGroupGraphPattern.getOutputSchema());
			topOfGroupGraphPattern = select;
		}
		
		((LinkedList)data).addFirst(topOfGroupGraphPattern);
		return data;
	}

	@Override
	public Object visit(ASTTriplesBlock node, Object data) {
		return node.childrenAccept(this, data);
	}

	@Override
	public Object visit(ASTGraphPatternNotTriples node, Object data) {
		return node.childrenAccept(this, data);
	}

	@Override
	public Object visit(ASTOptionalGraphPattern node, Object data) {
		return node.childrenAccept(this, data);
	}

	@Override
	public Object visit(ASTGraphGraphPattern node, Object data) {
		// if the graph term is an iri, than create a subplan for
		// this iri and return it
		if(!node.getGraphTerm().isVariable()){
			// the second child is the nested pattern
			
			LinkedList newData = new LinkedList();
			// set the graph term at the first position
			newData.add(node.getGraphTerm());
			// at the second position put the current
			// named stream for which the plan is build
			newData.add(node.getGraphTerm().getName());
			
			ILogicalOperator topOfNested = (ILogicalOperator)((LinkedList)node.jjtGetChild(1).jjtAccept(this, newData)).getFirst();
			((LinkedList)data).addFirst(topOfNested);
			return data;
		}
		// else if the graph term is a variable, than
		// create a subplan for each named stream
		// and union all these plans. Return the
		// top union operator
		else{
			// do it for every named stream
			ArrayList<ILogicalOperator> topOps = new ArrayList<ILogicalOperator>();
			for(SourceInfo si: this.namedStreams){
				LinkedList newData = new LinkedList();
				// set the graph term at the first position
				newData.add(node.getGraphTerm());
				// at the second position put the current
				// named stream for which the plan is build
				newData.add(si.getStreamName());
				
				ILogicalOperator topOfNested = (ILogicalOperator)((LinkedList)node.jjtGetChild(1).jjtAccept(this, newData)).getFirst();
				topOps.add(topOfNested);
			}
			
			// union all nested operators
			if(topOps.size()== 1){
				((LinkedList)data).addFirst(topOps.get(0));
			}
			else if(topOps.size() > 1){
				UnionAO union = new UnionAO();
				
				union.subscribeToSource(topOps.get(0), 0, 0, topOps.get(0).getOutputSchema());
				union.subscribeToSource(topOps.get(1), 1, 0, topOps.get(1).getOutputSchema());
				
				for(int i = 2; i<topOps.size(); i++){
					UnionAO innerUnion = new UnionAO();
					innerUnion.subscribeToSource(union, 0, 0, union.getOutputSchema());
					innerUnion.subscribeToSource(topOps.get(i), 1, 0, topOps.get(i).getOutputSchema());
					
					union = innerUnion;
				}
				((LinkedList)data).addFirst(union);
				
			}
			else{
				throw new RuntimeException("No nested pattern in GraphGraphPattern.");
			}
		}
		
		return data;
	}

	@Override
	public Object visit(ASTGroupOrUnionGraphPattern node, Object data) {		
		List<ILogicalOperator> topsOfNestedGroupGraphPatterns = new LinkedList<ILogicalOperator>();
		for(int i = 0; i<node.jjtGetNumChildren(); i++){
			LinkedList newData = new LinkedList(); // everytime a child is visited we need an empty list
			topsOfNestedGroupGraphPatterns.add((ILogicalOperator)((LinkedList)node.jjtGetChild(i).jjtAccept(this, newData)).getFirst());
			
		}
		
		// only 1 group graph pattern as child so return the corresponding
		// top operator
		if(topsOfNestedGroupGraphPatterns.size() == 1){
			((LinkedList)data).addFirst(topsOfNestedGroupGraphPatterns.get(0));
		}
		// more than 1 group graph pattern, so build a tree of unions
		// and return the top union operator
		else if(topsOfNestedGroupGraphPatterns.size() > 1){
			UnionAO union = new UnionAO();
			ILogicalOperator leftInput = topsOfNestedGroupGraphPatterns.get(0);
			ILogicalOperator rightInput = topsOfNestedGroupGraphPatterns.get(1);
			
			union.subscribeToSource(leftInput, 0, 0, leftInput.getOutputSchema());
			union.subscribeToSource(rightInput, 1, 0, rightInput.getOutputSchema());
			
			for(int i = 2; i<topsOfNestedGroupGraphPatterns.size(); i++){
				UnionAO innerUnion = new UnionAO();
				ILogicalOperator innerRightInput = topsOfNestedGroupGraphPatterns.get(i);
				innerUnion.subscribeToSource(union, 0, 0, union.getOutputSchema());
				innerUnion.subscribeSink(innerRightInput, 1, 0, innerRightInput.getOutputSchema());
				
				union = innerUnion;
			}
			
			((LinkedList)data).addFirst(union);
		}
		else{
			throw new RuntimeException("No child in GroupOrUnionGraphPattern.");
		}
		
		return data;
	}

	@Override
	public Object visit(ASTFilter node, Object data) {
		return node.childrenAccept(this, data);
	}

	@Override
	public Object visit(ASTConstraint node, Object data) {
		return node.childrenAccept(this, data);
	}

	@Override
	public Object visit(ASTFunctionCall node, Object data) {
		return node.childrenAccept(this, data);
	}

	@Override
	public Object visit(ASTArgList node, Object data) {
		return node.childrenAccept(this, data);
	}

	@Override
	public Object visit(ASTConstructTemplate node, Object data) {
		return node.childrenAccept(this, data);
	}

	@Override
	public Object visit(ASTConstructTriples node, Object data) {
		return node.childrenAccept(this, data);
	}

	@Override
	public Object visit(ASTTriplesSameSubject node, Object data) {
		return node.childrenAccept(this, data);
	}

	@Override
	public Object visit(ASTPropertyListNotEmpty node, Object data) {
		return node.childrenAccept(this, data);
	}

	@Override
	public Object visit(ASTPropertyList node, Object data) {
		return node.childrenAccept(this, data);
	}

	@Override
	public Object visit(ASTObjectList node, Object data) {
		return node.childrenAccept(this, data);
	}

	@Override
	public Object visit(ASTObject node, Object data) {
		return node.childrenAccept(this, data);
	}

	@Override
	public Object visit(ASTVerb node, Object data) {
		return node.childrenAccept(this, data);
	}

	@Override
	public Object visit(ASTTriplesNode node, Object data) {
		return node.childrenAccept(this, data);
	}

	@Override
	public Object visit(ASTBlankNodePropertyList node, Object data) {
		return node.childrenAccept(this, data);
	}

	@Override
	public Object visit(ASTCollection node, Object data) {
		return node.childrenAccept(this, data);
	}

	@Override
	public Object visit(ASTGraphNode node, Object data) {
		return node.childrenAccept(this, data);
	}

	@Override
	public Object visit(ASTVarOrTerm node, Object data) {
		return node.childrenAccept(this, data);
	}

	@Override
	public Object visit(ASTVarOrIRIref node, Object data) {
		return node.childrenAccept(this, data);
	}

	@Override
	public Object visit(ASTVar node, Object data) {
		return node.childrenAccept(this, data);
	}

	@Override
	public Object visit(ASTGraphTerm node, Object data) {
		return node.childrenAccept(this, data);
	}

	@Override
	public Object visit(ASTExpression node, Object data) {
		return node.childrenAccept(this, data);
	}

	@Override
	public Object visit(ASTConditionalOrExpression node, Object data) {
		return node.childrenAccept(this, data);
	}

	@Override
	public Object visit(ASTConditionalAndExpression node, Object data) {
		return node.childrenAccept(this, data);
	}

	@Override
	public Object visit(ASTValueLogical node, Object data) {
		return node.childrenAccept(this, data);
	}

	@Override
	public Object visit(ASTRelationalExpression node, Object data) {
		return node.childrenAccept(this, data);
	}

	@Override
	public Object visit(ASTNumericExpression node, Object data) {
		return node.childrenAccept(this, data);
	}

	@Override
	public Object visit(ASTAdditiveExpression node, Object data) {
		return node.childrenAccept(this, data);
	}

	@Override
	public Object visit(ASTMultiplicativeExpression node, Object data) {
		return node.childrenAccept(this, data);
	}

	@Override
	public Object visit(ASTUnaryExpression node, Object data) {
		return node.childrenAccept(this, data);
	}

	@Override
	public Object visit(ASTPrimaryExpression node, Object data) {
		return node.childrenAccept(this, data);
	}

	@Override
	public Object visit(ASTBrackettedExpression node, Object data) {
		return node.childrenAccept(this, data);
	}

	@Override
	public Object visit(ASTBuiltInCall node, Object data) {
		return node.childrenAccept(this, data);
	}

	@Override
	public Object visit(ASTRegexExpression node, Object data) {
		return node.childrenAccept(this, data);
	}

	@Override
	public Object visit(ASTIRIrefOrFunction node, Object data) {
		return node.childrenAccept(this, data);
	}

	@Override
	public Object visit(ASTRDFLiteral node, Object data) {
		return node.childrenAccept(this, data);
	}

	@Override
	public Object visit(ASTNumericLiteral node, Object data) {
		return node.childrenAccept(this, data);
	}

	@Override
	public Object visit(ASTNumericLiteralUnsigned node, Object data) {
		return node.childrenAccept(this, data);
	}

	@Override
	public Object visit(ASTNumericLiteralPositive node, Object data) {
		return node.childrenAccept(this, data);
	}

	@Override
	public Object visit(ASTNumericLiteralNegative node, Object data) {
		return node.childrenAccept(this, data);
	}

	@Override
	public Object visit(ASTBooleanLiteral node, Object data) {
		return node.childrenAccept(this, data);
	}

	@Override
	public Object visit(ASTString node, Object data) {
		return node.childrenAccept(this, data);
	}

	@Override
	public Object visit(ASTIRIref node, Object data) {
		return node.childrenAccept(this, data);
	}

	@Override
	public Object visit(ASTPrefixedName node, Object data) {
		return node.childrenAccept(this, data);
	}

	@Override
	public Object visit(ASTBlankNode node, Object data) {
		return node.childrenAccept(this, data);
	}

	@Override
	public Object visit(ASTIRI_REF node, Object data) {
		return node.childrenAccept(this, data);
	}

	@Override
	public Object visit(ASTStreamClause node, Object data) {
		return node.childrenAccept(this, data);
	}

	@Override
	public Object visit(ASTCreateStatement node, Object data) {
		this.isCreateStatement = true;
		
		// the second child is either socket or channel or csv source
		Node child = node.jjtGetChild(1);
		
		String streamName = node.getStreamName();
		boolean isPersistent = node.isPersistent();
		
		// the schema
		SDFAttributeList outputSchema = new SDFAttributeList("");
		
		SDFAttribute subject = new SDFAttribute(null,streamName + ".subject", SDFDatatype.STRING);
		outputSchema.add(subject);
		
		SDFAttribute predicate = new SDFAttribute(null,streamName + ".predicate", SDFDatatype.STRING);
		outputSchema.add(predicate);
		
		SDFAttribute object = new SDFAttribute(null,streamName + ".object", SDFDatatype.STRING);
		outputSchema.add(object);
		
		AccessAO accAO = null;
		if(child instanceof ASTSocket){
			ASTSocket socket = (ASTSocket)child;
//			accAO = new AccessAO(new SDFSource(streamName, "SPARQL_Access_Socket"));
			accAO = new AccessAO(new SDFSource(node.getStreamName(), RelationalAccessSourceTypes.RELATIONAL_ATOMIC_DATA_INPUT_STREAM_ACCESS));
			accAO.setHost(socket.getHost());
			accAO.setPort(socket.getPort());
			
			
		}
		else if(child instanceof ASTChannel){
			ASTChannel channel = (ASTChannel)child;
//			accAO = new AccessAO(new SDFSource(streamName, "SPARQL_ACCESS_Channel"));
			accAO = new AccessAO(new SDFSource(streamName, RelationalAccessSourceTypes.RELATIONAL_ATOMIC_DATA_INPUT_STREAM_ACCESS));
			accAO.setHost(channel.getHost());
			accAO.setPort(channel.getPort());
			
		}
		else if(child instanceof ASTCSVSource){
			ASTCSVSource csv = (ASTCSVSource)child;
//			accAO = new AccessAO(new SDFSource(streamName, "SPARQL_ACCESS_CSV"));
			accAO = new AccessAO(new SDFSource(streamName, RelationalAccessSourceTypes.RELATIONAL_ATOMIC_DATA_INPUT_STREAM_ACCESS));
			accAO.setFileURL(csv.getURL());
			
		}
		else{
			throw new RuntimeException("No access specification (Socket|Channel|CSV) given for stream definition.");
		}
		
		accAO.setOutputSchema(outputSchema);
		
		// before adding the acces operator, add the corresponding entity
		dd.addSourceType(streamName, accAO.getSourceType());
		dd.addEntitySchema(streamName, outputSchema, user);
		
		TimestampAO op = addTimestampAO(accAO);
		if(isPersistent){
			op.setUsingNoTime(true);
		}
		this.dd.setStream(node.getStreamName(), op, this.user);
		
		((LinkedList)data).addFirst(op);
					
		return data;
	}


	@Override
	public Object visit(ASTSocket node, Object data) {
		return node.childrenAccept(this, data);
	}

	@Override
	public Object visit(ASTChannel node, Object data) {
		return node.childrenAccept(this, data);
	}

	@Override
	public Object visit(ASTHost node, Object data) {
		return node.childrenAccept(this, data);
	}

	@Override
	public Object visit(ASTCSVSource node, Object data) {
		return node.childrenAccept(this, data);
	}

	@Override
	public Object visit(ASTIdentifier node, Object data) {
		return node.childrenAccept(this, data);
	}
	
	// ================================= utility methods =======================================
	
	
	private SDFAttribute outAttribute(String attributeName,
			AggregateFunctionName function) {
		String funcName = function.toString() + "(" + attributeName + ")";
		
		// in each case the input datatype is string
		SDFAttribute attribute = new SDFAttribute(null, funcName, SDFDatatype.STRING);
		
		return attribute;
	}
	
	
	/**
	 * Returns a list of attributes, that occur in both schemas.
	 * The even indices contain the attributes of the left schema.
	 * The odd (even +1 ) indices contain the attributes of the right schema.
	 * 
	 * @param leftSchema
	 * @param rightSchema
	 * @return
	 */
	private static SDFAttributeList getCommonVariables(SDFAttributeList leftSchema, SDFAttributeList rightSchema){
		SDFAttributeList commonSchema = new SDFAttributeList("");
		for(SDFAttribute leftAttr: leftSchema){
			for(SDFAttribute rightAttr: rightSchema){
				if(leftAttr.getAttributeName().equals(rightAttr.getAttributeName())){
					commonSchema.add(leftAttr);
					commonSchema.add(rightAttr);
				}
			}
		}
		return commonSchema;
	}
	
	/**
	 * Creates a join predicate of the form
	 * a.y = b.y AND a.x = b.x from two triple patterns
	 * like 
	 * {?y :pred  ?x
	 *  ?y :pred2 ?x}
	 */
	private static IPredicate createJoinPredicate(SDFAttributeList commonVars, SDFAttributeList leftSchema, SDFAttributeList rightSchema){
		// if there are no common vars, the predicate is always true
		if(commonVars.isEmpty()){
			return new TruePredicate();
		}
		
//		ArrayList<SDFExpression> exprs = new ArrayList<SDFExpression>();
		
		SDFAttributeList outputSchema = SDFAttributeList.union(leftSchema, rightSchema);
		IAttributeResolver attrRes = new DirectAttributeResolver(outputSchema);
		
		// create the join predicate as an expression completely parsed in MEP
		String exprStr = "";
		for(int i = 0; i<commonVars.getAttributeCount(); i += 2){
			SDFAttribute curLeftAttr = commonVars.get(i); // even indices contain the attributes of the left schema
			SDFAttribute curRightAttr = commonVars.get(i+1); // odd (even + 1) indices contain the attributes of the right schema
			if(i > 0){
				exprStr += " AND ";
			}
			exprStr += curLeftAttr.getPointURI() + " == " + curRightAttr.getPointURI();
//			SDFExpression expr = new SDFExpression(null, exprStr, attrRes);
//			exprs.add(expr);
		}
		
		SDFExpression expr = new SDFExpression(null, exprStr, attrRes);
		
		IPredicate retval = new TypeSafeRelationalPredicate(expr);
		
		
//		// more than one common variable
//		// so build a tree of and predicates
//		if(exprs.size() > 1){
//			TypeSafeRelationalPredicate firstRelational = new TypeSafeRelationalPredicate(exprs.get(0));
//			firstRelational.init(leftSchema, rightSchema);
//			
//			IPredicate left = firstRelational;
//			
//			for(int i = 1; i<exprs.size(); i++){
//				TypeSafeRelationalPredicate right = new TypeSafeRelationalPredicate(exprs.get(i));
//				right.init(leftSchema, rightSchema);
//				AndPredicate tempAnd = new AndPredicate(left, right);
//				left = tempAnd;
//			}
//			
//			retval = left;
//		}
//		// only one common variable, so
//		// return corresponding relational predicate
//		else if(exprs.size() == 1){
//			TypeSafeRelationalPredicate firstRelational = new TypeSafeRelationalPredicate(exprs.get(0));
//			firstRelational.init(leftSchema, rightSchema);
//			retval = firstRelational;
//		}
		
		return retval;
	}

	private TimestampAO addTimestampAO(ILogicalOperator operator) {
		TimestampAO timestampAO = new TimestampAO();
		for (SDFAttribute attr : operator.getOutputSchema()) {
			if (attr.getDatatype().getURI().equals("StartTimestamp")) {
				timestampAO.setStartTimestamp(attr);
			}

			if (attr.getDatatype().getURI().equals("EndTimestamp")) {
				timestampAO.setEndTimestamp(attr);
			}
		}

		timestampAO.subscribeTo(operator, operator.getOutputSchema());
		return timestampAO;
	}

}
