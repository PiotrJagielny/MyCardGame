import React from 'react';
import { Droppable, Draggable } from 'react-beautiful-dnd';
import {Card} from './../Interfaces/Card';
import CardComponent from '../CardComponent';


interface Props{
    Cards: Card[];
    refresh: boolean;
    droppableName: string;
}

export const CardsCollectionDisplay: React.FC<Props> = ({Cards, refresh, droppableName}) => {
  return (
    <div>
        <Droppable droppableId={droppableName}>
            {(provided) => (
              <div className="AllCards" ref={provided.innerRef} {...provided.droppableProps} style={{ display: 'flex', flexWrap: 'wrap' }}> 
                  {Cards.map((card, index) => (
                    <Draggable key={card.id.toString()} draggableId={card.id.toString()} index={index}>
                      {(provided) => (
                        <p {...provided.draggableProps} {...provided.dragHandleProps} ref={provided.innerRef} className="ggg">  
                          <CardComponent card = {card} ></CardComponent>
                        </p>
                      )}
                    </Draggable>
                  ))}
                  {provided.placeholder}
              </div>
            )}
          </Droppable>
    </div>
  )
}