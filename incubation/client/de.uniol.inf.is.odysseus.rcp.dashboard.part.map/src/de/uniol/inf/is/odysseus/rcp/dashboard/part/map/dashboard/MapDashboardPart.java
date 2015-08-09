package de.uniol.inf.is.odysseus.rcp.dashboard.part.map.dashboard;

import java.util.Collection;
import java.util.Formatter;
import java.util.HashMap;
import java.util.List;

import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.ToolBar;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Optional;
import com.google.common.collect.Lists;

import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.physicaloperator.IPhysicalOperator;
import de.uniol.inf.is.odysseus.core.physicaloperator.IPunctuation;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;
import de.uniol.inf.is.odysseus.core.securitypunctuation.ISecurityPunctuation;
import de.uniol.inf.is.odysseus.rcp.dashboard.AbstractDashboardPart;
import de.uniol.inf.is.odysseus.rcp.dashboard.part.TableDashboardPart;
import de.uniol.inf.is.odysseus.rcp.dashboard.part.map.layer.LayerConfiguration;

public class MapDashboardPart extends AbstractDashboardPart {

	private static final Logger LOG = LoggerFactory.getLogger(TableDashboardPart.class);

	private IPhysicalOperator operator;

	private String[] attributes;
	private int[] positions;
	private boolean refreshing = false;

	private final List<Tuple<?>> data = Lists.newArrayList();

	private String attributeList = "*";
	private String operatorName = "";

	private int maxData = 10;
	private int updateInterval = 1000;
	private int layerCounter = 0;

	private HashMap<Integer, LayerConfiguration> layerMap = new HashMap<Integer, LayerConfiguration>();

	@Override
	public void createPartControl(Composite parent, ToolBar toolbar) {
		// TODO Auto-generated method stub

	}

	public int getMaxData() {
		return maxData;
	}

	public void setMaxData(int maxData) {
		this.maxData = maxData;
	}

	public int getUpdateInterval() {
		return updateInterval;
	}

	public void setUpdateInterval(int updateInterval) {
		this.updateInterval = updateInterval;
	}

	public int getLayerCounter() {
		return this.layerCounter;
	}

	public HashMap<Integer, LayerConfiguration> getLayerList() {
		return layerMap;
	}

	public void addLayerToMap(Integer id, LayerConfiguration layerToAdd) {
		this.layerMap.put(id, layerToAdd);
	}

	public void deleteLayerFromMap(Integer id) {
		// this.layerMap.containsKey(id);
		layerMap.remove(id);
		reOrderMap(id);
		decreaseLayerCounter();
	}

	public void changeLayerOrder(Integer firstId, Integer secondId) {
		LayerConfiguration tmp = layerMap.get(firstId);
		layerMap.put(firstId, layerMap.get(secondId));
		layerMap.put(secondId, tmp);
	}

	public void increaseLayerCounter() {
		layerCounter++;
	}

	private void decreaseLayerCounter() {
		layerCounter--;
	}

	private void reOrderMap(int deletedId) {
		for (int i = deletedId; i < layerCounter; i++) {
			layerMap.put(i, layerMap.get(i + 1));
		}
	}
	
	/**
	 * The name of the root operator (configuration parameter) in case of two ore more root operators.
	 * 
	 * @return the operatorName
	 */
	public String getOperatorName() {
		return operatorName;
	}

	@Override
	public void onStart(Collection<IPhysicalOperator> physicalRoots) throws Exception {
		super.onStart(physicalRoots);

		operator = determinePyhsicalRoot(physicalRoots);

		positions = determinePositions(operator.getOutputSchema(), attributes);
		refreshAttributesList(operator.getOutputSchema()); // if attributes was
															// = "*"

	}

	private IPhysicalOperator determinePyhsicalRoot(Collection<IPhysicalOperator> physicalRoots) {
		for (IPhysicalOperator p : physicalRoots) {
			if (p.getName().equals(getOperatorName())) {
				return p;
			}
		}
		LOG.info("Select first physical root.");
		return physicalRoots.iterator().next();
	}

	@Override
	public void punctuationElementRecieved(IPhysicalOperator senderOperator, IPunctuation punctuation, int port) {
	}

	@Override
	public void securityPunctuationElementRecieved(IPhysicalOperator senderOperator, ISecurityPunctuation sp,
			int port) {
	}

	//TODO Füllen und auf Karte anpassen
//	@Override
//	public void streamElementRecieved(IPhysicalOperator senderOperator, IStreamObject<?> element, int port) {
//		if( element != null && senderOperator.equals(operator)) {
//			synchronized( data ) {
//				data.add(0, (Tuple<?>) element);
//				if (data.size() > maxData) {
//					data.remove(data.size() - 1);
//				}
//			}
//			
//			if( !refreshing && tableViewer.getInput() != null) {
//				refreshing = true;
//				PlatformUI.getWorkbench().getDisplay().asyncExec(new Runnable() {
//					@Override
//					public void run() {
//						synchronized( data ) {
//							//TODO FILL
//						}
//					}
//					
//					refreshing = false;
//				
//				});
//			}
//		}
//	}
	
	private static String formatListAttributeToString(List<?> list) {
		StringBuilder sb = new StringBuilder();
		Formatter formatter = new Formatter(sb);
		
		@SuppressWarnings("unchecked")
		int[] maxLengths = getMaxLenghts((List<Tuple<?>>) list);
		
		for(int i = 0; i < list.size(); ++i) {
			if(list.get(i) instanceof Tuple) {
				Tuple<?> tuple = (Tuple<?>) list.get(i);
				Object[] attributes = tuple.getAttributes();
				for(int j = 0; j < attributes.length; ++j) {
					Object attr = attributes[j];
					formatter.format("%1$" + maxLengths[j] +"s", attr);
					if(j < attributes.length -1) {
						sb.append("\t");
					}
				}
				if(i < list.size() -1) {
					sb.append("\r\n");
				}
			} else {
				formatter.close();
				return null;
			}
		}
		formatter.close();
		return sb.toString();
		
	}
	
	/**
	 * Returns the max length of the attributes of all tuples in the list.
	 * 
	 * @param list A list of tuples with attributes.
	 * @return an array int[i] that hat the highest length of the attributes with index i of all tuples in the list.
	 */
	private static int[] getMaxLenghts(List<Tuple<?>> list) {
		if(list == null || list.size() == 0) {
			return new int[0];
		}
		Tuple<?> t = list.get(0);
		if(t == null) {
			return new int[0];
		}
		int[] maxLengths = new int[t.getAttributes().length];
		for(Tuple<?> tuple : list) {
			Object[] attributes = tuple.getAttributes();
			if(attributes.length > maxLengths.length) {
				LOG.warn("length missmatch in list of tuples");
				return new int[0];
			}
			for(int i = 0; i < attributes.length; ++i) {
				if(maxLengths[i] < attributes[i].toString().length()) {
					maxLengths[i] = attributes[i].toString().length();
				}
			}
		}
		return maxLengths;
	}
	
	private void refreshAttributesList(SDFSchema outputSchema) {
		if( attributes.length == 0 ) {
			attributes = new String[outputSchema.size()];
			for( int i = 0; i< attributes.length; i++ ) {
				attributes[i] = outputSchema.getAttribute(i).getAttributeName();
			}
		}
	}

	public static String[] determineAttributes(final String attributeList) {
		if(attributeList == null || attributeList.trim().equals("")) {
			return new String[0];
		}
		if (attributeList.trim().equalsIgnoreCase("*")) {
			return new String[0];
		}
		
		String[] attributes = attributeList.trim().split(",");
		for (int i = 0; i < attributes.length; i++) {
			attributes[i] = attributes[i].trim();
		}
		return attributes;
	}

	private static int[] determinePositions(SDFSchema outputSchema, String[] attributes2) {
		int[] positions = null;
		if( attributes2.length > 0 ) {
			positions = new int[attributes2.length];
			for (int i = 0; i < attributes2.length; i++) {
				Optional<Integer> optPosition = getPosition(outputSchema, attributes2[i]);
				if( optPosition.isPresent() ) {
					positions[i] = optPosition.get();
				} else {
					throw new RuntimeException("Could not find position of " + attributes2[i]);
				}
			}	
		} else {
			positions = new int[outputSchema.size()];
			for( int i = 0; i < positions.length; i++ ) {
				positions[i] = i;
			}
		}
		return positions;
	}

	private static Optional<Integer> getPosition(SDFSchema outputSchema, String attribute ) {
		for (int j = 0; j < outputSchema.size(); j++) {
			if (outputSchema.get(j).getAttributeName().equals(attribute)) {
				return Optional.of(j);
			}
		}
		return Optional.absent();
	}

}