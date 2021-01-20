package com.itheima.health.service.impl;

import com.alibaba.dubbo.common.utils.StringUtils;
import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.itheima.health.dao.SetmealDao;
import com.itheima.health.entity.PageResult;
import com.itheima.health.entity.QueryPageBean;
import com.itheima.health.exception.MyException;
import com.itheima.health.pojo.CheckGroup;
import com.itheima.health.pojo.CheckItem;
import com.itheima.health.pojo.Setmeal;
import com.itheima.health.service.SetmealService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service(interfaceClass = SetmealService.class)
public class SetmealServiceImpl implements SetmealService {

    @Autowired
    private SetmealDao setmealDao;

    /**
     * 添加套餐
     * @param setmeal
     * @param checkgroupIds
     */
    @Override
    @Transactional
    public void add(Setmeal setmeal, Integer[] checkgroupIds) {
        // 调用业务添加
        setmealDao.add(setmeal);
        // 获取套餐Id
        Integer setmealId = setmeal.getId();
        // 遍历checkgroupIds数组
        if (null != checkgroupIds){
            for (Integer checkgroupId : checkgroupIds) {
                // 添加套餐与检查组关系
                setmealDao.addSetmealCheckGroup(setmealId,checkgroupId);
            }
        }
    }


    /**
     * 分页查询
     * @param queryPageBean
     * @return
     */
    @Override
    public PageResult<Setmeal> findPage(QueryPageBean queryPageBean) {
        // 获取页码和每页大小
        PageHelper.startPage(queryPageBean.getCurrentPage(),queryPageBean.getPageSize());
        // 条件查询
        if(!StringUtils.isEmpty(queryPageBean.getQueryString())){
            // 模糊查询
            queryPageBean.setQueryString("%"+queryPageBean.getQueryString()+"%");
        }
        // 分页查询语句
        Page<Setmeal> setmealPage = setmealDao.findByCondition(queryPageBean.getQueryString());
        // 分页结果封装到对象中
        return new PageResult<Setmeal>(setmealPage.getTotal(),setmealPage.getResult());
    }

    /**
     * 根据id查询
     * @param id
     * @return
     */
    @Override
    public Setmeal findById(int id) {
        return setmealDao.findById(id);
    }

    /**
     * 根据id查询检查组集合
     * @param id
     * @return
     */
    @Override
    public List<Integer> findCheckGroupIdsBySetmealId(int id) {

        return setmealDao.findCheckGroupIdsBySetmealId(id);
    }

    /**
     * 修改套餐
     * @param setmeal
     * @param checkgroupIds
     */
    @Override
    @Transactional
    public void update(Setmeal setmeal, Integer[] checkgroupIds) {
        // 调用dao修改
        setmealDao.update(setmeal);
        // 删除旧关系
        setmealDao.deleteSetmealCheckGroup(setmeal.getId());
        // 遍历数组
        if (null != checkgroupIds){
            // 添加新关系
            for (Integer checkgroupId : checkgroupIds) {
                setmealDao.addSetmealCheckGroup(setmeal.getId(), checkgroupId);
            }
        }
    }

    /**
     * 根据id删除
     * @param id
     */
    @Override
    @Transactional
    public void deleteById(int id){
        // 查询检查组是否被检查项使用
        int count = setmealDao.findCountBySetmealId(id);
        // 如果使用，报异常
        if (count > 0){
            // 使用就包异常
            throw new MyException("该套餐被使用了，不能删除!");
        }
        // 没使用就删除旧关系
        setmealDao.deleteSetmealCheckGroup(id);
        // 根据id删除
        setmealDao.deleteById(id);
    }

    /**
     * 查询套餐的所有图片
     * @return
     */
    @Override
    public List<String> findImgs() {
        return setmealDao.findImgs();
    }

    /**
     * 查询所有的套餐
     * @return
     */
    @Override
    public List<Setmeal> findAll() {
        return setmealDao.findAll();
    }

    /**
     * 通过id查询套餐详情
     * @param id
     * @return
     */
    @Override
    public Setmeal findDetailById(int id) {
        return setmealDao.findDetailById(id);
    }



}
