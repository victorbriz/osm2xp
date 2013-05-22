package com.osm2xp.model.options;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;

import com.osm2xp.model.osm.Tag;

/**
 * ObjectTagRule.
 * 
 * @author Benjamin Blanchet
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "LightTagRule", propOrder = { "height", "offset" })
public class LightTagRule extends TagsRules {

	protected int height;
	protected boolean offset;

	/**
	 * Default no-arg constructor
	 * 
	 */
	public LightTagRule() {
		super();
	}

	/**
	 * Fully-initialising value constructor
	 * 
	 */
	public LightTagRule(final Tag tag, final List<ObjectFile> objectsFiles,
			final int height, final boolean offset) {
		super(tag, objectsFiles);
		this.height = height;
		this.offset = offset;

	}

	public int getHeight() {
		return height;
	}

	public void setHeight(int height) {
		this.height = height;
	}

	public boolean isOffset() {
		return offset;
	}

	public void setOffset(boolean offset) {
		this.offset = offset;
	}

}
