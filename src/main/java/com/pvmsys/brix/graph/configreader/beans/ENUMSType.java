//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, vhudson-jaxb-ri-2.2-147 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2018.12.04 at 09:45:02 AM IST 
//


package com.pvmsys.brix.graph.configreader.beans;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for ENUMSType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="ENUMSType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="displaytype" type="{}displaytypeType"/>
 *         &lt;element name="searchtype" type="{}searchtypeType"/>
 *         &lt;element name="datasource" type="{}datasourceType"/>
 *         &lt;element name="resulttype" type="{}resulttypeType"/>
 *         &lt;element name="filtertype" type="{}filtertypeType"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ENUMSType", propOrder = {
    "displaytype",
    "searchtype",
    "datasource",
    "resulttype",
    "filtertype"
})
public class ENUMSType {

    @XmlElement(required = true)
    protected DisplayType displaytype;
    @XmlElement(required = true)
    protected SearchType searchtype;
    @XmlElement(required = true)
    protected Datasource datasource;
    @XmlElement(required = true)
    protected ResultType resulttype;
    @XmlElement(required = true)
    protected FilterType filtertype;

    /**
     * Gets the value of the displaytype property.
     * 
     * @return
     *     possible object is
     *     {@link DisplayType }
     *     
     */
    public DisplayType getDisplaytype() {
        return displaytype;
    }

    /**
     * Sets the value of the displaytype property.
     * 
     * @param value
     *     allowed object is
     *     {@link DisplayType }
     *     
     */
    public void setDisplaytype(DisplayType value) {
        this.displaytype = value;
    }

    /**
     * Gets the value of the searchtype property.
     * 
     * @return
     *     possible object is
     *     {@link SearchType }
     *     
     */
    public SearchType getSearchtype() {
        return searchtype;
    }

    /**
     * Sets the value of the searchtype property.
     * 
     * @param value
     *     allowed object is
     *     {@link SearchType }
     *     
     */
    public void setSearchtype(SearchType value) {
        this.searchtype = value;
    }

    /**
     * Gets the value of the datasource property.
     * 
     * @return
     *     possible object is
     *     {@link Datasource }
     *     
     */
    public Datasource getDatasource() {
        return datasource;
    }

    /**
     * Sets the value of the datasource property.
     * 
     * @param value
     *     allowed object is
     *     {@link Datasource }
     *     
     */
    public void setDatasource(Datasource value) {
        this.datasource = value;
    }

    /**
     * Gets the value of the resulttype property.
     * 
     * @return
     *     possible object is
     *     {@link ResultType }
     *     
     */
    public ResultType getResulttype() {
        return resulttype;
    }

    /**
     * Sets the value of the resulttype property.
     * 
     * @param value
     *     allowed object is
     *     {@link ResultType }
     *     
     */
    public void setResulttype(ResultType value) {
        this.resulttype = value;
    }

    /**
     * Gets the value of the filtertype property.
     * 
     * @return
     *     possible object is
     *     {@link FilterType }
     *     
     */
    public FilterType getFiltertype() {
        return filtertype;
    }

    /**
     * Sets the value of the filtertype property.
     * 
     * @param value
     *     allowed object is
     *     {@link FilterType }
     *     
     */
    public void setFiltertype(FilterType value) {
        this.filtertype = value;
    }

}
