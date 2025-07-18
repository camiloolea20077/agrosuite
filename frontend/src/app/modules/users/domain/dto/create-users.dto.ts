export interface CreateUsersDto {
    name: string;
    email: string;
    username: string;
    password: string;
    role_id: number;
    farmId: number;
    active: number;
}