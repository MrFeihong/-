package com.baidu.health.service.impl;

import com.alibaba.dubbo.common.utils.StringUtils;
import com.alibaba.dubbo.config.annotation.Service;
import com.baidu.health.exceptions.BusinessException;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.baidu.health.dao.CheckItemDao;
import com.baidu.health.entity.PageResult;
import com.baidu.health.entity.QueryPageBean;
import com.baidu.health.pojo.CheckItem;
import com.baidu.health.service.CheckItemService;
import org.springframework.beans.factory.annotation.Autowired;


import java.util.List;

@Service(interfaceClass = CheckItemService.class)
public class CheckItemServiceImpl implements CheckItemService {

    @Autowired
    private CheckItemDao checkItemDao;

    /**
     * 查询所有检查项
     * @return
     */
    @Override
    public List<CheckItem> findAll() {
        return checkItemDao.findAll();
    }

    /**
     * 添加检查项
     * @param checkItem
     */
    @Override
    public void add(CheckItem checkItem) {
        CheckItem findByNameCheckItem = checkItemDao.findByName(checkItem);
        //CheckItem findByNameCheckItem = checkItemDao.findByIdOrNameOrcoed(checkItem);
        CheckItem findByCoedCheckItem = checkItemDao.findByCoed(checkItem);
        //CheckItem findByCoedCheckItem = checkItemDao.findByIdOrNameOrcoed(checkItem);
        if (null != findByNameCheckItem) {
            throw new BusinessException("添加的检查项项目名相同！！");
            //TODO 设置常量类的返回消息
        }
        if (null != findByCoedCheckItem) {
            //TODO 设置常量类的返回消息
            throw new BusinessException("添加的检查项目编码相同！！");
        }
        checkItemDao.add(checkItem);
    }

    /**
     * 删除检查项
     * @param id
     */
    @Override
    public void deleteById(int id) {
        //先判断这个检查项是否被检查组使用了
        //调用dao查询检查项的id是否在t_checkgroup_checkitem表中存在记录
        int countByCheckItemId = checkItemDao.CountByCheckItemId(id);
        //被使用了则不能删除
        if (countByCheckItemId > 0) {
            //建议自定义异常类
            //TODO 设置常量类的返回消息
            throw new BusinessException("删除检查项失败");
        }
        //没使用就可以调用dao删除
        checkItemDao.deleteById(id);
    }

    /**
     * 根据id查询检查项回显
     * @param lzyCheckItem
     * @return
     */
    @Override
    public CheckItem findById(CheckItem lzyCheckItem) {
        return checkItemDao.findById(lzyCheckItem);
    }

    /**
     * 修改检查项
     * @param checkItem
     */
    @Override
    public void update(CheckItem checkItem) {
        CheckItem findByNameCheckItem = checkItemDao.findByName(checkItem);
        CheckItem findByCoedCheckItem = checkItemDao.findByCoed(checkItem);
        //判断为空说明没有相同的
        if (null != findByNameCheckItem){
            //不为空说明是相同的 传入的项目名在数据库中有
            //判断id是否相同 相同说明是自己的项目名
            if (findByNameCheckItem.getId() != checkItem.getId()){
                //不同说明不是自己的项目名
                throw new BusinessException("修改的检查项，项目名重复！！");
                 //TODO 设置常量类的返回消息
            }
        }
        //判断为空说明没有相同的
        if (null != findByCoedCheckItem){
            //不为空说明是相同的 传入的项目名在数据库中有
            //判断id是否相同 相同说明是自己的项目名
            if (findByCoedCheckItem.getId() != checkItem.getId()){
                //不同说明不是自己的项目名
                throw new BusinessException("修改的检查项，编码重复！！");
                //TODO 设置常量类的返回消息
            }
        }
        //执行修改
        checkItemDao.update(checkItem);
    }


    /**
     * 检查项的分页查询
     * @param queryPageBean
     * @return
     */
    @Override
    public PageResult<CheckItem> findPage(QueryPageBean queryPageBean) {
        //使用第二种，mapper接口方式调用，推荐使用方法。传入两个参数当前页数和每页条数
        // pageSize能无限大吗？使用三元运算符进行修改
        queryPageBean.setPageSize(queryPageBean.getPageSize()>50?50:queryPageBean.getPageSize());
        //传入两个数据当前页数和每页条数
        //pageHelper.startPage(1,10);
        PageHelper.startPage(queryPageBean.getCurrentPage(), queryPageBean.getPageSize());
        // 判断是否有条件查询
        if (StringUtils.isNotEmpty(queryPageBean.getQueryString())) {
            // 有查询条件， 使用模糊查询 拼接上%
            queryPageBean.setQueryString("%" + queryPageBean.getQueryString() + "%");
        }
        // page extends arrayList 返回的值
        Page<CheckItem> page = checkItemDao.findByCondition(queryPageBean.getQueryString());
        PageResult<CheckItem> pageResult = new PageResult<CheckItem>(page.getTotal(), page.getResult());
        //list<Country> list = countryMapper.selectIf(1);
        return pageResult;
    }
}

/*
 改bug
*/