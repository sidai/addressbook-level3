package seedu.addressbook.commands;

import seedu.addressbook.common.Messages;
import seedu.addressbook.data.exception.IllegalValueException;
import seedu.addressbook.data.person.*;
import seedu.addressbook.data.person.UniquePersonList.PersonNotFoundException;


/**
 * Edits a person's information identified using it's last displayed index in the address book.
 */
public class EditCommand extends Command {

    public static final String COMMAND_WORD = "edit";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ":\n" + "Edits a person's information in address book.\n\t"
            + "Example: " + COMMAND_WORD + " 1" + " phone" + " 12345678";

    public static final String MESSAGE_SUCCESS = "Edited person's data successfully";

    private final String toEdit;
    private final String editedContent;
    private boolean defaultPrivite = false;

    public EditCommand(int targetVisibleIndex, String toEdit, String editedContent) {
        super(targetVisibleIndex);
        this.toEdit = toEdit;
        this.editedContent = editedContent;
    }

    public String getEdit() {
        return toEdit;
    }

    @Override
    public CommandResult execute() {
        try {
            final ReadOnlyPerson target = getTargetPerson();
            ReadOnlyPerson toUpdate = target;
            if(toEdit.equalsIgnoreCase("name")) {
                target.setName(editedContent);
            }else if(toEdit.equalsIgnoreCase("address")) {
                target.setAddress(editedContent, defaultPrivite);
            }else if (toEdit.equalsIgnoreCase("phone")) {
                target.setPhone(editedContent, defaultPrivite);
            }else if(toEdit.equalsIgnoreCase("email")) {
                target.setEmail(editedContent, defaultPrivite);
            }else {
            	return new CommandResult("Incorrect input, only name, address, phone and email can be modified");
            }

            Person newPerson = new Person(target.getName(),
                                          target.getPhone(),
                                          target.getEmail(),
                                          target.getAddress(),
                                          target.getTags());
            addressBook.removePerson(toUpdate);
            addressBook.addPerson(newPerson);
            return new CommandResult(String.format(MESSAGE_SUCCESS));
        }catch (IndexOutOfBoundsException ie) {
            return new CommandResult(Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);
        }catch (PersonNotFoundException pnfe) {
            return new CommandResult(Messages.MESSAGE_PERSON_NOT_IN_ADDRESSBOOK);
        }catch (IllegalValueException ive) {
            return new CommandResult("Invalid input");
        }
    }

}
