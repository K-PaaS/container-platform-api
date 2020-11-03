package org.paasta.container.platform.api.events;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.paasta.container.platform.api.common.Constants;
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
@RequestMapping("/clusters/{cluster:.+}/namespaces/{namespace:.+}/events")
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
     * @param type the type
     * @param isAdmin the isAdmin
     * @return the events list
     */
    @ApiOperation(value = "Events 목록 조회(Get Events list)", nickname = "getEventsUidList")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "namespace", value = "네임스페이스 명", required = true, dataType = "string", paramType = "path"),
            @ApiImplicitParam(name = "resourceUid", value = "리소스 Uid 명",  required = true, dataType = "string", paramType = "path")
    })
    @GetMapping(value = "/resources/{resourceUid:.+}")
    public Object getEventsUidList(@PathVariable("namespace") String namespace,
                                   @PathVariable("resourceUid") String resourceUid,
                                   @ApiIgnore @RequestParam(value="type", required=false) String type,
                                   @ApiIgnore @RequestParam(required = false, name = "isAdmin") boolean isAdmin) {

        if (isAdmin) {
            return eventsService.getEventsUidListAdmin(namespace, resourceUid, type);
        }

        return eventsService.getEventsUidList(namespace, resourceUid, type);
    }

    /**
     * Events node 목록 조회(Get Events node)
     *
     * @param namespace the namespace
     * @param nodeName the node name
     * @param isAdmin the isAdmin
     * @return the events list
     */
    @ApiOperation(value = "Events 목록 조회(Get Events node)", nickname = "getEventsByNode")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "namespace", value = "네임스페이스 명", required = true, dataType = "string", paramType = "path"),
            @ApiImplicitParam(name = "nodeName", value = "노드 명", required = true, dataType = "string", paramType = "query")
    })
    @GetMapping(value = "/nodes/{nodeName:.+}")
    public Object getEventsByNode(@PathVariable(value = "namespace") String namespace,
                                  @PathVariable(value = "nodeName") String nodeName,
                                  @ApiIgnore @RequestParam(required = false, name = "isAdmin") boolean isAdmin) {

        if (isAdmin) {
            return eventsService.getEventsListByNodeAdmin(namespace, nodeName);
        }
        return eventsService.getEventsListByNode(namespace, nodeName);
    }


    /**
     * Events 목록 조회(Get Events list)
     *
     * @param cluster    the cluster
     * @param namespace  the namespace
     * @param offset     the offset
     * @param limit      the limit
     * @param orderBy    the orderBy
     * @param order      the order
     * @param searchName the searchName
     * @param isAdmin    the isAdmin
     * @return the endpoints list
     */
    @ApiOperation(value = "Events 목록 조회(Get Events list)", nickname = "getEventsList")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "cluster", value = "클러스터 명", required = true, dataType = "string", paramType = "path"),
            @ApiImplicitParam(name = "namespace", value = "네임스페이스 명", required = true, dataType = "string", paramType = "path"),
            @ApiImplicitParam(name = "offset", value = "목록 시작지점, 기본값 0", required = false, dataType = "int", paramType = "query"),
            @ApiImplicitParam(name = "limit", value = "한 페이지에 가져올 리소스 최대 수", required = false, dataType = "int", paramType = "query"),
            @ApiImplicitParam(name = "orderBy", value = "정렬 기준, 기본값 creationTime(생성날짜)", required = false, dataType = "string", paramType = "query"),
            @ApiImplicitParam(name = "order", value = "정렬 순서, 기본값 desc(내림차순)", required = false, dataType = "string", paramType = "query"),
            @ApiImplicitParam(name = "searchName", value = "리소스 명 검색", required = false, dataType = "string", paramType = "query")
    })
    @GetMapping
    public Object getEventsList(@PathVariable(value = "cluster") String cluster,
                                @PathVariable(value = "namespace") String namespace,
                                @RequestParam(required = false, defaultValue = "0") int offset,
                                @RequestParam(required = false, defaultValue = "0") int limit,
                                @RequestParam(required = false, defaultValue = "creationTime") String orderBy,
                                @RequestParam(required = false, defaultValue = "desc") String order,
                                @RequestParam(required = false, defaultValue = "") String searchName,
                                @ApiIgnore @RequestParam(required = false, name = "isAdmin") boolean isAdmin) {

        if (namespace.toLowerCase().equals(Constants.ALL_NAMESPACES)) {
            if (isAdmin) {
                return eventsService.getEventsListAllNamespacesAdmin(offset, limit, orderBy, order, searchName);
            } else {
                return Constants.FORBIDDEN_ACCESS_RESULT_STATUS;
            }
        }

        if (isAdmin) {
            return eventsService.getEventsListAdmin(namespace, offset, limit, orderBy, order, searchName);
        }
        return eventsService.getEventsList(namespace, offset, limit, orderBy, order, searchName);
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
