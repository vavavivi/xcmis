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
import org.xcmis.restatom.AtomCMIS;
import org.xcmis.spi.model.impl.StringProperty;

import java.util.List;

import javax.xml.namespace.QName;

/**
 * @author <a href="mailto:andrey.parfonov@exoplatform.com">Andrey Parfonov</a>
 * @version $Id: PropertyStringElement.java 2 2010-02-04 17:21:49Z andrew00x $
 */
public class PropertyStringElement extends PropertyElement<StringProperty>
{

   /**
    * Instantiates a new property string element.
    * 
    * @param internal the internal
    */
   public PropertyStringElement(Element internal)
   {
      super(internal);
   }

   /**
    * Instantiates a new property string element.
    * 
    * @param factory the factory
    * @param qname the qname
    */
   public PropertyStringElement(Factory factory, QName qname)
   {
      super(factory, qname);
   }

   /**
    * {@inheritDoc}
    */
   @Override
   public void build(StringProperty value)
   {
      if (value != null)
      {
         super.build(value);

         List<String> listString = value.getValues();
         if (listString != null && listString.size() > 0)
         {
            for (String v : listString)
            {
               addSimpleExtension(AtomCMIS.VALUE, v);
            }
         }
      }
   }

   /**
    * {@inheritDoc}
    */
   public StringProperty getProperty()
   {
      StringProperty s = new StringProperty();
      processPropertyElement(s);
      if (getElements() != null && getElements().size() > 0)
      {
         for (Element el : getElements())
         {
            s.getValues().add(el.getText());
         }
      }
      return s;
   }

}
