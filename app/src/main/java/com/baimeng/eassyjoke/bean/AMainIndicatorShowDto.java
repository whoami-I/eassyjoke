package com.baimeng.eassyjoke.bean;

import java.io.Serializable;

/**
 * 烟气实时检测dto
 */
public class AMainIndicatorShowDto implements Serializable {

    public AMainIndicatorShowDto() {

    }

    /**
     * 编号
     */
    public Long dataId;
    /**
     * 名称
     */
    public String name;
    /**
     * 序列号
     */
    public Long serialNo;

    /**
     * 日指标值
     */
    public double dayIndicatorValue;

    /**
     * 月指标值
     */
    public double monthIndicatorValue;

    /**
     * 年指标值
     */
    public double yearIndicatorValue;

    @Override
    public String toString() {
        return "AMainIndicatorShowDto{" +
                "dataId=" + dataId +
                ", name='" + name + '\'' +
                ", serialNo=" + serialNo +
                ", dayIndicatorValue=" + dayIndicatorValue +
                ", monthIndicatorValue=" + monthIndicatorValue +
                ", yearIndicatorValue=" + yearIndicatorValue +
                '}';
    }
}
