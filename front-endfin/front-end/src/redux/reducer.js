import initialState from "./initialState";
import * as actionTypes from './actionTypes';
import { combineReducers } from 'redux';

const messageReducer = (state = initialState, action) => {
    switch (action.type) {
        case actionTypes.messageType.ADD_MESSAGES:
            return {
                ...state,
                messages: [
                    ...state.messages.filter(msg => msg.timestamp !== action.payload.timestamp),
                    action.payload,
                ]
            };
        default:
            return state;
    }
};

export const rootReducer = combineReducers({
    messageState: messageReducer,
});

export default rootReducer;
