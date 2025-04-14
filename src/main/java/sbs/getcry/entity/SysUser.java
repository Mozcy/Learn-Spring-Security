package sbs.getcry.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.ToString;

import java.util.Date;

@Data
@ToString
@TableName("sys_user")
public class SysUser {
    @TableId(type = IdType.AUTO)
    private Long userId;        // 用户ID
    @NotBlank(message = "账号不能为空")
    private String userName;    // 用户账号
    @NotBlank(message = "昵称不能为空")
    private String nickName;    // 用户昵称
    private String sex;         // 用户性别（0男 1女 2未知）
    private String avatar;      // 头像地址
    // 只写入, 不返回(会接收前端过来的数据, 但不会返回给前端)
    @NotBlank(message = "密码不能为空")
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String password;    // 密码
    private String status;      // 帐号状态（0正常 1停用）
    private String delFlag;     // 删除标志（0代表存在 1代表删除）
    private String createBy;    // 创建者
    private Date createTime;    // 创建时间
    private String updateBy;    // 更新者
    private Date updateTime;    // 更新时间
}
