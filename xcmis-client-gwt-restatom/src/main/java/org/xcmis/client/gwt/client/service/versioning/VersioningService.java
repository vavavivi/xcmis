/**
 *  Copyright (C) 2010 eXo Platform SAS.
 *
 *  This is free software; you can redistribute it and/or modify it
 *  under the terms of the GNU Lesser General Public License as
 *  published by the Free Software Foundation; either version 2.1 of
 *  the License, or (at your option) any later version.
 *
 *  This software is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 *  Lesser General Public License for more details.
 *
 *  You should have received a copy of the GNU Lesser General Public
 *  License along with this software; if not, write to the Free
 *  Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 *  02110-1301 USA, or see the FSF site: http://www.fsf.org.
 */

package org.xcmis.client.gwt.client.service.versioning;

import org.xcmis.client.gwt.client.CmisArguments;
import org.xcmis.client.gwt.client.marshallers.CheckinMarshaller;
import org.xcmis.client.gwt.client.model.actions.CheckIn;
import org.xcmis.client.gwt.client.model.restatom.AtomEntry;
import org.xcmis.client.gwt.client.model.restatom.EntryCollection;
import org.xcmis.client.gwt.client.rest.AsyncRequest;
import org.xcmis.client.gwt.client.rest.AsyncRequestCallback;
import org.xcmis.client.gwt.client.rest.HTTPHeader;
import org.xcmis.client.gwt.client.rest.HTTPMethod;
import org.xcmis.client.gwt.client.service.versioning.event.AllVersionsReceivedEvent;
import org.xcmis.client.gwt.client.service.versioning.event.CancelCheckoutReceivedEvent;
import org.xcmis.client.gwt.client.service.versioning.event.CheckinReceivedEvent;
import org.xcmis.client.gwt.client.service.versioning.event.CheckoutReceivedEvent;
import org.xcmis.client.gwt.client.unmarshallers.EntryCollectionUnmarshaller;
import org.xcmis.client.gwt.client.unmarshallers.EntryUnmarshaller;

import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.http.client.RequestBuilder;

/**
 * Created by The eXo Platform SAS.
 *	
 * @author <a href="mailto:zhulevaanna@gmail.com">Ann Zhuleva</a>
 * @version $Id:   ${date} ${time}
 *
 */
public class VersioningService
{
   /**
    * Event bus.
    */
   private HandlerManager eventBus;

   /**
    * @param eventBus eventBus
    */
   public VersioningService(HandlerManager eventBus)
   {
      this.eventBus = eventBus;
   }

   /**
    * Create a private working copy of the document.
    * 
    * On success response received, {@link CheckoutReceivedEvent} event is fired
    * 
    * @param url url
    * @param objectId object id
    */
   public void checkOut(String url, String objectId)
   {
      AtomEntry document = new AtomEntry();
      CheckoutReceivedEvent event = new CheckoutReceivedEvent(document);
      EntryUnmarshaller unmarshaller = new EntryUnmarshaller(document);
      AsyncRequestCallback callback = new AsyncRequestCallback(eventBus, unmarshaller, event);
      AsyncRequest.build(RequestBuilder.POST, url + "?" + "objectid" + "=" + objectId).send(callback);
   }

   /**
    * Reverses the effect of a check-out. 
    * Removes the private working copy of the checked-out document, 
    * allowing other documents in the version series to be checked out again.
    * 
    * On success response received, {@link CancelCheckoutReceivedEvent} event is fired
    * 
    * @param url url
    */
   public void cancelCheckout(String url)
   {
      CancelCheckoutReceivedEvent event = new CancelCheckoutReceivedEvent();
      AsyncRequestCallback callback = new AsyncRequestCallback(eventBus, event);
      AsyncRequest.build(RequestBuilder.POST, url).header(HTTPHeader.X_HTTP_METHOD_OVERRIDE, HTTPMethod.DELETE).send(
         callback);
   }

   /**
    * Checks-in the Private Working Copy document.
    * 
    * On success response received, {@link CheckinReceivedEvent} event is fired
    * 
    * @param url url
    * @param checkIn checkIn
    */
   public void checkin(String url, CheckIn checkIn)
   {
      AtomEntry document = new AtomEntry();
      CheckinReceivedEvent event = new CheckinReceivedEvent(document);
      EntryUnmarshaller unmarshaller = new EntryUnmarshaller(document);

      String params = "";
      params += CmisArguments.MAJOR + "=" + checkIn.getMajor() + "&";
      params +=
         (checkIn.getCheckinComment() == null || checkIn.getCheckinComment().length() <= 0) ? ""
            : CmisArguments.CHECKIN_COMMENT + "=" + checkIn.getCheckinComment();

      CheckinMarshaller marshaller = new CheckinMarshaller(checkIn);

      AsyncRequestCallback callback = new AsyncRequestCallback(eventBus, unmarshaller, event);
      AsyncRequest.build(RequestBuilder.POST, url + "?" + CmisArguments.CHECKIN + "=true" + "&" + params).header(
         HTTPHeader.X_HTTP_METHOD_OVERRIDE, HTTPMethod.PUT).header(HTTPHeader.CONTENT_TYPE,
         "application/atom+xml;type=entry").data(marshaller).send(callback);
   }

   /**
    * Returns the list of all Document Objects in the specified Version Series, 
    * sorted by cmis:creationDate descending.
    * 
    * On success response received, {@link AllVersionsReceivedEvent} event is fired
    *  
    * @param url url
    * @param filter filter
    * @param includeAllowableActions include allowable actions
    */
   public void getAllVersions(String url, String filter, boolean includeAllowableActions)
   {
      EntryCollection entryCollection = new EntryCollection();
      AllVersionsReceivedEvent event = new AllVersionsReceivedEvent(entryCollection);
      EntryCollectionUnmarshaller unmarshaller = new EntryCollectionUnmarshaller(entryCollection);

      String params = CmisArguments.INCLUDE_ALLOWABLE_ACTIONS + "=" + includeAllowableActions + "&";
      params += (filter == null || filter.length() <= 0) ? "" : CmisArguments.FILTER + "=" + filter;

      AsyncRequestCallback callback = new AsyncRequestCallback(eventBus, unmarshaller, event);
      AsyncRequest.build(RequestBuilder.GET, url + "?" + params).send(callback);
   }

}