/*******************************************************************************
 * Copyright 2012 The Odysseus Team
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/
package de.uniol.inf.is.odysseus.rcp.dashboard.part;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.eclipse.jface.layout.TableColumnLayout;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.CellLabelProvider;
import org.eclipse.jface.viewers.ColumnWeightData;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.jface.viewers.ViewerCell;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.FontData;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.ToolBar;
import org.eclipse.ui.PlatformUI;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Optional;
import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.metadata.IStreamObject;
import de.uniol.inf.is.odysseus.core.physicaloperator.IPhysicalOperator;
import de.uniol.inf.is.odysseus.core.physicaloperator.IPunctuation;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;
import de.uniol.inf.is.odysseus.core.securitypunctuation.ISecurityPunctuation;
import de.uniol.inf.is.odysseus.rcp.dashboard.AbstractDashboardPart;

public class KeyedTableDashboardPart extends AbstractDashboardPart {

	private static final Logger LOG = LoggerFactory.getLogger(KeyedTableDashboardPart.class);

	private static final int COLOR_AGE_STEPS = 10;
	
	private IPhysicalOperator operator;

	private TableViewer tableViewer;
	private TableColumnLayout tableColumnLayout;
	private Composite tableComposite;
	private Font titleFont;
	
	private String[] attributes;
	private int[] positions;
	private boolean refreshing = false;

	private final Map<Integer, Long> hashTimestampMap = Maps.newHashMap();
	private final Map<Integer, Integer> hashPositionMap = Maps.newHashMap();
	private final List<Tuple<?>> tuplesForTable = Lists.newLinkedList();
	
	private String attributeList = "*";
	private int maxData = 10;
	private String title = "";
	private List<Color> ageColors = Lists.newArrayList();
	private boolean showAge = false;
	private long maxAgeMillis = 5000;
	
	private List<String> keyAttributes = Lists.newLinkedList();
	private int[] keyAttributeIndices = new int[0];
	
	public String getKeyAttributes() {
		return attributeListToString(keyAttributes);
	}

	private static String attributeListToString(List<String> attrs) {
		StringBuilder sb = new StringBuilder();
		for( int i = 0; i < attrs.size(); i++) {
			sb.append( attrs.get(i) );
			if( i < attrs.size() - 1) {
				sb.append(", ");
			}
		}
		return sb.toString();
	}

	public void setKeyAttributes(String keyAttributeList) {
		if( Strings.isNullOrEmpty(keyAttributeList)) {
			synchronized( tuplesForTable ) {
				clearData();
			}
		}
		String[] attrs = keyAttributeList.split("\\,");

		keyAttributes.clear();
		for( String attr : attrs ) {
			if( !keyAttributes.contains(attr) && !Strings.isNullOrEmpty(attr)) {
				keyAttributes.add(attr.trim());
			}
		}
		updateKeyAttributeIndex();
		clearData();
	}
	
	public long getMaxAgeMillis() {
		return maxAgeMillis;
	}

	public void setMaxAgeMillis(long maxAgeMillis) {
		if( maxAgeMillis >= 0 ) {
			this.maxAgeMillis = maxAgeMillis;
		}
	}

	private void clearData() {
		tuplesForTable.clear();
		hashPositionMap.clear();
		hashTimestampMap.clear();
	}

	private void updateKeyAttributeIndex() {
		keyAttributeIndices = new int[0];
		if( !keyAttributes.isEmpty() && operator != null) {
			keyAttributeIndices = new int[keyAttributes.size()];
			for( int i = 0; i < keyAttributes.size(); i++ ) {
				int index = getAttributeIndex(keyAttributes.get(i).trim());
				if( index != -1 ) {
					keyAttributeIndices[i] = index;
				}
			}
		}
	}

	private int getAttributeIndex(String keyAttribute) {
		for( int i = 0; i < operator.getOutputSchema().getAttributes().size(); i++ ) {
			SDFAttribute attribute = operator.getOutputSchema().get(i);
			
			if(attribute.getAttributeName().equals(keyAttribute)) {
				return i;
			}
		}
		
		return -1;
	}

	public String getAttributeList() {
		return attributeList;
	}

	public void setAttributeList(String attributeList) {
		this.attributeList = attributeList;
	}

	public int getMaxData() {
		return maxData;
	}

	public void setMaxData(int maxData) {
		this.maxData = maxData;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	@Override
	public void createPartControl(Composite parent, ToolBar toolbar) {
		if (Strings.isNullOrEmpty(attributeList)) {
			new Label(parent, SWT.NONE).setText("Attribute List is invalid!");
			return;
		}
		
		ageColors = determineAgeColors();
		attributes = determineAttributes(attributeList);
		
		Composite topComposite = new Composite(parent, SWT.NONE);
		topComposite.setLayoutData(new GridData(GridData.FILL_BOTH));
		topComposite.setLayout(new GridLayout(1, false));
		topComposite.setBackground(Display.getCurrent().getSystemColor(SWT.COLOR_WHITE));
		
		if( !Strings.isNullOrEmpty(title)) {
			createLabel(topComposite);
		}
		
		tableComposite = new Composite(topComposite, SWT.NONE);
		tableComposite.setLayoutData(new GridData(GridData.FILL_BOTH));
		tableColumnLayout = new TableColumnLayout();
		tableComposite.setLayout(tableColumnLayout);

		tableViewer = new TableViewer(tableComposite, SWT.BORDER | SWT.FULL_SELECTION);
		final Table table = tableViewer.getTable();
		table.setLayoutData(new GridData(GridData.FILL_BOTH));
		table.setLinesVisible(true);
		table.setHeaderVisible(true);
		
		parent.layout();
	}
	
	private List<Color> determineAgeColors() {
		List<Color> colors = Lists.newArrayList();
		for( int i = 0; i < COLOR_AGE_STEPS; i++ ) {
			float factor = (float)i / (float)COLOR_AGE_STEPS;
			
			int r = (int)(255 - (255 * factor));
			int g = (int)(255 * factor);
			int b = (int)(255 * factor);
			colors.add( new Color(Display.getCurrent(), r, g, b));
		}
		return colors;
	}

	private void createLabel(Composite topComposite) {
		Label label = new Label(topComposite, SWT.BOLD);
		label.setText(title);
		label.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		label.setAlignment(SWT.CENTER);
		label.setBackground(Display.getCurrent().getSystemColor(SWT.COLOR_WHITE));
		
		titleFont = createBoldFont(label.getFont());
		label.setFont(titleFont);
	}

	private static Font createBoldFont(Font baseFont) {
		FontData[] fontData = baseFont.getFontData();
		fontData[0].setStyle(SWT.BOLD);
		return new Font(Display.getCurrent(), fontData[0]);
	}
	
	@Override
	public void dispose() {
		if( titleFont != null ) {
			titleFont.dispose();
		}
		
		disposeAgeColors();
		
		super.dispose();
	}

	private void disposeAgeColors() {
		for( Color color : ageColors ) {
			color.dispose();
		}
		ageColors.clear();
	}

	@Override
	public void onLoad(Map<String, String> saved) {
		attributeList = saved.get("Attributes");
		maxData = Integer.valueOf(saved.get("MaxData"));
		title = saved.get("Title");
		showAge = Boolean.valueOf(saved.get("showAge"));
		maxAgeMillis = tryConvertToLong(saved.get("maxAge"));
		
		setKeyAttributes(saved.get("KeyAttribute"));
		
		updateKeyAttributeIndex();
	}

	private Long tryConvertToLong(String longString) {
		try {
			return Long.valueOf(longString);
		} catch( Throwable ignore ) {
			return maxAgeMillis;
		}
	}

	@Override
	public Map<String, String> onSave() {
		Map<String, String> toSaveMap = Maps.newHashMap();
		toSaveMap.put("Attributes", attributeList);
		toSaveMap.put("MaxData", String.valueOf(maxData));
		toSaveMap.put("Title", title);
		toSaveMap.put("KeyAttribute", getKeyAttributes());
		toSaveMap.put("showAge", String.valueOf(showAge));
		toSaveMap.put("maxAge", String.valueOf(maxAgeMillis));
		return toSaveMap;
	}

	@Override
	public void onStart(Collection<IPhysicalOperator> physicalRoots) throws Exception {
		super.onStart(physicalRoots);

		if (physicalRoots.size() > 1) {
			LOG.warn("Table DashboardPart only supports one query!");
		}

		operator = physicalRoots.iterator().next();
		positions = determinePositions(operator.getOutputSchema(), attributes);
		updateKeyAttributeIndex();
		refreshAttributesList( operator.getOutputSchema() ); // if attributes was = "*"
		
		deleteColumns();
		final int colCount = positions.length;
		for (int i = 0; i < colCount; i++) {

			final int fi = i;
			
			final TableViewerColumn column = new TableViewerColumn(tableViewer, SWT.NONE);
			column.getColumn().setText(attributes[i]);
			tableColumnLayout.setColumnData(column.getColumn(), new ColumnWeightData(5, 25, true));
			column.setLabelProvider(new CellLabelProvider() {
				@Override
				public void update(ViewerCell cell) {
					final Tuple<?> tuple = (Tuple<?>) cell.getElement();
					final Object attrValue = tuple.getAttribute(positions[fi]);
					cell.setText(attrValue != null ? attrValue.toString() : "null");
					
					if( !keyAttributes.isEmpty() && keyAttributes.contains(attributes[fi])) {
						cell.setBackground(Display.getCurrent().getSystemColor(SWT.COLOR_GRAY));
					} else {
						if( showAge && keyAttributeIndices.length > 0 ) {
							int hashCode = tuple.restrictedHashCode(keyAttributeIndices);
							Long timestamp = hashTimestampMap.get(hashCode);
							
							long age = System.currentTimeMillis() - timestamp;
							if( age > maxAgeMillis ) {
								cell.setBackground(Display.getCurrent().getSystemColor(SWT.COLOR_WHITE));
							} else {
								float ageStep = (float)maxAgeMillis / (float)COLOR_AGE_STEPS;
								int index = Math.min((int)(age / ageStep), COLOR_AGE_STEPS - 1);
								cell.setBackground(ageColors.get(index));
							}
							
						} else {
							cell.setBackground(Display.getCurrent().getSystemColor(SWT.COLOR_WHITE));
						}
					}
				}
			});
		}

		tableViewer.setContentProvider(ArrayContentProvider.getInstance());
		tableViewer.setInput(tuplesForTable);
		tableViewer.refresh();
		tableViewer.getTable().redraw();
		
		tableComposite.layout();
	}
	
	private void deleteColumns() {
		disposeAllColumns(tableViewer.getTable());
	}
	
	private static void disposeAllColumns(Table table) {
		while( table.getColumnCount() > 0 ) {
			table.getColumns()[0].dispose();
		}
	}

	@Override
	public void punctuationElementRecieved(IPhysicalOperator senderOperator, IPunctuation punctuation, int port) {
	}

	@Override
	public void securityPunctuationElementRecieved(IPhysicalOperator senderOperator, ISecurityPunctuation sp, int port) {
	}

	@Override
	public void streamElementRecieved(IPhysicalOperator senderOperator, IStreamObject<?> element, int port) {
		if( element != null ) {
			synchronized( tuplesForTable ) {
				Tuple<?> tuple = (Tuple<?>)element;
				int hash = 0;
				if( !keyAttributes.isEmpty() ) {
					hash = tuple.restrictedHashCode(keyAttributeIndices);
				} else {
					hash = tuple.hashCode();
				}
				hashTimestampMap.put(hash, System.currentTimeMillis());
				
				Integer currentPosition = hashPositionMap.get(hash);
				if( currentPosition == null ) {
					tuplesForTable.add(tuple);
					hashPositionMap.put(hash, tuplesForTable.size() - 1);
					
//					System.out.println("Added tuple " + tuple + " (Hash " + hash + ") Size is now " + tuplesForTable.size() );
				} else {
					// DEBUG check
//					Tuple<?> otherTuple = (Tuple<?>)tuplesForTable.get(currentPosition);
//					for( int keyIndex : keyAttributeIndices) {
//						Object value = tuple.getAttribute(keyIndex);
//						Object otherValue = otherTuple.getAttribute(keyIndex);
//						
//						if( !value.equals(otherValue)) {
//							System.err.println("Hashkollision!!! Hash = " + hash);
//							System.err.println("Neues Tupel: " + tuple);
//							System.err.println("Altes Tupel: " + otherTuple);
//							System.err.println("AttributIndices = " + arrayToString(keyAttributeIndices));
//							System.err.println();
//							break;
//						}
//					}
					
					tuplesForTable.set(currentPosition, tuple);
//					System.out.println("Changed position " + currentPosition);
				}
				
				if( tuplesForTable.size() > maxData ) {
					long oldest = Long.MAX_VALUE;
					Object oldestHash = null;
					for( Integer hash2 : hashTimestampMap.keySet() ) {
						Long ts = hashTimestampMap.get(hash2);
						if( oldestHash == null || ts < oldest ) {
							oldestHash = hash2;
							oldest = ts;
						}
					}
					
					int positionToRemove = hashPositionMap.get(oldestHash);
					tuplesForTable.remove(positionToRemove);
//					System.out.println("Removed: Size to " + tuplesForTable.size());
					hashTimestampMap.remove(oldestHash);
					hashPositionMap.remove(oldestHash);
					
					for( Object someHash : hashPositionMap.keySet().toArray() ) {
						Integer somePosition = hashPositionMap.get(someHash);
						if( somePosition > positionToRemove ) {
							hashPositionMap.put((Integer)someHash, somePosition - 1);
						}
					}
				}
			}
			
			if( !refreshing && tableViewer.getInput() != null) {
				refreshing = true;
				PlatformUI.getWorkbench().getDisplay().asyncExec(new Runnable() {
					@Override
					public void run() {
						synchronized( tuplesForTable ) {
							if (!tableViewer.getTable().isDisposed()) {
								tableViewer.refresh();
							}
							refreshing = false;
						}
					}
				});
			}
		}
	}

	@SuppressWarnings("unused")
	private String arrayToString(int[] keyAttributeIndices2) {
		StringBuilder sb = new StringBuilder();
		sb.append("[");
		for( int i = 0; i < keyAttributeIndices2.length; i++ ) {
			sb.append(keyAttributeIndices2[i]).append("  ");
		}
		sb.append("]");
		return sb.toString();
	}

	private void refreshAttributesList(SDFSchema outputSchema) {
		if( attributes.length == 0 ) {
			attributes = new String[outputSchema.size()];
			for( int i = 0; i< attributes.length; i++ ) {
				attributes[i] = outputSchema.getAttribute(i).getAttributeName();
			}
		}
	}

	private static String[] determineAttributes(final String attributeList) {
		if (attributeList.trim().equalsIgnoreCase("*")) {
			return new String[0];
		}
		
		String[] attributes = attributeList.trim().split(",");
		for (int i = 0; i < attributes.length; i++) {
			attributes[i] = attributes[i].trim();
		}
		return attributes;
	}

	private static int[] determinePositions(SDFSchema outputSchema, String[] attributes) {
		int[] positions = null;
		if( attributes.length > 0 ) {
			positions = new int[attributes.length];
			for (int i = 0; i < attributes.length; i++) {
				Optional<Integer> optPosition = getPosition(outputSchema, attributes[i]);
				if( optPosition.isPresent() ) {
					positions[i] = optPosition.get();
				} else {
					throw new RuntimeException("Could not position of " + attributes[i]);
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
	
	public String[] getAttributes() {
		return attributes;
	}

	public boolean isShowAge() {
		return showAge;
	}

	public void setShowAge(boolean showAge) {
		this.showAge = showAge;
	}
}
