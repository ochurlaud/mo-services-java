/* ----------------------------------------------------------------------------
 * Copyright (C) 2013      European Space Agency
 *                         European Space Operations Centre
 *                         Darmstadt
 *                         Germany
 * ----------------------------------------------------------------------------
 * System                : CCSDS MO MAL Java Implementation
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
package esa.mo.mal.impl.broker;

import esa.mo.mal.impl.util.MALCloseable;
import java.lang.reflect.InvocationTargetException;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.ccsds.moims.mo.mal.MALException;
import org.ccsds.moims.mo.mal.broker.MALBroker;
import org.ccsds.moims.mo.mal.broker.MALBrokerBinding;
import org.ccsds.moims.mo.mal.broker.MALBrokerHandler;

/**
 * Implementation of the MALBroker interface.
 */
public class MALBrokerImpl implements MALBroker, MALCloseable {

    /**
     * Logger
     */
    public static final java.util.logging.Logger LOGGER = Logger.getLogger("org.ccsds.moims.mo.mal.impl.broker");
    private final MALBrokerHandlerImpl handler;
    private final List<MALBrokerBindingImpl> bindings = new LinkedList<>();

    MALBrokerImpl() throws MALException {
        this.handler = createBrokerHandler();
    }

    MALBrokerImpl(MALBrokerHandlerImpl handler) throws MALException {
        this.handler = handler;
    }

    /**
     * Returns the broker handler for this broker.
     *
     * @return the handler.
     */
    public MALBrokerHandler getHandler() {
        return handler;
    }

    @Override
    public MALBrokerBinding[] getBindings() {
        return bindings.toArray(new MALBrokerBinding[bindings.size()]);
    }

    /**
     * Adds a binding implementation to this broker.
     *
     * @param binding The new binding.
     */
    protected void addBinding(MALBrokerBindingImpl binding) {
        bindings.add(binding);
        handler.malInitialize(binding);
    }

    private MALBrokerHandlerImpl createBrokerHandler() {
        final String clsName = System.getProperty("org.ccsds.moims.mo.mal.broker.class",
                MALBrokerHandlerImpl.class.getName());

        MALBrokerHandlerImpl broker = null;
        try {
            final Class cls = Thread.currentThread().getContextClassLoader().loadClass(clsName);

            broker = (MALBrokerHandlerImpl) cls.getConstructor(MALCloseable.class).newInstance(this);
            MALBrokerImpl.LOGGER.log(Level.FINE,
                    "Creating internal MAL Broker handler: {0}", cls.getSimpleName());
        } catch (ClassNotFoundException ex) {
            MALBrokerImpl.LOGGER.log(Level.WARNING,
                    "Unable to find MAL Broker handler class: {0}", clsName);
        } catch (InstantiationException ex) {
            MALBrokerImpl.LOGGER.log(Level.WARNING,
                    "Unable to instantiate MAL Broker handler: {0}", clsName);
        } catch (NoSuchMethodException ex) {
            MALBrokerImpl.LOGGER.log(Level.WARNING,
                    "Unable to instantiate MAL Broker handler: {0}", clsName);
        } catch (InvocationTargetException ex) {
            MALBrokerImpl.LOGGER.log(Level.WARNING,
                    "InvocationTargetExceptionUnable when instantiating MAL Broker handler class: {0}", clsName);
        } catch (IllegalAccessException ex) {
            MALBrokerImpl.LOGGER.log(Level.WARNING,
                    "IllegalAccessException when instantiating MAL Broker handler class: {0}", clsName);
        }

        if (broker == null) {
            broker = new MALBrokerHandlerImpl();
            MALBrokerImpl.LOGGER.fine("Creating internal MAL Broker handler: MALBrokerHandlerImpl");
        }

        return broker;
    }

    @Override
    public void close() throws MALException {
        handler.close();
    }
}
