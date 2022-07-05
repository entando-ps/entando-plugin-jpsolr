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

import com.agiletec.aps.system.EntThreadLocal;
import com.agiletec.aps.system.services.baseconfig.ConfigInterface;
import com.agiletec.aps.system.services.category.ICategoryManager;
import com.agiletec.aps.system.services.lang.ILangManager;
import com.agiletec.plugins.jacms.aps.system.services.searchengine.IIndexerDAO;
import com.agiletec.plugins.jacms.aps.system.services.searchengine.ISearchEngineDAOFactory;
import com.agiletec.plugins.jacms.aps.system.services.searchengine.ISearcherDAO;
import java.util.List;
import java.util.Map;
import org.apache.commons.lang3.StringUtils;
import org.entando.entando.aps.system.services.tenant.ITenantManager;
import org.entando.entando.aps.system.services.tenant.TenantConfig;
import org.entando.entando.ent.exception.EntException;
import org.entando.entando.ent.util.EntLogging.EntLogFactory;
import org.entando.entando.ent.util.EntLogging.EntLogger;
import org.springframework.beans.factory.annotation.Value;

/**
 * Classe factory degli elementi ad uso del SearchEngine.
 *
 * @author E.Santoboni
 */
public class SearchEngineDAOFactory implements ISearchEngineDAOFactory, ISolrSearchEngineDAOFactory {

    private static final EntLogger logger = EntLogFactory.getSanitizedLogger(SearchEngineDAOFactory.class);
    
    private static final String SOLR_ADDRESS_TENANT_PARAM = "solrAddress";
    private static final String SOLR_CORE_TENANT_PARAM = "solrCore";
    
    @Value("${SOLR_ADDRESS:http://localhost:8983/solr}")
    private String solrAddress;
    
    @Value("${SOLR_CORE:entando}")
    private String solrCore;
    
    private ConfigInterface configManager;
    private ILangManager langManager;
    private ICategoryManager categoryManager;
    private ITenantManager tenantManager;

    @Override
    public void init() throws Exception {
        // nothing to do
    }

    @Override
    public List<Map<String, Object>> getFields() {
        TenantConfig config = this.getTenantConfig();
        return SolrSchemaClient.getFields(this.getSolrAddress(config), this.getSolrCore(config));
    }

    @Override
    public boolean addField(Map<String, Object> properties) {
        TenantConfig config = this.getTenantConfig();
        return SolrSchemaClient.addField(this.getSolrAddress(config), this.getSolrCore(config), properties);
    }

    @Override
    public boolean replaceField(Map<String, Object> properties) {
        TenantConfig config = this.getTenantConfig();
        return SolrSchemaClient.replaceField(this.getSolrAddress(config), this.getSolrCore(config), properties);
    }

    @Override
    public boolean deleteField(String fieldKey) {
        TenantConfig config = this.getTenantConfig();
        return SolrSchemaClient.deleteField(this.getSolrAddress(config), this.getSolrCore(config), fieldKey);
    }

    @Override
    public boolean deleteAllDocuments() {
        TenantConfig config = this.getTenantConfig();
        return SolrSchemaClient.deleteAllDocuments(this.getSolrAddress(config), this.getSolrCore(config));
    }
    
    @Override
    public boolean checkCurrentSubfolder() throws EntException {
        // nothing to do
        return true;
    }

    @Override
    public IIndexerDAO getIndexer() throws EntException {
        TenantConfig config = this.getTenantConfig();
        IndexerDAO indexerDao = new IndexerDAO();
        indexerDao.setLangManager(this.getLangManager());
        indexerDao.setTreeNodeManager(this.getCategoryManager());
        indexerDao.setSolrAddress(this.getSolrAddress(config));
        indexerDao.setSolrCore(this.getSolrCore(config));
        return indexerDao;
    }

    @Override
    public ISearcherDAO getSearcher() throws EntException {
        TenantConfig config = this.getTenantConfig();
        SearcherDAO searcherDao = new SearcherDAO();
        searcherDao.setTreeNodeManager(this.getCategoryManager());
        searcherDao.setLangManager(this.getLangManager());
        searcherDao.setSolrAddress(this.getSolrAddress(config));
        searcherDao.setSolrCore(this.getSolrCore(config));
        return searcherDao;
    }

    @Override
    public IIndexerDAO getIndexer(String subDir) throws EntException {
        return this.getIndexer();
    }

    @Deprecated
    @Override
    public ISearcherDAO getSearcher(String subDir) throws EntException {
        return this.getSearcher();
    }

    @Override
    public void updateSubDir(String newSubDirectory) throws EntException {
        // nothing to do
    }
    
    @Override
    public void deleteSubDirectory(String subDirectory) {
        // nothing to do
    }

    public String getSolrAddress(TenantConfig config) {
        return this.getTenantParameter(config, SOLR_ADDRESS_TENANT_PARAM, this.solrAddress);
    }

    public String getSolrCore(TenantConfig config) {
        return this.getTenantParameter(config, SOLR_CORE_TENANT_PARAM, this.solrCore);
    }
    
    private String getTenantParameter(TenantConfig config, String paramName, String inCaseOfNull) {
        if (null != config) {
            return config.getProperty(paramName);
        }
        return inCaseOfNull;
    }
    
    private TenantConfig getTenantConfig() {
        String tenantCode = (String) EntThreadLocal.get(ITenantManager.THREAD_LOCAL_TENANT_CODE);
        TenantConfig config = null;
        if (!StringUtils.isBlank(tenantCode)) {
            config = this.getTenantManager().getConfig(tenantCode);
        }
        return config;
    }
    
    protected ConfigInterface getConfigManager() {
        return configManager;
    }
    public void setConfigManager(ConfigInterface configService) {
        this.configManager = configService;
    }

    protected ILangManager getLangManager() {
        return langManager;
    }
    public void setLangManager(ILangManager langManager) {
        this.langManager = langManager;
    }

    protected ICategoryManager getCategoryManager() {
        return categoryManager;
    }
    public void setCategoryManager(ICategoryManager categoryManager) {
        this.categoryManager = categoryManager;
    }
    
    protected ITenantManager getTenantManager() {
        return tenantManager;
    }
    public void setTenantManager(ITenantManager tenantManager) {
        this.tenantManager = tenantManager;
    }
    
}
