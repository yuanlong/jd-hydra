/*
 * Copyright jd
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */

package com.jd.bdp.hydra.persistent.dao.impl;

import com.jd.bdp.hydra.persistent.dao.ServiceMapper;
import com.jd.bdp.hydra.persistent.entity.ServicePara;

import org.mybatis.spring.SqlSessionTemplate;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ServiceMapperImpl implements ServiceMapper {
    private SqlSessionTemplate sqlSession;

    public void setSqlSession(SqlSessionTemplate sqlSession) {
        this.sqlSession = sqlSession;
    }

    @Override
    public int addService(ServicePara servicePara) {
        sqlSession.insert("addService", servicePara);
        return servicePara.getServiced();
    }

    @Override
    public ServicePara getService(String name) {
        Map<String, Object> map = new HashMap<>();
        map.put("name", name);
        return (ServicePara) sqlSession.selectOne("getServiceByName", map);
    }

    @Override
    public List<ServicePara> getAllService() {
        return (List<ServicePara>) sqlSession.selectList("getAllService");
    }
}
