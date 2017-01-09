/*
 * generated by Xtext 2.10.0
 */
package de.uniol.inf.is.odysseus.parser.novel.cql.serializer;

import com.google.inject.Inject;
import de.uniol.inf.is.odysseus.parser.novel.cql.cQL.AccessFramework;
import de.uniol.inf.is.odysseus.parser.novel.cql.cQL.Aggregation;
import de.uniol.inf.is.odysseus.parser.novel.cql.cQL.Alias;
import de.uniol.inf.is.odysseus.parser.novel.cql.cQL.And;
import de.uniol.inf.is.odysseus.parser.novel.cql.cQL.Attribute;
import de.uniol.inf.is.odysseus.parser.novel.cql.cQL.AttributeRef;
import de.uniol.inf.is.odysseus.parser.novel.cql.cQL.AttributeWithNestedStatement;
import de.uniol.inf.is.odysseus.parser.novel.cql.cQL.BoolConstant;
import de.uniol.inf.is.odysseus.parser.novel.cql.cQL.Bracket;
import de.uniol.inf.is.odysseus.parser.novel.cql.cQL.CQLPackage;
import de.uniol.inf.is.odysseus.parser.novel.cql.cQL.ChannelFormat;
import de.uniol.inf.is.odysseus.parser.novel.cql.cQL.ChannelFormatStream;
import de.uniol.inf.is.odysseus.parser.novel.cql.cQL.ChannelFormatView;
import de.uniol.inf.is.odysseus.parser.novel.cql.cQL.Comparision;
import de.uniol.inf.is.odysseus.parser.novel.cql.cQL.Create;
import de.uniol.inf.is.odysseus.parser.novel.cql.cQL.DataType;
import de.uniol.inf.is.odysseus.parser.novel.cql.cQL.Drop;
import de.uniol.inf.is.odysseus.parser.novel.cql.cQL.Equality;
import de.uniol.inf.is.odysseus.parser.novel.cql.cQL.ExpressionsModel;
import de.uniol.inf.is.odysseus.parser.novel.cql.cQL.FloatConstant;
import de.uniol.inf.is.odysseus.parser.novel.cql.cQL.IntConstant;
import de.uniol.inf.is.odysseus.parser.novel.cql.cQL.Minus;
import de.uniol.inf.is.odysseus.parser.novel.cql.cQL.Model;
import de.uniol.inf.is.odysseus.parser.novel.cql.cQL.MulOrDiv;
import de.uniol.inf.is.odysseus.parser.novel.cql.cQL.NOT;
import de.uniol.inf.is.odysseus.parser.novel.cql.cQL.Or;
import de.uniol.inf.is.odysseus.parser.novel.cql.cQL.Plus;
import de.uniol.inf.is.odysseus.parser.novel.cql.cQL.Select;
import de.uniol.inf.is.odysseus.parser.novel.cql.cQL.Source;
import de.uniol.inf.is.odysseus.parser.novel.cql.cQL.Statement;
import de.uniol.inf.is.odysseus.parser.novel.cql.cQL.StreamTo;
import de.uniol.inf.is.odysseus.parser.novel.cql.cQL.StringConstant;
import de.uniol.inf.is.odysseus.parser.novel.cql.cQL.Window_Timebased;
import de.uniol.inf.is.odysseus.parser.novel.cql.cQL.Window_Tuplebased;
import de.uniol.inf.is.odysseus.parser.novel.cql.services.CQLGrammarAccess;
import java.util.Set;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.xtext.Action;
import org.eclipse.xtext.Parameter;
import org.eclipse.xtext.ParserRule;
import org.eclipse.xtext.serializer.ISerializationContext;
import org.eclipse.xtext.serializer.acceptor.SequenceFeeder;
import org.eclipse.xtext.serializer.sequencer.AbstractDelegatingSemanticSequencer;
import org.eclipse.xtext.serializer.sequencer.ITransientValueService.ValueTransient;

@SuppressWarnings("all")
public class CQLSemanticSequencer extends AbstractDelegatingSemanticSequencer {

	@Inject
	private CQLGrammarAccess grammarAccess;
	
	@Override
	public void sequence(ISerializationContext context, EObject semanticObject) {
		EPackage epackage = semanticObject.eClass().getEPackage();
		ParserRule rule = context.getParserRule();
		Action action = context.getAssignedAction();
		Set<Parameter> parameters = context.getEnabledBooleanParameters();
		if (epackage == CQLPackage.eINSTANCE)
			switch (semanticObject.eClass().getClassifierID()) {
			case CQLPackage.ACCESS_FRAMEWORK:
				sequence_AccessFramework(context, (AccessFramework) semanticObject); 
				return; 
			case CQLPackage.AGGREGATION:
				sequence_Aggregation(context, (Aggregation) semanticObject); 
				return; 
			case CQLPackage.ALIAS:
				sequence_Alias(context, (Alias) semanticObject); 
				return; 
			case CQLPackage.AND:
				sequence_And(context, (And) semanticObject); 
				return; 
			case CQLPackage.ATTRIBUTE:
				if (rule == grammarAccess.getAttributeWithoutAliasRule()) {
					sequence_AttributeWithoutAlias(context, (Attribute) semanticObject); 
					return; 
				}
				else if (rule == grammarAccess.getAttributeRule()) {
					sequence_Attribute(context, (Attribute) semanticObject); 
					return; 
				}
				else break;
			case CQLPackage.ATTRIBUTE_REF:
				sequence_Atomic(context, (AttributeRef) semanticObject); 
				return; 
			case CQLPackage.ATTRIBUTE_WITH_NESTED_STATEMENT:
				sequence_AttributeWithNestedStatement(context, (AttributeWithNestedStatement) semanticObject); 
				return; 
			case CQLPackage.BOOL_CONSTANT:
				sequence_Atomic(context, (BoolConstant) semanticObject); 
				return; 
			case CQLPackage.BRACKET:
				sequence_Primary(context, (Bracket) semanticObject); 
				return; 
			case CQLPackage.CHANNEL_FORMAT:
				sequence_ChannelFormat(context, (ChannelFormat) semanticObject); 
				return; 
			case CQLPackage.CHANNEL_FORMAT_STREAM:
				sequence_ChannelFormatStream(context, (ChannelFormatStream) semanticObject); 
				return; 
			case CQLPackage.CHANNEL_FORMAT_VIEW:
				sequence_ChannelFormatView(context, (ChannelFormatView) semanticObject); 
				return; 
			case CQLPackage.COMPARISION:
				sequence_Comparison(context, (Comparision) semanticObject); 
				return; 
			case CQLPackage.CREATE:
				sequence_Create(context, (Create) semanticObject); 
				return; 
			case CQLPackage.DATA_TYPE:
				sequence_DataType(context, (DataType) semanticObject); 
				return; 
			case CQLPackage.DROP:
				sequence_Drop(context, (Drop) semanticObject); 
				return; 
			case CQLPackage.EQUALITY:
				sequence_Equalitiy(context, (Equality) semanticObject); 
				return; 
			case CQLPackage.EXPRESSIONS_MODEL:
				sequence_ExpressionsModel(context, (ExpressionsModel) semanticObject); 
				return; 
			case CQLPackage.FLOAT_CONSTANT:
				sequence_Atomic(context, (FloatConstant) semanticObject); 
				return; 
			case CQLPackage.INT_CONSTANT:
				sequence_Atomic(context, (IntConstant) semanticObject); 
				return; 
			case CQLPackage.MINUS:
				sequence_PlusOrMinus(context, (Minus) semanticObject); 
				return; 
			case CQLPackage.MODEL:
				sequence_Model(context, (Model) semanticObject); 
				return; 
			case CQLPackage.MUL_OR_DIV:
				sequence_MulOrDiv(context, (MulOrDiv) semanticObject); 
				return; 
			case CQLPackage.NOT:
				sequence_Primary(context, (NOT) semanticObject); 
				return; 
			case CQLPackage.OR:
				sequence_Or(context, (Or) semanticObject); 
				return; 
			case CQLPackage.PLUS:
				sequence_PlusOrMinus(context, (Plus) semanticObject); 
				return; 
			case CQLPackage.SELECT:
				sequence_Select(context, (Select) semanticObject); 
				return; 
			case CQLPackage.SOURCE:
				sequence_Source(context, (Source) semanticObject); 
				return; 
			case CQLPackage.STATEMENT:
				sequence_Statement(context, (Statement) semanticObject); 
				return; 
			case CQLPackage.STREAM_TO:
				sequence_StreamTo(context, (StreamTo) semanticObject); 
				return; 
			case CQLPackage.STRING_CONSTANT:
				sequence_Atomic(context, (StringConstant) semanticObject); 
				return; 
			case CQLPackage.WINDOW_TIMEBASED:
				sequence_Window_Timebased(context, (Window_Timebased) semanticObject); 
				return; 
			case CQLPackage.WINDOW_TUPLEBASED:
				sequence_Window_Tuplebased(context, (Window_Tuplebased) semanticObject); 
				return; 
			}
		if (errorAcceptor != null)
			errorAcceptor.accept(diagnosticProvider.createInvalidContextOrTypeDiagnostic(semanticObject, context));
	}
	
	/**
	 * Contexts:
	 *     AccessFramework returns AccessFramework
	 *
	 * Constraint:
	 *     (
	 *         (type='STREAM' | type='SINK') 
	 *         name=ID 
	 *         attributes+=Attribute+ 
	 *         datatypes+=DataType+ 
	 *         (attributes+=Attribute datatypes+=DataType)* 
	 *         wrapper=STRING 
	 *         protocol=STRING 
	 *         transport=STRING 
	 *         datahandler=STRING 
	 *         (keys+=STRING values+=STRING)+ 
	 *         (keys+=STRING values+=STRING)?
	 *     )
	 */
	protected void sequence_AccessFramework(ISerializationContext context, AccessFramework semanticObject) {
		genericSequencer.createSequence(context, semanticObject);
	}
	
	
	/**
	 * Contexts:
	 *     Aggregation returns Aggregation
	 *
	 * Constraint:
	 *     (name=ID attribute=AttributeWithoutAlias alias=Alias?)
	 */
	protected void sequence_Aggregation(ISerializationContext context, Aggregation semanticObject) {
		genericSequencer.createSequence(context, semanticObject);
	}
	
	
	/**
	 * Contexts:
	 *     Alias returns Alias
	 *
	 * Constraint:
	 *     name=ID
	 */
	protected void sequence_Alias(ISerializationContext context, Alias semanticObject) {
		if (errorAcceptor != null) {
			if (transientValues.isValueTransient(semanticObject, CQLPackage.Literals.ALIAS__NAME) == ValueTransient.YES)
				errorAcceptor.accept(diagnosticProvider.createFeatureValueMissing(semanticObject, CQLPackage.Literals.ALIAS__NAME));
		}
		SequenceFeeder feeder = createSequencerFeeder(context, semanticObject);
		feeder.accept(grammarAccess.getAliasAccess().getNameIDTerminalRuleCall_0(), semanticObject.getName());
		feeder.finish();
	}
	
	
	/**
	 * Contexts:
	 *     Expression returns And
	 *     Or returns And
	 *     Or.Or_1_0 returns And
	 *     And returns And
	 *     And.And_1_0 returns And
	 *
	 * Constraint:
	 *     (left=And_And_1_0 right=Equalitiy)
	 */
	protected void sequence_And(ISerializationContext context, And semanticObject) {
		if (errorAcceptor != null) {
			if (transientValues.isValueTransient(semanticObject, CQLPackage.Literals.AND__LEFT) == ValueTransient.YES)
				errorAcceptor.accept(diagnosticProvider.createFeatureValueMissing(semanticObject, CQLPackage.Literals.AND__LEFT));
			if (transientValues.isValueTransient(semanticObject, CQLPackage.Literals.AND__RIGHT) == ValueTransient.YES)
				errorAcceptor.accept(diagnosticProvider.createFeatureValueMissing(semanticObject, CQLPackage.Literals.AND__RIGHT));
		}
		SequenceFeeder feeder = createSequencerFeeder(context, semanticObject);
		feeder.accept(grammarAccess.getAndAccess().getAndLeftAction_1_0(), semanticObject.getLeft());
		feeder.accept(grammarAccess.getAndAccess().getRightEqualitiyParserRuleCall_1_2_0(), semanticObject.getRight());
		feeder.finish();
	}
	
	
	/**
	 * Contexts:
	 *     Expression returns AttributeRef
	 *     Or returns AttributeRef
	 *     Or.Or_1_0 returns AttributeRef
	 *     And returns AttributeRef
	 *     And.And_1_0 returns AttributeRef
	 *     Equalitiy returns AttributeRef
	 *     Equalitiy.Equality_1_0 returns AttributeRef
	 *     Comparison returns AttributeRef
	 *     Comparison.Comparision_1_0 returns AttributeRef
	 *     PlusOrMinus returns AttributeRef
	 *     PlusOrMinus.Plus_1_0_0_0 returns AttributeRef
	 *     PlusOrMinus.Minus_1_0_1_0 returns AttributeRef
	 *     MulOrDiv returns AttributeRef
	 *     MulOrDiv.MulOrDiv_1_0 returns AttributeRef
	 *     Primary returns AttributeRef
	 *     Atomic returns AttributeRef
	 *
	 * Constraint:
	 *     (value=AttributeWithoutAlias | value=AttributeWithNestedStatement)
	 */
	protected void sequence_Atomic(ISerializationContext context, AttributeRef semanticObject) {
		genericSequencer.createSequence(context, semanticObject);
	}
	
	
	/**
	 * Contexts:
	 *     Expression returns BoolConstant
	 *     Or returns BoolConstant
	 *     Or.Or_1_0 returns BoolConstant
	 *     And returns BoolConstant
	 *     And.And_1_0 returns BoolConstant
	 *     Equalitiy returns BoolConstant
	 *     Equalitiy.Equality_1_0 returns BoolConstant
	 *     Comparison returns BoolConstant
	 *     Comparison.Comparision_1_0 returns BoolConstant
	 *     PlusOrMinus returns BoolConstant
	 *     PlusOrMinus.Plus_1_0_0_0 returns BoolConstant
	 *     PlusOrMinus.Minus_1_0_1_0 returns BoolConstant
	 *     MulOrDiv returns BoolConstant
	 *     MulOrDiv.MulOrDiv_1_0 returns BoolConstant
	 *     Primary returns BoolConstant
	 *     Atomic returns BoolConstant
	 *
	 * Constraint:
	 *     (value='TRUE' | value='FALSE')
	 */
	protected void sequence_Atomic(ISerializationContext context, BoolConstant semanticObject) {
		genericSequencer.createSequence(context, semanticObject);
	}
	
	
	/**
	 * Contexts:
	 *     Expression returns FloatConstant
	 *     Or returns FloatConstant
	 *     Or.Or_1_0 returns FloatConstant
	 *     And returns FloatConstant
	 *     And.And_1_0 returns FloatConstant
	 *     Equalitiy returns FloatConstant
	 *     Equalitiy.Equality_1_0 returns FloatConstant
	 *     Comparison returns FloatConstant
	 *     Comparison.Comparision_1_0 returns FloatConstant
	 *     PlusOrMinus returns FloatConstant
	 *     PlusOrMinus.Plus_1_0_0_0 returns FloatConstant
	 *     PlusOrMinus.Minus_1_0_1_0 returns FloatConstant
	 *     MulOrDiv returns FloatConstant
	 *     MulOrDiv.MulOrDiv_1_0 returns FloatConstant
	 *     Primary returns FloatConstant
	 *     Atomic returns FloatConstant
	 *
	 * Constraint:
	 *     value=FLOAT_NUMBER
	 */
	protected void sequence_Atomic(ISerializationContext context, FloatConstant semanticObject) {
		if (errorAcceptor != null) {
			if (transientValues.isValueTransient(semanticObject, CQLPackage.Literals.FLOAT_CONSTANT__VALUE) == ValueTransient.YES)
				errorAcceptor.accept(diagnosticProvider.createFeatureValueMissing(semanticObject, CQLPackage.Literals.FLOAT_CONSTANT__VALUE));
		}
		SequenceFeeder feeder = createSequencerFeeder(context, semanticObject);
		feeder.accept(grammarAccess.getAtomicAccess().getValueFLOAT_NUMBERTerminalRuleCall_1_1_0(), semanticObject.getValue());
		feeder.finish();
	}
	
	
	/**
	 * Contexts:
	 *     Expression returns IntConstant
	 *     Or returns IntConstant
	 *     Or.Or_1_0 returns IntConstant
	 *     And returns IntConstant
	 *     And.And_1_0 returns IntConstant
	 *     Equalitiy returns IntConstant
	 *     Equalitiy.Equality_1_0 returns IntConstant
	 *     Comparison returns IntConstant
	 *     Comparison.Comparision_1_0 returns IntConstant
	 *     PlusOrMinus returns IntConstant
	 *     PlusOrMinus.Plus_1_0_0_0 returns IntConstant
	 *     PlusOrMinus.Minus_1_0_1_0 returns IntConstant
	 *     MulOrDiv returns IntConstant
	 *     MulOrDiv.MulOrDiv_1_0 returns IntConstant
	 *     Primary returns IntConstant
	 *     Atomic returns IntConstant
	 *
	 * Constraint:
	 *     value=INT
	 */
	protected void sequence_Atomic(ISerializationContext context, IntConstant semanticObject) {
		if (errorAcceptor != null) {
			if (transientValues.isValueTransient(semanticObject, CQLPackage.Literals.INT_CONSTANT__VALUE) == ValueTransient.YES)
				errorAcceptor.accept(diagnosticProvider.createFeatureValueMissing(semanticObject, CQLPackage.Literals.INT_CONSTANT__VALUE));
		}
		SequenceFeeder feeder = createSequencerFeeder(context, semanticObject);
		feeder.accept(grammarAccess.getAtomicAccess().getValueINTTerminalRuleCall_0_1_0(), semanticObject.getValue());
		feeder.finish();
	}
	
	
	/**
	 * Contexts:
	 *     Expression returns StringConstant
	 *     Or returns StringConstant
	 *     Or.Or_1_0 returns StringConstant
	 *     And returns StringConstant
	 *     And.And_1_0 returns StringConstant
	 *     Equalitiy returns StringConstant
	 *     Equalitiy.Equality_1_0 returns StringConstant
	 *     Comparison returns StringConstant
	 *     Comparison.Comparision_1_0 returns StringConstant
	 *     PlusOrMinus returns StringConstant
	 *     PlusOrMinus.Plus_1_0_0_0 returns StringConstant
	 *     PlusOrMinus.Minus_1_0_1_0 returns StringConstant
	 *     MulOrDiv returns StringConstant
	 *     MulOrDiv.MulOrDiv_1_0 returns StringConstant
	 *     Primary returns StringConstant
	 *     Atomic returns StringConstant
	 *
	 * Constraint:
	 *     value=STRING
	 */
	protected void sequence_Atomic(ISerializationContext context, StringConstant semanticObject) {
		if (errorAcceptor != null) {
			if (transientValues.isValueTransient(semanticObject, CQLPackage.Literals.STRING_CONSTANT__VALUE) == ValueTransient.YES)
				errorAcceptor.accept(diagnosticProvider.createFeatureValueMissing(semanticObject, CQLPackage.Literals.STRING_CONSTANT__VALUE));
		}
		SequenceFeeder feeder = createSequencerFeeder(context, semanticObject);
		feeder.accept(grammarAccess.getAtomicAccess().getValueSTRINGTerminalRuleCall_2_1_0(), semanticObject.getValue());
		feeder.finish();
	}
	
	
	/**
	 * Contexts:
	 *     AttributeWithNestedStatement returns AttributeWithNestedStatement
	 *
	 * Constraint:
	 *     (value=Attribute nested=NestedStatement)
	 */
	protected void sequence_AttributeWithNestedStatement(ISerializationContext context, AttributeWithNestedStatement semanticObject) {
		if (errorAcceptor != null) {
			if (transientValues.isValueTransient(semanticObject, CQLPackage.Literals.ATTRIBUTE_WITH_NESTED_STATEMENT__VALUE) == ValueTransient.YES)
				errorAcceptor.accept(diagnosticProvider.createFeatureValueMissing(semanticObject, CQLPackage.Literals.ATTRIBUTE_WITH_NESTED_STATEMENT__VALUE));
			if (transientValues.isValueTransient(semanticObject, CQLPackage.Literals.ATTRIBUTE_WITH_NESTED_STATEMENT__NESTED) == ValueTransient.YES)
				errorAcceptor.accept(diagnosticProvider.createFeatureValueMissing(semanticObject, CQLPackage.Literals.ATTRIBUTE_WITH_NESTED_STATEMENT__NESTED));
		}
		SequenceFeeder feeder = createSequencerFeeder(context, semanticObject);
		feeder.accept(grammarAccess.getAttributeWithNestedStatementAccess().getValueAttributeParserRuleCall_0_0(), semanticObject.getValue());
		feeder.accept(grammarAccess.getAttributeWithNestedStatementAccess().getNestedNestedStatementParserRuleCall_2_0(), semanticObject.getNested());
		feeder.finish();
	}
	
	
	/**
	 * Contexts:
	 *     AttributeWithoutAlias returns Attribute
	 *
	 * Constraint:
	 *     name=ID
	 */
	protected void sequence_AttributeWithoutAlias(ISerializationContext context, Attribute semanticObject) {
		if (errorAcceptor != null) {
			if (transientValues.isValueTransient(semanticObject, CQLPackage.Literals.ATTRIBUTE__NAME) == ValueTransient.YES)
				errorAcceptor.accept(diagnosticProvider.createFeatureValueMissing(semanticObject, CQLPackage.Literals.ATTRIBUTE__NAME));
		}
		SequenceFeeder feeder = createSequencerFeeder(context, semanticObject);
		feeder.accept(grammarAccess.getAttributeWithoutAliasAccess().getNameIDTerminalRuleCall_0(), semanticObject.getName());
		feeder.finish();
	}
	
	
	/**
	 * Contexts:
	 *     Attribute returns Attribute
	 *
	 * Constraint:
	 *     (name=ID alias=Alias?)
	 */
	protected void sequence_Attribute(ISerializationContext context, Attribute semanticObject) {
		genericSequencer.createSequence(context, semanticObject);
	}
	
	
	/**
	 * Contexts:
	 *     ChannelFormatStream returns ChannelFormatStream
	 *
	 * Constraint:
	 *     (
	 *         name=ID 
	 *         attributes+=Attribute+ 
	 *         datatypes+=DataType+ 
	 *         (attributes+=Attribute datatypes+=DataType)* 
	 *         host=ID 
	 *         port=INT
	 *     )
	 */
	protected void sequence_ChannelFormatStream(ISerializationContext context, ChannelFormatStream semanticObject) {
		genericSequencer.createSequence(context, semanticObject);
	}
	
	
	/**
	 * Contexts:
	 *     ChannelFormatView returns ChannelFormatView
	 *
	 * Constraint:
	 *     (name=ID select=Select)
	 */
	protected void sequence_ChannelFormatView(ISerializationContext context, ChannelFormatView semanticObject) {
		if (errorAcceptor != null) {
			if (transientValues.isValueTransient(semanticObject, CQLPackage.Literals.CHANNEL_FORMAT_VIEW__NAME) == ValueTransient.YES)
				errorAcceptor.accept(diagnosticProvider.createFeatureValueMissing(semanticObject, CQLPackage.Literals.CHANNEL_FORMAT_VIEW__NAME));
			if (transientValues.isValueTransient(semanticObject, CQLPackage.Literals.CHANNEL_FORMAT_VIEW__SELECT) == ValueTransient.YES)
				errorAcceptor.accept(diagnosticProvider.createFeatureValueMissing(semanticObject, CQLPackage.Literals.CHANNEL_FORMAT_VIEW__SELECT));
		}
		SequenceFeeder feeder = createSequencerFeeder(context, semanticObject);
		feeder.accept(grammarAccess.getChannelFormatViewAccess().getNameIDTerminalRuleCall_1_0(), semanticObject.getName());
		feeder.accept(grammarAccess.getChannelFormatViewAccess().getSelectSelectParserRuleCall_4_0(), semanticObject.getSelect());
		feeder.finish();
	}
	
	
	/**
	 * Contexts:
	 *     ChannelFormat returns ChannelFormat
	 *
	 * Constraint:
	 *     (stream=ChannelFormatStream | view=ChannelFormatView)
	 */
	protected void sequence_ChannelFormat(ISerializationContext context, ChannelFormat semanticObject) {
		genericSequencer.createSequence(context, semanticObject);
	}
	
	
	/**
	 * Contexts:
	 *     Expression returns Comparision
	 *     Or returns Comparision
	 *     Or.Or_1_0 returns Comparision
	 *     And returns Comparision
	 *     And.And_1_0 returns Comparision
	 *     Equalitiy returns Comparision
	 *     Equalitiy.Equality_1_0 returns Comparision
	 *     Comparison returns Comparision
	 *     Comparison.Comparision_1_0 returns Comparision
	 *
	 * Constraint:
	 *     (left=Comparison_Comparision_1_0 (op='>=' | op='<=' | op='<' | op='>') right=PlusOrMinus)
	 */
	protected void sequence_Comparison(ISerializationContext context, Comparision semanticObject) {
		genericSequencer.createSequence(context, semanticObject);
	}
	
	
	/**
	 * Contexts:
	 *     Create returns Create
	 *
	 * Constraint:
	 *     ((name='CREATE' | name='ATTACH') (channelformat=ChannelFormat | accessframework=AccessFramework))
	 */
	protected void sequence_Create(ISerializationContext context, Create semanticObject) {
		genericSequencer.createSequence(context, semanticObject);
	}
	
	
	/**
	 * Contexts:
	 *     DataType returns DataType
	 *
	 * Constraint:
	 *     (
	 *         value='INTEGER' | 
	 *         value='DOUBLE' | 
	 *         value='FLOAT' | 
	 *         value='STRING' | 
	 *         value='BOOLEAN' | 
	 *         value='STARTTIMESTAMP' | 
	 *         value='ENDTIMESTAMP'
	 *     )
	 */
	protected void sequence_DataType(ISerializationContext context, DataType semanticObject) {
		genericSequencer.createSequence(context, semanticObject);
	}
	
	
	/**
	 * Contexts:
	 *     Drop returns Drop
	 *
	 * Constraint:
	 *     (name='SINK' | name='STREAM')
	 */
	protected void sequence_Drop(ISerializationContext context, Drop semanticObject) {
		genericSequencer.createSequence(context, semanticObject);
	}
	
	
	/**
	 * Contexts:
	 *     Expression returns Equality
	 *     Or returns Equality
	 *     Or.Or_1_0 returns Equality
	 *     And returns Equality
	 *     And.And_1_0 returns Equality
	 *     Equalitiy returns Equality
	 *     Equalitiy.Equality_1_0 returns Equality
	 *
	 * Constraint:
	 *     (left=Equalitiy_Equality_1_0 (op='==' | op='!=') right=Comparison)
	 */
	protected void sequence_Equalitiy(ISerializationContext context, Equality semanticObject) {
		genericSequencer.createSequence(context, semanticObject);
	}
	
	
	/**
	 * Contexts:
	 *     ExpressionsModel returns ExpressionsModel
	 *
	 * Constraint:
	 *     elements+=Expression
	 */
	protected void sequence_ExpressionsModel(ISerializationContext context, ExpressionsModel semanticObject) {
		genericSequencer.createSequence(context, semanticObject);
	}
	
	
	/**
	 * Contexts:
	 *     Model returns Model
	 *
	 * Constraint:
	 *     statements+=Statement+
	 */
	protected void sequence_Model(ISerializationContext context, Model semanticObject) {
		genericSequencer.createSequence(context, semanticObject);
	}
	
	
	/**
	 * Contexts:
	 *     Expression returns MulOrDiv
	 *     Or returns MulOrDiv
	 *     Or.Or_1_0 returns MulOrDiv
	 *     And returns MulOrDiv
	 *     And.And_1_0 returns MulOrDiv
	 *     Equalitiy returns MulOrDiv
	 *     Equalitiy.Equality_1_0 returns MulOrDiv
	 *     Comparison returns MulOrDiv
	 *     Comparison.Comparision_1_0 returns MulOrDiv
	 *     PlusOrMinus returns MulOrDiv
	 *     PlusOrMinus.Plus_1_0_0_0 returns MulOrDiv
	 *     PlusOrMinus.Minus_1_0_1_0 returns MulOrDiv
	 *     MulOrDiv returns MulOrDiv
	 *     MulOrDiv.MulOrDiv_1_0 returns MulOrDiv
	 *
	 * Constraint:
	 *     (left=MulOrDiv_MulOrDiv_1_0 (op='*' | op='/') right=Primary)
	 */
	protected void sequence_MulOrDiv(ISerializationContext context, MulOrDiv semanticObject) {
		genericSequencer.createSequence(context, semanticObject);
	}
	
	
	/**
	 * Contexts:
	 *     Expression returns Or
	 *     Or returns Or
	 *     Or.Or_1_0 returns Or
	 *
	 * Constraint:
	 *     (left=Or_Or_1_0 right=And)
	 */
	protected void sequence_Or(ISerializationContext context, Or semanticObject) {
		if (errorAcceptor != null) {
			if (transientValues.isValueTransient(semanticObject, CQLPackage.Literals.OR__LEFT) == ValueTransient.YES)
				errorAcceptor.accept(diagnosticProvider.createFeatureValueMissing(semanticObject, CQLPackage.Literals.OR__LEFT));
			if (transientValues.isValueTransient(semanticObject, CQLPackage.Literals.OR__RIGHT) == ValueTransient.YES)
				errorAcceptor.accept(diagnosticProvider.createFeatureValueMissing(semanticObject, CQLPackage.Literals.OR__RIGHT));
		}
		SequenceFeeder feeder = createSequencerFeeder(context, semanticObject);
		feeder.accept(grammarAccess.getOrAccess().getOrLeftAction_1_0(), semanticObject.getLeft());
		feeder.accept(grammarAccess.getOrAccess().getRightAndParserRuleCall_1_2_0(), semanticObject.getRight());
		feeder.finish();
	}
	
	
	/**
	 * Contexts:
	 *     Expression returns Minus
	 *     Or returns Minus
	 *     Or.Or_1_0 returns Minus
	 *     And returns Minus
	 *     And.And_1_0 returns Minus
	 *     Equalitiy returns Minus
	 *     Equalitiy.Equality_1_0 returns Minus
	 *     Comparison returns Minus
	 *     Comparison.Comparision_1_0 returns Minus
	 *     PlusOrMinus returns Minus
	 *     PlusOrMinus.Plus_1_0_0_0 returns Minus
	 *     PlusOrMinus.Minus_1_0_1_0 returns Minus
	 *
	 * Constraint:
	 *     (left=PlusOrMinus_Minus_1_0_1_0 right=MulOrDiv)
	 */
	protected void sequence_PlusOrMinus(ISerializationContext context, Minus semanticObject) {
		if (errorAcceptor != null) {
			if (transientValues.isValueTransient(semanticObject, CQLPackage.Literals.MINUS__LEFT) == ValueTransient.YES)
				errorAcceptor.accept(diagnosticProvider.createFeatureValueMissing(semanticObject, CQLPackage.Literals.MINUS__LEFT));
			if (transientValues.isValueTransient(semanticObject, CQLPackage.Literals.MINUS__RIGHT) == ValueTransient.YES)
				errorAcceptor.accept(diagnosticProvider.createFeatureValueMissing(semanticObject, CQLPackage.Literals.MINUS__RIGHT));
		}
		SequenceFeeder feeder = createSequencerFeeder(context, semanticObject);
		feeder.accept(grammarAccess.getPlusOrMinusAccess().getMinusLeftAction_1_0_1_0(), semanticObject.getLeft());
		feeder.accept(grammarAccess.getPlusOrMinusAccess().getRightMulOrDivParserRuleCall_1_1_0(), semanticObject.getRight());
		feeder.finish();
	}
	
	
	/**
	 * Contexts:
	 *     Expression returns Plus
	 *     Or returns Plus
	 *     Or.Or_1_0 returns Plus
	 *     And returns Plus
	 *     And.And_1_0 returns Plus
	 *     Equalitiy returns Plus
	 *     Equalitiy.Equality_1_0 returns Plus
	 *     Comparison returns Plus
	 *     Comparison.Comparision_1_0 returns Plus
	 *     PlusOrMinus returns Plus
	 *     PlusOrMinus.Plus_1_0_0_0 returns Plus
	 *     PlusOrMinus.Minus_1_0_1_0 returns Plus
	 *
	 * Constraint:
	 *     (left=PlusOrMinus_Plus_1_0_0_0 right=MulOrDiv)
	 */
	protected void sequence_PlusOrMinus(ISerializationContext context, Plus semanticObject) {
		if (errorAcceptor != null) {
			if (transientValues.isValueTransient(semanticObject, CQLPackage.Literals.PLUS__LEFT) == ValueTransient.YES)
				errorAcceptor.accept(diagnosticProvider.createFeatureValueMissing(semanticObject, CQLPackage.Literals.PLUS__LEFT));
			if (transientValues.isValueTransient(semanticObject, CQLPackage.Literals.PLUS__RIGHT) == ValueTransient.YES)
				errorAcceptor.accept(diagnosticProvider.createFeatureValueMissing(semanticObject, CQLPackage.Literals.PLUS__RIGHT));
		}
		SequenceFeeder feeder = createSequencerFeeder(context, semanticObject);
		feeder.accept(grammarAccess.getPlusOrMinusAccess().getPlusLeftAction_1_0_0_0(), semanticObject.getLeft());
		feeder.accept(grammarAccess.getPlusOrMinusAccess().getRightMulOrDivParserRuleCall_1_1_0(), semanticObject.getRight());
		feeder.finish();
	}
	
	
	/**
	 * Contexts:
	 *     Expression returns Bracket
	 *     Or returns Bracket
	 *     Or.Or_1_0 returns Bracket
	 *     And returns Bracket
	 *     And.And_1_0 returns Bracket
	 *     Equalitiy returns Bracket
	 *     Equalitiy.Equality_1_0 returns Bracket
	 *     Comparison returns Bracket
	 *     Comparison.Comparision_1_0 returns Bracket
	 *     PlusOrMinus returns Bracket
	 *     PlusOrMinus.Plus_1_0_0_0 returns Bracket
	 *     PlusOrMinus.Minus_1_0_1_0 returns Bracket
	 *     MulOrDiv returns Bracket
	 *     MulOrDiv.MulOrDiv_1_0 returns Bracket
	 *     Primary returns Bracket
	 *
	 * Constraint:
	 *     inner=Expression
	 */
	protected void sequence_Primary(ISerializationContext context, Bracket semanticObject) {
		if (errorAcceptor != null) {
			if (transientValues.isValueTransient(semanticObject, CQLPackage.Literals.BRACKET__INNER) == ValueTransient.YES)
				errorAcceptor.accept(diagnosticProvider.createFeatureValueMissing(semanticObject, CQLPackage.Literals.BRACKET__INNER));
		}
		SequenceFeeder feeder = createSequencerFeeder(context, semanticObject);
		feeder.accept(grammarAccess.getPrimaryAccess().getInnerExpressionParserRuleCall_0_2_0(), semanticObject.getInner());
		feeder.finish();
	}
	
	
	/**
	 * Contexts:
	 *     Expression returns NOT
	 *     Or returns NOT
	 *     Or.Or_1_0 returns NOT
	 *     And returns NOT
	 *     And.And_1_0 returns NOT
	 *     Equalitiy returns NOT
	 *     Equalitiy.Equality_1_0 returns NOT
	 *     Comparison returns NOT
	 *     Comparison.Comparision_1_0 returns NOT
	 *     PlusOrMinus returns NOT
	 *     PlusOrMinus.Plus_1_0_0_0 returns NOT
	 *     PlusOrMinus.Minus_1_0_1_0 returns NOT
	 *     MulOrDiv returns NOT
	 *     MulOrDiv.MulOrDiv_1_0 returns NOT
	 *     Primary returns NOT
	 *
	 * Constraint:
	 *     expression=Primary
	 */
	protected void sequence_Primary(ISerializationContext context, NOT semanticObject) {
		if (errorAcceptor != null) {
			if (transientValues.isValueTransient(semanticObject, CQLPackage.Literals.NOT__EXPRESSION) == ValueTransient.YES)
				errorAcceptor.accept(diagnosticProvider.createFeatureValueMissing(semanticObject, CQLPackage.Literals.NOT__EXPRESSION));
		}
		SequenceFeeder feeder = createSequencerFeeder(context, semanticObject);
		feeder.accept(grammarAccess.getPrimaryAccess().getExpressionPrimaryParserRuleCall_1_2_0(), semanticObject.getExpression());
		feeder.finish();
	}
	
	
	/**
	 * Contexts:
	 *     Select returns Select
	 *     NestedStatement returns Select
	 *
	 * Constraint:
	 *     (
	 *         name='SELECT' 
	 *         distinct='DISTINCT'? 
	 *         (attributes+=Attribute | aggregations+=Aggregation | attributes+=Attribute | aggregations+=Aggregation)* 
	 *         sources+=Source+ 
	 *         sources+=Source* 
	 *         predicates=ExpressionsModel? 
	 *         (order+=Attribute+ order+=Attribute*)? 
	 *         having=ExpressionsModel?
	 *     )
	 */
	protected void sequence_Select(ISerializationContext context, Select semanticObject) {
		genericSequencer.createSequence(context, semanticObject);
	}
	
	
	/**
	 * Contexts:
	 *     Source returns Source
	 *
	 * Constraint:
	 *     (((name=ID (unbounded=Window_Unbounded | time=Window_Timebased | tuple=Window_Tuplebased)?) | nested=NestedStatement) alias=Alias?)
	 */
	protected void sequence_Source(ISerializationContext context, Source semanticObject) {
		genericSequencer.createSequence(context, semanticObject);
	}
	
	
	/**
	 * Contexts:
	 *     Statement returns Statement
	 *
	 * Constraint:
	 *     (type=Select | type=Create | type=StreamTo | type=Drop)
	 */
	protected void sequence_Statement(ISerializationContext context, Statement semanticObject) {
		genericSequencer.createSequence(context, semanticObject);
	}
	
	
	/**
	 * Contexts:
	 *     StreamTo returns StreamTo
	 *
	 * Constraint:
	 *     (name=ID (statement=Select | inputname=ID))
	 */
	protected void sequence_StreamTo(ISerializationContext context, StreamTo semanticObject) {
		genericSequencer.createSequence(context, semanticObject);
	}
	
	
	/**
	 * Contexts:
	 *     Window_Timebased returns Window_Timebased
	 *
	 * Constraint:
	 *     (size=INT unit=ID (advance_size=INT advance_unit=ID)?)
	 */
	protected void sequence_Window_Timebased(ISerializationContext context, Window_Timebased semanticObject) {
		genericSequencer.createSequence(context, semanticObject);
	}
	
	
	/**
	 * Contexts:
	 *     Window_Tuplebased returns Window_Tuplebased
	 *
	 * Constraint:
	 *     (size=INT advance_size=INT? partition_attribute=Attribute?)
	 */
	protected void sequence_Window_Tuplebased(ISerializationContext context, Window_Tuplebased semanticObject) {
		genericSequencer.createSequence(context, semanticObject);
	}
	
	
}
