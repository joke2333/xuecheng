package com.xuecheng.manage_cms.controller;

import com.xuecheng.api.cms.CmsPageControllerApi;
import com.xuecheng.framework.domain.cms.CmsPage;
import com.xuecheng.framework.domain.cms.request.QueryPageRequest;
import com.xuecheng.framework.domain.cms.response.CmsPageResult;
import com.xuecheng.framework.model.response.QueryResponseResult;
import com.xuecheng.framework.model.response.ResponseResult;
import com.xuecheng.manage_cms.service.PageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 页面管理控制层
 */
@RestController
@RequestMapping("/cms/page")
public class CmsPageController implements CmsPageControllerApi {

    @Autowired
    private PageService pageService;

    @GetMapping("/list/{page}/{size}")
    public QueryResponseResult findList(@PathVariable("page") int page, @PathVariable("size") int size, QueryPageRequest queryPageRequest) {
        return pageService.findList(page, size, queryPageRequest);
    }

    @PostMapping("/add")
    public CmsPageResult add(@RequestBody CmsPage cmspage) {
        return pageService.add(cmspage);
    }

    @GetMapping("/get/{id}")
    public CmsPage findById(@PathVariable String id) {
        return pageService.findByPageId(id);
    }

    @PutMapping("/edit/{id}")
    public CmsPageResult update(@PathVariable String id, @RequestBody CmsPage cmsPage) {
        return pageService.update(id, cmsPage);
    }

    @DeleteMapping("/del/{id}")
    public ResponseResult delete(@PathVariable String id) {
        return pageService.delete(id);
    }

}
