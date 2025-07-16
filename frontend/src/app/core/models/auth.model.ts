export interface LoginDto {
    email: string;
    password: string;
    farms?: number;
}

export interface AuthResponse {
    user: {
        id: number;
        name: string;
        email: string;
        farms: number;
        role: string;
        farm_name: string;
    };
    token: string;
}
