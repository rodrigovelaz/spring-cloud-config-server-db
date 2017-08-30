package org.rodrigovelaz.springcloudconfigserver.db.business;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
	
	private SpringCloudConfigRepository springCloudConfigRepository;
	
	public SpringCloudConfigRepository getSpringCloudConfigRepository() {
		return springCloudConfigRepository;
	}

	public void setSpringCloudConfigRepository(SpringCloudConfigRepository springCloudConfigRepository) {
		this.springCloudConfigRepository = springCloudConfigRepository;
	}
	
	private String getTableName(String application, String profile, String label) {
		return application + "-" + profile;
	}
	
	private String getLocation() {
		
		try {
			return this.springCloudConfigRepository.getJdbcTemplate().getDataSource().getConnection().getMetaData().getURL();
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