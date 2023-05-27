import React from 'react';

interface CardComponentProps {
  color: string;
  image: string;
  name: string;
  points: number;
}

const CardComponent: React.FC<CardComponentProps> = ({ color, image, name, points }) => {
  const rectangleStyle = {
    width: `${70}px`,
    height: `${41}px`,
    backgroundColor: color,
    margin: '5px',
  };

  let space:string = "";

  return <div style={rectangleStyle}>
        <label>{name}</label><br/>
        <label>{points}</label>
  </div>;
};

export default CardComponent;