import React, { useState, useEffect } from 'react';
import { DragDropContext, Droppable, Draggable, DropResult } from 'react-beautiful-dnd';
import  './DeckBuilderPage.css';

interface Card {
  name: string;
}

const DeckBuilderPage = () => {
  const [cardsData, setCardsData] = useState<Card[]>([]);
  const [cardsInDeck, setCardsInDeck] = useState<Card[]>([]);

  useEffect(() => {
    fetch('http://localhost:8000/DeckBuilder/GetAllCards')
      .then((res) => res.json())
      .then((cardsData: Card[]) => {
        setCardsData(cardsData);
      })
      .catch(console.error);

    console.log(cardsData);
  }, []);


  const onDragEnd = (result:DropResult) => {
    const {source, destination} = result;
    
    if(!destination){return;}
    if(destination.droppableId === source.droppableId && destination.index === source.index){return;}

    let add: Card = {name:"Nothing"};
    let allCards = cardsData;
    let allCardsInDeck = cardsInDeck;

    if(source.droppableId === "AllCards"){
      add=allCards[source.index];
      allCards.splice(source.index, 1);
    }
    else if(source.droppableId === "CardsInDeck"){
      add=allCardsInDeck[source.index];
      allCardsInDeck.splice(source.index, 1);
    }

    if(destination.droppableId === "CardsInDeck"){
      allCardsInDeck.splice(destination.index,0,add);
    }
    else{
      allCards.splice(destination.index,0,add);
    }

    setCardsData(allCards);
    setCardsInDeck(allCardsInDeck);
  }

  return (
    <div>
      DeckBuilderPage <br />

      <p>All Cards:</p>

      <DragDropContext onDragEnd={onDragEnd}>
        <Droppable droppableId="AllCards">
          {(provided) => (
            <div className="AllCards" ref={provided.innerRef} {...provided.droppableProps}>
              <ul>
                {cardsData.map((item, index) => (
                  <Draggable key={item.name} draggableId={item.name} index={index}>
                    {(provided) => (
                      <li {...provided.draggableProps} {...provided.dragHandleProps} ref={provided.innerRef}>
                        <div>{item.name} </div>
                        <br />
                      </li>
                    )}
                  </Draggable>
                ))}
                {provided.placeholder}
              </ul>
            </div>
          )}
        </Droppable>

        <Droppable droppableId="CardsInDeck">
          {(provided) => (
            <div className="AllCardsInDeck" ref={provided.innerRef} {...provided.droppableProps}>
              <ul>
                {cardsInDeck.map((item, index) => (
                  <Draggable key={item.name} draggableId={item.name} index={index}>
                    {(provided) => (
                      <li {...provided.draggableProps} {...provided.dragHandleProps} ref={provided.innerRef} className="ggg">
                        <div>{item.name} </div>
                        <br />
                      </li>
                    )}
                  </Draggable>
                ))}
                {provided.placeholder}
              </ul>
            </div>
          )}
        </Droppable>
      </DragDropContext>
    </div>
  );
};

export default DeckBuilderPage;
