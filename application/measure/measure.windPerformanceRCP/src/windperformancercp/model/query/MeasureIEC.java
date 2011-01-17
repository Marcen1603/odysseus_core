package windperformancercp.model.query;

import java.util.ArrayList;

import windperformancercp.model.query.QueryGenerator.Aggregation;
import windperformancercp.model.sources.ISource;

public class MeasureIEC extends APerformanceQuery {
	
	ArrayList<Stream> inputStreams;
	ArrayList<Assignment> assignList;

	int cutin;
	int eightyfivepercent;
	
	//TODO: auf streams setzen
	public MeasureIEC(String name, ArrayList<Stream> instr){
		super(name, APerformanceQuery.PMType.IEC,new ArrayList<ISource>());
	}
	
	public MeasureIEC(String name){
		super(name, APerformanceQuery.PMType.IEC,new ArrayList<ISource>());
		Pgen = new QueryGenerator(new PQLGenerator());
		Qgen = new QueryGenerator(new CQLGenerator());
	}
	
	public String generate(){
		
		OperatorResult actRes;
		ArrayList<String> dummyAtts = new ArrayList<String>();
		dummyAtts.add("timestamp");
		dummyAtts.add("avg_ws");
		dummyAtts.add("avg_power");
		dummyAtts.add("density");
		
		
		Stream dummyStr = new Stream("dummy",dummyAtts);
		//for all inputstreams:
		//TODO: projection
		//TODO: window
		//	iff mmstream consists of more than one stream: join
		//TODO: average
		
		//compute density: map
		//join all data
		
		
		//bin and normalize data via map
		actRes = Pgen.generateMap(dummyStr, new String[]{"timestamp","bin_Id","normalizedWSpeed","normalizedPower"}, 
				new String[]{dummyStr.getIthAttName(0),
				"floor(("+dummyStr.getIthAttName(1)+"*("+dummyStr.getIthAttName(3)+"/1225)^(1/3))/0.5",
				dummyStr.getIthAttName(1)+"*("+dummyStr.getIthAttName(3)+"/1225)^(1/3)",
				dummyStr.getIthAttName(2)
				}, "binnedData");
		
		queryText= queryText+actRes.getQuery();
		
		//aggregate
		Aggregation agg1 = Pgen.new Aggregation("AVG",actRes.getStream().getIthAttName(2),"final"+actRes.getStream().getIthAttName(2));
		Aggregation agg2 = Pgen.new Aggregation("AVG",actRes.getStream().getIthAttName(3),"final"+actRes.getStream().getIthAttName(3));
		
		actRes = Pgen.generateAggregation(actRes.getStream(), 
				new String[]{actRes.getStream().getIthAttName(0),actRes.getStream().getIthAttName(1)},
				new Aggregation[]{agg1,agg2}, "final");
		
		queryText= queryText+actRes.getQuery();
		
		return queryText;
	}


}
