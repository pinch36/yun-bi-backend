package com.yun.bi.backend.service.impl;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yun.bi.backend.model.dto.chart.ChartQueryRequest;
import com.yun.bi.backend.model.entity.Chart;
import com.yun.bi.backend.model.vo.ChartVO;
import com.yun.bi.backend.service.ChartService;
import com.yun.bi.backend.mapper.ChartMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.stream.Collectors;

/**
* @author ylw16
* @description 针对表【chart(图表信息表)】的数据库操作Service实现
* @createDate 2024-08-22 06:59:30
*/
@Service
public class ChartServiceImpl extends ServiceImpl<ChartMapper, Chart>
    implements ChartService{

    @Override
    public ChartVO getChartVO(Chart chart, HttpServletRequest request) {
        ChartVO chartVO = new ChartVO();
        BeanUtils.copyProperties(chart,chartVO);
        return chartVO;
    }

    @Override
    public Wrapper<Chart> getQueryWrapper(ChartQueryRequest chartQueryRequest) {
        QueryWrapper<Chart> chartQueryWrapper = new QueryWrapper<>();
        chartQueryWrapper.eq("id",chartQueryRequest.getId())
                .eq("userId",chartQueryRequest.getUserId())
                .like("name",chartQueryRequest.getName())
                .like("goal",chartQueryRequest.getGoal())
                .like("chartType",chartQueryRequest.getChartType());
        return chartQueryWrapper;
    }

    @Override
    public Page<ChartVO> getChartVOPage(Page<Chart> chartPage, HttpServletRequest request) {
        List<ChartVO> chartVOS = chartPage.getRecords().stream().map(chart -> {
            ChartVO chartVO = new ChartVO();
            BeanUtils.copyProperties(chart, chartVO);
            return chartVO;
        }).collect(Collectors.toList());
        Page<ChartVO> chartVOPage = new Page<>(chartPage.getCurrent(), chartPage.getSize());
        chartVOPage.setRecords(chartVOS);
        return chartVOPage;
    }

    @Override
    public void validChart(Chart chart, boolean b) {

    }
}




