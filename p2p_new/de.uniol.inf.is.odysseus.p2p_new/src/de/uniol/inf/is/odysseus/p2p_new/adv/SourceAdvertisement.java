package de.uniol.inf.is.odysseus.p2p_new.adv;

import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;

import net.jxta.document.Advertisement;
import net.jxta.document.Document;
import net.jxta.document.Element;
import net.jxta.document.MimeMediaType;
import net.jxta.document.StructuredDocumentFactory;
import net.jxta.document.StructuredTextDocument;
import net.jxta.document.TextElement;
import net.jxta.id.ID;

import com.google.common.base.Preconditions;

import de.uniol.inf.is.odysseus.core.server.logicaloperator.AccessAO;

public final class SourceAdvertisement extends Advertisement implements Serializable {

	private static final String ADVERTISEMENT_TYPE = "jxta:SourceAdvertisement";
	private static final long serialVersionUID = 1L;
	
	private ID id;
	private AccessAO accessOperator;

	public SourceAdvertisement(Element<?> root) {
		TextElement<?> doc = (TextElement<?>) Preconditions.checkNotNull(root, "Root element must not be null!");
		
		accessOperator = AccessAOCoverter.toAccessAO(doc, this);
	}

	public SourceAdvertisement(InputStream stream) throws IOException {
		this((StructuredTextDocument<?>) StructuredDocumentFactory.newStructuredDocument(MimeMediaType.XMLUTF8, Preconditions.checkNotNull(stream, "Stream must not be null!")));
	}

	public SourceAdvertisement(SourceAdvertisement sourceAdvertisement) {
		Preconditions.checkNotNull(sourceAdvertisement, "Advertisement to copy must not be null!");

		accessOperator = sourceAdvertisement.accessOperator;
		id = sourceAdvertisement.id;
	}

	SourceAdvertisement() {
		// for JXTA-side instances
	}

	public static String getAdvertisementType() {
		return ADVERTISEMENT_TYPE;
	}

	public AccessAO getAccessAO() {
		return accessOperator;
	}

	public void setAccessAO(AccessAO accessOperator) {
		this.accessOperator = accessOperator;
	}

	public void setID(ID id) {
		this.id = id;
	}

	@Override
	public String[] getIndexFields() {
		return AccessAOCoverter.getIndexableFieldTags();
	}

	@Override
	public ID getID() {
		return id;
	}

	@Override
	public Document getDocument(MimeMediaType asMimeType) {
		return AccessAOCoverter.toDocument(asMimeType, getID(), accessOperator);
	}

	@Override
	public SourceAdvertisement clone() {
		return new SourceAdvertisement(this);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((accessOperator.getSourcename() == null) ? 0 : accessOperator.getSourcename().hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (!(obj instanceof SourceAdvertisement)) {
			return false;
		}

		SourceAdvertisement other = (SourceAdvertisement) obj;
		if (accessOperator.getSourcename() == null) {
			if (other.accessOperator.getSourcename() != null) {
				return false;
			}
		} else if (!accessOperator.getSourcename().equals(other.accessOperator.getSourcename())) {
			return false;
		}
		return true;
	}
}
