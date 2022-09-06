package com.techelevator.tenmo.controller;

import com.techelevator.tenmo.dao.AccountDao;
import com.techelevator.tenmo.dao.UserDao;
import com.techelevator.tenmo.model.Account;
import com.techelevator.tenmo.model.Transfer;
import com.techelevator.tenmo.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.object.SqlCall;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.security.Principal;
import java.util.List;

@RestController
@PreAuthorize("isAuthenticated()")

public class ApplicationController {
    private AccountDao accountDao;
    private UserDao userDao;

    public ApplicationController(AccountDao accountDao, UserDao userDao) {
        this.accountDao = accountDao;
        this.userDao = userDao;
    }

    @GetMapping("/account/balance")
    public BigDecimal getBalance(Principal principal) {
        int userId = getCurrentUserId(principal);
        BigDecimal balance = accountDao.getAnAccountByUserId(userId).getBalance();
        return balance;
    }

    public int getCurrentUserId(Principal principal) {
        return userDao.findByUsername(principal.getName()).getId();
    }

    @GetMapping("/tenmo_user/username")
    public List<User> listUsers() {
        List<User> users = userDao.findAll();
        return users;
    }


}
