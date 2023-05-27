import React from 'react'
import { Droppable, Draggable } from 'react-beautiful-dnd';
import {Card} from './../Interfaces/Card';
import CardComponent from '../CardComponent';


interface Props{
    Cards: Card[];
    refresh: boolean;
}

export const CardsInDeckDisplay: React.FC<Props> = ({Cards, refresh}) => {
  return (
    <div>
        <p>Cards In Your Deck:</p><br/>
        <Droppable droppableId="CardsInDeck">
            {(provided) => (
              <div className="AllCardsInDeck" ref={provided.innerRef} {...provided.droppableProps} style={{ display: 'flex', flexWrap: 'wrap' }}>
                
                  {Cards.map((card, index) => (
                    <Draggable key={card.name} draggableId={card.name} index={index}>
                      {(provided) => (
                        <p {...provided.draggableProps} {...provided.dragHandleProps} ref={provided.innerRef} className="ggg">  
                          <CardComponent color={'blue'} image={'none'} name={card.name} points={card.points}></CardComponent>
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
