package de.uniol.inf.is.odysseus.usermanagement;


public class ServiceLevelAgreement extends AbstractServiceLevelAgreement{

	private static final long serialVersionUID = 3682984863695708771L;
	
	public ServiceLevelAgreement(String name) {
		super(name);
	}
	

	
	@Override
	public double oc(double currentSLAConformance)
			throws NotInitializedException {
		checkInitialized();
		IPercentileConstraint p = getPercentilConstraint(currentSLAConformance);
		int index = indexOf(p);
		double oc = 0;
		if (p.getLowSlaConformanceLevel() > 0){
			oc =  Math.pow((((p.getHighSlaConformanceLevel() - currentSLAConformance)/p.getWidth())),2) * delta[index];
		}else{
			oc =  Math.pow((((p.getHighSlaConformanceLevel() - currentSLAConformance)/p.getWidth())),2) * p.getPenalty();
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

	public String getCSVHeader(){
		return "i;levelUp;levelDown;urge;slapenalty";
	}
	
	@Override
	public String csvToString() {
		StringBuffer csvReturn = new StringBuffer();
		for (double i=1;i>=0;i=Math.round((i-0.001)*1000.0)/1000.0){
			double oc = oc(i);
			double mg = mg(i);
			csvReturn.append((int)(i*1000)+";" + Math.round(oc*100)/100+";"+  Math.round(mg*100)/100+";"+Math.round(Math.max(oc,mg)*100)/100+";"+(int)getPercentilConstraint(i).getPenalty());
		}
		return csvReturn.toString();
	}
}
