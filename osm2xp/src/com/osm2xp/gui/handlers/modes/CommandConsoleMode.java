package com.osm2xp.gui.handlers.modes;

import java.io.IOException;

import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.commands.IHandler;
import org.eclipse.core.commands.IHandlerListener;
import org.eclipse.core.runtime.FileLocator;

import com.osm2xp.constants.Osm2xpConstants;
import com.osm2xp.gui.Activator;
import com.osm2xp.gui.views.panels.generic.OutPutFormatPanel;
import com.osm2xp.utils.MiscUtils;
import com.osm2xp.utils.helpers.GuiOptionsHelper;
import com.osm2xp.utils.logging.Osm2xpLogger;

/**
 * CommandConsoleMode.
 * 
 * @author Benjamin Blanchet
 * 
 */
public class CommandConsoleMode implements IHandler {

	private static final String HTML_FILE = "html/modes/console/index.html";

	@Override
	public void addHandlerListener(IHandlerListener handlerListener) {
	}

	@Override
	public void dispose() {

	}

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		GuiOptionsHelper.getOptions().setOutputFormat(
				Osm2xpConstants.OUTPUT_FORMAT_CONSOLE);
		MiscUtils.switchPerspective(Osm2xpConstants.OUTPUT_FORMAT_CONSOLE);
		String url;
		try {
			url = FileLocator.toFileURL(
					Activator.getDefault().getBundle().getEntry(HTML_FILE))
					.getPath();
			OutPutFormatPanel.updateBrowserUrl(url);
		} catch (IOException e) {
			Osm2xpLogger.error(e.getMessage());
		}

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
