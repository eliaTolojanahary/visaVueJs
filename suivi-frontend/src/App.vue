<template>
  <div class="app">
    <header class="app-header">
      <div class="header-inner">
        <div class="header-logo">
          <svg viewBox="0 0 32 32" fill="none" class="logo-icon">
            <rect width="32" height="32" rx="8" fill="#1d4ed8"/>
            <path d="M8 22l4-8 4 6 3-4 5 6" stroke="#fff" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"/>
            <circle cx="23" cy="11" r="2.5" fill="#93c5fd"/>
          </svg>
          <div>
            <span class="header-title">Suivi Visa</span>
            <span class="header-sub">Madagascar</span>
          </div>
        </div>
        <span v-if="apiUrl" class="api-badge" :title="apiUrl">
          <span class="api-dot" />{{ apiHost }}
        </span>
      </div>
    </header>

    <main class="app-main">
      <section class="search-section">
        <div v-if="!hasSearched" class="search-intro">
          <h1 class="intro-title">Où en est votre demande&nbsp;?</h1>
          <p class="intro-sub">Saisissez votre numéro de demande ou de passeport pour suivre votre dossier.</p>
        </div>
        <SearchBar :loading="loading" :initial-mode="initialMode" :initial-query="initialQuery" @search="onSearch" />
      </section>

      <div v-if="autoSearched" class="qr-banner">
        <svg viewBox="0 0 20 20" fill="none" class="qr-icon">
          <rect x="2" y="2" width="7" height="7" rx="1" stroke="currentColor" stroke-width="1.5"/>
          <rect x="11" y="2" width="7" height="7" rx="1" stroke="currentColor" stroke-width="1.5"/>
          <rect x="2" y="11" width="7" height="7" rx="1" stroke="currentColor" stroke-width="1.5"/>
          <rect x="11" y="11" width="4" height="4" rx="0.5" stroke="currentColor" stroke-width="1.5"/>
          <path d="M16 15h2M16 11v4" stroke="currentColor" stroke-width="1.5" stroke-linecap="round"/>
        </svg>
        Résultat du scan QR
      </div>

      <div v-if="loading" class="skeleton-wrap">
        <div class="skeleton-card">
          <div class="sk sk--line sk--w60"></div>
          <div class="sk sk--line sk--w40" style="margin-top:8px"></div>
          <div class="sk sk--block" style="margin-top:16px;height:80px"></div>
        </div>
        <div class="skeleton-card">
          <div class="sk sk--line sk--w60"></div>
          <div class="sk sk--line sk--w40" style="margin-top:8px"></div>
        </div>
      </div>

      <Transition name="fade">
        <div v-if="error && !loading" class="error-box">
          <div class="error-icon">
            <svg viewBox="0 0 20 20" fill="none">
              <circle cx="10" cy="10" r="8" stroke="currentColor" stroke-width="1.5"/>
              <path d="M10 6v5M10 13.5v.5" stroke="currentColor" stroke-width="1.8" stroke-linecap="round"/>
            </svg>
          </div>
          <div>
            <strong>{{ error.title }}</strong>
            <p>{{ error.message }}</p>
          </div>
        </div>
      </Transition>

      <Transition name="fade">
        <div v-if="results && !loading" class="results-section">
          <div class="results-header">
            <span class="results-count">{{ totalCount }} demande{{ totalCount > 1 ? 's' : '' }} trouvée{{ totalCount > 1 ? 's' : '' }}</span>
          </div>
          <div v-if="mainDemande" class="results-group">
            <DemandeCard :demande="mainDemande" :primary="true" />
          </div>
          <template v-if="otherDemandes.length">
            <div class="other-label">
              <span>Autres demandes du même passeport</span>
              <span class="other-count">{{ otherDemandes.length }}</span>
            </div>
            <div class="results-group">
              <DemandeCard v-for="(d, i) in otherDemandes" :key="d.id || i" :demande="d" />
            </div>
          </template>
        </div>
      </Transition>
    </main>

    <footer class="app-footer">
      Système de suivi des demandes de visa — Ministère de l'Intérieur
    </footer>
  </div>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue'
import SearchBar from './components/SearchBar.vue'
import DemandeCard from './components/DemandeCard.vue'
import { searchByNumDemande, searchByNumPasseport } from '../api.js'

const loading = ref(false)
const results = ref(null)
const error = ref(null)
const hasSearched = ref(false)
const autoSearched = ref(false)
const initialMode = ref('demande')
const initialQuery = ref('')

const apiUrl = import.meta.env.VITE_API_URL || ''
const apiHost = computed(() => {
  try { return new URL(apiUrl).hostname } catch { return apiUrl }
})

const mainDemande = computed(() => {
  if (!results.value) return null
  return results.value.demandeCourante
    || results.value.principale
    || results.value.demandePrincipale
    || (Array.isArray(results.value.demandes) ? results.value.demandes[0] : null)
    || (Array.isArray(results.value) ? results.value[0] : null)
    || results.value
})

const otherDemandes = computed(() => {
  if (!results.value) return []
  const others = results.value.autresDemandes
    || results.value.autres
    || (Array.isArray(results.value.demandes) ? results.value.demandes.slice(1) : [])
    || (Array.isArray(results.value) ? results.value.slice(1) : [])
  return [...others].sort((a, b) =>
    new Date(a.createdAt || a.created_at || 0) - new Date(b.createdAt || b.created_at || 0)
  )
})

const totalCount = computed(() => (mainDemande.value ? 1 : 0) + otherDemandes.value.length)

async function onSearch({ mode, query }) {
  loading.value = true
  error.value = null
  results.value = null
  hasSearched.value = true
  try {
    const data = mode === 'demande'
      ? await searchByNumDemande(query)
      : await searchByNumPasseport(query)
    if (!data || (Array.isArray(data) && data.length === 0)) {
      error.value = { title: 'Aucun résultat', message: `Aucune demande trouvée pour "${query}". Vérifiez le numéro saisi.` }
    } else {
      results.value = data
    }
  } catch (e) {
    const isNotFound = e.message?.includes('404') || e.message?.toLowerCase().includes('introuvable')
    error.value = isNotFound
      ? { title: 'Numéro introuvable', message: `Aucune demande ne correspond à "${query}".` }
      : { title: 'Erreur réseau', message: 'Impossible de contacter le serveur. Vérifiez votre connexion.' }
  } finally {
    loading.value = false
  }
}

onMounted(() => {
  const params = new URLSearchParams(window.location.search)
  const numDemande = params.get('numDemande')
  const numPasseport = params.get('numPasseport')
  if (numDemande) {
    initialMode.value = 'demande'
    initialQuery.value = numDemande
    autoSearched.value = true
    onSearch({ mode: 'demande', query: numDemande })
  } else if (numPasseport) {
    initialMode.value = 'passeport'
    initialQuery.value = numPasseport
    autoSearched.value = true
    onSearch({ mode: 'passeport', query: numPasseport })
  }
})
</script>

<style>
@import url('https://fonts.googleapis.com/css2?family=IBM+Plex+Mono:wght@400;600&family=Plus+Jakarta+Sans:wght@400;500;600;700;800&display=swap');
*, *::before, *::after { box-sizing: border-box; margin: 0; padding: 0; }
html { font-family: 'Plus Jakarta Sans', -apple-system, sans-serif; -webkit-text-size-adjust: 100%; }
body { background: #f8fafc; color: #1e293b; min-height: 100dvh; }
#app { min-height: 100dvh; display: flex; flex-direction: column; }
</style>

<style scoped>
.app { min-height: 100dvh; display: flex; flex-direction: column; }

.app-header { background: #fff; border-bottom: 1px solid #e2e8f0; position: sticky; top: 0; z-index: 100; }
.header-inner { max-width: 640px; margin: 0 auto; padding: 12px 20px; display: flex; align-items: center; justify-content: space-between; }
.header-logo { display: flex; align-items: center; gap: 10px; }
.logo-icon { width: 32px; height: 32px; flex-shrink: 0; }
.header-title { display: block; font-size: 0.95rem; font-weight: 800; color: #0f172a; line-height: 1.1; }
.header-sub { display: block; font-size: 0.68rem; color: #94a3b8; text-transform: uppercase; letter-spacing: 0.07em; font-weight: 600; }
.api-badge { display: flex; align-items: center; gap: 5px; font-size: 0.7rem; color: #64748b; background: #f1f5f9; padding: 4px 10px; border-radius: 20px; font-family: 'IBM Plex Mono', monospace; max-width: 140px; overflow: hidden; text-overflow: ellipsis; white-space: nowrap; }
.api-dot { width: 6px; height: 6px; background: #22c55e; border-radius: 50%; flex-shrink: 0; animation: pulse 2s ease-in-out infinite; }
@keyframes pulse { 0%,100%{opacity:1} 50%{opacity:0.4} }

.app-main { flex: 1; max-width: 640px; width: 100%; margin: 0 auto; padding: 28px 20px 40px; display: flex; flex-direction: column; gap: 20px; }

.search-section { display: flex; flex-direction: column; gap: 18px; }
.search-intro { text-align: center; padding: 8px 0 4px; }
.intro-title { font-size: 1.55rem; font-weight: 800; color: #0f172a; line-height: 1.2; margin-bottom: 8px; }
.intro-sub { font-size: 0.88rem; color: #64748b; line-height: 1.5; }

.qr-banner { display: flex; align-items: center; gap: 10px; background: #eff6ff; border: 1px solid #bfdbfe; border-radius: 10px; padding: 10px 14px; font-size: 0.82rem; font-weight: 600; color: #1d4ed8; }
.qr-icon { width: 18px; height: 18px; flex-shrink: 0; }

.error-box { display: flex; gap: 14px; align-items: flex-start; background: #fff; border: 1.5px solid #fecaca; border-radius: 12px; padding: 16px 18px; }
.error-icon { width: 22px; height: 22px; flex-shrink: 0; margin-top: 1px; color: #dc2626; }
.error-icon svg { width: 100%; height: 100%; }
.error-box strong { font-size: 0.9rem; font-weight: 700; display: block; margin-bottom: 3px; color: #991b1b; }
.error-box p { font-size: 0.82rem; color: #b91c1c; line-height: 1.45; }

.results-section { display: flex; flex-direction: column; gap: 12px; }
.results-header { display: flex; align-items: center; }
.results-count { font-size: 0.78rem; font-weight: 700; text-transform: uppercase; letter-spacing: 0.07em; color: #64748b; }
.results-group { display: flex; flex-direction: column; gap: 10px; }
.other-label { display: flex; align-items: center; justify-content: space-between; margin-top: 6px; padding: 0 2px; }
.other-label span:first-child { font-size: 0.78rem; font-weight: 700; text-transform: uppercase; letter-spacing: 0.07em; color: #94a3b8; }
.other-count { font-size: 0.72rem; font-weight: 700; background: #e2e8f0; color: #475569; padding: 2px 8px; border-radius: 20px; }

.skeleton-wrap { display: flex; flex-direction: column; gap: 12px; }
.skeleton-card { background: #fff; border: 1.5px solid #e2e8f0; border-radius: 14px; padding: 20px; }
.sk { background: linear-gradient(90deg, #f1f5f9 25%, #e2e8f0 50%, #f1f5f9 75%); background-size: 200% 100%; animation: shimmer 1.4s ease-in-out infinite; border-radius: 6px; }
.sk--line { height: 14px; }
.sk--block { height: 60px; }
.sk--w60 { width: 60%; }
.sk--w40 { width: 40%; }
@keyframes shimmer { 0%{background-position:200% 0} 100%{background-position:-200% 0} }

.app-footer { text-align: center; padding: 16px 20px; font-size: 0.72rem; color: #cbd5e1; }

.fade-enter-active,.fade-leave-active { transition: opacity 0.25s, transform 0.25s; }
.fade-enter-from { opacity: 0; transform: translateY(8px); }
.fade-leave-to { opacity: 0; }

@media (max-width: 480px) {
  .app-main { padding: 20px 16px 32px; }
  .intro-title { font-size: 1.3rem; }
  .header-inner { padding: 10px 16px; }
}
</style>
