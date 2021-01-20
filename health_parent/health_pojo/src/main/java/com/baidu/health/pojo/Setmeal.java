package com.baidu.health.pojo;

import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.List;

/**
 * 体检套餐
 */
public class Setmeal implements Serializable {

    private Integer id;

    @NotBlank(message = "套餐不能为空")
    private String name;

    @NotNull(message = "套餐编号不能为空")
    @Length(min =4,max = 10,message = "套餐编码的长度必须为4-10个")
    private String code;

    @NotNull
    @Length(min =4,max = 10,message = "套餐助记的长度必须为4-10个")
    private String helpCode;

    @NotNull(message = "性别不能为空")
    @Range(min = 0, max = 2, message = "性别不能为空")
    private String sex;//套餐适用性别：0不限 1男 2女

    @NotNull(message = "使用年龄不能为空")
    @Pattern(regexp = "^((1[0-5])|[1-9])?\\d$", message = "请输入正确的套餐年龄")
    private String age;//套餐适用年龄

    @NotNull(message = "套餐价格不能为空")
    @Min(value = 10,message = "套餐价格不能低于10元")
    @Max(value = 99999,message = "套餐价格不能高于99999")
    private Float price;//套餐价格


    private String remark;


    private String attention;

    @NotBlank(message = "图片不能为空")
    private String img;//套餐对2图片存储路径-


    @Size(min = 1)
    private List<CheckGroup> checkGroups;//体检套餐对应的检查组，多对多关系

    public List<CheckGroup> getCheckGroups() {
        return checkGroups;
    }

    public void setCheckGroups(List<CheckGroup> checkGroups) {
        this.checkGroups = checkGroups;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getHelpCode() {
        return helpCode;
    }

    public void setHelpCode(String helpCode) {
        this.helpCode = helpCode;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public Float getPrice() {
        return price;
    }

    public void setPrice(Float price) {
        this.price = price;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getAttention() {
        return attention;
    }

    public void setAttention(String attention) {
        this.attention = attention;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }
}
