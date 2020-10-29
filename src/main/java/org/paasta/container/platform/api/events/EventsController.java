package org.paasta.container.platform.api.events;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.paasta.container.platform.api.common.model.ResultStatus;
import org.paasta.container.platform.api.common.util.ResourceExecuteManager;
import org.paasta.container.platform.api.endpoints.Endpoints;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import java.util.HashMap;

/**
 * Events Controller 클래스
 *
 * @author hrjin
 * @version 1.0
 * @since 2020.09.17
 */
@Api(value = "EventsController v1")
@RestController
@RequestMapping("/namespaces/{namespace:.+}/events")
public class EventsController {

    private final EventsService eventsService;

    /**
     * Instantiates a new Events controller
     *
     * @param eventsService the events service
     */
    @Autowired
    public EventsController(EventsService eventsService) {
        this.eventsService = eventsService;
    }


    /**
     * Events 목록 조회(Get Events list)
     *
     * @param namespace the namespace
     * @param resourceUid the resourceUid
     * @return the events list
     */
    @ApiOperation(value = "Events 상세 조회(Get Events detail)", nickname = "getEventsUidList")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "namespace", value = "네임스페이스 명", required = true, dataType = "string", paramType = "path"),
            @ApiImplicitParam(name = "resourceUid", value = "리소스 Uid 명",  required = true, dataType = "string", paramType = "path")
    })
    @GetMapping(value = "/resources/{resourceUid:.+}")
    public EventsList getEventsUidList(@PathVariable("namespace") String namespace,
                                       @PathVariable("resourceUid") String resourceUid,
                                       @ApiIgnore @RequestParam(value="type", required=false) String type) {
        return eventsService.getEventsUidList(namespace, resourceUid, type);
    }

    /**
     * Events 목록 조회(Get Events namespace)
     *
     * @param namespace the namespace
     * @return the events list
     */
    /*@GetMapping
    public EventsList getNamespaceEventsList(@PathVariable("namespace") String namespace) {
        return eventsService.getNamespaceEventsList(namespace);
    }*/

    /**
     * Events 목록 조회(Get Events list)
     *
     * @param cluster the cluster
     * @param namespace the namespace
     * @param limit the limit
     * @param continueToken the continueToken
     * @param searchParam the searchParam
     * @param isAdmin the isAdmin
     * @return the endpoints list
     */
    @ApiOperation(value = "Events 목록 조회(Get Events list)", nickname = "getEventsList")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "cluster", value = "클러스터 명", required = true, dataType = "string", paramType = "path"),
            @ApiImplicitParam(name = "namespace", value = "네임스페이스 명", required = true, dataType = "string", paramType = "path"),
            @ApiImplicitParam(name = "limit", value = "한 페이지에 가져올 리소스 최대 수", required = false, dataType = "int", paramType = "query"),
            @ApiImplicitParam(name = "continue", value = "컨티뉴 토큰", required = false, dataType = "string", paramType = "query"),
            @ApiImplicitParam(name = "searchParam", value = "검색 매개 변수", required = false, dataType = "string", paramType = "query")
    })
    @GetMapping
    public Object getEventsList(@PathVariable(value = "cluster") String cluster,
                                   @PathVariable(value = "namespace") String namespace,
                                   @RequestParam(required = false, defaultValue = "0") int limit,
                                   @RequestParam(required = false, name = "continue") String continueToken,
                                   @RequestParam(required = false) String searchParam,
                                   @ApiIgnore @RequestParam(required = false, name = "isAdmin") boolean isAdmin) {
        if (isAdmin) {
            return eventsService.getEventsListAdmin(namespace, limit, continueToken, searchParam);
        }
        return eventsService.getEventsList(namespace, limit, continueToken);
    }

    /**
     * Events 상세 조회(Get Events detail)
     *
     * @param namespace the namespace
     * @param resourceName the resource name
     * @param isAdmin the isAdmin
     * @return the events detail
     */
    @ApiOperation(value = "Events 상세 조회(Get Events detail)", nickname = "getEvents")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "namespace", value = "네임스페이스 명", required = true, dataType = "string", paramType = "path"),
            @ApiImplicitParam(name = "resourceName", value = "리소스 명",  required = true, dataType = "string", paramType = "path")
    })
    @GetMapping(value = "/{resourceName:.+}")
    public Object getEvents(@PathVariable(value = "namespace") String namespace,
                            @PathVariable(value = "resourceName") String resourceName,
                            @ApiIgnore @RequestParam(required = false, name = "isAdmin") boolean isAdmin) {

        // For Admin
        if (isAdmin) {
            return eventsService.getEventsAdmin(namespace, resourceName);
        }

        return eventsService.getEvents(namespace, resourceName);
    }

    /**
     * Events YAML 조회(Get Events yaml)
     *
     * @param namespace the namespace
     * @param resourceName the resource name
     * @return the events yaml
     */
    @ApiOperation(value = "Events YAML 조회(Get Events yaml)", nickname = "getEventsYaml")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "namespace", value = "네임스페이스 명", required = true, dataType = "string", paramType = "path"),
            @ApiImplicitParam(name = "resourceName", value = "리소스 명", required = true, dataType = "string", paramType = "path")
    })
    @GetMapping(value = "/{resourceName:.+}/yaml")
    public Events getEventsYaml(@PathVariable(value = "namespace") String namespace,
                                @PathVariable(value = "resourceName") String resourceName) {
        return eventsService.getEventsYaml(namespace, resourceName, new HashMap<>());
    }

}
