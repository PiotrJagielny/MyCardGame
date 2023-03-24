import React, { useState, useEffect } from 'react';
import { DragDropContext, Droppable, Draggable } from 'react-beautiful-dnd';
import  './DeckBuilderPage.css';

interface Card {
  name: string;
}

const SomeData = [
  { name: 'Card 1' },
  { name: 'Card 2' },
  { name: 'Card 3' },
];

const DeckBuilderPage = () => {
  const [CardsData, setCardsData] = useState<Card[]>([]);

  useEffect(() => {
    fetch('http://localhost:8000/DeckBuilder/GetAllCards')
      .then((res) => res.json())
      .then((CardsData: Card[]) => {
        setCardsData(CardsData);
      })
      .catch(console.error);

    console.log(CardsData);
  }, []);


  

  return (
    <div>
      DeckBuilderPage <br />

      <p>All Cards:</p>

      <DragDropContext onDragEnd={() => {}}>
        <Droppable droppableId="name">
          {(provided) => (
            <div className="AllCards" ref={provided.innerRef} {...provided.droppableProps}>
              <ul>
                {SomeData.map((item, index) => (
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
