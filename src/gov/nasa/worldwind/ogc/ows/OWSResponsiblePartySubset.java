/* Copyright (C) 2001, 2011 United States Government as represented by
the Administrator of the National Aeronautics and Space Administration.
All Rights Reserved.
*/
package gov.nasa.worldwind.ogc.ows;

import gov.nasa.worldwind.util.xml.AbstractXMLEventParser;

/**
 * Parses an OGC Web Service Common (OWS) ResponsiblePartySubsetType element and provides access to its contents. See
 * http://schemas.opengis.net/ows/2.0/ows19115subset.xsd.
 *
 * @author dcollins
 * @version $Id: OWSResponsiblePartySubset.java 14946 2011-03-11 23:25:18Z dcollins $
 */
public class OWSResponsiblePartySubset extends AbstractXMLEventParser
{
    public OWSResponsiblePartySubset(String namespaceURI)
    {
        super(namespaceURI);
    }

    public String getIndividualName()
    {
        return (String) this.getField("IndividualName");
    }

    public String getPositionName()
    {
        return (String) this.getField("PositionName");
    }

    public OWSContactInformation getContactInfo()
    {
        return (OWSContactInformation) this.getField("ContactInfo");
    }

    public OWSCodeType getRole()
    {
        return (OWSCodeType) this.getField("Role");
    }
}
