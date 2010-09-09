package de.uniol.inf.is.odysseus.action.demoapp;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;

public class AuctionMonitor {
	public enum AuctionStatus {GREEN, ORANGE, RED}
	private Table table;
	private Map<Integer, Integer> auctions;
	
	private Map<Integer, AuctionStatus> auctionStatus;
	private Shell shell;
	
	private Display display;;
	
	private static AuctionMonitor instance = new AuctionMonitor();

	public static AuctionMonitor getInstance() {
		return instance;
	}
	
	private AuctionMonitor(){
		this.auctions = new HashMap<Integer, Integer>();
		this.auctionStatus = new HashMap<Integer, AuctionStatus>();
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
		String[] columnTitels = {"Auction-ID", "Item name", "Highest bid", "Bidder", "Time", "Status"}; 
		for (String title : columnTitels){
			TableColumn column = new TableColumn(table, SWT.NONE);
			column.setText(title);
		}
		
		for (TableColumn col : table.getColumns()){
			col.pack();
			if (col.getText().equals("Item name") || col.getText().equals("Bidder")){
				col.setWidth(col.getWidth()*2);
			}else if (col.getText().equals("Time")){
				col.setWidth(col.getWidth()*3);
			}
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
	
	public Shell runApplication(Display display) {
		return this.init(display);
	}
	
	public void updateData (final String[] data, final AuctionStatus status) {
		final int id = Integer.valueOf(data[0]);
		
		this.auctionStatus.put(id, status);
		try {
			this.display.syncExec(new Runnable() {
				@Override
				public void run() {
						Integer rowID = auctions.get(id);
						TableItem item = null;
						
						if (rowID == null){
							rowID = table.getItemCount();
							AuctionMonitor.this.auctions.put(id, rowID);
							item = new TableItem(AuctionMonitor.this.table, 
									SWT.NONE, rowID.intValue());
							item.setText(data);
						}else {
							 item = AuctionMonitor.this.table.getItem(rowID.intValue());
							item.setText(data);						
						}
						
						Color bgColor = null; 
						switch (status){
						case GREEN:
							bgColor = AuctionMonitor.this.display.getSystemColor(SWT.COLOR_GREEN);
							break;
						case ORANGE:
							bgColor = AuctionMonitor.this.display.getSystemColor(SWT.COLOR_YELLOW);
							break;
						case RED:
						default:
							bgColor = AuctionMonitor.this.display.getSystemColor(SWT.COLOR_RED);
							break;
						}
						item.setBackground(data.length, bgColor);
						item.setText(data.length, "			");
						
						AuctionMonitor.this.shell.pack();
						
					
				}
			});
		}catch (Exception e){
			System.err.println("Error updating");
		};
	}

}
