/*
Copyright (C) 2001, 2011 United States Government
as represented by the Administrator of the
National Aeronautics and Space Administration.
All Rights Reserved.
*/
package gov.nasa.worldwind.examples.dataimport;

import gov.nasa.worldwind.*;
import gov.nasa.worldwind.avlist.*;
import gov.nasa.worldwind.cache.FileStore;
import gov.nasa.worldwind.data.TiledImageProducer;
import gov.nasa.worldwind.examples.ApplicationTemplate;
import gov.nasa.worldwind.examples.util.ExampleUtil;
import gov.nasa.worldwind.geom.*;
import gov.nasa.worldwind.layers.Layer;
import gov.nasa.worldwind.util.WWIO;
import org.w3c.dom.Document;

import javax.swing.*;
import java.awt.*;
import java.io.File;

/**
 * Illustrates how to install imagery into a World Wind <code>{@link FileStore}</code>.
 * <p/>
 * Image data is installed into a FileStore by executing the following steps: <ol> <li>Choose the FileStore location to
 * place the installed imagery. This example uses the default install location.</li> <li>Compute a unique cache name for
 * the imagery. In this example the cache name is "Examples/ImageName", where "ImageName" is the image's display name,
 * stripped of any illegal filename characters.</li> <li>Install the imagery by constructing, configuring and running a
 * {@link gov.nasa.worldwind.data.TiledImageProducer}.</li> <li>The installed imagery is subsequently described by a
 * configuration {@link org.w3c.dom.Document}, which we use to construct a Layer via the {@link Factory} method {@link
 * gov.nasa.worldwind.Factory#createFromConfigSource(Object, gov.nasa.worldwind.avlist.AVList)}.</li> </ol>
 *
 * @author tag
 * @version $Id: InstallImagery.java 15674 2011-06-20 20:02:32Z dcollins $
 */
public class InstallImagery extends ApplicationTemplate
{
    protected static final String BASE_CACHE_PATH = "Examples/"; // Define a subdirectory in the installed-data area

    // This example's imagery is downloaded from the following location.
    protected static final String IMAGE_URL =
        "http://worldwind.arc.nasa.gov/java/demos/data/wa-precip-24hmam-rgb.tif.zip";

    // Override ApplicationTemplate.AppFrame's constructor to install an elevation dataset.
    public static class AppFrame extends ApplicationTemplate.AppFrame
    {
        public AppFrame()
        {
            // Show the WAIT cursor because the installation may take a while.
            this.setCursor(new Cursor(Cursor.WAIT_CURSOR));

            // Install the imagery on a thread other than the event-dispatch thread to avoid freezing the UI.
            Thread t = new Thread(new Runnable()
            {
                public void run()
                {
                    installImagery();

                    // Restore the cursor.
                    setCursor(Cursor.getDefaultCursor());
                }
            });

            t.start();
        }

        protected void installImagery()
        {
            // Download the source file.
            File sourceFile = ExampleUtil.downloadAndUnzipToTempFile(WWIO.makeURL(IMAGE_URL), ".tif");

            // Get a reference to the FileStore into which we'll install the imagery.
            FileStore fileStore = WorldWind.getDataFileStore();

            // Install the imagery into the FileStore.
            final Layer layer = installSurfaceImage("WA Annual Precipitation", sourceFile, fileStore);
            if (layer == null)
                return;

            // Display a layer with the new imagery. Must do it on the event dispatch thread.
            SwingUtilities.invokeLater(new Runnable()
            {
                public void run()
                {
                    // Add the layer created by the install method to the layer list.
                    insertBeforePlacenames(AppFrame.this.getWwd(), layer);

                    // Update the layer panel to display the new layer for the installed imagery.
                    AppFrame.this.getLayerPanel().update(AppFrame.this.getWwd());

                    // Set the view to look at the installed image. Get the location from the layer's construction
                    // parameters.
                    AVList params = (AVList) layer.getValue(AVKey.CONSTRUCTION_PARAMETERS);
                    Sector sector = (Sector) params.getValue(AVKey.SECTOR);
                    AppFrame.this.getWwd().getView().goTo(new Position(sector.getCentroid(), 0), 2e6);
                }
            });
        }

        protected Layer installSurfaceImage(String displayName, Object imageSource, FileStore fileStore)
        {
            // Use the FileStore's install location as the destination for the installed imagery. The default install
            // location is the FileStore's area for permanent storage.
            File fileStoreLocation = DataInstallUtil.getDefaultInstallLocation(fileStore);

            // Create a unique cache name that specifies the installed data's location within the FileStore.
            String cacheName = BASE_CACHE_PATH + WWIO.replaceIllegalFileNameCharacters(displayName);

            // Create a parameter list specifying the install location information.
            AVList params = new AVListImpl();
            params.setValue(AVKey.FILE_STORE_LOCATION, fileStoreLocation.getAbsolutePath());
            params.setValue(AVKey.DATA_CACHE_NAME, cacheName);
            params.setValue(AVKey.DATASET_NAME, displayName);

            // Create a TiledImageProducer to install the imagery.
            TiledImageProducer producer = new TiledImageProducer();
            try
            {
                // Configure the TiledImageProducer with the parameter list and the image source.
                producer.setStoreParameters(params);
                producer.offerDataSource(imageSource, null);

                // Install the imagery.
                producer.startProduction();
            }
            catch (Exception e)
            {
                producer.removeProductionState(); // Clean up on failure.
                e.printStackTrace();
                return null;
            }

            // Extract the data configuration document from the installed results. If the installation successfully
            // completed, the TiledImageProducer should always contain a document in the production results, but test
            // the results anyway.
            Iterable<?> results = producer.getProductionResults();
            if (results == null || results.iterator() == null || !results.iterator().hasNext())
                return null;

            Object o = results.iterator().next();
            if (o == null || !(o instanceof Document))
                return null;

            // Construct a Layer by passing the data configuration document to a LayerFactory.
            Layer layer = (Layer) BasicFactory.create(AVKey.LAYER_FACTORY, ((Document) o).getDocumentElement());

            // The layer factory creates layers that are initially disabled, so enable the layer.
            layer.setEnabled(true);

            return layer;
        }
    }

    public static void main(String[] args)
    {
        ApplicationTemplate.start("World Wind Imagery Installation", InstallImagery.AppFrame.class);
    }
}
