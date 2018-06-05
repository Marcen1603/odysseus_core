package de.uniol.inf.is.odysseus.incubation.crypt.keymanagement.keys;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * Wrapper, to wrap any object with some metadata of keys. The Wrapped Object
 * does not has to be a Key, so you can wrap own implementations or byteArrays,
 * with contain information about keys.
 * 
 * @author MarkMilster
 *
 * @param <K>
 *            Object, to wrap. This could be a key.
 */
public class KeyWrapper<K extends Object> implements Serializable {

	private static final long serialVersionUID = 8839791650060851796L;

	protected int id;
	protected K key = null;
	protected LocalDateTime created;
	protected LocalDateTime valid;
	protected String comment;

	/**
	 * Default contrsuctor, to parse to JSON
	 */
	public KeyWrapper() {

	}

	/**
	 * Constructor to set Key and Metadata. 
	 * 
	 * @param id ID of the wrapped key
	 * @param key key or other object to wrap
	 * @param created timestamp of creation of the key
	 * @param valid timestamp, of next validation of the key
	 * @param comment comment of the key
	 */
	public KeyWrapper(int id, K key, LocalDateTime created, LocalDateTime valid, String comment) {
		this.setId(id);
		this.setKey(key);
		this.setCreated(created);
		this.setValid(valid);
		this.setComment(comment);
	}

	/**
	 * Acquires Metadata of a other KeyWrapper. This will not change the wrapped key. 
	 * 
	 * @param keyWrapper KeyWrapper, to acquire the metadata from. 
	 */
	public void acquireMetadata(KeyWrapper<?> keyWrapper) {
		this.setId(keyWrapper.id);
		this.setCreated(keyWrapper.created);
		this.setValid(keyWrapper.valid);
		this.setComment(keyWrapper.comment);
	}

	/**
	 * Returns the id
	 * 
	 * @return the id
	 */
	public int getId() {
		return id;
	}

	/**
	 * Sets the id
	 * 
	 * @param id
	 *            the id to set
	 */
	public void setId(int id) {
		this.id = id;
	}

	/**
	 * Returns the key
	 * 
	 * @return the key
	 */
	public K getKey() {
		return key;
	}

	/**
	 * Sets the key
	 * 
	 * @param key
	 *            the key to set
	 */
	public void setKey(K key) {
		this.key = key;
	}

	/**
	 * Returns the Timestamp of Creation
	 * 
	 * @return the created
	 */
	public LocalDateTime getCreated() {
		return created;
	}

	/**
	 * Sets the Timestamp of Creation
	 * 
	 * @param created
	 *            the created to set
	 */
	public void setCreated(LocalDateTime created) {
		this.created = created;
	}

	/**
	 * Returns the Timestamp of next Validation
	 * 
	 * @return the valid
	 */
	public LocalDateTime getValid() {
		return valid;
	}

	/**
	 * Sets the Timestamp of next Validation
	 * 
	 * @param valid
	 *            the valid to set
	 */
	public void setValid(LocalDateTime valid) {
		this.valid = valid;
	}

	/**
	 * Returns the Comment
	 * 
	 * @return the comment
	 */
	public String getComment() {
		return comment;
	}

	/**
	 * Sets the Comment
	 * 
	 * @param comment
	 *            the comment to set
	 */
	public void setComment(String comment) {
		this.comment = comment;
	}

	@Override
	public String toString() {
		return this.getKey().getClass().getName() + String.valueOf(created);
	}

}
