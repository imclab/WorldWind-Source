package gov.nasa.worldwind.formats.nitfs;
/*
Copyright (C) 2001, 2007 United States Government
as represented by the Administrator of the
National Aeronautics and Space Administration.
All Rights Reserved.
*/

/**
 * @author Lado Garakanidze
 * @version $Id: NitfsExtendedHeaderSegment Mar 31, 2007 1:06:23 AM
 */
class NITFSExtendedHeaderSegment extends NITFSSegment {
    public NITFSExtendedHeaderSegment(java.nio.ByteBuffer buffer, int headerStartOffset, int headerLength, int dataStartOffset, int dataLength)
    {
        super(NITFSSegmentType.EXTENDED_HEADER_SEGMENT, buffer, headerStartOffset, headerLength, dataStartOffset, dataLength);

        this.restoreBufferPosition();
    }
}
