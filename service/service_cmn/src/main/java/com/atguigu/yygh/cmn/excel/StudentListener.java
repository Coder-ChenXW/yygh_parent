package com.atguigu.yygh.cmn.excel;

import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class StudentListener extends AnalysisEventListener<Student> {

    private static final int BATCH_COUNT = 10;
    List<Student> list = new ArrayList();

    @Override
    public void invoke(Student student, AnalysisContext analysisContext) {
        // 批量操作
        list.add(student);
//        if (list.size() >= BATCH_COUNT) {
//            baseMapper.batchInsert(list);
//            list.clear();
//        }
        System.out.println(student);
    }

    @Override
    public void invokeHeadMap(Map<Integer, String> headMap, AnalysisContext context) {
        System.out.println("标题为:" + headMap);
    }

    @Override
    public void doAfterAllAnalysed(AnalysisContext analysisContext) {
//        baseMapper.batchInsert(list);
    }
}
