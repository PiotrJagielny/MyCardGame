interface StateData {
    userName: string;
    gameID: string;
    serverURL: string;
    webSocket: WebSocket | null;
}
export default StateData;
type Action = {type: string, payload:string, createdSocket: WebSocket};

export const reducer = (state:StateData= {userName:"", gameID: "", serverURL: "", webSocket: null}, action:Action) => {
    switch(action.type) {
        case "SET_USERNAME":
            return {...state, userName: action.payload};
        case "SET_GAME_ID":
            return {...state, gameID: action.payload};
        case "SET_SERVER_URL":
            return {...state, serverURL: action.payload};
        case "CONNECT_TO_SOCKET":
            return {...state, webSocket: action.createdSocket};
        default:
            return state;
    }

};