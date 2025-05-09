export interface LoginDto {
    email: string;
    password: string;
}

export interface AuthResponse {
    user: {
        id: number;
        name: string;
        email: string;
        company: number;
        role: string;
    };
    token: string;
}
