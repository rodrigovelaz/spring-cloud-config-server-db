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

import javax.annotation.PostConstruct;
import javax.persistence.EntityManager;
import javax.sql.DataSource;

import org.rodrigovelaz.springcloudconfigserver.db.persistence.entity.SpringCloudConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

/**
 * 
 * Repository of SpringCloudConfig
 * 
 * @author Rodrigo Velaz
 *
 */
@Repository
public class SpringCloudConfigRepositoryImpl implements SpringCloudConfigRepository {

	@Autowired
    private DataSource dataSource;
	
	@Autowired
	private EntityManager entityManager;
	
	private String query;
	
	public EntityManager getEntityManager() { return entityManager; }
	public void setEntityManager(EntityManager entityManager) { this.entityManager = entityManager; }
	
	@PostConstruct
	private void buildQuery() throws SQLException {
		
		String quote = this.getQuote();
		
		StringBuilder sb = new StringBuilder();
		sb.append("SELECT ");
		sb.append(quote);
		sb.append("id");
		sb.append(quote);
		sb.append(", ");
		sb.append(quote);
		sb.append("property");
		sb.append(quote);
		sb.append(", ");
		sb.append(quote);
		sb.append("value");
		sb.append(quote);
		sb.append("FROM ");
		sb.append(quote);
		sb.append("%s");
		sb.append(quote);
		
		this.query = sb.toString();
	}
	
	private String getQuote() throws SQLException {
		
		String url = this.dataSource.getConnection().getMetaData().getURL();
		
		if (url.toLowerCase().contains("mysql")) {
			return "`";
		}
		else {
			return "\"";	
		}
			 
	}

	/**
	 * 
	 * Find SpringCloudConfig by tableName
	 * 
	 * @param tableName Table name to use
	 * @return List of SpringCloudConfig
	 * @throws SQLException
	 */
	@Override
	public List<SpringCloudConfig> findByTableName(String tableName) throws SQLException {
		
		String queryFormat = String.format(this.query, tableName);
		
		@SuppressWarnings("unchecked")
		List<SpringCloudConfig> list = entityManager.createNativeQuery(queryFormat, SpringCloudConfig.class).getResultList();
		
	    return list;
		
	}

}