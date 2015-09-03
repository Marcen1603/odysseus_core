package de.uniol.inf.is.odysseus.core.util;

public class AbstractExtendedGraphNodeVisitor<NodeType, ResultType> implements
		IExtendedGraphNodeVisitor<NodeType, ResultType> {

	@Override
	public NodeActionResult nodeAction(NodeType node) {
		return NodeActionResult.MARK_AND_CONTINUE;
	}

	@Override
	public void beforeFromSinkToSourceAction(NodeType sink, NodeType source) {
	}

	@Override
	public void afterFromSinkToSourceAction(NodeType sink, NodeType source) {
	}

	@Override
	public void beforeFromSourceToSinkAction(NodeType source, NodeType sink) {
	}

	@Override
	public void afterFromSourceToSinkAction(NodeType source, NodeType sink) {
	}

	@Override
	public ResultType getResult() {
		return null;
	}

}
