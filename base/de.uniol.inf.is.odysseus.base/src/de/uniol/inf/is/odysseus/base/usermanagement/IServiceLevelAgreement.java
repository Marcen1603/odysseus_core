package de.uniol.inf.is.odysseus.base.usermanagement;

public interface IServiceLevelAgreement {
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
	double getMaxOcMg(double currentSLAConformance) throws NotInitializedException;	

}