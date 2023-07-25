import React from 'react';
import {Card} from './../Interfaces/Card';
import { Droppable, Draggable } from 'react-beautiful-dnd';
import './DuelPage.css';
import CardComponent from '../CardComponent';

interface Props{
    cardsInHand: Card[];
    cardInPlayChain: Card;
}

export const HandComponent: React.FC<Props> = ({cardsInHand, cardInPlayChain}) => {
  return (
    <div className="handComponent">
        <Droppable droppableId="Hand">
          {(provided) => (
            <div className = "HandContainer" ref={provided.innerRef} {...provided.droppableProps}>
            <div className="leftHandContent">
              <h3>Hand</h3>
            </div>
            {cardInPlayChain.name === "" ? 

            <div className="rightHandContainer" style={{display: 'flex'}}>
                {cardsInHand.map((card, index) =>(
                  <Draggable key={card.name} draggableId={card.name} index={index}>
                    {(provided) => (
                      <div {...provided.draggableProps} {...provided.dragHandleProps} ref={provided.innerRef}>
                        <CardComponent  card={{name:card.name, points:card.points, cardInfo:card.cardInfo}}></CardComponent>
                      </div>    
                    )}  
                  </Draggable>
                ))}
            </div>
            :
            //Show card in play chain, and make cards in hand not draggable
            <div>
             <div className="rightHandContainer" style={{display: 'flex'}}>
                 {cardsInHand.map((card, index) =>(
                       <div>
                        <CardComponent  card={{name:card.name, points:card.points, cardInfo:card.cardInfo}}></CardComponent>
                       </div>    
                     )  
                 )}
             </div>
            <Draggable key={cardInPlayChain.name} draggableId={cardInPlayChain.name} index={0}>
              {(provided) => (
                <div className="playChainCard"{...provided.draggableProps} {...provided.dragHandleProps} ref={provided.innerRef}>
                  <CardComponent  card={{name:cardInPlayChain.name, points:cardInPlayChain.points, cardInfo:cardInPlayChain.cardInfo}}></CardComponent>
                </div>    
              )}
            </Draggable>
            </div>
            }
            {provided.placeholder}
            </div>
          )}
        </Droppable>
    </div>
  )
}

export default HandComponent