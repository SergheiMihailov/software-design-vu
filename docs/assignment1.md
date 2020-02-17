# Assignment 1


## Introduction									
Authors: Serghei Mihailov, Bjorn Keyser, Yael Goede, Milos Delgorge

Our application, snippo, will be a simple code snippet manager. The system will simplify the managing of code snippets by providing formats and functionalities that will let the user search, group, edit, and create source code snippets. The system will be able to adapt to new languages introduced by the user, hereby increasing the potential user group to all programming language users. The snippets manager will be hosted locally on the machine. The user will be able to interact with the manager through the shell. Here the user can use a crud api to manage the available snippets. The system will also provide search functionality that will let the user search based on things such as tags, names and code language. The system will include features, such as syntax highlighting, a basic UI in shell and will potentially be integrated with github gist.

https://www.gistoapp.com/features
https://developer.github.com/v3/gists/

## Features
What will make our snippet manager special? How will we separate our project from the others? Our main focus will lay with usability and accessibility, to make the usage of our snippet manager as pleasant as possible.To achieve this we sought out a few main features we would like to implement in our project. The following table is ordered, starting with the most important feature, all the way down to the least important one.

| ID  | Short name  | Description  |
|---|---|---|
| F1 | API to Create/Read/Update/Delete snippets|Users should be able to easily access edit, create and delete snippets|
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
|QR1|Configurable language highlighting|Extensibility|t should be easy for the user to add a new code language.|
|QR2|Fast search|Responsiveness|Fast search, scalable with the number and size of snippets|
|QR3|Intuitive UI|Usability|A new user should be able to create/read/edit snippets within 5 minutes of using the tool|
|QR4|Persistent snippet storage|Reliability|Snippets remain reliably stored in memory until deleted inside the manager or manually deleted from the filesystem|
|QR5|Storing unsaved work|Reliability, Usability|In the case of unexpected shutdown of the process, unsaved work should be stored|

### Java libraries
| Name (with link) | Description  |
|---|---|
|Picocli(https://picocli.info/quick-guide.html#_what_is_picocli)|Picocli is a Java library and mini-framework for creating command line applications with almost no code.|
|Gists(https://developer.github.com/v3/gists/)|This api will be used as a bonus. Implementing the snippets manager with the gists api.|
|java.net.HttpUrlConnection(http://download.oracle.com/javase/7/docs/api/java/net/HttpURLConnection.html)|For http requests.|
|---|---|