package com.osm2xp.gui.views.panels.generic;

import java.io.File;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import math.geom2d.Point2D;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Spinner;
import org.eclipse.swt.widgets.Text;

import com.osm2xp.gui.views.LastFilesView;
import com.osm2xp.gui.views.panels.Osm2xpPanel;
import com.osm2xp.utils.helpers.GuiOptionsHelper;

/**
 * SceneryFilePanel.
 * 
 * @author Benjamin Blanchet
 * 
 */
public class SceneryFilePanel extends Osm2xpPanel {

	private static Label labelInputSceneFile;
	private static Text textInputSceneName;
	private static final String[] FILTER_NAMES = { "OSM files (*.osm,*.pbf;*.shp)" };
	private static final String[] FILTER_EXTS = { "*.osm;*.pbf;*.shp" };
	private static Button btnGenerateAllTiles;
	private static Spinner spinnerLatitude;
	private static Spinner spinnerLongitude;
	private Button btnBrowse;
	private GridData gridInputSceneFile;
	private Label lblSceneName;
	private GridData gridInputSceneName;
	private Group grpCoordinates;
	private GridData gridCoordinates;

	public SceneryFilePanel(final Composite parent, int style) {
		super(parent, SWT.BORDER);

	}

	/**
	 * Construct scene name
	 * 
	 * @return String the scene name
	 */
	private static String computeSceneName() {
		StringBuilder sceneName = new StringBuilder();
		// clean file extension
		File file = new File(GuiOptionsHelper.getOptions().getCurrentFilePath());
		String fileName = file.getName().substring(0,
				file.getName().indexOf("."));
		sceneName.append(fileName);
		if (GuiOptionsHelper.getOptions().isAppendHour()) {
			DateFormat dateFormat = new SimpleDateFormat("_dd_MM_yy_HH'_'mm");
			sceneName.append(dateFormat.format(new Date()));
		}
		return sceneName.toString();

	}

	public static void refreshCurrentFilePath() {
		labelInputSceneFile.setText(GuiOptionsHelper.getOptions()
				.getCurrentFilePath());
		GuiOptionsHelper.setSceneName(computeSceneName());
		String sceneName = computeSceneName();
		GuiOptionsHelper.setSceneName(sceneName);
		textInputSceneName.setText(sceneName);
	}

	public static Point2D getCoordinates() {
		if (btnGenerateAllTiles.getSelection()) {
			return null;
		} else {
			Point2D result = new Point2D(spinnerLatitude.getSelection(),
					spinnerLongitude.getSelection());
			return result;
		}
	}

	@Override
	protected void initLayout() {
		GridLayout gridLayout = new GridLayout(2, false);
		gridLayout.verticalSpacing = 15;
		gridLayout.horizontalSpacing = 15;
		setLayout(gridLayout);
		lblSceneName.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, false,
				true, 1, 1));
		labelInputSceneFile.setLayoutData(gridInputSceneFile);
		textInputSceneName.setLayoutData(gridInputSceneName);
		btnGenerateAllTiles.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER,
				false, true, 1, 1));
		grpCoordinates.setLayout(new GridLayout(7, false));
		grpCoordinates.setLayoutData(gridCoordinates);
	}

	@Override
	protected void initComponents() {

		// grid for file
		gridInputSceneFile = new GridData(SWT.LEFT, SWT.CENTER, true, true, 1,
				1);
		gridInputSceneFile.widthHint = 600;
		btnBrowse = new Button(this, SWT.NONE);
		btnBrowse.setText("Browse");
		// file text edit
		labelInputSceneFile = new Label(this, SWT.NONE);
		// scene name label
		lblSceneName = new Label(this, SWT.NONE);
		lblSceneName.setText("Scene name :");
		// grid for scene name
		gridInputSceneName = new GridData(SWT.LEFT, SWT.CENTER, true, true, 1,
				1);
		gridInputSceneName.widthHint = 300;
		// scene text edit
		textInputSceneName = new Text(this, SWT.BORDER);
		btnGenerateAllTiles = new Button(this, SWT.CHECK);
		btnGenerateAllTiles.setSelection(true);
		btnGenerateAllTiles.setText("Generate all tiles");
		grpCoordinates = new Group(this, SWT.NONE);
		gridCoordinates = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gridCoordinates.widthHint = 294;
		grpCoordinates.setText("Coordinates");
		grpCoordinates.setVisible(false);
		Label lblLatitude = new Label(grpCoordinates, SWT.NONE);
		lblLatitude.setText("Latitude:");
		new Label(grpCoordinates, SWT.NONE);
		spinnerLatitude = new Spinner(grpCoordinates, SWT.BORDER);
		spinnerLatitude.setMaximum(200);
		spinnerLatitude.setMinimum(-200);
		Label lblLongitude = new Label(grpCoordinates, SWT.NONE);
		lblLongitude.setText("Longitude:");
		spinnerLongitude = new Spinner(grpCoordinates, SWT.BORDER);
		spinnerLongitude.setMaximum(200);
		spinnerLongitude.setMinimum(-200);
		new Label(grpCoordinates, SWT.NONE);
		new Label(grpCoordinates, SWT.NONE);

	}

	@Override
	protected void bindComponents() {
		// TODO Auto-generated method stub

	}

	@Override
	protected void addComponentsListeners() {
		btnBrowse.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				FileDialog dlg = new FileDialog(getParent().getShell(),
						SWT.OPEN);
				dlg.setFilterNames(FILTER_NAMES);
				dlg.setFilterExtensions(FILTER_EXTS);
				String fileName = dlg.open();
				if (fileName != null) {
					GuiOptionsHelper.addUsedFile(fileName);
					GuiOptionsHelper.getOptions().setCurrentFilePath(fileName);
					labelInputSceneFile.setText(fileName);
					String sceneName = computeSceneName();
					GuiOptionsHelper.setSceneName(sceneName);
					textInputSceneName.setText(sceneName);
					LastFilesView.refreshList();
					if (fileName.toUpperCase().contains(".SHP")) {
						GuiOptionsHelper.askShapeFileNature(getShell());
					}
				}
			}
		});

		textInputSceneName.addModifyListener(new ModifyListener() {
			public void modifyText(ModifyEvent e) {
				GuiOptionsHelper.setSceneName(textInputSceneName.getText());
			}
		});
		btnGenerateAllTiles.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseDown(MouseEvent e) {
				grpCoordinates.setVisible(btnGenerateAllTiles.getSelection());
			}
		});

	}

}
