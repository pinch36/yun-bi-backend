package com.yun.bi.backend.bizmq;

import com.alibaba.excel.util.StringUtils;
import com.rabbitmq.client.Channel;
import com.yun.bi.backend.common.ErrorCode;
import com.yun.bi.backend.constant.ChartConstant;
import com.yun.bi.backend.exception.BusinessException;
import com.yun.bi.backend.manager.AiManager;
import com.yun.bi.backend.model.entity.Chart;
import com.yun.bi.backend.service.ChartService;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.support.AmqpHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

import static com.yun.bi.backend.constant.CommonConstant.BI_MODEL_ID;

@Component
@Slf4j
public class BIMessageConsumer {
    @Resource
    private ChartService chartService;
    @Resource
    private AiManager aiManager;

    /**
     * 接收消息的方法
     *
     * @param message     接收到的消息内容，是一个字符串类型
     * @param channel     消息所在的通道，可以通过该通道与 RabbitMQ 进行交互，例如手动确认消息、拒绝消息等
     * @param deliveryTag 消息的投递标签，用于唯一标识一条消息
     */
    // 使用@SneakyThrows注解简化异常处理
    @SneakyThrows
    @RabbitListener(queues = {BiMqConstant.BI_QUEUE_NAME}, ackMode = "MANUAL")
    public void receiveMessage(String message, Channel channel, @Header(AmqpHeaders.DELIVERY_TAG) long deliveryTag) {
        log.info("receiveMessage message = {}", message);
        if (StringUtils.isBlank(message)) {
            channel.basicNack(deliveryTag,false,false);
            throw new BusinessException(ErrorCode.SYSTEM_ERROR,"消息为空");
        }
        long chartId = Long.parseLong(message);
        Chart chart = chartService.getById(chartId);
        if (chart == null){
            channel.basicNack(deliveryTag,false,false);
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR,"图表为空");
        }
        Chart updateChartStatus = new Chart();
        updateChartStatus.setStatus(ChartConstant.STATUS_RUNNING);
        updateChartStatus.setId(chart.getId());
        boolean update = chartService.updateById(updateChartStatus);
        if (!update) {
            handleChartUpdateError(chart.getId(), "更新图表错误");
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "更新图表失败");
        }
//        String result = aiManager.doChat(BI_MODEL_ID, buildUserInput(chart));
        String result = "sdfos【【【【【spjidfwie【【【【【sodhfsdfhu";
        String[] splits = result.split("【【【【【");
        if (splits.length < 3) {
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "AI 生成错误");
        }
        String genChart = splits[1].trim();
        String genResult = splits[2].trim();
        Chart updateChartResult = new Chart();
        updateChartResult.setId(chart.getId());
        updateChartResult.setGenChart(genChart);
        //TODO 区分文件大小后分表存储
//        String genTable = "create table chart_"+chart.getId()+"\n" +
//                "(\n" +
//                "  日期  int null,\n" +
//                "  用户数 int null\n" +
//                ");\n";
//        Connection conn = null;
//        try {
//            conn = ds.getConnection();
//            // 执行非查询语句，返回影响的行数
//            int count = SqlExecutor.execute(conn,genTable);
//            log.info("影响行数：{}", count);
//        } catch (SQLException e) {
//            throw new RuntimeException(e);
//        }
        updateChartResult.setGenResult(genResult);
        updateChartResult.setStatus(ChartConstant.STATUS_SUCCEED);
        boolean updateResult = chartService.updateById(updateChartResult);
        if (!updateResult) {
            handleChartUpdateError(chart.getId(), "更新图表状态为成功失败");
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "更新图表失败");
        }
        // 手动确认消息的接收，向RabbitMQ发送确认消息
        channel.basicAck(deliveryTag, false);
    }

    /**
     * 构建用户输入
     *
     * @param chart 图表对象
     * @return 用户输入字符串
     */
    private String buildUserInput(Chart chart) {
        // 获取图表的目标、类型和数据
        String goal = chart.getGoal();
        String chartType = chart.getChartType();
        String csvData = chart.getChartData();

        // 构造用户输入
        StringBuilder userInput = new StringBuilder();
        userInput.append("分析需求：").append("\n");

        // 拼接分析目标
        String userGoal = goal;
        if (StringUtils.isNotBlank(chartType)) {
            userGoal += "，请使用" + chartType;
        }
        userInput.append(userGoal).append("\n");
        userInput.append("原始数据：").append("\n");
        userInput.append(csvData).append("\n");
        // 将StringBuilder转换为String并返回
        return userInput.toString();
    }

    private void handleChartUpdateError(Long chartId, String e) {
        Chart updateChartResult = new Chart();
        updateChartResult.setId(chartId);
        updateChartResult.setExecMessage(e);
        updateChartResult.setStatus("failed");
        boolean update = chartService.updateById(updateChartResult);
        if (!update) {
            log.error("更新图表状态为失败失败:{},{}", chartId, e);
        }
    }

}
