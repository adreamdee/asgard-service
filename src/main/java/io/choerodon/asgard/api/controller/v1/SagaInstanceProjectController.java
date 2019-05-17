package io.choerodon.asgard.api.controller.v1;

import java.util.Map;

import com.github.pagehelper.PageInfo;
import io.choerodon.base.annotation.Permission;
import io.choerodon.base.constant.PageConstant;
import io.choerodon.base.enums.ResourceType;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import io.choerodon.asgard.api.dto.SagaInstanceDTO;
import io.choerodon.asgard.api.dto.SagaInstanceDetailsDTO;
import io.choerodon.asgard.api.service.SagaInstanceService;
import io.choerodon.core.iam.InitRoleCode;
import io.choerodon.core.iam.ResourceLevel;

@Controller
@RequestMapping("/v1/sagas/projects/{project_id}/instances")
public class SagaInstanceProjectController {

    private SagaInstanceService sagaInstanceService;

    public SagaInstanceProjectController(SagaInstanceService sagaInstanceService) {
        this.sagaInstanceService = sagaInstanceService;
    }

    public void setSagaInstanceService(SagaInstanceService sagaInstanceService) {
        this.sagaInstanceService = sagaInstanceService;
    }

    @Permission(type = ResourceType.PROJECT, roles = {InitRoleCode.PROJECT_OWNER, InitRoleCode.PROJECT_ADMINISTRATOR})
    @GetMapping
    @ApiOperation(value = "项目层查询事务实例列表")
    @ResponseBody
    public ResponseEntity<PageInfo<SagaInstanceDTO>> pagingQuery(@PathVariable("project_id") long projectId,
                                                                 @RequestParam(value = "sagaCode", required = false) String sagaCode,
                                                                 @RequestParam(name = "status", required = false) String status,
                                                                 @RequestParam(name = "refType", required = false) String refType,
                                                                 @RequestParam(name = "refId", required = false) String refId,
                                                                 @RequestParam(name = "params", required = false) String params,
                                                                 @RequestParam(defaultValue = PageConstant.PAGE, required = false) final int page,
                                                                 @RequestParam(defaultValue = PageConstant.SIZE, required = false) final int size) {
        return sagaInstanceService.pageQuery(page, size, sagaCode, status, refType, refId, params, ResourceLevel.PROJECT.value(), projectId);
    }

    @Permission(type = ResourceType.PROJECT, roles = {InitRoleCode.PROJECT_OWNER, InitRoleCode.PROJECT_ADMINISTRATOR})
    @GetMapping(value = "/{id}", produces = "application/json")
    @ApiOperation(value = "查询某个事务实例运行详情")
    public ResponseEntity<String> query(@PathVariable("project_id") long projectId,
                                        @PathVariable("id") Long id) {
        return sagaInstanceService.query(id);
    }


    @Permission(type = ResourceType.PROJECT, roles = {InitRoleCode.PROJECT_OWNER, InitRoleCode.PROJECT_ADMINISTRATOR})
    @GetMapping(value = "/{id}/details", produces = "application/json")
    @ApiOperation(value = "查询事务实例的具体信息")
    public ResponseEntity<SagaInstanceDetailsDTO> queryDetails(@PathVariable("project_id") long projectId,
                                                               @PathVariable("id") Long id) {
        return new ResponseEntity<>(sagaInstanceService.queryDetails(id), HttpStatus.OK);
    }


    @Permission(type = ResourceType.PROJECT, roles = {InitRoleCode.PROJECT_OWNER, InitRoleCode.PROJECT_ADMINISTRATOR})
    @GetMapping(value = "/statistics", produces = "application/json")
    @ApiOperation(value = "统计项目下各个事务实例状态下的实例个数")
    public ResponseEntity<Map> statistics(@PathVariable("project_id") long projectId) {
        return new ResponseEntity<>(sagaInstanceService.statistics(ResourceLevel.PROJECT.value(), projectId), HttpStatus.OK);
    }


}
