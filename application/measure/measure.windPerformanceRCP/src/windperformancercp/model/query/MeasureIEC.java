package windperformancercp.model.query;

import java.util.ArrayList;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

import windperformancercp.model.query.QueryGenerator.Aggregation;
import windperformancercp.model.sources.ISource;
import windperformancercp.model.sources.WindTurbine;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "measureIEC", propOrder = {
    "cutin",
    "eightyfivepercent"
})
@XmlRootElement
public class MeasureIEC extends APerformanceQuery {
	

	private double cutin = 0;
	private double eightyfivepercent = 0;
	
	public MeasureIEC(MeasureIEC copy){
		this();
		if(copy != null){
			this.identifier = copy.getIdentifier();
			this.concernedSrc = copy.getConcernedSrc();
			this.concernedStr = copy.getConcernedStr();
			this.queryText = copy.getQueryText();
			this.assignments = copy.getAssignments();
			this.pitch = copy.getPitch();
			this.windspeedAttribute = copy.getWindspeedAttribute();
			this.powerAttribute = copy.getPowerAttribute();
			this.pressureAttribute = copy.getPressureAttribute();
			this.temperatureAttribute = copy.getTemperatureAttribute();
		
			this.cutin = copy.getCutin();
			this.eightyfivepercent = copy.getEightyfivepercent();
			this.queryText = copy.getQueryText();
		}
			
	}
	
	
	public MeasureIEC(String name, ArrayList<ISource> insrc){
		super(name, APerformanceQuery.PMType.IEC, insrc);
		extractTurbineData();
	}
	
	
	public MeasureIEC(){
		super();
		this.setMethod(APerformanceQuery.PMType.IEC);
	}
	
	public MeasureIEC(String name){
		super(name, APerformanceQuery.PMType.IEC,new ArrayList<ISource>());
	}
	
	@Override
	public void setConcernedSrc(ArrayList<ISource> srcList) {
		concernedSrc = srcList;
		extractTurbineData();
	}
	
	@Override
	public void addMember(ISource m){
		concernedSrc.add(m);
		extractTurbineData();
	}
	
	@Override
	public void addAllMembers(ArrayList<ISource> listm){
		concernedSrc.addAll(listm);
		extractTurbineData();
	}
	
	@Override
	public void extractTurbineData(){
		for(ISource src: concernedSrc){
			if(src.isWindTurbine()){				
				cutin = ((WindTurbine)src).getCutInWS();
				eightyfivepercent = ((WindTurbine)src).getEightyFiveWS();
				pitch = (((WindTurbine)src).getPowerControl()==0);
				break;
			}
		}
	}
	
	public void setCutin(double d){
		this.cutin = d;
	}
	
	public double getCutin(){
		return this.cutin;
	}
	
	public void setEightyfivepercent(double d){
		this.eightyfivepercent = d;
	}
	
	public double getEightyfivepercent(){
		return this.eightyfivepercent;
	}
	
	@Override
	public String generateQuery(){
		//TODO: neue methode, die den gesamten namen eines attributes angibt: streamname.attributname
		
		String tmpQuery = "";

		OperatorResult actRes1;

		ArrayList<Stream> streams = new ArrayList<Stream>();
		
		//projection to only needed attributes
		for(Stream str: concernedStr){
			if(! streams.contains(str)){
				actRes1 = projectStreamToAssignments(str,"projected_"+str.getName());
				streams.add(actRes1.getStream());
				tmpQuery = tmpQuery +actRes1.getQuery();
			}
		}
		
		//join all input streams
		actRes1 = Pgen.generateJoin(streams, "", "joinedInput");
		tmpQuery = tmpQuery +actRes1.getQuery();

		//window for aggregation
		actRes1 = Pgen.generateWindow(actRes1.getStream(), Pgen.new Window(600,"time",1,10), "windowedInput");
		tmpQuery = tmpQuery +actRes1.getQuery();
		
		//The IEC wants 10 minutes (=600 seconds)
		Aggregation[] aggs = new Aggregation[assignments.size()+1];
		aggs[0] = Pgen.new Aggregation("MAX",concernedStr.get(0).getName()+".timestamp", "timestamp");
		//generate aggregations
		for(int i = 0; i< assignments.size();i++){
			Assignment as = assignments.get(i);		
			aggs[i+1] = Pgen.new Aggregation("AVG",
					as.getRespStream().getIthAttName(as.getAttributeId()), 
					"avg_"+as.getRespStream().getIthAttName(as.getAttributeId()));
		}
		
		//now average the values of the constrained data stream 
		actRes1 = Pgen.generateAggregation(
				actRes1.getStream(), 
				null, //no group-by 
				aggs, 
				"avgValues");
		tmpQuery = tmpQuery +actRes1.getQuery();
		
		//afterwards select valid bins for iec curve from interval [v_cutin-1, 1.5*v_rated0.85], here [2,19]
		actRes1 = Pgen.generateSelection(actRes1.getStream(), 
										"avg_"+windspeedAttribute+">"+(cutin-1)+" AND "+"avg_"+windspeedAttribute+"<"+(1.5*eightyfivepercent), 
										"validWSpeedSelection");
		
		tmpQuery = tmpQuery +actRes1.getQuery();
			
		//compute density, bin and normalize data via map
		if(pitch){
			
			
			actRes1 = Pgen.generateMap(actRes1.getStream(), 
					new String[]{"timestamp", "bin_id" ,"normalizedWSpeed", "normalizedPower"}, 
					new String[]{"timestamp", 
								"floor((avg_"+windspeedAttribute+"*(100* avg_"+pressureAttribute+"/(287.058*(avg_"+temperatureAttribute+"+237.15))/1.225)^(1/3))/0.5)/2.0",
								"avg_"+windspeedAttribute+"*(100* avg_"+pressureAttribute+"/(287.058*(avg_"+temperatureAttribute+"+237.15))/1.225)^(1/3)",
								"avg_"+powerAttribute}, 
					"normalizedBinData");
		}
		else{ //stall
			actRes1 = Pgen.generateMap(actRes1.getStream(), 
					new String[]{"timestamp", "bin_id", "normalizedWSpeed", "normalizedPower"}, 
					new String[]{"timestamp", 
								"floor((avg_"+windspeedAttribute+")/0.5)/2.0",
								"avg_"+windspeedAttribute,
								"avg_"+powerAttribute+"*(1.225/100* avg_"+pressureAttribute+"/(287.058*(avg_"+temperatureAttribute+"+237.15)))"}, 
					"normalizedBinData");
		}
		
		tmpQuery = tmpQuery +actRes1.getQuery();
		
		//window for aggregation
		actRes1 = Pgen.generateWindow(actRes1.getStream(), Pgen.new Window("unbounded"), "windowedPreResult");
		tmpQuery = tmpQuery +actRes1.getQuery();
		
		//final aggregation
		actRes1 = Pgen.generateAggregation(actRes1.getStream(), 
				new String[]{actRes1.getStream().getIthAttName(1)}, 
				new Aggregation[]{
					Pgen.new Aggregation("MAX",actRes1.getStream().getIthAttName(0), "final_timestamp"),
					Pgen.new Aggregation("AVG",actRes1.getStream().getIthAttName(2), "final_WSpeed"),
					Pgen.new Aggregation("AVG",actRes1.getStream().getIthAttName(3), "final_Power")
					}, 
				"IECPlotData");
				
		tmpQuery = tmpQuery +actRes1.getQuery();
	
		queryText= tmpQuery;
		
		return queryText;
	}


}
