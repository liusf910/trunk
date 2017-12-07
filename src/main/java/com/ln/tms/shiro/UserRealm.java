package com.ln.tms.shiro;

import com.ln.tms.mapper.UserMapper;
import com.ln.tms.pojo.User;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * AuthenticationRealm
 *
 * @author HuaXin Team
 * @version 1.0.0
 */
@SuppressWarnings("SpringJavaAutowiringInspection")
public class UserRealm extends AuthorizingRealm {

    @Autowired
    private UserMapper userMapper;

    /**
     * 授权
     *
     * @param principals
     * @return 授权信息
     */
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
        //获取当前登录输入的用户名，等价于(String)  super.getAvailablePrincipal(principalCollection);
        Principal principal = (Principal) principals.fromRealm(getName()).iterator().next();
        if (principal != null) {
            User user = userMapper.selectByPrimaryKey(principal.getId());
            SimpleAuthorizationInfo simpleAuthorizationInfo = new SimpleAuthorizationInfo();
            List<String> permissions = userMapper.queryPmsByUserId(user.getUserId());
            simpleAuthorizationInfo.addStringPermissions(permissions);
            return simpleAuthorizationInfo;
        }
        return null;
    }

    /**
     * 认证
     *
     * @param token 令牌
     * @return 认证信息
     */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
        UsernamePasswordToken usernamePasswordToken = (UsernamePasswordToken) token;
        String username = usernamePasswordToken.getUsername();
        String password = new String(usernamePasswordToken.getPassword());
        if (StringUtils.isNotBlank(username) && StringUtils.isNotBlank(password)) {
            User u = new User();
            u.setLogonName(username);
            User user = userMapper.selectOne(u);
            if (user == null) {
                throw new UnknownAccountException();
            }
            if (user.getIsDelete().equals("1")) {
                throw new DisabledAccountException();
            }
            if (!password.equals(user.getLogonPwd())) {
                throw new IncorrectCredentialsException();
            }
            return new SimpleAuthenticationInfo(new Principal(user.getUserId(), username, user), password, getName());
        }
        throw new UnknownAccountException();
    }

    @Override
    public boolean isAuthorizationCachingEnabled() {
        return false;
    }
}
