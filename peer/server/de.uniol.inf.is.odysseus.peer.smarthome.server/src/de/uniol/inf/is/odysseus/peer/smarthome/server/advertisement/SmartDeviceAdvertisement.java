package de.uniol.inf.is.odysseus.peer.smarthome.server.advertisement;

import java.io.Serializable;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Enumeration;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Preconditions;

import net.jxta.document.Advertisement;
import net.jxta.document.Attributable;
import net.jxta.document.Document;
import net.jxta.document.Element;
import net.jxta.document.MimeMediaType;
import net.jxta.document.StructuredDocument;
import net.jxta.document.StructuredDocumentFactory;
import net.jxta.document.TextElement;
import net.jxta.id.ID;
import net.jxta.id.IDFactory;
import net.jxta.peer.PeerID;
import net.jxta.pipe.PipeID;

public class SmartDeviceAdvertisement extends Advertisement implements
		Serializable {

	private static final long serialVersionUID = 1253246425406629535L;
	private static final String ADVERTISEMENT_TYPE = "jxta:SmartDeviceAdvertisement";

	private static final Logger LOG = LoggerFactory
			.getLogger(SmartDeviceAdvertisement.class);

	private static final String ID_TAG = "id";
	private static final String NAME_TAG = "name";
	private static final String PEER_ID_TAG = "originalPeerID";
	private static final String PIPE_ID_TAG = "pipeID";
	// private static final String CONTEXT_NAME = "contextName";
	// private static final String SMART_DEVICE = "smartDevice";

	private static final String[] INDEX_FIELDS = new String[] { ID_TAG,
			NAME_TAG }; // PEER_ID_TAG,

	private ID id;
	private String name = "";
	private PipeID pipeID;
	private PeerID peerID;
	private String peerName = "";
	// private String contextName = "";
	//private SmartDevice smartDevice;

	public SmartDeviceAdvertisement(Element<?> root) {
		final TextElement<?> doc = (TextElement<?>) Preconditions.checkNotNull(
				root, "Root element must not be null!");

		final Enumeration<?> elements = doc.getChildren();
		while (elements.hasMoreElements()) {
			final TextElement<?> elem = (TextElement<?>) elements.nextElement();
			handleElement(elem);
		}
	}

	private void handleElement(TextElement<?> elem) {
		if (elem.getName().equals(ID_TAG)) {
			setID(toID(elem));
		} else if (elem.getName().equals(PEER_ID_TAG)) {
			setPeerID((PeerID) toID(elem));
		} else if (elem.getName().equals(PIPE_ID_TAG)) {
			setPipeID((PipeID) toID(elem));
		}

		/*
		 * } else if (elem.getName().equals(CONTEXT_NAME)) {
		 * setContextName(elem.getTextValue());
		 */

		/*
		 * else if (elem.getName().equals(SMART_DEVICE)) {
		 * setSmartDevice(elem.getTextValue()); }
		 */
	}
/*
	private void setSmartDevice(String smartDeviceString) {
		try {
			SmartDevice smartDevice = (SmartDevice) fromString(smartDeviceString);
			this.smartDevice = smartDevice;
		} catch (ClassNotFoundException e) {
			LOG.error(e.getMessage(), e);
		} catch (IOException e) {
			LOG.error(e.getMessage(), e);
		}
	}
	*/

	public SmartDeviceAdvertisement() {
		// for JXTA-side instances
	}

	public SmartDeviceAdvertisement(SmartDeviceAdvertisement viewAdvertisement) {
		Preconditions.checkNotNull(viewAdvertisement,
				"Advertisement to copy must not be null!");

		id = viewAdvertisement.id;
		peerID = viewAdvertisement.peerID;
		pipeID = viewAdvertisement.pipeID;
		//smartDevice = viewAdvertisement.smartDevice;
	}

	@Override
	public SmartDeviceAdvertisement clone() throws CloneNotSupportedException {
		return new SmartDeviceAdvertisement(this);
	}

	@Override
	public Document getDocument(MimeMediaType asMimeType) {
		final StructuredDocument<?> doc = StructuredDocumentFactory
				.newStructuredDocument(asMimeType, getAdvertisementType());
		if (doc instanceof Attributable) {
			((Attributable) doc).addAttribute("xmlns:jxta", "http://jxta.org");
		}

		appendTo(doc);

		return doc;
	}

	void appendTo(Element<?> doc) {
		appendElement(doc, ID_TAG, this.id.toString());
		appendElement(doc, NAME_TAG, this.name);
		appendElement(doc, PEER_ID_TAG, this.peerID.toString());
		//appendElement(doc, PIPE_ID_TAG, pipeID.toString());

		/*
		 * if (this.contextName != null) { appendElement(doc, CONTEXT_NAME,
		 * this.contextName); }
		 */

		// Append connectedFieldDevices as JSON:
		// connectedFieldDevices

		/*
		 * // String string = toString( new SomeClass() ); try { String
		 * connectedFDString = toString(smartDevice); appendElement(doc,
		 * SMART_DEVICE, connectedFDString); } catch (IOException e) {
		 * LOG.error(e.getMessage(), e); }
		 */
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	private static Element appendElement(Element appendTo, String tag,
			String value) {
		final Element ele = appendTo.getRoot().createElement(tag, value);
		appendTo.appendChild(ele);
		return ele;
	}

	@SuppressWarnings({ "rawtypes", "unchecked", "unused" })
	private static Element appendElement(Element appendTo, String tag) {
		final Element ele = appendTo.getRoot().createElement(tag);
		appendTo.appendChild(ele);
		return ele;
	}

	@Override
	public ID getID() {
		return id;
	}

	public void setID(ID id) {
		this.id = id;
	}

	@Override
	public String[] getIndexFields() {
		return INDEX_FIELDS;
	}

	public static String getAdvertisementType() {
		return ADVERTISEMENT_TYPE;
	}

	/*
	 * public void setContextName(String contextName) { this.contextName =
	 * contextName; }
	 * 
	 * public String getContextName() { return this.contextName; }
	 */

	public void setPeerID(PeerID peerID) {
		this.peerID = peerID;
	}

	public PeerID getPeerID() {
		return peerID;
	}

	public void setPeerName(String peerName) {
		this.peerName = peerName;
	}

	public String getPeerName() {
		return peerName;
	}

	public void setPipeID(PipeID pipeID) {
		this.pipeID = pipeID;
	}

	public PipeID getPipeID() {
		return pipeID;
	}

	private static ID toID(TextElement<?> elem) {
		return toID(elem.getTextValue());
	}

	private static ID toID(String text) {
		try {
			final URI id = new URI(text);
			return IDFactory.fromURI(id);
		} catch (URISyntaxException | ClassCastException ex) {
			LOG.error("Could not set id", ex);
			return null;
		}
	}

	/*
	public SmartDevice getSmartDevice() {
		return smartDevice;
	}

	public void setSmartDevice(SmartDevice smartDevice) {
		this.smartDevice = smartDevice;
	}
	*/

	/*
	// Read the object from Base64 string.
	private static Object fromString(String s) throws IOException,
			ClassNotFoundException {
		byte[] data = Base64Coder.decode(s);
		ObjectInputStream ois = new ObjectInputStream(new ByteArrayInputStream(
				data));
		Object o = ois.readObject();
		ois.close();
		return o;
	}

	// Write the object to a Base64 string.
	private static String toString(Serializable o) throws IOException {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		ObjectOutputStream oos = new ObjectOutputStream(baos);
		oos.writeObject(o);
		oos.close();
		return new String(Base64Coder.encode(baos.toByteArray()));
	}
	*/

	/**
	 * //Example: String string = toString( new SomeClass() );
	 * System.out.println(" Encoded serialized version " ); System.out.println(
	 * string ); SomeClass some = ( SomeClass ) fromString( string );
	 * System.out.println( "\n\nReconstituted object"); System.out.println( some
	 * );
	 */

}
