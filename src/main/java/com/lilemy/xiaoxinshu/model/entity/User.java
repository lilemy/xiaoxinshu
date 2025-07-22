package com.lilemy.xiaoxinshu.model.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

/**
 * @author lilemy
 * @date 2025/7/20 23:26
 */
@Data
@Schema(name = "User", description = "用户")
public class User {

    /**
     * 用户名
     */
    @NotBlank(message = "用户名不能为空")
    @Schema(description = "用户名")
    private String username;

    /**
     * 性别
     */
    @NotNull(message = "性别不能为空")
    @Schema(description = "性别")
    private Integer gender;

    /**
     * 年龄
     */
    @NotNull(message = "年龄不能为空")
    @Min(value = 0, message = "年龄必须大于或等于 0")
    @Max(value = 200, message = "年龄必须小于或等于 200")
    @Schema(description = "年龄")
    private Integer age;

    /**
     * 邮箱
     */
    @NotBlank(message = "邮箱不能为空")
    @Email(message = "邮箱格式不正确")
    @Schema(description = "邮箱")
    private String email;

    /**
     * 日期时间
     */
    private LocalDateTime dateTime;

    /**
     * 日期
     */
    private LocalDate date;

    /**
     * 时间
     */
    private LocalTime time;

}
