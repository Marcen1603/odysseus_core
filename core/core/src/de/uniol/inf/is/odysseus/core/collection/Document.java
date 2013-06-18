package de.uniol.inf.is.odysseus.core.collection;

import java.io.Serializable;

import de.uniol.inf.is.odysseus.core.metadata.AbstractStreamObject;
import de.uniol.inf.is.odysseus.core.metadata.IMetaAttribute;

public class Document<T extends IMetaAttribute> extends AbstractStreamObject<T>
		implements Serializable {

	final String content;
	
	private static final long serialVersionUID = -3819422555208481383L;

	public Document(String content){
		this.content = content;
	}
	
	public String getContent() {
		return content;
	}
	
	@Override
	public AbstractStreamObject<T> clone() {
		// Document is immutable
		return this;
	}

}
