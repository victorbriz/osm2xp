package com.osm2xp.exceptions;

/**
 * Data sink Exception.
 * 
 * @author Benjamin Blanchet
 * 
 */
@SuppressWarnings("serial")
public class DataSinkException extends Exception {

	public DataSinkException(String cause, Exception e) {
		super(cause, e);

	}
}
