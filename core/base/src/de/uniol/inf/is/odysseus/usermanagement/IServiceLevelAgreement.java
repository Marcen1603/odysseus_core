package de.uniol.inf.is.odysseus.usermanagement;

import java.io.Serializable;
import java.util.List;

import de.uniol.inf.is.odysseus.ICSVToString;

public interface IServiceLevelAgreement extends Serializable, ICSVToString{
	/**
	 * Add a new PercentileConstraints. 
	 * @param pc
	 */
	public void addPercentilConstraint(IPercentileConstraint pc) throws PercentileConstraintOverlapException;
	public IPercentileConstraint getPercentilConstraint(double currentSLAConformance) throws NotInitializedException;
	public void init() throws IllegalServiceLevelDefinition;
	public double oc(double currentSLAConformance) throws NotInitializedException;
	public double mg(double currentSLAConformance) throws NotInitializedException;
	public double getMaxOcMg(double currentSLAConformance) throws NotInitializedException;	
	public int getMaxUsers();
	void setMaxUsers(int maxUsers);
	public List<IPercentileConstraint> getPercentilConstraints();
	public String getName();
	public boolean isInitialized();
	void preCalc(int elements);
}