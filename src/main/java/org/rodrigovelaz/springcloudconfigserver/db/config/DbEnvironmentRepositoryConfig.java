package org.rodrigovelaz.springcloudconfigserver.db.config;

import org.rodrigovelaz.springcloudconfigserver.db.business.DbEnvironmentRepository;
import org.rodrigovelaz.springcloudconfigserver.db.persistence.repository.SpringCloudConfigRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.cloud.config.server.environment.EnvironmentRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.jdbc.core.JdbcTemplate;


/**
 * 
 * Db Enviroment Repository configuration
 * 
 * @author Rodrigo Velaz
 * @see JdbcTemplate
 *
 */
@Configuration
@Profile("db")
@ConditionalOnMissingBean(EnvironmentRepository.class)
public class DbEnvironmentRepositoryConfig {

	@Autowired
    private JdbcTemplate jdbcTemplate;
	
	@Bean
	public DbEnvironmentRepository dbEnvironmentRepository() {
		
		SpringCloudConfigRepository repository = new SpringCloudConfigRepository();
		repository.setJdbcTemplate(this.jdbcTemplate);
		
		DbEnvironmentRepository dbEnvironmentRepository = new DbEnvironmentRepository();
		dbEnvironmentRepository.setSpringCloudConfigRepository(repository);
		return dbEnvironmentRepository;
	}
	
}