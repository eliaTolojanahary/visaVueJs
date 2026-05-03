const API_BASE = import.meta.env.VITE_API_URL || 'http://localhost:8080/visa'

async function fetchJson(url) {
  const res = await fetch(url, {
    headers: { 'Accept': 'application/json' },
    cache: 'no-store'
  })
  if (!res.ok) {
    const text = await res.text()
    throw new Error(text || `Erreur HTTP ${res.status}`)
  }
  return res.json()
}

export async function searchByNumDemande(num) {
  return fetchJson(`${API_BASE}/api/demandes?numDemande=${encodeURIComponent(num.trim())}`)
}

export async function searchByNumPasseport(num) {
  return fetchJson(`${API_BASE}/api/demandes?numPasseport=${encodeURIComponent(num.trim())}`)
}
