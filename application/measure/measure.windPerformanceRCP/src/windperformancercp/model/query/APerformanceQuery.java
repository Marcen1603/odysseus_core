package windperformancercp.model.query;

import java.util.ArrayList;

import windperformancercp.model.sources.Attribute;
import windperformancercp.model.sources.Attribute.AttributeType;
import windperformancercp.model.sources.ISource;

public abstract class APerformanceQuery implements IPerformanceQuery {
	
	String identifier;
	PMType method;
	ArrayList<ISource> concernedSrc;
	ArrayList<Assignment> neededAssigns = new ArrayList<Assignment>();
	
	String queryText;
	
	QueryGenerator Pgen;
	QueryGenerator Qgen;

	
	public enum PMType{
		IEC,
		Langevin
	}

	public APerformanceQuery(String id, PMType method,ArrayList<ISource> sources){
		this.identifier = id;
		this.method = method;
		this.neededAssigns = new ArrayList<Assignment>(); 
		this.concernedSrc = new ArrayList<ISource>(sources);
		Pgen = new QueryGenerator(new PQLGenerator());
		Qgen = new QueryGenerator(new CQLGenerator());
		
	/*	Assignment asgn = new Assignment(Assignment.Kind.TIMESTAMP,Attribute.AttributeType.STARTTIMESTAMP);
		neededAssigns.add(asgn);*/
		Assignment asgn = new Assignment(Attribute.AttributeType.WINDSPEED);
		neededAssigns.add(asgn);
		asgn = new Assignment(Attribute.AttributeType.POWER);
		neededAssigns.add(asgn);
		asgn = new Assignment(Attribute.AttributeType.AIRPRESSURE);
		neededAssigns.add(asgn);
		asgn = new Assignment(AttributeType.AIRTEMPERATURE);
	}
	
	@Override
	public void setIdentifier(String id){
		this.identifier = id;
	}
	
	@Override
	public String getIdentifier(){
		return identifier;
	}
	
	@Override
	public String getQueryText(){
		return queryText;
	}
	
	@Override
	public void setQueryText(String text){
		this.queryText = text;
	}
	
	@Override
	public boolean register() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean deregister() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public String getMethod() {
		return method.toString();
	}
	
	@Override
	public void setAssignment(String what, Stream who){
		for(Assignment a: neededAssigns){
			if(a.getKind().toString().equalsIgnoreCase(what))
				a.setRespStream(who);
		}	
	}
	
	@Override
	public Stream getResponsibleStream(String what){
		for(Assignment a: neededAssigns){
			if(a.getKind().toString().equalsIgnoreCase(what))
				return a.getRespStream();
		}
		return null;
	}

	@Override
	public ArrayList<ISource> getMember() {
		return concernedSrc;
	}
	
	@Override
	public void addMember(ISource m){
		concernedSrc.add(m);
	}
	
	@Override
	public void addAllMembers(ArrayList<ISource> listm){
		concernedSrc.addAll(listm);
	}
	
	@Override
	public void clearMembers(){
		concernedSrc.clear();
	}
	
	@Override
	public ArrayList<Assignment> getAssignments(PMType what) {
		return neededAssigns;
	}
	
	@Override
	public ArrayList<OperatorResult> generateSourceStreams(){
		ArrayList<OperatorResult> result = new ArrayList<OperatorResult>(); 
		for(ISource src: concernedSrc){
			OperatorResult current = Qgen.generateCreateStream(src);
			result.add(current);
			System.out.println(current.getQuery());
		}
		
		return result;
	}
	
	@Override
	public ArrayList<Assignment> getPossibleAssignments(){
		ArrayList<Assignment> possibilities = new ArrayList<Assignment>();
		for(Assignment a: neededAssigns){
			for(ISource s: concernedSrc){
				for(Attribute at: s.getAttributeList()){
					if(a.getAttType().equals(at.getAttType())){
						Assignment newpos = new Assignment(a.getAttType(),s,s.getAttIndex(at));
						possibilities.add(newpos);
					}
				}
			}
		}
		return possibilities;
	}
	

	@Override
	public void setMethod(PMType type) {
		// TODO Auto-generated method stub
	}

}
