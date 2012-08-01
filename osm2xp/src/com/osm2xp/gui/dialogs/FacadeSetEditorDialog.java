package com.osm2xp.gui.dialogs;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.ColumnLabelProvider;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.ColorDialog;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Spinner;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.Text;

import com.osm2xp.exceptions.Osm2xpBusinessException;
import com.osm2xp.model.facades.Facade;
import com.osm2xp.model.facades.FacadeSet;
import com.osm2xp.utils.helpers.FacadeSetHelper;
import com.osm2xp.utils.logging.Osm2xpLogger;

/**
 * FacadeSetEditorDialog.
 * 
 * @author Benjamin Blanchet
 * 
 */
public class FacadeSetEditorDialog extends Dialog {
	private Text textName;
	private Text textAuthor;
	private Text textRoofColor;
	private Text textWallColor;
	private String facadeSetFolder;
	private Table table;
	private TableViewer viewer;
	private Facade currentFacade;
	private FacadeSet facadeSet;
	private Button btnIndustrial;
	private Button btnCommercial;
	private Button btnResidential;
	private Button btnSlopedRoof;
	private Button btnSimplebuildingOnly;
	private Spinner spinnerMinVector;
	private Spinner spinnerMaxVector;
	private Spinner spinnerMinHeight;
	private Spinner spinnerMaxHeight;
	private Group grpFacadeFile;

	/**
	 * Create the dialog.
	 * 
	 * @param parentShell
	 */
	public FacadeSetEditorDialog(Shell parentShell, String facadeSetFolder) {
		super(parentShell);
		this.facadeSetFolder = facadeSetFolder;
		this.facadeSet = FacadeSetHelper.getFacadeSet(facadeSetFolder);
	}

	/**
	 * Create contents of the dialog.
	 * 
	 * @param parent
	 */
	@Override
	protected Control createDialogArea(Composite parent) {
		Composite container = (Composite) super.createDialogArea(parent);
		container.setLayout(new FillLayout(SWT.HORIZONTAL));

		Group groupFacadesList = new Group(container, SWT.NONE);
		groupFacadesList.setLayout(new FillLayout(SWT.HORIZONTAL));
		viewer = new TableViewer(groupFacadesList, SWT.FULL_SELECTION);
		final TableViewerColumn colFile = new TableViewerColumn(viewer,
				SWT.NONE, 0);
		final TableColumn column = colFile.getColumn();
		column.setText("file");
		column.setWidth(450);
		column.setResizable(false);
		column.setMoveable(false);

		colFile.setLabelProvider(new ColumnLabelProvider() {
			@Override
			public String getText(Object element) {
				Facade p = (Facade) element;
				return p.getFile();
			}
		});

		table = viewer.getTable();
		table.setHeaderVisible(false);
		table.setLinesVisible(false);
		viewer.setContentProvider(new ArrayContentProvider());
		viewer.setInput(facadeSet.getFacades());

		viewer.addSelectionChangedListener(new ISelectionChangedListener() {

			@Override
			public void selectionChanged(SelectionChangedEvent event) {
				IStructuredSelection selection = (IStructuredSelection) viewer
						.getSelection();
				currentFacade = (Facade) selection.getFirstElement();
				updateProperties();

			}

		});

		Group groupProperties = new Group(container, SWT.NONE);
		groupProperties.setLayout(new GridLayout(1, false));

		Group grpFacadeSetProperties = new Group(groupProperties, SWT.NONE);
		GridData gd_grpFacadeSetProperties = new GridData(SWT.FILL, SWT.CENTER,
				true, false, 1, 1);
		gd_grpFacadeSetProperties.heightHint = 187;
		grpFacadeSetProperties.setLayoutData(gd_grpFacadeSetProperties);
		grpFacadeSetProperties.setText("Facade set properties");
		grpFacadeSetProperties.setBounds(0, 0, 70, 80);
		grpFacadeSetProperties.setLayout(new GridLayout(2, false));

		Label labelName = new Label(grpFacadeSetProperties, SWT.NONE);
		labelName.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false,
				false, 1, 1));
		labelName.setText("Facade set name : ");

		textName = new Text(grpFacadeSetProperties, SWT.BORDER);
		textName.addModifyListener(new ModifyListener() {
			public void modifyText(ModifyEvent e) {
				facadeSet.setName(textName.getText());
			}
		});
		textName.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false,
				1, 1));
		if (facadeSet.getName() != null) {
			textName.setText(facadeSet.getName());
		}
		Label labelAuthor = new Label(grpFacadeSetProperties, SWT.NONE);
		labelAuthor.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false,
				false, 1, 1));
		labelAuthor.setText("Author : ");

		textAuthor = new Text(grpFacadeSetProperties, SWT.BORDER);
		textAuthor.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true,
				false, 1, 1));
		textAuthor.addModifyListener(new ModifyListener() {
			public void modifyText(ModifyEvent e) {
				facadeSet.setAuthor(textAuthor.getText());
			}
		});

		if (facadeSet.getAuthor() != null) {
			textAuthor.setText(facadeSet.getAuthor());
		}
		Label labelDescription = new Label(grpFacadeSetProperties, SWT.NONE);
		labelDescription.setLayoutData(new GridData(SWT.FILL, SWT.CENTER,
				false, false, 1, 1));
		labelDescription.setText("Description");

		final StyledText styledTextDescription = new StyledText(
				grpFacadeSetProperties, SWT.BORDER);
		styledTextDescription.setLayoutData(new GridData(SWT.FILL, SWT.FILL,
				true, true, 1, 1));
		styledTextDescription.addModifyListener(new ModifyListener() {
			public void modifyText(ModifyEvent e) {
				facadeSet.setDescription(styledTextDescription.getText());
			}
		});
		if (facadeSet.getDescription() != null) {
			styledTextDescription.setText(facadeSet.getDescription());
		}
		grpFacadeFile = new Group(groupProperties, SWT.NONE);
		grpFacadeFile.setVisible(false);
		grpFacadeFile.setLayout(new GridLayout(2, false));
		grpFacadeFile.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true,
				true, 1, 1));
		grpFacadeFile.setText("Facade file");

		Label labelRoofColor = new Label(grpFacadeFile, SWT.NONE);
		labelRoofColor.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false,
				false, 1, 1));
		labelRoofColor.setText("Roof color : ");

		textRoofColor = new Text(grpFacadeFile, SWT.BORDER);

		textRoofColor.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true,
				false, 1, 1));

		textRoofColor.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseDown(MouseEvent e) {
				ColorDialog dlg = new ColorDialog(getShell());
				dlg.setText("Choose roof Color");
				RGB rgb = dlg.open();
				if (rgb != null) {
					textRoofColor.setText(rgb.red + "," + rgb.green + ","
							+ rgb.blue);
					currentFacade.setRoofColor(textRoofColor.getText());
				}
			}
		});
		Label labelWallColor = new Label(grpFacadeFile, SWT.NONE);
		labelWallColor.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false,
				false, 1, 1));
		labelWallColor.setText("Wall color :");

		textWallColor = new Text(grpFacadeFile, SWT.BORDER);
		textWallColor.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true,
				false, 1, 1));

		textWallColor.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseDown(MouseEvent e) {
				ColorDialog dlg = new ColorDialog(getShell());
				dlg.setText("Choose wall Color");
				RGB rgb = dlg.open();
				if (rgb != null) {
					textWallColor.setText(rgb.red + "," + rgb.green + ","
							+ rgb.blue);
					currentFacade.setWallColor(textWallColor.getText());
				}
			}
		});
		new Label(grpFacadeFile, SWT.NONE);
		new Label(grpFacadeFile, SWT.NONE);

		btnIndustrial = new Button(grpFacadeFile, SWT.CHECK);
		btnIndustrial.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				currentFacade.setIndustrial(btnIndustrial.getSelection());
			}
		});
		btnIndustrial.setText("Industrial");

		btnResidential = new Button(grpFacadeFile, SWT.CHECK);
		btnResidential.setText("Residential");
		btnResidential.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				currentFacade.setResidential(btnResidential.getSelection());
			}
		});
		btnCommercial = new Button(grpFacadeFile, SWT.CHECK);
		btnCommercial.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false,
				false, 1, 1));
		btnCommercial.setText("Commercial");
		btnCommercial.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				currentFacade.setCommercial(btnCommercial.getSelection());
			}
		});
		btnSimplebuildingOnly = new Button(grpFacadeFile, SWT.CHECK);
		btnSimplebuildingOnly.setText("Simple building only");
		btnSimplebuildingOnly.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				currentFacade.setSimpleBuildingOnly(btnSimplebuildingOnly
						.getSelection());
			}
		});
		btnSlopedRoof = new Button(grpFacadeFile, SWT.CHECK);
		btnSlopedRoof.setText("Sloped roof");
		btnSlopedRoof.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				currentFacade.setSloped(btnSlopedRoof.getSelection());
				currentFacade.setResidential(true);
				currentFacade.setSimpleBuildingOnly(true);
			}
		});
		new Label(grpFacadeFile, SWT.NONE);
		new Label(grpFacadeFile, SWT.NONE);
		new Label(grpFacadeFile, SWT.NONE);
		new Label(grpFacadeFile, SWT.NONE);
		new Label(grpFacadeFile, SWT.NONE);

		Label labelMinVector = new Label(grpFacadeFile, SWT.NONE);
		labelMinVector.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false,
				false, 1, 1));
		labelMinVector.setText("Minimum vector length :");

		spinnerMinVector = new Spinner(grpFacadeFile, SWT.BORDER);
		spinnerMinVector.addModifyListener(new ModifyListener() {
			public void modifyText(ModifyEvent e) {
				if (spinnerMinVector.getSelection() > 0) {
					currentFacade.setMinVectorLength((double) spinnerMinVector
							.getSelection() / 100);
				}

			}
		});
		spinnerMinVector.setMaximum(1000);
		spinnerMinVector.setIncrement(100);
		spinnerMinVector.setDigits(2);
		spinnerMinVector.setLayoutData(new GridData(SWT.FILL, SWT.CENTER,
				false, false, 1, 1));

		Label lblNewLabel = new Label(grpFacadeFile, SWT.NONE);
		lblNewLabel.setText("Maximum vector length :");

		spinnerMaxVector = new Spinner(grpFacadeFile, SWT.BORDER);
		spinnerMaxVector.setIncrement(100);
		spinnerMaxVector.setMaximum(1000);
		spinnerMaxVector.setDigits(2);
		spinnerMaxVector.setLayoutData(new GridData(SWT.FILL, SWT.CENTER,
				false, false, 1, 1));

		Label lblMinimumHeightmeters = new Label(grpFacadeFile, SWT.NONE);
		lblMinimumHeightmeters.setText("Minimum height (meters) :");

		spinnerMinHeight = new Spinner(grpFacadeFile, SWT.BORDER);
		spinnerMinHeight.setLayoutData(new GridData(SWT.FILL, SWT.CENTER,
				false, false, 1, 1));

		Label lblMaximumHeightmeters = new Label(grpFacadeFile, SWT.NONE);
		lblMaximumHeightmeters.setText("Maximum height (meters) : ");

		spinnerMaxHeight = new Spinner(grpFacadeFile, SWT.BORDER);
		spinnerMaxHeight.setLayoutData(new GridData(SWT.FILL, SWT.CENTER,
				false, false, 1, 1));

		spinnerMaxHeight.addModifyListener(new ModifyListener() {
			public void modifyText(ModifyEvent e) {
				if (spinnerMaxHeight.getSelection() > 0) {
					currentFacade.setMaxHeight(spinnerMaxHeight.getSelection());
				}

			}
		});

		spinnerMinHeight.addModifyListener(new ModifyListener() {
			public void modifyText(ModifyEvent e) {
				if (spinnerMinHeight.getSelection() > 0) {
					currentFacade.setMinHeight(spinnerMinHeight.getSelection());
				}

			}
		});
		spinnerMaxVector.addModifyListener(new ModifyListener() {
			public void modifyText(ModifyEvent e) {
				if (spinnerMaxVector.getSelection() > 0) {

					currentFacade.setMaxVectorLength((double) spinnerMaxVector
							.getSelection() / 100);
				}

			}
		});

		return container;
	}

	private void updateProperties() {
		grpFacadeFile.setVisible(true);
		if (currentFacade.getRoofColor() != null) {
			textRoofColor.setText(currentFacade.getRoofColor());
		} else {
			textRoofColor.setText("");
		}

		if (currentFacade.getWallColor() != null) {
			textWallColor.setText(currentFacade.getWallColor());
		} else {
			textWallColor.setText("");
		}

		btnResidential.setSelection(currentFacade.isResidential());
		btnCommercial.setSelection(currentFacade.isCommercial());
		btnIndustrial.setSelection(currentFacade.isIndustrial());
		btnSlopedRoof.setSelection(currentFacade.isSloped());
		btnSimplebuildingOnly
				.setSelection(currentFacade.isSimpleBuildingOnly());
		if (currentFacade.getMinVectorLength() > 0) {
			spinnerMinVector.setSelection((int) (currentFacade
					.getMinVectorLength() * 100));
		} else {
			spinnerMinVector.setSelection(0);
		}
		if (currentFacade.getMaxVectorLength() > 0) {
			spinnerMaxVector.setSelection((int) (currentFacade
					.getMaxVectorLength() * 100));
		} else {
			spinnerMaxVector.setSelection(0);
		}
		if (currentFacade.getMaxHeight() > 0) {
			spinnerMaxHeight.setSelection(currentFacade.getMaxHeight());
		} else {
			spinnerMaxHeight.setSelection(0);

		}
		if (currentFacade.getMinHeight() > 0) {
			spinnerMinHeight.setSelection(currentFacade.getMinHeight());
		} else {
			spinnerMinHeight.setSelection(0);
		}

	}

	/**
	 * Create contents of the button bar.
	 * 
	 * @param parent
	 */
	@Override
	protected void createButtonsForButtonBar(Composite parent) {
		Button button = createButton(parent, IDialogConstants.OK_ID,
				IDialogConstants.OK_LABEL, true);
		button.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseDown(MouseEvent e) {
				try {
					FacadeSetHelper.saveFacadeSet(facadeSet, facadeSetFolder);
				} catch (Osm2xpBusinessException e1) {
					Osm2xpLogger.error("Error saving facade set", e1);
				}
			}
		});

		createButton(parent, IDialogConstants.CANCEL_ID,
				IDialogConstants.CANCEL_LABEL, false);
	}

	/**
	 * Return the initial size of the dialog.
	 */
	@Override
	protected Point getInitialSize() {
		return new Point(733, 615);
	}

}
