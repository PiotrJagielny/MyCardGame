interface StateData {
    userName: string;
    gameID: string;
    serverURL: string;
}
export default StateData;
type Action = {type: string, payload:string};

export const reducer = (state:StateData= {userName:"", gameID: "", serverURL: ""}, action:Action) => {
    switch(action.type) {
        case "SET_USERNAME":
            return {...state, userName: action.payload};
        case "SET_GAME_ID":
            return {...state, gameID: action.payload};
        case "SET_SERVER_URL":
            return {...state, serverURL: action.payload};
        default:
            return state;
    }

};