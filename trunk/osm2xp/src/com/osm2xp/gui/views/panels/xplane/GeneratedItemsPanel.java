package com.osm2xp.gui.views.panels.xplane;

import org.eclipse.core.databinding.beans.PojoObservables;
import org.eclipse.jface.databinding.swt.SWTObservables;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;

import com.osm2xp.gui.views.panels.Osm2xpPanel;
import com.osm2xp.utils.helpers.XplaneOptionsHelper;

/**
 * GeneratedItemsPanel.
 * 
 * @author Benjamin Blanchet
 * 
 */
public class GeneratedItemsPanel extends Osm2xpPanel {

	private Button btnGenerateBuildings;
	private Button btnGenerateObjects;
	private Button btnGenerateForests;
	private Button btnGenerateStreetLights;

	public GeneratedItemsPanel(final Composite parent, final int style) {
		super(parent, style);
	}

	@Override
	protected void initComponents() {
		btnGenerateBuildings = new Button(this, SWT.CHECK);
		btnGenerateBuildings.setText("Generate facades buildings");
		btnGenerateObjects = new Button(this, SWT.CHECK);
		btnGenerateObjects.setText("Generate 3D objects");
		btnGenerateForests = new Button(this, SWT.CHECK);
		btnGenerateForests.setText("Generate Forests");
		btnGenerateStreetLights = new Button(this, SWT.CHECK);
		btnGenerateStreetLights.setText("Generate street lights");
	}

	@Override
	protected void bindComponents() {
		bindingContext.bindValue(SWTObservables
				.observeSelection(btnGenerateBuildings), PojoObservables
				.observeValue(XplaneOptionsHelper.getOptions(),
						"generateBuildings"));
		bindingContext.bindValue(SWTObservables
				.observeSelection(btnGenerateObjects), PojoObservables
				.observeValue(XplaneOptionsHelper.getOptions(), "generateObj"));
		bindingContext.bindValue(SWTObservables
				.observeSelection(btnGenerateForests), PojoObservables
				.observeValue(XplaneOptionsHelper.getOptions(), "generateFor"));
		bindingContext.bindValue(SWTObservables
				.observeSelection(btnGenerateStreetLights), PojoObservables
				.observeValue(XplaneOptionsHelper.getOptions(),
						"generateStreetLights"));
	}

	@Override
	protected void addComponentsListeners() {

	}

	@Override
	protected void initLayout() {

		GridLayout gridLayout = new GridLayout(2, false);
		gridLayout.verticalSpacing = 15;
		gridLayout.horizontalSpacing = 15;
		gridLayout.marginHeight = 15;
		setLayout(gridLayout);
		btnGenerateBuildings.setLayoutData(new GridData(SWT.FILL, SWT.FILL,
				false, false, 1, 1));
		btnGenerateObjects.setLayoutData(new GridData(SWT.FILL, SWT.FILL,
				false, false, 1, 1));
		btnGenerateForests.setLayoutData(new GridData(SWT.FILL, SWT.FILL,
				false, false, 1, 1));
		btnGenerateStreetLights.setLayoutData(new GridData(SWT.FILL, SWT.FILL,
				false, false, 1, 1));
	}

}
