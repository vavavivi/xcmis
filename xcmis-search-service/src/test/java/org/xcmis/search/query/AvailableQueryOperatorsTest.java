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
package org.xcmis.search.query;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static org.xcmis.search.model.constraint.Operator.EQUAL_TO;
import static org.xcmis.search.model.constraint.Operator.GREATER_THAN;
import static org.xcmis.search.model.constraint.Operator.GREATER_THAN_OR_EQUAL_TO;
import static org.xcmis.search.model.constraint.Operator.LESS_THAN;
import static org.xcmis.search.model.constraint.Operator.LESS_THAN_OR_EQUAL_TO;
import static org.xcmis.search.model.constraint.Operator.LIKE;
import static org.xcmis.search.model.constraint.Operator.NOT_EQUAL_TO;

import org.apache.commons.io.FileUtils;
import org.exoplatform.services.document.DocumentReaderService;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.xcmis.search.InvalidQueryException;
import org.xcmis.search.SearchService;
import org.xcmis.search.SearchServiceException;
import org.xcmis.search.config.IndexConfiguration;
import org.xcmis.search.config.SearchServiceConfiguration;
import org.xcmis.search.content.InMemorySchema;
import org.xcmis.search.content.Schema;
import org.xcmis.search.content.InMemorySchema.Builder;
import org.xcmis.search.content.Schema.Column;
import org.xcmis.search.content.Schema.Table;
import org.xcmis.search.content.command.InvocationContext;
import org.xcmis.search.content.interceptors.ContentReaderInterceptor;
import org.xcmis.search.lucene.InMemoryLuceneQueryableIndexStorage;
import org.xcmis.search.lucene.content.SchemaTableResolver;
import org.xcmis.search.model.Query;
import org.xcmis.search.model.constraint.Operator;
import org.xcmis.search.model.source.SelectorName;
import org.xcmis.search.query.QueryBuilder.ComparisonBuilder;
import org.xcmis.search.query.QueryBuilder.ConstraintBuilder;
import org.xcmis.search.value.CastSystem;
import org.xcmis.search.value.NameConverter;
import org.xcmis.search.value.SlashSplitter;
import org.xcmis.search.value.ToStringNameConverter;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * @author <a href="mailto:Sergey.Kabashnyuk@exoplatform.org">Sergey Kabashnyuk</a>
 * @version $Id: exo-jboss-codetemplates.xml 34360 2009-07-22 23:58:59Z ksm $
 *
 */
public class AvailableQueryOperatorsTest
{
   private QueryBuilder builder;

   private File tempDir;

   private Builder schemaBuilder;

   private Schema schema;

   private QueryBuilder qBuilder;

   private SearchService searchService;

   private final static String TABLE_NAME = "someTable";

   @Before
   public void beforeEach() throws SearchServiceException
   {
      builder = new QueryBuilder(mock(CastSystem.class));

      schemaBuilder = InMemorySchema.createBuilder();

      schema =
         schemaBuilder.addTable(TABLE_NAME, "column1")//
            .addColumn(TABLE_NAME, "booleanColumn", "String", true, new Operator[]{EQUAL_TO})//
            .addColumn(TABLE_NAME, "idColumn", "String", true, new Operator[]{EQUAL_TO, NOT_EQUAL_TO})//
            .addColumn(TABLE_NAME, "integerColumn", "String", true, new Operator[]{EQUAL_TO, GREATER_THAN, //
               GREATER_THAN_OR_EQUAL_TO, LESS_THAN, LESS_THAN_OR_EQUAL_TO, NOT_EQUAL_TO})//
            .addColumn(TABLE_NAME,
               "dateTimeColumn",
               "String",
               true,//
               new Operator[]{EQUAL_TO, GREATER_THAN, GREATER_THAN_OR_EQUAL_TO, LESS_THAN, LESS_THAN_OR_EQUAL_TO,
                  NOT_EQUAL_TO})//
            .addColumn(
               TABLE_NAME,
               "decimalColumn",
               "String",
               true,
               new Operator[]{EQUAL_TO, GREATER_THAN, GREATER_THAN_OR_EQUAL_TO, LESS_THAN, LESS_THAN_OR_EQUAL_TO,
                  NOT_EQUAL_TO})//
            .addColumn(TABLE_NAME, "htmlColumn", "String", true,
               new Operator[]{EQUAL_TO, GREATER_THAN, LIKE, NOT_EQUAL_TO})//
            .addColumn(TABLE_NAME, "stringColumn", "String", true,
               new Operator[]{EQUAL_TO, GREATER_THAN, LIKE, NOT_EQUAL_TO})//
            .addColumn(TABLE_NAME, "uriColumn", "String", true, new Operator[]{EQUAL_TO, NOT_EQUAL_TO, LIKE}).build();

      tempDir = new File(System.getProperty("java.io.tmpdir"), "search-service");
      if (tempDir.exists())
      {
         assertThat(FileUtils.deleteQuietly(tempDir), is(true));
      }
      assertThat(tempDir.mkdirs(), is(true));

      qBuilder = new QueryBuilder(mock(CastSystem.class));

      //value
      NameConverter<String> nameConverter = new ToStringNameConverter();
      SchemaTableResolver tableResolver = new SchemaTableResolver(nameConverter, schema);

      //index configuration
      IndexConfiguration indexConfuration = new IndexConfiguration();
      indexConfuration.setIndexDir(tempDir.getAbsolutePath());
      indexConfuration.setRootParentUuid(UUID.randomUUID().toString());
      indexConfuration.setRootUuid(UUID.randomUUID().toString());
      indexConfuration.setDocumentReaderService(mock(DocumentReaderService.class));
      indexConfuration.setQueryableIndexStorage(InMemoryLuceneQueryableIndexStorage.class.getName());

      //search service configuration
      SearchServiceConfiguration configuration = new SearchServiceConfiguration();
      configuration.setIndexConfiguration(indexConfuration);
      configuration.setContentReader(mock(ContentReaderInterceptor.class));
      configuration.setNameConverter(nameConverter);
      configuration.setTableResolver(tableResolver);
      configuration.setPathSplitter(new SlashSplitter());

      InvocationContext invocationContext = new InvocationContext();
      invocationContext.setSchema(schema);

      invocationContext.setTableResolver(tableResolver);
      invocationContext.setNameConverter(nameConverter);

      configuration.setDefaultInvocationContext(invocationContext);

      searchService = new SearchService(configuration);
      searchService.start();

   }

   @Test
   public void availableQueryOperatorsTest() throws QueryExecutionException, InvalidQueryException
   {
      Table table = schema.getTable(new SelectorName(TABLE_NAME));

      for (Column column : table.getColumns())
      {
         checkValid(column);
         checkInValid(column);
      }
   }

   /**
    * Check invalid operator's
    * @param column
    * @throws QueryExecutionException 
    */
   private void checkInValid(Column column) throws QueryExecutionException
   {

      Operator[] unAvailableQueryOperators = getUnAvailableQueryOperators(column.getAvailableQueryOperators());
      for (Operator operator : unAvailableQueryOperators)
      {
         ComparisonBuilder q =
            qBuilder.select(column.getName()).from(TABLE_NAME).where().propertyValue(TABLE_NAME, column.getName());

         ConstraintBuilder resultQ = null;
         switch (operator)
         {

            case EQUAL_TO :
               resultQ = q.isEqualTo().literal(new Long(1));
               break;
            case GREATER_THAN :
               resultQ = q.isGreaterThan().literal(new Long(1));
               break;
            case GREATER_THAN_OR_EQUAL_TO :
               resultQ = q.isGreaterThanOrEqualTo().literal(new Long(1));
               break;
            case LESS_THAN :
               resultQ = q.isLessThan().literal(new Long(1));
               break;
            case LESS_THAN_OR_EQUAL_TO :
               resultQ = q.isLessThanOrEqualTo().literal(new Long(1));
               break;
            case LIKE :
               resultQ = q.isLike().literal(new Long(1));
               break;
            case NOT_EQUAL_TO :
               resultQ = q.isNotEqualTo().literal(new Long(1));
               break;
            default :
               Assert.fail("unknown operator " + operator);
               break;
         }
         Query query = resultQ.end().query();
         try
         {
            searchService.execute(query);
            Assert.fail("InvalidQueryException should be thrown for invalid operator " + operator + " for columnt ='"
               + column.getName() + "'");
         }
         catch (InvalidQueryException e)
         {
            //ok
         }
      }
   }

   /**
    * Return the array of unAvailableQueryOperators
    * @param availableQueryOperators
    * @return
    */
   private Operator[] getUnAvailableQueryOperators(Operator[] availableQueryOperators)
   {
      List<Operator> result = new ArrayList<Operator>();
      for (Operator operator : Operator.ALL)
      {
         boolean isValid = false;
         for (int i = 0; i < availableQueryOperators.length; i++)
         {
            if (operator.equals(availableQueryOperators[i]))
            {
               isValid = true;
               break;
            }
         }
         if (!isValid)
         {
            result.add(operator);
         }
      }
      return result.toArray(new Operator[result.size()]);
   }

   /**
    * Check valid operator's
    * 
    * @param column
    * @throws InvalidQueryException 
    * @throws QueryExecutionException 
    */
   private void checkValid(Column column) throws QueryExecutionException, InvalidQueryException
   {

      for (Operator operator : column.getAvailableQueryOperators())
      {
         ComparisonBuilder q =
            qBuilder.select(column.getName()).from(TABLE_NAME).where().propertyValue(TABLE_NAME, column.getName());

         ConstraintBuilder resultQ = null;
         switch (operator)
         {

            case EQUAL_TO :
               resultQ = q.isEqualTo().literal(new Long(1));
               break;
            case GREATER_THAN :
               resultQ = q.isGreaterThan().literal(new Long(1));
               break;
            case GREATER_THAN_OR_EQUAL_TO :
               resultQ = q.isGreaterThanOrEqualTo().literal(new Long(1));
               break;
            case LESS_THAN :
               resultQ = q.isLessThan().literal(new Long(1));
               break;
            case LESS_THAN_OR_EQUAL_TO :
               resultQ = q.isLessThanOrEqualTo().literal(new Long(1));
               break;
            case LIKE :
               resultQ = q.isLike().literal(new Long(1));
               break;
            case NOT_EQUAL_TO :
               resultQ = q.isNotEqualTo().literal(new Long(1));
               break;
            default :
               Assert.fail("unknown operator " + operator);
               break;
         }
         Query query = resultQ.end().query();
         searchService.execute(query);
      }
   }
}
