## System Design

### Server-Client Infrastructure

Hence, we will discuss the differences in the game instance as a server or client below.

#### Server

#### Client


## Ensuring Thread Safety


### Concurrency between client and server
As the multiplayer game is played simutaneously by both players,  concurrency of the display on two players is critical to ensure smooth gameplay. When the game is initialized, one instance of the application chooses to be a server, and waits for another instance which is the client to connect. 

In order to eliminate concurrency issues that stem from possible network issues, the server maintains a master of the game instance, and all the interactions from both players will go through it for conflict resolution and processing.



#### Interactions

`DotInteractions` is an object used to model touches on the screen. It is a wrapper for an integer `x` and `y` that reflect their position on the board, as well as a `DotInteractionState` enum that can be:

- `TouchDown`
- `TouchMoved`
- `TouchUp`

The `DotsGame` object that runs on the server instance of the app will receive these interactions from both the current player, as well as remotely from the player running the client instance of the app.

#### Synchronisation

The concurrency issue with this game would be to synchronise the moves made by both players together, and ensure that the game logic detects and deals with these actions appropriately. This is actually the motivation behind the Master-slave design for the server and client, so that these issues are resolved easily by ensuring that moves can only be made on the game sequentially. 

Whenever interactions are made by either player, `doMove(interaction)` in `DotsGame` on the server will be called with the corresponding interactions made. 



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


