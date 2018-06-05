package de.uniol.inf.is.odysseus.incubation.crypt.common.messages;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Message to send the Metadata to get a PublicKey
 * 
 * @author MarkMilster
 *
 */
public class GetPublicKeyMessage implements Serializable {

	private static final long serialVersionUID = 4041035551336547237L;
	private ArrayList<Integer> id;

	/**
	 * Default constructor to parse to JSON
	 */
	public GetPublicKeyMessage() {
		this.id = new ArrayList<>();
	}

	/**
	 * Constructor with parameters
	 * 
	 * @param id
	 *            List of the ids of the PublicKeys, you want to receive
	 */
	public GetPublicKeyMessage(ArrayList<Integer> id) {
		this.id = id;
	}

	/**
	 * Returns the List of ids
	 * 
	 * @return the id
	 */
	public ArrayList<Integer> getId() {
		return id;
	}

	/**
	 * Sets the List of Ids
	 * 
	 * @param id
	 *            the id to set
	 */
	public void setId(ArrayList<Integer> id) {
		this.id = id;
	}

	/**
	 * Adds a id to the List of ids
	 * 
	 * @param id
	 *            The id to add
	 */
	public void addId(Integer id) {
		this.id.add(id);
	}

}
