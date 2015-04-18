# Conquer

## Introduction
**“Conquer”** is a fast-paced real-time multiplayer puzzle game that combines the elements of competition, strategy and logical thinking that pushes the players to achieve a score of 100 first. Each game for “Conquer” can be played individually or up to a maximum of 2 players at any time.

Based on the popular old-school classic, “Connect the Dots," Conquer goes beyond being just a simple digital implementation, the rules are tweaked and players will have to think and strategise on the spot in order to win.

Both players will be presented the same board of 8x8 `Dot`, with the main goal to connect all the dots of the same colour that are adjacent to one another. Once the dots of the same colour has been cleared, points will be awarded based on the length of the selected dots.

The main entertainment aspect of the game that separates “Conquer” from the classic is the ability to see the other player’s move in real-time. Since both players are competing on the same board, the ability to see the other player’s decision allows the element of strategy to come into play. Instead of simply gathering and finding the longest combination of dots first, one can decide to break an opponent’s combination in real-time since the moves of both players will be shown on both their devices.

The game’s competitive emphasis on speed, quick-thinking and strategy adds layers of fun and excitement as no two game play are the same. Each game will end when the player reaches a score of 100 first. Furthermore, the ability to see the opponent’s move in real-time motivates players to think twice before making a move.

This report discusses the various decision making and gives an overview of the entire game development progress in detail.

## System Requirements and Use Cases

### Game Requirements Overview
####Use Cases:
#####**Requirement ID: Conquer_REQ_UA1 - Single Player Mode**
######Requirement Description
Users will be able to start the single player mode when they select the `1 player` option. Once after which, users will be prompted with the loading of the main game screen. The user should remain logged into the single player mode until the game is over.

######Functional Part:

 - Enable the start of single player mode in Conquer
 - Able to retain the single player mode on the particular Android device used until the user completes the game.
 - The user can only access Game Features when the single player mode has started.
######Non-Fuctional Part:
 - Prompt users with the option to choose between single player mode and multiplayer mode.
 - Successful selection of single player mode could be immediately followed by the main game screen, where the user will be seamlessly granted access to Game Features.
 - The time needed to start the single player mode should not be too long (exceed 5 seconds).
 

#####**Use Case ID: Conquer_UC_UA1 - Single Player Mode**
| ID    |         Conquer_UC_UA1                            |
|----------------|:------------------------------------|
|Actor:          | Player1/Server                      |
|Objective:      |To play a game|
|Trigger:		| Single Player Button|
|Pre-Conditions: |-|
|Post-Conditions:|Gameplay will start  |
|Normal Flow :   |Player1 opens app, selects the "One Player" option. The game will then start.            |
|Interacts with:| main activity|


| Two Players (Multiplayer)    |                                     |
|----------------|:------------------------------------|
|Actor:          | Player1/Server                      |
|Objective:      |To connect to player's via IP address.|
|Pre-Conditions: |Android phone is connected to a wifi.|
|Post-Conditions:|Success: Gameplay will start when both parties successfully connects.  |
|                |Failure: Will not proceed to gameplay, needs to enter a valid IP address |
|Normal Flow :   |Player1 opens app, selects the "Two Players" option. They will be sent to another screen to input their friend's IP address and they connect.            |
|Interacts with:| note add specific classes it uses|


| Valid Move                     |                                                                            |
|----------------|:------------------------------------| 
|Actor:          |Player1/Player2|
|Objective:      |Select desired circle coloured combination. |
|Pre-Conditions: |Game is in play and has started.              |
|Post-Condition: |Success: valid move, all selected colours are the same.|
|                |Failure: non-valid move, selected coloured circle(s) is/are not the same.                             |
|Normal Flow:    |During gameplay                      |
|Interacts with: |                                     |


|Move Action                     |                                     |
|----------------|:------------------------------------|                
|Actor:          |Player1/Player2                      |
|Objective:      |Circle combination selection move    |
|Pre-Condition:  |Game is in play.                     |
|Post-Condition: |Move has been selected               |
|Normal Flow:    |The player makes the first selection by touching down on the circle, drags on other neighboring circles until desired selections are selected then lifts finger.|
|Interact with:  |

|Collision                    |                                                                           |
|----------------|:------------------------------------| 
|Actor:          |Player1/Player2                      |
|Pre-Condition:  |Game is in play and both players are selecting their circles.|
|Post-Condition: |The move of the slower player that selects the same circle will be denied.|
|Normal Flow:    |During game play, when one player's selection of circles is the same as the other, this creates a collision or conflict, however because it's first come first serve, the slower player that tries to select this circle will be unable to.|
|Interacts with: |                         |

|Game Over    |                                                                                      |
|----------------|:------------------------------------|
|Actor:       |Player1/Player2 |
|Objective:   |First player to score 100 points wins|
|Pre-Condition:|Game is in play|
|Post-Condition:|One player wins when they accumulate 100 points first. |
|Normal Flow: | Regular ongoing gameplay.|
|Interacts with:|                     |


![Use Case Diagram](https://www.dropbox.com/s/3e5cxa63qzuwtoq/Dotsflow.png?dl=0)

### Player Matchmaking

#### Implementation

Direct connection between two players is implemented using sockets. As response time is of critical importance in the gameplay, we want to ensure direct communication between the two players with as little overhead as possible. Using [Google Play Services](https://developer.android.com/google/play-services/) for matchmaking will introduce an additional overhead to the messages sent and add to the latency which should be minimised. 

As such, we are able to achieve response time of up to `50ms` over the school network using sockets, which is essential for the fast paced gameplay.

#### Seamless Connection

The disadvantage of using a lower level implementation of sockets to matchmake opponents as compared to a higher level implementation like Google Play Services is that the IP address of the opponent is needed to initialise the socket connection. It is intrusive and disrupts the game experience when the player has to slowly enter the IP. 

To address this issue, the [Parse](https://www.parse.com) cloud service is used to cache and retrieve IP addresses. When a player attempts to start a multiplayer game, a query is first made to the cloud to attempt to retrieve an IP address that corresponds to the local network, that has been created within a certain time window. If such an IP address is retrieved, the App will start a client game instance and attempt to connect to a server at that IP address. Otherwise, the App will start a server game instance and save its IP address into the cloud to allow other clients to connect. 

A possible issue with this implementation is that matchmaking is done on a sequential basis, and if multiple users are using the app, they would connect to each other sequentially and are thus unable to select their opponent. A possible solution to this would be to allow the user to input a username and select from possible hosts. Another would be to use Google Play Services for the purpose of matching the IP addresses before initialised the current direct socket connection. 

### Game Mechanics

Conquer has a simple game play: players are presented with a 8x8 board of coloured `dot` where the players will then try to link as many dots of the same colour as possible. The game then ends when either player hits 100 dots connected. Conquer has an even simpler game mechanic: to stop the other player from clearing dots while clearing more dots themselves real-time. This would make players competitive, and by giving the players this option to stop the opponent from reaching 100, the players could try to stop the opponent by interrupting their path of movement, or simply clearing more dots fast enough themselves.

## System Design

### Modular Design

The core logic of the app is developed seperately from the Android application, in an effort to preserve modularity and to separate possible issues from implementing the code in the Android OS.  As such, the game was first designed to operate in the command line, where a `Scanner` would parse text entered into `System.in` into a `DotsInteraction` object. When implementing this code in Android, the same back-end core of the game can be preserved, and all that needs to be done would be to swap out the `Scanner` and instead package touches into `DotsInteraction` objects.

Thus, the back end of the project is packaged as a `.jar` file that is imported as a library into the Android project. It is possible to run a version of the back end code on a computer using the same scanner input and another on the Android device for these two instances of the game to communicate and play together.

#### Interactions

`DotInteractions` is an object used to model touches on the screen. It is a wrapper for an integer `x` and `y` that reflect their position on the board, as well as a `DotInteractionState` enum that can be:

- `TouchDown`
- `TouchMoved`
- `TouchUp`

#### Callbacks

Usage of a callbacks system is implemented instead of using multiple threads when operations are executed, which will may create issues with thread safety. Also, such a callback system further preserves modularity and separates the front end of the Android game from the back end game logic. By using these callbacks, it would be simple to implement the back end code in another Java Virtual Machine apart from Android, by filling in the callback with the appropriate response that interfaces the front end and the back end.

`DotsAndroidCallbacks` is a callback interface that allows methods to be called when certain events happen, namely:

```java
onSocketConnected();
onValidPlayerInteraction(DotsInteraction interaction);
onBoardChanged(ArrayList<DotsPoint> changedPoints);
onGameOver(int winningPlayerId, int[] finalScore);
onScoreUpdated(int[] score);
latencyChanged(long latency);
```

These methods are filled in within `GameFragment` within the Android project files, with appropriate actions that are to be taken, such as updating the screen when the board changes. The Javadoc can be referred to for more details.


Following polymorphism, the `DotsServer` and `DotsClient` classes both share the same parent class `DotsServerClientParent`. 

Here are the constructors for the respective classes:

#### Initialisation

To follow polymorphism the `DotsServer` and `DotsClient` classes both share the same parent class `DotsServerClientParent`. 

To initialise and start the `DotsServerClientParent`, first invoke the constructor with the network arguments. The choice of initialising a server or client is decided by the Android `GameFragment`

```java
// Constructors 

// Server
DotsServerClientParent game = new DotsServer(PORT);
// Client
game = new DotsClient(SERVER_IP, PORT);
```

Next, the callback is set, before the object can be started.

```java
// Set callback
game.setAndroidCallback(new DotsAndroidCallback() {...});

// Start
game.start();
```

#### Android Interactions

When touches are made on the screen, the touches are turned into `DotsInteraction` objects and the following method is called:

```java
game.doMove(interaction);
```

If necessary, the board will be updated and the appropriate callbacks will trigger.

### Choice of Game Engine

The choice of game engine is important as it accounts for the library available for efficient drawing and embedding images on the screen, handling the overall game logic and other various functions such as the concurrency of the multiplayer aspect. A proper choice will prove crucial to the entire development of the game that can meet and suit our requirements. The following criteria are some of the important aspects we have carefully discussed and considered while deciding on the Game Engine.


- Performance: How well and efficient the game engine can draw and render pixels on screen.
- The amount of available libraries for both concurrency and the game logic.
- Programming Paradigms used as this would directly affect the structure of the various classes.
- The ease of use and robustness of the Game Engine.

Popular game libraries such as [LibGDX](http://libgdx.badlogicgames.com) and [AndEngine](http://www.andengine.org) were considered prior to the start of the project but we have decided that the use of [Native Android SDK](https://developer.android.com/tools/sdk/ndk/index.html) would suit our requirements better and the reason are as follows:

- Game engines have very strict guidelines on graphic requirements
Bitmaps have to be sized to a power of 2 to be compatible with [OpenGL ES 1.0](https://www.opengl.org), the android ecosystem is fairly large and diverse therefore most devices will not be able to support it.
- Customised fonts have to be pre-rendered into bitmaps and will take up additional memory in the device.
- Images used in game engines have to be compiled into sprites first for efficiency reasons due to the image dimensions restrictions. These sprites will then have to be sliced into various assets in the game which incurs additional overhead cost whenever an assert needs to be changed.
- Since we have decided to implement socket programming for the concurrency aspect of the game, native android SDK is a good compliment as it allows relatively easy integration as compared to the Game Engines.
- Native Android SDK provides good support for fonts and takes care of compatibility issues across the various screens with differing pixel densities.

### Client - Server Infrastructure

As the multiplayer game is played simutaneously by both players,  concurrency of the display on two players is critical to ensure smooth gameplay. When the game is initialized, one instance of the application chooses to be a server, and waits for another instance which is the client to connect. 

Hence, we will discuss the differences in the game instance as a server or client below.

#### Server

The server contains the `DotsGame` object that app will receive these interactions from both the current player, as well as remotely from the player running the client instance of the app.

Whenever interactions are made by either player, `doMove(interaction)` in `DotsGame` on the server will be called with the corresponding interactions made. This object will process the interaction,  call the appropriate callbacks on the same device, and update the client accordingly. 

#### Client

The client acts simply as a slave and merely sends touches from the player to there server, and updates the screen according to the received messages.

#### Message Sending

All types messages that are sent subclass the `DotsMessage` interface, and sent and received through the socket's `objectInputStream` and `objectOutputStream`. A listener thread runs on each device that constantly waits and reads a `DotsMessage` object from the `objectInputStream`, and is handled by checking the type of the object. The simplified code below illustrates how the client handles received messages.

```java
while(gameIsRunning) {
	DotsMessage message = DotsSocketHelper.readMessageFromServer(clientSocket);

	if (message instanceOf DotsMessageBoard) {
		
		// trigger onBoardChanged in callback
		
	} else if (message instanceOf DotsMessageInteraction) {
		
		// trigger onValidPlayerInteraction in callback
		
	} else {
	
		//...
		
	}
}

```
These messages are sent through [AwesomeSockets](https://github.com/skewedlines/AwesomeSockets "AwesomeSockets on GitHub"), a library written to simplify and execute these operations on a higher level.

#### Game Scenario

When a particular game move is made, the following checks and actions are executed.

1. If move is valid, update devices
2. If the board has been changed, update devices with the new board and score
3. Check for game over, and update devices

As touches are made on the server, the game objects processes that to determine if it is a valid move, and if so, it will invoke the callback `onValidPlayerInteraction()`, to allow the device to update the screen accordingly. At the same time, it packages and sends this interaction as a `DotsMessageInteraction` to the client, where the listener thread will process it and invoke the same callback to update the screen on the client device.

If these touches are made on the client device, the client will assume that the move is valid and invoke `onValidPlayerInteraction()` to display the touch feedback on the screen immedaitely. The same interaction is sent to the server device, and is picked up by the listener thread and processed to determine if the move is valid. As the client device has assumed the move to be valid, another `DotsMessageInteraction` will have to be sent back to the client if the move is invalid to update the callback and remove the wrongly displayed touch positions on the client device. 

The assumption of valid touches is made to create the illusion of instantaneous synchronisation between the client and server object. The alternative would be to wait for positive response from the server before the client displays the touch feedback on the screen, but such an implementation makes the the game appear to be slow on the client device. Play testing that the delay in showing valid touches on the screen will lead to the player questioning if his moves have been made, and in instances of high network traffic, the game experience might be impeded. 

Consider the case where a particular player makes a `TouchUp` interaction that updates the displayed board. The server device will recognise that the board has been updated, and it will trigger its  `onBoardChanged()` callback with an `List` of points that need to be updated. The score of the players will also be updated in `onScoreUpdated()` on the server device. Following that, the server will package and send a `DotsMessageBoard` and `DotsMessageScore` to the client. The same callbacks will now be triggered on the client device by the listener thread. 

Within the `DotsGame` object, checks will be made to determine if the maximum score limit has been reached, or if there are no longer any valid moves. If the game is over, `onGameOver()` is triggered on the server, and the `DotsMessageGameOver` message is sent to the client to trigger the same callback.

### Overview Application Activity Diagram

### Overall Game Logic

Following Objected-Orientated Programming principles, the game logic is divided across three objects. 

#### DotsGame

`DotsGame` a container for the entire game instance, and is the sole object on which `doMove(interaction)` is called with an interaction. This object will translate the interaction into an `x` and `y` index and trigger `DotsLogic`, and it also handles conflict resolution between two players.

#### DotsLogic

`DotsLogic` performs is a class that performs checks on the board, and is called from `DotsGame`. It holds helper methods to check for adjacency and validity of moves made on the `DotsBoard`.

#### DotsBoard

`DotsBoard` is an object that represents the game board, which is stored in a two-dimensional array. It contains methods that deal with updating the board itself, such as `clearDots(ArrayList<DotsPoint> dotsPointList)`, which clears the points in the argument and cascades tiles above down to fill the empty spaces.

###Game Screen Components

The game screen provides the main graphical user interface where the player will interact with the game. The main game screen is initialised by the` DotScreen` class in which the various sub game screen components are also initialised. Each of the components can also be represented by a class and each class has a paired functionality with the main game logic class. The main components of the game screen can be seen below.


###Game Component Classes


####Helper Class

Helper classes are a special set of classes where its main functionality is to assist in providing some functionality, which is not linked to the goal of the main application classes in which it is used. The main motivation behind the implementation of helper class is to fully utilise object-oriented programming in Java. The helper classes found in Conquer are listed below:

- `BitmapImporter`
- `ScreenDimensions`
- `Effects`

#####BitmapImporter

`BitmapImporter` takes in Images from the Resource folder and resizes it down to a memory acceptable level with the main intention of reducing and preventing OOM as mentioned above. With the `BitmapImporter` helper class, all ImageView objects are scaled to a smaller size to ensure that the images in placed do not take up too much memory.

`BitmapImporter` takes in images in the resources folder and resizes it down according to the `reqWidth` and `reqHeight`. The result is a smaller `bitmap` that will be used and placed in the dotsScreen.


#####ScreenDimensions

As there is a great variety of android hardware manufacturer, it is logical that there will be many different screen sizes. Therefore it is important to ensure that the various game screen will fit and look the same in all of the different screen sizes. `ScreenDimensions` is a helper class that contains getters that returns the width, height and pixel density of the android screen. 

- `getHeight`
- `getWidth`
- `getDensity`

#####Effects

`Effects` is a special helper class that contains all the various methods that produces different effects. Having the entire effects in a helper class allows easy access and implementation of the various different effects throughout the game architecture. `Effects` also uses `Handler` for the following purposes: (1) to schedule messages and `runnable` to be executed at some point in the future, (2) to enqueue an action to be performed on a different thread and (3) it is thread safe.

Some of the effects implemented include:

 - `castFadeInEffect`
 - `castFadeOutEffect`

The effects take in 4 arguments and can be applied to any view, with the ability to tweak the extend of fading and the duration of the effect.

####Sub Game Components

#####DotView

`DotView` is a parent class that extends `ImageView`. Following the concept of polymorphism, `DotView` is further extended into several different Children classes for the different `dot` types found in Conquer to allow easy switching of dots within the game.

 - `BlueDotView` 
 - `RedDotView` 
 - `GreenDotView` 
 - `OneDotView` 
 - `TouchedDotView`
 - `TwoDotView` 
 - `YellowDotView`

As Conquer requires the drawing of multiple images on the main game screen, a `getDrawable` method is implemented to ensure efficiency and to reduce possible Out Of Memory (OOM) situations. As such, the `getDrawable` method decodes the Images and sizes it down and returns a smaller bitmap that improves and reduces memory allocation.

#####ScoreBoard

`ScoreBoard` serves as an indication and announcement to the player of the current scores of the player in Single Player mode and of both players in Multiplayer mode.

These indications are conveyed via a `TextView` which is dynamically changing as the game progresses and as actions and moves are made by the players involved. 

The class diagram below details the attributes and the methods associated with this class.

#####DotsScreen

`DotsScreen` is the heart of the main game itself. It is the initialiser of the main 8 x 8 board of `dot` and sets the `dot` in placed according to the device screen dimensions with the help of the helper class, `ScreenDimensions`. All the main sub game components found in the main game screen are initialised within `DotsScreen`. Apart from the main 8 x 8 board, `DotsScreen` also initialises and sets the `ScoreBoard` together with the Header for the players, Score and Enemy. 

`DotsScreen` also contains an `UpdateScreen` method whereby it receives an ArrayList<DotsPoint> and updates the current board with the updated board. `UpdateScreen` therefore links the game logic with the `DotsScreen` to ensure that the correct display of `dot` is reflected to the players.

### Interaction between Classes

### Detailed Class Diagrams
####Game classes
DotsGameTask class
![DotsGameTask class](https://dl-web.dropbox.com/get/Dots/planets/diagrams/Game/DotsGameTask.png?_subject_uid=132339545&w=AABCO8lnWwtB7mJrgq0vh6F33QlicVAOzte6F0M0UXWa9g)

####Fragment classes
MainFragment class
![MainFragment class](https://dl-web.dropbox.com/get/Dots/planets/diagrams/Fragments/MainFragment.png?_subject_uid=132339545&w=AAApIr2H13-AidZljDsKUzDI8tcKMbda_XGEg-R4KI1vTg)

GameOverFragment class
![GameOverFragment class](https://dl-web.dropbox.com/get/Dots/planets/diagrams/Fragments/GameOverFragment.png?_subject_uid=132339545&w=AACSF4KtnpvL9S-SRGbcdduoP6Zk9SZQAsMfyfTkHD_8mQ)

FragmentTransactionHelper class
![FragmentTransactionHelper class](https://dl-web.dropbox.com/get/Dots/planets/diagrams/Fragments/FragmentTransactionHelper.png?_subject_uid=132339545&w=AAAJKr7UKOg3txM9rzuZj9iVdsKAeXQUHKJU_srVoDnisg)

ConnectionFragment class
![ConnectionFragment class](https://dl-web.dropbox.com/get/Dots/planets/diagrams/Fragments/ConnectionFragment.jpg?_subject_uid=132339545&w=AACx_o8ci6P9ZF8o2_ri9uUETqouR1aDea3CnMmKNrigcA)

GameFragment class
![GameFragment class](https://dl-web.dropbox.com/get/Dots/planets/diagrams/Fragments/GameFragment.png?_subject_uid=132339545&w=AACv3EePZDnOH7xGq_1bxV4UCEB2xyXDUgW5JAzria9eDA)


## System Testing
As explored earlier, the overall game structure of Conquer is extremely complex, consisting of a variety of interactions between the various front-end android development classes with the game logic and as well as the concurrency architecture. Therefore, it is extremely important that testing must be carried out at both an individual game component level and on an overall application level. The following sections will discuss how the various testing of Conquer were carried out at these specified levels.

### Game Component Level Testing
A two-pronged approach was adopted for individual game component level testing. The entire system is split into the various components which were then tested with the standard `JUnit` provided in Java Eclipse IDE.

 1. Basic Game Logic Testing
 2. Game Screen UI Components

####Basic Game Logic Testing
One of the most crucial aspect of game development, the basic game logic testing covers testing from the most fundamental aspect of the game to the complex integration of the front-end android development and the back-end concurrency architecture. The various components of the game architecture were extracted into individual methods, and then tested with` JUnit`. The more important logic being tested are listed as follows:

- **Testing Board Clearing Method**
In Conquer, the generation of new randomised coloured dots to replace dots to be cleared have to fulfil the two main criteria; it has to be randomised and the dots to be cleared should follow a cascading logic such that the rows above dots to be cleared should “fall” and cascade accordingly. This test ensures the integrity of the main game logic as it helps to ensure that the newly replaced dots are indeed randomised as well as the fulfilling the cascading requirement.

- **Testing Player Interaction**
Player interaction plays a rudimentary role in Conquer’s game logic framework. Testing was first carried out in console level, to ensure that the player’s intended input matches and is accurately received and processed by the game logic. Once extensive console level tests were done, the next level of player interaction includes the physical testing of the actual multi gesture finger touch by the player. As the `SurfaceViewDot` class tracks the `x`,`y` coordinate of a finger press, it is important to ensure that the three states of finger press is accurately detected by the game logic. The three main states includes `TouchDown`, `TouchMoved`, `TouchUp`. These checks ensure that both the correctness and incorrectness of a player’s intended input is detected, to accurately determine a player’s move.

The tests being described here are non exhaustive and more test have been implemented and carried out to ensure the integrity of the game as a whole.
####Application Level Testing

The next category of component level testing accounts for the various UI sub game components on the main Game Screen and its expected exhibited behaviour. This portion of testing helps to validate further the integrity of the messages being sent and processed by the concurrency architecture in placed. The test fundamental role is to check the correctness of messages sent and received and also the corresponding results are reflected to the player correctly. The following details are some of the sub game screen UI components tested:

- **Verify the correctness of points updated**
The number of points received by each player should reflect the main game logic’s point system as well as the actual intended moves of the player. This test ensures that the arithmetic logic used in the point system is implemented accurately as well as the legal moves made by the players.

- **Verify correctness of multiplayer input**
Since both players will be able to see each other’s input touch in real time on their respective android device, it is important to ensure that the board reflects the actual real time input of each player. This test ensures that both players moves are visible and testes for the efficiency and time lag of the board to display both players’ highlighted moves in real time. Efficiency testing is carried out and verified through the logs in android studio to ensure an accurate display of players moves as well as an optimised concurrency architecture in placed.


###Summary

## Ensuring Thread Safety


### Concurrency between client and server

In order to eliminate concurrency issues that stem from possible network issues, the server maintains a master of the game instance, and all the interactions from both players will go through it for conflict resolution and processing.

#### Synchronisation

The concurrency issue with this game would be to synchronise the moves made by both players together, and ensure that the game logic detects and deals with these actions appropriately. This is actually the motivation behind the Master-slave design for the server and client, so that these issues are resolved easily by ensuring that moves can only be made on the game sequentially.

Benchmarking of `doMove()` reveals that this operation takes an average of `~1ms`,  and thus the impact of the synchronised method call is negligable even when called over multiple threads.

As this method can be called from either the main thread, when touches are made on the same device, or the listener thread which listens for messages from the client device, this method is synchronised to ensure that only one thread can modify the states of the board and other state variables at once.

#### Touch Conflict Resolution

##### Same `Dot` Selected

The first case of conflict would be if a player tries to touch a point on the grid that has already been selected by the other player. To resolve this, when the `DotsGame` receives a particular `DotsInteraction`, it first checks to see if it that particular touched position has been touched by the other player, and if so deems it to be an invalid interaction. Otherwise, the interaction is stored, and it is only refreshed on a `TouchUp` by a player, or when another form of conflict below needs to be resolved.

#### Resolving Cascading Conflicts

A more complicated form of conflict is when a player executes a move that may affect the moves that are not completed for the other player, as the `Dots` on the board will cascade down and affect the selected positions.

To illustrate, the board below describes a `3x3` example board, with the numbers reflecting the `x` and `y` positions of the points, and `R`, `G`, `B`, `Y` the corresponding color of that point.

```
  0 1 2
0 R R R
1 G B B
2 Y Y R
```

Take the instance where `player0` touches point `(0,2)` and drags to point `(1,2)` without lifting his finger from the screen. The `DotsGame` object is now holding on to these two points he has touched, and is waiting for a `TouchUp` interaction to clear these dots and cascade the points above down the screen.

Suppose that now `player1` touches point `(1,1)` and drags his finger to point `(2,1)`. Similarly, the `DotsGame` will store these points and awaits a `TouchUp`. 

If `player1` lifts his finger from the screen, `DotsGame` will recognize the `TouchUp` and will thus trigger clearing of `(1,1)` and `(1,2)`, and subsequently cascades the points above down to fill the cleared positions. The moves of `player0` is not affected, and he can continue to lift his finger from the screen to clear the points he had selected.

However, consider the case where `player0` lifts his finger from the screen before `player1`. Points `(0,2)` and `(1,2)` will be cleared and points above these will cascade down. Now, we realise that points `(1,1)` and `(2,1)` which were held by `player` corresponds to the different colors `R` and `B` respectively, and is now an invalid move. Thefore, `DotsGame` has to recognise that, and clear the stored moves of the other player. 

In general, when a player holds points below that of the other player or vice versa, if the points below are cleared first, the points held above will be deemed to be invalid and a cancellation message will be sent to remove the visual reflection of the touch on the screen for the player whom once held the touches.


## Additional Features

### Animations

### Instructions
An **Rules** section was added to the Application. Instructions can be accessed from the Main Screen by tapping the “Rules” button found on the bottom center of the main game screen.

These game instructions serve as a “Instruction” page by providing detailed description to first time players the fundamentals of the gameplay. In the future, an interactive tutorial could be implemented instead, for more effective learning to take place.


## Conclusion

Game development proved to be a a tedious and complicated process due to the extent of the complexity and variety of the android platforms available. Apart from the various avoidance and implementation methods that have been utilised to tackle thread safety issues, this project emphasises on the implementation of the concept of modularity and organisating the various sub components when dealing with complex systems. From the macro level of identifying the various system requirements, down to the implicit design of the various sub components in the game architecture, breaking the entire game architecture down into various sub components resulted in a better controlled and refined way of code implementation that allowed easy updatability and bug-fixing. Conclusively, Conquer could be considered an overall successful implementation of an all-time old school classic with new gaming mechanics that will attract new players.

<!--se_discussion_list:{"Yno8yn1ZF175BVOmLgRtxito":{"selectionStart":31057,"type":"conflict","selectionEnd":31050,"discussionIndex":"Yno8yn1ZF175BVOmLgRtxito"},"mAWxxe8l38P6J7SMZZi0b68S":{"selectionStart":29032,"type":"conflict","selectionEnd":29039,"discussionIndex":"mAWxxe8l38P6J7SMZZi0b68S"},"3rXehXqwck6vfCRyFTjN8r4r":{"selectionStart":31057,"type":"conflict","selectionEnd":31050,"discussionIndex":"3rXehXqwck6vfCRyFTjN8r4r"},"w3nCXtktVpvMs1l2bAcSyziy":{"selectionStart":27964,"type":"conflict","selectionEnd":27978,"discussionIndex":"w3nCXtktVpvMs1l2bAcSyziy"},"7WacSW3NAe1gu5oOZbwCKgh1":{"selectionStart":29032,"type":"conflict","selectionEnd":29039,"discussionIndex":"7WacSW3NAe1gu5oOZbwCKgh1","commentList":[{"content":"darren"}]},"uzXjTSkDEkGctURJtmKAFCK1":{"selectionStart":29543,"type":"conflict","selectionEnd":29558,"discussionIndex":"uzXjTSkDEkGctURJtmKAFCK1","commentList":[{"content":"darren"}]},"oEqtj6M0vhPaB2308HxEaXue":{"selectionStart":31057,"type":"conflict","selectionEnd":31050,"discussionIndex":"oEqtj6M0vhPaB2308HxEaXue"}}-->