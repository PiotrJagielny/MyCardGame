This is a project where i make a card game. Backend is in Spring boot with java and frontend is in React with typescript. To store important
data such as username, i used react redux. For multiplayer with STOMP i used Spring web sockets on backend, and SockJS on frontend.
To store users decks i used PostgreSQL and JPA/Hibernate. 

This is card game based on rules of Gwint: The witcher card game.

In each move, a player can play one card or pass. If both players pass, the one with more points on the board wins. The player who wins two rounds wins the game. <br>

Currently, there are about 15 cards. Most of them dont have any effects, but not all. <br>
Fireball - Deals damage to an enemy card. <br>
Archer - A unit that can be placed and deals damage to an enemy card. <br>
Conflagration - Burns all cards with maximum points. <br>
Healer - Boosts every card on the player's board that has no more than 2 points. <br>
Booster - Boosts a single player card. <br>
Leader - Boosts every card in one row. <br>
Doubler - Doubles chosen card points. <br>
Rip - deals damage to whole row <br>
Rain - deals damage to max points card every turn <br>
Sharpshooter - deals damage to one enemy card and if this card dies, sharpshooter boost itself <br>
Cow - If cow dies, spawn chort in the same row <br>
Trebuchet - Deal i damage to random enemy every 2 turns<br>
Good person - Boost card on your board every 2 turns<br>
Gravedigger - Boost by number of cards on your graveyard on deploy<br>
Witch - Ressurect card from your graveyard<br>

To log in you have to enter name. Every user have its own instance of deck builder so there cannot be two users with same name.
![image](https://github.com/PiotrJagla/MyCardGame-MainProj/assets/76881722/b6a77471-4af0-4eed-ba58-181bf8404602)

This is multiplayer game, so when you click find enemy, there has to be other player waiting for fight to start a game (normally this ring on bottom is rotating).<br>
![image](https://github.com/PiotrJagla/MyCardGame-MainProj/assets/76881722/8be324bf-0f42-4e03-8533-e739ee734c1d)
<br>
Before you find an enemy, you have to choose which deck you want to use, and if this deck is not build correctly, you can't start Duel. <br>
![image](https://github.com/PiotrJagla/MyCardGame-MainProj/assets/76881722/0b5d2490-8119-4521-8f04-c64df069d3e7)







There is deck builder where you can add and remove cards from deck, add and remove decks
![image](https://github.com/PiotrJagla/MyCardGame-MainProj/assets/76881722/22f248e0-6f04-4352-b968-d5e81ca16d69)
![image](https://github.com/PiotrJagla/MyCardGame-MainProj/assets/76881722/48e4ff12-c46f-4f3a-a547-95da6b5f17be)



There is also duel page, where players can put cards from hand on one of three rows. 
![image](https://github.com/PiotrJagla/MyCardGame-MainProj/assets/76881722/b46f20cb-7e46-474f-a1f5-0a9d7fd4630c)
You can see cards on your graveyard as well as in your deck
![image](https://github.com/PiotrJagla/GwentClone-MainProj/assets/76881722/1966bda0-3da0-4e28-b994-b1148b5751b4)
![image](https://github.com/PiotrJagla/GwentClone-MainProj/assets/76881722/fd37d80c-d5a7-4d64-a818-7472f44ac872)


You can hover on a card, and its info appears
![image](https://github.com/PiotrJagla/MyCardGame-MainProj/assets/76881722/bf536ce4-d785-4c71-860c-0928bfd9a04e)
Cards that have effects every some amount of turns, appears as below: <br>
![image](https://github.com/PiotrJagla/GwentClone-MainProj/assets/76881722/2595e0f1-8079-4763-940f-fefdaa8706f4)








Based on card effect type, player can choose card to target:
![image](https://github.com/PiotrJagla/MyCardGame-MainProj/assets/76881722/ccfdffcf-7347-4f91-ab18-ce1c70ebf782)




Archer targetted Viking and dealt damage
![image](https://github.com/PiotrJagla/MyCardGame-MainProj/assets/76881722/58b0701e-3aaa-4d88-931c-a7dfc4e46446)





Also player can choose row to target
![image](https://github.com/PiotrJagla/MyCardGame-MainProj/assets/76881722/10aaa394-0881-4110-83cf-db5a54ed801f)




Player targetted second row with Rain, and now this row has weather effect that deals 2 damage every turn to max points card
![image](https://github.com/PiotrJagla/MyCardGame-MainProj/assets/76881722/4e38f21c-b346-4b15-8bdf-aca78b29476c)





Also there are cards that have effects, but player dont have to target anything
![image](https://github.com/PiotrJagla/MyCardGame-MainProj/assets/76881722/6b848339-a429-4f34-ae45-8780dcdde814)





After playing conflagration max points cards are burned.
![image](https://github.com/PiotrJagla/MyCardGame-MainProj/assets/76881722/ebe344cd-59e2-4417-8115-890f86d2ef1f)



<br>You can play cards in a chain, for example, play a card that allows you to choose another card from the deck, which you can then play.<br>
I played Priest, and a card from the deck that can be played appeared. I will choose to play a Healer.
![image](https://github.com/PiotrJagla/MyCardGame-MainProj/assets/76881722/c0779dd1-cc99-47a2-8dc0-899f784035ea)

Now, after choosing Healer, I can play this card as shown on the screen below. While playing Healer, I can't play any other cards from my hand.
![image](https://github.com/PiotrJagla/MyCardGame-MainProj/assets/76881722/cacf4088-e240-44e0-abf4-dbdde1c2ff2d)

After playing healer, all cards with max 2 points are boosted. (I am super excited about this feature because there was a little bit of thinking on how to implement that, and voilà!) But apart from this, it's just a cool functionality.
![image](https://github.com/PiotrJagla/MyCardGame-MainProj/assets/76881722/715b30dd-d62f-4776-8c7c-ea060c3dacf1)




<br> If enemy ended round, or you ended round, this information is displayed <br>
![image](https://github.com/PiotrJagla/MyCardGame-MainProj/assets/76881722/07aa64bb-7977-48c1-a3dc-d660f992bceb)

<br> If both players ended round, new round starts and in the middle of screen appears: <br>
![image](https://github.com/PiotrJagla/MyCardGame-MainProj/assets/76881722/0ca0c2f2-2e6a-4a02-a091-58bdd28e2120)



Player lost 2 rounds and lost game <br>
![image](https://github.com/PiotrJagla/MyCardGame-MainProj/assets/76881722/bbdc089a-5e84-48ac-a5fb-400f65c81e92)


After someone wins round, there appears a crown in the middle left on the proper side. <br>
Player won 2 rounds, 2 crowns appeared and won game <br>
![image](https://github.com/PiotrJagla/MyCardGame-MainProj/assets/76881722/4b5ef239-0584-4ee7-8958-9456e663d57e)


<br>If both players had same amount of points, they both score a crown. Here two players had same amount of points and game ended with draw<br>
![image](https://github.com/PiotrJagla/MyCardGame-MainProj/assets/76881722/3ceeddc5-9664-4f9c-a8ca-89237f3a8e98)













