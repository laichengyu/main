package seedu.address.model.coin;

import static seedu.address.commons.util.CollectionUtil.requireAllNonNull;

import java.util.Collections;
import java.util.Objects;
import java.util.Set;

import seedu.address.model.tag.Tag;
import seedu.address.model.tag.UniqueTagList;

/**
 * Represents a Coin in the address book.
 * Guarantees: details are present and not null, field values are validated, immutable.
 */
public class Coin {

    private final Code code;

    private final Amount totalAmountSold;
    private final Amount totalAmountBought;
    private final Amount totalDollarsSold;
    private final Amount totalDollarsBought;
    private final Price price;

    private final UniqueTagList tags;

    /**
     * Every field must be present and not null.
     */
    public Coin(Code code, Set<Tag> tags) {
        requireAllNonNull(code, tags);
        this.code = code;
        // protect internal tags from changes in the arg list
        this.tags = new UniqueTagList(tags);
        this.price = new Price();
        this.totalAmountSold = new Amount();
        this.totalAmountBought = new Amount();
        this.totalDollarsSold = new Amount();
        this.totalDollarsBought = new Amount();
    }

    /**
     * Copy constructor for coins.
     */
    public Coin(Coin toCopy) {
        requireAllNonNull(toCopy);
        this.code = toCopy.code;
        // protect internal tags from changes in the arg list
        this.tags = new UniqueTagList(toCopy.getTags());
        this.price = toCopy.price;
        this.totalAmountSold = new Amount(toCopy.getTotalAmountSold());
        this.totalAmountBought = new Amount(toCopy.getTotalAmountBought());
        this.totalDollarsSold = new Amount(toCopy.getTotalDollarsSold());
        this.totalDollarsBought = new Amount(toCopy.getTotalDollarsBought());
    }

    //@@author laichengyu
    /**
     * Copy constructor with price update.
     */
    public Coin(Coin toCopy, double newPrice) {
        requireAllNonNull(toCopy);
        this.code = toCopy.code;
        // protect internal tags from changes in the arg list
        this.tags = new UniqueTagList(toCopy.getTags());
        this.price = new Price(newPrice);
        this.totalAmountSold = new Amount(toCopy.getTotalAmountSold());
        this.totalAmountBought = new Amount(toCopy.getTotalAmountBought());
        this.totalDollarsSold = new Amount(toCopy.getTotalDollarsSold());
        this.totalDollarsBought = new Amount(toCopy.getTotalDollarsBought());
    }
    //@@author

    /**
     * Copy constructor with tag update.
     */
    public Coin(Coin toCopy, Set<Tag> tags) {
        requireAllNonNull(toCopy);
        this.code = toCopy.code;
        // protect internal tags from changes in the arg list
        this.tags = new UniqueTagList(tags);
        this.price = toCopy.price;
        this.totalAmountSold = new Amount(toCopy.getTotalAmountSold());
        this.totalAmountBought = new Amount(toCopy.getTotalAmountBought());
        this.totalDollarsSold = new Amount(toCopy.getTotalDollarsSold());
        this.totalDollarsBought = new Amount(toCopy.getTotalDollarsBought());
    }

    public Code getCode() {
        return code;
    }

    public Amount getCurrentAmountHeld() {
        return new Amount(totalAmountBought.getValue() - totalAmountSold.getValue());
    }

    public Price getPrice() {
        return price;
    }

    /**
     * Returns an immutable tag set, which throws {@code UnsupportedOperationException}
     * if modification is attempted.
     */
    public Set<Tag> getTags() {
        return Collections.unmodifiableSet(tags.toSet());
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        if (!(other instanceof Coin)) {
            return false;
        }

        Coin otherCoin = (Coin) other;
        return otherCoin.getCode().equals(this.getCode())
                && otherCoin.getCurrentAmountHeld().equals(this.getCurrentAmountHeld())
                && otherCoin.getPrice().equals(this.getPrice());
    }

    @Override
    public int hashCode() {
        // use this method for custom fields hashing instead of implementing your own
        return Objects.hash(code, tags, getCurrentAmountHeld(), price);
    }

    @Override
    public String toString() {
        final StringBuilder builder = new StringBuilder();
        builder.append(getCode())
                .append(" Amount: ")
                .append(getCurrentAmountHeld())
                .append(" Price: ")
                .append(getPrice())
                .append(" Tags: ");
        getTags().forEach(builder::append);
        return builder.toString();
    }

    public Amount getTotalAmountSold() {
        return totalAmountSold;
    }

    public void addTotalAmountSold(Double addAmount) {
        this.totalAmountSold.addValue(addAmount);
    }

    public Amount getTotalAmountBought() {
        return totalAmountBought;
    }

    public void addTotalAmountBought(Double addAmount) {
        this.totalAmountBought.addValue(addAmount);
    }

    public Double getTotalProfit() {
        return totalAmountSold.getValue() - totalAmountBought.getValue();
    }

    public Double getDollarsWorth() {
        return price.getValue() * getCurrentAmountHeld().getValue();
    }

    public Double getProfitability() {
        return getDollarsWorth() + ((getProfitability() > 0) ? 0 : getProfitability());
    }

    public Amount getTotalDollarsSold() {
        return totalDollarsSold;
    }

    public Amount getTotalDollarsBought() {
        return totalDollarsBought;
    }
}
