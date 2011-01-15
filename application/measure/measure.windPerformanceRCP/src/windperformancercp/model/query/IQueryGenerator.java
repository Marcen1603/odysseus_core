package windperformancercp.model.query;

import windperformancercp.model.query.QueryGenerator.Aggregation;
import windperformancercp.model.query.QueryGenerator.Window;
import windperformancercp.model.sources.ISource;

public interface IQueryGenerator {
	public OperatorResult generateCreateStream(ISource src);
	public OperatorResult generateRemoveStream(ISource src);
	public OperatorResult generateProjection(Stream instream, int[] attIndexes, String outputName);
	public OperatorResult generateWindow(Stream instream, Window win, String outputName);
	public OperatorResult generateSelection(Stream instream, String predicate, String outputName);
	public OperatorResult generateAggregation(Stream instream, String[] groupBy, Aggregation[] aggregations, String outputName);
	public OperatorResult generateMap(Stream instream, String[] resNames, String[] expressions, String outputName);
	public OperatorResult generateRename(Stream instream, String[] newAttNames, String outputName);

}
