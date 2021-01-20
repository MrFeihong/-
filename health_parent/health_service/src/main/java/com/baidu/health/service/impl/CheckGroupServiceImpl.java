package com.baidu.health.service.impl;

import com.alibaba.dubbo.common.utils.StringUtils;
import com.alibaba.dubbo.config.annotation.Service;
import com.baidu.health.constant.MessageConstant;
import com.baidu.health.dao.CheckGroupDao;
import com.baidu.health.entity.PageResult;
import com.baidu.health.entity.QueryPageBean;
import com.baidu.health.exceptions.BusinessException;
import com.baidu.health.pojo.CheckGroup;
import com.baidu.health.service.CheckGroupService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service(interfaceClass = CheckGroupService.class)
public class CheckGroupServiceImpl implements CheckGroupService {

    @Autowired
    private CheckGroupDao checkGroupDao;

    /**
     * 查询所有检查组  该方法不能直接暴露给外界
     * @return
     */
    @Override
    public List<CheckGroup> findAll() {
        return checkGroupDao.findAll();
    }

    /**
     * 添加检查组和检查组于检查项关系
     * @param checkGroup
     * @param checkitemIds
     */
    @Override
    @Transactional
    public void add(CheckGroup checkGroup, Integer[] checkitemIds) {
        //判断添加或者修改时是否有重复添加
        CheckGroup findByNameCheckGroup= checkGroupDao.findByName(checkGroup);
        //CheckGroup findByNameCheckGroup= checkGroupDao.findByIdOrNameOrCoed(checkGroup);
        CheckGroup findByCoedCheckGroup = checkGroupDao.findByCoed(checkGroup);
        if (null != findByNameCheckGroup) {
            throw new BusinessException("添加的检查项组名称相同！！");
            //TODO 设置常量类的返回消息
        }
        if (null != findByCoedCheckGroup) {
            //TODO 设置常量类的返回消息
            throw new BusinessException("添加的检查组编码相同！！");
        }
        //添加检查组
        checkGroupDao.add(checkGroup);
        //添加完后在做查询 获取id给下面用
        Integer checkGroupId = checkGroup.getId();
        // 遍历检查项id, 添加检查组与检查项的关系（查询id绑定中间表）
        //先判断勾选是否为空
        if(null != checkitemIds){
            // 有钩选
            //遍历出所有已勾选的检查项
            for (Integer checkitemId : checkitemIds) {
                //添加检查组与检查项的关系
                //检查组的id一个而检查项的id多个
                checkGroupDao.addCheckGroupCheckItem(checkGroupId, checkitemId);
            }
        }
    }

    /**
     * 根据id查询检查组 回显数据
     * @param lzyCheckGroup
     * @return
     */
    @Override
    public CheckGroup findById(CheckGroup lzyCheckGroup) {
        return checkGroupDao.findById(lzyCheckGroup);
    }


    /**
     * 通过检查组id查询选中的检查项id
     * @param id
     * @return
     */
    @Override
    public List<Integer> findCheckItemIdsByCheckGroupId(int id) {
        return checkGroupDao.findCheckItemIdsByCheckGroupId(id);
    }



    /**
     * 修改检查组
     * @param checkgroup 检查组信息
     * @param checkitemIds 选中的检查项id数组
     */
    @Override
    @Transactional
    public void update(CheckGroup checkgroup, Integer[] checkitemIds) {

        CheckGroup findByNameCheckGroup = checkGroupDao.findByName(checkgroup);
        CheckGroup findByCoedCheckGroup = checkGroupDao.findByCoed(checkgroup);
        //判断为空说明没有相同的
        if (null != findByNameCheckGroup){
            //不为空说明是相同的 传入的项目名在数据库中有
            //判断id是否相同 相同说明是自己的项目名
            if (findByNameCheckGroup.getId() != checkgroup.getId()){
                //不同说明不是自己的项目名
                throw new BusinessException("修改的检查组，名称重复！！");
                //TODO 设置常量类的返回消息
            }
        }
        //判断为空说明没有相同的
        if (null != findByCoedCheckGroup){
            //不为空说明是相同的 传入的项目名在数据库中有
            //判断id是否相同 相同说明是自己的项目名
            if (findByCoedCheckGroup.getId() != checkgroup.getId()){
                //不同说明不是自己的项目名
                throw new BusinessException("修改的检查组，编码重复！！");
                //TODO 设置常量类的返回消息
            }
        }
        //- 先执行更新检查组
        checkGroupDao.update(checkgroup);
        //- 先在执行删除旧关系
        checkGroupDao.deleteCheckGroupCheckItem(checkgroup.getId());
        //- 遍历选中的检查项id的数组
        if(null != checkitemIds){
            for (Integer checkitemId : checkitemIds) {
                //- 添加检查组与检查项的关系
                checkGroupDao.addCheckGroupCheckItem(checkgroup.getId(), checkitemId);
            }
        }
        //- 添加事务控制
    }
    /**
     * 检查项的分页查询
     * @param queryPageBean
     * @return
     */
    @Override
    public PageResult<CheckGroup> findPage(QueryPageBean queryPageBean) {
        queryPageBean.setPageSize(queryPageBean.getPageSize()>50?50:queryPageBean.getPageSize());
        PageHelper.startPage(queryPageBean.getCurrentPage(), queryPageBean.getPageSize());
        // 条件查询
        if(StringUtils.isNotEmpty(queryPageBean.getQueryString())){
            // 有查询条件， 模糊查询
            queryPageBean.setQueryString("%" + queryPageBean.getQueryString() + "%");
        }
        // page extends arrayList
        Page<CheckGroup> list = checkGroupDao.findByCondition(queryPageBean.getQueryString());
        PageResult<CheckGroup> pageResult = new PageResult<CheckGroup>(list.getTotal(),list.getResult());
        return pageResult;
    }
    /**
     * 删除检查组
     * @param id
     */
    @Override
    @Transactional
    public void deleteById(int id){
        // 检查 这个检查组是否被套餐使用了
        int count = checkGroupDao.findSetmealCountByCheckGroupId(id);
        if(count > 0){
            // 被使用了
            throw new BusinessException(MessageConstant.QUERY_CHECKGROUP_SUCCESS);
        }
        // 没有被套餐使用,就可以删除数据
        // 先删除检查组与检查项的关系
        checkGroupDao.deleteCheckGroupCheckItem(id);
        // 再删除检查组
        checkGroupDao.deleteById(id);
    }

}
