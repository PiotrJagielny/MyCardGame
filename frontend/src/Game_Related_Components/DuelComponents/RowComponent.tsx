import React from 'react';
import {Card} from './../Interfaces/Card';
import { Droppable } from 'react-beautiful-dnd';
import './DuelPage.css';

interface Props{
    cardsOnRow: Card[];
    pointsOnRow: number;
    rowDroppableId: string;
}

export const RowComponent: React.FC<Props> = ({cardsOnRow, pointsOnRow, rowDroppableId}) => {
  return (
    <div>
        <Droppable droppableId={rowDroppableId}>
          {(provided) => (
            <div className="BoardContainer" ref={provided.innerRef} {...provided.droppableProps}>
              <div className="leftBoardContent">
                <h3>Row 2: {pointsOnRow} points</h3>
              </div>
              <div className="rightBoardContent">
                <ul>
                  {cardsOnRow.map((card, index) =>(
                    <li>{card.name}, {card.points}</li>    
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

export default RowComponent