package com.zhs.zhs.entity;

/**
 * Created by Administrator on 2017/5/3 0003.
 */

public class ShoppingCart extends Entity{

    private String productId;

    private String productName;

    private int productImage;

    private double productPrice;  //单个商品的价格

    private String productType;

    private int productNum;

    /** 是否被选中 */
    private boolean isChecked;

    /** 是否是编辑状态 */
    private boolean isEditing;

    public ShoppingCart(String productId,String productName,int productImage,double productPrice,String productType,int productNum,boolean isChecked,boolean isEditing){
        this.productId = productId;
        this.productName = productName;
        this.productImage = productImage;
        this.productPrice = productPrice;
        this.productType = productType;
        this.productNum = productNum;
        this.isChecked = isChecked;
        this.isEditing = isEditing;
    }
    public ShoppingCart(){}


    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public int getProductImage() {
        return productImage;
    }

    public void setProductImage(int productImage) {
        this.productImage = productImage;
    }

    public double getProductPrice() {
        return productPrice;
    }

    public void setProductPrice(double productPrice) {
        this.productPrice = productPrice;
    }

    public String getProductType() {
        return productType;
    }

    public void setProductType(String productType) {
        this.productType = productType;
    }

    public int getProductNum() {
        return productNum;
    }

    public void setProductNum(int productNum) {
        this.productNum = productNum;
    }

    public boolean isChecked() {
        return isChecked;
    }

    public void setChecked(boolean checked) {
        isChecked = checked;
    }

    public boolean isEditing() {
        return isEditing;
    }

    public void setEditing(boolean editing) {
        isEditing = editing;
    }
}
