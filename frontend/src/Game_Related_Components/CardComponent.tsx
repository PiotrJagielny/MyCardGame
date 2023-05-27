import React from 'react';

interface CardComponentProps {
  color: string;
  image: string;
  name: string;
  points: number;
}

const CardComponent: React.FC<CardComponentProps> = ({ color, image, name, points }) => {
  const rectangleStyle = {
    width: `${79}px`,
    height: `${42}px`,
    backgroundColor: color,
    margin: '5px',
  };

  return <div style={rectangleStyle}>
        <label>{name}</label><br/>
        <label>{points}</label>
  </div>;
};

export default CardComponent;