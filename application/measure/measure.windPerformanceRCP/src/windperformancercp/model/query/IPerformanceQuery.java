package windperformancercp.model.query;

import java.util.ArrayList;

import windperformancercp.model.query.APerformanceQuery.PMType;
import windperformancercp.model.sources.ISource;

public interface IPerformanceQuery {
	
	public void setIdentifier(String id);
	public String getIdentifier();
	public String getQueryText();
	public void setQueryText(String text);
	public boolean register();
	public boolean deregister();
	public void setAssignment(String what, Stream who);
	public ArrayList<Assignment> getAssignments(PMType what);
	public Stream getResponsibleStream(String what);
	public String getMethod();
	public void setMethod(APerformanceQuery.PMType type);
	public ArrayList<ISource> getMember();
	public void addMember(ISource m);
	public void addAllMembers(ArrayList<ISource> listm);
	public void clearMembers();
	public ArrayList<OperatorResult> generateSourceStreams();
	public ArrayList<Assignment> getPossibleAssignments();
}
