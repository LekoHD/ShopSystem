package com.lekohd.shopsystem.exception;

/**
 * Created by Leon on 31.05.2015.
 * Project ShopSystem
 * <p/>
 * Copyright (C) 2014 Leon167 { LekoHD
 */
public class ItemBuyException extends RuntimeException {

    public ItemBuyException()  //InvalidMaterialException (Costum items)
    {
    }

    public ItemBuyException(String ex)
    {
        super (ex);
    }

}

