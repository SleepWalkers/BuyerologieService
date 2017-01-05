package com.buyerologie.user;

import java.util.List;

import com.buyerologie.user.exception.UserException;
import com.buyerologie.user.model.Authority;

/**
 * 用户权限服务类
 * 
 * @author Administrator
 * @version $Id: AuthorityService.java, v 0.1 2015年12月10日 上午10:44:36 Administrator Exp $
 */
public interface AuthorityService {

    /**
     * 获得所有权限
     * @return
     */
    List<Authority> get();

    /**
     * 获得用户的所有权限
     * @param userId
     * @return
     */
    List<String> getByUserId(int userId);

    /**
     * 根据权限ID获取用户权限
     * @author duanyu
     * 下午5:44:51
     * @param authorityId
     * @return
     */
    Authority getAuthority(int authorityId);

    /**
     * 授予用户权限
     * @param userId
     * @param authorityGroupId
     * @return 
     * @throws UserException 
     */
    void grant(int userId, String authorityEnName) throws UserException;

    /**
     * 撤回用户权限
     * @author duanyu
     * 下午5:48:25
     * @param userId
     * @param authorityGroupId
     * @throws UserException 
     */
    void revoke(int userId, String authorityEnName) throws UserException;
}
