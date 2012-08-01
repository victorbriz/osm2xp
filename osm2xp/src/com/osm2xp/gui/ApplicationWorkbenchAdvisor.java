package com.osm2xp.gui;

import org.eclipse.ui.application.IWorkbenchConfigurer;
import org.eclipse.ui.application.IWorkbenchWindowConfigurer;
import org.eclipse.ui.application.WorkbenchAdvisor;
import org.eclipse.ui.application.WorkbenchWindowAdvisor;

import com.osm2xp.constants.Perspectives;

/**
 * ApplicationWorkbenchAdvisor
 * 
 * @author Benjamin Blanchet
 * 
 */
public class ApplicationWorkbenchAdvisor extends WorkbenchAdvisor {

	public WorkbenchWindowAdvisor createWorkbenchWindowAdvisor(
			IWorkbenchWindowConfigurer configurer) {
		return new ApplicationWorkbenchWindowAdvisor(configurer);
	}

	public String getInitialWindowPerspectiveId() {
		return Perspectives.PERSPECTIVE_STARTUP;

	}

	@Override
	public void initialize(IWorkbenchConfigurer configurer) {
		// super.initialize(configurer);
		// configurer.setSaveAndRestore(true);

	}
}
