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
package de.uniol.inf.is.odysseus.rcp.views.query;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Timer;
import java.util.TimerTask;

import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.layout.TableColumnLayout;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.CellLabelProvider;
import org.eclipse.jface.viewers.ColumnViewer;
import org.eclipse.jface.viewers.ColumnWeightData;
import org.eclipse.jface.viewers.DoubleClickEvent;
import org.eclipse.jface.viewers.IDoubleClickListener;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerCell;
import org.eclipse.jface.viewers.ViewerComparator;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.handlers.IHandlerService;
import org.eclipse.ui.part.ViewPart;

import com.google.common.base.Preconditions;

import de.uniol.inf.is.odysseus.rcp.OdysseusRCPPlugIn;
import de.uniol.inf.is.odysseus.rcp.l10n.OdysseusNLS;

public class QueryView extends ViewPart {

    private TableViewer tableViewer;
    private Collection<IQueryViewData> queries = new ArrayList<IQueryViewData>();

    Timer refreshTimer = null;
    private boolean doAutoRefresh;

    @Override
    public void createPartControl(Composite parent) {

        Composite tableComposite = new Composite(parent, SWT.NONE);
        TableColumnLayout tableColumnLayout = new TableColumnLayout();
        tableComposite.setLayout(tableColumnLayout);

        tableViewer = new QueryTableViewer(tableComposite, SWT.MULTI | SWT.FULL_SELECTION);

        /************* ID ****************/
        TableViewerColumn idColumn = new TableViewerColumn(tableViewer, SWT.NONE);
        idColumn.getColumn().setText(OdysseusNLS.ID);
        idColumn.setLabelProvider(new CellLabelProvider() {
            @Override
            public void update(ViewerCell cell) {
                cell.setText(String.valueOf(((IQueryViewData) cell.getElement()).getId()));
            }
        });
        tableColumnLayout.setColumnData(idColumn.getColumn(), new ColumnWeightData(5, 25, true));
        ColumnViewerSorter sorter = new ColumnViewerSorter(tableViewer, idColumn) {
            @Override
            protected int doCompare(Viewer viewer, Object e1, Object e2) {
                IQueryViewData id1 = (IQueryViewData) e1;
                IQueryViewData id2 = (IQueryViewData) e2;
                if (id1.getId() > id2.getId())
                    return 1;
                else if (id1.getId() < id2.getId())
                    return -1;
                else
                    return 0;
            }
        };

        /************* Status ****************/
        TableViewerColumn statusColumn = new TableViewerColumn(tableViewer, SWT.NONE);
        statusColumn.getColumn().setText(OdysseusNLS.Status);
        // statusColumn.getColumn().setWidth(100);
        statusColumn.setLabelProvider(new CellLabelProvider() {
            @Override
            public void update(ViewerCell cell) {
                String text = ((IQueryViewData) cell.getElement()).getStatus();
                cell.setText(text);
            }
        });
        tableColumnLayout.setColumnData(statusColumn.getColumn(), new ColumnWeightData(5, 25, true));
        new ColumnViewerSorter(tableViewer, statusColumn) {
            @Override
            protected int doCompare(Viewer viewer, Object e1, Object e2) {
                IQueryViewData id1 = (IQueryViewData) e1;
                IQueryViewData id2 = (IQueryViewData) e2;
                String s1 = id1.getStatus();
                String s2 = id2.getStatus();

                if (s1.equals(s2))
                    return 0;
                else if (s1.equals(OdysseusNLS.Opened))
                    return 1;
                else if (s1.equals(OdysseusNLS.Active)) {
                    if (s2.equals(OdysseusNLS.Inactive))
                        return 1;
                    return -1;
                } else
                    return -1;
            }
        };

        /************* Priority ****************/
        TableViewerColumn priorityColumn = new TableViewerColumn(tableViewer, SWT.NONE);
        priorityColumn.getColumn().setText(OdysseusNLS.Priority);
        priorityColumn.setLabelProvider(new CellLabelProvider() {
            @Override
            public void update(ViewerCell cell) {
                cell.setText(String.valueOf(((IQueryViewData) cell.getElement()).getPriority()));
            }
        });
        tableColumnLayout.setColumnData(priorityColumn.getColumn(), new ColumnWeightData(5, 25, true));
        new ColumnViewerSorter(tableViewer, priorityColumn) {
            @Override
            protected int doCompare(Viewer viewer, Object e1, Object e2) {
                IQueryViewData id1 = (IQueryViewData) e1;
                IQueryViewData id2 = (IQueryViewData) e2;
                if (id1.getPriority() > id2.getPriority())
                    return 1;
                else if (id1.getPriority() < id2.getPriority())
                    return -1;
                else
                    return 0;
            }
        };

        /************* Parser ID ****************/
        TableViewerColumn parserIdColumn = new TableViewerColumn(tableViewer, SWT.NONE);
        parserIdColumn.getColumn().setText(OdysseusNLS.Parser);
        // parserIdColumn.getColumn().setWidth(100);
        parserIdColumn.setLabelProvider(new CellLabelProvider() {
            @Override
            public void update(ViewerCell cell) {
                IQueryViewData query = (IQueryViewData) cell.getElement();
                cell.setText(query.getParserId());
            }
        });
        tableColumnLayout.setColumnData(parserIdColumn.getColumn(), new ColumnWeightData(5, 25, true));
        new ColumnViewerSorter(tableViewer, parserIdColumn) {
            @Override
            protected int doCompare(Viewer viewer, Object e1, Object e2) {
                IQueryViewData q1 = (IQueryViewData) e1;
                IQueryViewData q2 = (IQueryViewData) e2;
                String id1 = q1.getParserId();
                String id2 = q2.getParserId();
                return id1.compareToIgnoreCase(id2);
            }
        };

        /************* User ****************/
        TableViewerColumn userColumn = new TableViewerColumn(tableViewer, SWT.NONE);
        userColumn.getColumn().setText(OdysseusNLS.User);
        // userColumn.getColumn().setWidth(400);
        userColumn.setLabelProvider(new CellLabelProvider() {
            @Override
            public void update(ViewerCell cell) {
                IQueryViewData query = (IQueryViewData) cell.getElement();
                cell.setText(query.getUserName());
            }
        });
        tableColumnLayout.setColumnData(userColumn.getColumn(), new ColumnWeightData(5, 25, true));
        new ColumnViewerSorter(tableViewer, userColumn) {
            @Override
            protected int doCompare(Viewer viewer, Object e1, Object e2) {
                IQueryViewData id1 = (IQueryViewData) e1;
                IQueryViewData id2 = (IQueryViewData) e2;
                return id1.getUserName().compareToIgnoreCase(id2.getUserName());
            }
        };

        /************* Query Text ****************/
        TableViewerColumn queryTextColumn = new TableViewerColumn(tableViewer, SWT.NONE);
        queryTextColumn.getColumn().setText(OdysseusNLS.QueryText);
        // queryTextColumn.getColumn().setWidth(400);
        queryTextColumn.setLabelProvider(new CellLabelProvider() {
            @Override
            public void update(ViewerCell cell) {
                IQueryViewData query = (IQueryViewData) cell.getElement();
                String text = query.getQueryText();
                if (text == null) {
                    cell.setText("[No Text]");
                    return;
                }
                text = text.replace('\n', ' ');
                text = text.replace('\r', ' ');
                text = text.replace('\t', ' ');
                cell.setText(text);
            }
        });
        tableColumnLayout.setColumnData(queryTextColumn.getColumn(), new ColumnWeightData(50, 200, true));
        new ColumnViewerSorter(tableViewer, queryTextColumn) {
            @Override
            protected int doCompare(Viewer viewer, Object e1, Object e2) {
                IQueryViewData q1 = (IQueryViewData) e1;
                IQueryViewData q2 = (IQueryViewData) e2;
                String text1 = q1.getQueryText();
                String text2 = q2.getQueryText();

                return text1.compareToIgnoreCase(text2);
            }
        };

        tableViewer.setContentProvider(ArrayContentProvider.getInstance());
        tableViewer.setInput(queries);
        getSite().setSelectionProvider(tableViewer);

        tableViewer.addDoubleClickListener(new IDoubleClickListener() {
            @Override
            public void doubleClick(DoubleClickEvent event) {
                IHandlerService handlerService = (IHandlerService) getSite().getService(IHandlerService.class);

                try {
                    handlerService.executeCommand("de.uniol.inf.is.odysseus.rcp.commands.CallGraphEditorCommand", null);
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        });

        sorter.setSorter(sorter, ColumnViewerSorter.NONE);

        // Contextmenu
        MenuManager menuManager = new MenuManager();
        Menu contextMenu = menuManager.createContextMenu(tableViewer.getTable());
        // Set the MenuManager
        tableViewer.getTable().setMenu(contextMenu);
        getSite().registerContextMenu(menuManager, tableViewer);

        initData();

        if (doAutoRefresh) {
            refreshTimer = createRefreshTimer();
        }
    }
    
    private Timer createRefreshTimer() {
    	Timer t = new Timer();
        t.scheduleAtFixedRate(new TimerTask() {

            @Override
            public void run() {
                try {
                    refreshTable();
                } catch (Exception e) {
                    this.cancel();
                }
            }
        }, 1000, 1000);
        
        return t;
    }

	private void initData() {
		IQueryViewDataProvider dataProvider = OdysseusRCPPlugIn.getQueryViewDataProvider();
        Preconditions.checkNotNull(dataProvider, "DataProvider for QueryView must not be null!");
        dataProvider.init(this);
        queries.addAll(dataProvider.getData());
	}

    @Override
    public void dispose() {
        super.dispose();
    }

    @Override
    public void setFocus() {

    }

    public void refreshTable() {
        PlatformUI.getWorkbench().getDisplay().asyncExec(new Runnable() {

            @Override
            public void run() {
                queries.clear();
                queries.addAll(OdysseusRCPPlugIn.getQueryViewDataProvider().getData());

                if (!tableViewer.getControl().isDisposed())
                    tableViewer.refresh();
            }

        });
    }

    private static abstract class ColumnViewerSorter extends ViewerComparator {
        public static final int ASC = 1;

        public static final int NONE = 0;

        public static final int DESC = -1;

        private int direction = 0;

        private TableViewerColumn column;

        private ColumnViewer viewer;

        public ColumnViewerSorter(ColumnViewer viewer, TableViewerColumn column) {
            this.column = column;
            this.viewer = viewer;
            this.column.getColumn().addSelectionListener(new SelectionAdapter() {

                @Override
                public void widgetSelected(SelectionEvent e) {
                    if (ColumnViewerSorter.this.viewer.getComparator() != null) {
                        if (ColumnViewerSorter.this.viewer.getComparator() == ColumnViewerSorter.this) {
                            int tdirection = ColumnViewerSorter.this.direction;

                            if (tdirection == ASC) {
                                setSorter(ColumnViewerSorter.this, DESC);
                            } else if (tdirection == DESC) {
                                setSorter(ColumnViewerSorter.this, NONE);
                            }
                        } else {
                            setSorter(ColumnViewerSorter.this, ASC);
                        }
                    } else {
                        setSorter(ColumnViewerSorter.this, ASC);
                    }
                }
            });
        }

        public void setSorter(ColumnViewerSorter sorter, int direction) {
            if (direction == NONE) {
                column.getColumn().getParent().setSortColumn(null);
                column.getColumn().getParent().setSortDirection(SWT.NONE);
                viewer.setComparator(null);
            } else {
                column.getColumn().getParent().setSortColumn(column.getColumn());
                sorter.direction = direction;

                if (direction == ASC) {
                    column.getColumn().getParent().setSortDirection(SWT.DOWN);
                } else {
                    column.getColumn().getParent().setSortDirection(SWT.UP);
                }

                if (viewer.getComparator() == sorter) {
                    viewer.refresh();
                } else {
                    viewer.setComparator(sorter);
                }

            }
        }

        @Override
        public int compare(Viewer viewer, Object e1, Object e2) {
            return direction * doCompare(viewer, e1, e2);
        }

        protected abstract int doCompare(Viewer viewer, Object e1, Object e2);
    }
}