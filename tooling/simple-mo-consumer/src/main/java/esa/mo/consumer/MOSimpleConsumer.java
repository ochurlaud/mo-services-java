/* ----------------------------------------------------------------------------
 * Copyright (C) 2024      European Space Agency
 *                         European Space Operations Centre
 *                         Darmstadt
 *                         Germany
 * ----------------------------------------------------------------------------
 * System                : ESA CCSDS MO Services
 * ----------------------------------------------------------------------------
 * Licensed under European Space Agency Public License (ESA-PL) Weak Copyleft – v2.4
 * You may not use this file except in compliance with the License.
 *
 * Except as expressly set forth in this License, the Software is provided to
 * You on an "as is" basis and without warranties of any kind, including without
 * limitation merchantability, fitness for a particular purpose, absence of
 * defects or errors, accuracy or non-infringement of intellectual property rights.
 * 
 * See the License for the specific language governing permissions and
 * limitations under the License. 
 * ----------------------------------------------------------------------------
 */
package esa.mo.consumer;

import esa.mo.common.impl.consumer.DirectoryConsumerServiceImpl;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.ccsds.moims.mo.common.directory.structures.ProviderSummaryList;
import org.ccsds.moims.mo.common.directory.structures.ServiceFilter;
import org.ccsds.moims.mo.common.structures.ServiceKey;
import org.ccsds.moims.mo.mal.MALException;
import org.ccsds.moims.mo.mal.MALInteractionException;
import org.ccsds.moims.mo.mal.helpertools.helpers.HelperMisc;
import org.ccsds.moims.mo.mal.structures.Identifier;
import org.ccsds.moims.mo.mal.structures.IdentifierList;
import org.ccsds.moims.mo.mal.structures.UOctet;
import org.ccsds.moims.mo.mal.structures.URI;
import org.ccsds.moims.mo.mal.structures.UShort;
import org.ccsds.moims.mo.mal.structures.UShortList;

/**
 * The MOSimpleConsumer class connects to a Directory service.
 */
public class MOSimpleConsumer {

    /**
     * Initializes the MO Simple Consumer.
     *
     * @param providerURI The URI of the provider to connect to.
     * @throws org.ccsds.moims.mo.mal.MALException if the service could not be started.
     * @throws java.net.MalformedURLException
     * @throws org.ccsds.moims.mo.mal.MALInteractionException
     */
    public void init(URI providerURI) throws MALException, MalformedURLException, MALInteractionException {
        try {
            HelperMisc.loadConsumerProperties();
        } catch (MalformedURLException ex) {
            Logger.getLogger(MOSimpleConsumer.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            // Ignore the exception if it does not exist - the file is becoming deprecated
            Logger.getLogger(MOSimpleConsumer.class.getName()).log(Level.FINE, null, ex);
        }

        DirectoryConsumerServiceImpl directoryService = new DirectoryConsumerServiceImpl(providerURI);

        IdentifierList wildcardList = new IdentifierList();
        wildcardList.add(new Identifier("*"));

        // Additional logic to save bandwidth in the Space2Ground link
        ServiceFilter filter = new ServiceFilter(new Identifier("*"),
                wildcardList, new Identifier("*"), null,
                new Identifier("*"),
                new ServiceKey(new UShort((short) 0), new UShort((short) 0), new UOctet((short) 0)),
                new UShortList()
        );

        // Do the lookup
        try {
            ProviderSummaryList summaryList = directoryService.getDirectoryStub().lookupProvider(filter);

            Logger.getLogger(MOSimpleConsumer.class.getName()).log(Level.INFO,
                    "The returned information from the Directory is: {0}", summaryList.toString());
        } catch (MALException | MALInteractionException e) {
            Logger.getLogger(MOSimpleConsumer.class.getName()).log(
                    Level.WARNING, "Could not connect to the Directory service!");
        } finally {
            directoryService.closeConnection();  // close the connection
        }
    }
}
