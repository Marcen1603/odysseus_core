package de.uniol.inf.is.odysseus.core.collection;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;


//public class SecurityPunctuation<T extends IMetaAttribute> extends MetaAttributeContainer<T> implements Serializable, Comparable<Tuple<?>> {
public class SecurityPunctuation implements Serializable {

	private static final long serialVersionUID = -3764088480159997287L;

	private String[] ddpStream;
	private Integer ddpStarttuple;
	private Integer ddpEndtuple;
	private String[] ddpName;
	private String[] srpRole;
	private Integer sign;
	private Long ts;
	
	public SecurityPunctuation(Object[] objects) {
		//Strings mit möglicher Mehrfach-Angabe zerschneiden:
		ddpStream = ((String) objects[0]).split(",");
		for(String ddp:ddpStream) {
			ddp.trim();
		}
		ddpStarttuple = (Integer) objects[1];
		ddpEndtuple = (Integer) objects[2];
		ddpName = ((String) objects[3]).split(",");
		for(String ddp:ddpName) {
			ddp.trim();
		}
		srpRole = ((String) objects[4]).split(",");
		for(String srp:srpRole) {
			srp.trim();
		}
		sign = (Integer) objects[5];
		ts = (Long) objects[6];
	}
	
	public SecurityPunctuation(SecurityPunctuation sp) {
		this.ddpStream = sp.ddpStream;
		this.ddpStarttuple = sp.ddpStarttuple;
		this.ddpEndtuple = sp.ddpEndtuple;
		this.ddpName = sp.ddpName;
		this.srpRole = sp.srpRole;
		this.sign = sp.sign;
		this.ts = sp.ts;
	}
	
	@Override
	public SecurityPunctuation clone() {
		SecurityPunctuation sp = new SecurityPunctuation(this);
		return sp;
	}
	

	public String[] getDDPdpStream() {
		return this.ddpStream;
	}
	public void setDDPStream(String[] ddpStream) {
		this.ddpStream = ddpStream;
	}
	public void setDDPStream(String ddpStream) {
		this.ddpStream[0] = ddpStream;
	}
	
	public Integer getDDPStarttuple() {
		return this.ddpStarttuple;
	}
	public void setDDPStarttuple(Integer ddpStarttuple) {
		this.ddpStarttuple = ddpStarttuple;
	}
	
	public Integer getDDPEndtuple() {
		return this.ddpEndtuple;
	}
	public void setDDPEndtuple(Integer ddpEndtuple) {
		this.ddpEndtuple = ddpEndtuple;
	}
	
	public String[] getDDPName() {
		return this.ddpName;
	}
	public void setDDPName(String[] ddpName) {
		this.ddpName = ddpName;
	}
	public void setDDPName(String ddpName) {
		this.ddpName[0] = ddpName;
	}
	
	public String[] getSRPRole() {
		return this.srpRole;
	}
	public void setSRPRole(String[] srpRole) {
		this.srpRole = srpRole;
	}
	public void setSRPRole(String srpRole) {
		this.srpRole[0] = srpRole;
	}
	
	public Integer getSign() {
		return this.sign;
	}
	public void setSign(Integer sign) {
		this.sign = sign;
	}
	
	public Long getTS() {
		return this.ts;
	}
	public void setTS(Long ts) {
		this.ts = ts;
	}
	
	
	public Boolean evaluateTS(Long ts) {
		if((this.getDDPStarttuple() == -1 && this.getDDPEndtuple() == -1) ||
				(ts > this.getDDPStarttuple() && (ts < this.getDDPEndtuple() && this.getDDPEndtuple() == -1))) {
			return true;
		}
		return false;
	}

	public Boolean evaluateRoles(List<String> userRoles) {
		if(this.getSRPRole().equals("")) {
			return true;
		}
		for(String role:this.getSRPRole()) {
			if(userRoles.contains(role)) {
				return true;
			}
		}
		return false;
	}
	
	/**
	 * Ist das überhaupt möglich? Haben Attribute überhaupt Namen???
	 * Namen werden bei ACCESS bestimmt --> nicht sicher...???
	 * 
	 * @param oldTuple
	 * @return
	 */
	public Tuple<?> evaluateAttributes(Object oldTuple) {
		if(this.getDDPName() == null) {
			return null;
		}
		if(this.getDDPName().length <= 1 && this.getDDPName()[0].equals("")) {
			return null;
		}
		
		ArrayList<Object> attributes = new ArrayList<Object>();
		for(Object object:((Tuple<?>) oldTuple).getAttributes()) {
			for(String attribute:this.getDDPName()) {
				if(attribute.equals(object)) {
					attributes.add(attribute);
				}
			}
		}
		Tuple<?> newTuple = new Tuple();
		return newTuple;
	}	
}
