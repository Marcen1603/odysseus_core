package de.uniol.inf.is.odysseus.iql.odl.serializer;

import com.google.inject.Inject;
import de.uniol.inf.is.odysseus.iql.odl.services.ODLGrammarAccess;
import java.util.List;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.xtext.IGrammarAccess;
import org.eclipse.xtext.RuleCall;
import org.eclipse.xtext.nodemodel.INode;
import org.eclipse.xtext.serializer.analysis.GrammarAlias.AbstractElementAlias;
import org.eclipse.xtext.serializer.analysis.GrammarAlias.GroupAlias;
import org.eclipse.xtext.serializer.analysis.GrammarAlias.TokenAlias;
import org.eclipse.xtext.serializer.analysis.ISyntacticSequencerPDAProvider.ISynNavigable;
import org.eclipse.xtext.serializer.analysis.ISyntacticSequencerPDAProvider.ISynTransition;
import org.eclipse.xtext.serializer.sequencer.AbstractSyntacticSequencer;

@SuppressWarnings("all")
public class ODLSyntacticSequencer extends AbstractSyntacticSequencer {

	protected ODLGrammarAccess grammarAccess;
	protected AbstractElementAlias match_IQLMethod___LeftParenthesisKeyword_3_0_RightParenthesisKeyword_3_2__q;
	protected AbstractElementAlias match_IQLSwitchStatement___DefaultKeyword_7_0_ColonKeyword_7_1__q;
	protected AbstractElementAlias match_ODLMethod___LeftParenthesisKeyword_2_1_0_RightParenthesisKeyword_2_1_2__q;
	protected AbstractElementAlias match_ODLOperator___LeftParenthesisKeyword_3_0_RightParenthesisKeyword_3_2__q;
	protected AbstractElementAlias match_ODLParameter___LeftParenthesisKeyword_3_0_RightParenthesisKeyword_3_2__q;
	
	@Inject
	protected void init(IGrammarAccess access) {
		grammarAccess = (ODLGrammarAccess) access;
		match_IQLMethod___LeftParenthesisKeyword_3_0_RightParenthesisKeyword_3_2__q = new GroupAlias(false, true, new TokenAlias(false, false, grammarAccess.getIQLMethodAccess().getLeftParenthesisKeyword_3_0()), new TokenAlias(false, false, grammarAccess.getIQLMethodAccess().getRightParenthesisKeyword_3_2()));
		match_IQLSwitchStatement___DefaultKeyword_7_0_ColonKeyword_7_1__q = new GroupAlias(false, true, new TokenAlias(false, false, grammarAccess.getIQLSwitchStatementAccess().getDefaultKeyword_7_0()), new TokenAlias(false, false, grammarAccess.getIQLSwitchStatementAccess().getColonKeyword_7_1()));
		match_ODLMethod___LeftParenthesisKeyword_2_1_0_RightParenthesisKeyword_2_1_2__q = new GroupAlias(false, true, new TokenAlias(false, false, grammarAccess.getODLMethodAccess().getLeftParenthesisKeyword_2_1_0()), new TokenAlias(false, false, grammarAccess.getODLMethodAccess().getRightParenthesisKeyword_2_1_2()));
		match_ODLOperator___LeftParenthesisKeyword_3_0_RightParenthesisKeyword_3_2__q = new GroupAlias(false, true, new TokenAlias(false, false, grammarAccess.getODLOperatorAccess().getLeftParenthesisKeyword_3_0()), new TokenAlias(false, false, grammarAccess.getODLOperatorAccess().getRightParenthesisKeyword_3_2()));
		match_ODLParameter___LeftParenthesisKeyword_3_0_RightParenthesisKeyword_3_2__q = new GroupAlias(false, true, new TokenAlias(false, false, grammarAccess.getODLParameterAccess().getLeftParenthesisKeyword_3_0()), new TokenAlias(false, false, grammarAccess.getODLParameterAccess().getRightParenthesisKeyword_3_2()));
	}
	
	@Override
	protected String getUnassignedRuleCallToken(EObject semanticObject, RuleCall ruleCall, INode node) {
		return "";
	}
	
	
	@Override
	protected void emitUnassignedTokens(EObject semanticObject, ISynTransition transition, INode fromNode, INode toNode) {
		if (transition.getAmbiguousSyntaxes().isEmpty()) return;
		List<INode> transitionNodes = collectNodes(fromNode, toNode);
		for (AbstractElementAlias syntax : transition.getAmbiguousSyntaxes()) {
			List<INode> syntaxNodes = getNodesFor(transitionNodes, syntax);
			if(match_IQLMethod___LeftParenthesisKeyword_3_0_RightParenthesisKeyword_3_2__q.equals(syntax))
				emit_IQLMethod___LeftParenthesisKeyword_3_0_RightParenthesisKeyword_3_2__q(semanticObject, getLastNavigableState(), syntaxNodes);
			else if(match_IQLSwitchStatement___DefaultKeyword_7_0_ColonKeyword_7_1__q.equals(syntax))
				emit_IQLSwitchStatement___DefaultKeyword_7_0_ColonKeyword_7_1__q(semanticObject, getLastNavigableState(), syntaxNodes);
			else if(match_ODLMethod___LeftParenthesisKeyword_2_1_0_RightParenthesisKeyword_2_1_2__q.equals(syntax))
				emit_ODLMethod___LeftParenthesisKeyword_2_1_0_RightParenthesisKeyword_2_1_2__q(semanticObject, getLastNavigableState(), syntaxNodes);
			else if(match_ODLOperator___LeftParenthesisKeyword_3_0_RightParenthesisKeyword_3_2__q.equals(syntax))
				emit_ODLOperator___LeftParenthesisKeyword_3_0_RightParenthesisKeyword_3_2__q(semanticObject, getLastNavigableState(), syntaxNodes);
			else if(match_ODLParameter___LeftParenthesisKeyword_3_0_RightParenthesisKeyword_3_2__q.equals(syntax))
				emit_ODLParameter___LeftParenthesisKeyword_3_0_RightParenthesisKeyword_3_2__q(semanticObject, getLastNavigableState(), syntaxNodes);
			else acceptNodes(getLastNavigableState(), syntaxNodes);
		}
	}

	/**
	 * Syntax:
	 *     ('(' ')')?
	 */
	protected void emit_IQLMethod___LeftParenthesisKeyword_3_0_RightParenthesisKeyword_3_2__q(EObject semanticObject, ISynNavigable transition, List<INode> nodes) {
		acceptNodes(transition, nodes);
	}
	
	/**
	 * Syntax:
	 *     ('default' ':')?
	 */
	protected void emit_IQLSwitchStatement___DefaultKeyword_7_0_ColonKeyword_7_1__q(EObject semanticObject, ISynNavigable transition, List<INode> nodes) {
		acceptNodes(transition, nodes);
	}
	
	/**
	 * Syntax:
	 *     ('(' ')')?
	 */
	protected void emit_ODLMethod___LeftParenthesisKeyword_2_1_0_RightParenthesisKeyword_2_1_2__q(EObject semanticObject, ISynNavigable transition, List<INode> nodes) {
		acceptNodes(transition, nodes);
	}
	
	/**
	 * Syntax:
	 *     ('(' ')')?
	 */
	protected void emit_ODLOperator___LeftParenthesisKeyword_3_0_RightParenthesisKeyword_3_2__q(EObject semanticObject, ISynNavigable transition, List<INode> nodes) {
		acceptNodes(transition, nodes);
	}
	
	/**
	 * Syntax:
	 *     ('(' ')')?
	 */
	protected void emit_ODLParameter___LeftParenthesisKeyword_3_0_RightParenthesisKeyword_3_2__q(EObject semanticObject, ISynNavigable transition, List<INode> nodes) {
		acceptNodes(transition, nodes);
	}
	
}