package de.uniol.inf.is.odysseus.securitypunctuation.datatype;

public class IndexEntry {
	long[] roles;
	AbstractSecurityPunctuation sp;
	
	public IndexEntry(long[] roles, AbstractSecurityPunctuation sp) {
		super();
		this.roles = roles;
		this.sp = sp;
	}
	public long[] getRoles() {
		return roles;
	}
	public void setRoles(long[] roles) {
		this.roles = roles;
	}
	public AbstractSecurityPunctuation getSp() {
		return sp;
	}
	public void setSp(AbstractSecurityPunctuation sp) {
		this.sp = sp;
	}
}
