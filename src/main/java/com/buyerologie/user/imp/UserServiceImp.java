package com.buyerologie.user.imp;

import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.springframework.security.authentication.encoding.Md5PasswordEncoder;
import org.springframework.stereotype.Service;

import com.buyerologie.enums.Punctuation;
import com.buyerologie.security.SecurityService;
import com.buyerologie.security.UserAuthCredentials;
import com.buyerologie.user.UserService;
import com.buyerologie.user.dao.UserDao;
import com.buyerologie.user.exception.UserException;
import com.buyerologie.user.exception.UserExistException;
import com.buyerologie.user.exception.UserNotFoundException;
import com.buyerologie.user.exception.UserPasswordBlankException;
import com.buyerologie.user.model.User;
import com.buyerologie.utils.StringUtil;

@Service(value = "userService")
public class UserServiceImp implements UserService {

    @Resource
    private UserDao             userDao;

    @Resource
    private SecurityService     securityService;

    private static final String DEFAULT_AVATAR = "/image/default-avatar.png";

    @Override
    public void modifyUserAuth(int userId, List<String> authList) {
        User user = userDao.selectById(userId);
        if (user == null) {
            return;
        }

        user.setAuth(StringUtil.buildStringListToString(authList, Punctuation.DOT.toString()));
        userDao.updateById(user);
    }

    @Override
    public boolean foundUser(String username) {
        if (StringUtils.isBlank(username)) {
            return false;
        }
        User user = userDao.selectByUsername(username);

        if (user == null) {
            return false;
        }
        return true;
    }

    @Override
    public User getUser(int userId) {
        if (userId < 0) {
            return null;
        }

        return userDao.selectById(userId);
    }

    @Override
    public User getUser(String username) {
        if (StringUtils.isBlank(username)) {
            return null;
        }
        return userDao.selectByUsername(username);
    }

    @Override
    public void register(User user) throws UserException {
        if (user == null) {
            return;
        }
        if (this.foundUser(user.getUsername())) {
            throw new UserExistException();
        }

        if (StringUtils.isBlank(user.getPassword())) {
            throw new UserPasswordBlankException();
        }
        user.setPassword(md5Encode(user.getPassword()));
        user.setAuth("ROLE_USER");
        user.setAvatar(DEFAULT_AVATAR);
        userDao.insert(user);
    }

    private String md5Encode(String password) {
        Md5PasswordEncoder m = new Md5PasswordEncoder();
        return m.encodePassword(password, null);
    }

    private static String md5Encode2(String password) {
        Md5PasswordEncoder m = new Md5PasswordEncoder();
        return m.encodePassword(password, null);
    }

    public static void main(String[] args) {
        System.out.println(md5Encode2("19900718"));
    }

    @Override
    public void edit(User user) throws UserException {
        if (user == null) {
            return;
        }
        User originalUser = userDao.selectById(user.getId());
        if (originalUser == null) {
            return;
        }

        if (StringUtils.isBlank(user.getPassword())) {
            throw new UserPasswordBlankException();
        }
        userDao.updateById(user);
    }

    @Override
    public User getCurrentUser() {
        UserAuthCredentials userAuthCredentials = securityService.getCurrentUser();
        if (userAuthCredentials == null) {
            return null;
        }
        return getUser(userAuthCredentials.getId());
    }

    @Override
    public void setPassword(int userId, String password) throws UserException {
        if (userId < 0 || StringUtils.isBlank(password)) {
            return;
        }
        User user = getUser(userId);
        if (user == null) {
            throw new UserNotFoundException();
        }
        user.setPassword(md5Encode(password));
        userDao.updateById(user);
    }

}
