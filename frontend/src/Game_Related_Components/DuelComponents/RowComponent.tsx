import React from 'react';
import {Card} from './../Interfaces/Card';
import { Droppable } from 'react-beautiful-dnd';
import './DuelPage.css';
import CardComponent from '../CardComponent';

interface Props{
    cardsOnRow: Card[];
    pointsOnRow: number;
    rowDroppableId: string;
    rowStatusImageURL: string;
}

export const RowComponent: React.FC<Props> = ({cardsOnRow, pointsOnRow, rowDroppableId, rowStatusImageURL}) => {
    
  return (
    <div >
        <Droppable droppableId={rowDroppableId} >
          {(provided) => (
            <div className="BoardContainer" ref={provided.innerRef} {...provided.droppableProps}>
              <div className="leftBoardContent" style={{backgroundImage: `url(${rowStatusImageURL})`, backgroundSize:'cover', backgroundPosition:'center'}} >
                <h3>{rowDroppableId}: {pointsOnRow} </h3>
              </div>
              <div className="rightBoardContent" >
                  {cardsOnRow.map((card, index) =>(
                        <CardComponent card={card} isOnRow = {true}></CardComponent>
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