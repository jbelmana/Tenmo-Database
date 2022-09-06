package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.Account;
import com.techelevator.tenmo.model.Transfer;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Component
public class JdbcTransferDao implements TransferDao {

    private JdbcTemplate jdbcTemplate;
    private JdbcAccountDao jdbcAccountDao;

    public JdbcTransferDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Transfer getTransferByTransferId(int transferId) {
        String sql = "SELECT * FROM transfer WHERE transfer_id = ?";
        SqlRowSet result = jdbcTemplate.queryForRowSet(sql, transferId);
        Transfer transfer = null;
        if (result.next()) {
            transfer = mapToRowTransfer(result);
        } else {
            throw new RuntimeException("Transfer not found.");
        }
        return transfer;
    }

    @Override
    public Transfer newTransfer(Transfer transfer) {
        int userFrom = transfer.getUserFrom();
        int userTo = transfer.getUserTo();
        BigDecimal amount = transfer.getAmount();
//        BigDecimal senderBalance = jdbcAccountDao.getBalanceByAccountNumber(transfer.getUserFrom());
//        if (transfer.getUserFrom() != transfer.getUserTo() && senderBalance.compareTo(transfer.getAmount()) >= 0) {
        String sql = "INSERT INTO transfer (account_from, account_to, amount) "
                + "VALUES (?,?,?) RETURNING transfer_id";
        int id = jdbcTemplate.queryForObject(sql, Integer.class, userFrom, userTo, amount);
        return getTransferByTransferId(id);
    }

//        return null;
//    }
//        else {
//            String sql = "INSERT INTO transfer (account_from, account_to, amount, transfer_status) "
//                    + "VALUES (?,?,?,'Rejected') RETURNING transfer_id";
//            int id = jdbcTemplate.queryForObject(sql, Integer.class, userFrom, userTo, amount);
//            return getTransferByTransferId(id);
//        }


    @Override
    public List<Transfer> getListOfTransfers(int accountId) {
        List<Transfer> transferList = new ArrayList<>();
        String sql = "SELECT * FROM transfer JOIN account a on a.account_id = transfer.account_from" +
                " WHERE account_id = ?";
        SqlRowSet results = this.jdbcTemplate.queryForRowSet(sql, accountId);
        while (results.next()) {
            transferList.add(mapToRowTransfer(results));
        }
        return transferList;
    }


    private Transfer mapToRowTransfer(SqlRowSet result) {
        Transfer transfer = new Transfer();
        transfer.setTransferId(result.getInt("transfer_id"));
        transfer.setUserFrom(result.getInt("account_from"));
        transfer.setUserTo(result.getInt("account_to"));
        transfer.setAmount(result.getBigDecimal("amount"));
        return transfer;
    }
}
