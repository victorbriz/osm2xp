package com.osm2xp.model.xplane;

import math.geom2d.Point2D;
import math.geom2d.polygon.LinearRing2D;

import com.osm2xp.exceptions.Osm2xpBusinessException;
import com.osm2xp.model.options.XplaneObjectTagRule;
import com.osm2xp.model.osm.OsmPolygon;
import com.osm2xp.utils.GeomUtils;
import com.osm2xp.utils.MiscUtils;

/**
 * XplaneDsfObject.
 * 
 * @author Benjamin Blanchet
 * 
 */
public class XplaneDsfObject {

	private Integer dsfIndex;
	private OsmPolygon osmPolygon;
	private XplaneObjectTagRule rule;
	private Integer angle;

	public Integer getAngle() {
		return angle;
	}

	public void setAngle(Integer angle) {
		this.angle = angle;
	}

	public XplaneDsfObject() {

	}

	public XplaneDsfObject(OsmPolygon poly, XplaneObjectTagRule rule) {
		this.osmPolygon = poly;
		this.rule = rule;
	}

	/**
	 * @param polygon
	 * @return
	 */
	private XplaneObjGeoRef getOriginAndAngle() {
		XplaneObjGeoRef result = null;

		LinearRing2D polygon = this.osmPolygon.getPolygon();

		if (polygon.getVertices().size() == 5) {

			for (int i = 0; i < polygon.getVertices().size() - 2; i++) {
				Point2D ptX = polygon.getVertex(i);
				Point2D ptOrigin = polygon.getVertex(i + 1);
				Point2D ptY = polygon.getVertex(i + 2);
				Point2D ptLast;

				if (i == polygon.getVertices().size() - 3) {
					ptLast = polygon.getVertex(0);
				} else {
					ptLast = polygon.getVertex(i + 3);
				}

				double segmentX = GeomUtils.latLongDistance(ptX.x, ptX.y,
						ptOrigin.x, ptOrigin.y);
				double segmentY = GeomUtils.latLongDistance(ptOrigin.x,
						ptOrigin.y, ptY.x, ptY.y);
				Boolean dimensionsCheck = false;
				// check if the rule x/y segments "fits" the current osm
				// polygon
				dimensionsCheck = ((segmentX > rule.getxVectorLength()) && (segmentX
						- rule.getxVectorLength() < 1))
						&& ((segmentY > rule.getyVectorLength()) && (segmentY
								- rule.getyVectorLength() < 1));

				// if that's the case, compute the rotation point (origin)
				// of
				// the object
				if (dimensionsCheck) {
					result = new XplaneObjGeoRef();
					result.origin = GeomUtils.getRotationPoint(ptOrigin, ptX,
							ptY, ptLast, rule.getRotationPointX(),
							rule.getRotationPointY());

					// compute angle
					// GeodeticCalculator gc = new GeodeticCalculator();
					// gc.setStartingGeographicPoint(ptOrigin);
					// gc.setDestinationGeographicPoint(ptX);
					//
					// Float test = (float) Math.toDegrees(Math.atan2(ptX.x
					// - ptOrigin.x, ptX.y - ptOrigin.y));
					Double anglePoly = Math.toDegrees(Math.atan2(ptX.x
							- ptOrigin.x, ptX.y - ptOrigin.y));
					if (anglePoly > 0) {
						anglePoly = 360 - anglePoly;
					} else {
						anglePoly = Math.abs(anglePoly);
					}

					result.angle = anglePoly.intValue();
				}

				if (result != null)
					break;
			}

		}

		return result;
	}

	private XplaneObjGeoRef computeObjGeoRef() {
		XplaneObjGeoRef result = new XplaneObjGeoRef();
		if (osmPolygon != null && osmPolygon.getPolygon() != null) {
			// if polygon is under 5 points, use first point
			if (osmPolygon.getPolygon().getVertices().size() < 5) {
				result = computeBasicOriginAndAngle();
			}
			// if more complex polygon
			else {
				// if simple rectangle and if rule is made for simple polygon
				if (osmPolygon.getPolygon().getVertices().size() == 5
						&& rule.isSimplePolygonOnly()) {
					result = getOriginAndAngle();
				}
				// if complex polygon
				else {
					result = computeComplexPolygonOriginAndAngle();
				}

			}
		}
		return result;
	}

	private XplaneObjGeoRef computeComplexPolygonOriginAndAngle() {
		XplaneObjGeoRef result = new XplaneObjGeoRef();
		result.origin = GeomUtils.getPolygonCenter(osmPolygon.getPolygon());
		if (rule.isRandomAngle()) {
			result.angle = MiscUtils.getRandomSize(0, 360);
		} else {
			result.angle = rule.getAngle();
		}
		return result;
	}

	private XplaneObjGeoRef computeBasicOriginAndAngle() {
		XplaneObjGeoRef result = new XplaneObjGeoRef();
		result.origin = osmPolygon.getPolygon().getFirstPoint();
		if (rule.isRandomAngle()) {
			result.angle = MiscUtils.getRandomSize(0, 360);
		} else {
			result.angle = rule.getAngle();
		}
		return result;
	}

	public String asObjDsfText() throws Osm2xpBusinessException {

		StringBuffer sb = new StringBuffer();
		// compute the center of the object.

		XplaneObjGeoRef obj = computeObjGeoRef();
		if (obj == null || obj.angle == null | obj.origin == null) {
			throw new Osm2xpBusinessException("Error computing 3D object");
		}
		sb.append("OBJECT " + this.dsfIndex + " " + obj.origin.y + " "
				+ obj.origin.x + " " + obj.angle);
		sb.append(System.getProperty("line.separator"));
		return sb.toString();
	}

	public Integer getDsfIndex() {
		return dsfIndex;
	}

	public void setDsfIndex(Integer dsfIndex) {
		this.dsfIndex = dsfIndex;
	}

	public OsmPolygon getOsmPolygon() {
		return osmPolygon;
	}

	public void setPolygon(OsmPolygon polygon) {
		this.osmPolygon = polygon;
	}

	public XplaneObjectTagRule getRule() {
		return rule;
	}

	public void setRule(XplaneObjectTagRule rule) {
		this.rule = rule;
	}

	/**
	 * inner class for angle and origin storage.
	 * 
	 * @author Benjamin Blanchet
	 * 
	 */
	private class XplaneObjGeoRef {
		protected Point2D origin;
		protected Integer angle;

	}
}
