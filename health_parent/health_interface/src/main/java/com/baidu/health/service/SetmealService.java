package com.baidu.health.service;

import com.baidu.health.entity.PageResult;
import com.baidu.health.entity.QueryPageBean;
import com.baidu.health.exceptions.BusinessException;
import com.baidu.health.pojo.Setmeal;

import java.util.List;
import java.util.Map;

public interface SetmealService {
    /**
     * 添加检查组套餐
     * @param setmeal
     * @param checkgroupIds
     */
    Integer add(Setmeal setmeal, Integer[] checkgroupIds)  throws BusinessException;

    /**
     * 套餐分页查询
     * @param queryPageBean
     * @return
     */
    PageResult<Setmeal> findPage(QueryPageBean queryPageBean);

    /**
     * 根据id查询套餐 回显数据
     * @param setmealLzy
     * @return
     */
    Setmeal findById(Setmeal setmealLzy);

    /**
     * 根据id查询被选中的id集合
     * @param id
     * @return
     */
    List<Integer> findCheckgroupIdsBySetmealId(int id);

    /**
     * 修改套餐信息
     * @param setmeal
     * @param checkgroupIds
     */
    void update(Setmeal setmeal, Integer[] checkgroupIds)  throws BusinessException;

    /**
     * 根据id删除套餐信息
     * @param id
     */
    void deleteById(int id)  throws BusinessException;
    /**
     * 查出数据库中的所有图片
     * @return
     */
    List<String> findImgs();

    /**
     * 查询所有套餐
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

    Setmeal findDetailById3(int id);

    /**
     * 统计每个套餐的预约数
     * @return
     */
    List<Map<String, Object>> getSetmealReport();
}
