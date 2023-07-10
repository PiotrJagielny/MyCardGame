



interface StateData {
    userNames: Map<number,string>;
    gameIDs: Map<number,string>;
    serverURL: string;
    maxClientID: number;
};
const initialState = (): StateData => {
    return {
        userNames: new  Map<number,string>(),
        gameIDs: new  Map<number,string>(),
        serverURL: "",
        maxClientID: 0
    };
};
interface payload{
    clientId: number;
    content: string;
};
export default StateData;
type Actionn = {type: string, payload:payload};

export const reducer = (state:StateData= initialState(), action:Actionn) => {
    switch(action.type) {
        case "SET_USERNAME":
            const updatedUserNames = new Map<number,string>(Array.from(state.userNames));
            updatedUserNames.set(action.payload.clientId, action.payload.content);
            console.log(action.payload.clientId + ":" +action.payload.content);
            return {...state, userNames: updatedUserNames};
        case "SET_GAME_ID":
            const updatedGameIDs = new Map<number,string>(state.gameIDs);
            updatedGameIDs.set(action.payload.clientId, action.payload.content);
            return {...state, gameIds: updatedGameIDs};
        case "SET_SERVER_URL":
            return {...state, serverURL: action.payload.content};
        case "INCREMENT_MAX_CLIENT_ID":
            return {...state, maxClientID: state.maxClientID + 1};
        default:
            return state;
    }

};