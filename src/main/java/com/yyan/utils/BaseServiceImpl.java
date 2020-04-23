package com.yyan.utils;


import org.springframework.cglib.beans.BeanMap;

import java.util.*;


public class BaseServiceImpl {

    //  查询成功数据组装
    public Map<String, Object> queryListSuccess(Object data, Integer count, Map param) {
        param = checkPageSize(param); // 对分页信息处理
        Map<String, Object> map = new HashMap<>();
        map.put("rows", data);
        map.put("pageNumber", param.get("pageNumber"));
        map.put("pageSize", param.get("pageSize"));
        map.put("total", count);
        return map;
    }

    //  去掉实体中没有必要的字段
    public List<?> queryListClearField(List<?> list, String[] arr) {
        List<Map<String, Object>> newList = new ArrayList<>();
        for (Object object : list) {
            Map<String, Object> map = new HashMap<>();
            map.putAll(BeanMap.create(object));
            for (String key : arr) {
                map.remove(key);// 去掉不必要的字段
            }
            newList.add(map);
        }
        return newList;
    }


    /**
     * 批量插入数据，为数据添加 id,createTime,updateTime
     *
     * @param list
     * @return
     */
    public List<?> insertListBefore(List<?> list) {
        List<Map<String, Object>> newList = new ArrayList<>();
        for (Object object : list) {
            Map<String, Object> map = new HashMap<>();
            String id = UUID.randomUUID().toString();
            System.out.println("id" + id);
            map.put("id", id); // 添加 id
            map.put("updateTime", new Date()); // 添加创建时间
            map.put("createTime", new Date()); // 添加修改时间
            newList.add(map);
        }
        return newList;
    }


    public Map checkPageSize(Map map) {

        Integer pageNumber = map.get("pageNumber") != null ? (Integer) map.get("pageNumber") : 10;
        Integer pageSize = map.get("pageSize") != null ? (Integer) map.get("pageSize") : 0;
        map.put("pageNumber", pageNumber);
        map.put("pageSize", pageSize);
        return map;
    }


}
