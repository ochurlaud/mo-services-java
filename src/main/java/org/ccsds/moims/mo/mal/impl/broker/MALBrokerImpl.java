/* ----------------------------------------------------------------------------
 * (C) 2010      European Space Agency
 *               European Space Operations Centre
 *               Darmstadt Germany
 * ----------------------------------------------------------------------------
 * System       : CCSDS MO MAL Implementation
 * Author       : Sam Cooper
 *
 * ----------------------------------------------------------------------------
 */
package org.ccsds.moims.mo.mal.impl.broker;

import java.lang.reflect.InvocationTargetException;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.ccsds.moims.mo.mal.MALException;
import org.ccsds.moims.mo.mal.broker.MALBroker;
import org.ccsds.moims.mo.mal.broker.MALBrokerBinding;
import org.ccsds.moims.mo.mal.broker.MALBrokerHandler;
import org.ccsds.moims.mo.mal.impl.broker.simple.SimpleBrokerHandler;
import org.ccsds.moims.mo.mal.impl.util.MALClose;
import org.ccsds.moims.mo.mal.structures.QoSLevel;
import org.ccsds.moims.mo.mal.transport.*;

/**
 * Implementation of the MALBroker interface.
 */
public class MALBrokerImpl extends MALClose implements MALBroker
{
  /**
   * Logger
   */
  public static final java.util.logging.Logger LOGGER = Logger.getLogger("org.ccsds.moims.mo.mal.impl.broker");
  private final MALBrokerHandler handler;
  private final boolean handlerIsLocalType;
  private final List<MALBrokerBindingImpl> bindings = new LinkedList<MALBrokerBindingImpl>();

  MALBrokerImpl(final MALClose parent) throws MALException
  {
    super(parent);
    this.handler = (MALBrokerHandlerImpl) addChild(createBrokerHandler());
    handlerIsLocalType = true;
  }

  MALBrokerImpl(final MALClose parent, MALBrokerHandler handler) throws MALException
  {
    super(parent);
    this.handler = handler;
    handlerIsLocalType = false;
  }

  /**
   * Returns the broker handler for this broker.
   *
   * @return the handler.
   */
  public MALBrokerHandler getHandler()
  {
    return handler;
  }

  @Override
  public MALBrokerBinding[] getBindings()
  {
    return bindings.toArray(new MALBrokerBinding[bindings.size()]);
  }

  /**
   * Returns the QoS used when contacting the provider.
   *
   * @param hdr The supplied header message.
   * @return The required QoS level.
   */
  public QoSLevel getProviderQoSLevel(final MALMessageHeader hdr)
  {
    if (handlerIsLocalType)
    {
      return ((MALBrokerHandlerImpl) handler).getProviderQoSLevel(hdr);
    }

    return QoSLevel.BESTEFFORT;
  }

  /**
   * Adds a binding implementation to this broker.
   *
   * @param binding The new binding.
   */
  protected void addBinding(MALBrokerBindingImpl binding)
  {
    bindings.add(binding);
    handler.malInitialize(binding);
  }

  private MALBrokerHandlerImpl createBrokerHandler()
  {
    final String clsName = System.getProperty("org.ccsds.moims.mo.mal.broker.class",
            SimpleBrokerHandler.class.getName());

    MALBrokerHandlerImpl broker = null;
    try
    {
      final Class cls = Thread.currentThread().getContextClassLoader().loadClass(clsName);

      broker = (MALBrokerHandlerImpl) cls.getConstructor(MALClose.class).newInstance(this);
      MALBrokerImpl.LOGGER.log(Level.INFO, "Creating internal MAL Broker handler: {0}", cls.getSimpleName());
    }
    catch (ClassNotFoundException ex)
    {
      MALBrokerImpl.LOGGER.log(Level.WARNING, "Unable to find MAL Broker handler class: {0}", clsName);
    }
    catch (InstantiationException ex)
    {
      MALBrokerImpl.LOGGER.log(Level.WARNING, "Unable to instantiate MAL Broker handler: {0}", clsName);
    }
    catch (NoSuchMethodException ex)
    {
      MALBrokerImpl.LOGGER.log(Level.WARNING, "Unable to instantiate MAL Broker handler: {0}", clsName);
    }
    catch (InvocationTargetException ex)
    {
      MALBrokerImpl.LOGGER.log(Level.WARNING, "InvocationTargetExceptionUnable when instantiating MAL Broker handler class: {0}", clsName);
    }
    catch (IllegalAccessException ex)
    {
      MALBrokerImpl.LOGGER.log(Level.WARNING, "IllegalAccessException when instantiating MAL Broker handler class: {0}", clsName);
    }

    if (null == broker)
    {
      broker = new SimpleBrokerHandler(this);
      MALBrokerImpl.LOGGER.info("Creating internal MAL Broker handler: SimpleBrokerHandler");
    }

    return broker;
  }
}
