/*
 * Copyright 2002-2017 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.rodrigovelaz.springcloudconfigserver.db.business;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.rodrigovelaz.springcloudconfigserver.db.persistence.entity.SpringCloudConfig;
import org.rodrigovelaz.springcloudconfigserver.db.persistence.repository.SpringCloudConfigRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.config.environment.Environment;
import org.springframework.cloud.config.environment.PropertySource;
import org.springframework.cloud.config.server.environment.EnvironmentRepository;
import org.springframework.cloud.config.server.environment.SearchPathLocator;

/**
 * 
 * Db Environment Repository with JDBC 
 * 
 * @author Rodrigo Velaz
 *
 */
public class DbEnvironmentRepository implements EnvironmentRepository, SearchPathLocator {
	
	private static final String DEFAULT_LABEL = "master";
	
	private Logger logger = LoggerFactory.getLogger(DbEnvironmentRepository.class);
	
	private DataSource dataSource;
	private SpringCloudConfigRepository springCloudConfigRepository;

	public DataSource getDataSource() { return dataSource; }
	public void setDataSource(DataSource dataSource) { this.dataSource = dataSource; }
	public SpringCloudConfigRepository getSpringCloudConfigRepository() { return springCloudConfigRepository; }
	public void setSpringCloudConfigRepository(SpringCloudConfigRepository springCloudConfigRepository) { this.springCloudConfigRepository = springCloudConfigRepository; }

	private String getTableName(String application, String profile, String label) {

		String tableName = application + "_" + profile;
		tableName = tableName.replaceAll("-", "_").toLowerCase();
		return tableName;
	}
	
	private String getLocation() {
		
		try {
			return this.dataSource.getConnection().getMetaData().getURL();
		}
		catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}
	
	@Override
	public Locations getLocations(String application, String profile, String label) {
		
			if (label == null) label = DEFAULT_LABEL;
		
			String[] locations = new String[1]; 
			locations[0] = this.getLocation();
			return new Locations(application, profile, label, null, locations);
	}

	@Override
	public Environment findOne(String application, String profile, String label) {

		if (label == null) label = DEFAULT_LABEL;
		
		String tableName = this.getTableName(application, profile, label);
		
		Map<String, String> values = this.getProperties(tableName);
		
		PropertySource propertySource = new PropertySource(tableName, values);
		
		String[] profiles = new String[1];
		profiles[0] = profile;
		
		Environment env = new Environment(this.getLocation(), profiles, label, null, null);
		env.add(propertySource);
		
		return env;
	}
	
	private Map<String, String> getProperties(String tableName) {
	
		Map<String, String> properties = new HashMap<String, String>();
		
		try {
		
			List<SpringCloudConfig> springCloudConfigs = this.springCloudConfigRepository.findByTableName(tableName);
	
			for (SpringCloudConfig springCloudConfig : springCloudConfigs) {
				properties.put(springCloudConfig.getProperty(), springCloudConfig.getValue());
			}
		
		}
		catch(Exception e) {
			logger.error("Error getting properties", e);
		}
		
		return properties;
		
	}

}