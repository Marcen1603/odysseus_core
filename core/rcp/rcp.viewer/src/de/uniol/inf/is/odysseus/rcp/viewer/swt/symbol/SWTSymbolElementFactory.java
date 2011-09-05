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
package de.uniol.inf.is.odysseus.rcp.viewer.swt.symbol;

import java.util.Map;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.PlatformUI;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.rcp.viewer.swt.symbol.impl.SWTArrowSymbolElement;
import de.uniol.inf.is.odysseus.rcp.viewer.swt.symbol.impl.SWTCircleSymbolElement;
import de.uniol.inf.is.odysseus.rcp.viewer.swt.symbol.impl.SWTLineConnectionSymbolElement;
import de.uniol.inf.is.odysseus.rcp.viewer.swt.symbol.impl.SWTFillCircleSymbolElement;
import de.uniol.inf.is.odysseus.rcp.viewer.swt.symbol.impl.SWTFillRectSymbolElement;
import de.uniol.inf.is.odysseus.rcp.viewer.swt.symbol.impl.SWTImageSymbolElement;
import de.uniol.inf.is.odysseus.rcp.viewer.swt.symbol.impl.SWTInvisibleSymbolElement;
import de.uniol.inf.is.odysseus.rcp.viewer.swt.symbol.impl.SWTRectSymbolElement;
import de.uniol.inf.is.odysseus.rcp.viewer.swt.symbol.impl.SWTSelectionSymbolElement;
import de.uniol.inf.is.odysseus.rcp.viewer.swt.symbol.impl.SWTSelectivitySymbolElement;
import de.uniol.inf.is.odysseus.rcp.viewer.symbol.IConnectionSymbolElement;
import de.uniol.inf.is.odysseus.rcp.viewer.symbol.ISymbolElement;
import de.uniol.inf.is.odysseus.rcp.viewer.symbol.ISymbolElementFactory;
import de.uniol.inf.is.odysseus.rcp.viewer.symbol.SymbolElementInfo;


public class SWTSymbolElementFactory<C> implements ISymbolElementFactory<C> {
	
	private static final Logger logger = LoggerFactory.getLogger( SWTSymbolElementFactory.class );
	
	@Override
	public ISymbolElement<C> createForNode( SymbolElementInfo info ) {
		ISymbolElement<C> element = null;
		if( info == null )
			return null;
		
		final String type = info.getType();
		final Map<String, String> params = info.getParams();
		
		if( type.equals( "fillCircle" ))
			element = new SWTFillCircleSymbolElement<C>( getColorFromParams(params));

		if( type.equals( "circle" ))
			element = new SWTCircleSymbolElement<C>(getColorFromParams(params));

		if( type.equals( "fillRectangle" ))
			element = new SWTFillRectSymbolElement<C>(getColorFromParams(params));

		if( type.equals( "rectangle" ))
			element = new SWTRectSymbolElement<C>(getColorFromParams(params));
		
		if( type.equals( "selector" )) 
			element = new SWTSelectionSymbolElement<C>(getColorFromParams(params));
		
		if( type.equals( "image" )) 
			element = createImageSymbolElement(params);
		
		if( type.equals( "invisible" ))
			element = new SWTInvisibleSymbolElement< C >();
		
		if( type.equals( "selectivity" ))
			element = new SWTSelectivitySymbolElement<C>();
		
		if (element == null ) {
			element = new SWTRectSymbolElement<C>( Display.getDefault().getSystemColor( SWT.COLOR_BLACK ));
			logger.warn( "Symboltype '" + type + "' not known!" );
		} 	
		return element;
		
	}

	@Override
	public ISymbolElement<C> createDefaultSymbolElement() {
		SWTRectSymbolElement<C> ele = new SWTRectSymbolElement<C>( Display.getDefault().getSystemColor( SWT.COLOR_BLACK ));
		return ele;
	}
	
	private ISymbolElement<C> createImageSymbolElement( Map<String, String> params) {
		final String resourceName = params.get( "resource" );
		if( resourceName == null || resourceName.length() == 0 ) {
			logger.warn( "Parameter " + resourceName + " for ImageSymbol not found or invalid" );
			return createDefaultSymbolElement();
		}
		
		return new SWTImageSymbolElement<C>(resourceName);
	}
	
	private Color getColorFromParams( Map<String, String> params ) {
		int r = clampInt(tryParseInt( params.get( "r" ), 0), 0, 255 );
		int g = clampInt(tryParseInt( params.get( "g" ), 0), 0, 255 );
		int b = clampInt(tryParseInt( params.get( "b" ), 0), 0, 255);
		
		return new Color(PlatformUI.getWorkbench().getDisplay(), r, g, b );
	}

	private static int tryParseInt( String str, int defValue ) {
		if( str == null )
			return defValue;
		
		int res = 0;
		try {
			res = Integer.parseInt( str );
			return res;
		} catch( NumberFormatException ex ) {
			if( logger.isWarnEnabled() )
				logger.warn( "Could not parseInt:" + str );
			return defValue;
		}
	}
	
	private static int clampInt( int value, int min, int max ) {
		return Math.max( min, Math.min( value, max ) );
	}

	@Override
	public IConnectionSymbolElement<C> createForConnection( String type ) {
		Display display = PlatformUI.getWorkbench().getDisplay();
		if( "Arrow".equals(type))
			return new SWTArrowSymbolElement< C >( display.getSystemColor( SWT.COLOR_BLACK ));
		
		return new SWTLineConnectionSymbolElement<C>( display.getSystemColor( SWT.COLOR_BLACK ) );
	}
}
