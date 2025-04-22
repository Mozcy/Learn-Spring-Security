package sbs.getcry.service.impl;

import com.alibaba.druid.util.StringUtils;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sbs.getcry.bo.LoginUserDetails;
import sbs.getcry.common.R;
import sbs.getcry.common.TokenService;
import sbs.getcry.dto.SysUserDTO;
import sbs.getcry.entity.SysUser;
import sbs.getcry.mapper.SysUserMapper;
import sbs.getcry.service.UserService;
import sbs.getcry.utils.JWTUtils;

/**
 * @Author : saeko
 * @Date: 2025/4/8 21:39
 * @Description: 用户操作实现类
 */
@Service
public class UserServiceImpl extends ServiceImpl<SysUserMapper, SysUser> implements UserService {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private TokenService tokenService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    /**
     * 登录实现
     *
     * @param sysUser
     * @return
     */
    @Override
    public R login(SysUser sysUser) {
        try {
            // 创建一个用户名和密码的认证令牌（未认证状态）
            // 第一个参数是用户名，第二个参数是密码
            UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(sysUser.getUserName(), sysUser.getPassword());

            // 使用 AuthenticationManager 对用户名密码进行认证
            // 会触发 UserDetailsService#loadUserByUsername 方法去查用户信息
            // 注意: 如果密码错误, 会抛出 BadCredentialsException 异常
            Authentication authenticate = authenticationManager.authenticate(authentication);

            // 从认证结果中获取认证后的用户信息（此时已通过认证）
            // authenticate.getPrincipal() 返回的是 UserDetails 接口的实现类
            LoginUserDetails details = (LoginUserDetails) authenticate.getPrincipal();

            // 生成 JWT 令牌
            String token = tokenService.createToken(details);
            return R.ok("登录成功", token);
        } catch (BadCredentialsException e) {
            return R.of(HttpStatus.UNAUTHORIZED.value(), "登陆失败, 用户名或密码错误!", null);
        }
    }

    /**
     * 登出实现
     *
     * @param request
     * @return
     */
    @Override
    public R logout(HttpServletRequest request) {
        String header = request.getHeader("Admin-Token");
        if (StringUtils.isEmpty(header)) {
            return R.of(HttpStatus.FORBIDDEN.value(), "登出失败, 令牌不能为空!", null);
        }
        String token = header.replace("Bearer ", "");
        try {
            String userKey = JWTUtils.getUserKey(token);
            LoginUserDetails loginUser = tokenService.getLoginUser(userKey);
            if (loginUser == null) {
                return R.fail("登出失败, 用户未登录!");
            }
            tokenService.deleteLoginUser(userKey);
        } catch (JwtException e) {
            return R.fail("登出失败, 令牌校验失败! 错误信息: " + e.getMessage());
        }
        return R.ok("登出成功");
    }

    /**
     * 注册实现
     *
     * @param sysUserDTO
     * @return
     */
    @Override
    @Transactional
    public R register(SysUserDTO sysUserDTO) {
        QueryWrapper queryWrapper = new QueryWrapper<SysUser>();
        queryWrapper.eq("user_name", sysUserDTO.getUserName());
        if (exists(queryWrapper)) {
            return R.fail("注册失败, 用户名已存在!", null);
        }
        String encodePassword = passwordEncoder.encode(sysUserDTO.getPassword());
        sysUserDTO.setPassword(encodePassword);
        // 创建用户实体类
        SysUser sysUser = new SysUser();
        sysUser.setUserName(sysUserDTO.getUserName());
        sysUser.setPassword(encodePassword);
        sysUser.setNickName(sysUserDTO.getNickName());
        sysUser.setSex(sysUserDTO.getSex());
        sysUser.setAvatar(sysUserDTO.getAvatar());
        // 写入到数据库中
        if (!save(sysUser)) {
            return R.fail("注册失败, 未知错误!", null);
        }
        return R.ok("注册成功");
    }

    /**
     * 通过userId删除用户
     *
     * @param userId
     * @return
     */
    @Override
    public R delete(long userId) {
        if (userId == 1) {
            return R.fail("禁止删除系统管理员!");
        } else if (removeById(userId)) {
            // 注意: 这删除用户后, 需要清除 Redis 中的登录用户信息, 测试就不做演示了! 实绩开发中需要注意
            return R.ok("删除成功");
        }
        return R.fail(String.format("删除失败, 用户ID [%d] 不存在!", userId));
    }

    /**
     * 获取用户信息实现
     *
     * @return
     */
    @Override
    public R info() {
        // 通过 SecurityContextHolder 获取当前用户的认证信息中的用户信息
        LoginUserDetails principal = (LoginUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return R.ok("success", principal.getSysUser());
    }
}
