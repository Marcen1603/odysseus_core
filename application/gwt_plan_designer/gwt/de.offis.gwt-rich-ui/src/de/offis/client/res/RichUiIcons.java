package de.offis.client.res;

import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.ImageResource;

public interface RichUiIcons extends ClientBundle {
	public static final RichUiIcons INSTANCE =  GWT.create(RichUiIcons.class);
	ImageResource cat_closed();
	ImageResource cat_open();
}
