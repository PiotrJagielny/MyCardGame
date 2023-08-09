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
              <div style={{display: 'flex'}}>
                  <h3>Hand : {cardsInHand.length} </h3>
                <div style={{marginTop:'2%',width:'39px', height:'44px',
                backgroundPosition:'center',backgroundSize:'cover',backgroundImage:'url(https://static.thenounproject.com/png/1212866-200.png)'}}></div>
              </div>
            </div>

            {cardInPlayChain.name === "" ? 

            <div className="rightHandContainer" style={{display: 'flex'}}>
                {cardsInHand.map((card, index) =>(
                  <Draggable key={card.id.toString()} draggableId={card.id.toString()} index={index}>
                    {(provided) => (
                      <div {...provided.draggableProps} {...provided.dragHandleProps} ref={provided.innerRef}>
                        <CardComponent  card={card}></CardComponent>
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
                        <CardComponent  card={card}></CardComponent>
                       </div>    
                     )  
                 )}
             </div>
            <Draggable key={cardInPlayChain.id.toString()} draggableId={cardInPlayChain.id.toString()} index={0}>
              {(provided) => (
                <div className="playChainCard"{...provided.draggableProps} {...provided.dragHandleProps} ref={provided.innerRef}>
                  <CardComponent  card={cardInPlayChain}></CardComponent>
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