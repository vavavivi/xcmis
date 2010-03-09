/*
 * Copyright (C) 2009 eXo Platform SAS.
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
package org.xcmis.search.result;

import org.xcmis.search.SearchServiceException;
import org.xcmis.search.model.ordering.Ordering;


/**
 * @author <a href="mailto:Sergey.Kabashnyuk@gmail.com">Sergey Kabashnyuk</a>
 * @version $Id: exo-jboss-codetemplates.xml 34027 2009-07-15 23:26:43Z
 *          aheritier $
 */
public interface ResultSorterFactory
{

   /**
    * Return default result sorter for given selectors.
    * 
    * @param orderings
    * @return
    */
   ResultSorter getDefaultResultSorter(String[] selectorNames);

   /**
    * @param orderings
    * @return
    * @throws SearchServiceException 
    */
   ResultSorter getResultSorter(Ordering[] orderings) throws SearchServiceException;

}