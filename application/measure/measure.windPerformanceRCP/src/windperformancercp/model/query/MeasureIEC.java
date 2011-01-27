package windperformancercp.model.query;

import java.util.ArrayList;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;

import windperformancercp.model.query.QueryGenerator.Aggregation;
import windperformancercp.model.sources.ISource;
import windperformancercp.model.sources.WindTurbine;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "iec_measure")
public class MeasureIEC extends APerformanceQuery {
	

	private double cutin = 3.0;
	private double eightyfivepercent = 10.0;
	private boolean pitch;
	

	String windspeedAttribute = "";
	String powerAttribute = "";
	String pressureAttribute = "";
	String temperatureAttribute = "";
	

	public MeasureIEC(String name, ArrayList<Stream> instr){
		super(name, APerformanceQuery.PMType.IEC,new ArrayList<ISource>());
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
		super.setConcernedSrc(srcList);
		extractTurbineData();
	}
	
	@Override
	public void addMember(ISource m){
		super.addMember(m);
		extractTurbineData();
	}
	
	@Override
	public void addAllMembers(ArrayList<ISource> listm){
		concernedSrc.addAll(listm);
		extractTurbineData();
	}
	
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
	public void setAssignments(ArrayList<Assignment> assigns){
		this.assignments = assigns;
		windspeedAttribute = getResponsibleAttribute("WINDSPEED");
		powerAttribute = getResponsibleAttribute("POWER");
		pressureAttribute = getResponsibleAttribute("AIRPRESSURE");
		temperatureAttribute = getResponsibleAttribute("AIRTEMPERATURE");
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

		//window
		actRes1 = Pgen.generateWindow(actRes1.getStream(), Pgen.new Window(10,1,"time"), "windowedInput");
		tmpQuery = tmpQuery +actRes1.getQuery();
		
		
		Aggregation[] aggs = new Aggregation[assignments.size()+1];
		aggs[0] = Pgen.new Aggregation("MAX","timestamp", "timestamp");
		//average
		for(int i = 0; i< assignments.size();i++){
			Assignment as = assignments.get(i);		
			aggs[i+1] = Pgen.new Aggregation("AVG",
					as.getRespStream().getIthAttName(as.getAttributeId()), 
					"avg_"+as.getRespStream().getIthAttName(as.getAttributeId()));
		}
		
		actRes1 = Pgen.generateAggregation(
				actRes1.getStream(), 
				null, 
				aggs, 
				"avgValues");
		tmpQuery = tmpQuery +actRes1.getQuery();
		
		//selection on windspeed
		actRes1 = Pgen.generateSelection(actRes1.getStream(), 
										"avg_"+windspeedAttribute+">"+(cutin-1)+" AND "+"avg_"+windspeedAttribute+"<"+(1.5*eightyfivepercent), 
										"validWSpeedSelection");
		
		tmpQuery = tmpQuery +actRes1.getQuery();
		
		//compute density
		actRes1 = Pgen.generateMap(actRes1.getStream(), 
				new String[]{"timestamp","avg_density","avg_windspeed","avg_power"},
				new String[]{"timestamp",
				"100* avg_"+pressureAttribute+"/(287.058*(avg_"+temperatureAttribute+"+237.15))",
				"avg_"+windspeedAttribute,
				"avg_"+powerAttribute},
				"avgValues_w_Density");
		tmpQuery = tmpQuery +actRes1.getQuery();
		
		//bin and normalize data via map
		if(pitch){
			actRes1 = Pgen.generateMap(actRes1.getStream(), 
					new String[]{actRes1.getStream().getIthAttName(0), "bin_id" ,"normalizedWSpeed", "normalizedPower"}, 
					new String[]{actRes1.getStream().getIthAttName(0), 
								"floor(("+actRes1.getStream().getIthAttName(2)+"*("+actRes1.getStream().getIthAttName(1)+"/1.225)^(1/3))/0.5)/2.0",
								actRes1.getStream().getIthAttName(2)+"*("+actRes1.getStream().getIthAttName(1)+"/1.225)^(1/3)",
								actRes1.getStream().getIthAttName(3)}, 
					"normalizedBinData");
		}
		else{ //stall
			actRes1 = Pgen.generateMap(actRes1.getStream(), 
					new String[]{actRes1.getStream().getIthAttName(0), "bin_id", "normalizedWSpeed", "normalizedPower"}, 
					new String[]{actRes1.getStream().getIthAttName(0), 
								"floor(("+actRes1.getStream().getIthAttName(2)+")/0.5)/2.0",
								actRes1.getStream().getIthAttName(2),
								actRes1.getStream().getIthAttName(3)+"*(1.225/"+actRes1.getStream().getIthAttName(1)+")"}, 
					"normalizedBinData");
		}
		
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
