package com.testpart1.entity;

import java.math.BigDecimal;

import com.baomidou.mybatisplus.annotation.*;

import java.util.Date;
import java.io.Serializable;
//import io.swagger.annotations.ApiModel;
//import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 *
 * </p>
 *
 * @author Planet
 * @since 2022-03-16
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
//@ApiModel(value="PPartChainDetails对象", description="")
@TableName("p_part_chain_details")
public class PPartChainDetails implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    private String createBy;

    @TableField(fill = FieldFill.INSERT)
    private Date createTime;

    private String updateBy;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Date updateTime;

    private Integer chainDepth;

    private Integer chainIndex;

    private String description;

    private Boolean isProcurable;

    private Boolean isRepairable;

    private Boolean isRollupBad;

    private Boolean isRollupGood;

    private Boolean isRollupGoodAsBad;

    private Long parentPartId;

    private Long partChainId;

    private Long partId;

    private Integer relationType;

    private BigDecimal rollupDemandPercent;

    private Boolean isActive;

    //进行判断是否已经检查过的字段
    @TableField(exist = false)
    private Boolean flag;
//    //存子元素的排序的字段
//    @TableField(exist = false)
//    private Integer childrenOrder;


}
