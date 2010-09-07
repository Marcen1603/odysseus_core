package de.uniol.inf.is.odysseus.base.usermanagement;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.StringTokenizer;

public class User {

	private final String hashFunction = "SHA-1";
	private String username;
	private final String password;
	static MessageDigest hash = null;

	User(String username, String password) {
		try {
			synchronized (User.class) {
				if (hash == null) {
					hash = MessageDigest.getInstance(hashFunction);
				}
			}
		} catch (NoSuchAlgorithmException e) {
		}
		this.username = username;
		this.password = hash(password);
	}

	User(String line) {
		StringTokenizer tokenizer = new StringTokenizer(line, " ");
		if (tokenizer.hasMoreTokens()) {
			username = tokenizer.nextToken();
		}else{
			throw new IllegalArgumentException("Cannot initiate user from line "+line);
		}
		if (tokenizer.hasMoreElements()) {
			password = tokenizer.nextToken();
		}else{
			throw new IllegalArgumentException("Cannot initiate user from line "+line);
		}
	}

	private String hash(String password) {
		StringBuffer hexString = new StringBuffer();
		if (hash != null) {
			synchronized (hash) {
				hash.reset();
				hash.update(password.getBytes());
				byte[] result = hash.digest();
				for (int i = 0; i < result.length; i++) {
					if (result[i] <= 15 && result[i] >= 0) {
						hexString.append("0");
					}
					hexString.append(Integer.toHexString(0xFF & result[i]));
				}
			}
			return hexString.toString();
		} else {
			return password;
		}
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((username == null) ? 0 : username.hashCode());
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
		User other = (User) obj;
		if (username == null) {
			if (other.username != null)
				return false;
		} else if (!username.equals(other.username))
			return false;
		return true;
	}

	public boolean validatePassword(String password, boolean passwordIsHash) {
		if (passwordIsHash) {
			return password.equals(password);
		} else {
			return this.password.equals(hash(password));
		}
	}

	public String getPassword() {
		return password;
	}

	@Override
	public String toString() {
		return username+" "+password;
	}
}
