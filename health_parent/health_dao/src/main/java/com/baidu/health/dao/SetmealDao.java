package com.baidu.health.dao;


import com.baidu.health.pojo.CheckGroup;
import com.baidu.health.pojo.CheckItem;
import com.baidu.health.pojo.Setmeal;
import com.github.pagehelper.Page;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface SetmealDao {
    /**
     *添加套餐信息 并且返回id
     * @param setmeal
     */
    void add(Setmeal setmeal);

    /**
     * 添加套餐与检查项关系
     * @param setmealId
     * @param checkgroupId
     */
    void addSetmealCheckGroup(@Param("setmealId") Integer setmealId, @Param("checkgroupId") Integer checkgroupId);

    /**
     * 根据name查询套餐 校验添加修改是否有重复
     * @param setmeal
     * @return
     */
    Setmeal findByName(Setmeal setmeal);

    /**
     * 根据编码查询套餐 校验添加修改是否有重复
     * @param setmeal
     * @return
     */
    Setmeal findByCode(Setmeal setmeal);

    /**
     * 套餐条件查询
     * @param queryString
     * @return
     */
    Page<Setmeal> findByCondition(String queryString);

    /**
     * 根据id查询套餐 回显数据
     * @param setmealLzy
     * @return
     */
    Setmeal findById(Setmeal setmealLzy);

    /**
     * 根据套餐id 查询检查组集合 回显数据
     * @param id
     * @return
     */
    List<Integer> findCheckgroupIdsBySetmealId(int id);

    /**
     * 修改套餐信息
     * @param setmeal
     */
    void update(Setmeal setmeal);

    /**
     * 删除套餐与检查组的旧关系
     * @param id
     */
    void deleteSetmealCheckGroup(Integer id);

    /**
     * 查询该套餐是否被订单使用
     * @param id
     * @return
     */
    int findOrderCountBySetmealId(int id);

    /**
     * 根据id删除套餐
     * @param id
     */
    void deleteById(int id);

    /**
     * 查数据中套餐的所有图片
     * @return
     */
    List<String> findImgs();

    /**
     * 查询所有的套餐
     * @return
     */
    List<Setmeal> findAll();

    /**
     * 查询套餐详情
     * @param id
     * @return
     */
    Setmeal findDetailById(int id);

    Setmeal findDetailById2(int id);

    List<CheckGroup> findCheckGroupListBySetmealId(int id);

    List<CheckItem> findCheckItemByCheckGroupId(Integer id);

    /**
     * 统计每个套餐的预约数
     * @return
     */
    List<Map<String, Object>> getSetmealReport();
}
