package com.yun.bi.backend.model.vo;

import lombok.Data;

/**
 * Created with IntelliJ IDEA.
 *
 * @Author: __yun
 * @Date: 2024/08/25/12:50
 * @Description:
 */
@Data
public class BiResponse {
    private String genChart;
    private String genResult;
    private Long chartId;
}
