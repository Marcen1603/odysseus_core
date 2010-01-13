package de.uniol.inf.is.odysseus.p2p.jxta.advertisements;

import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Enumeration;

import net.jxta.document.Advertisement;
import net.jxta.document.AdvertisementFactory;
import net.jxta.document.Attributable;
import net.jxta.document.Document;
import net.jxta.document.Element;
import net.jxta.document.MimeMediaType;
import net.jxta.document.StructuredDocument;
import net.jxta.document.StructuredDocumentFactory;
import net.jxta.document.StructuredTextDocument;
import net.jxta.document.TextElement;
import net.jxta.id.ID;
import net.jxta.id.IDFactory;
@SuppressWarnings("unchecked")
public class QueryTranslationSpezification extends Advertisement implements
		Serializable, Cloneable, Comparable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String query;

	private ID id = ID.nullID;
	
	private String peer;
	
	private String queryId;
	
	private String language;
	
	public String getLanguage() {
		return language;
	}

	public void setLanguage(String language) {
		this.language = language;
	}

	public String getQueryId() {
		return queryId;
	}

	public void setQueryId(String queryId) {
		this.queryId = queryId;
	}

	private String biddingPipe;


	public String getPeer() {
		return peer;
	}

	public void setPeer(String peer) {
		this.peer = peer;
	}

	public String getBiddingPipe() {
		return biddingPipe;
	}

	public void setBiddingPipe(String biddingPipe) {
		this.biddingPipe = biddingPipe;
	}

	private final static String queryTag = "query";
	private final static String idTag = "id";
	private final static String biddingPipeTag = "biddingPipe";
	private final static String peerTag = "peer";
	private final static String queryIdTag = "queryId";
	private final static String languageTag = "language";

	/**
	 * Indexable fields. Advertisements must define the indexables, in order to
	 * properly index and retrieve these advertisements locally and on the
	 * network
	 */
	private final static String[] fields = { idTag, queryIdTag, languageTag };

	public QueryTranslationSpezification(Element root) {
		TextElement doc = (TextElement) root;

		if (!getAdvertisementType().equals(doc.getName())) {
			throw new IllegalArgumentException("Could not construct : "
					+ getClass().getName() + "from doc containing a "
					+ doc.getName());
		}
		initialize(doc);
	}

	public QueryTranslationSpezification(InputStream stream) throws IOException {
		StructuredTextDocument doc = (StructuredTextDocument) StructuredDocumentFactory
				.newStructuredDocument(MimeMediaType.XMLUTF8, stream);
		initialize(doc);
	}

	protected void initialize(Element root) {
		if (!TextElement.class.isInstance(root)) {
			throw new IllegalArgumentException(getClass().getName()
					+ " only supports TextElement");
		}
		TextElement doc = (TextElement) root;

		if (!doc.getName().equals(getAdvertisementType())) {
			throw new IllegalArgumentException("Could not construct : "
					+ getClass().getName() + "from doc containing a "
					+ doc.getName());
		}
		Enumeration elements = doc.getChildren();

		while (elements.hasMoreElements()) {
			TextElement elem = (TextElement) elements.nextElement();

			if (!handleElement(elem)) {
				// LOG.warning("Unhandleded element \'" + elem.getName() +
				// "\' in " + doc.getName());
			}
		}
	}

	protected boolean handleElement(TextElement elem) {
		if (elem.getName().equals(idTag)) {
			try {
				URI id = new URI(elem.getTextValue());

				setID(IDFactory.fromURI(id));
			} catch (URISyntaxException badID) {
				throw new IllegalArgumentException(
						"unknown ID format in advertisement: "
								+ elem.getTextValue());
			} catch (ClassCastException badID) {
				throw new IllegalArgumentException(
						"Id is not a known id type: " + elem.getTextValue());
			}
			return true;
		}
		if (elem.getName().equals(queryTag)) {
			setQuery(elem.getTextValue());
			return true;
		}
		if (elem.getName().equals(biddingPipeTag)) {
			setBiddingPipe(elem.getTextValue());
			return true;
		}
		if (elem.getName().equals(peerTag)) {
			setPeer(elem.getTextValue());
			return true;
		}
		if (elem.getName().equals(queryIdTag)) {
			setQueryId(elem.getTextValue());
			return true;
		}
		if (elem.getName().equals(languageTag)) {
			setLanguage(elem.getTextValue());
			return true;
		}

		return false;
	}

	public QueryTranslationSpezification() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public Document getDocument(MimeMediaType asMimeType) {
		StructuredDocument adv = StructuredDocumentFactory
				.newStructuredDocument(asMimeType, getAdvertisementType());
		if (adv instanceof Attributable) {
			((Attributable) adv).addAttribute("xmlns:jxta", "http://jxta.org");
		}
		Element e;
		e = adv.createElement(idTag, getID().toString());
		adv.appendChild(e);
		e = adv.createElement(queryTag, getQuery().trim());		
		adv.appendChild(e);
		e = adv.createElement(biddingPipeTag, getBiddingPipe().toString().trim());
		adv.appendChild(e);
		e = adv.createElement(peerTag, getPeer().toString().trim());
		adv.appendChild(e);
		e = adv.createElement(queryIdTag, getQueryId().toString().trim());
		adv.appendChild(e);
		e = adv.createElement(languageTag, getLanguage().toString().trim());
		adv.appendChild(e);
		
		return adv;
	}

	public static String getAdvertisementType() {
		return "jxta:QueryTranslationSpezification";
	}

	@Override
	public ID getID() {
		return (id == null ? null : id);
	}

	@Override
	public String[] getIndexFields() {
		return fields;
	}

	@Override
	public int compareTo(Object other) {
		return getID().toString().compareTo(other.toString());
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj instanceof QueryTranslationSpezification) {
			QueryTranslationSpezification adv = (QueryTranslationSpezification) obj;
			return getID().equals(adv.getID());
		}
		return false;
	}

	public void setID(ID id) {
		this.id = (id == null ? null : id);
	}

	public String getQuery() {
		return query;
	}

	public void setQuery(String query) {
		this.query = query;
	}

	/**
	 * Instantiator
	 */
	public static class Instantiator implements
			AdvertisementFactory.Instantiator {
		/**
		 * Returns the identifying type of this Advertisement.
		 * 
		 * @return String the type of advertisement
		 */
		public String getAdvertisementType() {
			return QueryTranslationSpezification.getAdvertisementType();
		}

		/**
		 * Constructs an instance of <CODE>Advertisement</CODE> matching the
		 * type specified by the <CODE>advertisementType</CODE> parameter.
		 * 
		 * @return The instance of <CODE>Advertisement</CODE> or null if it
		 *         could not be created.
		 */
		public Advertisement newInstance() {
			return new QueryTranslationSpezification();
		}

		@Override
		public Advertisement newInstance(Element root) {
			return new QueryTranslationSpezification(root);
		}
	}
	
	public static void main(String[] args) {
		AdvertisementFactory.registerAdvertisementInstance(QueryTranslationSpezification.getAdvertisementType()
                ,
                new QueryTranslationSpezification.Instantiator());

		QueryTranslationSpezification advTutorial = (QueryTranslationSpezification) AdvertisementFactory.newAdvertisement(QueryTranslationSpezification.getAdvertisementType());

       
		IDFactory.newPeerGroupID();
		//advTutorial.setID(ID.nullID);
        advTutorial.setQuery("Select * From nexmark:person");
	}

}
