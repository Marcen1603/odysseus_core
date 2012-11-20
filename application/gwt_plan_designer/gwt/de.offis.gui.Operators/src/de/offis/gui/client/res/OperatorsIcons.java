package de.offis.gui.client.res;

import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.ImageResource;

/**
 * Contains images so GWT-Compiler can optimize.
 *
 * @author Alexander Funk
 * 
 */
public interface OperatorsIcons extends ClientBundle {
	public static final OperatorsIcons INSTANCE =  GWT.create(OperatorsIcons.class);

	ImageResource arrow_left();

	ImageResource bullet_delete();

	ImageResource bullet_green();

	ImageResource bullet_red();

	ImageResource page_white_edit();

	ImageResource application_xp_terminal();

	ImageResource chart_organisation_add();

	ImageResource chart_organisation_delete();

	ImageResource large_tiles();

	ImageResource save_as();
	
	ImageResource database32();

	ImageResource packagered();
	
	ImageResource packagegreen();
	
	ImageResource box_open();
	
	ImageResource package_delete();
	
	ImageResource bullet_red_green();
}
