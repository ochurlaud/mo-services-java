/* ----------------------------------------------------------------------------
 * Copyright (C) 2024      European Space Agency
 *                         European Space Operations Centre
 *                         Darmstadt
 *                         Germany
 * ----------------------------------------------------------------------------
 * System                : CCSDS MO Transport - HTTP
 * ----------------------------------------------------------------------------
 * Licensed under the European Space Agency Public License, Version 2.0
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
package esa.mo.mal.transport.http;

import java.util.Map;
import org.ccsds.moims.mo.mal.MALException;
import org.ccsds.moims.mo.mal.transport.MALTransport;
import org.ccsds.moims.mo.mal.transport.MALTransportFactory;

/**
 * Instance of the transport factory for a HTTP transport.
 */
public class HTTPTransportFactoryImpl extends MALTransportFactory {

    private HTTPTransport transport = null;

    /**
     * Constructor.
     *
     * @param protocol The protocol string.
     */
    public HTTPTransportFactoryImpl(final String protocol) {
        super(protocol);
    }

    @Override
    public synchronized MALTransport createTransport(final Map properties) throws MALException {
        if (transport == null) {
            transport = new HTTPTransport(getProtocol(), '/', false, this, properties);
            transport.init();
        }

        return transport;
    }
}
