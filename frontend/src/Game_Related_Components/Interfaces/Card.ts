export interface Card{
    id: number;
    name: string;
    points: number;
    basePoints:number;
    cardInfo: string;
    timer: number;
    color: string;
}

export {}; 

export function createEmptyCard() : Card {
    return {name:"", points:0, basePoints: 0, cardInfo:"", timer:-1, id: -1, color:""};
}


export function createCardWithName(cardName: string) : Card {
    return {name:cardName, points:0, basePoints: 0, cardInfo:"", timer:-1, id: -1, color:""};
}

export function createCardWithIdAndName(cardName: string, cardId: number) {
    return {name: cardName, points: 0, basePoints: 0, cardInfo:"", timer:-1, id:cardId, color:""}
}