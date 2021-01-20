package com.baidu.health.service.impl;

import com.alibaba.dubbo.common.utils.StringUtils;
import com.alibaba.dubbo.config.annotation.Service;
import com.baidu.health.dao.SetmealDao;
import com.baidu.health.entity.PageResult;
import com.baidu.health.entity.QueryPageBean;
import com.baidu.health.exceptions.BusinessException;
import com.baidu.health.pojo.CheckGroup;
import com.baidu.health.pojo.CheckItem;
import com.baidu.health.pojo.Setmeal;
import com.baidu.health.service.SetmealService;
import com.baidu.health.utils.RedisUtils;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import redis.clients.jedis.Jedis;

import javax.validation.constraints.Min;
import java.util.List;
import java.util.Map;

@Validated
@Service(interfaceClass = SetmealService.class)
public class SetmealServiceImpl implements SetmealService {

    @Autowired
    private SetmealDao setmealDao;
    /**
     * 添加检查组套餐
     * @param setmeal
     * @param checkgroupIds
     */
    @Override
    @Transactional
    public Integer add(Setmeal setmeal, Integer[] checkgroupIds) {
        Setmeal findByNameSetmeal =setmealDao.findByName(setmeal);
        Setmeal findByCodeSetmeal =setmealDao.findByCode(setmeal);
        if (null != findByNameSetmeal) {
            throw new BusinessException("添加的套餐名称相同！！");
            //TODO 设置常量类的返回消息
        }
        if (null != findByCodeSetmeal) {
            //TODO 设置常量类的返回消息
            throw new BusinessException("添加的套餐编码相同！！");
        }
        //先添加套餐信息 并且返回id 给下面建立关系时使用
        setmealDao.add(setmeal);
        //添加后使用SQL语句获取 添加的套餐id
        Integer setmealId =setmeal.getId();
        //添加检查组与套餐的关系 对套餐来说 套餐是1检查组是多
        //判断用户是否勾选检查组
        if (null !=checkgroupIds){
            //不为空则遍历勾选的检查组
            for (Integer checkgroupId : checkgroupIds) {
                setmealDao.addSetmealCheckGroup(setmealId, checkgroupId);
            }
        }
        return setmealId;
    }

    /**
     * 套餐分页查询
     * @param queryPageBean
     * @return
     */

    @Override
    public PageResult<Setmeal> findPage(QueryPageBean queryPageBean) {
        queryPageBean.setPageSize(queryPageBean.getPageSize()>50?50:queryPageBean.getPageSize());
        PageHelper.startPage(queryPageBean.getCurrentPage(),queryPageBean.getPageSize());
        // 条件查询
        if(StringUtils.isNotEmpty(queryPageBean.getQueryString())){
            // 模糊查询
            queryPageBean.setQueryString("%"+queryPageBean.getQueryString()+"%");
        }

        Page<Setmeal> setmealPage = setmealDao.findByCondition(queryPageBean.getQueryString());
        return new PageResult<Setmeal>(setmealPage.getTotal(),setmealPage.getResult());
    }
    /**
     * 根据id查询套餐 回显数据
     * @param setmealLzy
     * @return
     */
    @Override
    public Setmeal findById(Setmeal setmealLzy) {
        return setmealDao.findById(setmealLzy);
    }

    /**
     * 根据id查询被选中的id集合
     * @param id
     * @return
     */
    @Override
    public List<Integer> findCheckgroupIdsBySetmealId(@Min(value = 1,message ="id不能小于1") int id) {
        return setmealDao.findCheckgroupIdsBySetmealId(id);
    }

    /**
     * 修改套餐信息
     * @param setmeal
     * @param checkgroupIds
     */
    @Override
    @Transactional
    public void update(Setmeal setmeal, Integer[] checkgroupIds) {

        Setmeal findByNameSetmeal = setmealDao.findByName(setmeal);
        Setmeal findByCoedSetmeal = setmealDao.findByCode(setmeal);
        //判断为空说明没有相同的
        if (null != findByNameSetmeal){
            //不为空说明是相同的 传入的项目名在数据库中有
            //判断id是否相同 相同说明是自己的项目名
            if (findByCoedSetmeal.getId() != setmeal.getId()){
                //不同说明不是自己的项目名
                throw new BusinessException("修改的套餐，名称重复！！");
                //TODO 设置常量类的返回消息
            }
        }
        //判断为空说明没有相同的
        if (null != findByCoedSetmeal){
            //不为空说明是相同的 传入的项目名在数据库中有
            //判断id是否相同 相同说明是自己的项目名
            if (findByCoedSetmeal.getId() != setmeal.getId()){
                //不同说明不是自己的项目名
                throw new BusinessException("修改的套餐，编码重复！！");
                //TODO 设置常量类的返回消息
            }
        }
        //执行更新套餐操作
        setmealDao.update(setmeal);
        //删除套餐与检查组的旧关
        setmealDao.deleteSetmealCheckGroup(setmeal.getId());
        //添加套餐与检查组的新关系
        if(null != checkgroupIds){
            for (Integer checkgroupId : checkgroupIds) {
                setmealDao.addSetmealCheckGroup(setmeal.getId(), checkgroupId);
            }
        }
    }

    /**
     * 根据id删除套餐信息
     * @param id
     */
    @Override
    @Transactional
    public void deleteById(int id) {
        int count = setmealDao.findOrderCountBySetmealId(id);
        if (count > 0){
            //该套餐被订单使用了，不能删除
            throw new BusinessException("该套餐被订单使用了，不能删除！");
        }
        //该套餐没有被订单使用 先删除套餐与检查组的关系
        setmealDao.deleteSetmealCheckGroup(id);
        //再删除套餐
        setmealDao.deleteById(id);
    }
    /**
     * 查出数据库中的所有图片
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
        Jedis jedis = new Jedis();
        byte[] byt = jedis.get("findAllRedis".getBytes());

        if (byt==null){
            List<Setmeal> findAlINDB = setmealDao.findAll();
            byte[] personByte = RedisUtils.serialize(findAlINDB);
            jedis.set("findAllRedis".getBytes(),personByte);
            return findAlINDB;
        }
        List<Setmeal> findAll = (List<Setmeal>) RedisUtils.unserizlize(byt);
        return findAll;
    }

    /**
     * 查询套餐详情
     * @param id
     * @return
     */
    @Override
    public Setmeal findDetailById(int id) {
        return setmealDao.findDetailById(id);
    }
    @Override
    public Setmeal findDetailById2(int id) {
        return setmealDao.findDetailById2(id);
    }

    @Override
    public Setmeal findDetailById3(int id) {
        Jedis jedis = new Jedis();
        byte[] byt = jedis.get("findDetailById3Redis".getBytes());

        if (byt==null){
            // 查询套餐信息
            Setmeal setmealLzy =new Setmeal();
            setmealLzy.setId(id);
            Setmeal setmeal = setmealDao.findById(setmealLzy);
            // 查询套餐下的检查组
            List<CheckGroup> checkGroups = setmealDao.findCheckGroupListBySetmealId(id);

            if(null != checkGroups){
                for (CheckGroup checkGroup : checkGroups) {
                    // 通过检查组id检查检查项列表
                    List<CheckItem> checkItems = setmealDao.findCheckItemByCheckGroupId(checkGroup.getId());
                    // 设置这个检查组下所拥有的检查项
                    checkGroup.setCheckItems(checkItems);
                }
                //设置套餐下的所拥有的检查组
                setmeal.setCheckGroups(checkGroups);
            }

            byte[] personByte = RedisUtils.serialize(setmeal);
            jedis.set("findDetailById3Redis".getBytes(),personByte);
            return setmeal;
        }

        Setmeal setmeal = (Setmeal) RedisUtils.unserizlize(byt);
        return setmeal;
    }

    /**
     * 统计每个套餐的预约数
     * @return
     */
    @Override
    public List<Map<String, Object>> getSetmealReport() {
        return setmealDao.getSetmealReport();
    }

}
