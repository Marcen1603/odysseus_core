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
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.XmlType;

import measure.windperformancercp.model.sources.Attribute;
import measure.windperformancercp.model.sources.Attribute.AttributeType;
import measure.windperformancercp.model.sources.ISource;
import measure.windperformancercp.model.sources.WindTurbine;


@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "abstractPerformanceQuery", propOrder = {
    "identifier",
    "method",
    "connectState", 
    "concernedSrcKeys",
    "concernedStr",
    "assignments",
    "queryText",
    "strGenQueries",
    "strRemQueries",
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
	protected ArrayList<String> concernedSrcKeys;
	@XmlTransient
	protected ArrayList<Stream> concernedStr;
	@XmlTransient
	protected ArrayList<Assignment> assignments;
	@XmlTransient
	protected Timestamp starttime;
	protected boolean pitch;
	protected boolean connectState;
	
	@XmlTransient
	protected ArrayList<Integer> timestampAttributes;
	protected String windspeedAttribute = "";
	protected String powerAttribute = "";
	protected String pressureAttribute = "";
	protected String temperatureAttribute = "";
		
	protected String queryText;
	
	@XmlTransient
	protected ArrayList<String> strGenQueries;
	@XmlTransient
	protected ArrayList<String> strRemQueries;
	
	@XmlTransient
	protected QueryGenerator Pgen;
	@XmlTransient
	protected QueryGenerator Qgen;

	//TODO: was machst du denn hier??
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
			String tempAtt,
			ArrayList<String> generatorQueries){
		this(id,method,sources);
	
		this.extractSourceKeys(sources);
		this.extractTimestampAttributes(sources);
		this.setConcernedStr(streams);
		this.setQueryText(queryText);
		this.setAssignments(assigns);
		this.setPitch(pitch);
		this.setWindspeedAttribute(wsAtt);
		this.setPowerAttribute(powAtt);
		this.setPressureAttribute(prAtt);
		this.setTemperatureAttribute(tempAtt);
		this.setStrGenQueries(generatorQueries);
	}
	
	public APerformanceQuery(String id, PMType method,ArrayList<ISource> sources){
	
		this();
		
		this.setIdentifier(id);
		this.setMethod(method);
		this.extractSourceKeys(sources);
		this.extractTurbineData(sources);
		this.extractTimestampAttributes(sources);
	
		this.connectState = false;
	}
	
	public APerformanceQuery(){
		this.identifier = "";
		this.method = null;
		this.assignments = new ArrayList<Assignment>();
		this.concernedSrcKeys = new ArrayList<String>();
		//this.concernedSrc = new ArrayList<ISource>();
		this.concernedStr = new ArrayList<Stream>();
		this.timestampAttributes = new ArrayList<Integer>();
		this.pitch = false;
		this.connectState = false;
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
		this.strGenQueries = new ArrayList<String>();
		
	}
	
	@Override
	public void extractTurbineData(ArrayList<ISource> sources){
		for(ISource src: sources){
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
	
	//TODO umbenennen.
	@Override
	public QueryGenerator getSourceCreator(){
		return Qgen;
	}
	
	//TODO umbenennen. 
	@Override
	public QueryGenerator getQueryCreator(){
		return Pgen;
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


	@XmlElementWrapper(name = "concernedSourcesKeys")
	@XmlElement(name = "sourceKey")
	@Override
	public ArrayList<String> getConcernedSrcKeys(){
		return concernedSrcKeys;
	}
	
	public void extractSourceKeys(ArrayList<ISource> sources){
		ArrayList<String> sourceKeys = new ArrayList<String>();
		for(ISource s: sources){
			sourceKeys.add(s.getName());
		}
		this.concernedSrcKeys = sourceKeys;
	}
	
	@Override
	//public void setConcernedSrc(ArrayList<ISource> srcList) {
	public void setConcernedSrcKeys(ArrayList<String> srcKeyList) {
		this.concernedSrcKeys = srcKeyList;//new ArrayList<ISource>(srcList); 
	//	extractTurbineData();	//TODO: set at creation
	//	updateTimestampAttributes();
	}
	
	@XmlElementWrapper(name = "concernedStreams") 
	@XmlElement(name = "stream")
	@Override
	public ArrayList<Stream> getConcernedStr(){
		return concernedStr;
	}
	
	
	@XmlElementWrapper(name = "streamGeneratorQueryList") 
	@XmlElement(name = "generatorQuery")
	@Override
	public ArrayList<String> getStrGenQueries(){
		return strGenQueries;
	}
	
	@Override
	public void setStrRemQueries(ArrayList<String> list) {
		this.strRemQueries = list;
	}
	
	@XmlElementWrapper(name = "streamRemoverQueryList") 
	@XmlElement(name = "removerQuery")
	@Override
	public ArrayList<String> getStrRemQueries(){
		return strRemQueries;
	}
	
	@Override
	public void setStrGenQueries(ArrayList<String> list) {
		this.strGenQueries = list;
	}
	
	@Override
	public void setConcernedStr(ArrayList<Stream> strList) {
		this.concernedStr = strList;//new ArrayList<Stream>(strList);
	}
	
	@XmlElementWrapper(name = "timestampAttributeIndexList") 
	@XmlElement(name = "timestamp")
	@Override
	public ArrayList<Integer> getTimestampAttributes(){
		return timestampAttributes;
	}
	
	@Override
	public void setTimestampAttributes(ArrayList<Integer> tsal) {
		this.timestampAttributes = tsal;
	}
	
	
	@Override
	public void addMemberKey(String m){
		concernedSrcKeys.add(m);
	//	extractTurbineData();
		//updateTimestampAttributes();
	}
	
	@Override
	public void addAllMemberKeys(ArrayList<String> listm){
		concernedSrcKeys.addAll(listm);
	//	extractTurbineData();
	//	updateTimestampAttributes();
	}
	
	@Override
	public void clearMembers(){
		concernedSrcKeys.clear();
	//	updateTimestampAttributes();
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
	public ArrayList<String> generateSourceStreams(ArrayList<ISource> sources){
		ArrayList<String> qryresult = new ArrayList<String>(); 
		concernedStr.clear();
		for(ISource src: sources){
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
		strGenQueries = qryresult;
		return qryresult;
	}

	
	@Override
	public ArrayList<String> generateRemoveStreams(ArrayList<ISource> sources){
		ArrayList<String> qryresult = new ArrayList<String>(); 
		//concernedStr.clear();
		for(ISource src: sources){
			OperatorResult current = Qgen.generateRemoveStream(src);
			if(current != null){
				qryresult.add(current.getQuery());
			}
			else{
				//TODO: Fehlermeldung
			}
	//		System.out.println(current.getQuery());
		}
		strRemQueries = qryresult;
		return qryresult;
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
	public void extractTimestampAttributes(ArrayList<ISource> sources){
		boolean set = false;
		
		ArrayList<Integer> timestampPos= new ArrayList<Integer>();
	
		for(ISource src: sources){
			ArrayList<Attribute> atts = src.getAttributeList();
			set = false;
			for(int j = 0; j<atts.size();j++){
				Attribute att = atts.get(j);
				if(att.getAttType().equals(AttributeType.STARTTIMESTAMP)){
					timestampPos.add(j);
					set = true;
					break;
				}
			}
			if(!set)
				timestampPos.add(0);
		}
		setTimestampAttributes(timestampPos);
	}
	
	
	@Override
	public ArrayList<Assignment> getPossibleAssignments(ArrayList<ISource> sources){
		ArrayList<Assignment> possibilities = new ArrayList<Assignment>();
		for(Assignment a: assignments){
			for(ISource s:  sources){
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
	public ArrayList<String> extractSourcesFromAssignments(){
		ArrayList<String> sources = new ArrayList<String>();
		for(Assignment as: assignments){
			if(! sources.contains(as.getRespSource().getName()))
				sources.add(as.getRespSource().getName());
		}
		this.concernedSrcKeys = sources;
		return sources;
	}

}
