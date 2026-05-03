<template>
  <div class="demande-card" :class="{ 'demande-card--primary': primary }">
    <div class="card-header">
      <div class="card-header-left">
        <span class="badge-type">{{ demande.typeDemande || demande.type_demande || '—' }}</span>
        <span v-if="primary" class="badge-primary">Demande principale</span>
      </div>
      <span class="badge-statut" :class="statutClass">
        {{ statutLabel }}
      </span>
    </div>

    <div class="card-ref">
      <span class="ref-label">N° demande</span>
      <span class="ref-value">{{ demande.refDemande || demande.ref_demande || demande.numDemande || '—' }}</span>
    </div>

    <div class="card-meta">
      <div v-if="demande.nomComplet || demande.nom" class="meta-row">
        <span class="meta-icon">
          <svg viewBox="0 0 16 16" fill="none"><circle cx="8" cy="5.5" r="2.5" stroke="currentColor" stroke-width="1.3"/><path d="M2.5 13.5c0-3.038 2.462-5.5 5.5-5.5s5.5 2.462 5.5 5.5" stroke="currentColor" stroke-width="1.3" stroke-linecap="round"/></svg>
        </span>
        <span>{{ demande.nomComplet || ((demande.nom || '') + ' ' + (demande.prenom || '')).trim() }}</span>
      </div>
      <div v-if="demande.numPasseport || demande.numero_passeport" class="meta-row">
        <span class="meta-icon">
          <svg viewBox="0 0 16 16" fill="none"><rect x="2.5" y="1.5" width="11" height="13" rx="1.5" stroke="currentColor" stroke-width="1.3"/><path d="M5 6h6M5 9h4" stroke="currentColor" stroke-width="1.3" stroke-linecap="round"/></svg>
        </span>
        <span>{{ demande.numPasseport || demande.numero_passeport }}</span>
      </div>
      <div v-if="demande.createdAt || demande.created_at" class="meta-row">
        <span class="meta-icon">
          <svg viewBox="0 0 16 16" fill="none"><rect x="1.5" y="2.5" width="13" height="12" rx="1.5" stroke="currentColor" stroke-width="1.3"/><path d="M5 1v3M11 1v3M1.5 7h13" stroke="currentColor" stroke-width="1.3" stroke-linecap="round"/></svg>
        </span>
        <span>{{ formatDate(demande.createdAt || demande.created_at) }}</span>
      </div>
    </div>

    <div v-if="historique && historique.length" class="card-timeline">
      <div class="timeline-toggle" @click="open = !open">
        <span>Historique des statuts</span>
        <svg :class="{ rotated: open }" viewBox="0 0 16 16" fill="none">
          <path d="M4 6l4 4 4-4" stroke="currentColor" stroke-width="1.5" stroke-linecap="round"/>
        </svg>
      </div>
      <Transition name="slide">
        <div v-if="open" class="timeline-body">
          <StatusTimeline :items="historique" />
        </div>
      </Transition>
    </div>
  </div>
</template>

<script setup>
import { ref, computed } from 'vue'
import StatusTimeline from './StatusTimeline.vue'

const props = defineProps({
  demande: { type: Object, required: true },
  primary: { type: Boolean, default: false }
})

const open = ref(props.primary)

const historique = computed(() => {
  return props.demande.historique
    || props.demande.statutHistorique
    || props.demande.statut_historique
    || []
})

const statutLabel = computed(() => {
  return props.demande.statutActuel
    || props.demande.statut_actuel
    || props.demande.statut
    || '—'
})

const statutClass = computed(() => {
  const s = statutLabel.value.toLowerCase()
  if (s.includes('scan') || s.includes('terminé')) return 'statut--scan'
  if (s.includes('approuv') || s.includes('valid')) return 'statut--ok'
  if (s.includes('refus')) return 'statut--ko'
  if (s.includes('cours')) return 'statut--progress'
  return 'statut--default'
})

function formatDate(d) {
  if (!d) return ''
  return new Date(d).toLocaleDateString('fr-FR', {
    day: '2-digit', month: 'short', year: 'numeric'
  })
}
</script>

<style scoped>
.demande-card {
  background: #fff;
  border: 1.5px solid #e2e8f0;
  border-radius: 14px;
  padding: 20px;
  transition: box-shadow 0.2s;
}

.demande-card--primary {
  border-color: #1d4ed8;
  box-shadow: 0 0 0 3px rgba(29, 78, 216, 0.08), 0 4px 24px rgba(29,78,216,0.08);
}

.card-header {
  display: flex;
  align-items: flex-start;
  justify-content: space-between;
  gap: 8px;
  margin-bottom: 14px;
}

.card-header-left {
  display: flex;
  flex-wrap: wrap;
  gap: 6px;
  align-items: center;
}

.badge-type {
  font-size: 0.72rem;
  font-weight: 700;
  text-transform: uppercase;
  letter-spacing: 0.06em;
  color: #475569;
  background: #f1f5f9;
  padding: 3px 10px;
  border-radius: 20px;
}

.badge-primary {
  font-size: 0.68rem;
  font-weight: 700;
  text-transform: uppercase;
  letter-spacing: 0.05em;
  color: #1d4ed8;
  background: #eff6ff;
  padding: 3px 10px;
  border-radius: 20px;
}

.badge-statut {
  font-size: 0.72rem;
  font-weight: 700;
  padding: 4px 12px;
  border-radius: 20px;
  white-space: nowrap;
  flex-shrink: 0;
}

.statut--scan     { background: #eff6ff; color: #1d4ed8; }
.statut--ok       { background: #f0fdf4; color: #15803d; }
.statut--ko       { background: #fef2f2; color: #b91c1c; }
.statut--progress { background: #fffbeb; color: #b45309; }
.statut--default  { background: #f8fafc; color: #64748b; }

.card-ref {
  display: flex;
  flex-direction: column;
  gap: 2px;
  margin-bottom: 14px;
  padding-bottom: 14px;
  border-bottom: 1px solid #f1f5f9;
}

.ref-label {
  font-size: 0.68rem;
  text-transform: uppercase;
  letter-spacing: 0.08em;
  color: #94a3b8;
  font-weight: 600;
}

.ref-value {
  font-family: 'IBM Plex Mono', 'Courier New', monospace;
  font-size: 1.05rem;
  font-weight: 700;
  color: #0f172a;
  letter-spacing: 0.03em;
}

.card-meta {
  display: flex;
  flex-direction: column;
  gap: 7px;
  margin-bottom: 14px;
}

.meta-row {
  display: flex;
  align-items: center;
  gap: 8px;
  font-size: 0.83rem;
  color: #475569;
}

.meta-icon {
  width: 16px;
  height: 16px;
  display: flex;
  align-items: center;
  color: #94a3b8;
  flex-shrink: 0;
}

.meta-icon svg { width: 100%; height: 100%; }

.card-timeline {
  border-top: 1px solid #f1f5f9;
  padding-top: 12px;
}

.timeline-toggle {
  display: flex;
  align-items: center;
  justify-content: space-between;
  cursor: pointer;
  font-size: 0.78rem;
  font-weight: 600;
  color: #64748b;
  user-select: none;
  padding: 2px 0;
}

.timeline-toggle svg {
  width: 16px;
  height: 16px;
  color: #94a3b8;
  transition: transform 0.2s;
  flex-shrink: 0;
}

.timeline-toggle svg.rotated { transform: rotate(180deg); }

.timeline-body {
  margin-top: 12px;
  overflow: hidden;
}

.slide-enter-active,
.slide-leave-active {
  transition: all 0.25s ease;
}
.slide-enter-from,
.slide-leave-to {
  opacity: 0;
  transform: translateY(-6px);
}
</style>
