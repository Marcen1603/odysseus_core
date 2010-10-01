package de.uniol.inf.is.odysseus.usermanagement;


public class ServiceLevelAgreement extends AbstractServiceLevelAgreement{

	private static final long serialVersionUID = 3682984863695708771L;

	public ServiceLevelAgreement(String name) {
		super(name);
	}
	
	// TODO: Darüber nachdenken, ob man nicht einfach 100 Werte vorberechnet, sollte ausreichend sein ...
	
	@Override
	public double oc(double currentSLAConformance)
			throws NotInitializedException {
		checkInitialized();
		IPercentileConstraint p = getPercentilConstraint(currentSLAConformance);
		int index = indexOf(p);
		double oc = 0;
		if (p.getLowSlaConformanceLevel() > 0){
			oc =  Math.pow((((p.getHighSlaConformanceLevel() - currentSLAConformance)/p.getWidth())),2) * delta[index];
		}
		return oc;
	}

	@Override
	public double mg(double currentSLAConformance)
			throws NotInitializedException {
		checkInitialized();
		IPercentileConstraint p = getPercentilConstraint(currentSLAConformance);
		int index = indexOf(p);
		double mg = 0;
		if (p.getHighSlaConformanceLevel() < 1){
			mg =  Math.pow((((currentSLAConformance - p.getLowSlaConformanceLevel())/p.getWidth())),2) * delta[index-1];
		}
		return mg;
	}
}
