/**
 * 
 */
package de.uniol.inf.is.odysseus.incubation.crypt.common.messages;

/**
 * @author MarkMilster
 *
 */
public class GetPublicKeyMessage {
	
	private int id;
	
	public GetPublicKeyMessage(int id) {
		this.id = id;
	}

	/**
	 * @return the id
	 */
	public int getId() {
		return id;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(int id) {
		this.id = id;
	}

}
