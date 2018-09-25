package de.uniol.inf.is.odysseus.core.collection;

import java.io.Serializable;

import de.uniol.inf.is.odysseus.core.usermanagement.IUser;

public class Resource implements Serializable, Comparable<Resource> {

	private static final long serialVersionUID = -9162627611692098365L;
	final private String user;
	final private String resourceName;
	final private boolean marked;
	
	public Resource(IUser user, String resourceName) {
		this.user = user.getName();
		this.resourceName = resourceName;
		marked = false;
	}
	
	public Resource(IUser user, String resourceName, boolean marked) {
		this.user = user.getName();
		this.resourceName = resourceName;
		this.marked = marked;
	}
	
	public Resource(String resourcename) {
		int pos = resourcename.indexOf('.');
		if (pos > 0) {
			user = resourcename.substring(0, pos);
			this.resourceName = resourcename.substring(pos + 1);
		} else {
			throw new IllegalArgumentException("Parameter " + resourcename + " does not contain a '.'");
		}
		marked = false;
	}

	public Resource(String username, String resourceName) {
		this.user = username;
		this.resourceName = resourceName;
		marked = false;
	}
	
	public Resource(String username, String resourceName, boolean marked) {
		this.user = username;
		this.resourceName = resourceName;
		this.marked = marked;
	}

	public String getUser() {
		return user;
	}

	public String getResourceName() {
		return resourceName;
	}
	
	public boolean isMarked() {
		return marked;
	}

	public String getShortString(String caller) {
		return getUser().equals(caller)?getResourceName():toString();
	}

	
	@Override
	public String toString() {
		return new StringBuffer(user).append(".").append(resourceName).toString();
	}

	@Override
	public int compareTo(Resource other) {
		return toString().compareTo(other.toString());
	}

	public static boolean containsUser(IUser caller, String resourceName) {
		int pos = resourceName.indexOf(".");
		if (pos > 0) {
			return resourceName.substring(0, pos).equalsIgnoreCase(caller.getName());
		} else {
			return false;
		}
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((resourceName == null) ? 0 : resourceName.hashCode());
		result = prime * result + ((user == null) ? 0 : user.hashCode());
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
		Resource other = (Resource) obj;
		if (resourceName == null) {
			if (other.resourceName != null)
				return false;
		} else if (!resourceName.equals(other.resourceName))
			return false;
		if (user == null) {
			if (other.user != null)
				return false;
		} else if (!user.equals(other.user))
			return false;
		return true;
	}

	/**
	 * 
	 * This method first tries to create a resource from the resource parameter
	 * If no "." is contained, the given user will be added
	 * 
	 * @param resource
	 * @param user
	 * @return
	 */
	public static final Resource specialCreateResource(String resource, IUser user){
		// Not nescessary!
//		if (Resource.containsUser(user, resource)){
//			return new Resource(resource);
//		}
		if (resource.contains(".")){
			return new Resource(resource);
		}
		return new Resource(user, resource);
	}
	
	public static void main(String[] args) {
		Resource test = new Resource("test1.test2.test3");
		System.out.println(test.getUser());
		System.out.println(test.getResourceName());

		test = new Resource("test");
	}


}
