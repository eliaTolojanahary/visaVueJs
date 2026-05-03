<template>
  <div class="timeline">
    <div
      v-for="(item, i) in items"
      :key="i"
      class="timeline-item"
      :class="{ 'timeline-item--active': i === items.length - 1 }"
    >
      <div class="timeline-connector">
        <div class="timeline-dot" :class="dotClass(item.statut)">
          <svg v-if="i === items.length - 1" viewBox="0 0 12 12" fill="none">
            <circle cx="6" cy="6" r="5" stroke="currentColor" stroke-width="1.5"/>
            <circle cx="6" cy="6" r="2.5" fill="currentColor"/>
          </svg>
          <svg v-else viewBox="0 0 12 12" fill="none">
            <circle cx="6" cy="6" r="4.5" stroke="currentColor" stroke-width="1.5" fill="currentColor" fill-opacity="0.15"/>
          </svg>
        </div>
        <div v-if="i < items.length - 1" class="timeline-line" />
      </div>
      <div class="timeline-content">
        <span class="timeline-statut">{{ item.statut }}</span>
        <span class="timeline-date">{{ formatDate(item.date) }}</span>
      </div>
    </div>
  </div>
</template>

<script setup>
const props = defineProps({
  items: { type: Array, default: () => [] }
})

function formatDate(d) {
  if (!d) return ''
  const date = new Date(d)
  return date.toLocaleDateString('fr-FR', {
    day: '2-digit', month: 'short', year: 'numeric',
    hour: '2-digit', minute: '2-digit'
  })
}

function dotClass(statut) {
  const s = (statut || '').toLowerCase()
  if (s.includes('scan') || s.includes('terminé')) return 'dot--scan'
  if (s.includes('approuv') || s.includes('valid')) return 'dot--ok'
  if (s.includes('refus')) return 'dot--ko'
  if (s.includes('créé') || s.includes('cree') || s.includes('créé')) return 'dot--created'
  return 'dot--default'
}
</script>

<style scoped>
.timeline {
  display: flex;
  flex-direction: column;
  gap: 0;
}

.timeline-item {
  display: flex;
  gap: 12px;
  align-items: flex-start;
}

.timeline-connector {
  display: flex;
  flex-direction: column;
  align-items: center;
  flex-shrink: 0;
  width: 20px;
}

.timeline-dot {
  width: 20px;
  height: 20px;
  display: flex;
  align-items: center;
  justify-content: center;
  flex-shrink: 0;
}

.timeline-dot svg {
  width: 12px;
  height: 12px;
}

.dot--created  { color: #6b7280; }
.dot--scan     { color: #2563eb; }
.dot--ok       { color: #16a34a; }
.dot--ko       { color: #dc2626; }
.dot--default  { color: #9ca3af; }

.timeline-line {
  width: 2px;
  flex: 1;
  min-height: 20px;
  background: #e5e7eb;
  margin: 2px 0;
}

.timeline-content {
  display: flex;
  flex-direction: column;
  padding-bottom: 16px;
}

.timeline-statut {
  font-size: 0.82rem;
  font-weight: 600;
  color: #1e293b;
  font-family: 'IBM Plex Mono', monospace;
  letter-spacing: 0.01em;
}

.timeline-date {
  font-size: 0.72rem;
  color: #94a3b8;
  margin-top: 1px;
}

.timeline-item--active .timeline-statut {
  color: #1d4ed8;
}
</style>
