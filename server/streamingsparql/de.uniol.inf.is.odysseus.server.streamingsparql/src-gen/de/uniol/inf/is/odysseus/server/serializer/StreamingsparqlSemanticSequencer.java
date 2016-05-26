/*
 * generated by Xtext
 */
package de.uniol.inf.is.odysseus.server.serializer;

import com.google.inject.Inject;
import com.google.inject.Provider;
import de.uniol.inf.is.odysseus.server.services.StreamingsparqlGrammarAccess;
import de.uniol.inf.is.odysseus.server.streamingsparql.Aggregate;
import de.uniol.inf.is.odysseus.server.streamingsparql.Aggregation;
import de.uniol.inf.is.odysseus.server.streamingsparql.Base;
import de.uniol.inf.is.odysseus.server.streamingsparql.DatasetClause;
import de.uniol.inf.is.odysseus.server.streamingsparql.Filesinkclause;
import de.uniol.inf.is.odysseus.server.streamingsparql.Filterclause;
import de.uniol.inf.is.odysseus.server.streamingsparql.GraphNode;
import de.uniol.inf.is.odysseus.server.streamingsparql.GroupBy;
import de.uniol.inf.is.odysseus.server.streamingsparql.GroupClause;
import de.uniol.inf.is.odysseus.server.streamingsparql.GroupGraphPatternSub;
import de.uniol.inf.is.odysseus.server.streamingsparql.IRI;
import de.uniol.inf.is.odysseus.server.streamingsparql.InnerWhereClause;
import de.uniol.inf.is.odysseus.server.streamingsparql.LangTag;
import de.uniol.inf.is.odysseus.server.streamingsparql.NamedVariable;
import de.uniol.inf.is.odysseus.server.streamingsparql.Prefix;
import de.uniol.inf.is.odysseus.server.streamingsparql.PropertyList;
import de.uniol.inf.is.odysseus.server.streamingsparql.SelectQuery;
import de.uniol.inf.is.odysseus.server.streamingsparql.StreamingsparqlPackage;
import de.uniol.inf.is.odysseus.server.streamingsparql.TriplesSameSubject;
import de.uniol.inf.is.odysseus.server.streamingsparql.TypeTag;
import de.uniol.inf.is.odysseus.server.streamingsparql.UnNamedVariable;
import de.uniol.inf.is.odysseus.server.streamingsparql.Variable;
import de.uniol.inf.is.odysseus.server.streamingsparql.WhereClause;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.xtext.serializer.acceptor.ISemanticSequenceAcceptor;
import org.eclipse.xtext.serializer.acceptor.SequenceFeeder;
import org.eclipse.xtext.serializer.diagnostic.ISemanticSequencerDiagnosticProvider;
import org.eclipse.xtext.serializer.diagnostic.ISerializationDiagnostic.Acceptor;
import org.eclipse.xtext.serializer.sequencer.AbstractDelegatingSemanticSequencer;
import org.eclipse.xtext.serializer.sequencer.GenericSequencer;
import org.eclipse.xtext.serializer.sequencer.ISemanticNodeProvider.INodesForEObjectProvider;
import org.eclipse.xtext.serializer.sequencer.ISemanticSequencer;
import org.eclipse.xtext.serializer.sequencer.ITransientValueService;
import org.eclipse.xtext.serializer.sequencer.ITransientValueService.ValueTransient;

@SuppressWarnings("all")
public class StreamingsparqlSemanticSequencer extends AbstractDelegatingSemanticSequencer {

	@Inject
	private StreamingsparqlGrammarAccess grammarAccess;
	
	@Override
	public void createSequence(EObject context, EObject semanticObject) {
		if(semanticObject.eClass().getEPackage() == StreamingsparqlPackage.eINSTANCE) switch(semanticObject.eClass().getClassifierID()) {
			case StreamingsparqlPackage.AGGREGATE:
				sequence_Aggregate(context, (Aggregate) semanticObject); 
				return; 
			case StreamingsparqlPackage.AGGREGATION:
				sequence_Aggregation(context, (Aggregation) semanticObject); 
				return; 
			case StreamingsparqlPackage.BASE:
				sequence_Base(context, (Base) semanticObject); 
				return; 
			case StreamingsparqlPackage.DATASET_CLAUSE:
				sequence_DatasetClause(context, (DatasetClause) semanticObject); 
				return; 
			case StreamingsparqlPackage.FILESINKCLAUSE:
				sequence_Filesinkclause(context, (Filesinkclause) semanticObject); 
				return; 
			case StreamingsparqlPackage.FILTERCLAUSE:
				sequence_Filterclause(context, (Filterclause) semanticObject); 
				return; 
			case StreamingsparqlPackage.GRAPH_NODE:
				sequence_GraphNode(context, (GraphNode) semanticObject); 
				return; 
			case StreamingsparqlPackage.GROUP_BY:
				sequence_GroupBy(context, (GroupBy) semanticObject); 
				return; 
			case StreamingsparqlPackage.GROUP_CLAUSE:
				sequence_GroupClause(context, (GroupClause) semanticObject); 
				return; 
			case StreamingsparqlPackage.GROUP_GRAPH_PATTERN_SUB:
				sequence_GroupGraphPatternSub(context, (GroupGraphPatternSub) semanticObject); 
				return; 
			case StreamingsparqlPackage.IRI:
				sequence_IRI(context, (IRI) semanticObject); 
				return; 
			case StreamingsparqlPackage.INNER_WHERE_CLAUSE:
				sequence_InnerWhereClause(context, (InnerWhereClause) semanticObject); 
				return; 
			case StreamingsparqlPackage.LANG_TAG:
				sequence_LangTag(context, (LangTag) semanticObject); 
				return; 
			case StreamingsparqlPackage.NAMED_VARIABLE:
				sequence_Property(context, (NamedVariable) semanticObject); 
				return; 
			case StreamingsparqlPackage.PREFIX:
				sequence_UnNamedPrefix(context, (Prefix) semanticObject); 
				return; 
			case StreamingsparqlPackage.PROPERTY_LIST:
				sequence_PropertyList(context, (PropertyList) semanticObject); 
				return; 
			case StreamingsparqlPackage.SELECT_QUERY:
				sequence_SelectQuery(context, (SelectQuery) semanticObject); 
				return; 
			case StreamingsparqlPackage.TRIPLES_SAME_SUBJECT:
				sequence_TriplesSameSubject(context, (TriplesSameSubject) semanticObject); 
				return; 
			case StreamingsparqlPackage.TYPE_TAG:
				sequence_TypeTag(context, (TypeTag) semanticObject); 
				return; 
			case StreamingsparqlPackage.UN_NAMED_VARIABLE:
				sequence_UnNamedVariable(context, (UnNamedVariable) semanticObject); 
				return; 
			case StreamingsparqlPackage.VARIABLE:
				sequence_Variable(context, (Variable) semanticObject); 
				return; 
			case StreamingsparqlPackage.WHERE_CLAUSE:
				sequence_WhereClause(context, (WhereClause) semanticObject); 
				return; 
			}
		if (errorAcceptor != null) errorAcceptor.accept(diagnosticProvider.createInvalidContextOrTypeDiagnostic(semanticObject, context));
	}
	
	/**
	 * Constraint:
	 *     (aggregations+=Aggregation* groupby=GroupBy?)
	 */
	protected void sequence_Aggregate(EObject context, Aggregate semanticObject) {
		genericSequencer.createSequence(context, semanticObject);
	}
	
	
	/**
	 * Constraint:
	 *     (function=AGG_FUNCTION varToAgg=Variable aggName=STRING datatype=STRING?)
	 */
	protected void sequence_Aggregation(EObject context, Aggregation semanticObject) {
		genericSequencer.createSequence(context, semanticObject);
	}
	
	
	/**
	 * Constraint:
	 *     iref=IRI
	 */
	protected void sequence_Base(EObject context, Base semanticObject) {
		if(errorAcceptor != null) {
			if(transientValues.isValueTransient(semanticObject, StreamingsparqlPackage.Literals.BASE__IREF) == ValueTransient.YES)
				errorAcceptor.accept(diagnosticProvider.createFeatureValueMissing(semanticObject, StreamingsparqlPackage.Literals.BASE__IREF));
		}
		INodesForEObjectProvider nodes = createNodeProvider(semanticObject);
		SequenceFeeder feeder = createSequencerFeeder(semanticObject, nodes);
		feeder.accept(grammarAccess.getBaseAccess().getIrefIRIParserRuleCall_1_0(), semanticObject.getIref());
		feeder.finish();
	}
	
	
	/**
	 * Constraint:
	 *     (dataSet=IRI name=ID (type=WINDOWTYPE size=INT advance=INT? unit=UNITTYPE?)?)
	 */
	protected void sequence_DatasetClause(EObject context, DatasetClause semanticObject) {
		genericSequencer.createSequence(context, semanticObject);
	}
	
	
	/**
	 * Constraint:
	 *     path=STRING
	 */
	protected void sequence_Filesinkclause(EObject context, Filesinkclause semanticObject) {
		if(errorAcceptor != null) {
			if(transientValues.isValueTransient(semanticObject, StreamingsparqlPackage.Literals.FILESINKCLAUSE__PATH) == ValueTransient.YES)
				errorAcceptor.accept(diagnosticProvider.createFeatureValueMissing(semanticObject, StreamingsparqlPackage.Literals.FILESINKCLAUSE__PATH));
		}
		INodesForEObjectProvider nodes = createNodeProvider(semanticObject);
		SequenceFeeder feeder = createSequencerFeeder(semanticObject, nodes);
		feeder.accept(grammarAccess.getFilesinkclauseAccess().getPathSTRINGTerminalRuleCall_1_0(), semanticObject.getPath());
		feeder.finish();
	}
	
	
	/**
	 * Constraint:
	 *     (left=Variable operator=Operator right=Variable)
	 */
	protected void sequence_Filterclause(EObject context, Filterclause semanticObject) {
		if(errorAcceptor != null) {
			if(transientValues.isValueTransient(semanticObject, StreamingsparqlPackage.Literals.FILTERCLAUSE__LEFT) == ValueTransient.YES)
				errorAcceptor.accept(diagnosticProvider.createFeatureValueMissing(semanticObject, StreamingsparqlPackage.Literals.FILTERCLAUSE__LEFT));
			if(transientValues.isValueTransient(semanticObject, StreamingsparqlPackage.Literals.FILTERCLAUSE__OPERATOR) == ValueTransient.YES)
				errorAcceptor.accept(diagnosticProvider.createFeatureValueMissing(semanticObject, StreamingsparqlPackage.Literals.FILTERCLAUSE__OPERATOR));
			if(transientValues.isValueTransient(semanticObject, StreamingsparqlPackage.Literals.FILTERCLAUSE__RIGHT) == ValueTransient.YES)
				errorAcceptor.accept(diagnosticProvider.createFeatureValueMissing(semanticObject, StreamingsparqlPackage.Literals.FILTERCLAUSE__RIGHT));
		}
		INodesForEObjectProvider nodes = createNodeProvider(semanticObject);
		SequenceFeeder feeder = createSequencerFeeder(semanticObject, nodes);
		feeder.accept(grammarAccess.getFilterclauseAccess().getLeftVariableParserRuleCall_1_0(), semanticObject.getLeft());
		feeder.accept(grammarAccess.getFilterclauseAccess().getOperatorOperatorEnumRuleCall_2_0(), semanticObject.getOperator());
		feeder.accept(grammarAccess.getFilterclauseAccess().getRightVariableParserRuleCall_3_0(), semanticObject.getRight());
		feeder.finish();
	}
	
	
	/**
	 * Constraint:
	 *     (variable=Variable | literal=STRING | iri=IRI)
	 */
	protected void sequence_GraphNode(EObject context, GraphNode semanticObject) {
		genericSequencer.createSequence(context, semanticObject);
	}
	
	
	/**
	 * Constraint:
	 *     (variables+=Variable variables+=Variable*)
	 */
	protected void sequence_GroupBy(EObject context, GroupBy semanticObject) {
		genericSequencer.createSequence(context, semanticObject);
	}
	
	
	/**
	 * Constraint:
	 *     (conditions+=Variable conditions+=Variable*)
	 */
	protected void sequence_GroupClause(EObject context, GroupClause semanticObject) {
		genericSequencer.createSequence(context, semanticObject);
	}
	
	
	/**
	 * Constraint:
	 *     (graphPatterns+=TriplesSameSubject graphPatterns+=TriplesSameSubject*)
	 */
	protected void sequence_GroupGraphPatternSub(EObject context, GroupGraphPatternSub semanticObject) {
		genericSequencer.createSequence(context, semanticObject);
	}
	
	
	/**
	 * Constraint:
	 *     value=IRI_TERMINAL
	 */
	protected void sequence_IRI(EObject context, IRI semanticObject) {
		if(errorAcceptor != null) {
			if(transientValues.isValueTransient(semanticObject, StreamingsparqlPackage.Literals.IRI__VALUE) == ValueTransient.YES)
				errorAcceptor.accept(diagnosticProvider.createFeatureValueMissing(semanticObject, StreamingsparqlPackage.Literals.IRI__VALUE));
		}
		INodesForEObjectProvider nodes = createNodeProvider(semanticObject);
		SequenceFeeder feeder = createSequencerFeeder(semanticObject, nodes);
		feeder.accept(grammarAccess.getIRIAccess().getValueIRI_TERMINALTerminalRuleCall_1_0(), semanticObject.getValue());
		feeder.finish();
	}
	
	
	/**
	 * Constraint:
	 *     (name=[DatasetClause|ID] groupGraphPattern=GroupGraphPatternSub)
	 */
	protected void sequence_InnerWhereClause(EObject context, InnerWhereClause semanticObject) {
		if(errorAcceptor != null) {
			if(transientValues.isValueTransient(semanticObject, StreamingsparqlPackage.Literals.INNER_WHERE_CLAUSE__NAME) == ValueTransient.YES)
				errorAcceptor.accept(diagnosticProvider.createFeatureValueMissing(semanticObject, StreamingsparqlPackage.Literals.INNER_WHERE_CLAUSE__NAME));
			if(transientValues.isValueTransient(semanticObject, StreamingsparqlPackage.Literals.INNER_WHERE_CLAUSE__GROUP_GRAPH_PATTERN) == ValueTransient.YES)
				errorAcceptor.accept(diagnosticProvider.createFeatureValueMissing(semanticObject, StreamingsparqlPackage.Literals.INNER_WHERE_CLAUSE__GROUP_GRAPH_PATTERN));
		}
		INodesForEObjectProvider nodes = createNodeProvider(semanticObject);
		SequenceFeeder feeder = createSequencerFeeder(semanticObject, nodes);
		feeder.accept(grammarAccess.getInnerWhereClauseAccess().getNameDatasetClauseIDTerminalRuleCall_0_0_1(), semanticObject.getName());
		feeder.accept(grammarAccess.getInnerWhereClauseAccess().getGroupGraphPatternGroupGraphPatternSubParserRuleCall_1_0(), semanticObject.getGroupGraphPattern());
		feeder.finish();
	}
	
	
	/**
	 * Constraint:
	 *     lang=ID
	 */
	protected void sequence_LangTag(EObject context, LangTag semanticObject) {
		if(errorAcceptor != null) {
			if(transientValues.isValueTransient(semanticObject, StreamingsparqlPackage.Literals.LANG_TAG__LANG) == ValueTransient.YES)
				errorAcceptor.accept(diagnosticProvider.createFeatureValueMissing(semanticObject, StreamingsparqlPackage.Literals.LANG_TAG__LANG));
		}
		INodesForEObjectProvider nodes = createNodeProvider(semanticObject);
		SequenceFeeder feeder = createSequencerFeeder(semanticObject, nodes);
		feeder.accept(grammarAccess.getLangTagAccess().getLangIDTerminalRuleCall_1_0(), semanticObject.getLang());
		feeder.finish();
	}
	
	
	/**
	 * Constraint:
	 *     (property=GraphNode object=GraphNode)
	 */
	protected void sequence_PropertyList(EObject context, PropertyList semanticObject) {
		if(errorAcceptor != null) {
			if(transientValues.isValueTransient(semanticObject, StreamingsparqlPackage.Literals.PROPERTY_LIST__PROPERTY) == ValueTransient.YES)
				errorAcceptor.accept(diagnosticProvider.createFeatureValueMissing(semanticObject, StreamingsparqlPackage.Literals.PROPERTY_LIST__PROPERTY));
			if(transientValues.isValueTransient(semanticObject, StreamingsparqlPackage.Literals.PROPERTY_LIST__OBJECT) == ValueTransient.YES)
				errorAcceptor.accept(diagnosticProvider.createFeatureValueMissing(semanticObject, StreamingsparqlPackage.Literals.PROPERTY_LIST__OBJECT));
		}
		INodesForEObjectProvider nodes = createNodeProvider(semanticObject);
		SequenceFeeder feeder = createSequencerFeeder(semanticObject, nodes);
		feeder.accept(grammarAccess.getPropertyListAccess().getPropertyGraphNodeParserRuleCall_0_0(), semanticObject.getProperty());
		feeder.accept(grammarAccess.getPropertyListAccess().getObjectGraphNodeParserRuleCall_1_0(), semanticObject.getObject());
		feeder.finish();
	}
	
	
	/**
	 * Constraint:
	 *     (prefix=[Prefix|ID] name=ID)
	 */
	protected void sequence_Property(EObject context, NamedVariable semanticObject) {
		if(errorAcceptor != null) {
			if(transientValues.isValueTransient(semanticObject, StreamingsparqlPackage.Literals.NAMED_VARIABLE__PREFIX) == ValueTransient.YES)
				errorAcceptor.accept(diagnosticProvider.createFeatureValueMissing(semanticObject, StreamingsparqlPackage.Literals.NAMED_VARIABLE__PREFIX));
			if(transientValues.isValueTransient(semanticObject, StreamingsparqlPackage.Literals.NAMED_VARIABLE__NAME) == ValueTransient.YES)
				errorAcceptor.accept(diagnosticProvider.createFeatureValueMissing(semanticObject, StreamingsparqlPackage.Literals.NAMED_VARIABLE__NAME));
		}
		INodesForEObjectProvider nodes = createNodeProvider(semanticObject);
		SequenceFeeder feeder = createSequencerFeeder(semanticObject, nodes);
		feeder.accept(grammarAccess.getPropertyAccess().getPrefixPrefixIDTerminalRuleCall_0_0_1(), semanticObject.getPrefix());
		feeder.accept(grammarAccess.getPropertyAccess().getNameIDTerminalRuleCall_2_0(), semanticObject.getName());
		feeder.finish();
	}
	
	
	/**
	 * Constraint:
	 *     (
	 *         method='#ADDQUERY'? 
	 *         base=Base? 
	 *         prefixes+=Prefix* 
	 *         datasetClauses+=DatasetClause* 
	 *         (isDistinct?='DISTINCT' | isReduced?='REDUCED')? 
	 *         variables+=Variable 
	 *         variables+=Variable* 
	 *         whereClause=WhereClause 
	 *         filterclause=Filterclause? 
	 *         aggregateClause=Aggregate? 
	 *         filesinkclause=Filesinkclause?
	 *     )
	 */
	protected void sequence_SelectQuery(EObject context, SelectQuery semanticObject) {
		genericSequencer.createSequence(context, semanticObject);
	}
	
	
	/**
	 * Constraint:
	 *     (subject=GraphNode propertyList+=PropertyList propertyList+=PropertyList*)
	 */
	protected void sequence_TriplesSameSubject(EObject context, TriplesSameSubject semanticObject) {
		genericSequencer.createSequence(context, semanticObject);
	}
	
	
	/**
	 * Constraint:
	 *     type=GraphNode
	 */
	protected void sequence_TypeTag(EObject context, TypeTag semanticObject) {
		if(errorAcceptor != null) {
			if(transientValues.isValueTransient(semanticObject, StreamingsparqlPackage.Literals.TYPE_TAG__TYPE) == ValueTransient.YES)
				errorAcceptor.accept(diagnosticProvider.createFeatureValueMissing(semanticObject, StreamingsparqlPackage.Literals.TYPE_TAG__TYPE));
		}
		INodesForEObjectProvider nodes = createNodeProvider(semanticObject);
		SequenceFeeder feeder = createSequencerFeeder(semanticObject, nodes);
		feeder.accept(grammarAccess.getTypeTagAccess().getTypeGraphNodeParserRuleCall_1_0(), semanticObject.getType());
		feeder.finish();
	}
	
	
	/**
	 * Constraint:
	 *     iref=IRI_TERMINAL
	 */
	protected void sequence_UnNamedPrefix(EObject context, Prefix semanticObject) {
		genericSequencer.createSequence(context, semanticObject);
	}
	
	
	/**
	 * Constraint:
	 *     name=ID
	 */
	protected void sequence_UnNamedVariable(EObject context, UnNamedVariable semanticObject) {
		if(errorAcceptor != null) {
			if(transientValues.isValueTransient(semanticObject, StreamingsparqlPackage.Literals.UN_NAMED_VARIABLE__NAME) == ValueTransient.YES)
				errorAcceptor.accept(diagnosticProvider.createFeatureValueMissing(semanticObject, StreamingsparqlPackage.Literals.UN_NAMED_VARIABLE__NAME));
		}
		INodesForEObjectProvider nodes = createNodeProvider(semanticObject);
		SequenceFeeder feeder = createSequencerFeeder(semanticObject, nodes);
		feeder.accept(grammarAccess.getUnNamedVariableAccess().getNameIDTerminalRuleCall_1_0(), semanticObject.getName());
		feeder.finish();
	}
	
	
	/**
	 * Constraint:
	 *     (unnamed=UnNamedVariable | property=Property)
	 */
	protected void sequence_Variable(EObject context, Variable semanticObject) {
		genericSequencer.createSequence(context, semanticObject);
	}
	
	
	/**
	 * Constraint:
	 *     whereclauses+=InnerWhereClause+
	 */
	protected void sequence_WhereClause(EObject context, WhereClause semanticObject) {
		genericSequencer.createSequence(context, semanticObject);
	}
}
