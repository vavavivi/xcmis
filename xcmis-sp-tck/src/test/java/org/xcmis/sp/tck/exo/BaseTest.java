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

import junit.framework.TestCase;

import org.exoplatform.container.StandaloneContainer;
import org.exoplatform.services.log.ExoLogger;
import org.exoplatform.services.log.Log;
import org.xcmis.spi.BaseContentStream;
import org.xcmis.spi.CmisConstants;
import org.xcmis.spi.Connection;
import org.xcmis.spi.ConstraintException;
import org.xcmis.spi.ContentStream;
import org.xcmis.spi.DocumentData;
import org.xcmis.spi.FolderData;
import org.xcmis.spi.ItemsTree;
import org.xcmis.spi.NameConstraintViolationException;
import org.xcmis.spi.RelationshipData;
import org.xcmis.spi.Storage;
import org.xcmis.spi.StorageException;
import org.xcmis.spi.StorageProvider;
import org.xcmis.spi.model.CmisObject;
import org.xcmis.spi.model.Property;
import org.xcmis.spi.model.RepositoryCapabilities;
import org.xcmis.spi.model.TypeDefinition;
import org.xcmis.spi.model.UnfileObject;
import org.xcmis.spi.model.VersioningState;
import org.xcmis.spi.model.impl.IdProperty;
import org.xcmis.spi.model.impl.StringProperty;
import org.xcmis.spi.utils.MimeType;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author <a href="mailto:alexey.zavizionov@exoplatform.com">Alexey Zavizionov</a>
 * @version $Id:  $
 */
public abstract class BaseTest extends TestCase
{

   private static final Log LOG = ExoLogger.getLogger(BaseTest.class);

   protected StandaloneContainer container;

   protected StorageProvider storageProvider;

   protected TypeDefinition documentTypeDefinition;

   protected TypeDefinition folderTypeDefinition;

   protected TypeDefinition policyTypeDefinition;

   protected TypeDefinition relationshipTypeDefinition;

   protected String rootfolderID;

   protected FolderData rootFolder;

   protected String testroot;

   protected List<String> passedTests;

   protected List<String> failedTests;

   private Connection conn;

   @Override
   public void setUp() throws Exception
   {
      String containerConf = getClass().getResource("/conf/standalone/test-jcr-sp-configuration.xml").toString();
      StandaloneContainer.addConfigurationURL(containerConf);
      container = StandaloneContainer.getInstance();

      storageProvider = (StorageProvider)container.getComponentInstanceOfType(StorageProvider.class);

      rootfolderID = getStorage().getRepositoryInfo().getRootFolderId();
      rootFolder = (FolderData)getStorage().getObjectById(rootfolderID);

      documentTypeDefinition = getStorage().getTypeDefinition("cmis:document", true);
      folderTypeDefinition = getStorage().getTypeDefinition("cmis:folder", true);
      policyTypeDefinition = getStorage().getTypeDefinition("cmis:policy", true);
      relationshipTypeDefinition = getStorage().getTypeDefinition("cmis:relationship", true);
   }

   protected void tearDown() throws Exception
   {
      if (conn != null)
         conn.close();
      super.tearDown();
   }

   protected Connection getConnection()
   {
      if (conn == null)
         conn = storageProvider.getConnection();
      return conn;
   }

   protected Storage getStorage()
   {
      return getConnection().getStorage();
   }

   /**  
    *  STRUCTURE:
    *  
    *   root
    *    - testroot
    *       |- Folder1
    *       |- Folder2
    *       |   |- Folder3
    *       |   |   |- Doc4
    *       |   |- Doc3
    *       |- Doc1
    *       |- Doc2
    *       |- Doc5
    *       |- Doc6
    *       
    *   Rel1 = doc3, doc4
    *   Rel2 = doc1, doc2
    *   Rel3 = folder2, doc1
    *  
    */
   protected void createFolderTree() throws Exception
   {
      ContentStream cs = new BaseContentStream("1234567890".getBytes(), null, new MimeType("text", "plain"));

      FolderData testroot = createFolder(rootFolder, "testroot");

      FolderData folder1 = createFolder(testroot, "folder1");

      DocumentData doc1 = createDocument(testroot, "doc1", "1234567890");

      DocumentData doc2 = createDocument(testroot, "doc2", "1234567890");
      doc2.checkout();

      FolderData folder2 = createFolder(testroot, "folder2");

      DocumentData doc3 = createDocument(folder2, "doc3", "1234567890");

      FolderData folder3 = createFolder(folder2, "folder3");

      DocumentData doc4 = createDocument(folder3, "doc4", "1234567890");

      DocumentData doc5 = createDocument(testroot, "doc5", "1234567890");
      doc5.checkout();

      DocumentData doc6 = createDocument(testroot, "doc6", "1234567890");
      doc6.checkout();

      RelationshipData rel1 =
         getStorage().createRelationship(doc3, doc4, relationshipTypeDefinition,
            getPropsMap("cmis:relationship", "rel1"), null, null);
      RelationshipData rel2 =
         getStorage().createRelationship(doc1, doc2, relationshipTypeDefinition,
            getPropsMap("cmis:relationship", "rel2"), null, null);
      RelationshipData rel3 =
         getStorage().createRelationship(folder2, doc1, relationshipTypeDefinition,
            getPropsMap("cmis:relationship", "rel3"), null, null);

      this.testroot = testroot.getObjectId();
   }

   /**
    * @param testroot2
    * @param string
    * @param documentContent
    * @param major
    * @return
    * @throws IOException 
    * @throws StorageException 
    * @throws NameConstraintViolationException 
    * @throws ConstraintException 
    */
   protected DocumentData createDocument(FolderData parentFolder, String documentName, String documentContent)
      throws ConstraintException, NameConstraintViolationException, StorageException, IOException
   {
      ContentStream cs = new BaseContentStream(documentContent.getBytes(), null, new MimeType("text", "plain"));
      DocumentData doc =
         getStorage().createDocument(parentFolder, documentTypeDefinition, getPropsMap("cmis:document", documentName),
            cs, null, null, VersioningState.MAJOR);
      return doc;
   }

   /**
    * @param parentFolder
    * @param folderTypeDefinition2
    * @return
    */
   protected FolderData createFolder(FolderData parentFolder, String folderName) throws StorageException,
      NameConstraintViolationException, ConstraintException
   {
      FolderData testroot =
         getStorage().createFolder(parentFolder, folderTypeDefinition, getPropsMap("cmis:folder", folderName), null,
            null);
      return testroot;
   }

   protected void clear()
   {
      try
      {
         FolderData rootFolder = (FolderData)getStorage().getObjectById(testroot);
         //         List<CmisObject> stuff =
         //            objectTreeToList(getConnection().getDescendants(rootfolderID, -1, true, IncludeRelationships.BOTH, false,
         //               true, "", ""));
         //         for (CmisObject one : stuff)
         //         {
         //            if (one.getRelationship().size() > 0)
         //            {
         //               for (CmisObject relation : one.getRelationship())
         //               {
         //                  String rel_id = relation.getObjectInfo().getId();
         //                  try
         //                  {
         //                     ObjectData rel_data = getStorage().getObjectById(rel_id);
         //                     getStorage().deleteObject(rel_data, true);
         //                  }
         //                  catch (ObjectNotFoundException ex)
         //                  {
         //                     continue;
         //                  }
         //               }
         //            }
         //
         //            getStorage().deleteObject(getStorage().getObjectById(one.getObjectInfo().getId()), true);
         //         }

         getStorage().deleteTree(rootFolder, true, UnfileObject.DELETE, true);

      }
      catch (Exception e)
      {
         e.printStackTrace();
      }
   }

   public List<CmisObject> objectTreeToList(List<ItemsTree<CmisObject>> source)
   {
      List<CmisObject> result = new ArrayList<CmisObject>();
      for (ItemsTree<CmisObject> one : source)
      {
         CmisObject type = one.getContainer();
         if (one.getChildren() != null)
         {
            result.addAll(objectTreeToList(one.getChildren()));
         }
         result.add(type);
      }

      return result;
   }

   protected Map<String, Property<?>> getPropsMap(String baseType, String name)
   {
      org.xcmis.spi.model.PropertyDefinition<?> def =
         PropertyDefinitions.getPropertyDefinition(baseType, CmisConstants.NAME);
      org.xcmis.spi.model.PropertyDefinition<?> def2 =
         PropertyDefinitions.getPropertyDefinition(baseType, CmisConstants.OBJECT_TYPE_ID);
      Map<String, Property<?>> properties = new HashMap<String, Property<?>>();
      properties.put(CmisConstants.NAME, new StringProperty(def.getId(), def.getQueryName(), def.getLocalName(), def
         .getDisplayName(), name));
      properties.put(CmisConstants.OBJECT_TYPE_ID, new IdProperty(def2.getId(), def2.getQueryName(), def2
         .getLocalName(), def2.getDisplayName(), baseType));

      return properties;
   }

   protected RepositoryCapabilities getCapabilities()
   {
      return getStorage().getRepositoryInfo().getCapabilities();
   }

   protected void doFail(String message) throws Exception
   {
      System.out.println("FAILED");
      //failedTests.add(arg0);
      fail(message);
   }

   protected void doFail() throws Exception
   {
      System.out.println("FAILED");
      //failedTests.add(arg0);
      fail();
   }

   protected void pass()
   {
      System.out.println("PASSED");
      //passedTests.add(o);
   }
}
