=:=:=:=:=:=:=:=:=:=:=:=:=:=:=:=:=:=:=
CIS 120 Game Project README
PennKey: ngoswami
=:=:=:=:=:=:=:=:=:=:=:=:=:=:=:=:=:=:=

===================
=: Core Concepts :=
===================

- List the four core concepts, the features they implement, and why each feature
  is an approprate use of the concept. Incorporate the feedback you got after
  submitting your proposal.

  1. Dynamic Dispatch - I used it a couple times.  (1) - to make an abstract CardDeck class, as I had several different
  types of card decks, and each had different functionality.  There was a FannedDeck class, for the decks on the bottom row,
  a CollectionPile class for the four decks on the top right, a DiscardPile class for face-up cards from the extra pile, and 
  an ExtraPile to click through flipped options.  Each had a different "isValidNextCard" function, which determined if a new
  card could be added, "addCard" method as it would cause different repaint and orderings within the decks, "includesClick" 
  method as determining the location of differently sized decks were different, "getTopCard", and "getCardsUntil".  
  This was especially relevant because, if the user clicked on a general "deck", it allowed me to use dynamic dispatch 
  to have different types of decks process the click without redundant code. 

  2. Collections - each card deck was modelled internally as a list.  It was especially helpful because I needed access to 
  the head and tail of card decks, depending on which card deck I was manipulating.  I similarly needed to use the isEmpty 
  method from the linked list method.  It was also helpful in getting and removing the top cards when needed, as depending
  on the deck, I either needed the head card or the tail card. I could not have used another data structure such as an array
  because linked lists allow me to have a mutable size. 

  3. JUnit testing - I tested the methods for the different card decks.  These included isValidNextCard, addCard, removeCard,
  isEmpty, getCardsUntil.  I tested these on all the relevant types of card decks that the functions were implemented on.  
  Since I had 4 different card decks that extended the abstract card deck class, I had a lot of these test cases!  Also,
  depending on the different deck, I had to use JUnit testing to determine if the top card was face up or face down upon
  an action on it, or upon initialization.

  4. I/O - Upon winning a game within the time limit, a user can add themselves to the scoreboard.  The highscores are 
  written in a text file "Highscores.txt", and every time a user wins the game, the high score table is read and processed
  to add the new winner if they made it in the top 10.  The program will write the winner back into the highscore file if so.
  Also, if you at any point would like to view the high score table, you can simply click the "Scoreboard" button, which will
  also read from the highscore file, and write the top 10 in a popup.


=========================
=: Your Implementation :=
=========================

- Provide an overview of each of the classes in your code, and what their
  function is in the overall game.
  
  Suit - just an enum class for the 4 different suits for cards.
  Card - creates a card with fields for Suit, value, color, xLocation, yLocation, and whether it is face up. 
       - Also has a static function to determine if, given a fanned deck that it is in, if that card is the one clicked if
         in a layered pile.  
       - Can check if, given a position of a click, if that click occured in the card.
       - Each card has its own repaint method. 
  CardDeck - Abstract class to create several other card decks.  Encapsulates state of deck as a linkedlist. Some methods
       are not abstract, but abstract ones include isValidNextCard, includesClick, addCard. 
  CardDeckTest - file for JUnit testing
  CollectionPile - Extends card deck, serves as the depositories for the four different suits in the top right. 
  ExtraPile - Extends card deck.  In the game, you can click through these cards to find a new card to work with.  Any unused
       clicks send the card to the discard pile.  If you click on this spot while it is empty, then it will refill with all
       the cards from the discard pile.
  DiscardPile - Extends card deck.  In the game, this is where the unused cards from the extra pile go.  If the extra pile is
       empty, this pile will empty and send all cards back to the extra pile.
  Game - the main runnable part of the game.  Holds the design for the main frame, and integrates all the jpanels.
  GameBoard - 


- Were there any significant stumbling blocks while you were implementing your
  game (related to your design, or otherwise)?
  
  In implementing, I drew all my cards as just pictures on the screen.  This meant that I had to process clicks on the 
  cards by the position of the mouse click.  I was too far into the game to change how I had drawn cards, so it took a long
  time for me to formulate a way to handle these clicks.  
  
  Also, I had a lot of trouble working through IO.


- Evaluate your design. Is there a good separation of functionality? How well is
  private state encapsulated? What would you refactor, if given the chance?
  
  Private state is encapsulated very well.  The linked list from the card deck abstract class is only accessible from the 
  abstract class and there are a lot of getter and setter methods.  Even in the cards, you must SET coordinates, 
  instead of publicly changing them.  All fields in the card class are private and are accessed through setter methods. 
  
  In that way, my functionality is well separated, and private state is well encapsulated.  If I could refactor something,
  though, I would change how each card is just some painted figure and mouse clicks were handled with position of the mouse.
  
  If I could refactor, I would have used JLayers to handle the clicks on cards in different layers.  I couldn't figure it 
  out well when I tried earlier.

========================
=: External Resources :=
========================

- Cite any external resources (libraries, images, tutorials, etc.) that you may
  have used while implementing your game.
  
  I didn't use any images, but I used the JavaDocs and Oracle website a lot.  I used them for JOptionPanes, BufferedReader,
  BufferedWriter, ArrayList, etc. 


