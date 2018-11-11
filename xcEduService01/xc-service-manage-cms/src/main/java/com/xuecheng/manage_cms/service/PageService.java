package com.xuecheng.manage_cms.service;

import com.xuecheng.framework.domain.cms.CmsPage;
import com.xuecheng.framework.domain.cms.request.QueryPageRequest;
import com.xuecheng.framework.domain.cms.response.CmsCode;
import com.xuecheng.framework.domain.cms.response.CmsPageResult;
import com.xuecheng.framework.model.response.CommonCode;
import com.xuecheng.framework.model.response.QueryResponseResult;
import com.xuecheng.framework.model.response.QueryResult;
import com.xuecheng.framework.model.response.ResponseResult;
import com.xuecheng.manage_cms.dao.CmsPageRepository;
import io.netty.util.internal.StringUtil;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import javax.jws.Oneway;
import javax.swing.text.html.parser.Entity;
import java.util.Optional;

@Service
public class PageService {

    @Autowired
    private CmsPageRepository cmsPageRepository;

    /**
     * 分页条件查询页面
     *
     * @param page             页码，从1开始
     * @param size             每页显示数量
     * @param queryPageRequest
     * @return
     */
    public QueryResponseResult findList(int page, int size, QueryPageRequest queryPageRequest) {
        if (page <= 0) {
            page = 1;
        }
        if (size < 0) {
            size = 10;
        }
        page = page - 1;


        Pageable pageable = PageRequest.of(page, size);

        Example<CmsPage> example = this.getExample(queryPageRequest);

        Page<CmsPage> all;
        if (example == null) {
            all = cmsPageRepository.findAll(pageable);
        } else {
            // 查询
            all = cmsPageRepository.findAll(example, pageable);
        }

        // 封装返回对象
        QueryResult<CmsPage> objectQueryResult = new QueryResult<CmsPage>();
        objectQueryResult.setList(all.getContent());    // 集合
        objectQueryResult.setTotal(all.getTotalElements()); // 总数
        QueryResponseResult queryResponseResult = new QueryResponseResult(CommonCode.SUCCESS, objectQueryResult);
        return queryResponseResult;
    }

    // 封装条件查询
    private Example<CmsPage> getExample(QueryPageRequest queryPageRequest) {

        if (queryPageRequest == null) {
            return null;
        }
        // 设置匹配方式
        ExampleMatcher exampleMatcher = ExampleMatcher.matching()
                // 别名设置包含匹配
                .withMatcher("pageAliase", ExampleMatcher.GenericPropertyMatcher.of(ExampleMatcher.StringMatcher.CONTAINING));

        // 形成对象
        CmsPage cmsPage = new CmsPage();
        if (!StringUtil.isNullOrEmpty(queryPageRequest.getPageAliase())) {
            cmsPage.setPageAliase(queryPageRequest.getPageAliase());
        }
        if (!StringUtil.isNullOrEmpty(queryPageRequest.getPageId())) {
            cmsPage.setPageId(queryPageRequest.getPageId());
        }
        if (!StringUtil.isNullOrEmpty(queryPageRequest.getTemplateId())) {
            cmsPage.setTemplateId(queryPageRequest.getTemplateId());
        }
        if (!StringUtil.isNullOrEmpty(queryPageRequest.getPageName())) {
            cmsPage.setPageName(queryPageRequest.getPageName());
        }

        return Example.of(cmsPage, exampleMatcher);
    }

    // 新增页面
    public CmsPageResult add(CmsPage cmspage) {
        CmsPage page = cmsPageRepository.findBySiteIdAndPageWebPathAndPageName(cmspage.getSiteId(), cmspage.getPageWebPath(), cmspage.getPageName());
        // 判断添加的信息是否唯一
        if (page == null) {
            cmspage.setPageId(null);
            cmsPageRepository.save(cmspage);
            return new CmsPageResult(CommonCode.SUCCESS, cmspage);
        } else {
            return new CmsPageResult(CommonCode.FAIL, null);
        }

    }


    // 根据id查询页面
    public CmsPage findByPageId(String pageId) {
        if (pageId == null || pageId.equals("")) {
            return null;
        }
        Optional<CmsPage> optional = cmsPageRepository.findById(pageId);
        return optional.orElse(null);
    }

    // 根据id更新页面
    public CmsPageResult update(String pageId, CmsPage cmsPage) {
        if (cmsPage == null) {
            return new CmsPageResult(CommonCode.FAIL, null);
        }
        // 先查出数据
        CmsPage one = this.findByPageId(pageId);
        // 修改数据
        if (one != null) {
            //更新页面模板
            one.setTemplateId(cmsPage.getTemplateId());
            //更新所属站点
            one.setSiteId(cmsPage.getSiteId());
            //更新页面别名
            one.setPageAliase(cmsPage.getPageAliase());
            //更新页面名称
            one.setPageName(cmsPage.getPageName());
            //更新访问路径
            one.setPageWebPath(cmsPage.getPageWebPath());
            //更新物理路径
            one.setPagePhysicalPath(cmsPage.getPagePhysicalPath());
            //执行更新
            CmsPage save = cmsPageRepository.save(one);
            //返回成功
            return new CmsPageResult(CommonCode.SUCCESS, save);
        }
        return new CmsPageResult(CommonCode.FAIL, null);
    }

    // 根据id删除页面
    public ResponseResult delete(String id){
        if (id!=null || !id.equals("")){
            Optional<CmsPage> one = cmsPageRepository.findById(id);
            if (one.isPresent()){
                cmsPageRepository.deleteById(id);
                return ResponseResult.SUCCESS();
            }
        }
        return new ResponseResult(CmsCode.CMS_COURSE_PAGEISNULL);   // 页面不存在
    }

}
