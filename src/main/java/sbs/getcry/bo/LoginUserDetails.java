package sbs.getcry.bo;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import sbs.getcry.entity.SysUser;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @Author : saeko
 * @Date: 2025/3/31 14:13
 * @Description: 自定义登录认证逻辑
 */

@Data
@AllArgsConstructor
public class LoginUserDetails implements UserDetails {

    private SysUser sysUser;          // 用户信息
    private List<String> permissions; // 用户的权限集合

    /**
     * 获取当前用户的权限信息（角色/权限）
     *
     * @return 用户的权限集合
     */
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return permissions.stream().map(SimpleGrantedAuthority::new).collect(Collectors.toList());
    }

    /**
     * 获取当前用户的密码
     *
     * @return 用户密码
     */
    @Override
    public String getPassword() {
        return sysUser.getPassword();
    }

    /**
     * 获取当前用户的用户名
     *
     * @return 用户名
     */
    @Override
    public String getUsername() {
        return sysUser.getUserName();
    }

    /**
     * 账号是否未过期
     *
     * @return true 表示账号未过期，false 表示账号已过期
     */
    @Override
    public boolean isAccountNonExpired() {
        return UserDetails.super.isAccountNonExpired(); // 默认实现, 始终返回 true
    }

    /**
     * 账号是否未被锁定
     *
     * @return true 表示账号未被锁定，false 表示账号已被锁定
     */
    @Override
    public boolean isAccountNonLocked() {
        return UserDetails.super.isAccountNonLocked(); // 默认实现, 始终返回 true
    }

    /**
     * 凭据（密码）是否未过期
     *
     * @return true 表示密码未过期，false 表示密码已过期
     */
    @Override
    public boolean isCredentialsNonExpired() {
        return UserDetails.super.isCredentialsNonExpired(); // 默认实现, 始终返回 true
    }

    /**
     * 账号是否启用
     *
     * @return true 表示账号启用，false 表示账号被禁用
     */
    @Override
    public boolean isEnabled() {
        return sysUser.getStatus().equals("0");   // 0表示启用，1表示禁用
    }
}
