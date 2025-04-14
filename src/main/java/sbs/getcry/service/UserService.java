package sbs.getcry.service;

import com.baomidou.mybatisplus.extension.service.IService;
import jakarta.servlet.http.HttpServletRequest;
import sbs.getcry.common.R;
import sbs.getcry.entity.SysUser;

public interface UserService extends IService<SysUser> {

    /**
     * 登录
     *
     * @param sysUser
     * @return
     */
    R login(SysUser sysUser);

    /**
     * 登出
     *
     * @param request
     * @return
     */
    R logout(HttpServletRequest request);

    /**
     * 注册
     *
     * @param sysUser
     * @return
     */
    R register(SysUser sysUser);

    /**
     * 获取用户信息
     *
     * @return
     */
    R info();
}
