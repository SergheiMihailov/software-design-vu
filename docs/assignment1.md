# Assignment 1
## Introduction									

Authors: Serghei Mihailov, Bjorn Keyser, Yael Goede, Milos Delgorge

The name of the project is Snippo. The system we will be implementing will be an implementation based on the code snippets manager. The goal of the code snippets manager is to create a system to manage snippets of source code. The system will simplify the managing of snippets by providing formats and functionalities that will let the user search, edit, and create source code snippets. The system will be able to adapt to new languages introduced by the user, hereby increasing the potential user group to all programming language users. The snippets manager will be hosted locally on the machine. The user will be able to interact with the manager through the shell. Here the user can use a crud api to manage the available snippets. The system will also provide a search command that will make retrieving snippets easier. The search command will use other features, for example contents, tags and metadata to optimise the search speed. 

Existing snippet manager applications for inspiration:
1. Gisto: https://www.gistoapp.com/features
2. Github Gists: https://developer.github.com/v3/gists/.

## Features:

Author(s):Serghei Mihailov, Bjorn Keyser, Yael Goede, Milos Delgorge

| **ID** | **Short name** | **Description** |
| --- | --- | --- |
| F1 | API to Create/Read/Update/Delete snippets | Users shall be able to easily access edit, create and delete snippets by calling the tool from the CLI using arguments. |
| F2 | Snippet editor | Users shall be able to edit and save snippets via a basic Vim-like CLI editor with visual/insert/replace modes. |
| F3 | Tags | Users shall be able to add tags to snippets by which will make finding snippets easier. This shall include language and whether the snippet is "starred". |
| F5 | Search/Filter | Users can search for their snippets by content, tags, description, title, language. Like snippo -s "language:java tags:tag1,tag2 searchterm". |
| F6 | Configurable syntax highlighting | Users shall have their code for a specific language with highlighting of syntax that is configurable via a file (that they create or that has been provided) that maps keywords to colors. |
| F7 | Readme/help for the tool | Users shall be able to view a manual explaining the basic commands and how to use the tool. |
| F8 (optional) | Authentication | Users shall be able to create and use accounts where their personal protected snippets are stored. |
| F9 | Snippet metadata | Users shall be able to view snippet metadata such as creation and last modification date, count of times edited or opened. |
| F10 (optional) | Import/Export snippets | Users shall be able to import and export via a json file to reuse on a different machine using Snippo. |
| F11 (optional) | Integrated with Github Gist API | Users shall be able to use Snippo in conjunction with Github Gists: [https://developer.github.com/v3/gists/](https://developer.github.com/v3/gists/) |
| F12 | Basic interactive UI in shell | On launch the program shall display available commands (e.g. edit, create, delete) and execute the chosen ones until closed.  |

## Quality Requirements:

Author(s): Serghei Mihailov, Bjorn Keyser, Yael Goede, Milos Delgorge

| **ID** | **Short name** | **Quality attribute** | **Description** |
| --- | --- | --- | --- |
| QR1 | Configurable language highlighting | Extensibility | It shall be easy for the user to add a new code language. |
| QR2 | Fast search | Responsiveness | Fast search, scalable with the number and size of snippets |
| QR3 | Intuitive UI | Usability | A new user shall be able to create/read/edit snippets within 5 minutes of using the tool |
| QR4 | Persistent snippet storage | Reliability | Snippets remain reliably stored in memory until deleted inside the manager or manually deleted from the filesystem |
| QR5 | Storing unsaved work | Reliability, Usability | In the case of unexpected shutdown of the process, unsaved work shall be stored |

## Java libraries:

Authors: Serghei Mihailov, Bjorn Keyser, Yael Goede, Milos Delgorge

| Name | Description |
| --- | --- |
| [Picocli](https://picocli.info/quick-guide.html#_what_is_picocli) | Picocli is a Java library and mini-framework for creating command line applications with almost no code. |
| [Gists](https://developer.github.com/v3/gists/) | This api will be used as a bonus. Implementing the snippets manager with the gists api. |
| [java.net.HttpUrlConnection](http://download.oracle.com/javase/7/docs/api/java/net/HttpURLConnection.html) | For http requests. |
| [fastjson](https://github.com/alibaba/fastjson) | For managing json files |
| [Time4J](http://www.time4j.net/) | Managing datetime |
=======


## Introduction									
Authors: Serghei Mihailov, Bjorn Keyser, Yael Goede, Milos Delgorge

Our application, snippo, will be a simple code snippet manager. The system will simplify the managing of code snippets by providing formats and functionalities that will let the user search, group, edit, and create source code snippets. The system will be able to adapt to new languages introduced by the user, hereby increasing the potential user group to all programming language users. The snippets manager will be hosted locally on the machine. The user will be able to interact with the manager through the shell. Here the user can use a crud api to manage the available snippets. The system will also provide search functionality that will let the user search based on things such as tags, names and code language. The system will include features, such as syntax highlighting, a basic UI in shell and will potentially be integrated with github gist.

https://www.gistoapp.com/features
https://developer.github.com/v3/gists/

## Features
What will make our snippet manager special? How will we separate our project from the others? Our main focus will lay with usability and accessibility, to make the usage of our snippet manager as pleasant as possible.To achieve this we sought out a few main features we would like to implement in our project. The following table is ordered, starting with the most important feature, all the way down to the least important one.

| ID  | Short name  | Description  |
|---|---|---|
| F1 | API to Create/Read/Update/Delete snippets|Users shall be able to easily access edit, create and delete snippets|
|F1.1|Snippet editor|A basic Vim-like cli editor with visual/insert/replace modes|
|F2|Tags|Users can add tags to snippets by which will make finding snippets easier|
|F2|Choose language|The user can choose any supported programming language to make their snippet|
|F3|Search/Filter|Users can search for their snippets by content, tags, description, title, language.Like snippo -s “language:ja va tags:tag1,tag2 searchterm”|
|F4|Configurable syntax highlighting|highlighting of syntax that is configurable via a file that maps words to colors|
|F5|Readme/help for the tool|Explaining the basic commands and how to use the tool|
|F6| Shortcuts and shell parameters|In the style of: snippo -f “assignment1” returns files that contain “assignment1”; snippo -c “new” creates a snippet “new”|
|F7|Snippet metadata |(creation/modification date, etc) think of windows file data.|
|F8 (optional)|Integrated with Github Gist API https://developer.github.com/v3/gists/|
|F9|Basic UI in shell|Some very basic UI eg.  add delete search etc|


### Quality requirements
The usability of snippets is of course really imported for a snippet manager, therefore we reasoned allot about how we could guarantee the usability of snippets in the system. A big part of our quality requirements is therefore committed to the usability and reliability of the system.

| ID  | Short name  | Quality attribute | Description  |
|---|---|---|---|
| QR1  | Commands sanity checks | Reliability  | When the player issues a command, the syntax of the command shall always get validated against the format specified in F2 |
|QR1|Configurable language highlighting|Extensibility| It shall be easy for the user to add a new code language.|
|QR2|Fast filtering|Responsiveness|Fast and scalable search: at most 100ms P99.9 time to filter 1k snippets by 15 criteria (+1 for each tag, search term, date etc.) |
|QR3|Intuitive UI|Usability|A new user shall be able to create/read/edit snippets within 5 minutes of using the tool|
|QR4|Persistent snippet storage|Reliability|Snippets remain reliably stored in memory until deleted inside the manager or manually deleted from the filesystem|
|QR5|Storing unsaved work|Reliability, Usability|In the case of unexpected shutdown of the process, unsaved work shall be stored|

### Java libraries
| Name (with link) | Description  |
|---|---|
|Picocli(https://picocli.info/quick-guide.html#_what_is_picocli)|Picocli is a Java library and mini-framework for creating command line applications with almost no code.|
|Gists(https://developer.github.com/v3/gists/)|This api will be used as a bonus. Implementing the snippets manager with the gists api.|
|java.net.HttpUrlConnection(http://download.oracle.com/javase/7/docs/api/java/net/HttpURLConnection.html)|For http requests.|
|---|---|
