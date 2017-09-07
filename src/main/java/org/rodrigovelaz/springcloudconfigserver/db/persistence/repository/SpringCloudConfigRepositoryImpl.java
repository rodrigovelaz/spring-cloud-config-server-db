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

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.rodrigovelaz.springcloudconfigserver.db.persistence.entity.SpringCloudConfig;
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

	@PersistenceContext
	private EntityManager entityManager;
	
	public EntityManager getEntityManager() { return entityManager; }
	public void setEntityManager(EntityManager entityManager) { this.entityManager = entityManager; }

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
	     
		String query = "SELECT id, property, value FROM \"" + tableName.toUpperCase() + "\"";
		
		@SuppressWarnings("unchecked")
		List<SpringCloudConfig> list = entityManager.createNativeQuery(query, SpringCloudConfig.class).getResultList();
		
	    return list;
		
	}

/*
	public List<SpringCloudConfig> findByTableName(String tableName) throws SQLException {
		String query = "SELECT id, property, value FROM \"" + tableName.toUpperCase() + "\"";
		List<SpringCloudConfig> result = this.jdbcTemplate.query(query, rowMapper);
		return result;
	}*/

}