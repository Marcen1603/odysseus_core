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
package de.uniol.inf.is.odysseus.rcp.viewer.stream.table;

import org.eclipse.jface.viewers.ColumnViewer;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerComparator;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;

import de.uniol.inf.is.odysseus.core.collection.Tuple;

abstract class TupleColumnViewerSorter extends ViewerComparator {
    public static final int ASC = 1;

    public static final int NONE = 0;

    public static final int DESC = -1;

    private int direction = 0;

    private TableViewerColumn column;

    private ColumnViewer viewer;

    public TupleColumnViewerSorter(ColumnViewer viewer, TableViewerColumn column) {
        this.column = column;
        this.viewer = viewer;
        this.column.getColumn().addSelectionListener(new SelectionAdapter() {

            @Override
            public void widgetSelected(SelectionEvent e) {
                if (TupleColumnViewerSorter.this.viewer.getComparator() != null) {
                    if (TupleColumnViewerSorter.this.viewer.getComparator() == TupleColumnViewerSorter.this) {
                        int tdirection = TupleColumnViewerSorter.this.direction;

                        if (tdirection == ASC) {
                            setSorter(TupleColumnViewerSorter.this, DESC);
                        } else if (tdirection == DESC) {
                            setSorter(TupleColumnViewerSorter.this, NONE);
                        }
                    } else {
                        setSorter(TupleColumnViewerSorter.this, ASC);
                    }
                } else {
                    setSorter(TupleColumnViewerSorter.this, ASC);
                }
            }
        });
    }

    public void setSorter(TupleColumnViewerSorter sorter, int direction) {
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
        return direction * doCompare(viewer, (Tuple<?>)e1, (Tuple<?>)e2);
    }

    protected abstract int doCompare(Viewer viewer, Tuple<?> e1, Tuple<?> e2);
}
