package com.osm2xp.parsers.relationsLister;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import com.osm2xp.exceptions.Osm2xpBusinessException;
import com.osm2xp.model.osm.Relation;

import crosby.binary.BinaryParser;
import crosby.binary.Osmformat.DenseNodes;
import crosby.binary.Osmformat.HeaderBlock;
import crosby.binary.Osmformat.Node;
import crosby.binary.Osmformat.Way;
import crosby.binary.file.BlockInputStream;

/**
 * PbfRelationsLister.
 * 
 * @author Benjamin Blanchet
 * 
 */
public class PbfRelationsLister extends BinaryParser implements RelationsLister {

	private List<Relation> relationsList = new ArrayList<Relation>();
	private File file;

	public PbfRelationsLister(File file) {
		this.file = file;

	}

	/**
	 * 
	 */
	public void complete() {

	}

	@Override
	protected void parseRelations(List<crosby.binary.Osmformat.Relation> rels) {
	}

	@Override
	protected void parseDense(DenseNodes nodes) {

	}

	@Override
	protected void parseNodes(List<Node> nodes) {
	}

	@Override
	protected void parseWays(List<Way> ways) {

	}

	@Override
	protected void parse(HeaderBlock header) {
	}

	public void process() throws Osm2xpBusinessException {

		InputStream input = null;
		try {
			input = new FileInputStream(this.file);
		} catch (FileNotFoundException e) {
			throw new Osm2xpBusinessException(e.getMessage());
		}
		BlockInputStream bm = new BlockInputStream(input, this);
		try {
			bm.process();
		} catch (IOException e) {
			throw new Osm2xpBusinessException(e.getMessage());

		}
	}

	public List<Relation> getRelationsList() {
		return this.relationsList;
	}

}
