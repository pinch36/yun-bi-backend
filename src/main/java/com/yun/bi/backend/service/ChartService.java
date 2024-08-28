package com.yun.bi.backend.service;


import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.yun.bi.backend.model.dto.chart.ChartQueryRequest;
import com.yun.bi.backend.model.entity.Chart;
import com.yun.bi.backend.model.vo.ChartVO;

import javax.servlet.http.HttpServletRequest;

/**
* @author ylw16
* @description 针对表【chart(图表信息表)】的数据库操作Service
* @createDate 2024-08-22 06:59:30
*/
public interface ChartService extends IService<Chart> {

    ChartVO getChartVO(Chart chart, HttpServletRequest request);

    Wrapper<Chart> getQueryWrapper(ChartQueryRequest chartQueryRequest);

    Page<ChartVO> getChartVOPage(Page<Chart> chartPage, HttpServletRequest request);

    void validChart(Chart chart, boolean b);
}
