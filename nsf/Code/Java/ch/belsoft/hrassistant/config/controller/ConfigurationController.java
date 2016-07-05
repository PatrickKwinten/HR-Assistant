package ch.belsoft.hrassistant.config.controller;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import ch.belsoft.hrassistant.config.dao.ConfigurationDAO;
import ch.belsoft.hrassistant.config.model.ConfigDefault;
import ch.belsoft.hrassistant.config.model.ConfigType;
import ch.belsoft.hrassistant.config.model.IConfiguration;
import ch.belsoft.hrassistant.controller.ControllerBase;
import ch.belsoft.hrassistant.controller.IGuiController;
import ch.belsoft.tools.Logging;

public class ConfigurationController extends ControllerBase implements
IGuiController<IConfiguration>, Serializable {
    
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private static final String PAGETITLE_NEW = "New Configuration";
    private static final String PAGETITLE_EXISTING = "Configuration {NAME}: {DESCRIPTION}";
    private static final String TITLE_CONFIGURATIONLIST_ALL = "All configuration items";
    private static final String TITLE_CONFIGURATIONLIST_TYPE = "Other configuration items of type {NAME}";
    
    private ConfigDefault config = null;
    private ConfigurationDAO configurationDAO = new ConfigurationDAO();
    
    List<ConfigDefault> configurations = new ArrayList<ConfigDefault>();
    
    private String configTypeFilter = "";
    
    public ConfigurationController() {
        
    }
    
    public ConfigurationDAO getConfigurationDAO() {
        return configurationDAO;
    }
    
    public void setConfigurationDAO(ConfigurationDAO configurationDAO) {
        this.configurationDAO = configurationDAO;
    }
    
    public String getConfigurationListTitle() {
        String result = "";
        try {
            if (this.config.getType() == null
                    || this.config.getType().equals("")) {
                result = TITLE_CONFIGURATIONLIST_ALL
                + this.config.getTypeString();
            } else {
                result = TITLE_CONFIGURATIONLIST_TYPE.replace(
                        PAGETITLE_REPLACE_NAME, this.config.getType()
                        .toString());
            }
        } catch (Exception e) {
            Logging.logError(e);
        }
        return result;
    }
    
    public String[] getConfigTypeSelection() {
        return ConfigType.types();
    }
    
    public ConfigDefault getDataContext() {
        try {
            if (this.config == null) {
                String id = this.getId();
                if (!id.equals("")) {
                    this.config = configurationDAO.read(id);
                } else {
                    newDataItem = true;
                    this.config = new ConfigDefault();
                    
                }
            }
        } catch (Exception e) {
            Logging.logError(e);
        }
        return this.config;
    }
    
    public void clearDataItemList() {
        this.configurations = new ArrayList<ConfigDefault>();
    }
    
    public int getSearchResultCount() {
        return super.getListCount(this.configurations);
    }
    
    public List<ConfigDefault> getConfigurations() {
        
        try {
            if (this.configurations.size() == 0) {
                if (this.searchQuery.equals("")) {
                    this.configurations = this.configurationDAO.read();
                    
                } else {
                    this.configurations = this.configurationDAO
                    .search(this.searchQuery);
                }
            }
        } catch (Exception e) {
            Logging.logError(e);
        }
        return this.configurations;
    }
    
    public void removeFromList(ConfigDefault config) {
        try {
            this.remove(config);
            this.configurations.remove(config);
        } catch (Exception e) {
            Logging.logError(e);
        }
    }
    
    public void remove(ConfigDefault config) {
        try {
            this.configurationDAO.delete(config);
            this.clearDataItemList();
        } catch (Exception e) {
            Logging.logError(e);
        }
    }
    
    public void remove() {
        try {
            this.remove(this.config);
            
        } catch (Exception e) {
            Logging.logError(e);
        }
    }
    
    public void update() {
        try {
            if (this.newDataItem) {
                this.configurationDAO.create(config);
            } else {
                this.configurationDAO.update(config);
                this.config = configurationDAO.read(config.getId());
            }
            this.clearDataItemList();
        } catch (Exception e) {
            handleException(e);
            Logging.logError(e);
        }
    }
    
    public String getConfigTypeFilter() {
        return configTypeFilter;
    }
    
    public void setConfigTypeFilter(String configTypeFilter) {
        this.configTypeFilter = configTypeFilter;
    }
    
    public String getPageTitle() {
        String pageTitle = "";
        try {
            if (this.newDataItem) {
                pageTitle = PAGETITLE_NEW;
            } else {
                pageTitle = PAGETITLE_EXISTING.replace(PAGETITLE_REPLACE_NAME,
                        this.config.getType().toString());
                pageTitle = pageTitle.replace(PAGETITLE_REPLACE_DESCRIPTION,
                        this.config.getConfigKey());
            }
        } catch (Exception e) {
            Logging.logError(e);
        }
        return pageTitle;
    }
    
    public List<String> getJobIndustrySelection() {
        return applicationController
        .getConfigSelection(ConfigType.JOB_INDUSTRY);
    }
    
}
