import React from 'react';
import './CardComponent.css';
import {Card} from './Interfaces/Card';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import {faHourglassHalf} from '@fortawesome/free-solid-svg-icons';

interface CardComponentProps {
  card: Card;
  isOnRow?: boolean;
}

const CardComponent: React.FC<CardComponentProps> = ({  card, isOnRow = false}) => {
  const blockContextMenu = (event: React.MouseEvent<HTMLDivElement>) => {
    event.preventDefault();
  }

  const showInfo = () => {
    if(card.name !== "") {
      console.log(card.id);
      const wholeCardDisplay = document.createElement('div');
      wholeCardDisplay.classList.add('wholeCardDisplay');
      wholeCardDisplay.setAttribute('style', `
        position: absolute;
        top: 20%;
        padding:20px;
        border-radius: 10px;
        box-shadow: 0 10px 5px 0 #00000022; 
        flex-direction:column;
        background-image: url(${'https://www.pngkit.com/png/full/196-1963608_jpg-freeuse-library-uno-cards-template-png-for.png'});
        background-size: cover;
        background-position: top;
        background-color: black;
        margin-left:60%;
        height: 350px;
        width: 260px;
      `);
      wholeCardDisplay.setAttribute('id', 'wholeCardDisplay');
      wholeCardDisplay.innerHTML= `<div style="
        font-size: 40px;
        color: black;
        margin-left: 10%;
        margin-top: 10%;
        ">
       ${card.name}
       </div>
       <div style="
        font-size: 30px; 
        color: black;
        margin-left: 10%;
       ">${card.basePoints}
       </div>
       <div style="
        font-size: 30px; 
        color: black;
        margin-left: 10%;
        margin-top: 20%
       ">${card.cardInfo}
       </div>`;
      document.body.appendChild(wholeCardDisplay);

    }
  }
  const hideInfo= () => {
    const wholeCardDisplay: any = document.getElementById('wholeCardDisplay');
    if(wholeCardDisplay !== null)
      wholeCardDisplay.remove();
  }

  const getCardPointsColor = (points: number, basePoints: number) => {
    if(points === basePoints) {
      return "";
    }
    else if(points > basePoints) {
      return "green";
    }
    else if(points < basePoints) {
      return "red";
    }
  }


  return <div onContextMenu={blockContextMenu} onMouseEnter={showInfo}  onMouseLeave={hideInfo} onMouseDown={hideInfo} className="card">

        {isOnRow === true?
        <div id={card.id.toString()} className="name">{card.name}</div>
        :
        <div className="name">{card.name}</div>
         }

        {card.points === 0 ?
        <div>.</div>
        :
        <div style={{color: getCardPointsColor(card.points, card.basePoints)}}>{card.points}</div>
        }
        {card.timer !== -1 && <div><FontAwesomeIcon icon={faHourglassHalf} /> {card.timer}</div>}
  </div>
};

export default CardComponent;