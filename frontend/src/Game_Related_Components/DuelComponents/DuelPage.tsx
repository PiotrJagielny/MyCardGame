import React, {useState, useEffect} from 'react'
import {Card} from './../Interfaces/Card';
import { DragDropContext, DropResult } from 'react-beautiful-dnd';
import { Droppable, Draggable } from 'react-beautiful-dnd';

const DuelPage = () => {
  const [refresh, setRefresh] = useState(false);
  const [cardsInHand, setCardsInHand] = useState<Card[]>([]);
  const [cardsInDeck, setCardsInDeck] = useState<Card[]>([]);
  const [cardsOnBoard, setCardsOnBoard] = useState<Card[]>([]);
  const [pointsOnBoard, setPointsOnBoard] = useState<number>();

  const [deckData, setDeckData] = useState<Card[]>([]);


  useEffect(() => {
    fetch('http://localhost:8000/DeckBuilder/GetCardsInDeck')
      .then((res) => res.json())
      .then((deckData: Card[]) => {
        setDeckData(deckData);
      })
      .catch(console.error);
  }, []);
  
  useEffect(() => {
    if (deckData.length > 0) {
      fetch('http://localhost:8000/Duel/SetupDeck', {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json'
        },
        body: JSON.stringify(deckData)
      });
    }
  }, [deckData]);

  const fetchCardsData = () => {

    fetch('http://localhost:8000/Duel/GetHandCards')
      .then((res) => res.json())
      .then((cardsInHand: Card[]) => {
        setCardsInHand(cardsInHand);
      })
      .catch(console.error);

    fetch('http://localhost:8000/Duel/GetDeckCards')
      .then((res) => res.json())
      .then((cardsInDeck: Card[]) => {
        setCardsInDeck(cardsInDeck);
      })
      .catch(console.error);

    fetch('http://localhost:8000/Duel/GetBoardCards')
      .then((res) => res.json())
      .then((cardsOnBoard: Card[]) => {
        setCardsOnBoard(cardsOnBoard);
      })
      .catch(console.error);

    fetch('http://localhost:8000/Duel/GetBoardPoints')
      .then((res) => res.json())
      .then((pointsOnBoard: number) => {
        setPointsOnBoard(pointsOnBoard);
      })
      .catch(console.error);

    setRefresh(true);
  }




  const onDragEnd = (result:DropResult) => {
    const {source, destination} = result;
    
    if(!destination){return;}
    if(destination.droppableId === source.droppableId && destination.index === source.index){return;}

    let PostURL:string = '';

    if(destination.droppableId === "Hand"){
      PostURL = "http://localhost:8000/Duel/PutCardFromDeckBack"
    }
    else if(destination.droppableId === "Board"){
      PostURL = "http://localhost:8000/Duel/PutCardToDeck"
    }

    let cardDragged: Card = {name: result.draggableId};
    
    
  }

  return (
    <div>
      <h2>Let the battle begin</h2>
      <div>
        <button onClick={fetchCardsData}>Load data</button>
      </div>

      <div>
      <h1>Cards in deck remaining</h1>
      <ul>
       {cardsInDeck.map(card =>(
            <li>{card.name}</li>
        ))}
      </ul>
      </div>
      
      <DragDropContext onDragEnd = {onDragEnd}>
        
          
        <div>
          <h1>Hand</h1>
          <ul>
            {cardsInHand.map((card, index) =>(
              <Draggable key={card.name} draggableId={card.name} index={index}>
                {(provided) => (
                  <li {...provided.draggableProps} {...provided.dragHandleProps} ref = {provided.innerRef}>
                    {card.name}
                  </li>    
                )}  
              </Draggable>
            ))}
          </ul>
        </div>
          
        
        <Droppable droppableId="Board">
          {(provided) => (
            <div ref={provided.innerRef} {...provided.droppableProps}>
              <h1>Board: {pointsOnBoard} points</h1>
              <ul>
                {cardsOnBoard.map((card, index) =>(
                  <li>{card.name}</li>    
                ))}
              </ul>
            </div>
          )}
        </Droppable>
      </DragDropContext>
    </div>
  )
}

export default DuelPage