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
                    <Draggable key={card.name} draggableId={card.name} index={index}>
                      {(provided) => (
                        <p {...provided.draggableProps} {...provided.dragHandleProps} ref={provided.innerRef} className="ggg">  
                          {/* <CardComponent  card={name={card.name}, points={card.points},}></CardComponent> */}
                          <CardComponent card = {{name:card.name , points: card.points, cardInfo: card.cardInfo }} ></CardComponent>
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