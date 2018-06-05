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
import java.util.Formatter;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.eclipse.jface.layout.TableColumnLayout;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.CellLabelProvider;
import org.eclipse.jface.viewers.ColumnWeightData;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.jface.viewers.ViewerCell;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Device;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.FontData;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableItem;
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
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;
import de.uniol.inf.is.odysseus.rcp.dashboard.AbstractDashboardPart;

public class TableDashboardPart extends AbstractDashboardPart {

	private static final Logger LOG = LoggerFactory.getLogger(TableDashboardPart.class);

	private IPhysicalOperator operator;

	private TableViewer tableViewer;
	private TableColumnLayout tableColumnLayout;
	private Composite tableComposite;
	private Font titleFont;

	private String[] attributes;
	private int[] positions;
	private boolean refreshing = false;

	private final List<Tuple<?>> data = Lists.newArrayList();

	private String attributeList = "*";
	private int maxData = 10;
	private String title = "";
	private String operatorName = "";

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

	/**
	 * The name of the root operator (configuration parameter) in case of two
	 * ore more root operators.
	 *
	 * @return the operatorName
	 */
	public String getOperatorName() {
		return operatorName;
	}

	/**
	 * @param operatorName
	 *            the operatorName to set
	 */
	public void setOperatorName(String operatorName) {
		this.operatorName = operatorName;
	}

	@Override
	public void createPartControl(Composite parent, ToolBar toolbar) {
		if (Strings.isNullOrEmpty(attributeList)) {
			new Label(parent, SWT.NONE).setText("Attribute List is invalid!");
			return;
		}

		attributes = determineAttributes(attributeList);

		Composite topComposite = new Composite(parent, SWT.NONE);
		topComposite.setLayoutData(new GridData(GridData.FILL_BOTH));
		topComposite.setLayout(new GridLayout(1, false));
		topComposite.setBackground(Display.getCurrent().getSystemColor(SWT.COLOR_WHITE));

		if (!Strings.isNullOrEmpty(title)) {
			createLabel(topComposite);
		}

		tableComposite = new Composite(topComposite, SWT.NONE);
		tableComposite.setLayoutData(new GridData(GridData.FILL_BOTH));
		tableColumnLayout = new TableColumnLayout();
		tableComposite.setLayout(tableColumnLayout);

		tableViewer = new TableViewer(tableComposite, SWT.BORDER | SWT.FULL_SELECTION | SWT.SINGLE);
		final Table table = tableViewer.getTable();
		table.setLayoutData(new GridData(GridData.FILL_BOTH));
		table.setLinesVisible(true);
		table.setHeaderVisible(true);

		parent.layout();
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
		if (titleFont != null) {
			titleFont.dispose();
		}
		super.dispose();
	}

	@Override
	public void onLoad(Map<String, String> saved) {
		attributeList = saved.get("Attributes");
		maxData = Integer.valueOf(saved.get("MaxData"));
		title = saved.get("Title");
		operatorName = saved.get("Operator");
	}

	@Override
	public Map<String, String> onSave() {
		Map<String, String> toSaveMap = Maps.newHashMap();
		toSaveMap.put("Attributes", attributeList);
		toSaveMap.put("MaxData", String.valueOf(maxData));
		toSaveMap.put("Title", title);
		toSaveMap.put("Operator", operatorName);
		return toSaveMap;
	}

	@Override
	public void onStart(Collection<IPhysicalOperator> physicalRoots) throws Exception {
		super.onStart(physicalRoots);

		operator = determinePyhsicalRoot(physicalRoots);

		positions = determinePositions(operator.getOutputSchema(), attributes);
		refreshAttributesList(operator.getOutputSchema()); // if attributes was
															// = "*"

		deleteColumns();
		final int colCount = positions.length;
		for (int i = 0; i < colCount; i++) {

			final int finalI = i;

			final TableViewerColumn tableViewerColumn = new TableViewerColumn(tableViewer, SWT.NONE);
			tableViewerColumn.getColumn().setText(attributes[i]);
			tableColumnLayout.setColumnData(tableViewerColumn.getColumn(), new ColumnWeightData(5, 25, true));
			tableViewerColumn.setLabelProvider(new CellLabelProvider() {
				@Override
				public void update(ViewerCell cell) {
					final Tuple<?> tuple = (Tuple<?>) cell.getElement();
					final Object attrValue = tuple.getAttributes()[positions[finalI]];
					cell.setText(attrValue != null ? attrValue.toString() : "null");
				}
			});
		}

		tableViewer.setContentProvider(ArrayContentProvider.getInstance());
		tableViewer.setInput(data);
		tableViewer.refresh();
		tableViewer.getTable().redraw();

		tableComposite.layout();
	}

	/**
	 * @param physicalRoots
	 * @return
	 */
	private IPhysicalOperator determinePyhsicalRoot(Collection<IPhysicalOperator> physicalRoots) {
		for (IPhysicalOperator p : physicalRoots) {
			if (p.getName().equals(getOperatorName())) {
				return p;
			}
		}
		LOG.info("Select first physical root.");
		return physicalRoots.iterator().next();
	}

	private void deleteColumns() {
		disposeAllColumns(tableViewer.getTable());
	}

	private static void disposeAllColumns(Table table) {
		while (table.getColumnCount() > 0) {
			table.getColumns()[0].dispose();
		}
	}

	@Override
	public void punctuationElementRecieved(IPhysicalOperator senderOperator, IPunctuation punctuation, int port) {
	}

	@Override
	public void streamElementRecieved(IPhysicalOperator senderOperator, IStreamObject<?> element, int port) {
		if (element != null && senderOperator.equals(operator)) {
			synchronized (data) {
				data.add(0, (Tuple<?>) element);
				if (data.size() > maxData) {
					data.remove(data.size() - 1);
				}
			}

			if (!refreshing && tableViewer.getInput() != null) {
				refreshing = true;
				PlatformUI.getWorkbench().getDisplay().asyncExec(new Runnable() {
					@Override
					public void run() {
						synchronized (data) {
							if (!tableViewer.getTable().isDisposed()) {
								tableViewer.refresh();

								Table table = tableViewer.getTable();
								TableItem[] items = table.getItems();

								for (int i = 0; i < data.size(); ++i) {
									Tuple<?> tuple = data.get(i);
									Object[] attributes = tuple.getAttributes();
									for (int j = 0; j < attributes.length; ++j) {

										if (attributes[j] instanceof List) {
											String attr = formatListAttributeToString((List<?>) attributes[j]);
											if (attr == null)
												items[i].setText(j, items[i].getText(j).substring(1, items[i].getText(j).length() - 1).replaceAll(", ", "\r\n").replaceAll("\\|", "\t"));
											else {
												items[i].setText(j, attr);
											}
											items[i].setFont(j, SWTFontUtils.getMonospacedFont());
										}

									}
								}
							}

							refreshing = false;
						}
					}
				});
			}
		}
	}

	private static String formatListAttributeToString(List<?> list) {
		StringBuilder sb = new StringBuilder();
		Formatter formatter = new Formatter(sb);

		@SuppressWarnings("unchecked")
		int[] maxLengths = getMaxLenghts((List<Tuple<?>>) list);

		for (int i = 0; i < list.size(); ++i) {
			if (list.get(i) instanceof Tuple) {
				Tuple<?> tuple = (Tuple<?>) list.get(i);
				Object[] attributes = tuple.getAttributes();
				for (int j = 0; j < attributes.length; ++j) {
					Object attr = attributes[j];
					formatter.format("%1$" + maxLengths[j] + "s", attr);
					if (j < attributes.length - 1) {
						sb.append("\t");
					}
				}
				if (i < list.size() - 1) {
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
	 * @param list
	 *            A list of tuples with attributes.
	 * @return an array int[i] that hat the highest length of the attributes
	 *         with index i of all tuples in the list.
	 */
	private static int[] getMaxLenghts(List<Tuple<?>> list) {
		if (list == null || list.size() == 0) {
			return new int[0];
		}
		Tuple<?> t = list.get(0);
		if (t == null) {
			return new int[0];
		}
		int[] maxLengths = new int[t.getAttributes().length];
		for (Tuple<?> tuple : list) {
			Object[] attributes = tuple.getAttributes();
			if (attributes.length > maxLengths.length) {
				LOG.warn("length missmatch in list of tuples");
				return new int[0];
			}
			for (int i = 0; i < attributes.length; ++i) {
				if (maxLengths[i] < attributes[i].toString().length()) {
					maxLengths[i] = attributes[i].toString().length();
				}
			}
		}
		return maxLengths;
	}

	private void refreshAttributesList(SDFSchema outputSchema) {
		if (attributes.length == 0) {
			attributes = new String[outputSchema.size()];
			for (int i = 0; i < attributes.length; i++) {
				attributes[i] = outputSchema.getAttribute(i).getAttributeName();
			}
		}
	}

	public static String[] determineAttributes(final String attributeList) {
		if (attributeList == null || attributeList.trim().equals("")) {
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
		if (attributes2.length > 0) {
			positions = new int[attributes2.length];
			for (int i = 0; i < attributes2.length; i++) {
				Optional<Integer> optPosition = getPosition(outputSchema, attributes2[i]);
				if (optPosition.isPresent()) {
					positions[i] = optPosition.get();
				} else {
					throw new RuntimeException("Could not find position of " + attributes2[i]);
				}
			}
		} else {
			positions = new int[outputSchema.size()];
			for (int i = 0; i < positions.length; i++) {
				positions[i] = i;
			}
		}
		return positions;
	}

	private static Optional<Integer> getPosition(SDFSchema outputSchema, String attribute) {
		for (int j = 0; j < outputSchema.size(); j++) {
			if (outputSchema.get(j).getAttributeName().equals(attribute)) {
				return Optional.of(j);
			}
		}
		return Optional.absent();
	}

	/**
	 * {@link SWT} font related utility methods.
	 * https://bugs.eclipse.org/bugs/attachment.cgi?id=238603
	 */
	private static class SWTFontUtils {
		/** Cache: mapping from SWT devices to their monospaced fonts. */
		private static final Map<Device, Font> MONOSPACED_FONTS = new HashMap<>();

		/** Constructor for the {@link SWTFontUtils} class. */
		private SWTFontUtils() {
			// Static class.
		}

		/**
		 * Returns the monospaced font for the current display. The font will
		 * automatically be disposed once the display is disposed.
		 *
		 * <p>
		 * This method is thread safe.
		 * </p>
		 *
		 * @return The monospaced font for the current display.
		 * @throws IllegalStateException
		 *             If the method is not invoked from a SWT UI thread.
		 */
		public static Font getMonospacedFont() {
			synchronized (MONOSPACED_FONTS) {
				// Get current display.
				Display display = Display.getCurrent();
				if (display == null) {
					String msg = "Must be invoked for a SWT UI thread.";
					throw new IllegalStateException(msg);
				}

				// Forward request.
				return getMonospacedFont(display);
			}
		}

		/**
		 * Creates a monospaced font for the given display. The font will
		 * automatically be disposed once the display is disposed.
		 *
		 * <p>
		 * This method is thread safe.
		 * </p>
		 *
		 * @param display
		 *            The display for which to create a monospaced font.
		 * @return A monospaced font for the given display.
		 */
		public static Font getMonospacedFont(final Display display) {
			synchronized (MONOSPACED_FONTS) {
				// Based on class 'org.eclipse.jface.resource.FontRegistry' and
				// resources 'org.eclipse.jface.resource/jfacefonts*.properties'
				// from 'org.eclipse.jface' plug-in (version 3.9.1).

				// Use cache if possible.
				Font cachedFont = MONOSPACED_FONTS.get(display);
				if (cachedFont != null)
					return cachedFont;

				// Get operating system and windowing system names.
				String os = System.getProperty("os.name");
				String ws = SWT.getPlatform();
				os = StringUtils.deleteWhitespace(os).toLowerCase(Locale.US);
				ws = StringUtils.deleteWhitespace(ws).toLowerCase(Locale.US);

				// Get names to check, in order from specific to generic.
				String[] names = { os + "_" + ws, os, "" };

				// Get font data texts for platform.
				String[] fontDataTxts = null;
				for (String name : names) {
					if (name.equals("aix")) {
						fontDataTxts = new String[] { "adobe-courier|normal|12" };
						break;

					} else if (name.equals("hp-ux")) {
						fontDataTxts = new String[] { "adobe-courier|normal|14" };
						break;

					} else if (name.equals("linux_gtk")) {
						fontDataTxts = new String[] { "Monospace|normal|10" };
						break;

					} else if (name.equals("linux")) {
						fontDataTxts = new String[] { "adobe-courier|normal|12" };
						break;

					} else if (name.equals("macosx")) {
						fontDataTxts = new String[] { "Monaco|normal|11", "Courier|normal|12", "Courier New|normal|12" };
						break;

					} else if (name.equals("sunos") || name.equals("solaris")) {
						fontDataTxts = new String[] { "adobe-courier|normal|12" };
						break;

					} else if (name.equals("windows98")) {
						fontDataTxts = new String[] { "Courier New|normal|10", "Courier|normal|10", "Lucida Console|normal|9" };
						break;

					} else if (name.equals("windowsnt")) {
						fontDataTxts = new String[] { "Courier New|normal|10", "Courier|normal|10", "Lucida Console|normal|9" };
						break;

					} else if (name.equals("windows2000")) {
						fontDataTxts = new String[] { "Courier New|normal|10", "Courier|normal|10", "Lucida Console|normal|9" };
						break;

					} else if (name.equals("windowsxp")) {
						fontDataTxts = new String[] { "Courier New|normal|10", "Courier|normal|10", "Lucida Console|normal|9" };
						break;

					} else if (name.equals("windowsvista")) {
						fontDataTxts = new String[] { "Consolas|normal|10", "Courier New|normal|10" };
						break;

					} else if (name.equals("windows7")) {
						fontDataTxts = new String[] { "Consolas|normal|10", "Courier New|normal|10" };
						break;

					} else if (name.equals("windows8")) {
						fontDataTxts = new String[] { "Consolas|normal|10", "Courier New|normal|10" };
						break;

					} else if (name.equals("")) {
						fontDataTxts = new String[] { "Courier New|normal|10", "Courier|normal|10", "b&h-lucidabright|normal|9" };
						break;
					}
				}

				if (fontDataTxts != null) {
					// Null can't happen, but silences a warning.

					// Convert texts to font data.
					FontData[] fontDatas = new FontData[fontDataTxts.length];
					for (int i = 0; i < fontDatas.length; i++) {
						// Find splitters.
						String txt = fontDataTxts[i];
						int bar2 = txt.lastIndexOf('|');
						// Assert.assertTrue(bar2 != -1);
						int bar1 = txt.lastIndexOf('|', bar2 - 1);
						// Assert.assertTrue(bar1 != -1);

						// Get font name.
						String name = txt.substring(0, bar1);
						// Assert.assertTrue(name.length() > 0);

						// Get font style.
						String[] styles = txt.substring(bar1 + 1, bar2).split(",");
						int style = 0;
						for (String s : styles) {
							if (s.equals("normal")) {
								style |= SWT.NORMAL;
							} else if (s.equals("bold")) {
								style |= SWT.BOLD;
							} else if (s.equals("italic")) {
								style |= SWT.ITALIC;
							} else {
								throw new RuntimeException("Invalid style: " + s);
							}
						}

						// Get font height.
						int height = Integer.parseInt(txt.substring(bar2 + 1));

						// Create and add font date.
						fontDatas[i] = new FontData(name, height, style);
					}

					// Create font.
					final Font font = new Font(display, fontDatas);

					// Register dispose callback, to dispose of the font once
					// the
					// display is disposed.
					display.disposeExec(new Runnable() {
						@Override
						public void run() {
							synchronized (MONOSPACED_FONTS) {
								MONOSPACED_FONTS.remove(display);
								font.dispose();
							}
						}
					});

					// Add to cache.
					MONOSPACED_FONTS.put(display, font);

					// Return the new font.
					return font;
				}
				// can't happen
				return null;

			}
		}
	}

}
