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

/**
 * Standard implementation of a performance query 
 * @author Diana von Gallera
 *
 */
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
	//the name for the user
	protected String identifier;
	//the type of the performance query
	protected PMType method;
	//the names of the involved sources
	@XmlTransient
	protected ArrayList<String> concernedSrcKeys;
	//the resulting data stream sources
	@XmlTransient
	protected ArrayList<Stream> concernedStr;
	//the assignments between source and stream attributes
	@XmlTransient
	protected ArrayList<Assignment> assignments;
	
	//a timestamp for query starting for possible later purposes
	//@XmlTransient
	//protected Timestamp starttime;
	protected boolean pitch;
	//the connection state to the dsms
	protected boolean connectState;
	
	//the names of the corresponding attributes in the corresponding sources
	@XmlTransient
	protected ArrayList<Integer> timestampAttributes;
	protected String windspeedAttribute = "";
	protected String powerAttribute = "";
	protected String pressureAttribute = "";
	protected String temperatureAttribute = "";
	
	//the resulting query text
	protected String queryText;
	
	//the source registration/create stream query text
	@XmlTransient
	protected ArrayList<String> strGenQueries;
	//the source  deristration/remove stream query text
	@XmlTransient
	protected ArrayList<String> strRemQueries;
	
	//the pql query generator for main query
	@XmlTransient
	protected QueryGenerator Pgen;
	//the cql query generator for stream creation and deletion
	@XmlTransient
	protected QueryGenerator Qgen;

	//Das geht sicher auch anders
	//possibilites for query methody until now 
	public enum PMType{
		IEC,
		Langevin
	}
	/**
	 * Constructor for Abstract Performance Measurement
	 * @param id The name.
	 * @param method The type of performance query.
	 * @param sources The involved sources.
	 * @param streams The resulting data stream sources.
	 * @param queryText The resulting query text.
	 * @param assigns The assignments between source attributes and needed attributes.
	 * @param pitch Power control type of wind turbine for normalization.
	 * @param wsAtt Name of windspeed attribute.
	 * @param powAtt Name of power attribute.
	 * @param prAtt Name of pressure attribute.
	 * @param tempAttName of temperature attribute.
	 * @param generatorQueries Generator queries for data stream source generation.
	 * @param removerQueries Remover queries for data stream source deletion.
	 */
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
			ArrayList<String> generatorQueries,
			ArrayList<String> removerQueries){
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
		this.setStrRemQueries(removerQueries);
	}
	
	public APerformanceQuery(String id, PMType method,ArrayList<ISource> sources){
	
		this();
		
		this.setIdentifier(id);
		this.setMethod(method);
		this.extractSourceKeys(sources);
		this.extractTurbineData(sources);
		this.extractTimestampAttributes(sources);
		this.generateSourceStreams(sources);
		this.generateRemoveStreams(sources);
	
		this.connectState = false;
	}
	//zero arg constructor for serialization (JAXB).
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
		this.strRemQueries = new ArrayList<String>();
	}
	
	/**
	 * Get the power control type from first wind turbine.
	 */
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
	
	/**
	 * Returns the stream to the given kind of attribute
	 */
	@Override
	public Stream getResponsibleStream(String what){
		for(Assignment a: assignments){
			if(a.getAttType().toString().equalsIgnoreCase(what))
				return a.getRespStream();
		}
		return null;
	}
	
	/**
	 * Returns the name of the attribute which is responsible for the given kind
	 */
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
	public void setConcernedSrcKeys(ArrayList<String> srcKeyList) {
		this.concernedSrcKeys = srcKeyList; 
	//	extractTurbineData();	//TODO: set at creation
	//	updateTimestampAttributes();
	}
	
	
	@Override
	public void setConcernedStr(ArrayList<Stream> strList) {
		this.concernedStr = strList;
	}
	
	/**
	 * Returns the concerned streams
	 */
	@XmlElementWrapper(name = "concernedStreams") 
	@XmlElement(name = "stream")
	@Override
	public ArrayList<Stream> getConcernedStr(){
		return concernedStr;
	}
	
	/**
	 * Returns the list of stream generation/access queries
	 */
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
	
	/**
	 * Returns the list of stream remove/deaccess queries
	 */
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
	
	/**
	 * Add a key of a new concerned source
	 */
	@Override
	public void addMemberKey(String m){
		concernedSrcKeys.add(m);
	//	extractTurbineData();
		//updateTimestampAttributes();
	}
	
	/**
	 * Adds a list of keys of new concerned sources
	 */
	@Override
	public void addAllMemberKeys(ArrayList<String> listm){
		concernedSrcKeys.addAll(listm);
	//	extractTurbineData();
	//	updateTimestampAttributes();
	}
	
	/**
	 * Clears the concerned sources key list
	 */
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
	
	/**
	 * Searches the needed attributes from the given assignments
	 */
	@Override
	public void setAssignments(ArrayList<Assignment> assigns){
		this.assignments = assigns;
		windspeedAttribute = getResponsibleAttribute("WINDSPEED");
		powerAttribute = getResponsibleAttribute("POWER");
		pressureAttribute = getResponsibleAttribute("AIRPRESSURE");
		temperatureAttribute = getResponsibleAttribute("AIRTEMPERATURE");
	}
	
	/**
	 * Sets the name of corresponding wind speed attribute 
	 */
	@Override
	public void setWindspeedAttribute(String at){
		this.windspeedAttribute = at;
	}
	
	@Override
	public String getWindspeedAttribute(){
		return windspeedAttribute;
	}
	
	/**
	 * Sets the name of corresponding power attribute 
	 */
	@Override
	public void setPowerAttribute(String at){
		this.powerAttribute = at;
	}
	
	@Override
	public String getPowerAttribute(){
		return powerAttribute;
	}
	
	/**
	 * Sets the name of corresponding pressure attribute 
	 */
	@Override
	public void setPressureAttribute(String at){
		this.pressureAttribute = at;
	}
	
	@Override
	public String getPressureAttribute(){
		return pressureAttribute;
	}
	
	/**
	 * Sets the name of corresponding temperature attribute 
	 */
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
	
	/**
	 * Produces the data stream source generation query strings for each source
	 * via the set CGen 
	 */
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

	/**
	 * Produces the data stream source deletion query strings for each source
	 * via the set CGen 
	 */
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
	
	/**
	 * Projection query generation right after creation to only needed attributes.
	 */
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
	
	/**
	 * Searches timestamp attributes in source attributes and sets the actual timestamp attribute of this query.
	 */
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
	
	/**
	 * Compute cartesian product for all right possibilities to present them to the user for selection
	 */
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
	
	/**
	 * Return the sources keys from list of assignments
	 */
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
