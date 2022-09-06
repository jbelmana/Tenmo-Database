package com.techelevator.tenmo.exception;

import com.techelevator.tenmo.model.Transfer;

public class TransferFundsException extends Exception{
    public TransferFundsException (String errorMsg){
        super(errorMsg);
    }

}
