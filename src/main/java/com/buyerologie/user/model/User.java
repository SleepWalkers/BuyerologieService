package com.buyerologie.user.model;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;

import com.buyerologie.security.UserAuthCredentials;
import com.buyerologie.security.UserAuthority;

public class User implements UserAuthCredentials {
    /** @author Sleepwalker 2016年12月11日 下午9:31:50 */
    private static final long   serialVersionUID = -8302822722646489278L;

    private int                 id;

    private String              username;

    private String              password;

    private String              nickname;

    private String              avatar;

    private String              profession;

    private String              auth;

    private boolean             isEnable;

    private Timestamp           gmtCreated;

    private Timestamp           gmtModified;

    /** 权限 */
    private List<UserAuthority> userAuthorities  = new ArrayList<UserAuthority>();

    public User() {
        this.isEnable = true;
    }

    public User(String username, String password) {
        this.username = username;
        this.password = password;
        this.isEnable = true;
    }

    @Override
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Override
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    @Override
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getProfession() {
        return profession;
    }

    public void setProfession(String profession) {
        this.profession = profession;
    }

    public String getAuth() {
        return auth;
    }

    public void setAuth(String auth) {
        this.auth = auth;
        String[] authArr = auth.split(",");
        for (String loopAuth : authArr) {
            UserAuthority userAuthority = new UserAuthority(loopAuth);
            this.userAuthorities.add(userAuthority);
        }
    }

    public Timestamp getGmtCreated() {
        return gmtCreated;
    }

    public void setGmtCreated(Timestamp gmtCreated) {
        this.gmtCreated = gmtCreated;
    }

    public Timestamp getGmtModified() {
        return gmtModified;
    }

    public void setGmtModified(Timestamp gmtModified) {
        this.gmtModified = gmtModified;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return userAuthorities;
    }

    public List<UserAuthority> getUserAuthorities() {
        return userAuthorities;
    }

    public void setUserAuthorities(List<UserAuthority> userAuthorities) {
        this.userAuthorities = userAuthorities;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return getIsEnable();
    }

    public boolean getIsEnable() {
        return isEnable;
    }

    public void setIsEnable(boolean isEnable) {
        this.isEnable = isEnable;
    }
}