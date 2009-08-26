package de.uniol.inf.is.odysseus.iec61970.dataaccessserver.communication;



/**
 * Die zu transferierenden Daten mit Verbindungsinformationen
 * @author Mart Köhler
 *
 */
class Data {
	private Provider provider;
	private byte[] data;
	public Data(Provider provider, byte[] data) {
		this.provider = provider;
		this.data = data;
	}
	public Provider getProvider() {
		return provider;
	}
	public byte[] getData() {
		return data;
	}
}