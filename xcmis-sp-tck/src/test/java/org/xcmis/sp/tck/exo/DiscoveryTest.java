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

package org.xcmis.sp.tck.exo;

import org.xcmis.spi.CmisConstants;
import org.xcmis.spi.DocumentData;
import org.xcmis.spi.FolderData;
import org.xcmis.spi.ItemsList;
import org.xcmis.spi.RenditionFilter;
import org.xcmis.spi.model.CmisObject;
import org.xcmis.spi.model.IncludeRelationships;

import java.util.List;

/**
 * 2.2.6 Discovery Services
 * The Discovery Services (query) are used to search for query-able objects within the Repository.
 * 
 * @author <a href="mailto:alexey.zavizionov@exoplatform.com">Alexey Zavizionov</a>
 * @version $Id:  $
 */
public class DiscoveryTest extends BaseTest
{

   @Override
   public void setUp() throws Exception
   {
      super.setUp();
   }

   /**
    * 2.2.6.1 query.
    * 
    * Description: Executes a CMIS query statement against the contents of the Repository.
    */
   public void testQuery() throws Exception
   {
      String errSms = "\n 2.2.6.1 query. Doesn't work Query (Discovery service) with cmis:document to search content.";

      FolderData parentFolder = createFolder(rootFolder, "folder1");;
      DocumentData documentData = createDocument(parentFolder, "doc1", "Hello World!");
      String statement = "SELECT * FROM " + CmisConstants.DOCUMENT + " WHERE CONTAINS(\"Hello\")";
      ItemsList<CmisObject> query = null;

      query =
         getConnection().query(statement, true, false, IncludeRelationships.BOTH, true, RenditionFilter.ANY, -1, 0);

      assertNotNull(errSms, query);
      assertNotNull(errSms, query.getItems());
      assertNotNull(errSms, query.getItems().size());
      assertEquals(errSms, 1, query.getItems().size());

      List<CmisObject> result = query.getItems();
      for (CmisObject cmisObject : result)
      {
         assertNotNull(errSms, cmisObject);
         assertNotNull(errSms, cmisObject.getObjectInfo());
         assertNotNull(errSms, cmisObject.getObjectInfo().getId());
         assertEquals(errSms, documentData.getObjectId(), cmisObject.getObjectInfo().getId());
         assertEquals(errSms, documentData.getName(), cmisObject.getObjectInfo().getName());
      }
   }

   /**
    * 2.2.6.1 query.
    * 
    * Description: Executes a CMIS query statement against the contents of the Repository.
    */
   public void testQuery2() throws Exception
   {
      String errSms = "\n 2.2.6.1 query. Doesn't work Query (Discovery service) with cmis:document to search content.";

      FolderData parentFolder = createFolder(rootFolder, "folder1");;
      DocumentData documentData = createDocument(parentFolder, "doc1", "Hello World!");
      String statement = "SELECT * FROM " + CmisConstants.DOCUMENT + " WHERE CONTAINS(\"Hello\")";
      ItemsList<CmisObject> query = null;

      query =
         getConnection().query(statement, false, false, IncludeRelationships.BOTH, true, RenditionFilter.ANY, -1, 0);

      assertNotNull(errSms, query);
      assertNotNull(errSms, query.getItems());
      assertNotNull(errSms, query.getItems().size());
      assertEquals(errSms, 1, query.getItems().size());

      List<CmisObject> result = query.getItems();
      for (CmisObject cmisObject : result)
      {
         assertNotNull(errSms, cmisObject);
         assertNotNull(errSms, cmisObject.getObjectInfo());
         assertNotNull(errSms, cmisObject.getObjectInfo().getId());
         assertEquals(errSms, documentData.getObjectId(), cmisObject.getObjectInfo().getId());
         assertEquals(errSms, documentData.getName(), cmisObject.getObjectInfo().getName());
      }
   }

   //   /**
   //    * @see org.xcmis.sp.tck.exo.BaseTest#tearDown()
   //    */
   //   @Override
   //   protected void tearDown() throws Exception
   //   {
   //      ItemsList<CmisObject> children =
   //         getConnection().getChildren(rootfolderID, false, null, false, true, null, null, null, -1, 0);
   //      if (children != null && children.getItems() != null)
   //      {
   //         List<CmisObject> listChildren = children.getItems();
   //         for (CmisObject cmisObject : listChildren)
   //         {
   //            remove(cmisObject);
   //         }
   //      }
   //      super.tearDown();
   //   }
   //
   //   private void remove(CmisObject cmisObject) throws ObjectNotFoundException, ConstraintException,
   //      UpdateConflictException, VersioningException, StorageException, InvalidArgumentException, FilterNotValidException
   //   {
   //
   //      if (cmisObject.getObjectInfo().getBaseType().equals(BaseType.FOLDER))
   //      {
   //         ItemsList<CmisObject> children =
   //            getConnection().getChildren(cmisObject.getObjectInfo().getId(), false, null, false, true, null, null, null,
   //               -1, 0);
   //         if (children != null && children.getItems() != null)
   //         {
   //            List<CmisObject> listChildren = children.getItems();
   //            for (CmisObject cmisObject0 : listChildren)
   //            {
   //               remove(cmisObject0);
   //            }
   //         }
   //      }
   //      getConnection().deleteObject(cmisObject.getObjectInfo().getId(), true);
   //   }

}
