export class UsersModels {
    id: number
    name: string;
    email: string;
    username: string;
    password: string;
    role_id: number;
    farmId: number;
    active: number;
    permisos: string[];
    constructor(id: number, name: string, email: string, password: string, role_id: number, farmId: number, active: number, username: string, permisos: string[] = []) {
        this.id = id;
        this.name = name;
        this.permisos = permisos;
        this.username = username;
        this.email = email;
        this.password = password;
        this.role_id = role_id;
        this.farmId = farmId;
        this.active = active;
    }
}