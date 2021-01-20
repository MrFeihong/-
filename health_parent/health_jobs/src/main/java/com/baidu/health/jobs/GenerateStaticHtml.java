package com.baidu.health.jobs;

import com.alibaba.dubbo.config.annotation.Reference;
import com.baidu.health.pojo.Setmeal;
import com.baidu.health.service.SetmealService;
import com.baidu.health.utils.QiNiuUtils;
import freemarker.template.Configuration;
import freemarker.template.Template;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import javax.annotation.PostConstruct;
import java.io.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;


@Component
public class GenerateStaticHtml {

    @Autowired
    private JedisPool jedisPool;

    @Autowired
    private Configuration configuration;

    @Reference
    private SetmealService setmealService;

    @Value("${out_put_path}")
    private String out_put_path;// 静态文件存放的目录

    /**
     * 初始化freemarker配置信息
     */
    @PostConstruct
    public void init(){
        // 默认编码
        configuration.setDefaultEncoding("utf-8");
        // 加载模板路径,通过类路径来读取模板
        configuration.setClassForTemplateLoading(GenerateStaticHtml.class, "/ftl");
    }

    @Scheduled(initialDelay = 3000,fixedDelay = 1800000)
    public void generateHtml(){
        //生成静态页面
        Jedis jedis = jedisPool.getResource();
        // 1. 读取redis的key zset方式
        String key = "setmeal:static:html";
        Set<String> setmealIds = jedis.zrange(key, 0, -1);
        //    zadd key score 值 添加套餐
        //    zrang key 0 -1 获取集合的所有元素
        //    zrem key value 删除集合中的元素
        try {
            if(!CollectionUtils.isEmpty(setmealIds)) {
                // 2. 遍历套餐id集合
                for (String setmealIdStr : setmealIds) {
                    // 3. id|操作符0,1|时间戳 分割字段split
                    String[] setmealIdsArr = setmealIdStr.split("\\|");
                    String setmealId = setmealIdsArr[0];// 套餐id
                    String operation = setmealIdsArr[1]; // 操作符 0: 删除,1:生成详情页面
                    // 4. 判断操作类型
                    if("0".equals(operation)) {
                        // 5.   操作符0: 删除, 页面名称setmeal_{id}.html
                        File file = new File(out_put_path,"setmeal_" + setmealId + ".html");
                        if(file.exists()){
                            file.delete();
                        }
                        System.out.println("删除页面..." + setmealId);
                    }else if("1".equals(operation)) {
                        // 6.   操作符1: 生成静态页面
                        generateSetmealDetailHtml(setmealId);
                        System.out.println("生成套餐详情...." + setmealId);
                    }
                    // 删除处理过的套餐
                    jedis.zrem(key, setmealIdStr);
                }
                // 7. 生成列表静态页面
                generateSetmealList();
                System.out.println("生成静态列表页....");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        jedis.close();
    }

    /**
     * 生成套餐详情页面
     * @param setmealId
     */
    private void generateSetmealDetailHtml(String setmealId) throws Exception{
        // 查询套餐详情
        Setmeal setmealDetail = setmealService.findDetailById(Integer.valueOf(setmealId));
        // 拼接图片路径
        setmealDetail.setImg(QiNiuUtils.DOMAIN+setmealDetail.getImg());
        // 构建map数据
        // 构建数据map
        Map<String,Object> dataMap = new HashMap<String,Object>();
        // key要参考模板中的 <img src="${setmeal.img}"
        dataMap.put("setmeal",setmealDetail);
        // 定义保存的文件名
        String filename = out_put_path+"setmeal_" + setmealId + ".html";
        // 模板文件名
        String templateName = "mobile_setmeal_detail.ftl";
        // 调用方法生成页面
        generateStaticHtml(dataMap,filename,templateName);
    }

    /**
     * 生成套餐详情列表
     * @throws Exception
     */
    private void generateSetmealList() throws Exception {
        // 查询所有的套餐列表
        List<Setmeal> setmealList = setmealService.findAll();
        // 拼接图片完整路径
        setmealList.forEach(s->s.setImg(QiNiuUtils.DOMAIN+s.getImg()));
        // 构建数据map
        Map<String,Object> dataMap = new HashMap<String,Object>();
        // key要参考模板中的 <#list setmealList as setmeal>
        dataMap.put("setmealList",setmealList);
        // out_put_path 带上了最后一个/
        String filename = out_put_path + "mobile_setmeal.html";
        generateStaticHtml(dataMap,filename,"mobile_setmeal.ftl");
    }

    /**
     * 通过模板与数据，生成页面
     * @param dataMap
     * @param filename
     * @param templateName
     * @throws Exception
     */
    private void generateStaticHtml(Map<String,Object> dataMap, String filename, String templateName) throws Exception{
        // 获取模板
        Template template = configuration.getTemplate(templateName);
        // 定义writer输出流, 一定要注意，要指定utf-8编码，如不指定则使用iso-8859-1
        BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(filename),"utf-8"));
        // 模板填充数据到输出流writer
        template.process(dataMap,writer);
        // 关闭流writer
        writer.flush();
        writer.close();
    }
}
