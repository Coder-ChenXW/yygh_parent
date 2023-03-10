package com.atguigu.yygh.cmn.listener;

import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import com.atguigu.yygh.cmn.mapper.DictMapper;
import com.atguigu.yygh.model.cmn.Dict;
import com.atguigu.yygh.vo.cmn.DictEeVo;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.springframework.beans.BeanUtils;

import java.util.Map;

public class DictListener extends AnalysisEventListener<DictEeVo> {

    private DictMapper dictMapper;

    public DictListener(DictMapper dictMapper) {
        this.dictMapper = dictMapper;
    }

    @Override
    public void invoke(DictEeVo dictEeVo, AnalysisContext analysisContext) {
        Dict dict = new Dict();
        BeanUtils.copyProperties(dictEeVo, dict);

        QueryWrapper<Dict> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("id", dictEeVo.getId());
        Integer count = this.dictMapper.selectCount(queryWrapper);

        if (count > 0) {
            this.dictMapper.updateById(dict);
        } else {
            this.dictMapper.insert(dict);
        }

    }


    @Override
    public void doAfterAllAnalysed(AnalysisContext analysisContext) {

    }
}
