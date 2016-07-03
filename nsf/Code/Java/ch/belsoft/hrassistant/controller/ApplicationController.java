package ch.belsoft.hrassistant.controller;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Vector;

import javax.faces.context.FacesContext;

import ch.belsoft.hrassistant.config.controller.ConfigurationController;
import ch.belsoft.hrassistant.config.model.ConfigDefault;
import ch.belsoft.hrassistant.config.model.ConfigType;
import ch.belsoft.hrassistant.config.model.IConfiguration;
import ch.belsoft.tools.Logging;
import ch.belsoft.tools.Util;
import ch.belsoft.tools.XPagesUtil;

public class ApplicationController implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static final String REALHOMEPAGE = "dashboard.xsp";

	private LinkedHashMap<ConfigType, List<String>> configSelections = new LinkedHashMap<ConfigType, List<String>>();
	private LinkedHashMap<ConfigType, LinkedHashMap<String, IConfiguration>> configMap = new LinkedHashMap<ConfigType, LinkedHashMap<String, IConfiguration>>();

	private ConfigurationController configurationController = null;

	public ApplicationController() {
		initConfiguration();
	}

	/**
	 * getting the XSP Contect and redirect
	 */
	public void redirectToRealHomePage() {
		try {
			XPagesUtil.getXSPContext().redirectToPage(REALHOMEPAGE);
		} catch (Exception e) {
			Logging.logError(e);
		}
	}

	private void initConfiguration() {
		try {

			this.configurationController = (ConfigurationController) XPagesUtil
					.getViewScope().get("configurationController");

			if (this.configurationController == null) {
				FacesContext context = FacesContext.getCurrentInstance();
				this.configurationController = (ConfigurationController) context
						.getApplication().createValueBinding(
								"#{configurationController}").getValue(context);
			}
			
			for (IConfiguration config : this.configurationController
					.getConfigurations()) {
				this.addConfig(config);
			}

		} catch (Exception e) {
			Logging.logError(e);
		}
	}

	public IConfiguration getConfig(ConfigType configType, String sKey) {
		IConfiguration result = null;

		try {

			if (!configMap.containsKey(configType)) {
				initConfiguration();
				Util.logEvent("Configuration with type: " + configType
						+ " not found..");
			} else {
				LinkedHashMap<String, IConfiguration> configByKeys = configMap
						.get(configType);
				if (!configByKeys.containsKey(sKey)) {
					Util.logEvent("Configuration in type " + configType
							+ " with key :" + sKey + " not found");
				} else {
					result = configByKeys.get(sKey);
					/*
					 * Util.logEvent("getConfig, value:  " + sType +
					 * " with key :" + sKey + " :::" + result + ", value:" +
					 * result.getValue());
					 */
				}
			}
		} catch (Exception e) {
			Logging.logError(e);
		}

		return result;
	}

	public String getConfigValue(ConfigType configType, String sKey) {
		String result = "";
		try {
			// Util.logEvent("inside getConfigValue: " + sType + ", key: " +
			// sKey);

			if (sKey != null && !sKey.equals("")) {
				IConfiguration config = getConfig(configType, sKey);

				if (config != null) {
					result = config.getConfigValue();
				} else {
					result = sKey;
				}
			}

		} catch (Exception e) {
			Logging.logError(e);
		}
		return result;
	}

	public List<IConfiguration> getConfigItems(String type) {
		List<IConfiguration> result = new ArrayList<IConfiguration>();

		try {

			ConfigType configType = ConfigType.valueOf(type);
			result = getConfigItems(configType);

		} catch (Exception e) {
			Logging.logError(e);
		}

		return result;
	}

	public List<IConfiguration> getConfigItems(ConfigType configType) {
		List<IConfiguration> result = new ArrayList<IConfiguration>();

		try {

			if (!configMap.containsKey(configType)) {
				initConfiguration();
			}

			result = new ArrayList<IConfiguration>(configMap.get(configType)
					.values());

		} catch (Exception e) {
			Logging.logError(e);
		}

		return result;
	}

	public List<String> getConfigSelection(ConfigType configType) {

		List<String> vResult = new Vector<String>();

		try {

			if (!configSelections.containsKey(configType)) {
				initConfiguration();
			}

			vResult = configSelections.get(configType);

		} catch (Exception e) {
			Logging.logError(e);
		}

		return vResult;
	}

	private void addConfig(IConfiguration configItem) {
		try {

			// switch (configType) {
			// case CONNECTIONS_CREDENTIALS:
			// configItem = new ConfigCredentials(configType, key, value);
			// ((ConfigCredentials) configItem).setPassword(arrParams[0]);
			// break;
			// case BLOGPOST_TEMPLATE:
			// configItem = new ConfigBlogPostTemplate(configType, key,
			// value);
			// ((ConfigBlogPostTemplate)
			// configItem).setContent(arrParams[0]);
			// break;
			// default:
			// configItem = new ConfigDefault(configType, key, value);
			// break;
			// }

			LinkedHashMap<String, IConfiguration> mapConfig = null;
			List<String> vConfigSelection = null;

			if (configMap.containsKey(configItem.getType())) {
				mapConfig = configMap.get(configItem.getType());
			} else {
				mapConfig = new LinkedHashMap<String, IConfiguration>();
				configMap.put(configItem.getType(), mapConfig);
			}

			if (configSelections.containsKey(configItem.getType())) {
				vConfigSelection = configSelections.get(configItem.getType());
			} else {
				vConfigSelection = new Vector<String>();
				configSelections.put(configItem.getType(), vConfigSelection);
			}

			StringBuilder configSelection = new StringBuilder(configItem
					.getConfigValue());
			configSelection.append("|");
			configSelection.append(configItem.getConfigKey());

			vConfigSelection.add(configSelection.toString());

			mapConfig.put(configItem.getConfigKey(), configItem);
		} catch (Exception e) {
			Logging.logError(e);
		}
	}

	public ConfigurationController getConfigurationController() {
		return configurationController;
	}

	public void setConfigurationController(
			ConfigurationController configurationController) {
		this.configurationController = configurationController;
	}

	/*
	 * private void addConfig(ViewEntry entry) { try { String type = (String)
	 * entry.getColumnValues().elementAt(0); String key = (String)
	 * entry.getColumnValues().elementAt(2); String value = (String)
	 * entry.getColumnValues().elementAt(3); String params = (String)
	 * entry.getColumnValues().elementAt(5);
	 * 
	 * addConfig(type, key, value, params);
	 * 
	 * } catch (Exception e) { Util.logError(e); } }
	 */
}
