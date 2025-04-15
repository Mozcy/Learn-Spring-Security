package sbs.getcry.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

/**
 * @Author : saeko
 * @Date: 2025/4/15 16:40
 * @Description: 接收前端传递过来的登录信息
 */

@Data
public class SysUserDTO {
    @NotBlank(message = "账号不能为空")
    private String userName;    // 用户账号
    @NotBlank(message = "密码不能为空")
    private String password;    // 用户密码
    @NotBlank(message = "昵称不能为空")
    private String nickName;    // 用户昵称
    private String sex;         // 用户性别（0男 1女 2未知）
    private String avatar;      // 头像地址
}
