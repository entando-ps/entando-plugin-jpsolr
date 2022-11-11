/*
 * Copyright 2015-Present Entando Inc. (http://www.entando.com) All rights reserved.
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
package org.entando.entando.plugins.jpsolr;

import static com.agiletec.aps.BaseTestCase.createRequestContext;

import com.agiletec.aps.BaseTestCase;
import com.agiletec.aps.system.RequestContext;
import javax.servlet.ServletContext;
import org.junit.jupiter.api.BeforeAll;
import org.springframework.context.ApplicationContext;
import org.springframework.core.io.FileSystemResourceLoader;
import org.springframework.mock.web.MockServletContext;

public class CustomBaseTestCase extends BaseTestCase {
    
    @BeforeAll
    public static void setUp() throws Exception {
        try {
            ServletContext srvCtx = new MockServletContext("", new FileSystemResourceLoader());
            ApplicationContext applicationContext = new CustomConfigTestUtils().createApplicationContext(srvCtx);
            setApplicationContext(applicationContext);
            RequestContext reqCtx = createRequestContext(applicationContext, srvCtx);
            setRequestContext(reqCtx);
            setUserOnSession("guest");
        } catch (Exception e) {
            throw e;
        }
    }
    
}
