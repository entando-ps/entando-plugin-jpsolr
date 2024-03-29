/*
 * Copyright 2021-Present Entando Inc. (http://www.entando.com) All rights reserved.
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

import com.agiletec.plugins.jacms.aps.system.services.searchengine.ICmsSearchEngineManager;
import java.util.Collection;
import java.util.List;
import org.entando.entando.aps.system.services.searchengine.SearchEngineFilter;
import org.entando.entando.ent.exception.EntException;
import org.entando.entando.plugins.jpsolr.aps.system.solr.model.ContentTypeSettings;
import org.entando.entando.plugins.jpsolr.aps.system.solr.model.SolrFacetedContentsResult;

/**
 * @author E.Santoboni
 */
public interface ISolrSearchEngineManager extends ICmsSearchEngineManager {
    
    public void refreshCmsFields() throws EntException;
    
    public void refreshContentType(String typeCode) throws EntException;
    
    public Thread startReloadContentsReferencesByType(String typeCode) throws EntException;
    
    public List<ContentTypeSettings> getContentTypesSettings() throws EntException;
    
    public SolrFacetedContentsResult searchFacetedEntities(SearchEngineFilter[][] filters, 
            SearchEngineFilter[] categories, Collection<String> allowedGroups) throws EntException;
    
}
