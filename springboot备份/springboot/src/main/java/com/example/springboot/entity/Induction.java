package com.example.springboot.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

/**
 * <p>
 *
 * </p>
 *
 * @author li
 * @since 2023-07-28
 */
@Data
  public class Induction implements Serializable {

    private static final long serialVersionUID = 1L;

      @TableId(value = "id", type = IdType.AUTO)
      private Integer id;

      /**
     * 职系
     */
      private String grade;//职系

    private String estate;//职级

    private String position;//zhiwei

    private String worker;//区分

    private String site;//地点

    private String company;

    private String department;

    private String principal;//人

    private String entry;//入职

    private String internship;//实习期间

    private String trial;//试用期间

    private String modify;//原因

    private String difference;//在职区分

    private String dimission;//离职

    private Integer adminId;

    @TableField(exist = false)
    private String admin;//xingming

}
