package sbs.getcry.mapper;

import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * @Author : saeko
 * @Date: 2025/4/22 16:43
 * @Description: 权限信息mapper
 */

public interface SysPermMapper {


    /**
     * 根据用户 userId 查询
     * 用户角色表(sys_user_role)和角色权限表(sys_role_perm)关联查询用户拥有的权限(perm_key)
     * 查询流程:
     * 1.从 sys_user_role 找到 用户ID 对应的角色(role_id)
     * 2.通过 sys_role_perm 找到这些角色关联的权限(perm_id)
     * 3.从 sys_perm 获取权限的 perm_key
     *
     * @param userId
     * @return
     */
    @Select("SELECT sp.perm_key FROM sys_perm sp " +
            "JOIN sys_role_perm srp ON srp.perm_id = sp.perm_id " +
            "JOIN sys_user_role sur ON sur.role_id = srp.role_id " +
            "WHERE sur.user_id = #{userId}")
    List<String> selectPermByUserId(long userId);
}
