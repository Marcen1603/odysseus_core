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
			query = outputName+" = project({\n attributes = [";
			int i;
			for(i = 0; i< attIndexes.length-1; i++){
				if(attIndexes[i]<instream.getAttributeNames().size()){
					query = query+"'"+instream.getIthAttName(attIndexes[i])+"',";
					streamAtts.add(instream.getIthAttName(attIndexes[i]));
				}
				else{
					//TODO: Error
				}
			}
			if(attIndexes[i]<instream.getAttributeNames().size()){
				query = query+"'"+instream.getIthAttName(attIndexes[i])+"']},\n";
				streamAtts.add(instream.getIthAttName(attIndexes[i]));}
			else{
				//TODO: Error
			}
			query = query+instream.getName()+")\n";
		}
		
		else query = outputName+" = "+instream.getName()+"\n";
		Stream stream = new Stream(outputName, streamAtts);
		OperatorResult result = new OperatorResult(stream,query);
		
		return result;
	}
	
	@Override
	public OperatorResult generateWindow(Stream instream, Window win, String outputName){
		String query = "";
		query = outputName+" = window({\n" + win.toString()+"},\n";
		query = query + instream.getName()+")\n";
		OperatorResult result = new OperatorResult(new Stream(outputName,instream.getAttributeNames()),query);
		
		return result;
	}
	
	@Override
	public OperatorResult generateSelection(Stream instream, String predicate, String outputName) {
		String query = outputName+" = select({ predicate = ";
		query = query +  " RelationalPredicate('" +predicate+"')}, \n";
		// TODO Auto-generated method stub
		query = query+instream.getName()+")\n";
		OperatorResult result = new OperatorResult(new Stream(outputName,instream.getAttributeNames()),query);
		return result;
	}

	@Override
	public OperatorResult generateAggregation(Stream instream, String[] groupBy, Aggregation[] aggregations,
			String outputName) {
		String query = "";
		ArrayList<String> streamAtts = new ArrayList<String>();
		
		query = outputName+" = aggregation({";
		if(groupBy != null){
			query = query+"group_by= ['"+groupBy[0]+"'";
			streamAtts.add(groupBy[0]);
			for(int i = 1; i<groupBy.length;i++){
				query = query+",'"+groupBy[i]+"'";
				streamAtts.add(groupBy[i]);
			}
			query = query+"],\n ";
		}
		query = query+"aggregations = [\n";
		query = query+aggregations[0].toString();
		streamAtts.add(aggregations[0].getOutputName());
		for(int i = 1; i<aggregations.length;i++){
			query = query+", \n"+aggregations[i].toString();
			streamAtts.add(aggregations[i].getOutputName());
		}
		query = query+"]},\n"+instream.getName()+")\n";

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
			query= query+"]},\n map({ expressions = [ ";
			
			query=query+"'"+expressions[0]+"'";
		
			for(int i=1; i<expressions.length;i++){
				query=query+",\n '"+expressions[i]+"'";
			}
		
			query=query+"]},\n"+instream.getName()+"))\n";
			OperatorResult result = new OperatorResult(new Stream(outputName,streamAtts),query);
			return result;
		}
		//else: Error
		return null;
	}

	@Override
	public OperatorResult generateRename(Stream instream, String[] newAttNames,	String outputName) {
		String query = "";
		
		if(newAttNames.length == instream.getAttributeNames().size()){
			ArrayList<String> streamAtts = instream.getAttributeNames();
			query = outputName+" = rename({ aliases = ['";
			int i;
			for(i=0;i< newAttNames.length-1;i++){
				query = query+newAttNames[i]+"',";
				streamAtts.set(i, newAttNames[i]);
			}
			query = query+newAttNames[i]+"']},\n";
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
	
	
	@Override
	public OperatorResult generateJoin(ArrayList<Stream> instreams, String predicate, String outputName) {
		String query = outputName+" = join(\n";
		if(!predicate.equals("")){
			query = query+"{predicate=RelationalPredicate('"+predicate+"')},\n";
		}
		
		if(instreams.size()>1){
			ArrayList<String> streamAtts = new ArrayList<String>();
		
			query=query+instreams.get(0).getName();
			streamAtts.addAll(instreams.get(0).getAttributeNames());
		
			for(int i=1;i<instreams.size();i++){
				query=query+",\n"+instreams.get(i).getName();
				streamAtts.addAll(instreams.get(i).getAttributeNames());
			}
			query = query+")\n";
		
			OperatorResult result = new OperatorResult(new Stream(outputName,streamAtts),query);
			return result;
		}
		
		else{
			//TODO Errormessage
			return null;
		}
	}

}
