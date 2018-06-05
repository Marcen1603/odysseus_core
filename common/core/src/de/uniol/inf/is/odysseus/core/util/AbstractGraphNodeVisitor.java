package de.uniol.inf.is.odysseus.core.util;

public class AbstractGraphNodeVisitor<NodeType, ResultType> implements
		IGraphNodeVisitor<NodeType, ResultType> {

	@Override
	public void nodeAction(NodeType node) {		
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
