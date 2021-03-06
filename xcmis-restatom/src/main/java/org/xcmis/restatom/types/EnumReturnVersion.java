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

package org.xcmis.restatom.types;

/**
 * Enum for Return Version.
 * 
 * @author <a href="mailto:alexey.zavizionov@exoplatform.com">Alexey
 *         Zavizionov</a>
 * @version $Id: EnumReturnVersion.java 34360 2009-07-22 23:58:59Z sunman $
 * 
 */

public enum EnumReturnVersion {

   THIS("this"), //
   LATEST("latest"), //
   LATESTMAJOR("latestmajor");
   private final String value;

   EnumReturnVersion(String v)
   {
      value = v;
   }

   public String value()
   {
      return value;
   }

   public static EnumReturnVersion fromValue(String v)
   {
      for (EnumReturnVersion c : EnumReturnVersion.values())
      {
         if (c.value.equals(v))
         {
            return c;
         }
      }
      throw new IllegalArgumentException(v);
   }

   @Override
   public String toString()
   {
      return value();
   }

}
