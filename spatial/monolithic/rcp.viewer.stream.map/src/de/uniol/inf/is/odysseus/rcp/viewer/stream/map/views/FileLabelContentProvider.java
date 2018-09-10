package de.uniol.inf.is.odysseus.rcp.viewer.stream.map.views;

import java.io.File;

import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.swt.graphics.Image;

public class FileLabelContentProvider extends LabelProvider {

//	private static final Image folderImage = AbstractUIPlugin.imageDescriptorFromPlugin("de.vogella.rcp.intro.filebrowser", "icons/folder.gif").createImage();
//	private static final Image driveImage = AbstractUIPlugin.imageDescriptorFromPlugin("de.vogella.rcp.intro.filebrowser", "icons/filenav_nav.gif").createImage();
//	private static final Image fileImage = AbstractUIPlugin.imageDescriptorFromPlugin("de.vogella.rcp.intro.filebrowser", "icons/file_obj.gif").createImage();

	@Override
	public Image getImage(Object element) {
//		File file = (File) element;
//		if (file.isDirectory())
//			return file.getParent() != null ? folderImage : driveImage;
		return null;
	}

	@Override
	public String getText(Object element) {
		String fileName = ((File) element).getName();
		if (fileName.length() > 0) {
			return fileName;
		}
		return ((File) element).getPath();
	}
}
