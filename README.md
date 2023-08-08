

## General overview
This is a project where i make a Gwent: The Witcher card game clone. <br>
Backend is built with Spring boot with Java and frontend is made using React with Typescript. <br>
To store important data on frontend such as username, i used react redux. <br>
For multiplayer with STOMP i used Spring web sockets on backend, and SockJS on frontend. <br>
To store users decks i used PostgreSQL and JPA/Hibernate. <br>

## Rules
Game consists of 3 rounds, player who wins 2 rounds wins. When both players end round, one with more points on board wins round. <br>
For every won round a crown appears in the middle of board. <br>
In each move, a player can either play card or end round.<br>

## Cards
Currently, there are about 30 cards. Most of them dont have any effects.<br>

Here are cards with effect.

Spells: <br>
Fireball - Deals damage to an enemy card.<br>
Conflagration - Burns all cards with maximum points. <br>
Rip - deals damage to whole row <br>
Rain - deals damage to max points card every turn <br>

Units: <br>
Archer - A unit that can be placed and deals damage to an enemy card. <br>
Healer - Boosts every card on the player's board that has no more than 2 points. <br>
Booster - Boosts a single player card. <br>
Leader - Boosts every card in one row. <br>
Doubler - Doubles chosen card points. <br>
Sharpshooter - deals damage to one enemy card and if this card dies, sharpshooter boost itself <br>
Cow - If cow dies, spawn chort in the same row <br>
Trebuchet - Deal i damage to random enemy every 2 turns<br>
Good person - Boost card on your board every 2 turns<br>
Gravedigger - Boost by number of cards on your graveyard on deploy<br>
Witch - Ressurect card from your graveyard<br>
Wild roam - Play all copies of this card from you deck on the same row<br>
Supplier - Play from deck copy of chosen card from board <br>

## Entry page
To log in you have to enter name. Every user have its own instance of deck builder so there cannot be two users with same name.
![image](https://github.com/PiotrJagla/MyCardGame-MainProj/assets/76881722/b6a77471-4af0-4eed-ba58-181bf8404602)

## Main menu
This is multiplayer game, so when you click find enemy, there has to be other player waiting for fight to start a game (normally this ring on bottom is rotating).<br>
![image](https://github.com/PiotrJagla/MyCardGame-MainProj/assets/76881722/8be324bf-0f42-4e03-8533-e739ee734c1d)
<br>
Before you find an enemy, you have to choose which deck you want to use, and if this deck is not build correctly, you can't start Duel. <br>
![image](https://github.com/PiotrJagla/MyCardGame-MainProj/assets/76881722/0b5d2490-8119-4521-8f04-c64df069d3e7)

## Deck builder
There is deck builder where you can add and remove cards from deck, add and remove decks
![image](https://github.com/PiotrJagla/GwentClone-MainProj/assets/76881722/04792da9-0cdb-4f88-bfb6-9f24efb63e2d)

## Duel demo
This is duel page where battle is taking place. <br>
There will be quick demo of how whole game runs. <br>
<br>
At the start, players can change 3 cards from hand with card from deck.
![image](https://github.com/PiotrJagla/GwentClone-MainProj/assets/76881722/0a65085f-22c5-4782-98d1-9054024c2a23)
Then the battle begins. You can see cards in your deck as well as cards on your graveyard. There is also icon 
That tells you whether its your turn or not.
You can hover on a card to show its info.
![image](https://github.com/PiotrJagla/GwentClone-MainProj/assets/76881722/c08a3710-eaba-44dc-b98c-eee0b533a68a)

![image](https://github.com/PiotrJagla/GwentClone-MainProj/assets/76881722/1d90f158-24c6-4d6b-8b1e-a1e61426f4f5)
As we can see its my turn, so i choose to play knight, that doesnt have any effect. I can play card by dragging card from hand and dropping
on any row.
![image](https://github.com/PiotrJagla/GwentClone-MainProj/assets/76881722/01d436e8-7a1b-4d49-897b-3db94d7d88ed)
Here after playing knight, enemy played Trebuchet on his turn. This card deals damage to enemy card every 2 turns. The hourglass icon
shows how many turn are there to activate this effect. This icon appear on every card with timing effects.
![image](https://github.com/PiotrJagla/GwentClone-MainProj/assets/76881722/60e62583-4686-420c-8cd6-9ac6a0be3939)
Now i will play supplier, that allows me to play from deck copy of chosen card from board.
![image](https://github.com/PiotrJagla/GwentClone-MainProj/assets/76881722/cd486fd2-e8db-462f-970f-6a08b724be58)
I will choose knight.
![image](https://github.com/PiotrJagla/GwentClone-MainProj/assets/76881722/b647a59e-6360-4cff-9ddc-0ab90761c0ff)
I had 1 copy of knight in my deck, so now i can play this card. 
After i placed knight enemy played rain, this is row status that deals damage to one max player card on row every turn. 
Status is applied on enemy and player rows. As you can see trebuchet dealt damage and in 2 turns it will deal damage again.

![image](https://github.com/PiotrJagla/GwentClone-MainProj/assets/76881722/7e2c4813-6789-47d1-8d8a-f0994622c365)
Now i choose to play priest, that allows me to play one card from my deck.
![image](https://github.com/PiotrJagla/GwentClone-MainProj/assets/76881722/59b94e04-ef71-4f4f-ade1-081afb40ab16)
I will choose conflagranation. This is special card that burns all cards with max points, here it will burn knight and trebuchet.
![image](https://github.com/PiotrJagla/GwentClone-MainProj/assets/76881722/ec2fce2f-c331-4202-a2ef-d16fd8923e07)
Now enemy choosed to end round. I have more points on board so i will end round too.
![image](https://github.com/PiotrJagla/GwentClone-MainProj/assets/76881722/e2b0bc61-41f1-48dc-911d-9f9012a2c00e)
<br>When new round start, in the middle of screen this appears:<br>
![image](https://github.com/PiotrJagla/GwentClone-MainProj/assets/76881722/d8634f5b-7e2f-4af6-80b9-b864f7702be3)
<br>I won round so crown appeared in the middle.
![image](https://github.com/PiotrJagla/GwentClone-MainProj/assets/76881722/d49a2618-52ba-4a57-927a-f3c5e8ac3773)














<br>
<br>
<br>
<br>
<br>
<br>




Player lost 2 rounds and lost game <br>
![image](https://github.com/PiotrJagla/MyCardGame-MainProj/assets/76881722/bbdc089a-5e84-48ac-a5fb-400f65c81e92)


Player won 2 rounds, 2 crowns appeared and won game <br>
![image](https://github.com/PiotrJagla/MyCardGame-MainProj/assets/76881722/4b5ef239-0584-4ee7-8958-9456e663d57e)


<br>If both players had same amount of points, they both score a crown. Here two players had same amount of points and game ended with draw<br>
![image](https://github.com/PiotrJagla/MyCardGame-MainProj/assets/76881722/3ceeddc5-9664-4f9c-a8ca-89237f3a8e98)













