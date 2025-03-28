package sbs.getcry.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.ToString;

import java.util.Date;

@Data
@ToString
@TableName("sys_user")
public class SysUser {
    private Long userId;        // 用户ID
    private String userName;    // 用户账号
    private String nickName;    // 用户昵称
    private String sex;         // 用户性别（0男 1女 2未知）
    private String avatar;      // 头像地址
    private String password;    // 密码
    private String status;      // 帐号状态（0正常 1停用）
    private String delFlag;     // 删除标志（0代表存在 1代表删除）
    private String createBy;    // 创建者
    private Date createTime;    // 创建时间
    private String updateBy;    // 更新者
    private Date updateTime;    // 更新时间
}
