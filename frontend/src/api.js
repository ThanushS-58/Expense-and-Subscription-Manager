const API_URL = import.meta.env.VITE_API_URL || 'http://localhost:8080/api';

export async function request(path, options = {}) {
  const token = localStorage.getItem('ledgerly-token');
  const response = await fetch(`${API_URL}${path}`, {
    ...options,
    headers: {
      'Content-Type': 'application/json',
      ...(token ? { Authorization: `Bearer ${token}` } : {}),
      ...options.headers,
    },
  });
  if (response.status === 204) return null;
  const body = await response.json().catch(() => ({}));
  if (!response.ok) {
    if (response.status === 401) localStorage.removeItem('ledgerly-token');
    throw new Error(body.message || body.error || 'Something went wrong. Please try again.');
  }
  return body;
}

export const send = (path, method, data) => request(path, {
  method,
  ...(data ? { body: JSON.stringify(data) } : {}),
});