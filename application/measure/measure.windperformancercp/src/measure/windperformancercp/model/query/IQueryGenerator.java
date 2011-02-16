package measure.windperformancercp.model.query;

import java.util.ArrayList;

import measure.windperformancercp.model.sources.ISource;

import measure.windperformancercp.model.query.QueryGenerator.Aggregation;
import measure.windperformancercp.model.query.QueryGenerator.Window;

public interface IQueryGenerator {
	public OperatorResult generateCreateStream(ISource src);
	public OperatorResult generateRemoveStream(ISource src);
	public OperatorResult generateProjection(Stream instream, int[] attIndexes, String outputName);
	public OperatorResult generateWindow(Stream instream, Window win, String outputName);
	public OperatorResult generateSelection(Stream instream, String predicate, String outputName);
	public OperatorResult generateAggregation(Stream instream, String[] groupBy, Aggregation[] aggregations, String outputName);
	public OperatorResult generateMap(Stream instream, String[] resNames, String[] expressions, String outputName);
	public OperatorResult generateRename(Stream instream, String[] newAttNames, String outputName);
	public OperatorResult generateJoin(ArrayList<Stream> instreams, String predicate, String outputName);
	public OperatorResult generateUnion(ArrayList<Stream> instreams, String outputName);

}
