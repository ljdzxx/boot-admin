package com.hb0730.boot.admin.project.monitor.job.model.vo;

import com.hb0730.boot.admin.commons.web.model.BusinessVO;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotNull;

/**
 * <p>
 * 定时任务
 * </p>
 *
 * @author bing_huang
 * @since 2020-04-06
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class SystemJobVO extends BusinessVO {

    private static final long serialVersionUID = 1L;

    /**
     * 备注
     */
    private String description;

    /**
     * id
     */
    private Long id;

    /**
     * number
     */
    private String number;

    /**
     * name
     */
    private String name;

    /**
     * 调用目标
     */
    @NotNull(message = "调用不为空")
    private String beanName;

    /**
     * 调用方法
     */
    @NotNull(message = "调用方法不为空")
    private String methodName;

    /**
     * 参数
     */
    private String params;

    /**
     * 表达式
     */
    @NotNull(message = "表达式不为空")
    private String cron;
}