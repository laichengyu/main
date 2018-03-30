package seedu.address.model;

import static org.junit.Assert.assertEquals;
import static seedu.address.testutil.TypicalCoins.ALICE;
import static seedu.address.testutil.TypicalCoins.getTypicalAddressBook;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import seedu.address.model.coin.Coin;
import seedu.address.model.tag.Tag;

public class CoinBookTest {

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    private final CoinBook addressBook = new CoinBook();

    @Test
    public void constructor() {
        assertEquals(Collections.emptyList(), addressBook.getCoinList());
        assertEquals(Collections.emptyList(), addressBook.getTagList());
    }

    @Test
    public void resetData_null_throwsNullPointerException() {
        thrown.expect(NullPointerException.class);
        addressBook.resetData(null);
    }

    @Test
    public void resetData_withValidReadOnlyAddressBook_replacesData() {
        CoinBook newData = getTypicalAddressBook();
        addressBook.resetData(newData);
        assertEquals(newData, addressBook);
    }

    @Test
    public void resetData_withDuplicateCoins_throwsAssertionError() {
        // Repeat ALICE twice
        List<Coin> newCoins = Arrays.asList(ALICE, ALICE);
        List<Tag> newTags = new ArrayList<>(ALICE.getTags());
        CoinBookStub newData = new CoinBookStub(newCoins, newTags);

        thrown.expect(AssertionError.class);
        addressBook.resetData(newData);
    }

    @Test
    public void getCoinList_modifyList_throwsUnsupportedOperationException() {
        thrown.expect(UnsupportedOperationException.class);
        addressBook.getCoinList().remove(0);
    }

    @Test
    public void getTagList_modifyList_throwsUnsupportedOperationException() {
        thrown.expect(UnsupportedOperationException.class);
        addressBook.getTagList().remove(0);
    }

    /**
     * A stub ReadOnlyCoinBook whose coins and tags lists can violate interface constraints.
     */
    private static class CoinBookStub implements ReadOnlyCoinBook {
        private final ObservableList<Coin> coins = FXCollections.observableArrayList();
        private final ObservableList<Tag> tags = FXCollections.observableArrayList();
        private final Set<String> codes = new HashSet<>();

        CoinBookStub(Collection<Coin> coins, Collection<? extends Tag> tags) {
            this.coins.setAll(coins);
            this.tags.setAll(tags);
            this.codes.addAll(coins.stream().map(coin -> coin.getCode().toString()).collect(Collectors.toSet()));
        }

        @Override
        public ObservableList<Coin> getCoinList() {
            return coins;
        }

        @Override
        public ObservableList<Tag> getTagList() {
            return tags;
        }

        //@@author laichengyu
        @Override
        public Set<String> getCodeList() {
            return codes;
        }
        //@@author
    }

}
