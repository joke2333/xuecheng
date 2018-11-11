package com.xuecheng.api.cms;

import com.xuecheng.framework.domain.cms.CmsPage;
import com.xuecheng.framework.domain.cms.request.QueryPageRequest;
import com.xuecheng.framework.domain.cms.response.CmsPageResult;
import com.xuecheng.framework.model.response.QueryResponseResult;
import com.xuecheng.framework.model.response.ResponseResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;

@Api(value = "cms页面管理接口", description = "cms页面管理接口，提供页面的增、删、改、查")
public interface CmsPageControllerApi {

    @ApiOperation("分页查询页面列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "page", value = "页码", required = true, paramType = "path", dataType = "int"),
            @ApiImplicitParam(name = "size", value = "每页记录数", required = true, paramType = "path", dataType = "int"),
            @ApiImplicitParam(name = "queryPageRequest", value = "条件对象", required = false, paramType = "path", dataType = "QueryPageRequest")
    })
    public QueryResponseResult findList(int page, int size, QueryPageRequest queryPageRequest);

    @ApiOperation("新增页面")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "cmsPage", value = "页面对象", required = false, paramType = "path", dataType = "cmsPage")
    })
    public CmsPageResult add(CmsPage cmsPage);

    @ApiOperation("根据id查询页面")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "页面Id", required = true, paramType = "path", dataType = "string")
    })
    public CmsPage findByPageId(String id);

    @ApiOperation("更新页面")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "页面Id", required = true, paramType = "path", dataType = "string"),
            @ApiImplicitParam(name = "cmsPage", value = "页面对象", required = true, paramType = "path", dataType = "cmsPage")
    })
    public CmsPageResult update(String id, CmsPage cmsPage);

    @ApiOperation("删除页面")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "页面Id", required = true, paramType = "path", dataType = "string")
    })
    public ResponseResult delete(String id);


}
