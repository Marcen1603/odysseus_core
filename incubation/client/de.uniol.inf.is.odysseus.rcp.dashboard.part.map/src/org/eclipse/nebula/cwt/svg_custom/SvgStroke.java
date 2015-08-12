/****************************************************************************
 * Copyright (c) 2008, 2009 Jeremy Dowdall
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Jeremy Dowdall <jeremyd@aspencloud.com> - initial API and implementation
 *****************************************************************************/
package org.eclipse.nebula.cwt.svg_custom;

import org.eclipse.nebula.cwt.svg_custom.SvgDocument.VARIABLE;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;

class SvgStroke extends SvgPaint {

	Float width = null;
	Integer lineCap = SWT.CAP_FLAT;
	Integer lineJoin = SWT.JOIN_MITER;

	SvgStroke(SvgGraphic parent) {
		super(parent);
	}
	
	@Override
	public void apply() {
		if(paintServer != null) {
			paintServer.apply(true);
		} else {
			if (isVariable){
				Object o = parent.getVariable(VARIABLE.LineColor);
				if (o != null){
					Color c = (Color) o;
					gc.setForeground(c);
				}
			}
			if (isVariable){
				Object o = parent.getVariable(VARIABLE.LineWidth);
				if (o != null){
					Integer i = (Integer) o;
				gc.setLineWidth( i );
				}
			}
			else{
				Color c = new Color(gc.getDevice(), color >> 16, (color & 0x00FF00) >> 8, color & 0x0000FF);
				gc.setForeground(c);
				c.dispose();
				gc.setLineWidth((int)Math.ceil(width));
			}

			
			gc.setLineCap(lineCap);
			gc.setLineJoin(lineJoin);
			gc.setAlpha((int)(255 * opacity));
		}
	}

}