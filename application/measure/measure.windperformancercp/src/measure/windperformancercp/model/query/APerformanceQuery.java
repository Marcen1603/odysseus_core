/** Copyright [2011] [The Odysseus Team]
  *
  * Licensed under the Apache License, Version 2.0 (the "License");
  * you may not use this file except in compliance with the License.
  * You may obtain a copy of the License at
  *
  *     http://www.apache.org/licenses/LICENSE-2.0
  *
  * Unless required by applicable law or agreed to in writing, software
  * distributed under the License is distributed on an "AS IS" BASIS,
  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
  * See the License for the specific language governing permissions and
  * limitations under the License.
  */
package measure.windperformancercp.model.query;

import java.sql.Timestamp;
import java.util.ArrayList;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementRef;
import javax.xml.bind.annotation.XmlElementRefs;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.XmlType;

import measure.windperformancercp.model.sources.Attribute;
import measure.windperformancercp.model.sources.ISource;
import measure.windperformancercp.model.sources.MetMast;
import measure.windperformancercp.model.sources.WindTurbine;
import measure.windperformancercp.model.sources.Attribute.AttributeType;


@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "abstractPerformanceQuery", propOrder = {
    "identifier",
    "method",
    "connectState",
    "concernedSrc",
    "concernedStr",
    "assignments",
    "queryText",
    "timestampAttributes",
    "windspeedAttribute",
    "powerAttribute",
    "pressureAttribute",
    "temperatureAttribute",
    "pitch"
})
public abstract class APerformanceQuery implements IPerformanceQuery {
	
	protected String identifier;
	protected PMType method;
	@XmlTransient
	protected ArrayList<ISource> concernedSrc;
	@XmlTransient
	protected ArrayList<Stream> concernedStr;
	@XmlTransient
	protected ArrayList<Assignment> assignments;
	@XmlTransient
	protected Timestamp starttime;
	protected boolean pitch;
	protected boolean connectState = false;
	
	@XmlTransient
	protected ArrayList<Integer> timestampAttributes;
	protected String windspeedAttribute = "";
	protected String powerAttribute = "";
	protected String pressureAttribute = "";
	protected String temperatureAttribute = "";
		
	protected String queryText;
	
	@XmlTransient
	protected QueryGenerator Pgen;
	@XmlTransient
	protected QueryGenerator Qgen;

	
	public enum PMType{
		IEC,
		Langevin
	}

	public APerformanceQuery(String id, 
			PMType method,
			ArrayList<ISource> sources, 
			ArrayList<Stream> streams, 
			String queryText, 
			ArrayList<Assignment> assigns,
			boolean pitch,
			String wsAtt,
			String powAtt,
			String prAtt,
			String tempAtt){
		this(id,method,sources);
		this.setConcernedStr(streams);
		this.setQueryText(queryText);
		this.setAssignments(assigns);
		this.setPitch(pitch);
		this.setWindspeedAttribute(wsAtt);
		this.setPowerAttribute(powAtt);
		this.setPressureAttribute(prAtt);
		this.setTemperatureAttribute(tempAtt);
		
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
		this.timestampAttributes = new ArrayList<Integer>();
		this.pitch = false;
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
	public void extractTurbineData(){
		for(ISource src: concernedSrc){
			if(src.isWindTurbine()){				
				pitch = (((WindTurbine)src).getPowerControl()==0);
				break;
			}
		}
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
	public void setConnectStat(boolean c) {
		this.connectState = c;
	}
	
	@Override
	public boolean getConnectStat() {
		return connectState;
	}
	
	@Override
	public void setPitch(boolean p){
		this.pitch = p;
	}
	
	@Override
	public boolean getPitch(){
		return this.pitch;
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
	@XmlElementRefs( 
		{ 
		    @XmlElementRef( type = MetMast.class), 
		    @XmlElementRef( type = WindTurbine.class), 
		} )
	@Override
	public ArrayList<ISource> getConcernedSrc(){
		return concernedSrc;
	}
	
	@Override
	public void setConcernedSrc(ArrayList<ISource> srcList) {
		this.concernedSrc = new ArrayList<ISource>(srcList);
		extractTurbineData();
		updateTimestampAttributes();
	}
	
	@XmlElementWrapper(name = "concernedStreams") 
	@XmlElement(name = "stream")
	@Override
	public ArrayList<Stream> getConcernedStr(){
		return concernedStr;
	}
	
	
	@XmlElementWrapper(name = "timestampAttributeIndexList") 
	@XmlElement(name = "timestamp")
	public ArrayList<Integer> getTimestampAttributes(){
		return timestampAttributes;
	}
	
	public void setTimestampAttributes(ArrayList<Integer>  tsal) {
		this.timestampAttributes = tsal;
	}
	
	@Override
	public void setConcernedStr(ArrayList<Stream> strList) {
		this.concernedStr = new ArrayList<Stream>(strList);
	}
	
	
	@Override
	public void addMember(ISource m){
		concernedSrc.add(m);
		extractTurbineData();
		updateTimestampAttributes();
	}
	
	@Override
	public void addAllMembers(ArrayList<ISource> listm){
		concernedSrc.addAll(listm);
		extractTurbineData();
		updateTimestampAttributes();
	}
	
	@Override
	public void clearMembers(){
		concernedSrc.clear();
		updateTimestampAttributes();
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
		windspeedAttribute = getResponsibleAttribute("WINDSPEED");
		powerAttribute = getResponsibleAttribute("POWER");
		pressureAttribute = getResponsibleAttribute("AIRPRESSURE");
		temperatureAttribute = getResponsibleAttribute("AIRTEMPERATURE");
	}
	
	@Override
	public void setWindspeedAttribute(String at){
		this.windspeedAttribute = at;
	}
	
	@Override
	public String getWindspeedAttribute(){
		return windspeedAttribute;
	}
	
	@Override
	public void setPowerAttribute(String at){
		this.powerAttribute = at;
	}
	
	@Override
	public String getPowerAttribute(){
		return powerAttribute;
	}
	
	@Override
	public void setPressureAttribute(String at){
		this.pressureAttribute = at;
	}
	
	@Override
	public String getPressureAttribute(){
		return pressureAttribute;
	}
	
	@Override
	public void setTemperatureAttribute(String at){
		this.temperatureAttribute = at;
	}
	
	@Override
	public String getTemperatureAttribute(){
		return temperatureAttribute;
	}
	
	public void updateTimestampAttributes(){
		boolean set = false;
		timestampAttributes.clear();
		for(int i =0; i< concernedSrc.size();i++){
			ArrayList<Attribute> atts = concernedSrc.get(i).getAttributeList();
			set = false;
			for(int j = 0; j<atts.size();j++){
				Attribute att = atts.get(j);
				if(att.getAttType().equals(AttributeType.STARTTIMESTAMP)){
					timestampAttributes.add(j);
					set = true;
					break;
				}
			}
			if(!set)
				timestampAttributes.add(0);
		}
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
	public ArrayList<String> generateRemoveStreams(){
		ArrayList<String> qryresult = new ArrayList<String>(); 
		concernedStr.clear();
		for(ISource src: concernedSrc){
			OperatorResult current = Qgen.generateRemoveStream(src);
			if(current != null){
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
	public OperatorResult projectStreamToAssignments(Stream str, int ts, String outputName){
		ArrayList<Integer> newattsPos = new ArrayList<Integer>(); 
		if(ts != -1)
			newattsPos.add((Integer)ts);
		
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
