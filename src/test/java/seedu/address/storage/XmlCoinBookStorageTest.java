package seedu.address.storage;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static seedu.address.testutil.TypicalCoins.ALICE;
import static seedu.address.testutil.TypicalCoins.HOON;
import static seedu.address.testutil.TypicalCoins.IDA;
import static seedu.address.testutil.TypicalCoins.getTypicalAddressBook;

import java.io.IOException;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.rules.TemporaryFolder;

import seedu.address.commons.exceptions.DataConversionException;
import seedu.address.commons.util.FileUtil;
import seedu.address.model.CoinBook;
import seedu.address.model.ReadOnlyCoinBook;

public class XmlCoinBookStorageTest {
    private static final String TEST_DATA_FOLDER = FileUtil.getPath("./src/test/data/XmlAddressBookStorageTest/");

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Rule
    public TemporaryFolder testFolder = new TemporaryFolder();

    @Test
    public void readAddressBook_nullFilePath_throwsNullPointerException() throws Exception {
        thrown.expect(NullPointerException.class);
        readAddressBook(null);
    }

    private java.util.Optional<ReadOnlyCoinBook> readAddressBook(String filePath) throws Exception {
        return new XmlCoinBookStorage(filePath).readCoinBook(addToTestDataPathIfNotNull(filePath));
    }

    private String addToTestDataPathIfNotNull(String prefsFileInTestDataFolder) {
        return prefsFileInTestDataFolder != null
                ? TEST_DATA_FOLDER + prefsFileInTestDataFolder
                : null;
    }

    @Test
    public void read_missingFile_emptyResult() throws Exception {
        assertFalse(readAddressBook("NonExistentFile.xml").isPresent());
    }

    @Test
    public void read_notXmlFormat_exceptionThrown() throws Exception {

        thrown.expect(DataConversionException.class);
        readAddressBook("NotXmlFormatAddressBook.xml");

        /* IMPORTANT: Any code below an exception-throwing line (like the one above) will be ignored.
         * That means you should not have more than one exception test in one method
         */
    }

    @Test
    public void readAddressBook_invalidCoinAddressBook_throwDataConversionException() throws Exception {
        thrown.expect(DataConversionException.class);
        readAddressBook("invalidCoinAddressBook.xml");
    }

    @Test
    public void readAddressBook_invalidAndValidCoinAddressBook_throwDataConversionException() throws Exception {
        thrown.expect(DataConversionException.class);
        readAddressBook("invalidAndValidCoinAddressBook.xml");
    }

    @Test
    public void readAndSaveAddressBook_allInOrder_success() throws Exception {
        String filePath = testFolder.getRoot().getPath() + "TempAddressBook.xml";
        CoinBook original = getTypicalAddressBook();
        XmlCoinBookStorage xmlAddressBookStorage = new XmlCoinBookStorage(filePath);

        //Save in new file and read back
        xmlAddressBookStorage.saveCoinBook(original, filePath);
        ReadOnlyCoinBook readBack = xmlAddressBookStorage.readCoinBook(filePath).get();
        assertEquals(original, new CoinBook(readBack));

        //Modify data, overwrite exiting file, and read back
        original.addCoin(HOON);
        original.removeCoin(ALICE);
        xmlAddressBookStorage.saveCoinBook(original, filePath);
        readBack = xmlAddressBookStorage.readCoinBook(filePath).get();
        assertEquals(original, new CoinBook(readBack));

        //Save and read without specifying file path
        original.addCoin(IDA);
        xmlAddressBookStorage.saveCoinBook(original); //file path not specified
        readBack = xmlAddressBookStorage.readCoinBook().get(); //file path not specified
        assertEquals(original, new CoinBook(readBack));

    }

    @Test
    public void saveAddressBook_nullAddressBook_throwsNullPointerException() {
        thrown.expect(NullPointerException.class);
        saveAddressBook(null, "SomeFile.xml");
    }

    /**
     * Saves {@code addressBook} at the specified {@code filePath}.
     */
    private void saveAddressBook(ReadOnlyCoinBook addressBook, String filePath) {
        try {
            new XmlCoinBookStorage(filePath).saveCoinBook(addressBook, addToTestDataPathIfNotNull(filePath));
        } catch (IOException ioe) {
            throw new AssertionError("There should not be an error writing to the file.", ioe);
        }
    }

    @Test
    public void saveAddressBook_nullFilePath_throwsNullPointerException() throws IOException {
        thrown.expect(NullPointerException.class);
        saveAddressBook(new CoinBook(), null);
    }


}
