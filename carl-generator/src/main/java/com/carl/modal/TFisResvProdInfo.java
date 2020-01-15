package com.carl.modal;

import java.math.BigDecimal;
import java.util.Date;

public class TFisResvProdInfo {
    private Long id;

    private String assetsNo;

    private Long bgAssetId;

    private String productName;

    private Date transBeginTime;

    private Date transEndTime;

    private Date offlinePayEndTime;

    private Date inceptionDate;

    private Date dueDate;

    private Integer productLimit;

    private BigDecimal minBuyAmt;

    private Byte status;

    private Byte productRank;

    private Byte whiteListType;

    private Integer whiteListGroupId;

    private Byte agreementType;

    private String agreementNo;

    private String agreementTemplateId;

    private Byte assignmentGroupStatus;

    private Byte contractMouldAuditStatus;

    private String contractMouldPath;

    private String contractMouldAuditRemarks;

    private String productProfitJson;

    private Date createTime;

    private Date modifyTime;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAssetsNo() {
        return assetsNo;
    }

    public void setAssetsNo(String assetsNo) {
        this.assetsNo = assetsNo == null ? null : assetsNo.trim();
    }

    public Long getBgAssetId() {
        return bgAssetId;
    }

    public void setBgAssetId(Long bgAssetId) {
        this.bgAssetId = bgAssetId;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName == null ? null : productName.trim();
    }

    public Date getTransBeginTime() {
        return transBeginTime;
    }

    public void setTransBeginTime(Date transBeginTime) {
        this.transBeginTime = transBeginTime;
    }

    public Date getTransEndTime() {
        return transEndTime;
    }

    public void setTransEndTime(Date transEndTime) {
        this.transEndTime = transEndTime;
    }

    public Date getOfflinePayEndTime() {
        return offlinePayEndTime;
    }

    public void setOfflinePayEndTime(Date offlinePayEndTime) {
        this.offlinePayEndTime = offlinePayEndTime;
    }

    public Date getInceptionDate() {
        return inceptionDate;
    }

    public void setInceptionDate(Date inceptionDate) {
        this.inceptionDate = inceptionDate;
    }

    public Date getDueDate() {
        return dueDate;
    }

    public void setDueDate(Date dueDate) {
        this.dueDate = dueDate;
    }

    public Integer getProductLimit() {
        return productLimit;
    }

    public void setProductLimit(Integer productLimit) {
        this.productLimit = productLimit;
    }

    public BigDecimal getMinBuyAmt() {
        return minBuyAmt;
    }

    public void setMinBuyAmt(BigDecimal minBuyAmt) {
        this.minBuyAmt = minBuyAmt;
    }

    public Byte getStatus() {
        return status;
    }

    public void setStatus(Byte status) {
        this.status = status;
    }

    public Byte getProductRank() {
        return productRank;
    }

    public void setProductRank(Byte productRank) {
        this.productRank = productRank;
    }

    public Byte getWhiteListType() {
        return whiteListType;
    }

    public void setWhiteListType(Byte whiteListType) {
        this.whiteListType = whiteListType;
    }

    public Integer getWhiteListGroupId() {
        return whiteListGroupId;
    }

    public void setWhiteListGroupId(Integer whiteListGroupId) {
        this.whiteListGroupId = whiteListGroupId;
    }

    public Byte getAgreementType() {
        return agreementType;
    }

    public void setAgreementType(Byte agreementType) {
        this.agreementType = agreementType;
    }

    public String getAgreementNo() {
        return agreementNo;
    }

    public void setAgreementNo(String agreementNo) {
        this.agreementNo = agreementNo == null ? null : agreementNo.trim();
    }

    public String getAgreementTemplateId() {
        return agreementTemplateId;
    }

    public void setAgreementTemplateId(String agreementTemplateId) {
        this.agreementTemplateId = agreementTemplateId == null ? null : agreementTemplateId.trim();
    }

    public Byte getAssignmentGroupStatus() {
        return assignmentGroupStatus;
    }

    public void setAssignmentGroupStatus(Byte assignmentGroupStatus) {
        this.assignmentGroupStatus = assignmentGroupStatus;
    }

    public Byte getContractMouldAuditStatus() {
        return contractMouldAuditStatus;
    }

    public void setContractMouldAuditStatus(Byte contractMouldAuditStatus) {
        this.contractMouldAuditStatus = contractMouldAuditStatus;
    }

    public String getContractMouldPath() {
        return contractMouldPath;
    }

    public void setContractMouldPath(String contractMouldPath) {
        this.contractMouldPath = contractMouldPath == null ? null : contractMouldPath.trim();
    }

    public String getContractMouldAuditRemarks() {
        return contractMouldAuditRemarks;
    }

    public void setContractMouldAuditRemarks(String contractMouldAuditRemarks) {
        this.contractMouldAuditRemarks = contractMouldAuditRemarks == null ? null : contractMouldAuditRemarks.trim();
    }

    public String getProductProfitJson() {
        return productProfitJson;
    }

    public void setProductProfitJson(String productProfitJson) {
        this.productProfitJson = productProfitJson == null ? null : productProfitJson.trim();
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getModifyTime() {
        return modifyTime;
    }

    public void setModifyTime(Date modifyTime) {
        this.modifyTime = modifyTime;
    }
}