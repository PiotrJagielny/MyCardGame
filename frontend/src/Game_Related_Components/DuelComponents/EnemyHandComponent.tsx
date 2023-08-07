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
            <div className = "HandContainer" >
            <div className="leftHandContent">
              <div style={{display: 'flex'}}>
                  <h3>Hand : {cardsInHand.length} </h3>
                <div style={{marginTop:'2%',width:'39px', height:'44px',
                backgroundPosition:'center',backgroundSize:'cover',backgroundImage:'url(https://static.thenounproject.com/png/1212866-200.png)'}}></div>
              </div>
            </div>


            <div className="rightHandContainer" style={{display: 'flex'}}>
                {cardsInHand.map((card, index) =>(
                      <div >
                        <CardComponent  card={{name:card.name, points:card.points, cardInfo:card.cardInfo}}></CardComponent>
                      </div>    
                ))}
            </div>
            </div>
    </div>
  )
}

export default HandComponent