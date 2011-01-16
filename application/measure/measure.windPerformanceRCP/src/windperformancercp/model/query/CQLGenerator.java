package windperformancercp.model.query;

import java.util.ArrayList;

import windperformancercp.model.query.QueryGenerator.Aggregation;
import windperformancercp.model.query.QueryGenerator.Window;
import windperformancercp.model.sources.Attribute;
import windperformancercp.model.sources.ISource;

public class CQLGenerator implements IQueryGenerator {

	@Override
	public OperatorResult generateCreateStream(ISource src){
		String query = "CREATE STREAM "+src.getStreamIdentifier()+"(";
		
		ArrayList<Attribute> srcAttr = src.getAttributeList();
		query = query + srcAttr.get(0).getName()+" ";
		query = query + srcAttr.get(0).getDataType();
		
		for(int i = 1; i< srcAttr.size();i++){
			query = query + ","+srcAttr.get(i).getName()+" ";
			query = query + srcAttr.get(i).getDataType();
		}
		query = query+") SOCKET "+src.getHost()+" : "+src.getPort()+"\n";
		
		Stream stream = new Stream(src.getStreamIdentifier(),src.getAttributeNameList());
		
		OperatorResult result = new OperatorResult(stream, query);
		
		return result;
	}
	
	@Override
	public OperatorResult generateRemoveStream(ISource src){
		String query = "REMOVE STREAM "+src.getStreamIdentifier()+"\n";
		return new OperatorResult(null, query);
	}


	@Override
	public OperatorResult generateProjection(Stream instream, int[] attIndexes,
			String outputName) {
		//throw new NotImplementedException();
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public OperatorResult generateWindow(Stream instream, Window win, String outputName) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public OperatorResult generateSelection(Stream instream, String predicate, String outputName) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public OperatorResult generateAggregation(Stream instream, String[] groupBy, Aggregation[] aggregations,
			String outputName){
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public OperatorResult generateMap(Stream instream, String[] resNames, String[] expressions, String outputName) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public OperatorResult generateRename(Stream instream, String[] newAttNames,
			String outputName) {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public OperatorResult generateJoin(ArrayList<Stream> instreams, String predicate, String outputName){
		// TODO Auto-generated method stub
		return null;
	}

}
