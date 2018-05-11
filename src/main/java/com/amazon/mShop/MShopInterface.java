package com.amazon.mShop;

public interface MShopInterface {
	public void mShopSearchForItem(String targetSearchItemNameStrg, String expectedSearchItemNameStrg, String expectedSearchedItemPrice);
	public void mShopLogin(String username, String password);
	public void mShopSearchForItem(String targetSearchItemNameStrg);
	public void mShopVerifySearchItem(String expectedSearchItemNameStrg);
	public void mShopClickSearchItem(String expectedSearchItemNameStrg);
	public void mShopVerifySearchItemPrice(String expectedSearchedItemPrice);

}

