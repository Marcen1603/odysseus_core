package de.uniol.inf.is.odysseus.rcp.viewer.view.swt.symbol.impl;

import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Point;

import de.uniol.inf.is.odysseus.rcp.viewer.view.graph.INodeView;
import de.uniol.inf.is.odysseus.rcp.viewer.view.graph.Vector;
import de.uniol.inf.is.odysseus.rcp.viewer.view.swt.symbol.SWTConnectionSymbolElement;

public class SWTArrowSymbolElement<C> extends SWTConnectionSymbolElement<C> {

	private static final int ARROW_SIZE = 20;
	private Color color;
	
	public SWTArrowSymbolElement( Color lineColor ) {
		color = lineColor;
	}

	@Override
	public void draw( Vector start, Vector end, float zoomFactor ) {
		
		if( getActualGC() == null ) {
			return;
		}
	
		getActualGC().setForeground( color );
		getActualGC().drawLine( (int)start.getX(), (int)start.getY(), (int)end.getX(), (int)end.getY() );
		
		// Pfeilspitze berechnen
		if( getConnectionView() == null )
			return;
		
		final INodeView<C> node = getConnectionView().getViewedEndNode();
		if( node == null )
			return;
		
		final float nx = (float)start.getX();
		final float ny = (float)start.getY();		
		final float mx = (float)end.getX();
		final float my = (float)end.getY();
		
		final float fx = (float)end.getX() - node.getWidth() * zoomFactor * 0.5f;
		final float fy = (float)end.getY() - node.getHeight() * zoomFactor * 0.5f;
		final float gx = fx + node.getWidth() * zoomFactor;
		final float gy = fy;
		final float hx = fx;
		final float hy = fy + node.getHeight() * zoomFactor;
		final float ix = fx + node.getWidth() * zoomFactor;
		final float iy = fy + node.getHeight() * zoomFactor;
		
		// Schnittpunktberechnung
		// Quelle: http://www.delphipraxis.net/topic85928.html
		Point cross = null;
		cross = crossingPoint( mx, my, nx, ny, hx, hy, ix, iy ); // unten
		if( cross == null ) {
			cross = crossingPoint( mx, my, nx, ny, fx, fy, gx, gy ); // oben
			if( cross == null ) {
				cross = crossingPoint( mx, my, nx, ny, fx, fy, hx, hy ); //links
				if( cross == null ) {
					cross = crossingPoint( mx, my, nx, ny, gx, gy, ix, iy ); //rechts
				}
			}
		}
		
		if( cross != null ) {
			
			// Pfeil zeichnen
			float ox = nx - mx;
			float oy = ny - my;
			
			final float length = (float)Math.sqrt( ox * ox + oy * oy  );
			ox = ( ox / length ) * ARROW_SIZE;
			oy = ( oy / length ) * ARROW_SIZE;
			
			final int px = (int)(cross.x + ( ox - oy * 0.3f ) * zoomFactor );
			final int py = (int)(cross.y + ( oy + ox * 0.3f ) * zoomFactor );
			final int qx = (int)(cross.x + ( ox + oy * 0.3f ) * zoomFactor );
			final int qy = (int)(cross.y + ( oy - ox * 0.3f ) * zoomFactor );
			
			int[] poly = new int[] {
					cross.x, cross.y, px, py, qx, qy
			};
			
			getActualGC().setBackground( color );
			getActualGC().fillPolygon( poly );
		}
	}
	
	@Override
	public void update(  ) {}

	private static Point crossingPoint( float ax1, float ay1, float ax2, float ay2, float bx1, float by1, float bx2, float by2 ) {
		float n = ax1*by2 + bx2*ay2 + ax2*by1 + bx1*ay1 - bx2*ay1 - ax1*by1 - bx1*ay2 - ax2*by2;
		if( n < 0.0000001f && n > -0.0000001f ) {
			// n ist gewissermaßen 0
			// kein Schnittpunkt
			return null;
		}
		
		float t = (ax1*ay2 + ax2*by1 + bx1*ay1 - ax2*ay1 - bx1*ay2 - ax1*by1) / n;
		if( t < 0 || t > 1 )
			return null;
		
		float s = (ax1*by2 + bx2*by1 + bx1*ay1 - bx2*ay1 - bx1*by2 - ax1*by1) / n;
		if( s < 0 || s > 1 )
			return null;
		
		return new Point((int)(bx1 + t*(bx2 - bx1)), (int)(by1 + t*(by2 - by1)));
	}
	
	
//	Function CrossingPoint(AX1,AY1,AX2,AY2, BX1,BY1,BX2,BY2:Integer; Var X,Y:Integer):Boolean;
//	Var
//	  T,S,N:Extended;
//	Begin
//	  Result:=false;
//	  // Nenner berechnen (Matrize)
//	  N:=AX1*BY2 + BX2*AY2 + AX2*BY1 + BX1*AY1 - BX2*AY1 - AX1*BY1 - BX1*AY2 - AX2*BY2;
//	  // Möglicherweise Abruch wegen Divison durch Null
//	  If N=0 then Exit;
//	  // T berechnen (Matrize)
//	  T:=(AX1*AY2 + AX2*BY1 + BX1*AY1 - AX2*AY1 - BX1*AY2 - AX1*BY1) / N;
//	  // Wenn T nicht im Intervall [0,1] liegt dann haben
//	  // beide Linien keinen Schnittpunkt
//	  If (T<0) or (T>1) then Exit;
//	  // S berechnen (Matrize)
//	  S:=(AX1*BY2 + BX2*BY1 + BX1*AY1 - BX2*AY1 - BX1*BY2 - AX1*BY1) / N;
//	  // Wenn S nicht im Intervall [0,1] liegt dann haben
//	  // beide Linien keinen Schnittpunkt
//	  If (S<0) or (S>1) then Exit;
//	  // Berechnung mit T
//	  X:=Round(BX1+T*(BX2-BX1));
//	  Y:=Round(BY1+T*(BY2-BY1));
//	  // Berechnung mit S
//	  // X:=Round(AX1+S*(AX2-AX1));
//	  // Y:=Round(AY1+S*(AY2-AY1));
//	  Result:=true;
//	End;
}
