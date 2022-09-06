package com.techelevator.tenmo.controller;

import com.techelevator.tenmo.dao.AccountDao;
import com.techelevator.tenmo.dao.TransferDao;
import com.techelevator.tenmo.dao.UserDao;
import com.techelevator.tenmo.exception.TransferFundsException;
import com.techelevator.tenmo.model.Account;
import com.techelevator.tenmo.model.Transfer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.security.Principal;
import java.util.List;

@RestController
@PreAuthorize("isAuthenticated()")
public class TransferController {
    @Autowired
    UserDao userDao;
    @Autowired
    AccountDao accountDao;
    @Autowired
    TransferDao transferDao;

    @GetMapping("/transfer")
    public List<Transfer> getListOfTransfers(Principal principal) {
        String username = principal.getName();
        int userID = userDao.findIdByUsername(username);
        Account account = accountDao.getAnAccountByUserId(userID);
        int accountId = account.getAccountId();
        List<Transfer> transferList = transferDao.getListOfTransfers(accountId);
        return transferList;
    }

    @GetMapping("/transfer/{transferid}")
    public Transfer getTransferByTransferId(@PathVariable int transferid) {
        return transferDao.getTransferByTransferId(transferid);
    }

    @ResponseStatus(HttpStatus.CREATED)
    @RequestMapping(path = "/transfer/sendMoney", method = RequestMethod.POST)
    public Transfer transferFunds(@RequestBody Transfer transfer) throws TransferFundsException {
        Transfer postedTransfer = transferDao.newTransfer(transfer);
        accountDao.transferFunds(transfer);
        return postedTransfer;
    }
}
