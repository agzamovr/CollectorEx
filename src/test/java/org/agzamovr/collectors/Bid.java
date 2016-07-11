package org.agzamovr.collectors;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import static java.lang.System.currentTimeMillis;
import static java.util.Arrays.asList;

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

    static List<Bid> getBids() {
        Date past = new Date(currentTimeMillis() - 1000);
        Date present = new Date();
        Date future = new Date(currentTimeMillis() + 1000);
        Bid bid1 = new Bid(1, BigDecimal.ONE, future, null, present);
        Bid bid2 = new Bid(2, new BigDecimal("2"), present, null, present);
        Bid bid3 = new Bid(3, new BigDecimal("2"), future, null, present);
        Bid bid4 = new Bid(4, new BigDecimal("3"), future, 2, present);
        Bid bid5 = new Bid(5, new BigDecimal("3"), future, 1, present);
        Bid bid6 = new Bid(6, new BigDecimal("4"), future, null, past);
        Bid bid7 = new Bid(7, new BigDecimal("4"), future, null, present);
        return asList(bid1, bid2, bid3, bid4, bid5, bid6, bid7);
    }
}
