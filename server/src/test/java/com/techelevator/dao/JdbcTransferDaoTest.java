package com.techelevator.dao;

import com.techelevator.tenmo.dao.JdbcTransferDao;
import com.techelevator.tenmo.model.Transfer;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.jdbc.core.JdbcTemplate;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.Assert.*;

public class JdbcTransferDaoTest extends BaseDaoTests {
    private static final Transfer TRANSFER_1 = new Transfer(3001, 2001, 2002, new BigDecimal("500.00"));
    private static final Transfer TRANSFER_2 = new Transfer(3002, 2002, 2003, new BigDecimal("50.00"));
    private static final Transfer TRANSFER_3 = new Transfer(3003, 2002, 2003, new BigDecimal("100.00"));
    private static final Transfer TRANSFER_4 = new Transfer(3004, 2001, 2003, new BigDecimal("500.00" ));
    private static final Transfer TRANSFER_5 = new Transfer(3005, 2005, 2004, new BigDecimal("500.00" ));

    private Transfer testTransfer;

    private JdbcTransferDao sut;


    @Before
    public void setup() {
        JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
        sut = new JdbcTransferDao(jdbcTemplate);
        testTransfer = new Transfer(3000, 2001, 2002, new BigDecimal("50.00"));
    }

    @Test
    public void getTransfersByTansferId_returns_list_of_all_transfers_as_requested() {
    Transfer transfers = sut.getTransferByTransferId(3001);
    assertTransferMatch(TRANSFER_1, transfers);

    transfers = sut.getTransferByTransferId(3002);
    assertTransferMatch(TRANSFER_2, transfers);
    }

    @Test
    public void newTransfer_returns_transfer_with_id_and_expected_values() {
        Transfer createdTransfer = sut.newTransfer(testTransfer);
        Integer newId = createdTransfer.getTransferId();
        Assert.assertTrue(newId > 0);
        testTransfer.setTransferId(newId);
        assertTransferMatch(testTransfer, createdTransfer);
    }


    @Test
    public void getListOfTransfers_Returns_List_Of_All_Transfers() {
        List<Transfer> transfers = sut.getListOfTransfers(2001);
        Assert.assertEquals(2, transfers.size());
        assertTransferMatch(TRANSFER_1, transfers.get(0));
        assertTransferMatch(TRANSFER_4, transfers.get(1));

        transfers = sut.getListOfTransfers(2002);
        Assert.assertEquals(2, transfers.size());
        assertTransferMatch(TRANSFER_2, transfers.get(0));
        assertTransferMatch(TRANSFER_3, transfers.get(1));
    }

    private void assertTransferMatch(Transfer expected, Transfer actual) {
//        Assert.assertEquals(expected.getTimesheetId(), actual.getTimesheetId());
//        Assert.assertEquals(expected.getEmployeeId(), actual.getEmployeeId());
//        Assert.assertEquals(expected.getProjectId(), actual.getProjectId());
        //Assert.assertEquals(expected.getTransferType(), actual.getTransferType());
        }
    }