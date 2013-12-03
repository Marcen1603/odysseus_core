package de.uniol.inf.is.odysseus.p2p_new.distribute.centralized.advertisements.operatorhelpers;

import java.util.Map.Entry;

import net.jxta.document.Element;
import net.jxta.document.MimeMediaType;
import net.jxta.document.StructuredDocument;
import net.jxta.document.TextElement;
import de.uniol.inf.is.odysseus.core.physicaloperator.IPhysicalOperator;

public interface IPhysicalOperatorHelper<T extends IPhysicalOperator> {
	public Class<T> getOperatorClass();
	@SuppressWarnings("rawtypes")
	public StructuredDocument<?> generateStatement(IPhysicalOperator o, MimeMediaType mimeType, boolean startJxtaConnections, StructuredDocument rootDoc, Element toAppendTo);
	public Entry<Integer,T> createOperatorFromStatement(TextElement<?> doc, boolean startJxtaConnections);
}
