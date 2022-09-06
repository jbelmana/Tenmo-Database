package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.exception.TransferFundsException;
import com.techelevator.tenmo.model.Account;
import com.techelevator.tenmo.model.Transfer;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.math.BigDecimal;

@Component
public class JdbcAccountDao implements AccountDao {
    private JdbcTemplate jdbcTemplate;

    public JdbcAccountDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }



    @Override
    public BigDecimal getBalance(int userId) {
        String sql = "SELECT balance FROM account where user_id = ?";
        SqlRowSet results = null;
        BigDecimal balance = null;
        try {
            results = jdbcTemplate.queryForRowSet(sql, userId);
            if (results.next()) {
                balance = results.getBigDecimal("balance");
            }
        } catch (DataAccessException e) {
            System.out.println("Error accessing data.");
        }
        return balance;
    }

    @Override
    public void transferFunds(Transfer transfer) throws TransferFundsException {
        BigDecimal senderBalance = getBalanceByAccountNumber(transfer.getUserFrom());
        if (transfer.getUserFrom() != transfer.getUserTo() && senderBalance.compareTo(transfer.getAmount()) >= 0) {
            String fromSql = "UPDATE account SET balance = balance - ? WHERE account_id = ?";
            jdbcTemplate.update(fromSql, transfer.getAmount(), transfer.getUserFrom());
            String toSql = "UPDATE account SET balance = balance + ? WHERE account_id = ?";
            jdbcTemplate.update(toSql, transfer.getAmount(), transfer.getUserTo());
        }else throw new TransferFundsException ("Error cannot complete transaction");
    }

    public BigDecimal getBalanceByAccountNumber(int accountId) {
        String sql = "SELECT balance FROM account WHERE account_id = ?";
        SqlRowSet results = null;
        BigDecimal balance = null;
        try {
            results = jdbcTemplate.queryForRowSet(sql, accountId);
            if (results.next()) {
                balance = results.getBigDecimal("balance");
            }
        } catch (DataAccessException e) {
            System.out.println("Error accessing data.");
        }
        return balance;
    }


    @Override
    public Account getAnAccountByUserId(int userId) {
        Account account = null;
        String sql = "SELECT * FROM account WHERE user_id = ?";
        try {
            SqlRowSet results = jdbcTemplate.queryForRowSet(sql, userId);
            while (results.next()) {
                account = mapToRowAccount(results);
            }
        } catch (DataAccessException e) {
            System.out.println("Error accessing data.");
        }
        return account;
    }

    @Override
    public Account getAccountById(int accountId) {
        Account account = null;
        String sql = "SELECT * FROM account WHERE account_id = ?";

        try {
            SqlRowSet results = jdbcTemplate.queryForRowSet(sql, accountId);
            while (results.next()) {
                account = mapToRowAccount(results);
            }
        } catch (DataAccessException e) {
            System.out.println("Error accessing data.");
        }
        return account;
    }

    private Account mapToRowAccount(SqlRowSet result) {
        Account account = new Account();
        account.setBalance(result.getBigDecimal("balance"));
        account.setAccountId(result.getInt("account_id"));
        account.setUserId(result.getInt("user_id"));
        return account;
    }


}
