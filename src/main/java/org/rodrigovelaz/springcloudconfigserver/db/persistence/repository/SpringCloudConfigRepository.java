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

package org.rodrigovelaz.springcloudconfigserver.db.persistence.repository;

import java.sql.SQLException;
import java.util.List;

import org.rodrigovelaz.springcloudconfigserver.db.persistence.entity.SpringCloudConfig;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

/**
 * 
 * Repository of SpringCloudConfig
 * 
 * @author Rodrigo Velaz
 *
 */
public class SpringCloudConfigRepository {

    private JdbcTemplate jdbcTemplate;
    private RowMapper<SpringCloudConfig> rowMapper = new BeanPropertyRowMapper<SpringCloudConfig>(SpringCloudConfig.class);

    public JdbcTemplate getJdbcTemplate() {
		return jdbcTemplate;
	}

	public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}

	/**
	 * 
	 * Find SpringCloudConfig by tableName
	 * 
	 * @param tableName Table name to use
	 * @return List of SpringCloudConfig
	 * @throws SQLException
	 */
    public List<SpringCloudConfig> findByTableName(String tableName) throws SQLException {
    	String query = "SELECT id, property, value FROM \"" + tableName.toUpperCase() + "\"";
    	List<SpringCloudConfig> result = this.jdbcTemplate.query(query, rowMapper);
    	return result;
    }

}