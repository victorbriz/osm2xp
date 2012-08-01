package com.osm2xp.model.osm;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

/**
 * Relation.
 * 
 * @author Benjamin Blanchet
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = { "tag", "member" })
@XmlRootElement(name = "relation")
public class Relation {

	protected Tag tag;
	protected List<Member> member;
	@XmlAttribute(name = "id", required = true)
	protected long id;

	/**
	 * Default no-arg constructor
	 * 
	 */
	public Relation() {
		super();
	}

	/**
	 * Fully-initialising value constructor
	 * 
	 */
	public Relation(final Tag tag, final List<Member> member, final long id) {
		this.tag = tag;
		this.member = member;
		this.id = id;
	}

	/**
	 * Gets the value of the tag property.
	 * 
	 * @return possible object is {@link Tag }
	 * 
	 */
	public Tag getTag() {
		return tag;
	}

	/**
	 * Sets the value of the tag property.
	 * 
	 * @param value
	 *            allowed object is {@link Tag }
	 * 
	 */
	public void setTag(Tag value) {
		this.tag = value;
	}

	/**
	 * Gets the value of the member property.
	 * 
	 * <p>
	 * This accessor method returns a reference to the live list, not a
	 * snapshot. Therefore any modification you make to the returned list will
	 * be present inside the JAXB object. This is why there is not a
	 * <CODE>set</CODE> method for the member property.
	 * 
	 * <p>
	 * For example, to add a new item, do as follows:
	 * 
	 * <pre>
	 * getMember().add(newItem);
	 * </pre>
	 * 
	 * 
	 * <p>
	 * Objects of the following type(s) are allowed in the list {@link Member }
	 * 
	 * 
	 */
	public List<Member> getMember() {
		if (member == null) {
			member = new ArrayList<Member>();
		}
		return this.member;
	}

	/**
	 * Gets the value of the id property.
	 * 
	 */
	public long getId() {
		return id;
	}

	/**
	 * Sets the value of the id property.
	 * 
	 */
	public void setId(long value) {
		this.id = value;
	}

}
