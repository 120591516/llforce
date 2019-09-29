package com.llvision.face.base;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import springfox.documentation.annotations.ApiIgnore;

/**
 * controller基础类
 * @Author yudong
 * @Date 2018年07月05日 下午4:06
 */
public class BaseController<Biz extends BaseBiz>{

    @Autowired
    protected  Biz baseBiz;

}
