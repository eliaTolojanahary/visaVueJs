<template>
  <form class="search-bar" @submit.prevent="onSubmit">
    <div class="search-tabs">
      <button
        type="button"
        class="tab"
        :class="{ 'tab--active': mode === 'demande' }"
        @click="mode = 'demande'"
      >N° Demande</button>
      <button
        type="button"
        class="tab"
        :class="{ 'tab--active': mode === 'passeport' }"
        @click="mode = 'passeport'"
      >N° Passeport</button>
    </div>

    <div class="search-input-wrap">
      <span class="search-icon">
        <svg viewBox="0 0 20 20" fill="none">
          <circle cx="8.5" cy="8.5" r="5.5" stroke="currentColor" stroke-width="1.6"/>
          <path d="M13.5 13.5L17 17" stroke="currentColor" stroke-width="1.6" stroke-linecap="round"/>
        </svg>
      </span>
      <input
        ref="inputRef"
        v-model="query"
        class="search-input"
        :placeholder="mode === 'demande' ? 'Ex: 20240101-120000-TRV' : 'Ex: AB123456'"
        autocomplete="off"
        autocorrect="off"
        spellcheck="false"
        :aria-label="mode === 'demande' ? 'Numéro de demande' : 'Numéro de passeport'"
      />
      <button v-if="query" type="button" class="clear-btn" @click="clear" aria-label="Effacer">
        <svg viewBox="0 0 16 16" fill="none">
          <path d="M4 4l8 8M12 4L4 12" stroke="currentColor" stroke-width="1.5" stroke-linecap="round"/>
        </svg>
      </button>
    </div>

    <button type="submit" class="search-btn" :disabled="!query.trim() || loading">
      <span v-if="loading" class="spinner" />
      <span v-else>Rechercher</span>
    </button>
  </form>
</template>

<script setup>
import { ref, watch } from 'vue'

const props = defineProps({
  loading: { type: Boolean, default: false },
  initialMode: { type: String, default: 'demande' },
  initialQuery: { type: String, default: '' }
})

const emit = defineEmits(['search'])

const mode = ref(props.initialMode)
const query = ref(props.initialQuery)
const inputRef = ref(null)

watch(() => props.initialQuery, v => { if (v) query.value = v })
watch(() => props.initialMode, v => { mode.value = v })

function onSubmit() {
  const q = query.value.trim()
  if (!q) return
  emit('search', { mode: mode.value, query: q })
}

function clear() {
  query.value = ''
  inputRef.value?.focus()
}

defineExpose({ mode, query })
</script>

<style scoped>
.search-bar {
  display: flex;
  flex-direction: column;
  gap: 10px;
  width: 100%;
}

.search-tabs {
  display: flex;
  background: #f1f5f9;
  border-radius: 10px;
  padding: 3px;
  gap: 3px;
}

.tab {
  flex: 1;
  padding: 8px 12px;
  border: none;
  background: transparent;
  border-radius: 8px;
  font-size: 0.8rem;
  font-weight: 600;
  color: #64748b;
  cursor: pointer;
  transition: all 0.15s;
  font-family: inherit;
}

.tab--active {
  background: #fff;
  color: #1d4ed8;
  box-shadow: 0 1px 4px rgba(0,0,0,0.08);
}

.search-input-wrap {
  position: relative;
  display: flex;
  align-items: center;
}

.search-icon {
  position: absolute;
  left: 14px;
  width: 18px;
  height: 18px;
  color: #94a3b8;
  pointer-events: none;
  display: flex;
  align-items: center;
}

.search-icon svg { width: 100%; height: 100%; }

.search-input {
  width: 100%;
  padding: 14px 44px 14px 44px;
  border: 1.5px solid #e2e8f0;
  border-radius: 12px;
  font-size: 0.95rem;
  font-family: 'IBM Plex Mono', 'Courier New', monospace;
  color: #0f172a;
  background: #fff;
  outline: none;
  transition: border-color 0.15s, box-shadow 0.15s;
  box-sizing: border-box;
}

.search-input:focus {
  border-color: #1d4ed8;
  box-shadow: 0 0 0 3px rgba(29, 78, 216, 0.1);
}

.search-input::placeholder {
  color: #c4cdd8;
  font-family: inherit;
}

.clear-btn {
  position: absolute;
  right: 14px;
  background: none;
  border: none;
  cursor: pointer;
  color: #94a3b8;
  display: flex;
  align-items: center;
  padding: 4px;
  border-radius: 50%;
  transition: color 0.15s, background 0.15s;
}

.clear-btn:hover {
  color: #475569;
  background: #f1f5f9;
}

.clear-btn svg { width: 14px; height: 14px; }

.search-btn {
  width: 100%;
  padding: 14px;
  background: #1d4ed8;
  color: #fff;
  border: none;
  border-radius: 12px;
  font-size: 0.95rem;
  font-weight: 700;
  font-family: inherit;
  cursor: pointer;
  transition: background 0.15s, transform 0.1s;
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 8px;
  letter-spacing: 0.01em;
  min-height: 50px;
}

.search-btn:hover:not(:disabled) { background: #1e40af; }
.search-btn:active:not(:disabled) { transform: scale(0.98); }
.search-btn:disabled { background: #93c5fd; cursor: not-allowed; }

.spinner {
  width: 18px;
  height: 18px;
  border: 2.5px solid rgba(255,255,255,0.3);
  border-top-color: #fff;
  border-radius: 50%;
  animation: spin 0.7s linear infinite;
}

@keyframes spin { to { transform: rotate(360deg); } }
</style>
