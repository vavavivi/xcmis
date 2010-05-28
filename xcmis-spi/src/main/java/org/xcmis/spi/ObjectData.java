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

package org.xcmis.spi;

import org.xcmis.spi.model.AccessControlEntry;
import org.xcmis.spi.model.BaseType;
import org.xcmis.spi.model.Property;
import org.xcmis.spi.model.RelationshipDirection;
import org.xcmis.spi.model.TypeDefinition;
import org.xcmis.spi.model.Permission.BasicPermissions;

import java.util.Calendar;
import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * @author <a href="mailto:andrey00x@gmail.com">Andrey Parfonov</a>
 * @version $Id: ObjectData.java 316 2010-03-09 15:20:28Z andrew00x $
 */
public interface ObjectData
{

   void accept(ObjectDataVisitor visitor);

   // ACL

   /**
    * Set new ACL for object. New ACL overwrite existed one. If ACL capability
    * is not supported then this method must throw {@link NotSupportedException}
    * .
    *
    * @param acl ACL that should replace currently applied ACL
    * @throws ConstraintException if current object is not controllable by ACL,
    *         see {@link TypeDefinition#isControllableACL()}.
    */
   void setACL(List<AccessControlEntry> acl) throws ConstraintException;

   /**
    * Get ACL currently applied to object. If ACL capability is not supported
    * then this method must throw {@link NotSupportedException}.
    *
    * @param onlyBasicPermissions if <code>true</code> then only CMIS basic
    *        permissions {@link BasicPermissions} must be returned if
    *        <code>false</code> then basic permissions and repository specific
    *        permissions must be returned
    * @return applied ACL. If there is no ACL applied to object or if object is
    *         not controllable by ACL empty list must be returned, never
    *         <code>null</code>
    * @see BasicPermissions
    */
   List<AccessControlEntry> getACL(boolean onlyBasicPermissions);

   // Policies

   /**
    * Applied specified policy to the current object. If Policy object type is
    * not supported then this method must throw {@link NotSupportedException}.
    *
    * @param policy policy to be applied
    * @throws ConstraintException if current object is not controllable by
    *         Policy, see {@link TypeDefinition#isControllablePolicy()}.
    */
   void applyPolicy(PolicyData policy) throws ConstraintException;

   /**
    * Get policies applied to the current object. If Policy object type is not
    * supported then this method must throw {@link NotSupportedException}.
    *
    * @return applied Policies. If there is no policies applied to object or if
    *         object is not controllable by policy then empty list must be
    *         returned, never <code>null</code>
    */
   Collection<PolicyData> getPolicies();

   /**
    * Remove specified policy from object. This method must not remove Policy
    * object itself. If Policy object type is not supported then this method
    * must throw {@link NotSupportedException}.
    *
    * @param policy the policy object
    * @throws ConstraintException if current object is not controllable by
    *         Policy, see {@link TypeDefinition#isControllablePolicy()}.
    */
   void removePolicy(PolicyData policy) throws ConstraintException;

   //

   /**
    * To get object's base type.
    * 
    * @return base type of object
    * @see BaseType
    */
   BaseType getBaseType();

   /**
    * Shortcut to 'cmis:changeToken' property.
    *
    * @return 'cmis:changeToken' property
    */
   String getChangeToken();

   /**
    * Shortcut to 'cmis:createdBy' property.
    *
    * @return 'cmis:createdBy' property
    */
   String getCreatedBy();

   /**
    * Shortcut to 'cmis:creationDate' property.
    *
    * @return 'cmis:creationDate' property
    */
   Calendar getCreationDate();

   /**
    * Shortcut to 'cmis:lastModifiedBy' property.
    *
    * @return 'cmis:lastModifiedBy' property
    */
   String getLastModifiedBy();

   /**
    * Shortcut to 'cmis:lastModificationDate' property.
    *
    * @return 'cmis:lastModificationDate' property
    */
   Calendar getLastModificationDate();

   /**
    * Shortcut to 'cmis:name' property.
    *
    * @return 'cmis:name' property
    */
   String getName();

   /**
    * Shortcut to 'cmis:objectId' property.
    *
    * @return 'cmis:objectId' property
    */
   String getObjectId();

   /**
    * Get object parent.
    *
    * @return parent of current object
    * @throws ConstraintException if object has more then one parent or if
    *         current object is root folder
    */
   FolderData getParent() throws ConstraintException;

   /**
    * Get collections of parent folders. It may contains exactly one object for
    * single-filed and empty collection for unfiled object or root folder.
    *
    * @return collection of object's parents
    */
   Collection<FolderData> getParents();

   /**
    * Objects relationships.
    *
    * @param direction relationship's direction.
    * @param type relationship type. If
    *        <code>includeSubRelationshipTypes == true</code> then all
    *        descendants of this type must be returned. If
    *        <code>includeSubRelationshipTypes == true</code> only relationship
    *        of the same type must be returned
    * @param includeSubRelationshipTypes if <code>true</code>, then the return
    *        all relationships whose object types are descendant types of
    *        <code>typeId</code>.
    * @return relationships if object has not any relationships then empty
    *         {@link ItemsIterator} must be returned, never <code>null</code>
    * @see RelationshipDirection
    */
   ItemsIterator<RelationshipData> getRelationships(RelationshipDirection direction, TypeDefinition type,
      boolean includeSubRelationshipTypes);

   /**
    * To get the object's type id.
    * 
    * @return type id
    */
   String getTypeId();

   /**
    * To get the object's type definition.
    * 
    * @return type definition of object
    */
   TypeDefinition getTypeDefinition();

   /**
    * Shortcut setter for 'cmis:name' property.
    *
    *@param name the String name
    * @throws NameConstraintViolationException if <i>cmis:name</i> specified in
    *         properties throws conflict
    */
   void setName(String name) throws NameConstraintViolationException;

   // Properties

   /**
    * @param id property ID
    * @return property with specified ID or <code>null</code>
    */
   Property<?> getProperty(String id);

   /**
    * To get the object's properties.
    * 
    * @return the set of CMIS properties
    */
   Map<String, Property<?>> getProperties();

   /**
    * Get subset of properties accepted by {@link PropertyFilter}.
    *
    * @param filter property filter
    * @return subset of properties
    */
   Map<String, Property<?>> getProperties(PropertyFilter filter);

   /**
    * Set or update property. Changes will be updated immediately.
    * This is implementation specific. Empty
    * list for property value {@link Property#getValues()} minds the property
    * will be in 'value not set' state. If property is required then
    * {@link ConstraintException} will be thrown.
    *
    * @param property the new property
    * @throws ConstraintException if value of the property violates the
    *         min/max/required/length constraints specified in the property
    *         definition in the object type
    */
   void setProperty(Property<?> property) throws ConstraintException;

   /**
    * Set or add new properties. Properties will be merged with existed one and
    * not replace whole set of existed properties. Changes will be updated
    * immediately. This is
    * implementation specific. Empty list for property value
    * {@link Property#getValues()} minds the property will be in 'value not set'
    * state. If property is required then {@link ConstraintException} will be
    * thrown.
    *
    * @param properties the new set of properties
    * @throws ConstraintException if value of any of the properties violates the
    *         min/max/required/length constraints specified in the property
    *         definition in the object type
    * @throws NameConstraintViolationException if <i>cmis:name</i> specified in
    *         properties throws conflict
    */
   void setProperties(Map<String, Property<?>> properties) throws ConstraintException, NameConstraintViolationException;

   // Content stream

   /**
    * Get the content stream with specified id. Often it should be rendition
    * stream. If object has type other then Document and
    * <code>streamId == null</code> then this method return <code>null</code>.
    * For Document objects default content stream will be returned.
    *
    * @param streamId the content stream id
    * @return content stream or <code>null</code>
    */
   ContentStream getContentStream(String streamId);

}
