package com.moft.biz.interfaces.facade;

import org.springframework.web.bind.annotation.RestController;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.QueryParam;

/**
 * Created by EalenXie on 2018/8/30 18:10.
 */
@RestController
@Path("q/biz")
public class BizController {
    @GET
    @Path("/{name}")
    public String getFilter(@PathParam("name") String name, @QueryParam("code") String code) {
        return String.format("getFilter name=%s, code=%s", name, code);
    }
}
