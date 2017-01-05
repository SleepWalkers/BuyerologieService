package com.buyerologie.user.imp;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.buyerologie.user.AuthorityService;
import com.buyerologie.user.UserService;
import com.buyerologie.user.dao.AuthorityDao;
import com.buyerologie.user.exception.UserException;
import com.buyerologie.user.model.Authority;
import com.buyerologie.user.model.User;
import com.buyerologie.utils.StringUtil;
import com.buyerologie.utils.UserAuthUtil;

@Service(value = "authorityService")
public class AuthorityServiceImp implements AuthorityService {

    @Resource
    private AuthorityDao authorityDao;

    @Resource
    private UserService  userService;

    @Override
    public List<Authority> get() {
        return authorityDao.selectAll();
    }

    @Override
    public List<String> getByUserId(int userId) {
        if (userId <= 0) {
            return null;
        }
        User user = userService.getUser(userId);
        if (user == null) {
            return null;
        }

        return UserAuthUtil.getAuthList(user.getAuth());
    }

    @Override
    public Authority getAuthority(int authorityId) {
        return authorityDao.selectById(authorityId);
    }

    @Override
    public void grant(int userId, String authorityEnName) throws UserException {
        User user = userService.getUser(userId);
        if (user == null) {
            return;
        }

        List<String> newAuthList = UserAuthUtil.addNewAuth(
            UserAuthUtil.getAuthList(user.getAuth()), authorityEnName);

        user.setAuth(StringUtil.buildStringListToString(newAuthList, ","));
        userService.edit(user);
    }

    @Override
    public void revoke(int userId, String authorityEnName) throws UserException {
        User user = userService.getUser(userId);
        if (user == null) {
            return;
        }

        List<String> newAuthList = UserAuthUtil.removeAuth(
            UserAuthUtil.getAuthList(user.getAuth()), authorityEnName);

        user.setAuth(StringUtil.buildStringListToString(newAuthList, ","));
        userService.edit(user);
    }

}
