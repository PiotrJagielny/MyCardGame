import React, {useState} from 'react';
import Modal from 'react-modal';

interface CardComponentProps {
  color: string;
  image: string;
  name: string;
  points: number;
}

const CardComponent: React.FC<CardComponentProps> = ({ color, image, name, points }) => {
  const [showModal, setShowModal] = useState(false);
  const [cardInfo, setCardInfo] = useState<string>("");
  const rectangleStyle = {
    width: `${79}px`,
    height: `${42}px`,
    backgroundColor: color,
    margin: '5px',
  };

  const getInfo = (event: React.MouseEvent<HTMLDivElement>) => {

    event.preventDefault();
    if(event.button === 2) {
      fetch(`http://localhost:8000/Duel/getCardInfo/${name}`)
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
  return <div style={rectangleStyle} onContextMenu={blockContextMenu} onMouseDown={getInfo}>
        <label>{name}</label><br/>
        <label>{points}</label>
      <Modal isOpen={showModal}  style={{content: {width:'300px', height:'200px', background:'gray',},}}>
        <h2>{cardInfo}</h2>
        <button onClick={() => setShowModal(false)}>Close</button>
        
      </Modal>
  </div>
};

export default CardComponent;