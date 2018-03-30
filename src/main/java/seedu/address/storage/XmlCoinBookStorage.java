package seedu.address.storage;

import static java.util.Objects.requireNonNull;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Optional;
import java.util.logging.Logger;

import seedu.address.commons.core.LogsCenter;
import seedu.address.commons.exceptions.DataConversionException;
import seedu.address.commons.exceptions.IllegalValueException;
import seedu.address.commons.util.FileUtil;
import seedu.address.model.ReadOnlyCoinBook;

/**
 * A class to access CoinBook data stored as an xml file on the hard disk.
 */
public class XmlCoinBookStorage implements CoinBookStorage {

    private static final Logger logger = LogsCenter.getLogger(XmlCoinBookStorage.class);

    private static final String backupFilePath = "data/backup.xml";

    private String filePath;

    public XmlCoinBookStorage(String filePath) {
        this.filePath = filePath;
    }

    public String getCoinBookFilePath() {
        return filePath;
    }

    @Override
    public Optional<ReadOnlyCoinBook> readCoinBook() throws DataConversionException, IOException {
        return readCoinBook(filePath);
    }

    /**
     * Similar to {@link #readCoinBook()}
     * @param filePath location of the data. Cannot be null
     * @throws DataConversionException if the file is not in the correct format.
     */
    public Optional<ReadOnlyCoinBook> readCoinBook(String filePath) throws DataConversionException,
                                                                                 FileNotFoundException {
        requireNonNull(filePath);

        File addressBookFile = new File(filePath);

        if (!addressBookFile.exists()) {
            logger.info("CoinBook file "  + addressBookFile + " not found");
            return Optional.empty();
        }

        XmlSerializableCoinBook xmlAddressBook = XmlFileStorage.loadDataFromSaveFile(new File(filePath));
        try {
            return Optional.of(xmlAddressBook.toModelType());
        } catch (IllegalValueException ive) {
            logger.info("Illegal values found in " + addressBookFile + ": " + ive.getMessage());
            throw new DataConversionException(ive);
        }
    }

    @Override
    public void saveCoinBook(ReadOnlyCoinBook addressBook) throws IOException {
        saveCoinBook(addressBook, filePath);
    }

    /**
     * Similar to {@link #saveCoinBook(ReadOnlyCoinBook)}
     * @param filePath location of the data. Cannot be null
     */
    public void saveCoinBook(ReadOnlyCoinBook addressBook, String filePath) throws IOException {
        requireNonNull(addressBook);
        requireNonNull(filePath);

        File file = new File(filePath);
        FileUtil.createIfMissing(file);
        XmlFileStorage.saveDataToFile(file, new XmlSerializableCoinBook(addressBook));
    }

    //@@author laichengyu
    @Override
    public void backupCoinBook(ReadOnlyCoinBook addressBook) throws IOException {
        saveCoinBook(addressBook, backupFilePath);
    }
    //@@author

}
