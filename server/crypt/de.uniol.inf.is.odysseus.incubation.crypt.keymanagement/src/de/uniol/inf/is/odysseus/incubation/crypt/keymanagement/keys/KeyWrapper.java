package de.uniol.inf.is.odysseus.incubation.crypt.keymanagement.keys;

import java.io.Serializable;
import java.time.LocalDateTime;

public class KeyWrapper<K extends Object> implements Serializable {

	private static final long serialVersionUID = 8839791650060851796L;

	protected int id;
	protected K key = null;
	protected LocalDateTime created;
	protected LocalDateTime valid;
	protected String comment;

	public KeyWrapper() {

	}

	public KeyWrapper(int id, K key, LocalDateTime created, LocalDateTime valid, String comment) {
		this.setId(id);
		this.setKey(key);
		this.setCreated(created);
		this.setValid(valid);
		this.setComment(comment);
	}

	public void acquireMetadata(KeyWrapper<?> keyWrapper) {
		this.setId(keyWrapper.id);
		this.setCreated(keyWrapper.created);
		this.setValid(keyWrapper.valid);
		this.setComment(keyWrapper.comment);
	}

	/**
	 * @return the id
	 */
	public int getId() {
		return id;
	}

	/**
	 * @param id
	 *            the id to set
	 */
	public void setId(int id) {
		this.id = id;
	}

	/**
	 * @return the key
	 */
	public K getKey() {
		return key;
	}

	/**
	 * @param key
	 *            the key to set
	 */
	public void setKey(K key) {
		this.key = key;
	}

	/**
	 * @return the created
	 */
	public LocalDateTime getCreated() {
		return created;
	}

	/**
	 * @param created
	 *            the created to set
	 */
	public void setCreated(LocalDateTime created) {
		this.created = created;
	}

	/**
	 * @return the valid
	 */
	public LocalDateTime getValid() {
		return valid;
	}

	/**
	 * @param valid
	 *            the valid to set
	 */
	public void setValid(LocalDateTime valid) {
		this.valid = valid;
	}

	/**
	 * @return the comment
	 */
	public String getComment() {
		return comment;
	}

	/**
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
