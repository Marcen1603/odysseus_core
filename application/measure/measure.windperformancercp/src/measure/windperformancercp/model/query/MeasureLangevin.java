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

import java.util.ArrayList;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

import measure.windperformancercp.model.query.QueryGenerator.Aggregation;
import measure.windperformancercp.model.sources.ISource;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "measureLangevin", propOrder = {
    "tau",
    "frequency"
})
@XmlRootElement
public class MeasureLangevin extends APerformanceQuery {
	
	private int tau = 5; 
	private int frequency = 10; //TODO
	
	
	public MeasureLangevin(MeasureLangevin copy){
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
			this.queryText = copy.getQueryText();
			this.tau = copy.getTau();
			this.frequency = copy.getFrequency();
		}
	}
	
	public MeasureLangevin(String name, ArrayList<ISource> insrc, int tau, int frequency){
		this.identifier = name;
		this.method = APerformanceQuery.PMType.Langevin;
		this.concernedSrc = insrc;
		this.tau = tau;
		this.frequency = frequency;
	}

	
	public MeasureLangevin(String name){
		super(name, APerformanceQuery.PMType.Langevin,new ArrayList<ISource>());
	}
	
	public MeasureLangevin(){
		super();
		this.setMethod(APerformanceQuery.PMType.Langevin);
	}
	
	
	@Override
	public String generateQuery(){
		String tmpQuery = "";

		OperatorResult actRes1;
		OperatorResult actRes2;
		OperatorResult actRes3;

		ArrayList<Stream> streams = new ArrayList<Stream>(); 
		//projection to only needed attributes and the timestamp
		for(int i = 0; i< concernedStr.size(); i++){
			Stream str = concernedStr.get(i);
			if(! streams.contains(str)){
				actRes1 = projectStreamToAssignments(str,timestampAttributes.get(i),"projected_"+str.getName());
				streams.add(actRes1.getStream());
				tmpQuery = tmpQuery +actRes1.getQuery();
			}
			
		}
		
		if(streams.size()>1){
			for(Stream str: streams){
			//window for join
				actRes1 = Pgen.generateWindow(str, 
						Pgen.new Window("tuple",1), 
						"windowed_"+str.getName());
				tmpQuery = tmpQuery +actRes1.getQuery();
			}
			
			//join all input streams
				actRes1 = Pgen.generateJoin(streams, "", "joinedInput");
				tmpQuery = tmpQuery +actRes1.getQuery();
		
				//compute density
				actRes1 = Pgen.generateMap(actRes1.getStream(), 
						new String[]{"timestamp","density","windspeed","power"},
						new String[]{concernedStr.get(0).getIthFullAttName(timestampAttributes.get(0)),
						"100*"+pressureAttribute+"/(287.058*("+temperatureAttribute+"+237.15))",
						windspeedAttribute,
						powerAttribute},
					"values_w_Density");
				tmpQuery = tmpQuery +actRes1.getQuery();
		}
		else{
			if(concernedStr.size()==0) return null;
			//compute density
			actRes1 = Pgen.generateMap(concernedStr.get(0), 
					new String[]{"timestamp","density","windspeed","power"},
					new String[]{"timestamp",
					"100*"+pressureAttribute+"/(287.058*("+temperatureAttribute+"+237.15))",
					windspeedAttribute,
					powerAttribute},
				"values_w_Density");
			tmpQuery = tmpQuery +actRes1.getQuery();
		}
		
		//normalize data via map and add waiter for next data element
		if(pitch){
			actRes1 = Pgen.generateMap(actRes1.getStream(), 
					new String[]{"timestamp", "Vbin_id","Pbin_id", "WSpeedN", "PowerN", "correspTimestamp"}, 
					new String[]{"timestamp", 
								"floor(windspeed*(density/1.225)^(1/3))", //"floor((windspeed*(density/1.225)^(1/3))/0.5)/2.0",
								"floor(power/160)", //"floor(power/10)",	//TODO: parametrize me!
								"windspeed*(density/1.225)^(1/3)",
								"power",
								"timestamp+"+tau}, 
					"normalizedBinData_w_nE");
		}
		else{ //stall
			actRes1 = Pgen.generateMap(actRes1.getStream(), 
					new String[]{"timestamp", "Vbin_id","Pbin_id", "WSpeedN", "PowerN", "correspTimestamp"}, 
					new String[]{"timestamp",
								"floor(windspeed)",	//"floor(windspeed/0.5)/2.0",
								"floor(power/160)", //"floor(power/10)",
								"windspeed",
								"power*(1.225/density)",
								"timestamp+"+tau}, 
					"normalizedBinData_w_nE");
		}

		tmpQuery = tmpQuery +actRes1.getQuery();
		
		//window 'waiter for next data element', window for join
		actRes1 = Pgen.generateWindow(actRes1.getStream(), 
				Pgen.new Window("time",tau+1,1), 
				"windowed_nBD_wnE");
		tmpQuery = tmpQuery +actRes1.getQuery();
		
		//project 'normalized bin data' to timestamp and power 
		actRes2 = Pgen.generateProjection(actRes1.getStream(), 
						new int[]{0,4}, 
						"tPlusTauInfo");
		tmpQuery = tmpQuery +actRes2.getQuery();

		//rename it for the join
		actRes2 = Pgen.generateRename(actRes2.getStream(), 
				new String[]{"tPlusTau", "PowerNTPlusTau"}, 
				"renamedTPlusTauInfo");
		tmpQuery = tmpQuery +actRes2.getQuery();
		
		//only take the last element, window for join
		actRes2 = Pgen.generateWindow(actRes2.getStream(), 
				Pgen.new Window("tuple",1), 
				"windowed_TPTI");
		tmpQuery = tmpQuery +actRes2.getQuery();
		
		//the join for computing the difference P_(t+tau) - P_t
		streams = new ArrayList<Stream>();
		streams.add(actRes1.getStream());
		streams.add(actRes2.getStream());
		
		actRes1 = Pgen.generateJoin(streams, 
				"correspTimestamp=tPlusTau", 
				"joinForIncrement");
		tmpQuery = tmpQuery +actRes1.getQuery();
		
		//computing the difference (for D1) and it's square (for D2)
		actRes1 = Pgen.generateMap(actRes1.getStream(), 
				new String[]{"timestamp", "Vbin_id","Pbin_id","Difference_Pt_Ptptau", "Difference_Pt_Ptptau_Square"}, 
				new String[]{"timestamp", "Vbin_id","Pbin_id","PowerNTPlusTau - PowerN","(PowerNTPlusTau - PowerN)^2"}, 
				"incrementData"); 
		tmpQuery = tmpQuery +actRes1.getQuery();
		
		//unbounded window for aggregation
		actRes1 = Pgen.generateWindow(actRes1.getStream(), 
				Pgen.new Window("unbounded"), 	 
				"windowedIncrement"); 
		tmpQuery = tmpQuery +actRes1.getQuery();
		
		
		Aggregation[] aggs = new Aggregation[3];
		aggs[0] = Pgen.new Aggregation("AVG","Difference_Pt_Ptptau", "preD1");
		aggs[1] = Pgen.new Aggregation("AVG","Difference_Pt_Ptptau_Square", "preD2");
		aggs[2] = Pgen.new Aggregation("COUNT","timestamp", "N");
		
		actRes1 = Pgen.generateAggregation(actRes1.getStream(), 
				new String[]{"Vbin_id","Pbin_id"}, 
				aggs, 
				"precomputedDriftAndDiffusionData");
		tmpQuery = tmpQuery +actRes1.getQuery();
	
		//select the bins with enough data in it
		actRes1 = Pgen.generateSelection(actRes1.getStream(), 
				"N > 100",	//TODO: parametrize me!
				"validBins");
		tmpQuery = tmpQuery +actRes1.getQuery();
		
		//compute D1, D2 and StdDev for D1
		actRes1 = Pgen.generateMap(actRes1.getStream(), 
				new String[]{"Vbin_id","Pbin_id", "D1","D2","StdDev_D1"}, 
				new String[]{"Vbin_id","Pbin_id", 
				"preD1/"+tau, //D1
				"preD2/(2.0*"+tau+")",	//D2 //TODO: parametrize data rate (here = 1)
				"(abs(2*"+frequency+"*(preD2/(2.0*"+tau+"))-(preD1/"+tau+")^2)/N)^(1/2)"}, 
				"driftAndDiffusionWithStdDevData");
		tmpQuery = tmpQuery +actRes1.getQuery();
		
		/// #### Compute Fixpoints ####
		//---- negative dots ----
		actRes2 = Pgen.generateSelection(actRes1.getStream(),
				"D1 <= 0", 
				"signSplittedNegData");
		tmpQuery = tmpQuery +actRes2.getQuery();
		
		//window for aggregation
		actRes2 = Pgen.generateWindow(actRes2.getStream(), Pgen.new Window("unbounded"), "windowedNegData");
		tmpQuery = tmpQuery +actRes2.getQuery();
		
		actRes3 = Pgen.generateAggregation(actRes2.getStream(), 
				new String[]{"Vbin_id"}, 
				new Aggregation[]{Pgen.new Aggregation("MIN","D1", "MinD1Neg")}, 
				"minNegD1");
		tmpQuery = tmpQuery +actRes3.getQuery();
		
		actRes3 = Pgen.generateRename(actRes3.getStream(), 
				new String[]{"vbin_id","MinD1Neg"}, 
				"renamedMinNegforJoining");
		tmpQuery = tmpQuery +actRes3.getQuery();
		
		streams = new ArrayList<Stream>();
		streams.add(actRes2.getStream());
		streams.add(actRes3.getStream());
		
		actRes2 = Pgen.generateJoin(streams, 
				"Vbin_id = vbin_id", 
				"signSplittedNegDataWithMin");
		tmpQuery = tmpQuery +actRes2.getQuery();
		
		//select
		actRes2 = Pgen.generateSelection(actRes2.getStream(), 
				"D1 >= MinD1Neg", 
				"selectNegData");
		tmpQuery = tmpQuery +actRes2.getQuery();
		
		
		//project
		actRes2 = Pgen.generateProjection(actRes2.getStream(),
				new int[]{0,1,2}, 
				"projectNegData");
		tmpQuery = tmpQuery +actRes2.getQuery();
		
		//rename
		actRes2 = Pgen.generateRename(actRes2.getStream(),
				new String[]{"Vbin_idNeg","Pbin_idNeg","MinD1Neg"},
				"relevantNegDataforFixPC");
		tmpQuery = tmpQuery +actRes2.getQuery();
		
		
		//++++ positive dots ++++
		actRes1 = Pgen.generateSelection(actRes1.getStream(),
				"D1 > 0", 
				"signSplittedPosData");
		tmpQuery = tmpQuery +actRes1.getQuery();
		
		//window for aggregation
		actRes1 = Pgen.generateWindow(actRes1.getStream(), Pgen.new Window("unbounded"), "windowedPosData");
		tmpQuery = tmpQuery +actRes1.getQuery();
		
		actRes3 = Pgen.generateAggregation(actRes1.getStream(), 
				new String[]{"Vbin_id"}, 
				new Aggregation[]{Pgen.new Aggregation("MIN","D1", "MinD1Pos")}, 
				"minPosD1");
		tmpQuery = tmpQuery +actRes3.getQuery();
		
		actRes3 = Pgen.generateRename(actRes3.getStream(), 
				new String[]{"vbin_id","MinD1Pos"}, 
				"renamedMinPosforJoining");
		tmpQuery = tmpQuery +actRes3.getQuery();
		
		streams = new ArrayList<Stream>();
		streams.add(actRes1.getStream());
		streams.add(actRes3.getStream());
		
		actRes1 = Pgen.generateJoin(streams, 
				"Vbin_id = vbin_id", 
				"signSplittedPosDataWithMin");
		tmpQuery = tmpQuery +actRes1.getQuery();
		
		//select
		actRes1 = Pgen.generateSelection(actRes1.getStream(), 
				"D1 <= MinD1Pos", 
				"selectPosData");
		tmpQuery = tmpQuery +actRes1.getQuery();
				
		//project
		actRes1 = Pgen.generateProjection(actRes1.getStream(),
				new int[]{0,1,2},	//TODO: korrekt? oben auch? 
				"projectPosData");
		tmpQuery = tmpQuery +actRes1.getQuery();
		
		//rename
		actRes1 = Pgen.generateRename(actRes1.getStream(),
				new String[]{"Vbin_idPos","Pbin_idPos","MinD1Pos"},
				"renamedRelevantPosDataforJoin");
		tmpQuery = tmpQuery +actRes1.getQuery();
		
		
		//join pos and neg data
		streams = new ArrayList<Stream>();
		streams.add(actRes1.getStream());
		streams.add(actRes2.getStream());
		
		actRes1 = Pgen.generateJoin(streams,
				"Vbin_idPos = Vbin_idNeg",
				"combinedDPs_MinDist");
		tmpQuery = tmpQuery +actRes1.getQuery();
		
		
		actRes1 = Pgen.generateSelection(actRes1.getStream(), 
				"Pbin_idPos <= Pbin_idNeg", 
				"selectStableFPScenario");
		tmpQuery = tmpQuery +actRes1.getQuery();
		
		
		queryText = tmpQuery;
		return queryText;
	}
	
	public void setTau(int t){
		this.tau = t;
	}
	
	public int getTau(){
		return tau;
	}
	
	public void setFrequency(int t){
		this.frequency = t;
	}
	
	public int getFrequency(){
		return frequency;
	}


}
