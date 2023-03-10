package com.atguigu.yygh.cmn.excel;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.write.style.ColumnWidth;
import org.springframework.web.bind.annotation.ExceptionHandler;

public class Student {

    @ExcelProperty(value = "学生id")
    private Integer sid;
    @ExcelProperty(value = "学生姓名")
    @ColumnWidth(value = 50)
    private String name;
    @ExcelProperty(value = "学生年龄")
    private Integer age;
    @ExcelProperty(value = "学生性别")
    private boolean gender;


    public Student() {
    }

    public Student(Integer sid, String name, Integer age, boolean gender) {
        this.sid = sid;
        this.name = name;
        this.age = age;
        this.gender = gender;
    }

    /**
     * 获取
     * @return sid
     */
    public Integer getSid() {
        return sid;
    }

    /**
     * 设置
     * @param sid
     */
    public void setSid(Integer sid) {
        this.sid = sid;
    }

    /**
     * 获取
     * @return name
     */
    public String getName() {
        return name;
    }

    /**
     * 设置
     * @param name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * 获取
     * @return age
     */
    public Integer getAge() {
        return age;
    }

    /**
     * 设置
     * @param age
     */
    public void setAge(Integer age) {
        this.age = age;
    }

    /**
     * 获取
     * @return gender
     */
    public boolean isGender() {
        return gender;
    }

    /**
     * 设置
     * @param gender
     */
    public void setGender(boolean gender) {
        this.gender = gender;
    }

    public String toString() {
        return "Student{sid = " + sid + ", name = " + name + ", age = " + age + ", gender = " + gender + "}";
    }
}
