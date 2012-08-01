package com.osm2xp.gui.perspectives;

import org.eclipse.ui.IPageLayout;
import org.eclipse.ui.IPerspectiveFactory;

/**
 * Xplane9ConfigurationPerspective.
 * 
 * @author Benjamin Blanchet
 * 
 */
public class Xplane9ConfigurationPerspective implements IPerspectiveFactory {

	@Override
	public void createInitialLayout(IPageLayout layout) {
		layout.setEditorAreaVisible(false);
		layout.setFixed(false);
	}

}
