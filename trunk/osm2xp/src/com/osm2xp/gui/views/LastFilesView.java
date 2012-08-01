package com.osm2xp.gui.views;

import java.io.File;

import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Table;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.ScrolledForm;
import org.eclipse.ui.part.ViewPart;
import org.eclipse.wb.swt.ResourceManager;

import com.osm2xp.gui.views.panels.generic.SceneryFilePanel;
import com.osm2xp.utils.helpers.GuiOptionsHelper;

/**
 * LastFilesView.
 * 
 * @author Benjamin Blanchet
 * 
 */
public class LastFilesView extends ViewPart {
	private static final FormToolkit formToolkit = new FormToolkit(
			Display.getDefault());
	private Table lastFilesTable;
	private static TableViewer lastFilesTableViewer;

	public LastFilesView() {
	}

	@Override
	public void createPartControl(final Composite parent) {

		ScrolledForm scrldfrmLastOsmFiles = formToolkit
				.createScrolledForm(parent);
		scrldfrmLastOsmFiles.setImage(ResourceManager.getPluginImage(
				"com.osm2xp", "images/toolbarsIcons/lastFiles_32.png"));

		formToolkit.paintBordersFor(scrldfrmLastOsmFiles);
		scrldfrmLastOsmFiles.setText("Last osm files");
		scrldfrmLastOsmFiles.getBody()
				.setLayout(new FillLayout(SWT.HORIZONTAL));

		lastFilesTableViewer = new TableViewer(scrldfrmLastOsmFiles.getBody(),
				SWT.BORDER | SWT.FULL_SELECTION);
		lastFilesTable = lastFilesTableViewer.getTable();
		lastFilesTable.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseDoubleClick(MouseEvent e) {
				if (!lastFilesTableViewer.getSelection().isEmpty()) {
					IStructuredSelection selection = (IStructuredSelection) lastFilesTableViewer
							.getSelection();
					GuiOptionsHelper.getOptions().setCurrentFilePath(
							(String) selection.getFirstElement());
					SceneryFilePanel.refreshCurrentFilePath();
					if (((String) selection.getFirstElement()).toUpperCase()
							.contains(".SHP")) {
						GuiOptionsHelper.askShapeFileNature(parent.getShell());
					}
				}
			}
		});
		formToolkit.paintBordersFor(lastFilesTable);
		if (GuiOptionsHelper.getOptions().getLastFiles() != null) {
			lastFilesTableViewer.setContentProvider(new ArrayContentProvider());
			lastFilesTableViewer.setLabelProvider(new LabelProvider() {
				public Image getImage(Object element) {
					if (element.toString().toUpperCase().indexOf(".PBF") != -1) {
						return ResourceManager.getPluginImage("com.osm2xp",
								"images/toolbarsIcons/file_16.png");
					} else {
						return ResourceManager.getPluginImage("com.osm2xp",
								"images/toolbarsIcons/fileBlank_16.png");
					}

				}

				public String getText(Object element) {
					File osmFile = new File(element.toString());
					if (osmFile.exists()) {
						return osmFile.getName() + "  -  (" + osmFile.getPath()
								+ ")";
					} else
						return null;

				}
			});
			lastFilesTableViewer.setInput(GuiOptionsHelper.getOptions()
					.getLastFiles());

		}
	}

	public static void refreshList() {
		lastFilesTableViewer.refresh();
	}

	@Override
	public void setFocus() {
		// TODO Auto-model.options method stub

	}
}
