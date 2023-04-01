import React from 'react'
import { Droppable, Draggable } from 'react-beautiful-dnd';
import {Card} from './../Interfaces/Card';


interface Props{
    Cards: Card[];
}

export const CardsInDeckDisplay: React.FC<Props> = ({Cards}) => {
  return (
    <div>
        <Droppable droppableId="CardsInDeck">
            {(provided) => (
              <div className="AllCardsInDeck" ref={provided.innerRef} {...provided.droppableProps}>
                <p>Cards In Your Deck:</p>
                <ul>
                  {Cards.map((card, index) => (
                    <Draggable key={card.name} draggableId={card.name} index={index}>
                      {(provided) => (
                        <li {...provided.draggableProps} {...provided.dragHandleProps} ref={provided.innerRef} className="ggg">
                          <div>{card.name} </div>
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
    </div>
  )
}
