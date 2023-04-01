import React from 'react';
import { Droppable, Draggable } from 'react-beautiful-dnd';
import {Card} from './../Interfaces/Card';


interface Props{
    Cards: Card[];
}

export const AllCardsDisplay: React.FC<Props> = ({Cards}) => {
  return (
    <div>
        <Droppable droppableId="AllCards">
            {(provided) => (
              <div className="AllCards" ref={provided.innerRef} {...provided.droppableProps}>
                <p>All Cards:</p>
                <ul>
                  {Cards.map((card, index) => (
                    <Draggable key={card.name} draggableId={card.name} index={index}>
                      {(provided) => (
                        <li {...provided.draggableProps} {...provided.dragHandleProps} ref={provided.innerRef}>
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
