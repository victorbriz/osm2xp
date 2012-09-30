package com.osm2xp.gui.handlers;

import org.apache.commons.lang.StringUtils;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.commands.IHandler;
import org.eclipse.core.commands.IHandlerListener;

import com.osm2xp.constants.Osm2xpConstants;
import com.osm2xp.constants.Perspectives;
import com.osm2xp.controllers.BuildController;
import com.osm2xp.exceptions.Osm2xpBusinessException;
import com.osm2xp.gui.dialogs.utils.Osm2xpDialogsHelper;
import com.osm2xp.utils.helpers.FsxOptionsHelper;
import com.osm2xp.utils.helpers.GuiOptionsHelper;
import com.osm2xp.utils.helpers.XplaneOptionsHelper;
import com.osm2xp.utils.logging.Osm2xpLogger;

/**
 * CommandBuildScene.
 * 
 * @author Benjamin Blanchet
 * 
 */
public class CommandBuildScene implements IHandler {

	@Override
	public void addHandlerListener(IHandlerListener handlerListener) {
		// TODO Auto-model.options method stub

	}

	@Override
	public void dispose() {
		// TODO Auto-model.options method stub

	}

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {

		if (getConfigurationErrors() == null) {
			BuildController bc = new BuildController();
			try {
				bc.launchBuild();
			} catch (Osm2xpBusinessException e) {
				Osm2xpLogger.error("Error building scene.", e);
			}
		} else {
			Osm2xpDialogsHelper
					.displayErrorDialog("Bad configuration, please check following errors:\n"
							+ getConfigurationErrors());
		}
		return null;
	}

	private String getConfigurationErrors() {

		StringBuilder errors = new StringBuilder();
		// Common validation
		if (GuiOptionsHelper.getOptions().getCurrentFilePath() == null) {
			errors.append("-No osm file selected.\n");
		}
		// Xplane validation
		if (GuiOptionsHelper.getOptions().getOutputFormat()
				.equals(Perspectives.PERSPECTIVE_XPLANE10)
				|| GuiOptionsHelper.getOptions().getOutputFormat()
						.equals(Perspectives.PERSPECTIVE_XPLANE9)) {

			if (XplaneOptionsHelper.getOptions().isGenerateBuildings()
					&& XplaneOptionsHelper.getOptions().getFacadeSet() == null) {
				errors.append("-No facade set selected.\n");
			}
		}
		// FSX validation
		if (GuiOptionsHelper.getOptions().getOutputFormat()
				.equals(Perspectives.PERSPECTIVE_FSX)) {
			if (StringUtils.isBlank(FsxOptionsHelper.getOptions()
					.getBglCompPath())) {
				errors.append("-bglComp.exe location not set!\n");
			}
		}
		if (errors.length() > 0) {
			return errors.toString();
		}
		return null;

	}

	@Override
	public boolean isEnabled() {
		// TODO Auto-model.options method stub
		return true;
	}

	@Override
	public boolean isHandled() {
		// TODO Auto-model.options method stub
		return true;
	}

	@Override
	public void removeHandlerListener(IHandlerListener handlerListener) {
		// TODO Auto-model.options method stub

	}

}
