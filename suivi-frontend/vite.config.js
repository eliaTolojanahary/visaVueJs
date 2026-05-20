import { defineConfig } from 'vite'
import vue from '@vitejs/plugin-vue'

// https://vite.dev/config/
export default defineConfig({
  plugins: [vue()],
  server: {
    // 1. Pour les requêtes Cross-Origin (CORS)
    cors: true, 
    
    // 2. Pour rendre l'app accessible sur le réseau local/externe
    host: true, 
  }
})
