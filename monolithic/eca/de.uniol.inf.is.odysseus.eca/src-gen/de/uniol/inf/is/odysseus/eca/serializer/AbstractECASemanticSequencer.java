/*
 * generated by Xtext 2.12.0
 */
package de.uniol.inf.is.odysseus.eca.serializer;

import com.google.inject.Inject;
import de.uniol.inf.is.odysseus.eca.eCA.ACTIONS;
import de.uniol.inf.is.odysseus.eca.eCA.COMMANDACTION;
import de.uniol.inf.is.odysseus.eca.eCA.CONDITIONS;
import de.uniol.inf.is.odysseus.eca.eCA.Constant;
import de.uniol.inf.is.odysseus.eca.eCA.DefinedEvent;
import de.uniol.inf.is.odysseus.eca.eCA.ECAPackage;
import de.uniol.inf.is.odysseus.eca.eCA.EcaValue;
import de.uniol.inf.is.odysseus.eca.eCA.Expression;
import de.uniol.inf.is.odysseus.eca.eCA.FREECONDITION;
import de.uniol.inf.is.odysseus.eca.eCA.MAPCONDITION;
import de.uniol.inf.is.odysseus.eca.eCA.Model;
import de.uniol.inf.is.odysseus.eca.eCA.QUERYCONDITION;
import de.uniol.inf.is.odysseus.eca.eCA.RNDQUERY;
import de.uniol.inf.is.odysseus.eca.eCA.Rule;
import de.uniol.inf.is.odysseus.eca.eCA.RuleSource;
import de.uniol.inf.is.odysseus.eca.eCA.SOURCECONDITION;
import de.uniol.inf.is.odysseus.eca.eCA.SUBCONDITION;
import de.uniol.inf.is.odysseus.eca.eCA.SYSTEMCONDITION;
import de.uniol.inf.is.odysseus.eca.eCA.Source;
import de.uniol.inf.is.odysseus.eca.eCA.Timer;
import de.uniol.inf.is.odysseus.eca.eCA.Window;
import de.uniol.inf.is.odysseus.eca.services.ECAGrammarAccess;
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
public abstract class AbstractECASemanticSequencer extends AbstractDelegatingSemanticSequencer {

	@Inject
	private ECAGrammarAccess grammarAccess;
	
	@Override
	public void sequence(ISerializationContext context, EObject semanticObject) {
		EPackage epackage = semanticObject.eClass().getEPackage();
		ParserRule rule = context.getParserRule();
		Action action = context.getAssignedAction();
		Set<Parameter> parameters = context.getEnabledBooleanParameters();
		if (epackage == ECAPackage.eINSTANCE)
			switch (semanticObject.eClass().getClassifierID()) {
			case ECAPackage.ACTIONS:
				sequence_ACTIONS(context, (ACTIONS) semanticObject); 
				return; 
			case ECAPackage.COMMANDACTION:
				sequence_COMMANDACTION(context, (COMMANDACTION) semanticObject); 
				return; 
			case ECAPackage.CONDITIONS:
				sequence_CONDITIONS(context, (CONDITIONS) semanticObject); 
				return; 
			case ECAPackage.CONSTANT:
				sequence_Constant(context, (Constant) semanticObject); 
				return; 
			case ECAPackage.DEFINED_EVENT:
				sequence_DefinedEvent(context, (DefinedEvent) semanticObject); 
				return; 
			case ECAPackage.ECA_VALUE:
				sequence_EcaValue(context, (EcaValue) semanticObject); 
				return; 
			case ECAPackage.EXPRESSION:
				if (rule == grammarAccess.getACTIONSRule()
						|| action == grammarAccess.getACTIONSAccess().getACTIONSLeftAction_1_0()
						|| rule == grammarAccess.getSUBACTIONSRule()) {
					sequence_SUBACTIONS(context, (Expression) semanticObject); 
					return; 
				}
				else if (rule == grammarAccess.getCONDITIONSRule()
						|| action == grammarAccess.getCONDITIONSAccess().getCONDITIONSLeftAction_1_0()
						|| rule == grammarAccess.getSUBCONDITIONRule()) {
					sequence_SUBCONDITION(context, (Expression) semanticObject); 
					return; 
				}
				else break;
			case ECAPackage.FREECONDITION:
				sequence_FREECONDITION(context, (FREECONDITION) semanticObject); 
				return; 
			case ECAPackage.MAPCONDITION:
				sequence_MAPCONDITION(context, (MAPCONDITION) semanticObject); 
				return; 
			case ECAPackage.MODEL:
				sequence_Model(context, (Model) semanticObject); 
				return; 
			case ECAPackage.QUERYCONDITION:
				sequence_QUERYCONDITION(context, (QUERYCONDITION) semanticObject); 
				return; 
			case ECAPackage.RNDQUERY:
				sequence_RNDQUERY(context, (RNDQUERY) semanticObject); 
				return; 
			case ECAPackage.RULE:
				sequence_Rule(context, (Rule) semanticObject); 
				return; 
			case ECAPackage.RULE_SOURCE:
				sequence_RuleSource(context, (RuleSource) semanticObject); 
				return; 
			case ECAPackage.SOURCECONDITION:
				sequence_SOURCECONDITION(context, (SOURCECONDITION) semanticObject); 
				return; 
			case ECAPackage.SUBCONDITION:
				sequence_SUBCONDITION(context, (SUBCONDITION) semanticObject); 
				return; 
			case ECAPackage.SYSTEMCONDITION:
				sequence_SYSTEMCONDITION(context, (SYSTEMCONDITION) semanticObject); 
				return; 
			case ECAPackage.SOURCE:
				sequence_Source(context, (Source) semanticObject); 
				return; 
			case ECAPackage.TIMER:
				sequence_Timer(context, (Timer) semanticObject); 
				return; 
			case ECAPackage.WINDOW:
				sequence_Window(context, (Window) semanticObject); 
				return; 
			}
		if (errorAcceptor != null)
			errorAcceptor.accept(diagnosticProvider.createInvalidContextOrTypeDiagnostic(semanticObject, context));
	}
	
	/**
	 * Contexts:
	 *     ACTIONS returns ACTIONS
	 *     ACTIONS.ACTIONS_1_0 returns ACTIONS
	 *
	 * Constraint:
	 *     (left=ACTIONS_ACTIONS_1_0 right=SUBACTIONS)
	 */
	protected void sequence_ACTIONS(ISerializationContext context, ACTIONS semanticObject) {
		if (errorAcceptor != null) {
			if (transientValues.isValueTransient(semanticObject, ECAPackage.Literals.ACTIONS__LEFT) == ValueTransient.YES)
				errorAcceptor.accept(diagnosticProvider.createFeatureValueMissing(semanticObject, ECAPackage.Literals.ACTIONS__LEFT));
			if (transientValues.isValueTransient(semanticObject, ECAPackage.Literals.ACTIONS__RIGHT) == ValueTransient.YES)
				errorAcceptor.accept(diagnosticProvider.createFeatureValueMissing(semanticObject, ECAPackage.Literals.ACTIONS__RIGHT));
		}
		SequenceFeeder feeder = createSequencerFeeder(context, semanticObject);
		feeder.accept(grammarAccess.getACTIONSAccess().getACTIONSLeftAction_1_0(), semanticObject.getLeft());
		feeder.accept(grammarAccess.getACTIONSAccess().getRightSUBACTIONSParserRuleCall_1_2_0(), semanticObject.getRight());
		feeder.finish();
	}
	
	
	/**
	 * Contexts:
	 *     COMMANDACTION returns COMMANDACTION
	 *
	 * Constraint:
	 *     (subActname=ID (functAction=RNDQUERY | actionValue=EcaValue | innerAction+=COMMANDACTION+)?)
	 */
	protected void sequence_COMMANDACTION(ISerializationContext context, COMMANDACTION semanticObject) {
		genericSequencer.createSequence(context, semanticObject);
	}
	
	
	/**
	 * Contexts:
	 *     CONDITIONS returns CONDITIONS
	 *     CONDITIONS.CONDITIONS_1_0 returns CONDITIONS
	 *
	 * Constraint:
	 *     (left=CONDITIONS_CONDITIONS_1_0 right=SUBCONDITION)
	 */
	protected void sequence_CONDITIONS(ISerializationContext context, CONDITIONS semanticObject) {
		if (errorAcceptor != null) {
			if (transientValues.isValueTransient(semanticObject, ECAPackage.Literals.CONDITIONS__LEFT) == ValueTransient.YES)
				errorAcceptor.accept(diagnosticProvider.createFeatureValueMissing(semanticObject, ECAPackage.Literals.CONDITIONS__LEFT));
			if (transientValues.isValueTransient(semanticObject, ECAPackage.Literals.CONDITIONS__RIGHT) == ValueTransient.YES)
				errorAcceptor.accept(diagnosticProvider.createFeatureValueMissing(semanticObject, ECAPackage.Literals.CONDITIONS__RIGHT));
		}
		SequenceFeeder feeder = createSequencerFeeder(context, semanticObject);
		feeder.accept(grammarAccess.getCONDITIONSAccess().getCONDITIONSLeftAction_1_0(), semanticObject.getLeft());
		feeder.accept(grammarAccess.getCONDITIONSAccess().getRightSUBCONDITIONParserRuleCall_1_2_0(), semanticObject.getRight());
		feeder.finish();
	}
	
	
	/**
	 * Contexts:
	 *     Constant returns Constant
	 *
	 * Constraint:
	 *     (name=ID constValue=INT)
	 */
	protected void sequence_Constant(ISerializationContext context, Constant semanticObject) {
		if (errorAcceptor != null) {
			if (transientValues.isValueTransient(semanticObject, ECAPackage.Literals.CONSTANT__NAME) == ValueTransient.YES)
				errorAcceptor.accept(diagnosticProvider.createFeatureValueMissing(semanticObject, ECAPackage.Literals.CONSTANT__NAME));
			if (transientValues.isValueTransient(semanticObject, ECAPackage.Literals.CONSTANT__CONST_VALUE) == ValueTransient.YES)
				errorAcceptor.accept(diagnosticProvider.createFeatureValueMissing(semanticObject, ECAPackage.Literals.CONSTANT__CONST_VALUE));
		}
		SequenceFeeder feeder = createSequencerFeeder(context, semanticObject);
		feeder.accept(grammarAccess.getConstantAccess().getNameIDTerminalRuleCall_2_0(), semanticObject.getName());
		feeder.accept(grammarAccess.getConstantAccess().getConstValueINTTerminalRuleCall_4_0(), semanticObject.getConstValue());
		feeder.finish();
	}
	
	
	/**
	 * Contexts:
	 *     DefinedEvent returns DefinedEvent
	 *
	 * Constraint:
	 *     (name=ID definedSource=Source definedAttribute=ID definedOperator=Operator definedValue=EcaValue)
	 */
	protected void sequence_DefinedEvent(ISerializationContext context, DefinedEvent semanticObject) {
		if (errorAcceptor != null) {
			if (transientValues.isValueTransient(semanticObject, ECAPackage.Literals.DEFINED_EVENT__NAME) == ValueTransient.YES)
				errorAcceptor.accept(diagnosticProvider.createFeatureValueMissing(semanticObject, ECAPackage.Literals.DEFINED_EVENT__NAME));
			if (transientValues.isValueTransient(semanticObject, ECAPackage.Literals.DEFINED_EVENT__DEFINED_SOURCE) == ValueTransient.YES)
				errorAcceptor.accept(diagnosticProvider.createFeatureValueMissing(semanticObject, ECAPackage.Literals.DEFINED_EVENT__DEFINED_SOURCE));
			if (transientValues.isValueTransient(semanticObject, ECAPackage.Literals.DEFINED_EVENT__DEFINED_ATTRIBUTE) == ValueTransient.YES)
				errorAcceptor.accept(diagnosticProvider.createFeatureValueMissing(semanticObject, ECAPackage.Literals.DEFINED_EVENT__DEFINED_ATTRIBUTE));
			if (transientValues.isValueTransient(semanticObject, ECAPackage.Literals.DEFINED_EVENT__DEFINED_OPERATOR) == ValueTransient.YES)
				errorAcceptor.accept(diagnosticProvider.createFeatureValueMissing(semanticObject, ECAPackage.Literals.DEFINED_EVENT__DEFINED_OPERATOR));
			if (transientValues.isValueTransient(semanticObject, ECAPackage.Literals.DEFINED_EVENT__DEFINED_VALUE) == ValueTransient.YES)
				errorAcceptor.accept(diagnosticProvider.createFeatureValueMissing(semanticObject, ECAPackage.Literals.DEFINED_EVENT__DEFINED_VALUE));
		}
		SequenceFeeder feeder = createSequencerFeeder(context, semanticObject);
		feeder.accept(grammarAccess.getDefinedEventAccess().getNameIDTerminalRuleCall_2_0(), semanticObject.getName());
		feeder.accept(grammarAccess.getDefinedEventAccess().getDefinedSourceSourceParserRuleCall_4_0(), semanticObject.getDefinedSource());
		feeder.accept(grammarAccess.getDefinedEventAccess().getDefinedAttributeIDTerminalRuleCall_6_0(), semanticObject.getDefinedAttribute());
		feeder.accept(grammarAccess.getDefinedEventAccess().getDefinedOperatorOperatorParserRuleCall_7_0(), semanticObject.getDefinedOperator());
		feeder.accept(grammarAccess.getDefinedEventAccess().getDefinedValueEcaValueParserRuleCall_8_0(), semanticObject.getDefinedValue());
		feeder.finish();
	}
	
	
	/**
	 * Contexts:
	 *     EcaValue returns EcaValue
	 *
	 * Constraint:
	 *     (intValue=INT | idValue=ID | constValue=[Constant|ID] | stringValue=STRING | doubleValue=DOUBLE)
	 */
	protected void sequence_EcaValue(ISerializationContext context, EcaValue semanticObject) {
		genericSequencer.createSequence(context, semanticObject);
	}
	
	
	/**
	 * Contexts:
	 *     FREECONDITION returns FREECONDITION
	 *
	 * Constraint:
	 *     freeCondition=STRING
	 */
	protected void sequence_FREECONDITION(ISerializationContext context, FREECONDITION semanticObject) {
		if (errorAcceptor != null) {
			if (transientValues.isValueTransient(semanticObject, ECAPackage.Literals.FREECONDITION__FREE_CONDITION) == ValueTransient.YES)
				errorAcceptor.accept(diagnosticProvider.createFeatureValueMissing(semanticObject, ECAPackage.Literals.FREECONDITION__FREE_CONDITION));
		}
		SequenceFeeder feeder = createSequencerFeeder(context, semanticObject);
		feeder.accept(grammarAccess.getFREECONDITIONAccess().getFreeConditionSTRINGTerminalRuleCall_0(), semanticObject.getFreeCondition());
		feeder.finish();
	}
	
	
	/**
	 * Contexts:
	 *     MAPCONDITION returns MAPCONDITION
	 *
	 * Constraint:
	 *     mapCond=STRING
	 */
	protected void sequence_MAPCONDITION(ISerializationContext context, MAPCONDITION semanticObject) {
		if (errorAcceptor != null) {
			if (transientValues.isValueTransient(semanticObject, ECAPackage.Literals.MAPCONDITION__MAP_COND) == ValueTransient.YES)
				errorAcceptor.accept(diagnosticProvider.createFeatureValueMissing(semanticObject, ECAPackage.Literals.MAPCONDITION__MAP_COND));
		}
		SequenceFeeder feeder = createSequencerFeeder(context, semanticObject);
		feeder.accept(grammarAccess.getMAPCONDITIONAccess().getMapCondSTRINGTerminalRuleCall_1_0(), semanticObject.getMapCond());
		feeder.finish();
	}
	
	
	/**
	 * Contexts:
	 *     Model returns Model
	 *
	 * Constraint:
	 *     (constants+=Constant | defEvents+=DefinedEvent | windowSize=Window | timeIntervall=Timer | rules+=Rule)+
	 */
	protected void sequence_Model(ISerializationContext context, Model semanticObject) {
		genericSequencer.createSequence(context, semanticObject);
	}
	
	
	/**
	 * Contexts:
	 *     QUERYCONDITION returns QUERYCONDITION
	 *
	 * Constraint:
	 *     (queryNot='!'? queryFunct=RNDQUERY)
	 */
	protected void sequence_QUERYCONDITION(ISerializationContext context, QUERYCONDITION semanticObject) {
		genericSequencer.createSequence(context, semanticObject);
	}
	
	
	/**
	 * Contexts:
	 *     RNDQUERY returns RNDQUERY
	 *
	 * Constraint:
	 *     (priOperator=Operator priVal=INT (sel='MIN' | sel='MAX')? stateName=ID)
	 */
	protected void sequence_RNDQUERY(ISerializationContext context, RNDQUERY semanticObject) {
		genericSequencer.createSequence(context, semanticObject);
	}
	
	
	/**
	 * Contexts:
	 *     RuleSource returns RuleSource
	 *
	 * Constraint:
	 *     (defSource=[DefinedEvent|ID] | newSource=Source | preSource=PREDEFINEDSOURCE)
	 */
	protected void sequence_RuleSource(ISerializationContext context, RuleSource semanticObject) {
		genericSequencer.createSequence(context, semanticObject);
	}
	
	
	/**
	 * Contexts:
	 *     Rule returns Rule
	 *
	 * Constraint:
	 *     (name=ID source=RuleSource ruleConditions=CONDITIONS ruleActions=ACTIONS)
	 */
	protected void sequence_Rule(ISerializationContext context, Rule semanticObject) {
		if (errorAcceptor != null) {
			if (transientValues.isValueTransient(semanticObject, ECAPackage.Literals.RULE__NAME) == ValueTransient.YES)
				errorAcceptor.accept(diagnosticProvider.createFeatureValueMissing(semanticObject, ECAPackage.Literals.RULE__NAME));
			if (transientValues.isValueTransient(semanticObject, ECAPackage.Literals.RULE__SOURCE) == ValueTransient.YES)
				errorAcceptor.accept(diagnosticProvider.createFeatureValueMissing(semanticObject, ECAPackage.Literals.RULE__SOURCE));
			if (transientValues.isValueTransient(semanticObject, ECAPackage.Literals.RULE__RULE_CONDITIONS) == ValueTransient.YES)
				errorAcceptor.accept(diagnosticProvider.createFeatureValueMissing(semanticObject, ECAPackage.Literals.RULE__RULE_CONDITIONS));
			if (transientValues.isValueTransient(semanticObject, ECAPackage.Literals.RULE__RULE_ACTIONS) == ValueTransient.YES)
				errorAcceptor.accept(diagnosticProvider.createFeatureValueMissing(semanticObject, ECAPackage.Literals.RULE__RULE_ACTIONS));
		}
		SequenceFeeder feeder = createSequencerFeeder(context, semanticObject);
		feeder.accept(grammarAccess.getRuleAccess().getNameIDTerminalRuleCall_1_0(), semanticObject.getName());
		feeder.accept(grammarAccess.getRuleAccess().getSourceRuleSourceParserRuleCall_2_0(), semanticObject.getSource());
		feeder.accept(grammarAccess.getRuleAccess().getRuleConditionsCONDITIONSParserRuleCall_4_0(), semanticObject.getRuleConditions());
		feeder.accept(grammarAccess.getRuleAccess().getRuleActionsACTIONSParserRuleCall_6_0(), semanticObject.getRuleActions());
		feeder.finish();
	}
	
	
	/**
	 * Contexts:
	 *     SOURCECONDITION returns SOURCECONDITION
	 *
	 * Constraint:
	 *     (condAttribute=ID operator=Operator value=EcaValue)
	 */
	protected void sequence_SOURCECONDITION(ISerializationContext context, SOURCECONDITION semanticObject) {
		if (errorAcceptor != null) {
			if (transientValues.isValueTransient(semanticObject, ECAPackage.Literals.SOURCECONDITION__COND_ATTRIBUTE) == ValueTransient.YES)
				errorAcceptor.accept(diagnosticProvider.createFeatureValueMissing(semanticObject, ECAPackage.Literals.SOURCECONDITION__COND_ATTRIBUTE));
			if (transientValues.isValueTransient(semanticObject, ECAPackage.Literals.SOURCECONDITION__OPERATOR) == ValueTransient.YES)
				errorAcceptor.accept(diagnosticProvider.createFeatureValueMissing(semanticObject, ECAPackage.Literals.SOURCECONDITION__OPERATOR));
			if (transientValues.isValueTransient(semanticObject, ECAPackage.Literals.SOURCECONDITION__VALUE) == ValueTransient.YES)
				errorAcceptor.accept(diagnosticProvider.createFeatureValueMissing(semanticObject, ECAPackage.Literals.SOURCECONDITION__VALUE));
		}
		SequenceFeeder feeder = createSequencerFeeder(context, semanticObject);
		feeder.accept(grammarAccess.getSOURCECONDITIONAccess().getCondAttributeIDTerminalRuleCall_0_0(), semanticObject.getCondAttribute());
		feeder.accept(grammarAccess.getSOURCECONDITIONAccess().getOperatorOperatorParserRuleCall_1_0(), semanticObject.getOperator());
		feeder.accept(grammarAccess.getSOURCECONDITIONAccess().getValueEcaValueParserRuleCall_2_0(), semanticObject.getValue());
		feeder.finish();
	}
	
	
	/**
	 * Contexts:
	 *     ACTIONS returns Expression
	 *     ACTIONS.ACTIONS_1_0 returns Expression
	 *     SUBACTIONS returns Expression
	 *
	 * Constraint:
	 *     comAction=COMMANDACTION
	 */
	protected void sequence_SUBACTIONS(ISerializationContext context, Expression semanticObject) {
		if (errorAcceptor != null) {
			if (transientValues.isValueTransient(semanticObject, ECAPackage.Literals.EXPRESSION__COM_ACTION) == ValueTransient.YES)
				errorAcceptor.accept(diagnosticProvider.createFeatureValueMissing(semanticObject, ECAPackage.Literals.EXPRESSION__COM_ACTION));
		}
		SequenceFeeder feeder = createSequencerFeeder(context, semanticObject);
		feeder.accept(grammarAccess.getSUBACTIONSAccess().getComActionCOMMANDACTIONParserRuleCall_0(), semanticObject.getComAction());
		feeder.finish();
	}
	
	
	/**
	 * Contexts:
	 *     CONDITIONS returns Expression
	 *     CONDITIONS.CONDITIONS_1_0 returns Expression
	 *     SUBCONDITION returns Expression
	 *
	 * Constraint:
	 *     (subsource=SOURCECONDITION | subsys=SYSTEMCONDITION)
	 */
	protected void sequence_SUBCONDITION(ISerializationContext context, Expression semanticObject) {
		genericSequencer.createSequence(context, semanticObject);
	}
	
	
	/**
	 * Contexts:
	 *     CONDITIONS returns SUBCONDITION
	 *     CONDITIONS.CONDITIONS_1_0 returns SUBCONDITION
	 *     SUBCONDITION returns SUBCONDITION
	 *
	 * Constraint:
	 *     (subfree=FREECONDITION | submap=MAPCONDITION | queryCond=QUERYCONDITION)?
	 */
	protected void sequence_SUBCONDITION(ISerializationContext context, SUBCONDITION semanticObject) {
		genericSequencer.createSequence(context, semanticObject);
	}
	
	
	/**
	 * Contexts:
	 *     SYSTEMCONDITION returns SYSTEMCONDITION
	 *
	 * Constraint:
	 *     (systemAttribute=SYSTEMFUNCTION operator=Operator value=EcaValue)
	 */
	protected void sequence_SYSTEMCONDITION(ISerializationContext context, SYSTEMCONDITION semanticObject) {
		if (errorAcceptor != null) {
			if (transientValues.isValueTransient(semanticObject, ECAPackage.Literals.SYSTEMCONDITION__SYSTEM_ATTRIBUTE) == ValueTransient.YES)
				errorAcceptor.accept(diagnosticProvider.createFeatureValueMissing(semanticObject, ECAPackage.Literals.SYSTEMCONDITION__SYSTEM_ATTRIBUTE));
			if (transientValues.isValueTransient(semanticObject, ECAPackage.Literals.SYSTEMCONDITION__OPERATOR) == ValueTransient.YES)
				errorAcceptor.accept(diagnosticProvider.createFeatureValueMissing(semanticObject, ECAPackage.Literals.SYSTEMCONDITION__OPERATOR));
			if (transientValues.isValueTransient(semanticObject, ECAPackage.Literals.SYSTEMCONDITION__VALUE) == ValueTransient.YES)
				errorAcceptor.accept(diagnosticProvider.createFeatureValueMissing(semanticObject, ECAPackage.Literals.SYSTEMCONDITION__VALUE));
		}
		SequenceFeeder feeder = createSequencerFeeder(context, semanticObject);
		feeder.accept(grammarAccess.getSYSTEMCONDITIONAccess().getSystemAttributeSYSTEMFUNCTIONParserRuleCall_1_0(), semanticObject.getSystemAttribute());
		feeder.accept(grammarAccess.getSYSTEMCONDITIONAccess().getOperatorOperatorParserRuleCall_2_0(), semanticObject.getOperator());
		feeder.accept(grammarAccess.getSYSTEMCONDITIONAccess().getValueEcaValueParserRuleCall_3_0(), semanticObject.getValue());
		feeder.finish();
	}
	
	
	/**
	 * Contexts:
	 *     Source returns Source
	 *
	 * Constraint:
	 *     name=ID
	 */
	protected void sequence_Source(ISerializationContext context, Source semanticObject) {
		if (errorAcceptor != null) {
			if (transientValues.isValueTransient(semanticObject, ECAPackage.Literals.SOURCE__NAME) == ValueTransient.YES)
				errorAcceptor.accept(diagnosticProvider.createFeatureValueMissing(semanticObject, ECAPackage.Literals.SOURCE__NAME));
		}
		SequenceFeeder feeder = createSequencerFeeder(context, semanticObject);
		feeder.accept(grammarAccess.getSourceAccess().getNameIDTerminalRuleCall_0(), semanticObject.getName());
		feeder.finish();
	}
	
	
	/**
	 * Contexts:
	 *     Timer returns Timer
	 *
	 * Constraint:
	 *     timerIntervallValue=INT
	 */
	protected void sequence_Timer(ISerializationContext context, Timer semanticObject) {
		if (errorAcceptor != null) {
			if (transientValues.isValueTransient(semanticObject, ECAPackage.Literals.TIMER__TIMER_INTERVALL_VALUE) == ValueTransient.YES)
				errorAcceptor.accept(diagnosticProvider.createFeatureValueMissing(semanticObject, ECAPackage.Literals.TIMER__TIMER_INTERVALL_VALUE));
		}
		SequenceFeeder feeder = createSequencerFeeder(context, semanticObject);
		feeder.accept(grammarAccess.getTimerAccess().getTimerIntervallValueINTTerminalRuleCall_3_0(), semanticObject.getTimerIntervallValue());
		feeder.finish();
	}
	
	
	/**
	 * Contexts:
	 *     Window returns Window
	 *
	 * Constraint:
	 *     windowValue=INT
	 */
	protected void sequence_Window(ISerializationContext context, Window semanticObject) {
		if (errorAcceptor != null) {
			if (transientValues.isValueTransient(semanticObject, ECAPackage.Literals.WINDOW__WINDOW_VALUE) == ValueTransient.YES)
				errorAcceptor.accept(diagnosticProvider.createFeatureValueMissing(semanticObject, ECAPackage.Literals.WINDOW__WINDOW_VALUE));
		}
		SequenceFeeder feeder = createSequencerFeeder(context, semanticObject);
		feeder.accept(grammarAccess.getWindowAccess().getWindowValueINTTerminalRuleCall_3_0(), semanticObject.getWindowValue());
		feeder.finish();
	}
	
	
}
