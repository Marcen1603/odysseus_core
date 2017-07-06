/*******************************************************************************
 * Copyright 2012 The Odysseus Team
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/
package de.uniol.inf.is.odysseus.rcp.views.mepfunctions;

import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.layout.TableColumnLayout;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.CellLabelProvider;
import org.eclipse.jface.viewers.ColumnWeightData;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerCell;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.part.ViewPart;

import com.google.common.base.Strings;
import com.google.common.collect.Lists;

import de.uniol.inf.is.odysseus.core.mep.IMepFunction;
import de.uniol.inf.is.odysseus.mep.FunctionSignature;
import de.uniol.inf.is.odysseus.mep.MEP;
import de.uniol.inf.is.odysseus.rcp.l10n.OdysseusNLS;

public class MEPFunctionsView extends ViewPart {

	TableViewer tableViewer;
	Text filterText;
///	static private Font strikeFont;

	@Override
	public void createPartControl(Composite parent) {
		final GridLayout gridLayout = new GridLayout();
		gridLayout.numColumns = 1;
		parent.setLayout(gridLayout);
		this.filterText = new Text(parent, SWT.BORDER);
		this.filterText.setLayoutData(new GridData(GridData.GRAB_HORIZONTAL
				| GridData.HORIZONTAL_ALIGN_FILL));
		this.filterText.setMessage(OdysseusNLS.Filter);

		Composite tableComposite = new Composite(parent, SWT.NONE);
		tableComposite.setLayoutData(new GridData(GridData.FILL_BOTH));
		TableColumnLayout tableColumnLayout = new TableColumnLayout();
		tableComposite.setLayout(tableColumnLayout);

		this.tableViewer = new TableViewer(tableComposite);
		this.tableViewer.getTable().setHeaderVisible(true);
		this.tableViewer.getTable().setLinesVisible(true);
		this.tableViewer.setContentProvider(ArrayContentProvider.getInstance());

		createColumns(this.tableViewer, tableColumnLayout);
		refresh("");
		this.filterText.addModifyListener(new ModifyListener() {

			@Override
			public void modifyText(ModifyEvent e) {
				try {
					refresh(MEPFunctionsView.this.filterText.getText());
				} catch (final Exception ex) {
					MessageDialog.openWarning(e.display.getActiveShell(),
							"Error", ex.getMessage());
				}
			}
		});
		
		// TODO: Dispose font?

//		if (strikeFont == null) {
//			Label l = new Label(parent, SWT.NONE);
//			FontDescriptor boldDescriptor = FontDescriptor.createFrom(
//					l.getFont()).setStyle(TextAttribute.STRIKETHROUGH);
//			strikeFont = boldDescriptor.createFont(l.getDisplay());
//		}

	}

	@Override
	public void setFocus() {
		this.tableViewer.getTable().setFocus();
	}

	public void refresh() {
		this.refresh("");
	}

	public void refresh(final String filter) {
		if (!PlatformUI.getWorkbench().getDisplay().isDisposed()) {
			PlatformUI.getWorkbench().getDisplay().asyncExec(new Runnable() {

				@Override
				public void run() {
					Set<FunctionSignature> functionSymbols = MEP.getFunctions();
					List<MEPFunctionInfo> functionInfos;
					if (Strings.isNullOrEmpty(filter)) {
						functionInfos = determineFunctionInfos(functionSymbols);
					} else {
						String regexFilter = wildcardToRegexPattern(filter
								.toLowerCase());
						HashSet<FunctionSignature> filteredSymbols = new HashSet<>();
						Iterator<FunctionSignature> functionsIter = functionSymbols
								.iterator();
						while (functionsIter.hasNext()) {
							FunctionSignature function = functionsIter.next();
							MEPFunctionInfo funtionInfo = MEPFunctionInfo
									.fromMEPFunction(MEP.getFunction(function));
							try {
								if (funtionInfo.getSymbol().toLowerCase()
										.matches(regexFilter)) {
									filteredSymbols.add(function);
								}
							} catch (Exception e) {
								// Add function to list in case of RegEx
								// exception
								filteredSymbols.add(function);
							}
						}
						functionInfos = determineFunctionInfos(filteredSymbols);
					}
					MEPFunctionsView.this.tableViewer.setInput(functionInfos);
					MEPFunctionsView.this.tableViewer.refresh();
				}
			});
		}

	}

	private static void createColumns(TableViewer tableViewer,
			TableColumnLayout tableColumnLayout) {
		TableViewerColumn symbolColumn = createColumn(tableViewer,
				tableColumnLayout, OdysseusNLS.Symbol, new CellLabelProvider() {
					@Override
					public void update(ViewerCell cell) {
						String text = ((MEPFunctionInfo) cell.getElement())
								.getSymbol(); 
						
						
						if (((MEPFunctionInfo) cell.getElement())
								.isDeprecated()) {
							cell.setBackground(Display.getCurrent().getSystemColor(SWT.COLOR_GRAY));
							text = "-- "+text;
						}
						cell.setText(text);
					}
				}, 5);
		ColumnViewerSorter sorter = new ColumnViewerSorter(tableViewer,
				symbolColumn) {
			@Override
			protected int doCompare(Viewer viewer, Object e1, Object e2) {
				return ((MEPFunctionInfo) e1).getSymbol().compareToIgnoreCase(
						((MEPFunctionInfo) e2).getSymbol());
			}
		};
		sorter.setSorter(sorter, ColumnViewerSorter.ASC);

		TableViewerColumn returnTypeColumn = createColumn(tableViewer,
				tableColumnLayout, OdysseusNLS.ReturnType,
				new CellLabelProvider() {
					@Override
					public void update(ViewerCell cell) {
						cell.setText(((MEPFunctionInfo) cell.getElement())
								.getResultType());
					}
				}, 5);
		sorter = new ColumnViewerSorter(tableViewer, returnTypeColumn) {
			@Override
			protected int doCompare(Viewer viewer, Object e1, Object e2) {
				return ((MEPFunctionInfo) e1).getResultType()
						.compareToIgnoreCase(
								((MEPFunctionInfo) e2).getResultType());
			}
		};

		TableViewerColumn arityColumn = createColumn(tableViewer,
				tableColumnLayout, OdysseusNLS.Arity, new CellLabelProvider() {
					@Override
					public void update(ViewerCell cell) {
						cell.setText(String.valueOf(((MEPFunctionInfo) cell
								.getElement()).getArity()));
					}
				}, 1);
		sorter = new ColumnViewerSorter(tableViewer, arityColumn) {
			@Override
			protected int doCompare(Viewer viewer, Object e1, Object e2) {
				int a1 = ((MEPFunctionInfo) e1).getArity();
				int a2 = ((MEPFunctionInfo) e2).getArity();
				if (a1 > a2) {
					return -1;
				} else if (a1 == a2) {
					return 0;
				} else {
					return 1;
				}
			}
		};

		TableViewerColumn argTypesColumn = createColumn(tableViewer,
				tableColumnLayout, OdysseusNLS.ArgumentTypes,
				new CellLabelProvider() {
					@Override
					public void update(ViewerCell cell) {
						cell.setText(((MEPFunctionInfo) cell.getElement())
								.getArgTypes().toString());
					}
				}, 20);
		sorter = new ColumnViewerSorter(tableViewer, argTypesColumn) {
			@Override
			protected int doCompare(Viewer viewer, Object e1, Object e2) {
				return ((MEPFunctionInfo) e1)
						.getArgTypes()
						.toString()
						.compareToIgnoreCase(
								((MEPFunctionInfo) e2).getArgTypes().toString());
			}
		};
	}

	static List<MEPFunctionInfo> determineFunctionInfos(
			Set<FunctionSignature> functionSignatures) {
		List<MEPFunctionInfo> functionInfos = Lists.newArrayList();
		for (FunctionSignature functionSignature : functionSignatures) {
			IMepFunction<?> function = MEP.getFunction(functionSignature);
			functionInfos.add(MEPFunctionInfo.fromMEPFunction(function));
		}
		return functionInfos;
	}

	private static TableViewerColumn createColumn(TableViewer tableViewer,
			TableColumnLayout tableColumnLayout, String title,
			CellLabelProvider labelProvider, int weight) {
		TableViewerColumn column = new TableViewerColumn(tableViewer, SWT.NONE);
		column.getColumn().setText(title);
		column.setLabelProvider(labelProvider);
		tableColumnLayout.setColumnData(column.getColumn(),
				new ColumnWeightData(weight, 25, true));
		return column;
	}

	static String wildcardToRegexPattern(String wildcard) {
		String regex = wildcard;
		regex = regex.replace(".", "\\.");
		regex = regex.replace("*", ".*");
		regex = regex.replace("?", ".");
		regex = regex.replace("!", "^");
		return regex + ".*";
	}
}
