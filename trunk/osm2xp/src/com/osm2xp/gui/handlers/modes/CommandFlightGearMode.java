package com.osm2xp.gui.handlers.modes;

import java.io.File;

import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.commands.IHandler;
import org.eclipse.core.commands.IHandlerListener;
import org.eclipse.core.resources.ResourcesPlugin;

import com.osm2xp.constants.Perspectives;
import com.osm2xp.gui.views.panels.generic.OutPutFormatPanel;
import com.osm2xp.utils.MiscUtils;
import com.osm2xp.utils.helpers.GuiOptionsHelper;

/**
 * CommandOsmMode.
 * 
 * @author Benjamin Blanchet
 * 
 */
public class CommandFlightGearMode implements IHandler {

	private static final String HTML_FILE = ResourcesPlugin.getWorkspace()
			.getRoot().getLocation()
			+ File.separator
			+ "resources"
			+ File.separator
			+ "html"
			+ File.separator
			+ "modes"
			+ File.separator
			+ "flightGear"
			+ File.separator + "index.html";

	@Override
	public void addHandlerListener(IHandlerListener handlerListener) {
	}

	@Override
	public void dispose() {

	}

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		GuiOptionsHelper.getOptions().setOutputFormat(
				Perspectives.PERSPECTIVE_FLIGHT_GEAR);
		MiscUtils.switchPerspective(Perspectives.PERSPECTIVE_FLIGHT_GEAR);
		OutPutFormatPanel.updateBrowserUrl(HTML_FILE);
		return null;
	}

	@Override
	public boolean isEnabled() {
		return true;
	}

	@Override
	public boolean isHandled() {
		return true;
	}

	@Override
	public void removeHandlerListener(IHandlerListener handlerListener) {

	}

}
