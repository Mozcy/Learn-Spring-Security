package sbs.getcry.contrller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import sbs.getcry.common.R;
import sbs.getcry.dto.SysUserDTO;
import sbs.getcry.entity.SysUser;
import sbs.getcry.service.UserService;

@RestController
@RequestMapping("/user")
@Validated
public class UserController {

    @Autowired
    private UserService userService;

    /**
     * 登录API
     *
     * @param sysUser
     * @return
     */
    @PostMapping("/login")
    public R login(@RequestBody SysUser sysUser) {
        return userService.login(sysUser);
    }

    /**
     * 登出API
     *
     * @param request
     * @return
     */
    @GetMapping("/logout")
    public R logout(HttpServletRequest request) {
        return userService.logout(request);
    }

    /**
     * 注册API
     *
     * @param sysUserDTO
     * @return
     */
    @PostMapping("/register")
    public R register(@RequestBody @Valid SysUserDTO sysUserDTO) {
        return userService.register(sysUserDTO);
    }

    /**
     * 删除用户
     * @param userId
     * @return
     */
    @GetMapping("/delete")
    @PreAuthorize("hasAuthority('user:delete')")
    public R delete(@NotNull(message = "userId 不能为空") Long userId) {
        return userService.delete(userId);
    }

    /**
     * 获取用户信息API
     *
     * @return
     */
    @GetMapping("/info")
    @PreAuthorize("hasAuthority('user:view')")
    public R info() {
        return userService.info();
    }
}
