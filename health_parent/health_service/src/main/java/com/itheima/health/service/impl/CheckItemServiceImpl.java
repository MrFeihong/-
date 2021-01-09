package com.itheima.health.service.impl;

import com.alibaba.dubbo.common.utils.StringUtils;
import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.itheima.health.dao.CheckItemDao;
import com.itheima.health.entity.PageResult;
import com.itheima.health.entity.QueryPageBean;
import com.itheima.health.exception.MyException;
import com.itheima.health.pojo.CheckItem;
import com.itheima.health.service.CheckItemService;
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
     * 添加
     * @param checkItem
     */
    @Override
    public void add(CheckItem checkItem) {
        checkItemDao.add(checkItem);
    }


    /**
     * 分页查询
     * @param queryPageBean
     * @return
     */
    @Override
    public PageResult<CheckItem> findPage(QueryPageBean queryPageBean) {

        PageHelper.startPage(queryPageBean.getCurrentPage(), queryPageBean.getPageSize());
        // 条件查询
        if(!StringUtils.isEmpty(queryPageBean.getQueryString())){
            // 模糊查询
            queryPageBean.setQueryString("%" + queryPageBean.getQueryString() + "%");
        }
        // 分页查询语句
        Page<CheckItem> page = checkItemDao.findByCondition(queryPageBean.getQueryString());
        // 分页结果封装到对象中
        PageResult<CheckItem> pageResult = new PageResult<CheckItem>(page.getTotal(),page.getResult());
        return pageResult;
    }


    /**
     * 根据id查询
     * @param id
     * @return
     */
    @Override
    public CheckItem findById(int id) {
        return checkItemDao.findById(id);
    }
    /**
     * 修改
     * @param checkItem
     */
    @Override
    public void update(CheckItem checkItem) {
        checkItemDao.update(checkItem);
    }


    /**
     * 通过id删除
     * @param id
     * @throws MyException
     */
    @Override
    public void deleteById(int id){
        //  统计使用了这个id的个数
        int count = checkItemDao.findCountByCheckItemId(id);
        if(count > 0){
            throw new MyException("该检查项被使用了，不能删除!");
        }
        // 删除
        checkItemDao.deleteById(id);
    }
}
