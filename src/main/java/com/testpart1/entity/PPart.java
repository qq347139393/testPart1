package com.testpart1.entity;

import java.math.BigDecimal;

import com.baomidou.mybatisplus.annotation.*;

import java.util.Date;
import java.io.Serializable;
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
//@ApiModel(value="PPart对象", description="")
@TableName("p_part")
public class PPart implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    private String createBy;

    @TableField(fill = FieldFill.INSERT)
    private Date createTime;

    private String updateBy;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Date updateTime;

    private BigDecimal cost;

    private BigDecimal cusNum1;

    private BigDecimal cusNum2;

    private BigDecimal cusNum3;

    private BigDecimal cusNum4;

    private BigDecimal cusNum5;

    private String cusText1;

    private String cusText2;

    private String cusText3;

    private String cusText4;

    private String cusText5;

    private String description;

    private Integer ecoOrderQty;

    private String hostPartId;

    private Boolean isActive;

    private Boolean isPartKit;

    private Boolean isProcurable;

    private Boolean isRepairable;

    private Integer lotSize;

    private Integer lotSizeRound;

    private Integer minOrderQty;

    private Integer packSize;

    private Integer packSizeRound;

    private Long partCriticalId;

    private Long partFamilyId;

    private Long partGroupId;

    private String partName;

    private String partNum;

    private Long partTypeId;

    private Long plannerCodeId;

    private Long priVendorId;

    private Long priVendorLocId;

    private BigDecimal price;

    private BigDecimal procureLen;

    private BigDecimal procureVar;

    private BigDecimal replenLen;

    private BigDecimal replenVar;

    private String vendorPartNum;

    private BigDecimal volume;

    private BigDecimal washRate;

    private BigDecimal weight;


}
