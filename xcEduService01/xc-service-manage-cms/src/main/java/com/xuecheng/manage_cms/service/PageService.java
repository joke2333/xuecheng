package com.xuecheng.manage_cms.service;

import com.xuecheng.framework.domain.cms.CmsPage;
import com.xuecheng.framework.domain.cms.request.QueryPageRequest;
import com.xuecheng.framework.domain.cms.response.CmsPageResult;
import com.xuecheng.framework.model.response.CommonCode;
import com.xuecheng.framework.model.response.QueryResponseResult;
import com.xuecheng.framework.model.response.QueryResult;
import com.xuecheng.manage_cms.dao.CmsPageRepository;
import io.netty.util.internal.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import javax.swing.text.html.parser.Entity;

@Service
public class PageService{

    @Autowired
    private CmsPageRepository cmsPageRepository;

    /**
     * 分页条件查询页面
     * @param page 页码，从1开始
     * @param size 每页显示数量
     * @param queryPageRequest
     * @return
     */
    public QueryResponseResult findList(int page, int size, QueryPageRequest queryPageRequest) {
        if (page<=0){
            page=1;
        }
        if (size<0){
            size=10;
        }
        page=page-1;
        if (queryPageRequest ==null){
            queryPageRequest = new QueryPageRequest();
        }

        Pageable pageable = PageRequest.of(page,size);

        // 设置匹配方式
        ExampleMatcher exampleMatcher = ExampleMatcher.matching()
                // 别名设置包含匹配
                .withMatcher("pageAliase", ExampleMatcher.GenericPropertyMatcher.of(ExampleMatcher.StringMatcher.CONTAINING));

        // 形成对象
        CmsPage cmsPage = new CmsPage();
        if(!StringUtil.isNullOrEmpty(queryPageRequest.getPageAliase())){
            cmsPage.setPageAliase(queryPageRequest.getPageAliase());
        }
        if(!StringUtil.isNullOrEmpty(queryPageRequest.getPageId())){
            cmsPage.setPageId(queryPageRequest.getPageId());
        }
        if(!StringUtil.isNullOrEmpty(queryPageRequest.getTemplateId())){
            cmsPage.setTemplateId(queryPageRequest.getTemplateId());
        }
        if(!StringUtil.isNullOrEmpty(queryPageRequest.getPageName())){
            cmsPage.setPageName(queryPageRequest.getPageName());
        }

        // 条件对象
        Example<CmsPage> example = Example.of(cmsPage,exampleMatcher);


        // 查询
        Page<CmsPage> all = cmsPageRepository.findAll(example,pageable);
        // 封装返回对象
        QueryResult<CmsPage> objectQueryResult = new QueryResult<CmsPage>();
        objectQueryResult.setList(all.getContent());    // 集合
        objectQueryResult.setTotal(all.getTotalElements()); // 总数
        QueryResponseResult queryResponseResult = new QueryResponseResult(CommonCode.SUCCESS,objectQueryResult);
        return queryResponseResult;
    }

    // 新增页面
    public CmsPageResult add(CmsPage cmspage){
        CmsPage page = cmsPageRepository.findBySiteIdAndPageWebPathAndPageName(cmspage.getSiteId(), cmspage.getPageWebPath(), cmspage.getPageName());
        // 判断添加的信息是否唯一
        if (page==null){
            cmspage.setPageId(null);
            cmsPageRepository.save(cmspage);
            return new CmsPageResult(CommonCode.SUCCESS,cmspage);
        }else {
            return new CmsPageResult(CommonCode.FAIL,null);
        }

    }

}
