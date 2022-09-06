package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.exception.TransferFundsException;
import com.techelevator.tenmo.model.Account;
import com.techelevator.tenmo.model.Transfer;

import java.math.BigDecimal;

public interface AccountDao {

    BigDecimal getBalance(int userId);

    void transferFunds(Transfer transfer) throws TransferFundsException;

    Account getAnAccountByUserId(int userId);


    Account getAccountById(int id);
}
