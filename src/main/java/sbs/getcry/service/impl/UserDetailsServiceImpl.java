package sbs.getcry.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import sbs.getcry.bo.LoginUserDetails;
import sbs.getcry.entity.SysUser;
import sbs.getcry.mapper.SysPermMapper;
import sbs.getcry.mapper.SysUserMapper;

import java.util.List;

/**
 * @Author : saeko
 * @Date: 2025/3/31 14:18
 * @Description: 从数据库或其他存储中加载用户信息
 */

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private SysUserMapper sysUserMapper;

    @Autowired
    private SysPermMapper sysPermMapper;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // 1. 根据用户名查询用户信息
        QueryWrapper queryWrapper = new QueryWrapper<SysUser>()
                .eq("user_name", username);
        SysUser sysUser = sysUserMapper.selectOne(queryWrapper);

        // 2. 如果用户不存在，抛出异常
        if (sysUser == null) {
            throw new UsernameNotFoundException("当前用户不存在");
        }

        // 3. 通过 user_id -> role_id -> perm_id 查询用户的权限信息
        List<String> perms = sysPermMapper.selectPermByUserId(sysUser.getUserId());

        // 4. 如果用户存在，返回用户信息
        LoginUserDetails loginUserDetails = new LoginUserDetails(sysUser, perms);

        return loginUserDetails;
    }
}
