package com.yun.bi.backend.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.yun.bi.backend.model.entity.Chart;

import java.util.List;
import java.util.Map;

/**
* @author ylw16
* @description 针对表【chart(图表信息表)】的数据库操作Mapper
* @createDate 2024-08-22 06:59:30
* @Entity generator.domain.Chart
*/
public interface ChartMapper extends BaseMapper<Chart> {
    List<Map<String,Object>> queryChartData(String querySql);
}




