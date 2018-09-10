package de.uniol.inf.is.odysseus.mining.weka.frequentitem;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import weka.core.Attribute;
import weka.core.DenseInstance;
import weka.core.Instance;
import weka.core.Instances;
import weka.core.OptionHandler;
import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.metadata.ITimeInterval;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;
import de.uniol.inf.is.odysseus.mining.frequentitem.IFrequentPatternMiner;
import de.uniol.inf.is.odysseus.mining.frequentitem.Pattern;
import de.uniol.inf.is.odysseus.mining.weka.mapping.WekaAttributeResolver;

public class WekaFrequentPatternMiner<M extends ITimeInterval> implements IFrequentPatternMiner<M> {

	private IWekaFPM<M> associator;
	private WekaAttributeResolver war;

	@Override
	public void init(SDFSchema inputschema, int support) {
		this.war = new WekaAttributeResolver(inputschema);

	}
	
	private ArrayList<Attribute> createWekaAttributes(Set<String> allNames){
		ArrayList<Attribute> wekaAttributes = new ArrayList<>();
		for(String name : allNames){
			Attribute wekaAttribute = new Attribute(name, Arrays.asList("FALSE", "TRUE"));
			wekaAttributes.add(wekaAttribute);
		}
		return wekaAttributes;
	}

	private void fillNames(List<List<Tuple<M>>> transactions, Map<Integer, TreeSet<String>> matrix, Set<String> allNames){
		int id = 0;
		for (List<Tuple<M>> transaction : transactions) {
			for (Tuple<M> tuple : transaction) {
				String name = tuple.getAttribute(0);				
				if(!matrix.containsKey(id)){
					matrix.put(id, new TreeSet<String>());
				}
				Set<String>  atts = matrix.get(id);
				atts.add(name);			
				allNames.add(name);				
			}
			id++;
		}
	}
	
	private Instances createInstances(ArrayList<Attribute> wekaAttributes, Map<Integer, TreeSet<String>> matrix){
		Instances instances = new Instances(this.war.getSchema().getURI(), wekaAttributes, 10);
		for (Integer row : matrix.keySet()) {
			Instance inst = new DenseInstance(wekaAttributes.size());			
			for (Attribute attr : wekaAttributes) {
				if(matrix.get(row).contains(attr.name())){
					inst.setValue(attr, "TRUE");
				}
			}		
			inst.setDataset(instances);
			instances.add(inst);
		}
		return instances;
	}
	@Override
	public List<Pattern<M>> createFrequentSets(List<List<Tuple<M>>> transactions, M metadata) {
		// get all names since weka needs them...

		// key: transactions
		// val: used attributes
		Map<Integer, TreeSet<String>> matrix = new HashMap<Integer, TreeSet<String>>();
		Set<String> allNames = new HashSet<>();
		
		fillNames(transactions, matrix, allNames);
		
		// ok - our instance has names-size attributes (verticalize tuples)
		ArrayList<Attribute> wekaAttributes = createWekaAttributes(allNames);

		Instances instances = createInstances(wekaAttributes, matrix);
		try {
			this.associator.buildAssociations(instances);
			return this.associator.getItemSets(metadata);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return new ArrayList<>();
	}

	@Override
	public void setOptions(Map<String, String> options) {
		try {
			String modelSmall = options.get("model");
			String model = modelSmall.toUpperCase();
			if (model != null) {
				switch (model) {
				case "FPGROWTH":
					FPGrowthWeka<M> fpg = new FPGrowthWeka<M>();
					fpg.setFindAllRulesForSupportLevel(true);				
					this.associator = fpg;
					break;
				case "APRIORI":
					this.associator = new AprioriWeka<M>();
					break;
				default:
					throw new IllegalArgumentException("There is no classifier model called " + modelSmall + "!");
				}
				if (options.containsKey("arguments")) {
					String[] wekaoptions = weka.core.Utils.splitOptions(options.get("arguments"));
					if (this.associator instanceof OptionHandler) {
						((OptionHandler) associator).setOptions(wekaoptions);
					}
				}
			} else {
				throw new IllegalArgumentException("Parameter \"model\" is not defined!");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public String getName() {
		return "weka";
	}

	@Override
	public List<String> getAlgorithmNames() {
		return Arrays.asList("FPGROWTH", "APRIORI");
	}

}
