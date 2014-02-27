package de.uniol.inf.is.odysseus.peer.rcp.views;

import java.util.List;

import net.jxta.document.Advertisement;

import org.eclipse.jface.layout.TableColumnLayout;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.CellLabelProvider;
import org.eclipse.jface.viewers.ColumnWeightData;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerCell;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.part.ViewPart;

import com.google.common.collect.Lists;

import de.uniol.inf.is.odysseus.p2p_new.IAdvertisementListener;
import de.uniol.inf.is.odysseus.p2p_new.IAdvertisementManager;
import de.uniol.inf.is.odysseus.peer.rcp.RCPP2PNewPlugIn;

public class AdvertisementsView extends ViewPart implements IAdvertisementListener {

	private TableViewer advTable;
	private final List<Advertisement> advertisements = Lists.newArrayList();

	@Override
	public void createPartControl(Composite parent) {
		final Composite tableComposite = new Composite(parent, SWT.NONE);
		final TableColumnLayout tableColumnLayout = new TableColumnLayout();
		tableComposite.setLayout(tableColumnLayout);

		advTable = new TableViewer(tableComposite, SWT.MULTI | SWT.FULL_SELECTION);
		advTable.getTable().setHeaderVisible(true);
		advTable.getTable().setLinesVisible(true);
		advTable.setContentProvider(ArrayContentProvider.getInstance());

		/************* Index ****************/
		TableViewerColumn indexColumn = new TableViewerColumn(advTable, SWT.NONE);
		indexColumn.getColumn().setText("Index");
		indexColumn.setLabelProvider(new CellLabelProvider() {
			@Override
			public void update(ViewerCell cell) {
				Advertisement advertisement = (Advertisement) cell.getElement();
				cell.setText(String.valueOf(advertisements.indexOf(advertisement)));
			}
		});
		tableColumnLayout.setColumnData(indexColumn.getColumn(), new ColumnWeightData(1, 10, true));
		ColumnViewerSorter sorter = new ColumnViewerSorter(advTable, indexColumn) {
			@Override
			protected int doCompare(Viewer viewer, Object e1, Object e2) {
				Advertisement adv1 = (Advertisement) e1;
				Advertisement adv2 = (Advertisement) e2;
				int index1 = advertisements.indexOf(adv1);
				int index2 = advertisements.indexOf(adv2);

				return Integer.compare(index1, index2);
			}
		};
		sorter.setSorter(sorter, ColumnViewerSorter.NONE);

		/************* AdvertisementType ****************/
		TableViewerColumn advTypeColumn = new TableViewerColumn(advTable, SWT.NONE);
		advTypeColumn.getColumn().setText("Type");
		advTypeColumn.setLabelProvider(new CellLabelProvider() {
			@Override
			public void update(ViewerCell cell) {
				Advertisement advertisement = (Advertisement) cell.getElement();
				cell.setText(advertisement.getClass().getSimpleName());
			}
		});
		tableColumnLayout.setColumnData(advTypeColumn.getColumn(), new ColumnWeightData(10, 10, true));
		new ColumnViewerSorter(advTable, advTypeColumn) {
			@Override
			protected int doCompare(Viewer viewer, Object e1, Object e2) {
				Advertisement adv1 = (Advertisement) e1;
				Advertisement adv2 = (Advertisement) e2;
				String advName1 = adv1.getClass().getSimpleName();
				String advName2 = adv2.getClass().getSimpleName();

				return advName1.compareTo(advName2);
			}
		};

		advTable.setInput(advertisements);
		RCPP2PNewPlugIn.getAdvertisementManager().addAdvertisementListener(this);
	}

	@Override
	public void dispose() {
		RCPP2PNewPlugIn.getAdvertisementManager().removeAdvertisementListener(this);
		super.dispose();
	}

	@Override
	public void setFocus() {
		advTable.getTable().setFocus();
	}

	private void refreshTable() {
		if (advTable.getTable().isDisposed()) {
			return;
		}

		PlatformUI.getWorkbench().getDisplay().asyncExec(new Runnable() {

			@Override
			public void run() {
				synchronized (advertisements) {
					if (!advTable.getTable().isDisposed()) {
						advTable.refresh();
					}
				}
			}
		});
	}

	@Override
	public void advertisementAdded(IAdvertisementManager sender, Advertisement adv) {
		String className = adv.getClass().toString();
		if( className.startsWith("class de.uniol.inf.is.odysseus")) {
			synchronized (advertisements) {
				advertisements.add(adv);
			}
		}
		refreshTable();
	}

	@Override
	public void advertisementRemoved(IAdvertisementManager sender, Advertisement adv) {
		synchronized (advertisements) {
			advertisements.remove(adv);
		}
		refreshTable();
	}
}
