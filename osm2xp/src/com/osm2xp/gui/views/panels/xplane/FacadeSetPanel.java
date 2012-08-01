package com.osm2xp.gui.views.panels.xplane;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Spinner;

import com.osm2xp.exceptions.Osm2xpBusinessException;
import com.osm2xp.gui.views.panels.Osm2xpPanel;
import com.osm2xp.utils.FilesUtils;
import com.osm2xp.utils.helpers.XplaneOptionsHelper;
import com.osm2xp.utils.logging.Osm2xpLogger;

/**
 * FacadeSetPanel.
 * 
 * @author Benjamin Blanchet
 * 
 */
public class FacadeSetPanel extends Osm2xpPanel {

	private Combo comboFacade;
	private Label lblFacadeSet;
	private GridData gridFacade;
	private Spinner spinnerLod;
	private Button btnSlopedRoofs;
	private Button btnHardBuildings;

	public FacadeSetPanel(final Composite parent, final int style) {
		super(parent, style);
	}

	@Override
	protected void initLayout() {
		final GridLayout gridLayout = new GridLayout(2, false);
		gridLayout.marginHeight = 15;
		gridLayout.horizontalSpacing = 15;
		gridLayout.verticalSpacing = 15;
		setLayout(gridLayout);
		comboFacade.setLayoutData(gridFacade);
		spinnerLod.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, true, true,
				1, 1));
		btnSlopedRoofs.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false,
				true, 1, 1));
		btnHardBuildings.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, true,
				false, 1, 1));
	}

	@Override
	protected void initComponents() {
		lblFacadeSet = new Label(this, SWT.NONE);
		lblFacadeSet.setText("Facade set : ");
		comboFacade = new Combo(this, SWT.READ_ONLY);
		gridFacade = new GridData(SWT.LEFT, SWT.CENTER, true, true, 1, 1);
		gridFacade.minimumWidth = 200;
		final Label lblLod = new Label(this, SWT.NONE);
		lblLod.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, false, true, 1,
				1));
		lblLod.setText("L.O.D :");

		spinnerLod = new Spinner(this, SWT.BORDER);
		spinnerLod.setMaximum(100000);
		String[] items = null;
		try {
			items = FilesUtils.listFacadesSets().toArray(new String[] {});
			comboFacade.setItems(items);
		} catch (Osm2xpBusinessException e) {
			Osm2xpLogger.error("Error getting facades sets list", e);
		}

		if (XplaneOptionsHelper.getOptions().getFacadeSet() != null) {
			comboFacade
					.setText(XplaneOptionsHelper.getOptions().getFacadeSet());
		}
		btnSlopedRoofs = new Button(this, SWT.CHECK);
		btnSlopedRoofs.setText("Sloped roofs");
		btnHardBuildings = new Button(this, SWT.CHECK);
		btnHardBuildings.setText("Hard buildings");
	}

	@Override
	protected void bindComponents() {
		bindComponent(comboFacade, XplaneOptionsHelper.getOptions(),
				"facadeSet");
		bindComponent(spinnerLod, XplaneOptionsHelper.getOptions(), "facadeLod");
		bindComponent(btnSlopedRoofs, XplaneOptionsHelper.getOptions(),
				"generateSlopedRoofs");
		bindComponent(btnHardBuildings, XplaneOptionsHelper.getOptions(),
				"hardBuildings");
	}

	@Override
	protected void addComponentsListeners() {
	}
}
