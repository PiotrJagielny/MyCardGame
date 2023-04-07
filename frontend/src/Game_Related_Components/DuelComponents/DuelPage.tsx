import React, {useState, useEffect} from 'react';
import {Card} from './../Interfaces/Card';
import { DragDropContext, DropResult } from 'react-beautiful-dnd';
import { Droppable, Draggable } from 'react-beautiful-dnd';

const DuelPage = () => {
  const [refresh, setRefresh] = useState(false);
  const [cardsInHand, setCardsInHand] = useState<Card[]>([]);
  const [cardsInDeck, setCardsInDeck] = useState<Card[]>([]);
  const [cardsOnBoard, setCardsOnBoard] = useState<Card[]>([]);
  const [pointsOnBoard, setPointsOnBoard] = useState<number>();

  const [cardsInHand2, setCardsInHand2] = useState<Card[]>([]);
  const [cardsInDeck2, setCardsInDeck2] = useState<Card[]>([]);
  const [cardsOnBoard2, setCardsOnBoard2] = useState<Card[]>([]);
  const [pointsOnBoard2, setPointsOnBoard2] = useState<number>();



  const [deckData, setDeckData] = useState<Card[]>([]);


  useEffect(() => {
    const controller = new AbortController();
    fetch('http://localhost:8000/DeckBuilder/GetCardsInDeck')
      .then((res) => res.json())
      .then((deckData: Card[]) => {
        setDeckData(deckData);
      })
      .catch(console.error);

      return () => {
        controller.abort();
      };
  }, []);
  
  useEffect(() => {
    const controller = new AbortController();
    if (deckData.length > 0) {
      fetch('http://localhost:8000/Duel/SetupDecks', {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json'
        },
        body: JSON.stringify(deckData),
        signal: controller.signal
      });
    }

    return () => {
      controller.abort();
    };

  }, [deckData]);

  const fetchCardsData = () => {

    fetch('http://localhost:8000/Duel/getHandCards_player1')
      .then((res) => res.json())
      .then((cardsInHand: Card[]) => {
        setCardsInHand(cardsInHand);
      })
      .catch(console.error);

    fetch('http://localhost:8000/Duel/getDeckCards_player1')
      .then((res) => res.json())
      .then((cardsInDeck: Card[]) => {
        setCardsInDeck(cardsInDeck);
      })
      .catch(console.error);

    fetch('http://localhost:8000/Duel/getBoardCards_player1')
      .then((res) => res.json())
      .then((cardsOnBoard: Card[]) => {
        setCardsOnBoard(cardsOnBoard);
      })
      .catch(console.error);

    fetch('http://localhost:8000/Duel/getBoardPoints_player1')
      .then((res) => res.json())
      .then((pointsOnBoard: number) => {
        setPointsOnBoard(pointsOnBoard);
      })
      .catch(console.error);

      // PLAYER 2

      fetch('http://localhost:8000/Duel/getHandCards_player2')
      .then((res) => res.json())
      .then((cardsInHand2: Card[]) => {
        setCardsInHand2(cardsInHand2);
      })
      .catch(console.error);

    fetch('http://localhost:8000/Duel/getDeckCards_player2')
      .then((res) => res.json())
      .then((cardsInDeck2: Card[]) => {
        setCardsInDeck2(cardsInDeck2);
      })
      .catch(console.error);

    fetch('http://localhost:8000/Duel/getBoardCards_player2')
      .then((res) => res.json())
      .then((cardsOnBoard2: Card[]) => {
        setCardsOnBoard2(cardsOnBoard2);
      })
      .catch(console.error);

    fetch('http://localhost:8000/Duel/getBoardPoints_player2')
      .then((res) => res.json())
      .then((pointsOnBoard2: number) => {
        setPointsOnBoard2(pointsOnBoard2);
      })
      .catch(console.error);

    setRefresh(true);
  }






  const onDragEnd_player1 = (result:DropResult) => {
    const {source, destination} = result;
    
    if(!destination){return;}
    if(destination.droppableId === "Hand"){return;}

    let cardDragged: Card = {name: result.draggableId};
    fetch('http://localhost:8000/Duel/playCard_player1', {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json'
        },
        body: JSON.stringify(cardDragged)
      });
    fetchCardsData();
  }

  const onDragEnd_player2 = (result:DropResult) => {
    const {source, destination} = result;
    
    if(!destination){return;}
    if(destination.droppableId === "Hand"){return;}

    let cardDragged: Card = {name: result.draggableId};
    fetch('http://localhost:8000/Duel/playCard_player2', {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json'
        },
        body: JSON.stringify(cardDragged)
      });
    fetchCardsData();
  }

  return (
    
    <div>
      <h2>Let the battle begin</h2>
      <div>
        <button onClick={fetchCardsData}>Load data</button>
      </div>


      {/* |||||||||||||||| TO JEST GRACZ 1 |||||||||||||||| */}
      <div>
      <h1>Cards in deck remaining</h1>
      <ul>
       {cardsInDeck.map(card =>(
            <li>{card.name}</li>
        ))}
      </ul>
      </div>
      
      <DragDropContext onDragEnd = {(onDragEnd_player1)}>
        
        <Droppable droppableId="Hand">
          {(provided) => (
            <div ref={provided.innerRef} {...provided.droppableProps}>
              <h1>Hand</h1>
              <ul>
                {cardsInHand.map((card, index) =>(
                  <Draggable key={card.name} draggableId={card.name} index={index}>
                    {(provided) => (
                      <li {...provided.draggableProps} {...provided.dragHandleProps} ref={provided.innerRef}>
                        {card.name}
                      </li>    
                    )}  
                  </Draggable>
                ))}
              </ul>
              {provided.placeholder}  
            </div>
          )}
        </Droppable>
          
        
        <Droppable droppableId="Board">
          {(provided) => (
            <div ref={provided.innerRef} {...provided.droppableProps}>
              <h1>Board: {pointsOnBoard} points</h1>
              <ul>
                {cardsOnBoard.map((card, index) =>(
                  <li>{card.name}</li>    
                ))}
              </ul>
              {provided.placeholder}    
            </div>
          )}
        </Droppable>
      </DragDropContext>

      {/* |||||||||||||||| TO JEST GRACZ 2 |||||||||||||||| */}
      
      <DragDropContext onDragEnd = {(onDragEnd_player2)}>
        

      <Droppable droppableId="Board">
          {(provided) => (
            <div ref={provided.innerRef} {...provided.droppableProps}>
              <h1>Board: {pointsOnBoard2} points</h1>
              <ul>
                {cardsOnBoard2.map((card, index) =>(
                  <li>{card.name}</li>    
                ))}
              </ul>
              {provided.placeholder}    
            </div>
          )}
        </Droppable>

        <Droppable droppableId="Hand">
          {(provided) => (
            <div ref={provided.innerRef} {...provided.droppableProps}>
              <h1>Hand</h1>
              <ul>
                {cardsInHand2.map((card, index) =>(
                  <Draggable key={card.name} draggableId={card.name} index={index}>
                    {(provided) => (
                      <li {...provided.draggableProps} {...provided.dragHandleProps} ref={provided.innerRef}>
                        {card.name}
                      </li>    
                    )}  
                  </Draggable>
                ))}
              </ul>
              {provided.placeholder}  
            </div>
          )}
        </Droppable>
          
      </DragDropContext>


      <div>
      <h1>Cards in deck remaining</h1>
      <ul>
       {cardsInDeck2.map(card =>(
            <li>{card.name}</li>
        ))}
      </ul>
      </div>
    </div>
  )
}

export default DuelPage