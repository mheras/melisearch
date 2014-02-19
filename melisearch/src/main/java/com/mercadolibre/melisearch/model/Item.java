package com.mercadolibre.melisearch.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.net.URL;
import java.util.List;

/**
 * Created by Martin A. Heras on 05/02/14.
 */
public class Item implements Serializable {

    private String mId;
    private String mTitle;
    private String mSubtitle;
    private String mCurrencyId;
    private BigDecimal mPrice;
    private int mAvailableQuantity;
    private int mSoldQuantity;
    private URL mThumbnail;
    private List<ItemPicture> mPictures;

    public String getId() {
        return mId;
    }

    public void setId(String mId) {
        this.mId = mId;
    }

    public String getTitle() {
        return mTitle;
    }

    public void setTitle(String mTitle) {
        this.mTitle = mTitle;
    }

    public String getSubtitle() {
        return mSubtitle;
    }

    public void setSubtitle(String mSubtitle) {
        this.mSubtitle = mSubtitle;
    }

    public String getCurrencyId() {
        return mCurrencyId;
    }

    public void setCurrencyId(String mCurrencyId) {
        this.mCurrencyId = mCurrencyId;
    }

    public BigDecimal getPrice() {
        return mPrice;
    }

    public void setPrice(BigDecimal mPrice) {
        this.mPrice = mPrice;
    }

    public int getAvailableQuantity() {
        return mAvailableQuantity;
    }

    public void setAvailableQuantity(int mAvailableQuantity) {
        this.mAvailableQuantity = mAvailableQuantity;
    }

    public int getSoldQuantity() {
        return mSoldQuantity;
    }

    public void setSoldQuantity(int mSoldQuantity) {
        this.mSoldQuantity = mSoldQuantity;
    }

    public URL getThumbnail() {
        return mThumbnail;
    }

    public void setThumbnail(URL thumbnail) {
        this.mThumbnail = thumbnail;
    }

    public List<ItemPicture> getPictures() {
        return mPictures;
    }

    public void setPictures(List<ItemPicture> pictures) {
        this.mPictures = pictures;
    }

    @Override
    public String toString() {
        return mTitle;
    }
}
