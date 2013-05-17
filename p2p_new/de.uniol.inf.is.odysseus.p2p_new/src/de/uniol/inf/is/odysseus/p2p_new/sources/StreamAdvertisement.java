package de.uniol.inf.is.odysseus.p2p_new.sources;

import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.util.Objects;

import net.jxta.document.Advertisement;
import net.jxta.document.Document;
import net.jxta.document.Element;
import net.jxta.document.MimeMediaType;
import net.jxta.document.StructuredDocumentFactory;
import net.jxta.document.TextElement;
import net.jxta.id.ID;

import com.google.common.base.Preconditions;

import de.uniol.inf.is.odysseus.core.server.logicaloperator.AccessAO;

public final class StreamAdvertisement extends Advertisement implements Serializable {

	private static final String ADVERTISEMENT_TYPE = "jxta:StreamAdvertisement";
	private static final long serialVersionUID = 1L;

	private ID id;

	private AccessAO accessOperator;

	public StreamAdvertisement(Element<?> root) {
		final TextElement<?> doc = (TextElement<?>) Preconditions.checkNotNull(root, "Root element must not be null!");

		accessOperator = AccessAOCoverter.toAccessAO(doc, this);
	}

	public StreamAdvertisement(InputStream stream) throws IOException {
		this(StructuredDocumentFactory.newStructuredDocument(MimeMediaType.XMLUTF8, Preconditions.checkNotNull(stream, "Stream must not be null!")));
	}

	public StreamAdvertisement(StreamAdvertisement sourceAdvertisement) {
		Preconditions.checkNotNull(sourceAdvertisement, "Advertisement to copy must not be null!");

		accessOperator = sourceAdvertisement.accessOperator;
		id = sourceAdvertisement.id;
	}

	StreamAdvertisement() {
		// for JXTA-side instances
	}

	@Override
	public StreamAdvertisement clone() {
		return new StreamAdvertisement(this);
	}
	
	@Override
	public boolean equals(Object obj) {
		if (obj == this) {
			return true;
		}
		if (!(obj instanceof StreamAdvertisement)) {
			return false;
		}
		
		StreamAdvertisement adv = (StreamAdvertisement) obj;
		
		if( adv.accessOperator == null ) {
			if( accessOperator != null ) {
				return false;
			}
		} else if( accessOperator == null ) {
			return false;
		}
		
		return Objects.equals(adv.accessOperator.getSourcename(), accessOperator.getSourcename()) && 
				Objects.equals(adv.id, id);
	}
	
	@Override
	public int hashCode() {
		return Objects.hash(accessOperator.getSourcename(), id);
	}

	public AccessAO getAccessAO() {
		return accessOperator;
	}

	@Override
	public Document getDocument(MimeMediaType asMimeType) {
		return AccessAOCoverter.toDocument(asMimeType, getID(), accessOperator);
	}

	@Override
	public ID getID() {
		return id;
	}

	@Override
	public String[] getIndexFields() {
		return AccessAOCoverter.getIndexableFieldTags();
	}

	public void setAccessAO(AccessAO accessOperator) {
		this.accessOperator = accessOperator;
	}

	public void setID(ID id) {
		this.id = id;
	}

	public static String getAdvertisementType() {
		return ADVERTISEMENT_TYPE;
	}
}
