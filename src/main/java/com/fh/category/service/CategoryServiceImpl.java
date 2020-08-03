package com.fh.category.service;

import com.fh.category.mapper.CategoryMapper;

import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class CategoryServiceImpl implements CategoryService {

    @Resource
    private CategoryMapper categoryMapper;

    //查询
    public List<Map<String, Object>> queryList() {


        //查询所有的节点
        List<Map<String, Object>> allList=  categoryMapper.queryList();

        //父节点//
        List<Map<String, Object>> parentList=  new ArrayList<Map<String, Object>>();


        for (Map<String, Object> map : allList) {
            if (map.get("pid").equals(0)){
                //所有父节点
                parentList.add(map);
            }
        }
        //调用子节点方法
        selectChildren(parentList,allList);
        return parentList ;
    }



    //查詢子节点
    public void  selectChildren(List<Map<String, Object>> parentList,List<Map<String, Object>> allList){
        for (Map<String, Object> pmap : parentList) {

            //子节点的集合
            List<Map<String, Object>> childrenList=  new ArrayList<Map<String, Object>>();
            for (Map<String, Object> amap : allList) {
                if (pmap.get("id").equals(amap.get("pid"))){
                    //子节点下面还有子子节点  所以子节点成父节点
                    childrenList.add(amap);
                }
            }

            //子节点长度不为空 则放入父节点   "children"前端对应
            if(childrenList!=null && childrenList.size()>0){
                pmap.put("children",childrenList);

                //自己调用自己  寻找子节点中的子节点
                selectChildren(childrenList,allList);
            }
        }
    }



}
