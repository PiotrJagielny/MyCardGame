export interface Card{
    name: string;
    points: number;
    cardInfo: string;
    timer: number;
}

export {}; 

export function createEmptyCard() : Card {
    return {name:"", points:0, cardInfo:"", timer:-1};
}


export function createCardWithName(cardName: string) : Card {
    return {name:cardName, points:0, cardInfo:"", timer:-1};
}