package de.uniol.inf.is.odysseus.base.usermanagement;

import java.io.Serializable;
import java.util.List;

public interface IServiceLevelAgreement extends Serializable{
	/**
	 * Add a new PercentileConstraints. 
	 * @param pc
	 */
	public void addPercentilConstraint(IPercentileConstraint pc) throws PercentileConstraintOverlapException;
	public IPercentileConstraint getPercentilConstraint(double currentSLAConformance) throws NotInitializedException;
	public double getMaxPenalty();
	public void init() throws IllegalServiceLevelDefinition;
	public double oc(double currentSLAConformance) throws NotInitializedException;
	public double mg(double currentSLAConformance) throws NotInitializedException;
	public double getMaxOcMg(double currentSLAConformance) throws NotInitializedException;	
	public int getMaxUsers();
	void setMaxUsers(int maxUsers);
	public List<IPercentileConstraint> getPercentilConstraints();
	public String getName();

}