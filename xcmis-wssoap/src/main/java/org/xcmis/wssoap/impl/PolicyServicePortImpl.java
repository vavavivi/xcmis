/**
 * Copyright (C) 2010 eXo Platform SAS.
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 */

package org.xcmis.wssoap.impl;

import org.xcmis.core.CmisObjectType;
import org.xcmis.messaging.CmisExtensionType;
import org.xcmis.soap.CmisException;
import org.xcmis.soap.PolicyServicePort;
import org.xcmis.spi.CmisRegistry;
import org.xcmis.spi.Connection;
import org.xcmis.spi.utils.Logger;

/**
 * @author <a href="mailto:max.shaposhnik@exoplatform.com">Max Shaposhnik</a>
 * @version $Id: PolicyServicePortImpl.java 2 2010-02-04 17:21:49Z andrew00x $
 */
@javax.jws.WebService(//name = "PolicyServicePort",
serviceName = "PolicyService", //
portName = "PolicyServicePort", //
targetNamespace = "http://docs.oasis-open.org/ns/cmis/ws/200908/", //
wsdlLocation = "/wsdl/CMISWS-Service.wsdl" //,
//   endpointInterface = "org.xcmis.soap.PolicyServicePort"
)
public class PolicyServicePortImpl implements PolicyServicePort
{

   /** Logger. */
   private static final Logger LOG = Logger.getLogger(PolicyServicePortImpl.class);

   /**
    * Constructs instance of <code>PolicyServicePortImpl</code> .
    *
    */
   public PolicyServicePortImpl()
   {
   }

   /**
    * {@inheritDoc}
    */
   public CmisExtensionType applyPolicy(java.lang.String repositoryId, java.lang.String policyId,
      java.lang.String objectId, CmisExtensionType extension) throws CmisException
   {
      if (LOG.isDebugEnabled())
      {
         LOG.debug("Executing operation applyPolicy");
      }
      Connection conn = null;
      try
      {
         conn = CmisRegistry.getInstance().getConnection(repositoryId);

         conn.applyPolicy(policyId, objectId);
      }
      catch (Exception e)
      {
         LOG.error("Apply policy error: " + e.getMessage(), e);
         throw ExceptionFactory.generateException(e);
      }
      finally
      {
         if (conn != null)
         {
            conn.close();
         }
      }
      return new CmisExtensionType();
   }

   /**
    * {@inheritDoc}
    */
   public java.util.List<CmisObjectType> getAppliedPolicies(java.lang.String repositoryId, java.lang.String objectId,
      java.lang.String propertyFilter, CmisExtensionType extension) throws CmisException
   {
      if (LOG.isDebugEnabled())
      {
         LOG.debug("Executing operation getAppliedPolicies");
      }
      Connection conn = null;
      try
      {
         conn = CmisRegistry.getInstance().getConnection(repositoryId);

         return TypeConverter.getCmisObjectTypeList(conn.getAppliedPolicies(objectId, false, propertyFilter));
      }
      catch (Exception e)
      {
         LOG.error("Get applied policies error: " + e.getMessage(), e);
         throw ExceptionFactory.generateException(e);
      }
      finally
      {
         if (conn != null)
         {
            conn.close();
         }
      }
   }

   /**
    * {@inheritDoc}
    */
   public CmisExtensionType removePolicy(java.lang.String repositoryId, java.lang.String policyId,
      java.lang.String objectId, CmisExtensionType extension) throws CmisException
   {
      if (LOG.isDebugEnabled())
      {
         LOG.debug("Executing operation removePolicy");
      }
      Connection conn = null;
      try
      {
         conn = CmisRegistry.getInstance().getConnection(repositoryId);

         conn.removePolicy(policyId, objectId);
      }
      catch (Exception e)
      {
         LOG.error("Remove applied policy error: " + e.getMessage(), e);
         throw ExceptionFactory.generateException(e);
      }
      finally
      {
         if (conn != null)
         {
            conn.close();
         }
      }
      return new CmisExtensionType();
   }
}
