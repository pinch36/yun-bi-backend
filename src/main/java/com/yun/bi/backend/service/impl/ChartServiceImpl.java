package com.yun.bi.backend.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yun.bi.backend.model.entity.Chart;
import com.yun.bi.backend.service.ChartService;
import com.yun.bi.backend.mapper.ChartMapper;
import org.springframework.stereotype.Service;

/**
* @author ylw16
* @description 针对表【chart(图表信息表)】的数据库操作Service实现
* @createDate 2024-08-22 06:59:30
*/
@Service
public class ChartServiceImpl extends ServiceImpl<ChartMapper, Chart>
    implements ChartService{

}




