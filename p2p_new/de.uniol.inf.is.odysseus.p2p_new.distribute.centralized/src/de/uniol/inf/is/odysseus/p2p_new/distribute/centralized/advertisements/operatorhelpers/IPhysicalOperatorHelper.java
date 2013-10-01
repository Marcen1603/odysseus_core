package de.uniol.inf.is.odysseus.p2p_new.distribute.centralized.advertisements.operatorhelpers;

import java.util.Map.Entry;

import net.jxta.document.MimeMediaType;
import net.jxta.document.StructuredDocument;
import net.jxta.document.TextElement;
import de.uniol.inf.is.odysseus.core.physicaloperator.IPhysicalOperator;

public interface IPhysicalOperatorHelper<T extends IPhysicalOperator> {
	public Class<T> getOperatorClass();
	public StructuredDocument<?> generateStatement(IPhysicalOperator o, MimeMediaType mimeType, boolean startJxtaConnections);
	public Entry<Integer,T> createOperatorFromStatement(StructuredDocument<? extends TextElement<?>> doc, boolean startJxtaConnections);
}
