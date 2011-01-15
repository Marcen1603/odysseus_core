package windperformancercp.model.query;

import java.util.ArrayList;

import windperformancercp.model.sources.ISource;

public class MeasureIEC extends APerformanceQuery {
	
	ArrayList<Stream> inputStreams;
	ArrayList<Assignment> assignList;

	int cutin;
	int eightyfivepercent;
	QueryGenerator Pgen;
	QueryGenerator Qgen;
	

	public MeasureIEC(String name, ArrayList<Stream> instr){
		super(name, APerformanceQuery.PMType.IEC,new ArrayList<ISource>());
		Pgen = new QueryGenerator(new PQLGenerator());
		Qgen = new QueryGenerator(new CQLGenerator());
	}
	
	public String generate(){
		
		OperatorResult actRes;
		
		//for all inputstreams:
		//TODO: projection
		//TODO: window
		//	iff mmstream consists of more than one stream: join
		//TODO: average
		
		//compute density: map
		//join all data
		//bin and normalize data via map
		//aggregate

		
		return queryText;
	}

}
