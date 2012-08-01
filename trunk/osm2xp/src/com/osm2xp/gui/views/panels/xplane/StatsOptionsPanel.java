package com.osm2xp.gui.views.panels.xplane;

import org.eclipse.core.databinding.DataBindingContext;
import org.eclipse.core.databinding.beans.PojoObservables;
import org.eclipse.jface.databinding.swt.SWTObservables;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;

import com.osm2xp.utils.helpers.XplaneOptionsHelper;

/**
 * StatsOptionsPanel.
 * 
 * @author Benjamin Blanchet
 * 
 */
public class StatsOptionsPanel extends Composite {

	public StatsOptionsPanel(final Composite parent, final int style) {
		super(parent, style);
		DataBindingContext bindingContext = new DataBindingContext();
		GridLayout gridLayout = new GridLayout(2, false);
		gridLayout.verticalSpacing = 15;
		gridLayout.horizontalSpacing = 15;
		gridLayout.marginHeight = 15;
		setLayout(gridLayout);
		Button btnGenerateXmlStats = new Button(this, SWT.CHECK);
		btnGenerateXmlStats.setLayoutData(new GridData(SWT.FILL, SWT.FILL,
				false, false, 1, 1));
		btnGenerateXmlStats.setText("Generate XML stats");
		bindingContext.bindValue(SWTObservables
				.observeSelection(btnGenerateXmlStats), PojoObservables
				.observeValue(XplaneOptionsHelper.getOptions(),
						"generateXmlStats"));

		Button btnGeneratePdfStats = new Button(this, SWT.CHECK);
		btnGeneratePdfStats.setLayoutData(new GridData(SWT.FILL, SWT.FILL,
				false, false, 1, 1));
		btnGeneratePdfStats.setText("Generate PDF stats");
		bindingContext.bindValue(SWTObservables
				.observeSelection(btnGeneratePdfStats), PojoObservables
				.observeValue(XplaneOptionsHelper.getOptions(),
						"generatePdfStats"));

	}

}
