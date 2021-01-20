package com.baidu.health.dao;

import com.baidu.health.pojo.CheckGroup;
import com.github.pagehelper.Page;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface CheckGroupDao {
    /**
     * 查询所有检查组方法
     * @return
     */
    List<CheckGroup> findAll();

//    查询
    /**
     * 检查项的分页查询
     */
    Page<CheckGroup> findByCondition(String queryString);

//   添加
    /**
     * 添加检查组
     * @param checkGroup
     */
    void add(CheckGroup checkGroup);

    /**
     * 根据name查询检查组 判断添加或者修改时是否有重复添加
     * @param checkGroup
     * @return
     */
    CheckGroup findByName(CheckGroup checkGroup);

    /**
     * 根据coed查询检查组 判断添加或者修改时是否有重复添加
     * @param checkGroup
     * @return
     */
    CheckGroup findByCoed(CheckGroup checkGroup);

//    修改
    /**
     * 根据id查询检查组 回显数据
     * @param lzyCheckGroup
     * @return
     */
    CheckGroup findById(CheckGroup lzyCheckGroup);

    /**
     * 通过检查组id查询选中的检查项id 回显数据
     * @param id
     * @return
     */
    List<Integer> findCheckItemIdsByCheckGroupId(int id);

    /**
     * 先更新检查组
     * @param checkgroup
     */
    void update(CheckGroup checkgroup);

    /**
     * 先删除旧关系 中间表
     * @param id
     */
    void deleteCheckGroupCheckItem(Integer id);

    /**
     * 添加检查组与检查项的关系
     * @param checkgroupId 注意要取别名，类型相同 @Param注解
     * @param checkitemId
     */
    void addCheckGroupCheckItem(@Param("checkgroupId") Integer checkgroupId, @Param("checkitemId") Integer checkitemId);

//   删除
    /**
     * 删除时查询是否被套餐使用
     * @param id
     * @return
     */
    int findSetmealCountByCheckGroupId(int id);


    /**
     * 删除检查组
     * @param id
     */
    void deleteById(int id);

    /**
     * 根据 id name coed 查询检查组
     * @param checkGroup
     * @return
     */
    CheckGroup findByIdOrNameOrCoed(CheckGroup checkGroup);
}
