import React, {useState, useEffect} from 'react'

interface Card{
    name: string;
}

const DeckBuilderPage = () => {
    const [CardsData, setCardsData] = useState<Card[]>([]);

    useEffect(() => {
        fetch('http://localhost:8000/DeckBuilder/GetAllCards')
          .then(res => res.json())
          .then((CardsData: Card[]) => {
            setCardsData(CardsData);
          })
          .catch(console.error);
        
          console.log(CardsData);
      }, []);
      
    

    return (
        <div>
            DeckBuilderPage <br/>

            <p>All Cards:</p>
            {CardsData.map(item => (
                <div key={item.name}>
                    <label>{item.name}</label>
                </div>
            ))}
        </div>
    )
}

export default DeckBuilderPage