---
layout: default.md
title: "Developer Guide"
pageNav: 3
---

# Avengers Assemble Developer Guide

<!-- * Table of Contents -->
<page-nav-print />

--------------------------------------------------------------------------------------------------------------------

## **Acknowledgements**

This project is based on the AddressBook-Level3 project created by the [SE-EDU initiative](https://se-education.org).

--------------------------------------------------------------------------------------------------------------------

## **Setting up, getting started**

Refer to the guide [_Setting up and getting started_](SettingUp.md).

--------------------------------------------------------------------------------------------------------------------

## **Design**

### Architecture

<puml src="diagrams/ArchitectureDiagram.puml" width="280" />

The ***Architecture Diagram*** given above explains the high-level design of the App.

Given below is a quick overview of main components and how they interact with each other.

**Main components of the architecture**

**`Main`** (consisting of classes [`Main`](https://github.com/se-edu/addressbook-level3/tree/master/src/main/java/seedu/address/Main.java) and [`MainApp`](https://github.com/se-edu/addressbook-level3/tree/master/src/main/java/seedu/address/MainApp.java)) is in charge of the app launch and shut down.
* At app launch, it initializes the other components in the correct sequence, and connects them up with each other.
* At shut down, it shuts down the other components and invokes cleanup methods where necessary.

The bulk of the app's work is done by the following four components:

* [**`UI`**](#ui-component): The UI of the App.
* [**`Logic`**](#logic-component): The command executor.
* [**`Model`**](#model-component): Holds the data of the App in memory.
* [**`Storage`**](#storage-component): Reads data from, and writes data to, the hard disk.

[**`Commons`**](#common-classes) represents a collection of classes used by multiple other components.

**How the architecture components interact with each other**

The *Sequence Diagram* below shows how the components interact with each other for the scenario where the user issues the command `delete 1`.

<puml src="diagrams/ArchitectureSequenceDiagram.puml" width="574" />

Each of the four main components (also shown in the diagram above),

* defines its *API* in an `interface` with the same name as the Component.
* implements its functionality using a concrete `{Component Name}Manager` class (which follows the corresponding API `interface` mentioned in the previous point.)

For example, the `Logic` component defines its API in the `Logic.java` interface and implements its functionality using the `LogicManager.java` class which follows the `Logic` interface. Other components interact with a given component through its interface rather than the concrete class (reason: to prevent outside component's being coupled to the implementation of a component), as illustrated in the (partial) class diagram below.

<puml src="diagrams/ComponentManagers.puml" width="300" />

The sections below give more details of each component.

### UI component

The **API** of this component is specified in [`Ui.java`](https://github.com/se-edu/addressbook-level3/tree/master/src/main/java/seedu/address/ui/Ui.java)

<puml src="diagrams/UiClassDiagram.puml" alt="Structure of the UI Component"/>

The UI consists of a `MainWindow` that is made up of parts e.g.`CommandBox`, `ResultDisplay`, `PersonListPanel`, `StatusBarFooter` etc. All these, including the `MainWindow`, inherit from the abstract `UiPart` class which captures the commonalities between classes that represent parts of the visible GUI.

The `UI` component uses the JavaFx UI framework. The layout of these UI parts are defined in matching `.fxml` files that are in the `src/main/resources/view` folder. For example, the layout of the [`MainWindow`](https://github.com/se-edu/addressbook-level3/tree/master/src/main/java/seedu/address/ui/MainWindow.java) is specified in [`MainWindow.fxml`](https://github.com/se-edu/addressbook-level3/tree/master/src/main/resources/view/MainWindow.fxml)

The `UI` component,

* executes user commands using the `Logic` component.
* listens for changes to `Model` data so that the UI can be updated with the modified data.
* keeps a reference to the `Logic` component, because the `UI` relies on the `Logic` to execute commands.
* depends on some classes in the `Model` component, as it displays `Person` object residing in the `Model`.

### Logic component

**API** : [`Logic.java`](https://github.com/se-edu/addressbook-level3/tree/master/src/main/java/seedu/address/logic/Logic.java)

Here's a (partial) class diagram of the `Logic` component:

<puml src="diagrams/LogicClassDiagram.puml" width="550"/>

How the `Logic` component works:

1. When `Logic` is called upon to execute a command, it is passed to an `AddressBookParser` object which in turn creates a parser that matches the command (e.g., `DeleteCommandParser`) and uses it to parse the command.
2. This results in a `Command` object (more precisely, an object of one of its subclasses e.g., `DeleteCommand`) which is executed by the `LogicManager`.
3. The command can communicate with the `Model` when it is executed (e.g. to delete a person).<br>
   Note that although this is shown as a single step in the diagram above (for simplicity), in the code it can take several interactions (between the command object and the `Model`) to achieve.
4. The result of the command execution is encapsulated as a `CommandResult` object which is returned back from `Logic`.

Here are the other classes in `Logic` (omitted from the class diagram above) that are used for parsing a user command:

<puml src="diagrams/ParserClasses.puml" width="600"/>

How the parsing works:
* When called upon to parse a user command, the `AddressBookParser` class creates an `XYZCommandParser` (`XYZ` is a placeholder for the specific command name e.g., `AddCommandParser`) which uses the other classes shown above to parse the user command and create a `XYZCommand` object (e.g., `AddCommand`) which the `AddressBookParser` returns back as a `Command` object.
* All `XYZCommandParser` classes (e.g., `AddCommandParser`, `DeleteCommandParser`, ...) inherit from the `Parser` interface so that they can be treated similarly where possible e.g, during testing.

The sequence diagram below illustrates the interactions within the `Logic` component, taking a simple `execute("delete 1")` API call as an example.

<puml src="diagrams/DeleteSequenceDiagram.puml" alt="Interactions Inside the Logic Component for the `delete 1` Command" />

**Note:** The lifeline for `DeleteCommandParser` should end at the destroy marker (X) but due to a limitation of PlantUML, the lifeline continues till the end of diagram.
</box>

The sequence diagram below illustrates a more in-depth view of the interactions within the 'Logic' component.
It takes a user input into the UI, `add n|Dohn Joe p|98765432 a|123 e|dohn@gm.com m|A1234567X s|S1 r|R1`, as an example.

<puml src="diagrams/AddSequenceDiagram.puml" alt="Detailed Interactions Inside the Logic Component for the `add n/Dohn Joe p/98765432 a/123 e/dohn@gm.com m/A1234567X s/S1 r/R1` User Input" />

<box type="info" seamless>

**Note:** Similar to the above sequence diagram, the lifeline for `AddCommandParser` and `AddCommand` should end at the destroy marker (X) but due to a limitation of PlantUML, the lifeline continues till the end of diagram.
</box>

The tokenizing and parsing is detailed as follows:
<puml src="diagrams/AddCommandParsing.puml" alt="Detailed Interactions for Parsing Fields of the Add command." />


### Model component
**API** : [`Model.java`](https://github.com/se-edu/addressbook-level3/tree/master/src/main/java/seedu/address/model/Model.java)

<puml src="diagrams/ModelClassDiagram.puml" width="450" />


The `Model` component,

* stores the address book data i.e., all `Person` objects (which are contained in a `UniquePersonList` object).
* stores the currently 'selected' `Person` objects (e.g., results of a search query) as a separate _filtered_ list which is exposed to outsiders as an unmodifiable `ObservableList<Person>` that can be 'observed' e.g. the UI can be bound to this list so that the UI automatically updates when the data in the list change.
* stores a `UserPref` object that represents the user’s preferences. This is exposed to the outside as a `ReadOnlyUserPref` objects.
* does not depend on any of the other three components (as the `Model` represents data entities of the domain, they should make sense on their own without depending on other components)

<box type="info" seamless>

**Note:** An alternative (arguably, a more OOP) model is given below. It has a `Tag` list in the `AddressBook`, which `Person` references. This allows `AddressBook` to only require one `Tag` object per unique tag, instead of each `Person` needing their own `Tag` objects.<br>

<puml src="diagrams/BetterModelClassDiagram.puml" width="450" />

</box>


### Storage component

**API** : [`Storage.java`](https://github.com/se-edu/addressbook-level3/tree/master/src/main/java/seedu/address/storage/Storage.java)

<puml src="diagrams/StorageClassDiagram.puml" width="550" />

The `Storage` component,
* can save both address book data and user preference data in JSON format, and read them back into corresponding objects.
* inherits from both `AddressBookStorage` and `UserPrefStorage`, which means it can be treated as either one (if only the functionality of only one is needed).
* depends on some classes in the `Model` component (because the `Storage` component's job is to save/retrieve objects that belong to the `Model`)

### Common classes

Classes used by multiple components are in the `seedu.addressbook.commons` package.

--------------------------------------------------------------------------------------------------------------------

## **Implementation**

This section describes some noteworthy details on how certain features are implemented.

### **`export`**

The `export` command allows users to export the details of each person currently displayed in the `PersonListPanel` to a CSV file. The CSV file is generated in the file `./addressbookdata/avengersassemble.csv`.

#### Implementation Details

The user uses the `find` feature to filter out the relevant persons, which will be displayed in the `PersonListPanel`. 
The `export` feature utilizes the `filteredPersons` list stored in `Model` to retrieve the relavant data displayed in `PersonListPanel`.
The `export` feature also relies the Jackson Dataformat CSV module and the Jackson Databind module write the details of persons to the CSV file `./addressbookdata/avengersassemble.csv`.

#### Parsing User Input

The `ExportCommand` class is instantiated directly by the `AddressBookParser` class when the user inputs the `export` command as the `export` command does not require any additional arguments.

#### Executing the Command

When the user inputs an `export` command, it is passed to an `AddressBookParser` which creates an `ExportCommand` object. The `LogicManager` then calls the `execute` method in the `ExportCommand` class.

#### Data Retrieval

The `excute` method calls the `Model#getFilteredPersonList()` to retrieve the `filteredPersons` list stored in `Model`. This lists stores the relevant persons currently displayed in the `PersonListPanel`.
It then creates a temporary `AddressBook` object and iterates through the `filteredPerons` list to add each person from the list into the AddressBook.

#### JSON Serialization

The information in the temporary `AddressBook` is written into a JSON file, `filteredaddressbook.json` using the `JsonAddressBookStorage#writeToJsonFile()` method.

#### CSV Conversion

The `export` feature relies on the Jackson Dataformat CSV module and the Jackson Databind module to read the JSON file and write the information into a CSV file.
Given below is are the steps taken to convert the JSON-formatted data to CSV:

* The JSON data is read from the JSON file and converted into a JSON tree using Jackson's `ObjectMapper`.
* The JSON tree is processed to extract the array of persons.
* A CSV schema is dynamically built based on the structure of the JSON array.
* The CSV schema and JSON data are used to write to the CSV file using Jackson's `CsvMapper`.

The following sequence diagram shows the interactions within the different classes when the user inputs an `export` command.

<puml src="diagrams/ExportSequenceDiagram.puml" alt="Sequence Diagram for the `export` Command" />

#### Alternative Implementations

* **Alternative 1 (current choice):** Exports the details of persons displayed in `PersonListPanel`.
    * Pros: Users do not have to go through the extra step of filtering through the persons in the CSV file in an external software.
    * Cons: The extent to which users can filter the persons displayed is highly dependent on the `find` feature.

* **Alternative 2:** Exports **all** contacts stored in the address book.
    * Pros: 
      * Easy to implement.
      * The `export` feature is not reliant on the `find` feature to update the `filteredPersons` list.
    * Cons: Users need to manually filter and sort through the CSV file if they require certain data which may be less efficient.

### **`copy`**

The `copy` command enables users to quickly copy the email addresses of the persons currently displayed to them in the
`PersonListPanel`. The copied emails are stored in the users' clipboard and can be pasted into an email client.
This feature is useful when users need to send emails to a group of persons.

#### Implementation Details

The copy command is a child of the `command` class and relies on the `filteredPersons` list in the `Model` component, 
as well as the `java.awt` package to copy the emails of all currently displayed persons to the users' clipboard.

#### Parsing User Input

The `CopyCommand` class is instantiated directly by the `AddressBookParser` class when the user inputs the `copy` command.
This is because the `copy` command does not require any additional arguments from the user.

#### Executing the Command

The `CopyCommand` class is created by the `AddressBookParser` class and passed to the `Logic` component for execution.
The `Logic` component then executes the command by calling the `execute` method in the `CopyCommand` class.

#### Copying Emails:

The `CopyCommand` class is responsible for executing the command for obtaining the emails of the filtered persons and copying them to the clipboard.
It iterates through the `filteredPersons` list in the `Model` component and extracts the email addresses of each person.
The email addresses are then concatenated into a single string, separated by commas, and copied to the clipboard using the `java.awt` package.

#### User Interface Interaction

After the `CopyCommand` is executed, the `UI` component updates the `ResultDisplay` to show a message indicating that the emails have been copied to the clipboard.

The following activity diagram summarizes the steps involved in executing the `copy` command:
<puml src="diagrams/CopyImplementationActivityDiagram.puml" width="1000" />

#### **Considerations**

##### Reliance on `find` Command

The `copy` command is designed to be used with the find command, which filters the persons displayed in the `PersonListPanel`.
Consequently, the flexibility of the `copy` command relies heavily on the implementation of the `find` command.
Due to this dependency, any changes to the `find` command may affect the functionality of the `copy` command.

##### Extensibility

Due to the simplicity of the `copy` command, there are limited opportunities for extending its functionality. 
However, future enhancements could include the ability to copy other details of persons, such as phone numbers or addresses.

##### Alternative Implementations

**Alternative 1: Copying emails of all persons**

Copies the emails of all persons in the address book, regardless of whether they are currently displayed in the `PersonListPanel`.
However, this approach may lead to users copying a large number of emails unintentionally, which could be overwhelming.
Furthermore, it may not be clear to users which emails are being copied.

**Alternative 2: Copying emails into a file**

Instead of copying the emails to the clipboard, the emails could be saved into a file.
This approach would allow users to access the emails at a later time and would prevent the loss of copied emails if the clipboard is cleared.
However, it may be less convenient for users who want to paste the emails directly into an email client.


### Addition of fields: Matriculation Number (`Matric`)

The optional `Matric` field enables the user to store the matriculation number of a person. The field is stored as a `Matric` in the `Person` object.
The `Studio` and `Reflection` fields are similarly implemented.

#### Implementation Details
The `Matric` class is a simple wrapper class that ensures it is valid according to NUS matriculation number format and is not empty.
The `Matric` field is used by the `add` and `edit` commands.

#### Parsing User Input: `add`
For the `add` command, as opposed to the `name` and other fields, the parser does not check if a prefix for `Matric` is present. This is because we define the `Matric` field to be optional as contacts (e.g. professors) do not need to have a matriculation number.

Then, the parser verifies that there are no duplicate prefixes for `Matric` in a single `add` command.
A new Person is then created with the `Matric` field set to the parsed `Matric` object.

#### Parsing User Input: `edit`
For the `edit` command, the parser will add or update the `Matric` field of the person being edited.


### Automatic Tagging of Persons

A `student` tag is automatically added during the parsing of the `add` command based on the presence of the `Matric` field of the person being added.

#### Implementation Details
During the parsing of the `add` command, the parser will check if the `Matric` field is present, meaning they are a student.
The parser also generates `Tag` objects based on the user input. The existing tags are updated with the new automatically generated tag.

The activity diagram is as follows:
<puml src="diagrams/AutomaticTaggingActivityDiagram.puml" alt="Activity Diagram for Auto Tagging Feature" />

### `import`

#### Implementation

The `ImportCommand` class is responsible for importing contacts from a CSV file. 
The `ImportCommandParser` class is responsible for parsing the user input and creating an `ImportCommand` object. The `ImportCommand` class then reads the CSV file and add the contacts to the `Model`.
The import process is done using a series of addCommands, which are executed in the same order as the rows in the CSV file.
It uses the addCommand so as to take advantage of the validation and error handling that is already implemented in the addCommand.
The import process is done in the following steps:
- ImportCommand reads the CSV file with the given file path.
- The CSV file is parsed and converts each row into the input a user would give to add the person (uses addCommand).
- The addCommand is then executed passing the same model as import command.
- The addCommand then adds the person to the model.

The sequence diagram below illustrates the interactions within the `Logic` component when the user issues the command `import`. 

<puml src="diagrams/ImportSequenceDiagram.puml" alt="Interactions Inside the Logic Component for the `import` Command" />

Reference Diagram for each addCommand in importCommand

<puml src="diagrams/ImportSequenceDiagramRef.puml" alt="Interactions Inside the Add Component for the `import` Command" />

#### Design Considerations

**Aspect: How to handle duplicate persons**
 
Handled by addCommand, which will check if the person already exists in the model. If the person already exists, the addCommand will not add the person and will return an error message.

**Aspect: How to handle invalid CSV files**

Handled by ImportCommand, which will check if the CSV file is valid.

The validities checked are:
- The file exists
- The file is a CSV file
- **The first row of the file is the header row. In which all compulsory fields are present. Headers that are not recognized will be ignored.**

If the file is not valid, an error message will be returned.

### **`find`**

The `find` command lets users search for persons by substring matching. The user can select any parameter to search under: `NAME`, `EMAIL`, `TAG`, `MATRIC`, `REFLECTION`, `STUDIO`, and `TAGS` can all be used. E.g. to search for all persons under studio `S2`, the user can use `find s|s2`.

#### Implementation Details
The `find` feature makes use of the predicate class `PersonDetailContainsKeywordPredicate` and the method `updateFilteredPersonList` to update the model to show only persons that fufill the criteria that the user has keyed in.

##### Parsing User Input

The `FindCommandParser` class is responsible for parsing user input to extract search criteria. It uses the `ArgumentTokenizer` to tokenize the input string, extracting prefixes and their associated values. Following that, the `extractPrefixForFindCommand` method ensures that only one valid, non-empty prefix is provided in the input.

##### Predicate Creation

The `PersonDetailContainsKeywordPredicate` class implements the `Predicate` interface to filter contacts based on search criteria. It takes a prefix and keyword as parameters, allowing it to filter contacts based on specific details like name, phone number, etc.

With the prefix and the value extracted from parsing the user input, a `PersonDetailContainsKeywordPredicate` is created.

##### Executing the Command

The `FindCommand` class is responsible for executing the command for filtering the list in the application. It takes in a `PersonDetailContainsKeywordPredicate` as a parameter and has a `execute` method inherited from its parent class of `Command`

Using the `PersonDetailContainsKeywordPredicate` created from parsing user input, a `FindCommand` is created. the `execute` method is then called by the `LogicManager`.

##### Updating Filtered Person List:

The `ModelManager` class implements the `Model` interface and manages the application's data. It maintains a `filteredPersons` list, which is a FilteredList of contacts based on the applied predicate. The `updateFilteredPersonList` method implemented in `ModelManager` updates the filtered list based on the provided predicate.

When the `FindCommand` is executed, the `updateFilteredPersonList` method is called with the `PersonDetailContainsKeywordPredicate` as a parameter. This updates the `filteredPersons` list to show only persons that fufill the predicate.

##### User Interface Interaction

After the `filteredPersons` list is updated, the user interface is updated such that the `PersonListPanel` now shows persons the fufill the predicate generated by the original user input.

The following sequence diagram illustrates the `find` command with the user input `find n|Alice`
<puml src="diagrams/FindImplementationSequenceDiagram.puml" width="550" />

The following activity Diagram illustrates the user execution of the `find` command
<puml src="diagrams/FindImplementationActivityDiagram.puml" width="550" />

#### **Considerations**

##### User Interface Consistency

The choice of implementing the command to use prefixes to determine the filter criteria ensures consistency with other commands in the application. As this command follows a similar structure to all other commands, it is easier for users to learn and use the application.

##### Flexibility in Search Criteria

By allowing users to specify search criteria using different prefixes (name, phone, email, etc.), the implementation offers flexibility.
Users can search for contacts based on various details, enhancing the usability of the feature. In the context of our potential users, we considered that users would likely have to sometimes filter students by their classes, or filter people by their roles (student, tutor, professor). So we opted to implement this feature with the flexibility of using all prefixes to account for all these potential use cases.

##### Predicate-based Filtering

As the `Model` class was built prior to the implementation of this feature, we did our best to re-use available methods instead of unnecessarily re-programing already existing logic. Hence, we decided to craft the command around the idea of a custom predicate as the `Model` class already had a `updateFilteredPersonList` method implemented that would filter persons using a predicate.

##### Extensibility

This design allows for easy extension to accommodate future enhancements or additional search criteria. New prefixes can be added to support additional search criteria without significant changes as we merely need to update our `Predicate` logic. This ensures that the implementation remains adaptable to evolving requirements and we can upgrade and improve the feature whenever required.

### **`deleteShown`**

#### Implementation Details

The `deleteShown` command is a child of the `Command` class and relies on the `filteredPersons` list in the `Model` component to delete the persons currently displayed in the `PersonListPanel`.

##### Executing the Command
If the list shows between 0 and the total number of existing persons, the `deleteShown` command will delete the persons currently displayed in the `PersonListPanel`.

##### Updating Filtered Person List
After deleting all persons currently displayed in the `PersonListPanel`, the `filteredPersons` list in the `Model` component is updated to show all remaining persons in the address book.

#### Considerations
##### Reliance on `find` Command
Similarly to the `copy` command, the `deleteShown` command is designed to be used with the find command, which filters the persons displayed in the `PersonListPanel`. Consequently, the flexibility of the `deleteShown` command relies heavily on the implementation of the `find` command. Due to this dependency, any changes to the `find` command may affect the functionality of the `deleteShown` command.

### \[Proposed\] Undo/redo feature

#### Proposed Implementation

The proposed undo/redo mechanism is facilitated by `VersionedAddressBook`. It extends `AddressBook` with an undo/redo history, stored internally as an `addressBookStateList` and `currentStatePointer`. Additionally, it implements the following operations:

* `VersionedAddressBook#commit()` — Saves the current address book state in its history.
* `VersionedAddressBook#undo()` — Restores the previous address book state from its history.
* `VersionedAddressBook#redo()` — Restores a previously undone address book state from its history.

These operations are exposed in the `Model` interface as `Model#commitAddressBook()`, `Model#undoAddressBook()` and `Model#redoAddressBook()` respectively.

Given below is an example usage scenario and how the undo/redo mechanism behaves at each step.

Step 1. The user launches the application for the first time. The `VersionedAddressBook` will be initialized with the initial address book state, and the `currentStatePointer` pointing to that single address book state.

<puml src="diagrams/UndoRedoState0.puml" alt="UndoRedoState0" />

Step 2. The user executes `delete 5` command to delete the 5th person in the address book. The `delete` command calls `Model#commitAddressBook()`, causing the modified state of the address book after the `delete 5` command executes to be saved in the `addressBookStateList`, and the `currentStatePointer` is shifted to the newly inserted address book state.

<puml src="diagrams/UndoRedoState1.puml" alt="UndoRedoState1" />

Step 3. The user executes `add n|David …​` to add a new person. The `add` command also calls `Model#commitAddressBook()`, causing another modified address book state to be saved into the `addressBookStateList`.

<puml src="diagrams/UndoRedoState2.puml" alt="UndoRedoState2" />

<box type="info" seamless>

**Note:** If a command fails its execution, it will not call `Model#commitAddressBook()`, so the address book state will not be saved into the `addressBookStateList`.

</box>

Step 4. The user now decides that adding the person was a mistake, and decides to undo that action by executing the `undo` command. The `undo` command will call `Model#undoAddressBook()`, which will shift the `currentStatePointer` once to the left, pointing it to the previous address book state, and restores the address book to that state.

<puml src="diagrams/UndoRedoState3.puml" alt="UndoRedoState3" />


<box type="info" seamless>

**Note:** If the `currentStatePointer` is at index 0, pointing to the initial AddressBook state, then there are no previous AddressBook states to restore. The `undo` command uses `Model#canUndoAddressBook()` to check if this is the case. If so, it will return an error to the user rather
than attempting to perform the undo.

</box>

The following sequence diagram shows how an undo operation goes through the `Logic` component:

<puml src="diagrams/UndoSequenceDiagram-Logic.puml" alt="UndoSequenceDiagram-Logic" />

<box type="info" seamless>

**Note:** The lifeline for `UndoCommand` should end at the destroy marker (X) but due to a limitation of PlantUML, the lifeline reaches the end of diagram.

</box>

Similarly, how an undo operation goes through the `Model` component is shown below:

<puml src="diagrams/UndoSequenceDiagram-Model.puml" alt="UndoSequenceDiagram-Model" />

The `redo` command does the opposite — it calls `Model#redoAddressBook()`, which shifts the `currentStatePointer` once to the right, pointing to the previously undone state, and restores the address book to that state.

<box type="info" seamless>

**Note:** If the `currentStatePointer` is at index `addressBookStateList.size() - 1`, pointing to the latest address book state, then there are no undone AddressBook states to restore. The `redo` command uses `Model#canRedoAddressBook()` to check if this is the case. If so, it will return an error to the user rather than attempting to perform the redo.

</box>

Step 5. The user then decides to execute the command `list`. Commands that do not modify the address book, such as `list`, will usually not call `Model#commitAddressBook()`, `Model#undoAddressBook()` or `Model#redoAddressBook()`. Thus, the `addressBookStateList` remains unchanged.

<puml src="diagrams/UndoRedoState4.puml" alt="UndoRedoState4" />

Step 6. The user executes `clear`, which calls `Model#commitAddressBook()`. Since the `currentStatePointer` is not pointing at the end of the `addressBookStateList`, all address book states after the `currentStatePointer` will be purged. Reason: It no longer makes sense to redo the `add n|David …​` command. This is the behavior that most modern desktop applications follow.

<puml src="diagrams/UndoRedoState5.puml" alt="UndoRedoState5" />

The following activity diagram summarizes what happens when a user executes a new command:

<puml src="diagrams/CommitActivityDiagram.puml" width="250" />

#### Design considerations:

**Aspect: How undo & redo executes:**

* **Alternative 1 (current choice):** Saves the entire address book.
    * Pros: Easy to implement.
    * Cons: May have performance issues in terms of memory usage.

* **Alternative 2:** Individual command knows how to undo/redo by
  itself.
    * Pros: Will use less memory (e.g. for `delete`, just save the person being deleted).
    * Cons: We must ensure that the implementation of each individual command are correct.

_{more aspects and alternatives to be added}_

### \[Proposed\] Data archiving

_{Explain here how the data archiving feature will be implemented}_


--------------------------------------------------------------------------------------------------------------------

## **Documentation, logging, testing, configuration, dev-ops**

* [Documentation guide](Documentation.md)
* [Testing guide](Testing.md)
* [Logging guide](Logging.md)
* [Configuration guide](Configuration.md)
* [DevOps guide](DevOps.md)

--------------------------------------------------------------------------------------------------------------------

## **Appendix: Requirements**

### Product scope

**Target user profile**:

* Name: Sarah Johnson
* Age: 23
* Occupation: Head Tutor for CS1101S

* head tutor for CS1101S course
* has a need to manage various aspects of course administration
* has a need to schedule classes
* has a need to coordinate with teaching assistants
* has a need to effectively communicate with students
* has a need to manage a significant number of persons
* prefer desktop apps over other types
* can type fast
* prefers typing to mouse interactions
* is reasonably comfortable using CLI apps

**Value proposition**:

* manage persons faster than a typical mouse/GUI driven app
* Centralised platform to store and manage person details for all relevant individuals involved in course administration
* Easier access to information through organising relevant persons into different subgroups
* Direct linkages to students’ schoolwork for easier tracking
* Able to set up the address book through different data-loading options
* Able to assist with management of large scale communication

**Problem Scope**:

* The CS1101S Head Tutor will face challenges in effectively organising and managing contact information within the department due to the large scale the course has to operate on. Existing methods, such as paper-based lists or basic digital spreadsheets, lack the necessary functionality to efficiently handle the diverse needs of proper contact management. There is a need for a user-friendly and offline-capable address book solution tailored specifically to the needs of a single user. This address book system should provide features such as easy contact entry and editing, quick search functionality, customizable categorization options, and the ability to add notes for each contact. Additionally, it should operate offline without requiring an internet connection and should not rely on complex database management systems.
* While the address book system will greatly improve contact management and organisation for the CS1101S Head Tutor, it will not address broader departmental communication or collaboration needs beyond individual contact management since the address book is designed to be a single-user system. It will not facilitate communication between users or provide collaboration tools for group projects or tasks. Additionally, the address book system will not handle complex data analysis or reporting functions beyond basic contact information management. Finally, while the system will provide offline functionality, it will not offer real-time synchronisation with online databases or cloud storage solutions.



### User stories

Priorities: High (must have) - `* * *`, Medium (nice to have) - `* *`, Low (unlikely to have) - `*`

| Priority | As a …​                                      | I want to …​                            | So that I can…​                                                          |
|----------|---------------------------------------------|----------------------------------------|-------------------------------------------------------------------------|
| `* * *`  | potential user exploring the app            | see the app populated with sample data | immediately see an example of the app in use                            |
| `* * *`  | new user                                    | easily clear the example data          | start using the app with real-life data                                 |
| `* * *`  | new user                                    | see usage instructions                 | refer to instructions when I forget how to use the App                  |
| `* * *`  | new user                                    | add persons with their details         | start populating the address book                                       |
| `* * *`  | new user                                    | save the data I input into the app     | don't lose the information I've entered                                 |
| `* * *`  | user                                        | add a new person                       |                                                                         |
| `* * *`  | user                                        | delete a person                        | remove entries that I no longer need                                    |
| `* * *`  | user                                        | update and edit person details         | keep my address book accurate                                           |
| `* * *`  | user                                        | find a person by name                  | locate details of persons without having to go through the entire list  |
| `* * *`  | user                                        | find a person by name                  | locate details of persons without having to go through the entire list  |
| `* * *`  | head tutor using the app                    | categorise my persons into groups      | manage different tutorial groups effectively                            |
| `* * *`  | head tutor using the app                    | copy email addresses of a group        | effectively communicate with target groups                              |
| `* * *`  | user                                        | find a person by name                  | locate details of persons without having to go through the entire list  |
| `* *`    | user                                        | hide private person details            | minimize chance of someone else seeing them by accident                 |
| `* *`    | experienced user                            | use the address book offline           | update and interact with it anywhere                                    |
| `*`      | user with many persons in the address book  | sort persons by name                   | locate a person easily                                                  |

*{More to be added}*

### Use cases

(For all use cases below, the **System** is the `AddressBook` and the **Actor** is the `user`, unless specified otherwise)

**Use case: UC01 — Delete a person**

**MSS:**

1.  User !!requests to list persons (UC04)!!
2.  AddressBook shows a list of persons
3.  User requests to delete a specific person in the list
4.  AddressBook deletes the person

    Use case ends.

**Extensions:**

* 2a. The list is empty.

  Use case ends.

* 3a. The given index is invalid.

    * 3a1. AddressBook shows an error message.

      Use case resumes at step 2.

**Use case: UC02 — Help**

**MSS:**

1.  User requests help information.
2.  AddressBook displays help information.
3.  User reads the help information.

    Use case ends.

**Use case: UC03 — Add a person**

**MSS:**

1.  User requests to add a new person and inputs details for the new person.
2.  AddressBook saves the new person's information.
3.  AddressBook confirms the addition of the new person.

    Use case ends.

**Extensions:**

*  1a. User does not input all compulsory parameters along with the person.

    *  1a1. AddressBook prompts the user on the proper usage of the command.

        Step 1a1 is repeated until the data entered is correct.

        Use case resumes at step 2.

*  1b. User tries to add a person with an existing email address.

    *  1b1. AddressBook displays an error message informing the user that the email address already exists.

        Step 1b1 is repeated until a valid email address is entered.

        Use case resumes at step 2.

**Use case: UC04 — List all persons**

**MSS:**

1.  User requests to list persons.
2.  AddressBook shows the list of persons.
3.  User views the list of persons.

    Use case ends.

**Extensions:**

* 2a. The list is empty.

    * 2a1. AddressBook displays a message indicating that the list is empty.

      Use case ends.

**Use case: UC05 — Edit a person's details**

**MSS:**

1.  User requests to edit a specific person with updated details.
2.  AddressBook saves the updated details.
3.  AddressBook confirms the successful update.

    Use case ends.

**Extensions:**

*   1a. User does not input enough parameters along with the person.

    *  1a1. AddressBook prompts the user on the proper usage of the command.

       Step 1a1 is repeated until the data entered is correct.

*   1b. The selected person does not exist.

    *  1b1. AddressBook displays an error message indicating that the person does not exist.

      Use case ends.

**Use case: UC06 — Find persons**

**MSS:**

1.  User requests to find a specific person matching the search criteria.
2.  AddressBook displays a list of persons matching the criteria.

    Use case ends.

**Extensions:**

*    1a. No persons match the search criteria.

     *   1a1. AddressBook displays a message indicating that no persons match the criteria.

     Use case ends.


**Use case: UC07 — Import persons**

**MSS**
1. User requests to import persons from a csv file.
2. AddressBook displays a message that all persons have been imported.
3. User is able to see all the persons imported when a list of persons is requested.
   Use case ends.

**Extension**

*   1a. AddressBook cannot find file to be imported.

    *   1a1. AddressBook displays a message indicating that the file is not recognised.

    Use case ends.

*   1b. The file to be imported is not a csv file.

    *  1b1. AddressBook displays an error message indicating that the file type is not recognised and should be a csv file

    Use case ends.


**Use case: UC08 — Copy email addresses**

**MSS:**

1.  User requests to copy emails of currently displayed persons.
2.  AddressBook copies the emails of currently displayed persons
into user's clipboard.
3.  AddressBook notifies the user that emails have been copied.
4.  User can paste emails when composing emails.

    Use case ends.

**Extensions:**

*   2a. No persons currently displayed.

    * 2a1. AddressBook displays a message indicating that
    no persons are currently displayed.

    Use case ends.

**Use case: UC09 — Clear all persons**

**MSS:**

1.  User requests to clear all persons.
2.  AddressBook clears all persons.
3.  AddressBook displays a message indicating that all persons have been cleared.

    Use case ends.

**Extensions:**

*    1a. User inputs extraneous parameters.

     *   1a1. AddressBook displays a message indicating that an extraneous parameter was found, and confirms User's intention.

         Use case ends.

**Use case: UC10 — Export listed persons to CSV**

**MSS:**

1.  User !!requests to filter persons (UC06)!! by desired requirements
2. User requests to export all listed persons and details to a CSV file.
3. AddressBook exports the persons to a CSV file.
4. AddressBook displays a message to confirm that all listed persons have been exported to a CSV file.

    Use case ends.

**Extensions:**

* 2a. No persons are listed.
  * 2a2. AddressBook displays a message indicating that there is no persons to export.

    Use case ends.

**Use case: UC11 - Delete shown persons**

**MSS:**

1. User !!requests to filter persons (UC06)!! by desired requirements
2. User requests to delete all listed persons.
3. AddressBook deletes all listed persons.
4. AddressBook displays a message to confirm that all listed persons have been deleted.

    Use case ends.

**Extensions:**

* 2a. No persons are listed.
    * 2a1. AddressBook displays a message indicating that there is no persons to delete.
  
        Use case ends.

* 2b. User has a filtered view that contains all existing persons.
     * 2b1. AddressBook displays a message indicating that all persons cannot be deleted at once.
    
        Use case ends.

**Use case: UC12 — Import Exam Results**

**MSS:**
1. User requests to import exam results from a csv file.
2. AddressBook displays a message that all exam results have been imported.

    Use case ends.

**Extensions:**

* 2a. AddressBook cannot find the file specified.
    * 2a1. AddressBook displays a message indicating that the file is not recognised.
  
        Use case ends.
* 2b. The file to be imported is not a csv file.
    * 2b1. AddressBook displays an error message indicating that the file type is not recognised and should be a csv file

        Use case ends.
* 2c. There are duplicate entries in the csv file.
    * 2c1. AddressBook displays a message indicating that there are duplicate entries in the csv file, and only the first instance has been kept.
  
        Use case ends.
* 2d. The csv file contains invalid entries.
    * 2d1. AddressBook displays a message indicating that there are invalid entries in the csv file, and all other valid entries have been imported.
  
        Use case ends.

**Use case: UC13 — Exit application**

**MSS:**

1.  User requests to exit the application.
2.  AddressBook exits the application.

    Use case ends.

**Extensions:**

*    1a. User inputs extraneous parameters.

     *   1a1. AddressBook displays a message indicating that an extraneous parameter was found, and confirms User's intention.

         Use case ends.

### Non-Functional Requirements

1.   Should work on any _mainstream OS_ as long as it has Java `11` or above installed.
2.   Should be able to hold up to 1000 persons without a noticeable sluggishness in performance for typical usage.
3.   A user with above average typing speed for regular English text (i.e. not code, not system admin commands) should be able to accomplish most of the tasks faster using commands than using the mouse.
4.   A user should be able to import up to 1000 persons from an external source without a noticeable sluggishness in performance for typical usage.
5.   The application should provide comprehensive documentation and help resources to assist users in understanding how to use the software effectively.


*{More to be added}*

### Glossary

* **Mainstream OS**: Windows, Linux, Unix, MacOS
* **Private person detail**: A person detail that is not meant to be shared with others

--------------------------------------------------------------------------------------------------------------------

## **Appendix: Instructions for manual testing**

Given below are instructions to test the app manually.

<box type="info" seamless>

**Note:** These instructions only provide a starting point for testers to work on;
testers are expected to do more *exploratory* testing.

</box>

### Launch and shutdown

1. Initial launch

    1. Download the jar file and copy into an empty folder

    1. Double-click the jar file Expected: Shows the GUI with a set of sample persons. The window size may not be optimum.

1. Saving window preferences

    1. Resize the window to an optimum size. Move the window to a different location. Close the window.

    1. Re-launch the app by double-clicking the jar file.<br>
       Expected: The most recent window size and location is retained.

1. _{ more test cases …​ }_

### Deleting a person

1. Deleting a person while all persons are being shown

    1. Prerequisites: List all persons using the `list` command. Multiple persons in the list.

    1. Test case: `delete 1`<br>
       Expected: First person is deleted from the list. Details of the deleted person shown in the status message. Timestamp in the status bar is updated.

    1. Test case: `delete 0`<br>
       Expected: No person is deleted. Error details shown in the status message. Status bar remains the same.

    1. Other incorrect delete commands to try: `delete`, `delete x`, `...` (where x is larger than the list size)<br>
       Expected: Similar to previous.

1. _{ more test cases …​ }_

### Saving data

1. Dealing with missing/corrupted data files

    1. _{explain how to simulate a missing/corrupted file, and the expected behavior}_

1. _{ more test cases …​ }_
