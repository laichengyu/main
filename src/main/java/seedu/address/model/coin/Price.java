package seedu.address.model.coin;

/**
 * Represents a Coin's price in the address book.
 */
public class Price {

    private Double value;

    /**
     * Constructs a {@code Price}.
     */
    public Price() {
        this.value = 0.0;
    }

    /**
     * Constructs a {@code Price} with given value.
     */
    public Price(double value) {
        this.value = value;
    }

    public Double getValue() {
        return value;
    }

    public void setValue(Double value) {
        this.value = value;
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof Price // instanceof handles nulls
                && this.value.equals(((Price) other).value)); // state check
    }

    public String toString() {
        return this.value.toString();
    }
}
