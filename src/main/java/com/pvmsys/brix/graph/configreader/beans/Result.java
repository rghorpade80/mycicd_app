//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, vhudson-jaxb-ri-2.2-147 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2018.12.04 at 09:45:02 AM IST 
//

package com.pvmsys.brix.graph.configreader.beans;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;

/**
 * <p>
 * Java class for resultType complex type.
 * 
 * <p>
 * The following schema fragment specifies the expected content contained within
 * this class.
 * 
 * <pre>
 * &lt;complexType name="resultType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="parameter" type="{}parameterType" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="node" type="{}nodeType" maxOccurs="unbounded" minOccurs="0"/>
 *       &lt;/sequence>
 *       &lt;attribute name="type" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="checkboxrequired" type="{http://www.w3.org/2001/XMLSchema}byte" />
 *       &lt;attribute name="islink" type="{http://www.w3.org/2001/XMLSchema}byte" />
 *       &lt;attribute name="key" type="{http://www.w3.org/2001/XMLSchema}string" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "resultType", propOrder = { "parameter", "node" })
public class Result {
	protected List<Parameter> parameter;
	protected List<Node> node;
	@XmlAttribute(name = "type")
	protected String type;
	@XmlAttribute(name = "checkboxrequired")
	protected String checkboxrequired;
	@XmlAttribute(name = "islink")
	protected String islink;
	@XmlAttribute(name = "link")
	protected String link;
	@XmlAttribute(name = "key")
	protected String key;

	/**
	 * Gets the value of the parameter property.
	 * 
	 * <p>
	 * This accessor method returns a reference to the live list, not a
	 * snapshot. Therefore any modification you make to the returned list will
	 * be present inside the JAXB object. This is why there is not a
	 * <CODE>set</CODE> method for the parameter property.
	 * 
	 * <p>
	 * For example, to add a new item, do as follows:
	 * 
	 * <pre>
	 * getParameter().add(newItem);
	 * </pre>
	 * 
	 * 
	 * <p>
	 * Objects of the following type(s) are allowed in the list {@link Parameter
	 * }
	 * 
	 * 
	 */
	public List<Parameter> getParameter() {
		if (parameter == null) {
			parameter = new ArrayList<Parameter>();
		}
		return this.parameter;
	}

	/**
	 * Gets the value of the node property.
	 * 
	 * <p>
	 * This accessor method returns a reference to the live list, not a
	 * snapshot. Therefore any modification you make to the returned list will
	 * be present inside the JAXB object. This is why there is not a
	 * <CODE>set</CODE> method for the node property.
	 * 
	 * <p>
	 * For example, to add a new item, do as follows:
	 * 
	 * <pre>
	 * getNode().add(newItem);
	 * </pre>
	 * 
	 * 
	 * <p>
	 * Objects of the following type(s) are allowed in the list {@link Node }
	 * 
	 * 
	 */
	public List<Node> getNode() {
		if (node == null) {
			node = new ArrayList<Node>();
		}
		return this.node;
	}

	/**
	 * Gets the value of the type property.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getType() {
		return type;
	}

	/**
	 * Sets the value of the type property.
	 * 
	 * @param value
	 *            allowed object is {@link String }
	 * 
	 */
	public void setType(String value) {
		this.type = value;
	}

	/**
	 * Gets the value of the checkboxrequired property.
	 * 
	 * @return possible object is {@link Byte }
	 * 
	 */
	public String getCheckboxrequired() {
		return checkboxrequired;
	}

	/**
	 * Sets the value of the checkboxrequired property.
	 * 
	 * @param value
	 *            allowed object is {@link Byte }
	 * 
	 */
	public void setCheckboxrequired(String value) {
		this.checkboxrequired = value;
	}

	/**
	 * Gets the value of the islink property.
	 * 
	 * @return possible object is {@link Byte }
	 * 
	 */
	public String getIslink() {
		return islink;
	}

	/**
	 * Sets the value of the islink property.
	 * 
	 * @param value
	 *            allowed object is {@link Byte }
	 * 
	 */
	public void setIslink(String value) {
		this.islink = value;
	}

	/**
	 * Gets the value of the key property.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getKey() {
		return key;
	}

	/**
	 * Sets the value of the key property.
	 * 
	 * @param value
	 *            allowed object is {@link String }
	 * 
	 */
	public void setKey(String value) {
		this.key = value;
	}

	/**
	 * @param parameter
	 *            the parameter to set
	 */
	public void setParameter(List<Parameter> parameter) {
		this.parameter = parameter;
	}

	/**
	 * @param node
	 *            the node to set
	 */
	public void setNode(List<Node> node) {
		this.node = node;
	}

	public String getLink() {
		return link;
	}

	public void setLink(String link) {
		this.link = link;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "Result [parameter=" + parameter + ", node=" + node + ", type=" + type + ", checkboxrequired="
				+ checkboxrequired + ", islink=" + islink + ", key=" + key + "]";
	}

}
