package com.web.service.impl;

import com.web.dao.UserDao;
import com.web.model.User;
import com.web.service.UserService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;


@Service
@Transactional(rollbackFor = Exception.class)
public class UserServiceImpl implements UserService {

    @Resource
    private UserDao userDao;


    public List<User> getAllUser() {
        return userDao.selectAllUser();
    }
}
