package de.uniol.inf.is.odysseus.trajectory.physical.compare;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.vividsolutions.jts.geom.Point;

public class OWDTrajectory {
	
	private int level;
	
	public List<GridCell> adjacenceCells = new ArrayList<>();
	
	int size = 1;
	
	public OWDTrajectory(List<Point> points, int level) {
		
		adjacenceCells.add(null);
		
		for(int i = 0; i < points.size() - 1; i++) {
			//vom i-ten zum (i+1)-ten punkt die Gitterzellen ermitteln
			
			List<GridCell> list = new ArrayList<>();
			
			adjacenceCells.remove(0);
			
			Point p1 = points.get(i);
			Point p2 = points.get(i + 1);
		
			
			double p1X = p1.getX();
			double p1Y = p1.getY();
			
			double p2X = p2.getX();
			double p2Y = p2.getY();
					
			
			// Steigungen ausrechnen
			double mx = (p2Y - p1Y) / Math.abs(p2X - p1X);
			double my = (p2X - p1X) / Math.abs(p2Y - p1Y);
			
			double xCurr = p1X;
			double yCurr = p1Y;
			
			System.out.println(mx);
			
			while(xCurr < p2X) {
				
				System.out.println((int)xCurr);
				System.out.println((int)yCurr);
				System.out.println("-------------");
				
				list.add(new GridCell((int)xCurr, (int)yCurr));
				
				double xNext = (int)xCurr + 1;
				double yNext = (int)yCurr + 1;
				
				double yStep = (xNext - xCurr) * mx;
				double xStep = (yNext - yCurr) * my;
				
				if(xCurr + xStep >= xNext) {
					xCurr = xNext;
					yCurr += yStep;
				} else {
					//yCurr + yStep >= yNext
					yCurr = yNext;
					xCurr += xStep;
				}	
			}
			adjacenceCells.addAll(list);
		}
	}
	
	public class GridCell {
		public int x;
		public int y;
		
		public GridCell(int x, int y) {
			this.x = x;
			this.y = y;
		}
	}
}