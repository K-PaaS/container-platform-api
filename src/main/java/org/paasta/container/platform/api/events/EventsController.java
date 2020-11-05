package org.paasta.container.platform.api.events;

import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

/**
 * Events Controller 클래스
 *
 * @author kjhoon
 * @version 1.0
 * @since 2020.11.05
 */
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
     * 특정 Namespace 의 전체 Events 목록 조회(Get Events list in a Namespace)
     *
     * @param cluster    the cluster
     * @param namespace  the namespace
     * @param offset     the offset
     * @param limit      the limit
     * @param orderBy    the orderBy
     * @param order      the order
     * @param searchName the searchName
     * @param isAdmin    the isAdmin
     * @return the events list
     */
    @ApiOperation(value = "특정 Namespace 의 전체 Events 목록 조회(Get Events list in a Namespace)", nickname = "getNamespaceEventsList")
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
    public Object getNamespaceEventsList(@PathVariable(value = "cluster") String cluster,
                                         @PathVariable(value = "namespace") String namespace,
                                         @RequestParam(required = false, defaultValue = "0") int offset,
                                         @RequestParam(required = false, defaultValue = "0") int limit,
                                         @RequestParam(required = false, defaultValue = "creationTime") String orderBy,
                                         @RequestParam(required = false, defaultValue = "") String order,
                                         @RequestParam(required = false, defaultValue = "") String searchName,
                                         @ApiIgnore @RequestParam(required = false, name = "isAdmin") boolean isAdmin) {

        if (isAdmin) {
            return eventsService.getNamespaceEventsListAdmin(namespace, offset, limit, orderBy, order, searchName);
        }

        return eventsService.getNamespaceEventsList(namespace, offset, limit, orderBy, order, searchName);
    }


    /**
     * Events 목록 조회(Get Events list)
     *
     * @param cluster     the cluster
     * @param namespace   the namespace
     * @param resourceUid the resourceUid
     * @param offset      the offset
     * @param limit       the limit
     * @param orderBy     the orderBy
     * @param order       the order
     * @param searchName  the searchName
     * @param type        the type
     * @param isAdmin     the isAdmin
     * @return the events list
     */
    @ApiOperation(value = "Events 목록 조회(Get Events list)", nickname = "getEventsList")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "cluster", value = "클러스터 명", required = true, dataType = "string", paramType = "path"),
            @ApiImplicitParam(name = "namespace", value = "네임스페이스 명", required = true, dataType = "string", paramType = "path"),
            @ApiImplicitParam(name = "resourceUid", value = "리소스 uid", required = true, dataType = "string", paramType = "path"),
            @ApiImplicitParam(name = "offset", value = "목록 시작지점, 기본값 0", required = false, dataType = "int", paramType = "query"),
            @ApiImplicitParam(name = "limit", value = "한 페이지에 가져올 리소스 최대 수", required = false, dataType = "int", paramType = "query"),
            @ApiImplicitParam(name = "orderBy", value = "정렬 기준, 기본값 creationTime(생성날짜)", required = false, dataType = "string", paramType = "query"),
            @ApiImplicitParam(name = "order", value = "정렬 순서, 기본값 desc(내림차순)", required = false, dataType = "string", paramType = "query"),
            @ApiImplicitParam(name = "searchName", value = "리소스 명 검색", required = false, dataType = "string", paramType = "query")
    })
    @GetMapping(value = "/resources/{resourceUid:.+}")
    public Object getEventsList(@PathVariable(value = "cluster") String cluster,
                                @PathVariable(value = "namespace") String namespace,
                                @PathVariable(value = "resourceUid") String resourceUid,
                                @RequestParam(required = false, defaultValue = "0") int offset,
                                @RequestParam(required = false, defaultValue = "0") int limit,
                                @RequestParam(required = false, defaultValue = "creationTime") String orderBy,
                                @RequestParam(required = false, defaultValue = "") String order,
                                @RequestParam(required = false, defaultValue = "") String searchName,
                                @ApiIgnore @RequestParam(value = "type", required = false) String type,
                                @ApiIgnore @RequestParam(required = false, name = "isAdmin") boolean isAdmin) {

        if (isAdmin) {
            return eventsService.getEventsListAdmin(namespace, resourceUid, type, offset, limit, orderBy, order, searchName);
        }

        return eventsService.getEventsList(namespace,resourceUid, type, offset, limit, orderBy, order, searchName);
    }

}
