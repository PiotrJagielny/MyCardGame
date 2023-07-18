import React from 'react';
import {Card} from './../Interfaces/Card';
import { Droppable } from 'react-beautiful-dnd';
import './DuelPage.css';
import CardComponent from '../CardComponent';

interface Props{
    cardsOnRow: Card[];
    pointsOnRow: number;
    rowDroppableId: string;
}

export const RowComponent: React.FC<Props> = ({cardsOnRow, pointsOnRow, rowDroppableId}) => {
  return (
    <div>
        <Droppable droppableId={rowDroppableId} >
          {(provided) => (
            <div className="BoardContainer" ref={provided.innerRef} {...provided.droppableProps}>
              <div className="leftBoardContent">
                <h3>{rowDroppableId}: {pointsOnRow} points</h3>
              </div>
              <div className="rightBoardContent" style={{display: 'flex'}}>
                  {cardsOnRow.map((card, index) =>(
                    <CardComponent  name={card.name} points={card.points}></CardComponent>
                  ))}
              {provided.placeholder}    
              </div>
            </div>
          )}
        </Droppable>
    </div>
  )
}

export default RowComponent