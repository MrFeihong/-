package com.baidu.security;

import com.baidu.health.pojo.Permission;
import com.baidu.health.pojo.Role;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


public class UserService implements UserDetailsService {
    /**
     * 获取登陆用户信息
     *
     * @param username 从前端传过来的用户名
     * @return 用户名，密码，权限集合
     * @throws UsernameNotFoundException
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // 假设从数据库查询, 从数据库查询到的用户信息（用户名，密码，角色，权限）
        com.baidu.health.pojo.User userInDb = findByUsername(username);
        if(null != userInDb){
            // 授权, userDetail是security需要的
            //
            //String password, 密码，必须是从数据库查询到的密码
            //Collection<? extends GrantedAuthority> authorities 用户的权限集合 权限模型
            List<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>();
            // 遍历用户的角色与权限
            // 用户拥有的角色  //角色的集 role是pojo
            Set<Role> roles = userInDb.getRoles();
            if(null != roles){
                GrantedAuthority sga = null;
                //获取角色名
                for (Role role : roles) {
                    // 角色名, 授予角色
                    sga = new SimpleGrantedAuthority(role.getKeyword());
                    //把角色名添加进 权限模型中
                    authorities.add(sga);
                    // 授予权限, 这个角色下所拥有的权限 获取该角色下所有的权限集合 pojo.set
                    Set<Permission> permissions = role.getPermissions();
                    if(null != permissions){
                        // 遍历该角色下的所有权限 拿到每一个权限
                        for (Permission permission : permissions) {
                            // 授予权限 权限.拿到他的权限值 添加进权限模型 的权限中
                            sga = new SimpleGrantedAuthority(permission.getKeyword());
                            authorities.add(sga);
                        }
                    }
                }
            }
            // 然后把 用户名密码权限集合赋值给user
            User securityUser = new User(username, userInDb.getPassword(),authorities);
            return securityUser;

        }
        return null;
    }

    /**
     * 这个用户admin/admin, 有ROLE_ADMIN角色，角色下有ADD_CHECKITEM权限
     * 假设从数据库查询
     * @param username
     * @return
     */
    private com.baidu.health.pojo.User findByUsername (String username){
        if("admin".equals(username)) {
            com.baidu.health.pojo.User user = new com.baidu.health.pojo.User();
            user.setUsername("admin");
            // 使用密文
            user.setPassword("$2a$10$u/BcsUUqZNWUxdmDhbnoeeobJy6IBsL1Gn/S0dMxI2RbSgnMKJ.4a");

            // 角色
            Role role = new Role();
            role.setKeyword("ROLE_ADMIN");//设置角色

            // 权限
            Permission permission = new Permission();
            permission.setKeyword("ROLE_ABC");//设置权限

            // 给角色添加权限
            role.getPermissions().add(permission);//在角色的权限集合中设置了权限

            // 把角色放进 user的权限集合中集合
            Set<Role> roleList = new HashSet<Role>();
            roleList.add(role);

            //角色
            role = new Role();
            //添加ABC角色 但是判别时只是角色
            role.setKeyword("ABC");
            roleList.add(role); //权限集合中添加了校色

            // 设置用户的角色
            user.setRoles(roleList);
            System.out.println("======================");
            System.out.println(user);
            System.out.println("======================");
            return user;
        }
        return null;
    }

    public static void main(String[] args) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        // 加密
        //System.out.println(encoder.encode("1234"));
        //System.out.println(encoder.encode("1234"));
        //System.out.println(encoder.encode("1234"));

        // 验证密码
        // 第1个 为明文
        // 第2个 为密文
        System.out.println(encoder.matches("1234", "$2a$10$H0FGS7LPDDHfXONVGLQUu.xe/MXW8JtzzHr3XkwFK59rVMDeEsWBG"));
        System.out.println(encoder.matches("1234", "$2a$10$EzyuHV1dhaQ4hr3al5w0IO78ZPIcWCi1w9LqP3Wo8mPkk73UL7qkO"));
        System.out.println(encoder.matches("1234", "$2a$10$9Gxn3nbf1fbEMXbrWvLb3ODKAOP0lYe0GvFRNEPfW5pqjgiYQ2R3m"));
        System.out.println(encoder.matches("1234", "$2a$10$u/BcsUUqZNWUxdmDhbnoeeobJy6IBsL1Gn/S0dMxI2RbSgnMKJ.4a"));
        System.out.println(encoder.matches("1234", "$2a$10$u/BcsUUqZNWUxdmDhbnoeeobJy6IBsL1Gn/S0dMxI2RbSgnMKJ.4a"));

    }
}
