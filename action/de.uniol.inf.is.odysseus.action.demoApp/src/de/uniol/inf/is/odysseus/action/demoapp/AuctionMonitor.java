package de.uniol.inf.is.odysseus.action.demoapp;

import java.util.HashMap;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;

public class AuctionMonitor {
	private Table table;
	private HashMap<Integer, Integer> auctions;
	private Shell shell;
	private Display display;
	
	private static AuctionMonitor instance = new AuctionMonitor();


	private AuctionMonitor(){
		auctions = new HashMap<Integer, Integer>();
	}
	
	public Shell runApplication(Display display) {
		return this.init(display);
	}
	
	private Shell init(Display display) {
		this.display = display;
		
		//create shell
		this.shell = new Shell(display);
		shell.setLayout(new FillLayout());
		
		//create composite with scrollbars, holding table
		ScrolledComposite scrolledComposite = new ScrolledComposite(shell,
				SWT.H_SCROLL | SWT.V_SCROLL);
		scrolledComposite.setLayout(new FillLayout());

		scrolledComposite.setExpandVertical(true);
		scrolledComposite.setExpandHorizontal(true);
		
		//create table
		table = new Table(scrolledComposite, SWT.MULTI | SWT.FULL_SELECTION);
		table.setHeaderVisible(true);
		table.setLinesVisible(true);		
		
		//create columns
		String[] columnTitels = {"Auction-ID", "Item name", "Highest bid", "Bidder", "Status", "Time"}; 
		for (String title : columnTitels){
			TableColumn column = new TableColumn(table, SWT.NONE);
			column.setText(title);
		}
		
		for (TableColumn col : table.getColumns()){
			col.pack();
		}

		//set sizes for composite/shell
		scrolledComposite.setContent(table);
		Point point = table.computeSize(SWT.DEFAULT, SWT.DEFAULT);
		table.setSize(point);
		scrolledComposite.setMinSize(point);
		
		point = scrolledComposite.computeSize(SWT.DEFAULT, SWT.DEFAULT);
		shell.pack();
		shell.open();
		return shell;
	}
	
	public void updateData (final String[] data) {
		try {
			this.display.syncExec(new Runnable() {
				@Override
				public void run() {
						int id = Integer.valueOf(data[0]);
						Integer rowID = auctions.get(id);
						if (rowID == null){
							rowID = table.getItemCount();
							auctions.put(id, rowID);
							TableItem item = new TableItem(table, SWT.NONE, rowID.intValue());
							item.setText(data);
						}else {
							TableItem item = AuctionMonitor.this.table.getItem(rowID.intValue());
							item.setText(data);
						}
						
						AuctionMonitor.this.shell.pack();
						
					
				}
			});
		}catch (Exception e){
			System.err.println("Error updating");
		};
	}
	
	public static AuctionMonitor getInstance() {
		return instance;
	}

}
