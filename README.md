<br />
<p align="center">
  <h1 align="center">Asteroids</h1>

  <p align="center">
    This project is an extension to the Asteroids game, containing very cool features
  </p>
</p>

## Table of Contents

* [About the Project](#about-the-project)
  * [Built With](#built-with)
* [Getting Started](#getting-started)
  * [Prerequisites](#prerequisites)
  * [Installation](#installation)
* [Design Description](#design-description)
  * [View](#view)
    * [Buttons](#buttons)
    * [Panels](#panels)
  * [Control](#control)
    * [Scoreboard Actions](#scoreboard-actions)
    * [Singleplayer Actions](#singleplayer-actions)
    * [Multiplayer Actions](#multiplayer-actions)
  * [Model](#model)
    * [Networking](#networking)
      * [Server](#server)
      * [ThreadHandler](#threadhandler)
      * [Client](#client)
* [Evaluation](#evaluation)
* [Teamwork](#teamwork)
* [Extras](#extras)

## About The Project

For this project, we upgraded an existing Asteroids game to give it more functionality and features.

### Built With

* [Maven](https://maven.apache.org/)

## Getting Started

To get a local copy up and running follow these simple steps.

### Prerequisites

The latest versions of the following:

* Java
* Maven 

### Installation

1. Navigate to the Asteroids folder
2. Clean and build the project using:
```sh
mvn install
```
3. Run the `Main` method of Asteroids using:
```sh
mvn exec:java
```
4. Alternatively you can run the `main` method in `Asteroids.java` using an IDE of your choice (e.g. IntelliJ)

Should you want to run this program standalone, you can create a JAR file with the following maven command:

```sh
mvn clean package
```
The JAR file will appear in the `/target` directory.

## Design Description
The main structure is divided into 5 packages; control, game_observer, model, util and view. There is also a main class Asteroids, used to start up the game by creating a new frame in which everything will be displayed. The packages and their contents will be described below.

### View
The `view` package contains two subpackages, `menu` and `view_models`. Besides, it contains the classes `AsteroidsFrame` and `AsteroidsPanel` which create the frame and panel of the game. The `menu` package contains the panel for the Main Menu, the Multiplayer Menu and the Scoreboard Menu. Furthermore it contains a package `buttons` that contains the classes for all the buttons and a package `files` where the used files such as the background for the panels are stored. The `view_models` contains the code to draw objects such as asteroids, bullets and spaceships on the panel. At all times, there is a `Menu` tab and a `Game` tab. When the user is in a game, the Menu tab can be clicked to quit the game or perform other actions. Clicking the Quit Game button, will take the user back to the Menu.

#### Buttons
For each button, a class is created. All those classes are very similar (besides the naming), in their constructor the `super` constructor is called, in which a new corresponding action is created. Furthermore the buttonproperties are set.

#### Panels
The AsteroidsFrame of our game is divided into 'pages'. When a button is clicked, this will repaint the view to a new 'page'. Starting with the `AsteroidsFrame`, there is a method that will decide which panel to show. This can either be a `MainMenuPanel`, a `MultiPlayerMenuPanel` or a `ScoreboardMenuPanel`. All these classes extend from the abstract class `MenuPanel`. By default, the `AsteroidsFrame` will create a `MainMenuPanel`. This panel contains buttons such as 'New Game' and 'Multiplayer. In case the `Multiplayer` button is clicked, the `AsteroidsFrame` will create a new `MultiplayerMenuPanel`, which will overwrite the current panel. When the `Scoreboard` button is clicked, the `AsteroidsFrame` will create a new `ScoreboardMenuPanel`, showing the Scoreboard Menu. 
Each MenuPanel again has its own pages, for example when hosting a new Multiplayer game, the MenuPanel changes to the page containing the buttons `Quit Game` and `Stop Hosting`. 

### Control
The `control` package contains all the actions that are connected to the buttons. Furthermore, it contains the classes `GameUpdater` and `PlayerKeyListener`. The `control` package has a package `menuActions` which contains all the classes for the actions related to the menu buttons. The code of the GameUpdater and PlayerKeyListener is more or less the same as the beginning of the project, since this code was given. We did however, change the GameUpdater, namely the `updatePhysics()` method and the asteroid methods. We added a PropertyChangeSupport to this class, such that every time the game is updated, i.e. a bullet is fired, an asteroid is added/destroyed and so on, an event is fired. When such an event is fired, the `ThreadHandler` will know that something has happened and that it needs to send this updated info to its clients. We also added ship-to-ship collision checking and by adding a field to the `Bullet` class, we know when to increment the score of a specific ship, which is checked in the `GameUpdater`. Lastly, there is a class `ForceCloseAction` which closes the application by calling `System.exit(0)`.

#### Scoreboard Actions
There are two classes related to scoreboard actions, `ClearScoreboardAction` and `ScoreboardAction`. For the first class, when this action is performed it will call the deleteScores method of the Database class, so that the database will be empty of entries and then it will repaint the panel so that the scores are removed. The other class will set the MenuPage of the `AsteroidsFrame` to '2', so that a Scoreboard panel will be shown.

#### Singleplayer Actions
For the singleplayer, there is the `NewGameAction` and the `QuitSPGameAction`. The NewGameAction updates the ship with chosen name and color and will start the game. The QuitSPGameAction lets the player quit the game and return to the main menu.

#### Multiplayer Actions
There are a few actions concerning multiplayer, namely `HostGameAction`, `JoinGameAction`, `MultiplayerAction`, `SpectateGameAction`, `StopHostingAction` and `QuitMPGameAction`. Performing the HostGameAction will start up a multiplayer game and starts a server. When JoinGameAction is invoked, a game is started and a new Client is created, which will try to connect to the provided server. 'MultiPlayerAction' will simply set the MenuPage of the AsteroidsFrame to 1, so that the Multiplayer menu will be shown. The StopHostingAction will shut down the server and let everyone connected return to the main menu, and with QuitMPGameAction a user quits the game it is connected to. The `SpectateGameAction` creates a new Client and connects the user to the game. 

### Model
The `model` has a `Networking` package containing the code for the UDP networking. Besides the classes that were already in the model package, a `Database` class was added. This class contains all the database related methods. The constructor of this class will call the createTable method, so that if there is no table yet in the database, it will be created. Everytime before a SQL query is performed, the database connection is established. We use a SQLite database, the files of the database and the driver are found in the Asteroids folder.

#### Networking
The networking is divided in a Server and Client side. Additionally, there is a class `ConnectionDetails` which contains a combination of InetAddress ipaddress and int port. The class contains getters for both variables and is used to describe the combination of IpAddress and port of a connection. The `Packet` class contains the data that will be transferred, this can be a collection of asteroids, collection of bullets, array of spaceships or all of the above. Furthermore it can also indicate when a game is over or if the Client is a spectator or player. These different types are represented as an int and based on the type of the packet, it will be known during the networking which information has to be sent. The `PacketHandler` handles the operations on datagram packets, it contains methods to send or receive a packet. The Server, Client and ThreadHandler extend this class. The `Updates` class is used as a middle man between the game data and the players. This is to avoid constant and multiple data requests from the game that can cause a `ConcurrentModificationException`. For this reason, the Updates class has both a `PropertyChangeSupport` and a `PropertyChangeEvent`. When a change happens in the game, the class Updates gets notified and retrieves the needed data. It then notifies the threads so that they can retrieve the data.

##### Server
The `Server` class will represent a server during the networking. It has a DatagramSocket and while the server is running, it waits for incoming connections. When an initialisation packet is received from a client, it will store the ConnectionDetails of this packet, send a packet back and start a new Thread for this client. It adds this thread to an ArrayList, so that when the server is terminated, it knows which threads also have to be terminated. While storing the ConnectionDetails, the data of the packet is also used to determine if the incoming connection is a spectator or a player and initialises the threads accordingly.

##### ThreadHandler
The `ThreadHandler` is used to send data to clients and receive data from clients. It starts by sending all the game details of the host, to the client(s) so that there is a game to start with. After this, only when there is a change in this game data, this updated data will be send to clients, depending on which data changed. For example, if an asteroid is added to the asteroid collection, the ThreadHandler will send this updated list to the client. Initially, our intention was that at this same time, the ThreadHandler would receive the spaceship data from the client, so that it could be added to the game. However, we were not able to establish a working two-way connection correctly in time. The class still contains the method `unpack()`, even though it is not used. We are aware that unused code should not be left in the final project. However, we kept it since it resembled one of our attempts to make it work. The method `setDetails()` represents the behavior that the server should take when receiving a packet from the Client.

##### Client
The `Client` class represents the client side of the networking. It starts by sending an initialisation packet to the server to establish the connection, it will receive a packet back to update its ConnectionDetails and then it is ready to send and receive information to and from the server. From the server, it periodically receives game details. The received data is from the `Packet` class and depending on the type of the packet, the game details will be updated. Furthermore, the client should send its own spaceship data to the server, so that this can be added to the game. However, as explained above, we could not get this work correctly. Nonetheless, if a player connects to the server using `Join Game`, a spaceship is added for this user. A spectator is also able to see the additional spaceships.

Since in order to spectate it is only necessary to have a one-way connection, spectating works as expected. The user can connect to a game by giving the IP and will be able to spectate the game. As intended, no spaceship will spawn when this connection is established. In the Multiplayer games, a user cannot select its own username and color in order to prevent multiple players having the same name and/or color. If a player or spectator loses the connection, or if the connection is terminated by the host, then the user is prompted with a message saying the connection has been lost. In addition, the game will close and return to the menu.

## Evaluation

A large part of the program works well. The database works as intended, there are no problems connecting to the database and the methods to operate on the database also work fine and we even have a `deleteScores()` method that correctly clears the table of the database. Moreover, everything on the Singleplayer side is working as expected. The user is able to set a unique username, which will be displayed on the scoreboard, and choose a spaceship color that will be displayed in game. When the game is over (either by quitting the game or having the spaceship destroyed), the username and score are added to the scoreboard. 

Besides the fact that not everything is implemented, the existing part of the Networking performs correctly. When hosting a game, a server is started where clients can connect to. The server is able to correctly tell if an incoming connection is a player or spectator and act accordingly. Although the client-side spaceship movement is not sent to the server, the server still spawns spaceships that have their own scores and energy bars for each player. We spent a lot of time trying to establish a clean two-way connection between a Client and a Server, but unfortunately we were unsuccessfull to still finish this in time. 

While working on the program, we didn't encounter many errors. The only thing that sporadically happened on a Multiplayer game is that we would get a `ConcurrentModificationException`. This happens because some of the game data is being changed and accessed at the same time. We have mitigated this error significantly by making some methods `synchronized` and by using the `Updates` class on Networking. 

In terms of testing, not many classes that can be tested have been added. We wanted to test the constructors of the Server and Client and see if we could have them send packages to each other, but to connect a Client to a Server we needed the Serverport, which we could not access. We did do some testing on the constructors of the `Packet` class, the `ConnectionDetails` class and the `Updates` class.

Would there have been more time available, we firstly would have had the networking problem solved. Additionally, we would have liked to add some images like a title screen and game over image to be displayed at the end of a game.

## Teamwork

We did a lot of the work together, while sitting next to each other. André did most work on the view, in terms of creating the menus and corresponding buttons and actions. Moreover, he did more work on preparing the needed data for the networking part. Jessica worked with the database and constructed the methods for the database. She also did the beginning of networking, such as creating the classes and method (outlines) for the Server, Client, Threadhandler etc. We then worked together on sending and receiving the needed data. Furthermore, there have been a few small changes that only a specific person did, but most work was done together. 

## Extras

When entering a Singleplayer game, the player is able to select a username and color for its spaceship, which will be displayed when the game starts. When viewing the scoreboard, it is possible to clear the scoreboard, removing all entries. 



<!-- Below you can find some sections that you would normally put in a README, but we decided to leave out (either because it is not very relevant, or because it is covered by one of the added sections) -->

<!-- ## Usage -->
<!-- Use this space to show useful examples of how a project can be used. Additional screenshots, code examples and demos work well in this space. You may also link to more resources. -->

<!-- ## Roadmap -->
<!-- Use this space to show your plans for future additions -->

<!-- ## Contributing -->
<!-- You can use this section to indicate how people can contribute to the project -->

<!-- ## License -->
<!-- You can add here whether the project is distributed under any license -->


<!-- ## Contact -->
<!-- If you want to provide some contact details, this is the place to do it -->

<!-- ## Acknowledgements  -->
