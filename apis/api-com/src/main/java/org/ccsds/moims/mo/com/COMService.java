/* ----------------------------------------------------------------------------
 * Copyright (C) 2015      European Space Agency
 *                         European Space Operations Centre
 *                         Darmstadt
 *                         Germany
 * ----------------------------------------------------------------------------
 * System                : CCSDS MO COM Java API
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
package org.ccsds.moims.mo.com;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import org.ccsds.moims.mo.mal.MALOperation;
import org.ccsds.moims.mo.mal.MALService;
import org.ccsds.moims.mo.mal.ServiceKey;
import org.ccsds.moims.mo.mal.structures.Identifier;
import org.ccsds.moims.mo.mal.structures.UShort;

/**
 * This class is deprecated. It is only here for backward compatibility with the
 * old MAL.
 */
@Deprecated
public class COMService extends MALService {

    private final Map<Integer, COMObject> objectsByNumber = new HashMap<>();

    public COMService(final ServiceKey serviceKey, final Identifier serviceName,
            final MALOperation[] operations) {
        this(serviceKey, serviceName, operations, new COMObject[0]);
    }

    public COMService(final ServiceKey serviceKey, final Identifier serviceName,
            final MALOperation[] operations, final COMObject[] comObjects) {
        super(serviceKey, serviceName, operations);

        for (COMObject comObject : comObjects) {
            objectsByNumber.put(comObject.getObjectType().getNumber().getValue(), comObject);
        }
    }

    /**
     * Return an object identified by its number.
     *
     * @param opNumber The number of the object.
     * @return The found operation or null.
     */
    public COMObject getObjectByNumber(final UShort opNumber) {
        return objectsByNumber.get(opNumber.getValue());
    }

    /**
     * Returns the set of objects.
     *
     * @return The set of objects or an empty array if none defined.
     */
    public COMObject[] getObjects() {
        return (COMObject[]) Arrays.asList(objectsByNumber.values()).toArray();
    }
}
