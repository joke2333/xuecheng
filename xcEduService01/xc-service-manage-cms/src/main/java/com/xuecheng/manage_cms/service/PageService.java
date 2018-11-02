package com.xuecheng.manage_cms.service;

import com.xuecheng.framework.domain.cms.CmsPage;
import com.xuecheng.framework.domain.cms.request.QueryPageRequest;
import com.xuecheng.framework.domain.cms.response.CmsCode;
import com.xuecheng.framework.model.response.CommonCode;
import com.xuecheng.framework.model.response.QueryResponseResult;
import com.xuecheng.framework.model.response.QueryResult;
import com.xuecheng.framework.model.response.ResultCode;
import com.xuecheng.manage_cms.dao.CmsPageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
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

        Pageable pageable = PageRequest.of(page,size);
        // 查询
        Page<CmsPage> all = cmsPageRepository.findAll(pageable);
        //
        QueryResult<CmsPage> objectQueryResult = new QueryResult<CmsPage>();
        objectQueryResult.setList(all.getContent());    // 集合
        objectQueryResult.setTotal(all.getTotalElements()); // 总数
        QueryResponseResult queryResponseResult = new QueryResponseResult(CommonCode.SUCCESS,objectQueryResult);
        return queryResponseResult;
    }

}
