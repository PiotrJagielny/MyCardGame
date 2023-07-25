import React from 'react';
import './CardComponent.css';
import {Card} from './Interfaces/Card';

interface CardComponentProps {
  card: Card;
}

const CardComponent: React.FC<CardComponentProps> = ({  card}) => {
  const blockContextMenu = (event: React.MouseEvent<HTMLDivElement>) => {
    event.preventDefault();
  }

  const showInfo = () => {
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
     ">${card.points}
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
  const hideInfo= () => {
    const wholeCardDisplay: any = document.getElementById('wholeCardDisplay');
    if(wholeCardDisplay !== null)
      wholeCardDisplay.remove();
  }


  return <div  onContextMenu={blockContextMenu} onMouseEnter={showInfo}  onMouseLeave={hideInfo} onMouseDown={hideInfo} className="card">
        <div className="name">{card.name}</div>
        <div>{card.points}</div>
  </div>
};

export default CardComponent;