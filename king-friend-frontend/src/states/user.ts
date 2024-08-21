import {UserType} from  '../models/user'
let currentUser: UserType;
export const setCurrentUserState = (user:UserType)=>{
        currentUser= user;
}

export const getCurrentUserState = ()=>{
    return currentUser;
}
