import { Injectable } from '@angular/core';
import * as localForage from 'localforage';
import { AuthResponse } from '../models/auth.model';

@Injectable({ providedIn: 'root' })
export class IndexDBService {
  private readonly AUTH_KEY = 'authResponse';

  constructor() {
    localForage.config({
      name: 'app-db',
      version: 1.0,
      storeName: 'authData',
      description: 'Datos de sesión del usuario autenticado',
    });
  }

  // Guardar los datos de autenticación en IndexedDB
  async saveAuthData(data: AuthResponse): Promise<void> {
    try {
      await localForage.setItem<AuthResponse>(this.AUTH_KEY, data);
    } catch (error) {
      console.error('Error guardando datos en IndexedDB:', error);
    }
  }

  // Cargar los datos desde IndexedDB
  async loadDataAuthDB(): Promise<AuthResponse | null> {
    try {
      const data = await localForage.getItem<AuthResponse>(this.AUTH_KEY);
      return data ?? null;
    } catch (error) {
      console.error('Error cargando datos de IndexedDB:', error);
      return null;
    }
  }

  // Eliminar datos (logout)
  async deleteDataAuthDB(): Promise<void> {
    try {
      await localForage.removeItem(this.AUTH_KEY);
    } catch (error) {
      console.error('Error eliminando datos de IndexedDB:', error);
    }
  }

  // Verificar si hay sesión guardada
  async isAuthenticated(): Promise<boolean> {
    const data = await this.loadDataAuthDB();
    return !!data?.token;
  }

  // Obtener solo el token
  async getToken(): Promise<string | null> {
    const data = await this.loadDataAuthDB();
    return data?.token ?? null;
  }

  // Obtener solo el ID de la finca
  async getFarmId(): Promise<number | null> {
    const data = await this.loadDataAuthDB();
    return data?.user?.farm ?? null;
  }
  // Obtener solo el ID del usuario
  async getUserId(): Promise<number | null> {
    const data = await this.loadDataAuthDB();
    return data?.user?.id ?? null;
  }
}
