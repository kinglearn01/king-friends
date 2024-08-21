type TeamType = {
    id:number;
    name:string;
    description:string;
    expireTime?:Date;
    maxNum:number;
    password?:string;
    status:number;
    createTime:Date;
    updateTime:Date;
    createUser?:UserType;
};
    