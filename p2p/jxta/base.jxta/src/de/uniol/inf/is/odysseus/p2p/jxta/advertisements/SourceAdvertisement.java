/********************************************************************************** 
  * Copyright 2011 The Odysseus Team
  *
  * Licensed under the Apache License, Version 2.0 (the "License");
  * you may not use this file except in compliance with the License.
  * You may obtain a copy of the License at
  *
  *     http://www.apache.org/licenses/LICENSE-2.0
  *
  * Unless required by applicable law or agreed to in writing, software
  * distributed under the License is distributed on an "AS IS" BASIS,
  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
  * See the License for the specific language governing permissions and
  * limitations under the License.
  */
package de.uniol.inf.is.odysseus.p2p.jxta.advertisements;

import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Enumeration;

import de.uniol.inf.is.odysseus.p2p.ISourceAdvertisement;

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

/**
 * Beschreibt eine Quelle im P2P-Netzwerk
 * 
 * @author christian, Marco Grawunder
 * 
 */
@SuppressWarnings({ "unchecked", "rawtypes" })
public class SourceAdvertisement extends Advertisement implements Serializable, ISourceAdvertisement {

	private static final long serialVersionUID = 1L;

	private String sourceName;
	private String user;
	private String logicalPlan;
	
	private ID id = ID.nullID;
	private String peer;
	private String peerID="";
	private String sourceId;
	private boolean isView;
	private String sourceType;
	private String entity;

	private final static String sourceNameTag = "sourceName";
	private final static String idTag = "id";
	private final static String peerTag = "peer";
	private final static String peerIDTag = "peerID";
	private final static String sourceIdTag = "sourceId";
	private final static String userTag = "user";
	private final static String logicalPlanTag = "logicalplan";
	private final static String isViewTag = "isView";
	private final static String sourceTypeTag = "sourceType";
	private final static String entityTag = "entity";
	

	/**
	 * Indexable fields. Advertisements must define the indexables, in order to
	 * properly index and retrieve these advertisements locally and on the
	 * network
	 */
	private final static String[] fields = { idTag, sourceIdTag, sourceNameTag,
			userTag, logicalPlanTag, peerTag ,peerIDTag, isViewTag, sourceTypeTag, entityTag};

	public SourceAdvertisement(Element root) {
		TextElement doc = (TextElement) root;

		if (!getAdvertisementType().equals(doc.getName())) {
			throw new IllegalArgumentException("Could not construct : "
					+ getClass().getName() + "from doc containing a "
					+ doc.getName());
		}
		initialize(doc);
	}

	public SourceAdvertisement(InputStream stream) throws IOException {
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
		if (elem.getName().equals(sourceNameTag)) {
			setSourceName(elem.getTextValue());
			return true;
		}
		if (elem.getName().equals(peerIDTag)) {
			setPeerID(elem.getTextValue());
			return true;
		}		
		if (elem.getName().equals(peerTag)) {
			setPeer(elem.getTextValue());
			return true;
		}
		if (elem.getName().equals(sourceIdTag)) {
			setSourceId(elem.getTextValue());
			return true;
		}
		if (elem.getName().equals(userTag)) {
			setUser(elem.getTextValue());
			return true;
		}
		if (elem.getName().equals(logicalPlanTag)) {
			setLogicalPlan(elem.getTextValue());
			return true;
		}
		if (elem.getName().equals(isViewTag)) {
			setIsView(elem.getTextValue());
			return true;
		}		
		if (elem.getName().equals(sourceTypeTag)) {
			setSourceType(elem.getTextValue());
			return true;
		}
		if (elem.getName().equals(entityTag)) {
			setEntity(elem.getTextValue());
			return true;
		}

		return false;
	}

	public SourceAdvertisement() {
	}

	public SourceAdvertisement(SourceAdvertisement sourceAdvertisement) {
		sourceName = sourceAdvertisement.sourceName;
		user = sourceAdvertisement.user;
		logicalPlan = sourceAdvertisement.logicalPlan;
		peer = sourceAdvertisement.peer;
		peerID = sourceAdvertisement.peerID;
		sourceId = sourceAdvertisement.sourceId;
		isView = sourceAdvertisement.isView;
		sourceType = sourceAdvertisement.sourceType;
		entity = sourceAdvertisement.entity;
		id = sourceAdvertisement.id;
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
		e = adv.createElement(sourceNameTag, getSourceName().trim());
		adv.appendChild(e);
		e = adv.createElement(peerTag, getPeer().toString().trim());
		adv.appendChild(e);
		e = adv.createElement(peerIDTag, getPeerID().toString().trim());
		adv.appendChild(e);
		e = adv.createElement(sourceIdTag, getSourceId().toString().trim());
		adv.appendChild(e);
		e = adv.createElement(userTag, getUser().trim());
		adv.appendChild(e);
		e = adv.createElement(logicalPlanTag, getLogicalPlan().trim());
		adv.appendChild(e);
		e = adv.createElement(isViewTag, ""+isView);
		adv.appendChild(e);		
		e = adv.createElement(sourceTypeTag, sourceType);
		adv.appendChild(e);		
		e = adv.createElement(entityTag, entity);
		adv.appendChild(e);				
		return adv;
	}

	public static String getAdvertisementType() {
		return "jxta:SourceAdvertisement";
	}

	@Override
	public ID getID() {
		return (id == null ? null : id);
	}

	@Override
	public String[] getIndexFields() {
		return fields;
	}


	public void setID(ID id) {
		this.id = (id == null ? null : id);
	}

	@Override
	public String getSourceName() {
		return sourceName;
	}

	public void setSourceName(String sourceName) {
		this.sourceName = sourceName;
	}

	public void setUser(String user) {
		this.user = user;
	}

	public String getUser() {
		return user;
	}

	public String getLogicalPlan() {
		return logicalPlan;
	}

	public void setLogicalPlan(String logicalPlan) {
		this.logicalPlan = logicalPlan;
	}
	
	public String getEntity() {
		return entity;
	}
	
	public void setEntity(String entity) {
		this.entity = entity;
	}

	public String getSourceId() {
		return sourceId;
	}

	public void setSourceId(String sourceId) {
		this.sourceId = sourceId;
	}

	public String getPeer() {
		return peer;
	}

	public void setPeer(String peer) {
		this.peer = peer;
	}
	
	@Override
	public String getPeerID() {
		return peerID;
	}
	
	public void setPeerID(String peerID) {
		this.peerID = peerID;
	}
	
	public boolean isView(){
		return isView;
	}
	
	public void setIsView(String isView){
		setIsView(Boolean.parseBoolean(isView));
	}

	private void setIsView(boolean isView) {
		this.isView = isView;
	}

	public String getSourceType() {
		return sourceType;
	}
	
	public void setSourceType(String sourceType){
		this.sourceType = sourceType;
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
		@Override
		public String getAdvertisementType() {
			return SourceAdvertisement.getAdvertisementType();
		}

		/**
		 * Constructs an instance of <CODE>Advertisement</CODE> matching the
		 * type specified by the <CODE>advertisementType</CODE> parameter.
		 * 
		 * @return The instance of <CODE>Advertisement</CODE> or null if it
		 *         could not be created.
		 */
		@Override
		public Advertisement newInstance() {
			return new SourceAdvertisement();
		}

		@Override
		public Advertisement newInstance(Element root) {
			return new SourceAdvertisement(root);
		}
	}

	@Override
	public SourceAdvertisement clone() {
		return new SourceAdvertisement(this);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((peerID == null) ? 0 : peerID.hashCode());
		result = prime * result
				+ ((sourceName == null) ? 0 : sourceName.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		SourceAdvertisement other = (SourceAdvertisement) obj;
		if (peerID == null) {
			if (other.peerID != null)
				return false;
		} else if (!peerID.equals(other.peerID))
			return false;
		if (sourceName == null) {
			if (other.sourceName != null)
				return false;
		} else if (!sourceName.equals(other.sourceName))
			return false;
		return true;
	}

}
