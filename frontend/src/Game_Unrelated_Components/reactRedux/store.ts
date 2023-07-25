import {createStore} from 'redux';
import {reducer} from './reducer';
import storage from 'redux-persist/lib/storage';
import {persistReducer} from 'redux-persist';

const persistConfig = {
    key: "root",
    version: 1,
    storage
}
const persistedReducer = persistReducer(persistConfig, reducer); 

export const store = createStore(persistedReducer);