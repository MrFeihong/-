package com.itheima.health.service.impl;

import com.alibaba.druid.util.StringUtils;
import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.itheima.health.dao.CheckGroupDao;
import com.itheima.health.entity.PageResult;
import com.itheima.health.entity.QueryPageBean;
import com.itheima.health.exception.MyException;
import com.itheima.health.pojo.CheckGroup;
import com.itheima.health.service.CheckGroupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Service(interfaceClass = CheckGroupService.class)
public class CheckGroupServiceImpl implements CheckGroupService {

    @Autowired
    private CheckGroupDao checkGroupDao;

    /**
     * 查询所有检查组
     * @return
     */
    @Override
    public List<CheckGroup> findAll() {
        return checkGroupDao.findAll();
    }

    /**
     * 添加检查组
     * @param checkGroup
     * @param checkitemIds
     */
    @Override
    @Transactional
    public void add(CheckGroup checkGroup, Integer[] checkitemIds) {
        String code = checkGroup.getCode();
        if(StringUtils.isEmpty(code)){
            throw new MyException("编码不能为空");
        }
        if(code.length() > 32){
            throw new MyException("编码的长度不能超过32位");
        }
        // 调用DAO添加检查组
        checkGroupDao.add(checkGroup);
        // 获取检查组的id
        Integer checkGroupId = checkGroup.getId();
        // 遍历检查项id, 添加检查组与检查项的关系
        if(null != checkitemIds){
            // 钩选
            for (Integer checkitemId : checkitemIds) {
                //添加检查组与检查项的关系
                checkGroupDao.addCheckGroupCheckItem(checkGroupId, checkitemId);
            }
        }
    }



    /**
     *分页查询
     * @param queryPageBean
     * @return
     */
    @Override
    public PageResult<CheckGroup> findPage(QueryPageBean queryPageBean) {
        // 获取页码和每页大小
        PageHelper.startPage(queryPageBean.getCurrentPage(),queryPageBean.getPageSize());
        // 条件查询
        if (!StringUtils.isEmpty(queryPageBean.getQueryString())){
            // 模糊查询
            queryPageBean.setQueryString("%" + queryPageBean.getQueryString() + "%");
        }
        // 分页查询语句
        Page<CheckGroup> page = checkGroupDao.findByCondition(queryPageBean.getQueryString());
        // 分页结果封装到对象中
        return new PageResult<CheckGroup>(page.getTotal(),page.getResult());
    }


    /**
     * 根据id查询
     * @param id
     * @return
     */
    @Override
    public CheckGroup findById(Integer id) {
        return checkGroupDao.findById(id);
    }



    /**
     * 根据id查询检查组中的检查项
     * @param id
     * @return
     */
    @Override
    public List<Integer> findCheckItemIdsByCheckGroupId(Integer id) {
        return checkGroupDao.findCheckItemIdsByCheckGroupId(id);
    }
    /**
     * 修改
     * @param checkgroup
     * @param checkitemIds
     */
    @Override
    @Transactional
    public void update(CheckGroup checkgroup, Integer[] checkitemIds) {
        // 调用dao修改
        checkGroupDao.update(checkgroup);
        // 删除旧关系
        checkGroupDao.deleteCheckGroupCheckItem(checkgroup.getId());
        // 遍历数组
        if(null != checkitemIds){
            // 添加新关系
            for (Integer checkitemId : checkitemIds) {
                checkGroupDao.addCheckGroupCheckItem(checkgroup.getId(), checkitemId);
            }
        }
    }


    /**
     * 根据id删除
     * @param id
     */
    @Override
    @Transactional
    public void deleteById(Integer id) {
        // 查询检查组是否被检查项使用
        Integer count = checkGroupDao.findCountByCheckGroupId(id);
        // 如果使用，报异常
        if (count > 0){
            throw new MyException("该检查组被使用了，不能删除!");
        }
        checkGroupDao.deleteCheckGroupCheckItem(id);
        // 根据id删除
        checkGroupDao.deleteById(id);
    }


}
