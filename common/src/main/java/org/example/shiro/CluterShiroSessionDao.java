package org.example.shiro;

import org.apache.shiro.session.Session;
import org.apache.shiro.session.UnknownSessionException;
import org.apache.shiro.session.mgt.eis.AbstractSessionDAO;
import org.example.Util.Constant;
import org.example.Util.cache.J2CacheTool;
import com.google.common.collect.Sets;
import java.io.Serializable;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

/**
 * @Author: houlintao
 * @Date:2020/5/28 下午3:00
 * @email 437547058@qq.com
 * @Version 1.0
 *
 * 集群session管理类，这里使用Shiro提供的AbstractSessionDAO接口
 */
public class CluterShiroSessionDao extends AbstractSessionDAO {


    @Override
    protected Serializable doCreate(Session session) {
        Serializable sessionId = generateSessionId(session);
        assignSessionId(session,sessionId);

        final String key = Constant.SESSION_KEY+sessionId.toString();
        //将session放进缓存
        setShiroSession(key,session);

        return sessionId;
    }

    @Override
    protected Session doReadSession(Serializable sessionId) {

        if (sessionId==null){
            return null;
        }
        final String key = Constant.SESSION_KEY+sessionId.toString();
        /**约束集群从J2Cache缓存中读取session，提高读取速度*/
        return getShiroSession(key);
    }

    @Override
    public void update(Session session) throws UnknownSessionException {
        final String key = Constant.SESSION_KEY + session.getId().toString();
        setShiroSession(key, session);
    }

    @Override
    public void delete(Session session) {
        final String key = Constant.SESSION_KEY + session.getId().toString();
        J2CacheTool.delete(key);
    }

    @Override
    public Collection<Session> getActiveSessions() {

        Collection<String> keys = J2CacheTool.keys(Constant.SESSION_KEY + "*");
        /**这里使用google的Set类生成一个set*/
        Set<Session> hashSet = Sets.newHashSet();
        for (String key : keys){
            Session shiroSession = getShiroSession(key);
            if (shiroSession != null){
                hashSet.add(shiroSession);
            }
        }
        return hashSet;
    }

    /**
     *@date: 2020/5/28 下午3:06
     *@param:
     *@return:
     *@Description:将session和其对应的key放进缓存
     */
    private void setShiroSession(String key,Session session){
        J2CacheTool.put(key,session);
    }
    /**
     *@date: 2020/5/28 下午3:07
     *@param:
     *@return:
     *@Description:从J2Cache缓存中获取key对应的session对象
     */
    private Session getShiroSession(String key){
        return (Session) J2CacheTool.get(key);
    }
}
