/*
Copyright (C) 2001, 2011 United States Government
as represented by the Administrator of the
National Aeronautics and Space Administration.
All Rights Reserved.
*/
package gov.nasa.worldwind.examples;

/**
 * Illustrates how to configure and display World Wind <code>{@link gov.nasa.worldwind.render.SurfaceShape}s</code>.
 * Surface shapes are used to visualize flat standard shapes types that follow the terrain. This illustrates how to use
 * all 7 standard surface shape types:
 * <p/>
 * <ul> <li><code>{@link gov.nasa.worldwind.render.SurfacePolygon}</code></li> <li><code>{@link
 * gov.nasa.worldwind.render.SurfaceEllipse}</code></li> <li><code>{@link gov.nasa.worldwind.render.SurfaceCircle}</code></li>
 * <li><code>{@link gov.nasa.worldwind.render.SurfaceQuad}</code></li> <li><code>{@link
 * gov.nasa.worldwind.render.SurfaceSquare}</code></li> <li><code>{@link gov.nasa.worldwind.render.SurfaceSector}</code></li>
 * <li><code>{@link gov.nasa.worldwind.render.SurfacePolyline}</code></li> </ul>
 *
 * @author dcollins
 * @version $Id: SurfaceShapes.java 15694 2011-06-22 02:49:00Z dcollins $
 */
public class SurfaceShapes extends DraggingShapes
{
    public static void main(String[] args)
    {
        ApplicationTemplate.start("World Wind Surface Shapes", AppFrame.class);
    }
}
