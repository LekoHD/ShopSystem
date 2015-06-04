package com.lekohd.shopsystem;

import java.util.List;

/**
 * Created by Leon on 04.06.2015.
 * Project ShopSystem
 * <p/>
 * Copyright (C) 2014 Leon167 { LekoHD
 */
public class ShopDataCollection {

    private List<ShopData> shops;

    public ShopDataCollection(List<ShopData> shops)
    {
        this.shops = shops;
    }

    public List<ShopData> getShops()
    {
        return shops;
    }

    public void setShops(List<ShopData> shops)
    {
        this.shops = shops;
    }

}
