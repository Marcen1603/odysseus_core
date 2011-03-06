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

import measure.windperformancercp.model.sources.ISource;


//Context
public class QueryGenerator {
		
	//Strategy
	private IQueryGenerator language;
	
	public class Aggregation{
		String type;
		String inputName;
		String outputName;
		
		public Aggregation (String t, String i, String o){
			this.type = t;
			this.inputName = i;
			this.outputName = o;
		}
		
		public String getInputName(){
			return this.inputName;
		}
		
		public String getOutputName(){
			return this.outputName;
		}
		
		public String toString(){
			return "['"+this.type+"', '"+inputName+"', '"+outputName+"']";
		}
	}
	
	public class Window{
		int size = 0;
		int advance = 0;
		String type;
		int slide = 0;
		String[] partition = null;
		
		/**
		 * Window parameter
		 * @param t type
		 */
		public Window(String t){
			this.type = t;
		}
		/**
		 * Window parameter
		 * @param t type
		 * @param s size
		 */
		public Window(String t, int s){
			this.size = s;
			this.type = t;
		}
		
		/**
		 * Window parameter
		 * @param t type
		 * @param s size
		 * @param a advance
		 */
		public Window(String t, int s,  int a){
			this.size = s;
			this.advance = a;
			this.type = t;
		}
		
		/**
		 * Window parameter
		 * @param t type
		 * @param s size
		 * @param a advance
		 * @param sl slide
		 */
		public Window(String t, int s,  int a, int sl){
			this(t,s,a);
			this.slide = sl;
		}
		
		/**
		 * Window parameter
		 * @param t type
		 * @param s size
		 * @param a advance
		 * @param par partition attributes
		 */
		public Window(String t, int s, int a, String[] par){
			this(t,s,a);
			this.partition = par;
		}

		public String toString(){
			
			if(type.equals("unbounded"))
				return "type = 'unbounded'";
			
			String res = "size = "+size+", type = '"+type+"'";
			
			if(advance > 0) res = res + ", advance = "+advance;
			if(partition!=null){
				res = res + ", partition = ['"+partition[0]+"'";
				for(int i = 1; i< partition.length;i++){
					res = res + ",'"+partition[i]+"'";
				}
				res= res+"]";
			}
			else
				if(slide > 0) res = res + ", slide = "+slide;
			return res;
		}
	}
	
	
	public QueryGenerator(IQueryGenerator lang){
		this.language = lang;
	}
	
	public OperatorResult generateCreateStream(ISource src){
		return this.language.generateCreateStream(src);
	}
	public OperatorResult generateRemoveStream(ISource src){
		return this.language.generateRemoveStream(src);
	}
	public OperatorResult generateProjection(Stream instream, int[] attIndexes, String outputName){
		return this.language.generateProjection(instream, attIndexes, outputName);
	}
	public OperatorResult generateWindow(Stream instream, Window win, String outputName){
		return this.language.generateWindow(instream, win, outputName);
	}
	public OperatorResult generateSelection(Stream instream, String predicate, String outputName){
		return this.language.generateSelection(instream, predicate, outputName);
	}
	public OperatorResult generateAggregation(Stream instream, String[] groupBy, Aggregation[] aggregations, String outputName){
		return this.language.generateAggregation(instream, groupBy, aggregations, outputName);
	}
	public OperatorResult generateRename(Stream instream, String[] newAttNames, String outputName) {
		return this.language.generateRename(instream, newAttNames, outputName);
	}
	public OperatorResult generateMap(Stream instream, String[] resNames, String[] expressions, String outputName){
		return this.language.generateMap(instream, resNames, expressions, outputName);
	}
	public OperatorResult generateJoin(ArrayList<Stream> instreams, String predicate, String outputName){
		return this.language.generateJoin(instreams, predicate, outputName);
	}
	
	public OperatorResult generateUnion(ArrayList<Stream> instreams, String outputName){
		return this.language.generateUnion(instreams, outputName);
	}

	
	//TODO
	public String generateIECQuery(IPerformanceQuery qry){
		String query = "";
		
		return query;
	}
	
}
