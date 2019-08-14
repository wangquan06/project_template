package com.moft.sysauth.application.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;


@FeignClient(value = "biz")
public interface BizService {
    @RequestMapping(value = "rest/q/biz/{name}",method = RequestMethod.GET)
    String getFilter(@PathVariable("name") String name, @RequestParam("code") String code);
}
