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
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for criteriaType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="criteriaType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="parameter" type="{}parameterType" maxOccurs="unbounded" minOccurs="0"/>
 *       &lt;/sequence>
 *       &lt;attribute name="noofrows" type="{http://www.w3.org/2001/XMLSchema}byte" />
 *       &lt;attribute name="noofcolumns" type="{http://www.w3.org/2001/XMLSchema}byte" />
 *       &lt;attribute name="columnwidth" type="{http://www.w3.org/2001/XMLSchema}string" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "criteriaType", propOrder = {
    "parameter"
})
public class Criteria {

    


	protected List<Parameter> parameter;
    @XmlAttribute(name = "noofrows")
    protected Byte noofrows;
    @XmlAttribute(name = "noofcolumns")
    protected Byte noofcolumns;
    @XmlAttribute(name = "columnwidth")
    protected String columnwidth;
    
    @XmlTransient
    protected Layout layout;

    /**
     * Gets the value of the parameter property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the parameter property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getParameter().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link Parameter }
     * 
     * 
     */
    
    /**
	 * @return the layout
	 */
	public Layout getLayout() {
		return layout;
	}


	/**
	 * @param layout the layout to set
	 */
	public void setLayout(Layout layout) {
		this.layout = layout;
	}


	/**
	 * @param parameter the parameter to set
	 */
	public void setParameter(List<Parameter> parameter) {
		this.parameter = parameter;
	}
    
    
    public List<Parameter> getParameter() {
        if (parameter == null) {
            parameter = new ArrayList<Parameter>();
        }
        return this.parameter;
    }

    /**
     * Gets the value of the noofrows property.
     * 
     * @return
     *     possible object is
     *     {@link Byte }
     *     
     */
    public Byte getNoofrows() {
        return noofrows;
    }

    /**
     * Sets the value of the noofrows property.
     * 
     * @param value
     *     allowed object is
     *     {@link Byte }
     *     
     */
    public void setNoofrows(Byte value) {
        this.noofrows = value;
    }

    /**
     * Gets the value of the noofcolumns property.
     * 
     * @return
     *     possible object is
     *     {@link Byte }
     *     
     */
    public Byte getNoofcolumns() {
        return noofcolumns;
    }

    /**
     * Sets the value of the noofcolumns property.
     * 
     * @param value
     *     allowed object is
     *     {@link Byte }
     *     
     */
    public void setNoofcolumns(Byte value) {
        this.noofcolumns = value;
    }

    /**
     * Gets the value of the columnwidth property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String[] getColumnwidth() {
        return columnwidth.toString().split(",");
    }

    /**
     * Sets the value of the columnwidth property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setColumnwidth(String value) {
        this.columnwidth = value;
    }
    
    /* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "Criteria [parameter=" + parameter + ", noofrows=" + noofrows + ", noofcolumns=" + noofcolumns
				+ ", columnwidth=" + columnwidth + ", layout=" + layout + "]";
	}

}
