package com.testpart1.entity;

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
//@ApiModel(value="PPartChain对象", description="")
@TableName("p_part_chain")
public class PPartChain implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    private String createBy;

    @TableField(fill = FieldFill.INSERT)
    private Date createTime;

    private String updateBy;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Date updateTime;

    private String description;

    private String hostPartChainId;

    private Boolean isGlobal;

    private String partChainName;


}
