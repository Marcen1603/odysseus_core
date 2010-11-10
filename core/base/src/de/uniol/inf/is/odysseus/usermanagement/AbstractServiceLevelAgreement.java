package de.uniol.inf.is.odysseus.usermanagement;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

abstract public class AbstractServiceLevelAgreement implements IServiceLevelAgreement{

	private static final long serialVersionUID = 7791032987035486163L;

	private List<IPercentileConstraint> pcs = new ArrayList<IPercentileConstraint>();
	
	transient protected double[] delta = null;
	transient private boolean initialized = false;
	int maxUsers = -1;

	private String name;
	
	public AbstractServiceLevelAgreement(String name) {
		this.name = name;
	}
	
	@Override
	public void addPercentilConstraint(IPercentileConstraint pc) throws PercentileConstraintOverlapException{
		for (IPercentileConstraint p:pcs){
			if (p.overlaps(pc)){
				throw new PercentileConstraintOverlapException(p+" and "+pc);
			}
		}
		pcs.add(pc);
		initialized = false;
	}
	
	@Override
	public void init() throws IllegalServiceLevelDefinition{
		Collections.sort(pcs);
		// More than one Percentile
		if (pcs.size() <= 1){
			throw new IllegalServiceLevelDefinition("More than one percentile contstraint required");
		}
		if (!(pcs.get(0).getHighSlaConformanceLevel() == 1.0 && pcs.get(pcs.size()-1).getLowSlaConformanceLevel() == 0.0)){
			throw new IllegalServiceLevelDefinition("Range not covered completly");
		}
		// Calc Deltas for SLAs		
		delta = new double[pcs.size() - 1 ];
		Iterator<IPercentileConstraint> iter = pcs.iterator();
		int i=0;
		IPercentileConstraint last = iter.next();
		while (iter.hasNext()){
			IPercentileConstraint current = iter.next();
			delta[i] = current.getPenalty()-last.getPenalty() ;
			if (last.getLowSlaConformanceLevel() != current.getHighSlaConformanceLevel()){
				throw new IllegalServiceLevelDefinition("Range not covered complety");
			}
			i++;
			last = current;
		}
		initialized = true;
	}
	
	protected void checkInitialized() throws NotInitializedException {
		if (!initialized){
			throw new NotInitializedException();
		}
	}
	

	@Override
	public IPercentileConstraint getPercentilConstraint(double currentSLAConformance) throws NotInitializedException{
		if (!initialized){
			throw new NotInitializedException();
		}
		for (IPercentileConstraint pc:pcs){
			if (pc.contains(currentSLAConformance)){
				return pc;
			}
		}
		return null;
	}
	
	protected int indexOf(IPercentileConstraint p) {
		return pcs.indexOf(p);
	}
	
	@Override
	public double getMaxPenalty(){
		return pcs.get(pcs.size()-1).getPenalty();
	}
	
	@Override
	public String toString() {
		StringBuffer ret = new StringBuffer("Percentiles: ");
		for (IPercentileConstraint s: pcs){
			ret.append(s).append("\n");
		}
		if (initialized){
			ret.append("Initialized: ");
			for (double v:delta){
				ret.append(" "+v);
			}
		}else{
			ret.append("Not Initialized");
		}
		return ret.toString();
	}
	
	@Override
	public double getMaxOcMg(double currentSLAConformance) throws NotInitializedException{
		return Math.max(oc(currentSLAConformance), mg(currentSLAConformance));
	}
	
	@Override
	public void setMaxUsers(int maxUsers) {
		this.maxUsers = maxUsers;
	}
	
	@Override
	public int getMaxUsers() {
		return maxUsers;
	}

	@Override
	public List<IPercentileConstraint> getPercentilConstraints() {
		return Collections.unmodifiableList(pcs);
	}
	
	@Override
	public String getName() {
		return name;
	}
	
	@Override
	public boolean isInitialized() {
		return initialized;
	}
}
