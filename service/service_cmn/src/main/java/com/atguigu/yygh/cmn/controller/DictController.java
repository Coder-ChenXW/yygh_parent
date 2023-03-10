package com.atguigu.yygh.cmn.controller;


import com.alibaba.excel.EasyExcel;
import com.atguigu.yygh.cmn.excel.Student;
import com.atguigu.yygh.cmn.excel.StudentListener;
import com.atguigu.yygh.cmn.listener.DictListener;
import com.atguigu.yygh.cmn.service.DictService;
import com.atguigu.yygh.common.result.R;
import com.atguigu.yygh.model.cmn.Dict;
import com.atguigu.yygh.vo.cmn.DictEeVo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 组织架构表 前端控制器
 * </p>
 *
 * @author atguigu
 * @since 2022-05-23
 */
@RestController
@RequestMapping("/admin/cmn")
@CrossOrigin
public class DictController {


    @Autowired
    private DictService dictService;


    /**
     * @description: 文件上传
     * @author: ChenXW
     * @date: 2023-03-09 13:39
     */
    @PostMapping("/upload")
    public R upload(MultipartFile file) throws IOException {
        dictService.upload(file);
        return R.ok();

    }


    /**
     * @description: 数据字典下载
     * @author: ChenXW
     * @date: 2023-03-09 11:12
     */
    @GetMapping("/download")
    public void download(HttpServletResponse response) throws IOException {

        dictService.download(response);

    }

    /**
     * @description: 数据字典
     * @author: ChenXW
     * @date: 2023-03-09 09:24
     */
    @GetMapping("/childList/{pid}")
    public R getChildListByPid(@PathVariable Long pid) {
        List<Dict> list = dictService.getChildListByPid(pid);
        return R.ok().data("items", list);
    }
}

