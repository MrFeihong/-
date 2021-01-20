package com.itheima.health.dao;

import com.github.pagehelper.Page;
import com.itheima.health.pojo.CheckGroup;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface CheckGroupDao {

    /**
     * 查询所有
     * @return
     */
    List<CheckGroup> findAll();

    /***
     * 添加
     * @param checkGroup
     */
    void add(CheckGroup checkGroup);

    /**
     * 添加检查组与检查项的关系
     * @param
     * @param checkitemId
     */
    void addCheckGroupCheckItem(@Param("checkgroupId") Integer checkgroupId, @Param("checkitemId") Integer checkitemId);

    /**
     * 分页查询
     * @param queryString
     * @return
     */
    Page<CheckGroup> findByCondition(String queryString);

    /**
     * 根据id查询
     * @param id
     * @return
     */
    CheckGroup findById(Integer id);

    /**
     * 根据id查询检查组中的检查项
     * @param id
     * @return
     */
    List<Integer> findCheckItemIdsByCheckGroupId(Integer id);


    /**
     * 修改
     * @param checkgroup
     */
    void update(CheckGroup checkgroup);

    /**
     * 删除旧关系
     * @param id
     */
    void deleteCheckGroupCheckItem(Integer id);


    /**
     *查询关系
     * @param id
     * @return
     */
    int findCountByCheckGroupId(Integer id);

    /**
     * 根据id删除
     * @param id
     */
    void deleteById(Integer id);

}
