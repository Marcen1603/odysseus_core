package windperformancercp.model.query;

import java.util.ArrayList;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;

import windperformancercp.model.sources.ISource;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "iec_measure")
public class MeasureLangevin extends APerformanceQuery {

	QueryGenerator Pgen;
	QueryGenerator Qgen;
	
	//TODO: auf streams setzen
	public MeasureLangevin(String name, ArrayList<Stream> instr){
		super(name, APerformanceQuery.PMType.Langevin,new ArrayList<ISource>());
	}
	
	public MeasureLangevin(String name){
		super(name, APerformanceQuery.PMType.Langevin,new ArrayList<ISource>());
	}
	
	public MeasureLangevin(){
		super();
		this.setMethod(APerformanceQuery.PMType.Langevin);
	}
	
	public String generate(){
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

		//waiter for next data element
		actRes1 = Pgen.generateMap(actRes1.getStream(), 
				new String[]{"timestamp", "Vbin_id","Pbin_id","wSpeedN","PowerN","correspTimestamp"}, 
				new String[]{"timestamp","floor(("},	//TODO: und hier gehts weiter:  windSpeedWT1*(density/1.225)^(1/3))/0.5)',  'floor(realPowerWT1/6)',
				  //'windSpeedWT1*(density/1.225)^(1/3)',
				  //'realPowerWT1',
				  //'timestamp+5']}, 
				
				"waiterForNextDataElement");
		
		tmpQuery = tmpQuery +actRes1.getQuery();
		
		queryText = tmpQuery;
		return queryText;
	}

	@Override
	public void setMethod(PMType type) {
		// TODO Auto-generated method stub
	}

}
