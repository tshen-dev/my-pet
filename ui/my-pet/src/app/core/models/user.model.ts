export interface User {
  id: number;
  email: string;
  username: string;
  firstName: string;
  lastName: string;
  password: string;
}

export interface PageResponse<Type> {
  content: Type[]
  empty: boolean
}

export interface ApiResponse<Type> {
  message: string
  data: Type
}