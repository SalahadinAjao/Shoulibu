package org.example.shiro;

import org.apache.commons.lang.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.subject.Subject;
import org.example.Util.Constant;
import org.example.Util.cache.J2CacheTool;
import org.example.dao.SysMenuDao;
import org.example.dao.SysUserDao;
import org.example.entity.SysMenuEntity;
import org.example.entity.SysUserEntity;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.*;

/**
 * 用户认证realm
 * @Author: houlintao
 * @Date:2020/5/19 上午9:44
 * @email 437547058@qq.com
 * @Version 1.0
 */
public class UserRealm extends AuthorizingRealm {


    @Autowired
    private SysMenuDao sysMenuDao;
    @Autowired
    private SysUserDao sysUserDao;

    //获取授权信息，在权限验证时调用
    /**
     *@date: 2020/6/2 下午12:09
     *@param:
     *@return:
     *@Description:获取授权信息，获取当前用户的所有权限并返回
     */
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
        SysUserEntity user = (SysUserEntity) principalCollection.getPrimaryPrincipal();
        Long userId = user.getUserId();

        //当前用户对应权限列表
        List<String> permsList;
        //如果当前用户是系统管理员，有超级权限
        if (userId == Constant.SUPER_ADMIN){
            List<SysMenuEntity> menuEntities = sysMenuDao.queryList(new HashMap<String, Object>());
            int length = menuEntities.size();
            permsList = new ArrayList<>(length);

            //遍历集合
            for (SysMenuEntity menu : menuEntities){
                permsList.add(menu.getPerms());
            }
        }else {//如果当前用户不是超级管理员则直接通过对应的userId获取权限

            permsList = sysUserDao.queryALlPerms(userId);

        }
        /**使用一个set对象将list中的权限对象进行处理以便后续的
         * SimpleAuthorizationInfo对象进行处理。
         * SimpleAuthorizationInfo对象接受一个set参数
         **/
        HashSet<String> hashSet = new HashSet<>();
        if (permsList != null && permsList.size()!=0){
            for (String perm : permsList){
                if (StringUtils.isBlank(perm)){
                    continue;
                }
                /**
                 *在这里之所以要使用addAll方法而不是add方法主要是因为使用addAll方法
                 * 可以在出现相同的元素的时候由方法直接处理，而如果使用add方法在遇到
                 * 相同元素的时候会抛出异常；
                 */
                hashSet.addAll(Arrays.asList(perm.trim().split(",")));
            }
        }
        SimpleAuthorizationInfo simpleAuthorizationInfo = new SimpleAuthorizationInfo();
        simpleAuthorizationInfo.setStringPermissions(hashSet);
        return simpleAuthorizationInfo;
    }

    /**
     *@date: 2020/5/28 上午7:29
     *@param:SHiro框架内置的Token
     *@return:
     *@Description:在用户登录时调用的认证函数，封装了我们自定义的登录处理逻辑，获取
     * 用户认证信息
     */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {
        Object tokenPrincipal = authenticationToken.getPrincipal();
        //通过shiro内置的authenticationToken对象获取当前用户名和密码
        String username = (String) tokenPrincipal;
        String password = new String((char[]) authenticationToken.getCredentials());

        //获取用户实体对象
        SysUserEntity userEntity = sysUserDao.queryByName(username);

        if (userEntity == null){
            throw new UnknownAccountException("该系统用户不存在，请核对账号");
        }
        //如果用户实体存在，就验证密码
        if (!password.equals(userEntity.getPassword())){
            throw new IncorrectCredentialsException("账号或密码不匹配，请核对");
        }

        //密码和账号都对了就验证账号状态
        if (userEntity.getStatus() == 0){
            throw new LockedAccountException("账号已被锁定！");
        }

        /**
         * 账号密码和锁定状态都通过后就可以将此用户放入session中了,这里的
         * subject对象代表的就是当前登录系统的用户，可以是人，也可以是其他的
         * 东西
         */
        Subject subject = SecurityUtils.getSubject();
        Session subjectSession = subject.getSession(true);
        subjectSession.setAttribute(Constant.CURRENT_USER,userEntity);

        List<String> perms;

        /**系统管理员，有超级权限*/
        if (userEntity.getUserId() == Constant.SUPER_ADMIN){
            List<SysMenuEntity> sysMenuEntities = sysMenuDao.queryList(new HashMap<String, Object>());

            int length = sysMenuEntities.size();

            perms = new ArrayList<>(length);

            for (SysMenuEntity menu : sysMenuEntities){
                perms.add(menu.getPerms());
            }
        }else {/**不是超级用户则直接通过SysUserDao获取权限*/
              perms = sysUserDao.queryALlPerms(userEntity.getUserId());
        }

        /**
         *将userId和对应的权限放入缓存
         */
        J2CacheTool.put(Constant.PERMS_LIST+userEntity.getUserId(),perms);

        /**AuthenticationInfo对象中存储的是Subject的身份认证信息，Shiro会调用*/
        SimpleAuthenticationInfo simpleAuthenticationInfo = new SimpleAuthenticationInfo(userEntity, password, getName());

        return simpleAuthenticationInfo;
    }
}
