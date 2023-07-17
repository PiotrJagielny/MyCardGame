import React, {useState} from 'react';
import Modal from 'react-modal';
import {useSelector} from 'react-redux';
import StateData from './../Game_Unrelated_Components/reactRedux/reducer';
import './CardComponent.css';

interface CardComponentProps {
  name: string;
  points: number;
}

const CardComponent: React.FC<CardComponentProps> = ({  name, points }) => {
  const [showModal, setShowModal] = useState(false);
  const [cardInfo, setCardInfo] = useState<string>("");


  const serverURL = useSelector<StateData, string>((state) => state.serverURL); 

  const getInfo = (event: React.MouseEvent<HTMLDivElement>) => {

    event.preventDefault();
    if(event.button === 2) {
      fetch(`${serverURL}/Duel/getCardInfo/${name}`)
        .then((res) => res.text())
        .then((cardInfo: string) => {
          setCardInfo(cardInfo); 
          setShowModal(true);
        })
        .catch(console.error);
      }
  }
  
  const blockContextMenu = (event: React.MouseEvent<HTMLDivElement>) => {
  event.preventDefault();
  }

  return <div  onContextMenu={blockContextMenu} onMouseDown={getInfo} className="card">
        <div className="name">{name}</div>
        <div>{points}</div>

      <Modal isOpen={showModal}  style={{content: {width:'300px', height:'200px', background:'gray',},}}>
        <h2>{cardInfo}</h2>
        <button onClick={() => setShowModal(false)}>Close</button>
        
      </Modal>
  </div>
};

export default CardComponent;