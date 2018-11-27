/*
 * Copyright (c) 2018, vindell (https://github.com/vindell).
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package org.apache.shiro.spring.boot.biz;

import java.util.Map;

/**
 * 动态权限
 * https://www.cnblogs.com/007sx/p/7381475.html
 * @author 		： <a href="https://github.com/vindell">vindell</a>
 */
public class ShiroPermsRestableFilterFactoryBean extends ShiroBizFilterFactoryBean  {
	
	/*@Autowired
    private RolesPermissionsService rolesPermissionsService;
	
	 *//**
     * 初始化设置过滤链
     *//*
    @Override
    public void setFilterChainDefinitions(String definitions) {
//        String token =  manageUserService.getAdminToken(0);

        //可从数据库读取后，添加至过滤链，参考此处已注释的代码
        definition = definitions;//记录配置的静态过滤链
//        List<Permission> permissions = permissService.findAll();

        List<RolesPermissions> rolesPermissions = rolesPermissionsService.selectList(new EntityWrapper<>());
        Set<String> urls = new LinkedHashSet<>();
        for (RolesPermissions rolesPermission : rolesPermissions) {
            urls.add(rolesPermission.getUrl());
        }

        Map<String,String> otherChains = new HashMap<>();
        for (String url : urls) {
            StringBuilder roleOrFilters = new StringBuilder();
            for (int i = 0; i < rolesPermissions.size(); i++) {
                if (Objects.equals(url, rolesPermissions.get(i).getUrl())) {
                    if (i == 0) {
                        roleOrFilters.append(rolesPermissions.get(i).getRolesName());
                    }else{
                        roleOrFilters.append(",").append(rolesPermissions.get(i).getRolesName());
                    }
                }
            }
            String rolesStr = roleOrFilters.toString();
            if (!"".equals(rolesStr)) {
                otherChains.put(url, "roleOrFilter["+rolesStr+"]"); //  /discover/newstag  authc,roles[user,admin]
            }
        }
        //加载配置默认的过滤链
        Ini ini = new Ini();
        ini.load(definitions);
        Ini.Section section = ini.getSection(IniFilterChainResolverFactory.URLS);
        if (CollectionUtils.isEmpty(section)) {
            section = ini.getSection(Ini.DEFAULT_SECTION_NAME);
        }
        //加上数据库中过滤链
        section.putAll(otherChains);
        section.put("/**", "anon");
        setFilterChainDefinitionMap(section);
    }*/
	
	@Override
	public void setFilterChainDefinitionMap(Map<String, String> filterChainDefinitionMap) {
		
		
		
		super.setFilterChainDefinitionMap(filterChainDefinitionMap);
	}
	

}