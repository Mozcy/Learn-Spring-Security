package sbs.getcry.contrller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import sbs.getcry.common.R;
import sbs.getcry.dto.SysUserDTO;
import sbs.getcry.entity.SysUser;
import sbs.getcry.service.UserService;

@RestController
@RequestMapping("/user")
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
     * 获取用户信息API
     *
     * @return
     */
    @GetMapping("/info")
    public R info() {
        return userService.info();
    }
}
