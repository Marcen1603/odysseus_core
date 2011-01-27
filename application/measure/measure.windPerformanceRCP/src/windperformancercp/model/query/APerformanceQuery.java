package windperformancercp.model.query;

import java.sql.Timestamp;
import java.util.ArrayList;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlType;

import windperformancercp.model.sources.Attribute;
import windperformancercp.model.sources.Attribute.AttributeType;
import windperformancercp.model.sources.ISource;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "abstractPerformanceQuery", propOrder = {
    "identifier",
    "method",
    "concernedSrc",
    "concernedStr",
    "assignments",
    "starttime",
    "queryText"
})
public abstract class APerformanceQuery implements IPerformanceQuery {
	
	String identifier;
	PMType method;
	ArrayList<ISource> concernedSrc;
	ArrayList<Stream> concernedStr;
	ArrayList<Assignment> assignments;
	Timestamp starttime;
	
	String queryText;
	
	QueryGenerator Pgen;
	QueryGenerator Qgen;

	
	public enum PMType{
		IEC,
		Langevin
	}

	public APerformanceQuery(String id, PMType method,ArrayList<ISource> sources){
		this();
		
		this.setIdentifier(id);
		this.setMethod(method);
		this.setConcernedSrc(sources);
	}
	
	public APerformanceQuery(){
		this.identifier = "";
		this.method = null;
		this.assignments = new ArrayList<Assignment>();
		this.concernedSrc = new ArrayList<ISource>();
		this.concernedStr = new ArrayList<Stream>();
		Pgen = new QueryGenerator(new PQLGenerator());
		Qgen = new QueryGenerator(new CQLGenerator());
	
		Assignment asgn = new Assignment(Attribute.AttributeType.WINDSPEED);
		assignments.add(asgn);
		asgn = new Assignment(Attribute.AttributeType.POWER);
		assignments.add(asgn);
		asgn = new Assignment(Attribute.AttributeType.AIRPRESSURE);
		assignments.add(asgn);
		asgn = new Assignment(AttributeType.AIRTEMPERATURE);
		assignments.add(asgn);
	
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

	//TODO: hier mal aufräumen
	

	@Override
	public void setMethod(PMType type) {
		this.method = type;
	}
	
	@Override
	public PMType getMethod() {
		return method;
	}
	
	@Override
	public void setAssignment(String what, Stream who){
		for(Assignment a: assignments){
			if(a.getAttType().toString().equalsIgnoreCase(what))
				a.setRespStream(who);
		}	
	}
	
	@Override
	public Stream getResponsibleStream(String what){
		for(Assignment a: assignments){
			if(a.getAttType().toString().equalsIgnoreCase(what))
				return a.getRespStream();
		}
		return null;
	}
	
	@Override
	public String getResponsibleAttribute(String what){
		for(Assignment a: assignments){
			if(a.getAttType().toString().equalsIgnoreCase(what))
				return a.getRespSource().getIthAtt(a.getAttributeId()).getName();
		}
		return "";
	}

	@XmlElementWrapper(name = "concernedSources") 
	@XmlElement(name = "source")
	@Override
	public ArrayList<ISource> getConcernedSrc(){
		return concernedSrc;
	}
	
	@Override
	public void setConcernedSrc(ArrayList<ISource> srcList) {
		this.concernedSrc = new ArrayList<ISource>(srcList);
	}
	
	@XmlElementWrapper(name = "concernedStreams") 
	@XmlElement(name = "stream")
	@Override
	public ArrayList<Stream> getConcernedStr(){
		return concernedStr;
	}
	
	@Override
	public void setConcernedStr(ArrayList<Stream> strList) {
		this.concernedStr = new ArrayList<Stream>(strList);
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
	
	@XmlElementWrapper(name = "assignmentList") 
	@XmlElement(name = "assignment")
	@Override
	public ArrayList<Assignment> getAssignments() {
		return assignments;
	}
	
	@Override
	public void setAssignments(ArrayList<Assignment> assigns){
		this.assignments = assigns;
	}
	
	@Override
	public ArrayList<String> generateSourceStreams(){
		ArrayList<String> qryresult = new ArrayList<String>(); 
		concernedStr.clear();
		for(ISource src: concernedSrc){
			OperatorResult current = Qgen.generateCreateStream(src);
			if(current != null){
				concernedStr.add(current.getStream());
				for(Assignment as: assignments){
					if(as.getRespSource().equals(src))
						as.setRespStream(current.getStream());
				}
				qryresult.add(current.getQuery());
			}
			else{
				//TODO: Fehlermeldung
			}
	//		System.out.println(current.getQuery());
		}
		return qryresult;
	}
	
	@Override
	public String generateQuery(){
		return null;
	}

	@Override
	public ArrayList<Assignment> getPossibleAssignments(){
		ArrayList<Assignment> possibilities = new ArrayList<Assignment>();
		for(Assignment a: assignments){
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
	public OperatorResult projectStreamToAssignments(Stream str, String outputName){
		ArrayList<Integer> newattsPos = new ArrayList<Integer>(); 
		for (Assignment as: assignments){
			if(as.getRespStream().equals(str)){
					newattsPos.add(as.getAttributeId());
			}
		}
			
		int[] tmp = new int[newattsPos.size()];
		for(int i=0;i<tmp.length;i++){
			tmp[i] = ((Integer)newattsPos.get(i)).intValue();
		}
		OperatorResult result = Pgen.generateProjection(str, tmp, outputName);
		return result;
	}
		
	@Override
	public ArrayList<ISource> extractSourcesFromAssignments(){
		ArrayList<ISource> sources = new ArrayList<ISource>();
		for(Assignment as: assignments){
			if(! sources.contains(as.getRespSource()))
				sources.add(as.getRespSource());
		}
		this.concernedSrc = sources;
		return sources;
	}

}
