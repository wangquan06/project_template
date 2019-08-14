package com.moft.sysauth.interfaces.facade.account.c;

import com.moft.sysauth.application.client.BizService;
import com.moft.sysauth.interfaces.dto.SimpleRestResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;

@RestController
@Path("c/account")
public class CAccountController {
    @Autowired
    private BizService bizService;

    @POST
    @Path("/login")
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    @Produces(MediaType.APPLICATION_JSON)
    public SimpleRestResult login() {
        SimpleRestResult result = new SimpleRestResult();

        result.setSuccess(true);
        return result;
    }

    @GET
    @Path("/test")
    public String test(@QueryParam(value = "name") String name, @QueryParam(value = "code") String code) {
        return bizService.getFilter(name, code);
    }
}
