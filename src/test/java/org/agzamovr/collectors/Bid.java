package org.agzamovr.collectors;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;

class Bid {
    final int num;
    final BigDecimal price;
    final Date shippingDate;
    final Integer experience;
    final Date sentDate;

    public Bid(int num, BigDecimal price, Date shippingDate, Integer experience, Date sentDate) {
        this.num = num;
        this.price = price;
        this.shippingDate = shippingDate;
        this.experience = experience;
        this.sentDate = sentDate;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public Date getShippingDate() {
        return shippingDate;
    }

    public Integer getExperience() {
        return experience;
    }

    public Date getSentDate() {
        return sentDate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Bid bid = (Bid) o;

        if (num != bid.num) return false;
        if (price != null ? !price.equals(bid.price) : bid.price != null) return false;
        if (shippingDate != null ? !shippingDate.equals(bid.shippingDate) : bid.shippingDate != null) return false;
        if (experience != null ? !experience.equals(bid.experience) : bid.experience != null) return false;
        return sentDate.equals(bid.sentDate);

    }

    @Override
    public int hashCode() {
        int result = num;
        result = 31 * result + (price != null ? price.hashCode() : 0);
        result = 31 * result + (shippingDate != null ? shippingDate.hashCode() : 0);
        result = 31 * result + (experience != null ? experience.hashCode() : 0);
        result = 31 * result + sentDate.hashCode();
        return result;
    }

    @Override
    public String toString() {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy");
        return "num: " + num + ", price: " + price +
                ", shipping date: " + simpleDateFormat.format(shippingDate) +
                ", sent date: " + simpleDateFormat.format(sentDate);
    }
}
