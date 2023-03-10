package com.atguigu.yygh.cmn.excel;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.ExcelWriter;
import com.alibaba.excel.write.builder.ExcelWriterSheetBuilder;
import com.alibaba.excel.write.metadata.WriteSheet;

import java.util.ArrayList;
import java.util.List;

public class ExcelWriteDemo {

    // 方式一
//    public static void main(String[] args) {
//        List<Student> students = new ArrayList<>();
//        students.add(new Student(1,"刘德华",61,true));
//        students.add(new Student(2,"张学友",55,true));
//        students.add(new Student(3,"郭富城",58,true));
//        students.add(new Student(4,"黎明",64,true));
//        EasyExcel.write("C:\\Users\\ChenXW\\Desktop\\hello.xlsx", Student.class).sheet("学生列表")
//                .doWrite(students);
//    }

    // 方式二
    public static void main(String[] args) {
        List<Student> students = new ArrayList<>();
        students.add(new Student(1, "刘德华", 61, true));
        students.add(new Student(2, "张学友", 55, true));
        students.add(new Student(3, "郭富城", 58, true));
        students.add(new Student(4, "黎明", 64, true));

        List<Student> studentList = new ArrayList<>();
        studentList.add(new Student(5, "熏悟空", 61, true));
        studentList.add(new Student(6, "居八戒", 55, true));

        ExcelWriter excelWriter = EasyExcel.write("C:\\Users\\ChenXW\\Desktop\\abc.xlsx", Student.class)
                .build();

        WriteSheet sheet1 = EasyExcel.writerSheet(0, "学生列表1").build();
        WriteSheet sheet2 = EasyExcel.writerSheet(1, "学生列表2").build();
        excelWriter.write(students, sheet1);
        excelWriter.write(studentList, sheet2);

        excelWriter.finish();

    }


}
