package seedu.addressbook.logic;

import seedu.addressbook.commands.Command;
import seedu.addressbook.commands.CommandResult;
import seedu.addressbook.data.AddressBook;
import seedu.addressbook.data.person.ReadOnlyPerson;
import seedu.addressbook.parser.Parser;
import seedu.addressbook.storage.StorageFile;
import seedu.addressbook.storage.Storage;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

/**
 * Represents the main Logic of the AddressBook.
 */
public class Logic {


    private LinkedList<Storage> storageList = new LinkedList<Storage> ();
    private AddressBook addressBook;
    private int storageIndex = storageList.size();

    /** The list of person shown to the user most recently.  */
    private List<? extends ReadOnlyPerson> lastShownList = Collections.emptyList();

    public Logic() throws Exception{
        addStorage(initializeStorage());
        setAddressBook(storageList.getLast().load());
    }

    Logic(Storage storageFile, AddressBook addressBook){
        addStorage(storageFile);
        setAddressBook(addressBook);
    }

    public void setStorageIndex(int index) {
    	this.storageIndex = index;
    }

    void addStorage(Storage storage){
        this.storageList.add(storage);
    }

    void setAddressBook(AddressBook addressBook){
        this.addressBook = addressBook;
    }

    /**
     * Creates the Storage object based on the user specified path (if any) or the default storage path.
     * @throws Storage.InvalidStorageFilePathException if the target file path is incorrect.
     */
    private Storage initializeStorage() throws Storage.InvalidStorageFilePathException {
        Storage storage = new StorageFile();
        return storage;
    }

    public String getStorageFilePath() {
        return storageList.get(storageIndex - 1).getPath();
    }

    /**
     * Unmodifiable view of the current last shown list.
     */
    public List<ReadOnlyPerson> getLastShownList() {
        return Collections.unmodifiableList(lastShownList);
    }

    protected void setLastShownList(List<? extends ReadOnlyPerson> newList) {
        lastShownList = newList;
    }

    /**
     * Parses the user command, executes it, and returns the result.
     * @throws Exception if there was any problem during command execution.
     */
    public CommandResult execute(String userCommandText) throws Exception {
        Command command = new Parser().parseCommand(userCommandText);
        CommandResult result = execute(command);
        recordResult(result);
        return result;
    }

    /**
     * Executes the command, updates storage, and returns the result.
     *
     * @param command user command
     * @return result of the command
     * @throws Exception if there was any problem during command execution.
     */
    private CommandResult execute(Command command) throws Exception {
        command.setData(addressBook, lastShownList);
        CommandResult result = command.execute();
        storageList.get(storageIndex - 1).save(addressBook);
        return result;
    }

    /** Updates the {@link #lastShownList} if the result contains a list of Persons. */
    private void recordResult(CommandResult result) {
        final Optional<List<? extends ReadOnlyPerson>> personList = result.getRelevantPersons();
        if (personList.isPresent()) {
            lastShownList = personList.get();
        }
    }
}
