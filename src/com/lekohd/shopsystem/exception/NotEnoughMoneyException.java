package com.lekohd.shopsystem.exception;

/**
 * Created by Leon on 02.06.2015.
 * Project ShopSystem
 * <p/>
 * Copyright (C) 2014 Leon167 { LekoHD
 */
public class NotEnoughMoneyException extends RuntimeException {

    public NotEnoughMoneyException()  //InvalidMaterialException (Costum items)
    {
    }

    public NotEnoughMoneyException(String ex)
    {
        super (ex);
    }

}
