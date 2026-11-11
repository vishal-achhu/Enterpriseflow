const BASE = import.meta.env.VITE_API_BASE_URL ?? 'http://localhost:8080/api';
export function token() { return localStorage.getItem('ef_token'); }
export async function api<T>(path: string, options: RequestInit = {}): Promise<T> {
  const headers = new Headers(options.headers);
  headers.set('Content-Type', 'application/json');
  if (token()) headers.set('Authorization', `Bearer ${token()}`);
  const response = await fetch(`${BASE}${path}`, {...options, headers});
  if (!response.ok) throw new Error((await response.text()) || `Request failed (${response.status})`);
  if (response.status === 204) return undefined as T;
  return response.json() as Promise<T>;
}
