package com.techelevator.dao;

import com.techelevator.tenmo.dao.JdbcAccountDao;
import com.techelevator.tenmo.model.Account;
import com.techelevator.tenmo.model.Transfer;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.jdbc.core.JdbcTemplate;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.Assert.*;

public class JdbcAccountDaoTest extends BaseDaoTests{
    private static final Account ACCOUNT_1 = new Account(1001, 2001, new BigDecimal("1000.00"));
    private static final Account ACCOUNT_2 = new Account(1002, 2002, new BigDecimal("1000.00"));
    private static final Account ACCOUNT_3 = new Account(1003, 2003, new BigDecimal("50.00"));
    private static final Account ACCOUNT_4 = new Account(1004, 2004, new BigDecimal("5000.00"));


    private Account testAccount;

    private JdbcAccountDao sut;


    @Before
    public void setup() {
        JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
        sut = new JdbcAccountDao(dataSource);
        testAccount = new Account(1001, 2001, new BigDecimal("1000.00"));
    }

    @Test
    public void getBalance() {
        BigDecimal retrieveBalance = sut.getBalance(1001);
        Assert.assertEquals(new BigDecimal("1000.00"), retrieveBalance);
    }

    @Test
    public void transferFunds() {
        Account account = sut.getAnAccountByUserId(21);
        account.setBalance(new BigDecimal("1200.00"));

        sut.transferFunds(new BigDecimal("200.00"), 1001);

        Account updatedAccount = sut.getAnAccountByUserId(1001);
        assertAccountsMatch(account,updatedAccount);
    }


    @Test
    public void getAnAccountByUserId() {
        Account account2 = sut.getAnAccountByUserId(1001);
        Assert.assertNotNull(account2);

        assertAccountsMatch(ACCOUNT_2, account2);


        Account account1 = sut.getAnAccountByUserId(1001);
        Assert.assertNotNull(account1);

        assertAccountsMatch(ACCOUNT_1, account1);

        Account account = sut.getAnAccountByUserId(21);
        account.setBalance(new BigDecimal("800.00"));

        sut.transferFunds();

        Account updatedAccount = sut.getAnAccountByUserId(1002);

        assertAccountsMatch(account,updatedAccount);
    }

    @Test
    public void getAccountById(){}
    private void assertAccountsMatch(Account expected, Account actual) {
        Assert.assertEquals(expected.getAccountId(), actual.getAccountId());
        Assert.assertEquals(expected.getBalance(), actual.getBalance());
    }
}