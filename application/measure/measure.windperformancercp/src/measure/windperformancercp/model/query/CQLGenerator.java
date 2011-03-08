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
import measure.windperformancercp.model.sources.Attribute;
import measure.windperformancercp.model.sources.ISource;

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
	public OperatorResult generateAggregation(Stream instream, String[] groupBy, int dumpValueCnt, Aggregation[] aggregations,
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
	
	@Override
	public OperatorResult generateUnion(ArrayList<Stream> instreams, String outputName){
		// TODO Auto-generated method stub
		return null;
	}

}
