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

package org.xcmis.restatom.abdera;

import org.apache.abdera.factory.Factory;
import org.apache.abdera.model.Element;
import org.apache.abdera.model.ExtensibleElementWrapper;
import org.xcmis.restatom.AtomCMIS;
import org.xcmis.spi.object.impl.BaseProperty;

import javax.xml.namespace.QName;

/**
 * @author <a href="mailto:andrey.parfonov@exoplatform.com">Andrey Parfonov</a>
 * @version $Id: PropertyElement.java 2 2010-02-04 17:21:49Z andrew00x $
 */
public abstract class PropertyElement<T extends BaseProperty<?>> extends ExtensibleElementWrapper
{

   /**
    * Instantiates a new property element.
    * 
    * @param internal the internal
    */
   public PropertyElement(Element internal)
   {
      super(internal);
   }

   /**
    * Instantiates a new property element.
    * 
    * @param factory the factory
    * @param qname the qname
    */
   public PropertyElement(Factory factory, QName qname)
   {
      super(factory, qname);
   }

   /**
    * Builds the element.
    * 
    * @param property the property
    */
   public void build(T property)
   {
      if (property != null)
      {
         setAttributeValue(AtomCMIS.PROPERTY_DEFINITION_ID.getLocalPart(), property.getId());
         setAttributeValue(AtomCMIS.LOCAL_NAME.getLocalPart(), property.getLocalName());
         setAttributeValue(AtomCMIS.DISPLAY_NAME.getLocalPart(), property.getDisplayName());
         setAttributeValue(AtomCMIS.QUERY_NAME.getLocalPart(), property.getQueryName());
      }
   }

   /**
    * Gets the property.
    * 
    * @return the property
    */
   public abstract T getProperty();

   /**
    * Process property element.
    * 
    * @param obj the obj
    */
   protected void processPropertyElement(T obj)
   {
      obj.setId(getAttributeValue(AtomCMIS.PROPERTY_DEFINITION_ID.getLocalPart()));
      obj.setLocalName(getAttributeValue(AtomCMIS.LOCAL_NAME.getLocalPart()));
      obj.setDisplayName(getAttributeValue(AtomCMIS.DISPLAY_NAME.getLocalPart()));
      obj.setQueryName(getAttributeValue(AtomCMIS.QUERY_NAME.getLocalPart()));
   }

}
