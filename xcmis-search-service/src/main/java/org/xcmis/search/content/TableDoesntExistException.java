/*
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
package org.xcmis.search.content;

import org.xcmis.search.SearchServiceException;

/**

 */
public class TableDoesntExistException extends SearchServiceException
{

   /**
    * 
    */
   private static final long serialVersionUID = 8382555942692535582L;

   /**
    * 
    */
   public TableDoesntExistException()
   {
      super();
   }

   /**
    * @param message
    * @param cause
    */
   public TableDoesntExistException(String message, Throwable cause)
   {
      super(message, cause);
   }

   /**
    * @param message
    */
   public TableDoesntExistException(String message)
   {
      super(message);
   }

   /**
    * @param cause
    */
   public TableDoesntExistException(Throwable cause)
   {
      super(cause);
   }

}
