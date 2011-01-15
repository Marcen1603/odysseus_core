package windperformancercp.model.query;

import java.util.ArrayList;

import windperformancercp.model.query.QueryGenerator.Aggregation;
import windperformancercp.model.query.QueryGenerator.Window;
import windperformancercp.model.sources.ISource;

public class PQLGenerator implements IQueryGenerator {

	@Override
	public OperatorResult generateCreateStream(ISource src) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public OperatorResult generateRemoveStream(ISource src) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public OperatorResult generateProjection(Stream instream, int[] attIndexes, String outputName ){
		String query = "";
		
		ArrayList<String> streamAtts = new ArrayList<String>();
		if(attIndexes.length>0){
			query = outputName+" = project({ attributes = [";
			int i;
			for(i = 0; i< attIndexes.length-1; i++){
				if(attIndexes[i]<instream.getAttributes().size()){
					query = query+"'"+instream.getIthAttName(attIndexes[i])+"',";
					streamAtts.add(instream.getIthAttName(attIndexes[i]));
				}
				else{
					//TODO: Error
				}
			}
			if(attIndexes[i]<instream.getAttributes().size()){
				query = query+"'"+instream.getIthAttName(attIndexes[i])+"']},";
				streamAtts.add(instream.getIthAttName(attIndexes[i]));}
			else{
				//TODO: Error
			}
			query = query+instream.getName()+")\n";
		}
		
		else query = instream.getName()+" = "+outputName+"\n";
		Stream stream = new Stream(outputName, streamAtts);
		OperatorResult result = new OperatorResult(stream,query);
		
		return result;
	}
	
	@Override
	public OperatorResult generateWindow(Stream instream, Window win, String outputName){
		String query = "";
		query = outputName+" = window({" + win.toString()+"}, ";
		query = query + instream.getName()+")\n";
		OperatorResult result = new OperatorResult(new Stream(outputName,instream.getAttributes()),query);
		
		return result;
	}
	
	@Override
	public OperatorResult generateSelection(Stream instream, String predicate, String outputName) {
		String query = outputName+" = select({ predicate = ";
		query = query +  " RelationalPredicate('" +predicate+"')}, ";
		// TODO Auto-generated method stub
		query = query+instream.getName()+")\n";
		OperatorResult result = new OperatorResult(new Stream(outputName,instream.getAttributes()),query);
		return result;
	}

	@Override
	public OperatorResult generateAggregation(Stream instream, String[] groupBy, Aggregation[] aggregations,
			String outputName) {
		String query = "";
		ArrayList<String> streamAtts = new ArrayList<String>();
		
		query = outputName+" = aggregation({ group_by= [";
		if(groupBy.length>0){
			query = query+"'"+groupBy[0]+"'";
			streamAtts.add(groupBy[0]);
			for(int i = 1; i<groupBy.length;i++){
				query = query+",'"+groupBy[i]+"'";
				streamAtts.add(groupBy[i]);
			}
			query = query+"], aggregations = [";
			query = query+aggregations[0].toString();
			streamAtts.add(aggregations[0].getOutputName());
			for(int i = 1; i<aggregations.length;i++){
				query = query+","+aggregations[i].toString();
				streamAtts.add(aggregations[i].getOutputName());
			}
			query = query+"]},"+instream.getName()+")\n";
			
		}

		OperatorResult result = new OperatorResult(new Stream(outputName,streamAtts),query);
		
		return result;
	}

	//Ich weiss, nicht das Eleganteste, aber es tut..
	@Override
	public OperatorResult generateMap(Stream instream, String[] resNames, String[] expressions, String outputName) {
		
		if(resNames.length == expressions.length){
			ArrayList<String> streamAtts = new ArrayList<String>();
			String query = outputName+" = rename({ aliases=[ ";
			query= query+"'"+resNames[0]+"'";
			streamAtts.add(resNames[0]);
		
			for(int i=1;i<resNames.length;i++){
				query= query+",'"+resNames[i]+"'";
				streamAtts.add(resNames[i]);
			}
			query= query+"]}, map({ expressions = [ ";
			
			query=query+"'"+expressions[0]+"'";
		
			for(int i=1; i<expressions.length;i++){
				query=query+", '"+expressions[i]+"'";
			}
		
			query=query+"]},"+instream.getName()+"))\n";
			OperatorResult result = new OperatorResult(new Stream(outputName,streamAtts),query);
			return result;
		}
		//else: Error
		return null;
	}

	@Override
	public OperatorResult generateRename(Stream instream, String[] newAttNames,	String outputName) {
		String query = "";
		
		if(newAttNames.length == instream.getAttributes().size()){
			ArrayList<String> streamAtts = instream.getAttributes();
			query = outputName+" = rename({ aliases = ['";
			int i;
			for(i=0;i< newAttNames.length-1;i++){
				query = query+newAttNames[i]+"',";
				streamAtts.set(i, newAttNames[i]);
			}
			query = query+newAttNames[i]+"']},";
			streamAtts.set(i, newAttNames[i]);
			
			query = query+instream.getName()+")\n";
			OperatorResult result = new OperatorResult(new Stream(outputName,streamAtts),query);
			return result;
		}
		else{
			//TODO Errormessage
			return null;
		}
	}

}
