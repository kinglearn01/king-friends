import myAxios from "../plugins/myAxios";
import {getCurrentUserState, setCurrentUserState} from "../states/user"

export const getCurrentUser = async () =>{
    const currentUser = getCurrentUserState();
    const res = await myAxios.get('/user/current');

    if(res.data.code === 0){
         setCurrentUserState(res.data);
         return res.data;
    }
    return null    
}

