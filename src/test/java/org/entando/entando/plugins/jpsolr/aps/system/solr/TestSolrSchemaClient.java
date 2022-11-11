/*
 * Copyright 2022-Present Entando Inc. (http://www.entando.com) All rights reserved.
 *
 * This library is free software; you can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation; either version 2.1 of the License, or (at your option)
 * any later version.
 *
 * This library is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 */
package org.entando.entando.plugins.jpsolr.aps.system.solr;

import com.agiletec.aps.BaseTestCase;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.entando.entando.plugins.jpsolr.CustomBaseTestCase;
import org.entando.entando.plugins.jpsolr.SolrTestUtils;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

/**
 * @author E.Santoboni
 */
public class TestSolrSchemaClient {

    @BeforeAll
    public static void startUp() throws Exception {
        SolrTestUtils.startContainer();
        CustomBaseTestCase.setUp();
    }
    
    @AfterAll
    public static void tearDown() throws Exception {
        BaseTestCase.tearDown();
        SolrTestUtils.stopContainer();
    }
    
    @Test
    public void testGetFields() throws Throwable {
        String address = System.getenv("SOLR_ADDRESS");
        String core = System.getenv("SOLR_CORE");
        List<Map<String, Object>> fields = SolrSchemaClient.getFields(address, core);
        Assertions.assertNotNull(fields);
    }
    
    @Test
    public void testAddDeleteField() throws Throwable {
        String address = System.getenv("SOLR_ADDRESS");
        String core = System.getenv("SOLR_CORE");
        String fieldName = "test_solr";
        List<Map<String, Object>> fields = SolrSchemaClient.getFields(address, core);
        Assertions.assertNotNull(fields);
        try {
            Map<String, Object> addedFiled = fields.stream().filter(f -> f.get("name").equals(fieldName)).findFirst().orElse(null);
            Assertions.assertNull(addedFiled);

            Map<String, Object> properties = new HashMap<>();
            properties.put("name", fieldName);
            properties.put("type", "text_general");
            boolean result = SolrSchemaClient.addField(address, core, properties);
            Assertions.assertTrue(result);

            fields = SolrSchemaClient.getFields(address, core);
            Assertions.assertNotNull(fields);
            addedFiled = fields.stream().filter(f -> f.get("name").equals(fieldName)).findFirst().orElse(null);
            Assertions.assertNotNull(addedFiled);
            Assertions.assertEquals("text_general", addedFiled.get("type"));

            properties.put("type", "plong");
            result = SolrSchemaClient.replaceField(address, core, properties);
            Assertions.assertTrue(result);

            fields = SolrSchemaClient.getFields(address, core);
            Assertions.assertNotNull(fields);
            addedFiled = fields.stream().filter(f -> f.get("name").equals(fieldName)).findFirst().orElse(null);
            Assertions.assertNotNull(addedFiled);
            Assertions.assertEquals("plong", addedFiled.get("type"));
        } catch (Exception e) {
        } finally {
            boolean result = SolrSchemaClient.deleteField(address, core, fieldName);
            Assertions.assertTrue(result);

            fields = SolrSchemaClient.getFields(address, core);
            Assertions.assertNotNull(fields);
            Map<String, Object> addedFiled = fields.stream().filter(f -> f.get("name").equals(fieldName)).findFirst().orElse(null);
            Assertions.assertNull(addedFiled);
        }
    }
    
}
