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