package windperformancercp.model.query;

import java.util.ArrayList;

import windperformancercp.model.sources.ISource;

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
		
		public Window(String t){
			this.type = t;
		}
		
		public Window(int s, String t){
			this.size = s;
			this.type = t;
		}
		
		public Window(int s, String t, int a){
			this.size = s;
			this.advance = a;
			this.type = t;
		}
		
		public Window(int s, String t, int a, int sl){
			this(s,t,a);
			this.slide = sl;
		}
		
		public Window(int s, String t, int a, String[] par){
			this(s,t,a);
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

	
	//TODO
	public String generateIECQuery(IPerformanceQuery qry){
		String query = "";
		
		return query;
	}
	
}
