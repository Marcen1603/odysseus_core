package windperformancercp.model.query;

import java.util.ArrayList;

import windperformancercp.model.sources.Attribute;
import windperformancercp.model.sources.Attribute.AttributeType;
import windperformancercp.model.sources.ISource;

public abstract class APerformanceQuery implements IPerformanceQuery {
	
	String identifier;
	PMType method;
	ArrayList<ISource> concernedSrc;
	ArrayList<Assignment> neededAssigns;
	String queryText;

	
	public enum PMType{
		IEC,
		Langevin
	}

	public APerformanceQuery(String id, PMType method,ArrayList<ISource> sources){
		this.identifier = id;
		this.method = method;
		this.neededAssigns = new ArrayList<Assignment>(); 
		this.concernedSrc = new ArrayList<ISource>(sources);
		
		Assignment asgn = new Assignment(Assignment.Kind.TIMESTAMP,Attribute.AttributeType.STARTTIMESTAMP);
		neededAssigns.add(asgn);
		asgn = new Assignment(Assignment.Kind.WINDSPEED,Attribute.AttributeType.WINDSPEED);
		neededAssigns.add(asgn);
		asgn = new Assignment(Assignment.Kind.POWER,Attribute.AttributeType.POWER);
		neededAssigns.add(asgn);
		asgn = new Assignment(Assignment.Kind.PRESSURE,Attribute.AttributeType.AIRPRESSURE);
		neededAssigns.add(asgn);
		asgn = new Assignment(Assignment.Kind.TEMPERATURE,AttributeType.AIRTEMPERATURE);
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

}
