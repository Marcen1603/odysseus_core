package de.uniol.inf.is.odysseus.base.usermanagement;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class User {

	private String username;
	private String password;
	static MessageDigest md5 = null;

	User(String username, String password) {
		try {
			synchronized (User.class) {
				if (md5 == null) {
					md5 = MessageDigest.getInstance("MD5");
				}
			}
		} catch (NoSuchAlgorithmException e) {
		}
		this.username = username;
		this.password = md5(password);
	}

	private String md5(String password) {
		StringBuffer hexString = new StringBuffer();
		if (md5 != null) {
			synchronized (md5) {
				md5.reset();
				md5.update(password.getBytes());
				byte[] result = md5.digest();
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

	public boolean validatePassword(String password, boolean passwordIsMd5) {
		if (passwordIsMd5) {
			return password.equals(password);
		} else {
			return this.password.equals(md5(password));
		}
	}

	public String getPassword() {
		return password;
	}

}
