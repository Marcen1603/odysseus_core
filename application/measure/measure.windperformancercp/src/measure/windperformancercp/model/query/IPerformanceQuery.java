package measure.windperformancercp.model.query;

import java.util.ArrayList;

import measure.windperformancercp.model.sources.IDialogResult;
import measure.windperformancercp.model.sources.ISource;

import measure.windperformancercp.model.query.APerformanceQuery.PMType;

public interface IPerformanceQuery extends IDialogResult {
	
	public void setIdentifier(String id);
	public String getIdentifier();
	public String getQueryText();
	public void setQueryText(String text);
	public boolean register();
	public boolean deregister();
	public void extractTurbineData();
	public void setPitch(boolean p);
	public boolean getPitch();
	public void setAssignment(String what, Stream who);
	public ArrayList<Assignment> getAssignments();
	public void setAssignments(ArrayList<Assignment> assigns);
	public ArrayList<Assignment> getPossibleAssignments();
	public void setWindspeedAttribute(String at);
	public String getWindspeedAttribute();
	public void setPowerAttribute(String at);
	public String getPowerAttribute();
	public void setPressureAttribute(String at);
	public String getPressureAttribute();
	public void setTemperatureAttribute(String at);
	public String getTemperatureAttribute();	
	public Stream getResponsibleStream(String what);
	public String getResponsibleAttribute(String what);
	public PMType getMethod();
	public void setConnectStat(boolean c);
	public boolean getConnectStat();
	public void setMethod(APerformanceQuery.PMType type);
	public ArrayList<ISource> getConcernedSrc();
	public void setConcernedSrc(ArrayList<ISource> srcList);
	public ArrayList<Stream> getConcernedStr();
	public void setConcernedStr(ArrayList<Stream> strList);
	public void addMember(ISource m);
	public void addAllMembers(ArrayList<ISource> listm);
	public void clearMembers();
	public ArrayList<String> generateSourceStreams();
	public ArrayList<String> generateRemoveStreams();
	public String generateQuery();
	public ArrayList<ISource> extractSourcesFromAssignments();
	OperatorResult projectStreamToAssignments(Stream str, int ts, String outputName);
}
