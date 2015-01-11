package de.uniol.inf.is.odysseus.p2p_new.dictionary;

import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.util.Enumeration;

import net.jxta.document.Advertisement;
import net.jxta.document.Document;
import net.jxta.document.Element;
import net.jxta.document.MimeMediaType;
import net.jxta.document.StructuredDocumentFactory;
import net.jxta.document.TextElement;
import net.jxta.id.ID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Preconditions;

public class DataSourceAdvertisement extends Advertisement implements Serializable {

	private static final long serialVersionUID = 1L;
	private static final String ADVERTISEMENT_TYPE = "jxta:DataSourceAdvertisement";
	private static final Logger LOG = LoggerFactory.getLogger(DataSourceAdvertisement.class);

	private static final String ID_TAG = "id";
	private static final String NAME_TAG = "name";
	private static final String PQL_TAG = "pql";
	
	private static final String[] INDEX_FIELDS = new String[] { ID_TAG, NAME_TAG };

	private ID id;
	private String name;
	private String pql;
	
	public DataSourceAdvertisement(Element<?> root) {
		final TextElement<?> doc = (TextElement<?>) Preconditions.checkNotNull(root, "Root element must not be null!");

		final Enumeration<?> elements = doc.getChildren();
		while (elements.hasMoreElements()) {
			final TextElement<?> elem = (TextElement<?>) elements.nextElement();
			handleElement(elem);
		}
	}

	public DataSourceAdvertisement(InputStream stream) throws IOException {
		this(StructuredDocumentFactory.newStructuredDocument(MimeMediaType.XMLUTF8, Preconditions.checkNotNull(stream, "Stream must not be null!")));
	}

	public DataSourceAdvertisement(DataSourceAdvertisement adv) {
		Preconditions.checkNotNull(adv, "Advertisement to copy must not be null!");

		id = adv.id;
		name = adv.name;
		pql = adv.pql;
	}

	public DataSourceAdvertisement() {
		// for JXTA-side instances
	}

	@Override
	public DataSourceAdvertisement clone() throws CloneNotSupportedException {
		return new DataSourceAdvertisement(this);
	}

	@Override
	public Document getDocument(MimeMediaType asMimeType) {
		return null;
	}
	
	private void handleElement(TextElement<?> elem) {
		
	}

	@Override
	public ID getID() {
		return id;
	}
	
	public void setID(ID id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}
	
	public void setName( String name ) {
		this.name = name;
	}
	
	public String getPQLText() {
		return pql;
	}
	
	public void setPQLText( String text ) {
		this.pql = text;
	}
	
	@Override
	public String[] getIndexFields() {
		return INDEX_FIELDS;
	}

}
