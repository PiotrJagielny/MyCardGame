import React, {useState} from 'react'

interface Cards{
    names: string[];
}

const DeckBuilderPage = () => {
    const [CardsData, setCardsData] = useState<Cards>();


    return (
        <div>DeckBuilderPage</div>
    )
}

export default DeckBuilderPage