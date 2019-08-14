package com.moft.sysauth.interfaces.facade.app.q;
import com.moft.sysauth.domain.entity.Department;
import com.moft.sysauth.domain.repository.DepartmentRepository;
import com.moft.sysauth.infrastructure.util.BeanMapperUtil;
import com.moft.sysauth.interfaces.dto.ArrayRestResult;
import com.moft.sysauth.interfaces.dto.DepartmentDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.util.List;

@RestController
@Path("q/department")
public class QDepartmentController {
    @Autowired
    private DepartmentRepository departmentRepository;
    @GET
    @Path("/")
    @Produces(MediaType.APPLICATION_JSON)
    public ArrayRestResult get() {
        ArrayRestResult result = new ArrayRestResult();

        List<Department> list = departmentRepository.findAll();
        List<DepartmentDTO> dtoList = BeanMapperUtil.convertToBeanList(list, DepartmentDTO.class);
        result.setContents(dtoList);
        result.setSuccess(true);
        return result;
    }
}
