# Assignment 2

### Implemented feature

| ID  | Short name  | Description  |
|---|---|---|
| F1| Basic interactive UI in shell | On launch the program shall display available commands (e.g. edit, create, delete) and execute the chosen ones until closed.  |
| F1.1| API to List All snippets | Users shall be able to list the data of all snippets (ID, title, language, tags) except for content |
| F1.2 | API to Create snippets | Users shall be able to edit snippets by calling the tool from the CLI using arguments. |
| F1.3 | API to Read a single snippet | Users shall be able to read full snippet data by calling the tool from the CLI using arguments. |
| F1.4 | API to Update snippets | Users shall be able to modify snippet content, title, language and tags by calling the tool from the CLI using arguments. |
| F1.5 | API to Delete snippets | Users shall be able to delete specific snippets by calling the tool from the CLI using arguments. |
| F2 | Snippet editor | Users shall be able to edit and save snippets via a basic Vim-like CLI editor with visual/insert/replace modes. |
| F3 | Tags | Users shall be able to add tags to snippets which will make snippets filterable by tag and finding snippets easier.|
| F4 | Filter | Users can filter for their snippets by content, tags, description, title, language. Like snippo -f "language:java tags:tag1,tag2 searchterm". |
| F5 | Configurable syntax highlighting | Users shall have their code for a specific language with highlighting of syntax that is configurable via a file (that they create or that has been provided) that maps keywords to colors. |
| F6 | Snippet metadata | Users shall be able to view snippet metadata such as creation and last modification date, count of times edited or opened. |
| F7 (optional) | Import/Export snippets | Users shall be able to import and export snippets via a json file to reuse on a different machine using Snippo. |
| F8 | Basic interactive UI in shell | On launch the program shall display available commands (e.g. edit, create, delete) and execute the chosen ones until closed.  |

### Used modeling tool
StarUML http://staruml.io/

## Class diagram									
Author(s): `Yael Goede`, `Serghei Mihailov`

![Class Diagram](ClassDiagramSerghei.svg)

### class: Snippet
This class represents the snippet objects, and thus contains the meta data and original data from the snippet.

#### Attributes
* _pathToJson_
    * this attribute makes the snippet unique, there for the content of the snippet can be retrieved separately. 
* _title_
    * the title attribute gives a quick summary of the contents of the snippet, this improves the usability and retrievability of the snippet.
* _content_
    * this is where the actual snippet code is stored.
* _language_
    * This attributes specifies the language the snippet utilises.
* _tags_
    * This attributes specifies the tags associated with the snippet. Tags are used to retrieve and manage snippets.
* _created_
    * This attribute contains the time/date the snippet was originally created.
* _modified_
    * This attribute will be update with the current time/date when the snippet is modified. 
#### Operations
* _Snippet(in pathToJson:String, in title :String, in content :String, in language :String, in tags :String[*])_
    * this is the constructor method of the snippet class
* _writeSnippetToJson(): void_
    * converts the snippet object into json format
* _onModification(): void_
    * updated the modification date field of the modified snippet
* _edit(): void_
    * opens an editor of the snippet that allows to modify its content.
* _getTitle(): String_   
    * Returns the title of the current snippet.
* _setTitle(in title:String): void_
    * Set the title of the current snippet.
* _getContent(): String_
    * returns the content field of the current snippet object.
* _setContent(in content:String): void_
    * sets the content field of the current snippet object.
* _getLanguage(): String_
    * returns the language field
* _setLanguage(in language:String): void_
    * just sets the language attribute to a value
* _getTags(): String[*]_
    * returns the tag String array
* _setTags(in tags:String[*]): void_
    * just sets the tags attribute to a value
* _getCreated(): Date_
    * returns creation date
* _getModified(): Date_
    * returns last modification date
* _toString(): String_
    * Combines all the field of the snippet object into a readable String.
#### Associations
* _Snippet > JsonIO_
    * described
* _Snippet - Editor_
    * The snippet instantiates the editor to edit its contents.
### class: Editor
This class takes care of the editor part, meaning syntax highlighting and editing the snippet content. The editor class is in a directed relation, only with the snippetManager class.
#### Attributes
* _textArea: RSyntaxTextArea_
    * the input text field that stores users modifications to the content of the snippet and provides syntax highlighting.
* _snippetToEdit: Snippet_
    * the snippet edited.
#### Operations
* _Editor(snippet:Snippet): void_
    * Initializes all the UI elements (frame, menu, textarea, listeners) and launches the editor window.
* _onSave(): void_
    * on a save event (e.g. user presses the Save menu item) this sets the content of the edited snippet to the content of the text area.
* _getFullEditorContent(): String_
    * This operation returns all the content in the textarea field currently in the editor.
#### Associations
* _Snippet - Editor_
    * described in Snippet.
### class: CliUI
This class implements the UI, and thus controls the navigation within the menu and further actions with the program by the user.
#### Attributes
* _snippetManager: snippetManager_
    * contains the snippetManager object that the user interacts with. 
* _isOpen: boolean_
    * Keeps track of the current state of the application.
* _keyBoard: Scanner_
    * Contains a scanner object for reading user input.
#### Operations
* _CliUI(in snippetManager:SnippetManager): CliUI_
    * Constructor function
* _uiLoop(): void_
    * Will display the menu and execute entered commands until the user quits.
* _displayMenu(): void_
    * Simply displays hardcoded menu options.
* _getAndExecuteCommand(): void_
    * Reads keyboard input and executes the corresponding command (as per the menu). 
* _createSnippet(): void_
    * the method makes all the necessary client-side operations to create a snippet, like prompting for input and making calls to backend.
* _deleteSnippet(): void_
    * same as create but delete.
* _editSnippet(): void_
    * same as create but edit.
* _filterSnippets(): void_
    * same as create but filter. Also prints the output of the snippets that match the filter.
* _quit(): void_
    * stops the ui loop by setting the loop variable `isOpen` to `false`.
* _runCommandsOnArgs(in args:String): void_
    * this parses the command line arguments if there are any and executes the respective command, displaying the output, then the app finishes.
    
#### Associations
* _CliUI < Main_
    * The Cli class is called from main and handles all the user interactions with the program.
* _CliUI > SnippetManager_
    * The Cli is initialized using a specific snippet manager as backend. 0..* Clis can use 1 snippet manager, but only one snippet manager per Cli.

### class: snippetManager
This class keeps track of all the snippets and is the only class able to modify the snippets.
#### Attributes
* _pathToSnippoDir: String_
    * Contains the path to the directory where all snippets are stored.
* _snippets: HashMap<Integer, Snippet>_
    * Keeps track of all snippets currently in the manager.
#### Operations
* _SnippetManager(pathToSnippoDir:String): void_
    * Constructor function
* _loadSnippets(folder:File): void_
    * uses JsonIO to read all snippets in the `pathToSnippoDir` folder.
* _listSnippets(snippetsToList:Map<Integer, Snippet>): String_
    * given an integer-snippet map, prints the list of snippets.
* _listAll(): String_
    * same as listSnippets but for all snippets managed.
* _create(title:String, content:String, language:String, tags :String[*]): Integer_
    * creates a snippet object with the given arguments.
* _read(id:Integer): Snippet_
    * returns the snippet with the provided id.
* _delete(id:Integer): void_
    * deletes the snippet with the provided id.
* _edit(id:Integer): void_
    * calls the snippets edit method.
* _filter(wordToContain:String, tags:String[*], language:String): HashMap<Integer, Snippet>_
    * filters the snippets managed and returns an integer-snippet map.
* _getNextId(): Int_
    * gets the next available id for a new snippet.
* _isValidId(id:Integer): Boolean_
    * checks if an integer is a valid id for an existing snippet.
* _generatePathToSnippetJson(snippetId:Integer): String_
    * returns the relative path for the requested snippet (by id).
* _getSnippets(): HashMap<Integer; Snippet>_
    * returns all the managed snippets as an integer-snippet hashmap.
#### Associations
* _SnippetManager < Main_
    *  On the start of the program the main class constructs a snippetmanager class which maintains all the operations on the snippets.
* _SnippetManager > Snippet_
    * described
* _SnippetManager > JsonIO_
    * described
    
### class: JsonIO
This class takes care of the conversion between string type and Json type using the GSON library.
#### Attributes
* _g: Gson_
    * contains the Gson object
#### Operations
* _JsonIO(): void_
    * instantiatest a Gson object.
* _getInstance(): JsonIO_
    * returns the singleton json object.
* _writeToJson(pathToJson:String, object :Object): void_
    * serializes an object.
* _loadFromJson(pathToJson:String): Snippet_
    * deserializes a Snippet.
* _onException(e:Exception): void_
    * handles exceptions like file not found or bad data.
#### Associations
* _JsonIO < Main_
    * already described
* _JsonIO < Snippet_
    * The snippet class uses the JsonIO class to convert snippet objects to json, to store snippet objects.
* _JsonIO < SnippetManager_
    * The snippet manager class uses the JsonIO class to convert json to objects, to retrieve snippet objects.

### class: Main
 
#### Attributes
* _snippetManager: SnippetManager_
    * the snippet manager used by the current instance of the app.
* _cliUI: CliUI_
    * the cliUI used by the current instance of the app.
#### Operations
* _main(args:String[*]): void_
    * Instantiates SnippetManager, CliUI and JsonIO. After this, CliUI handles the flow of the app.
#### Associations
* _Main > JsonIO_
    * instantiates.
* _Main > SnippetManager_
    * instantiates.
* _Main > CliUI_
    * instantiates.
    
## Object diagrams								
Author(s): Yael Goede

![snapshot Diagram](snapshotDiagram.png)
In the diagram above a snapshot of the system is shown where the database class is filled with 4 snippets. All snippets have a unique id which makes them unique from each other. Further more all fields are filled with the required information, such as the language, tags and content field. 
The content field contains the actual code used as snippet by the user. The language field specifies the programming language which is valid for the content field. This field enables features such as the text highlighting feature, which is language specific.

## State machine diagrams									
Author(s): `Björn Keyser`

### Class CLiUI
![snapshot Diagram](cli_stateDiagram.jpeg)

#### States
- Initial State: Here we display the menu and wait for the user input. In event of valid user input, one of the 6 functionalities is executed.
- View all snippets: Lists all snippets and their attributes (title, content, tags, language, metadata)
- Prompt for details [for creation of snippet]: In event of choosing to create a snippet, the user is prompted for a title, the language and tags. A snippet is then created and a editor is opened with no content. 
- Editor mode: The editor loads content if the editor opened a existing snippet, and a blank page otherwise. On exit, the user is prompted to either save the file or not.
- Prompt for ID: In case of deleting or editing a snippet, the user is prompted for the ID of the snippet to be deleted/edited. If the ID is valid the snippet is deleted/opened in the editor.
- View found snippets: In event of filtering, the user can specify parameters for filtering on title name, content or tags and the found snippets are displayed.
- Quit: quit the program	
							
## Sequence diagrams									
Author(s): Milos

This chapter contains the specification of at least 2 UML sequence diagrams of your system, together with a textual description of all its elements. Here you have to focus on specific situations you want to describe. For example, you can describe the interaction of player when performing a key part of the videogame, during a typical execution scenario, in a special case that may happen (e.g., an error situation), when finalizing a fantasy soccer game, etc.

For each sequence diagram you have to provide:
- a title representing the specific situation you want to describe;
- a figure representing the sequence diagram;
- a textual description of all its elements in a narrative manner (you do not need to structure your description into tables in this case). We expect a detailed description of all the interaction partners, their exchanged messages, and the fragments of interaction where they are involved. For each sequence diagram we expect a description of about 300-500 words.

The goal of your sequence diagrams is both descriptive and prescriptive, so put the needed level of detail here, finding the right trade-off between understandability of the models and their precision.

Maximum number of words for this section: 2500
### Sequence diagram for Creating a snippet
![Sequence diagram](SEQDCreateSS.png)
### Description
The user selects create in the Command line interface. After selecting create, the user is prompted 
with 3 reply messages asking for the title, language and tags for the snippet the user wants to create. 
After the user provides this information, the Command line interface calls create() to the snippet 
manager, which will fist call a method called getNextId, which will return the next available
ID the snippet can have. After it has the ID, the snippet manager creates a new instance of a snippet with that ID and the information specified by the user. When a new snippet is constructed, a new file is made and the snippet calls writeSnippetToJson, which is part of the JsonIO class. 
This will write the relevant data for the snippet to the file.
The snippet objects are stored in a hashmap called snippets. 
After a new snippet object is created, the snippet manager adds a new entry to the hashmap, 
containing the ID and created snippet. 
After this is done, the snippet manager will call edit on that snippet, and the user will be able to start writing the contents for their newly created snippet.

### Sequence diagram for Editing a snippet
![Sequence diagram](SEQDEditSS.png)
### Description
When the user choses the edit snippet option from the Command line interface and provided the ID of the snippet he/she wants to edit back to the Cli, the Cli makes a call Edit to the snippet manager class. fist the snippet manager will check if the ID the user provided is valid. If the ID is not valid, an error message will be prompted to the user through the Command line interface. If the ID is valid, the snippet manager will send a message to the snippet that is about to be edited, Edit(snippetToEdit). The snippet will then open an editor. When this instance of the editor
class is constructed, it will grab the content from the snippet the user has selected to edit by calling getContent on the snippet and will display that in the textfield. Now that the editor is open the user can freely edit the contents of the snippet. When the user has edited his/hers snippet and wishes to close 
out of the editor and presses save, the method setContent is called on the snippet
object that’s currently being edited. This method is responsible for updating the Json snippet’s content. The argument passed to this method is getFullEditorContent, which returns the current content in the editor. When setContent is called on the snippet object, the snippet calls the method writeToJson, which is part of the JsonIO class. This method updates the Json object so that the changes are actually saved. This JsonIO class is a singleton class that is nice to have, because then the snippet manager doesn’t have to be responsible for saving snippets, but the snippets can “save themselves” by using that helper class, which is good for modularity, and will make changing specific details in the way snippets are saved/stored easier to do because of the fact there are less dependencies between classes.

### Sequence diagram for the filter method
![Sequence diagram](SSSEQDFilter.png)
### Description						
The user selects the filter option in the Command line interface. 
The Cli will return a message asking the user to specify filterterms, language, and tags to filter by.
After the user has provided these arguments to the Cli, the Cli then calls Filter() with those parameters to the snippet manager. Filter will return a string object that contains snippets. 
This string is constructed by the listSnippet method. listSnippet will return all snippets in string form, but with the filter method we reduce that to only the snippets that conform to the filter specified by the user. 
This string will be provided back to the Cli, which then prints that string for the user to see.		

# Implementation
Author(s): `Serghei`, `Milos`

### Strategy
* To make sure our implementation is consistent with the presented uml diagrams, we iteratively implemented the features, classes and quality requirements. By using the agile development method our team could track open tasks through the scrumboard available on the github projects platform.
* Timeline:
    * Week 1 (17.02-23.02): Brainstorm the application. We went back and forth discussing requirements and implementation. Sketch the first class and sequence diagram. First class diagram: CliUI -> Snippet Manager -> Snippet -> Editor. (include pic)
	* Week 2 (24.02-01.03): Minimum functional app including all the classes described in the class model and the main features (CRUD, basic CLI UI, filtering, snippet persistence using json and import/export). As we were developing the app it became easier to see the structural dependencies between classes and to adjust our class diagram as well as add new relevant classes (like the singleton JsonIO for managing json read/write). We also adjusted the sequence diagrams to better model the calls made between the classes as they grew in complexity.
	* Week 3 (02.03-08.03): We spent this week adjusting our code to the revisited class and sequence models and to properly place the complexity inside the right classes. For instance we made the editor to only interface with the Snippet class and removed its usages inside the SnippetManager class. This way we removed an unnecessary responsibility from the SnippetManager and decreased the complexity of the project. Moreover, we added new features and polished the existing ones. The editor was implemented and allowed for a simpler modification of snippets with syntax highlighting. Instead of saving all snippets in one json file, the app now uses a directory with one file per snippet, which made export and import easier. Finally, we created the state machine and the object diagrams to describe the behavior of our app.

### Key Solution
There were two key solutions that made developing the app easier: `org.fife.ui.rsyntaxtextarea` library for syntax highlighting and `com.google.gson.Gson` for serializing and deserializing snippets. They provided a lot of utility while saving development time and reducing the complexity of the project. If we were to write our own syntax highlighting engine, it would have taken us enormous effort to come up with the requirements and an implementation, let alone collecting all the necessary data about syntax of programming languages. The same applies to Gson.

### location of the main Java class
_src/main/java/Main.java_

### Location of the Jar file
_out/artifacts/software_design_vu_2020_jar/software-design-vu-2020.jar_
### 30-second video
Click the image below to view the video

[![IMAGE ALT TEXT HERE](https://img.youtube.com/vi/watch?v=E64M_YNNRSk)](https://www.youtube.com/watch?v=E64M_YNNRSk)

## References
