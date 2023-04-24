import React from 'react';
import {Card} from './../Interfaces/Card';
import { Droppable, Draggable } from 'react-beautiful-dnd';
import './DuelPage.css';

interface Props{
    
    cardsInHand: Card[];
}

export const HandComponent: React.FC<Props> = ({cardsInHand}) => {
  return (
    <div>
        <Droppable droppableId="Hand">
          {(provided) => (
            <div className = "HandContainer" ref={provided.innerRef} {...provided.droppableProps}>
            <div className="leftHandContent">
              <h3>Hand</h3>
            </div>
            <div className="rightHandContainer">
              <ul>
                {cardsInHand.map((card, index) =>(
                  <Draggable key={card.name} draggableId={card.name} index={index}>
                    {(provided) => (
                      <li {...provided.draggableProps} {...provided.dragHandleProps} ref={provided.innerRef}>
                        {card.name}, {card.points}
                      </li>    
                    )}  
                  </Draggable>
                ))}
              </ul>
            </div>
              {provided.placeholder}  
            </div>
          )}
        </Droppable>
    </div>
  )
}

export default HandComponent