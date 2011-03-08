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

import measure.windperformancercp.model.query.QueryGenerator.Aggregation;
import measure.windperformancercp.model.query.QueryGenerator.Window;
import measure.windperformancercp.model.sources.ISource;

public interface IQueryGenerator {
	public OperatorResult generateCreateStream(ISource src);
	public OperatorResult generateRemoveStream(ISource src);
	public OperatorResult generateProjection(Stream instream, int[] attIndexes, String outputName);
	public OperatorResult generateWindow(Stream instream, Window win, String outputName);
	public OperatorResult generateSelection(Stream instream, String predicate, String outputName);
	public OperatorResult generateAggregation(Stream instream, String[] groupBy, int dumpValueCnt, Aggregation[] aggregations, String outputName);
	public OperatorResult generateMap(Stream instream, String[] resNames, String[] expressions, String outputName);
	public OperatorResult generateRename(Stream instream, String[] newAttNames, String outputName);
	public OperatorResult generateJoin(ArrayList<Stream> instreams, String predicate, String outputName);
	public OperatorResult generateUnion(ArrayList<Stream> instreams, String outputName);

}
