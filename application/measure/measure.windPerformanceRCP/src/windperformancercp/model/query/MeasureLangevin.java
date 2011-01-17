package windperformancercp.model.query;

import java.util.ArrayList;

import windperformancercp.model.sources.ISource;

public class MeasureLangevin extends APerformanceQuery {
	
	ArrayList<Stream> inputStreams;
	ArrayList<Assignment> assignList;

	QueryGenerator Pgen;
	QueryGenerator Qgen;
	
	//TODO: auf streams setzen
	public MeasureLangevin(String name, ArrayList<Stream> instr){
		super(name, APerformanceQuery.PMType.Langevin,new ArrayList<ISource>());
		Pgen = new QueryGenerator(new PQLGenerator());
		Qgen = new QueryGenerator(new CQLGenerator());
	}
	
	public MeasureLangevin(String name){
		super(name, APerformanceQuery.PMType.Langevin,new ArrayList<ISource>());
		Pgen = new QueryGenerator(new PQLGenerator());
		Qgen = new QueryGenerator(new CQLGenerator());
	}
	
	public String generate(){
	
		return queryText;
	}

	@Override
	public void setMethod(PMType type) {
		// TODO Auto-generated method stub
	}

}
