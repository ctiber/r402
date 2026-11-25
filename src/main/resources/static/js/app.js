import { createApp } from './vendor/vue.esm-browser.prod.js';
import { lire } from './api.js';
import EnTete from './composants/entete.js';
import ListeComptes from './composants/liste-comptes.js';
import FormulaireVirement from './composants/formulaire-virement.js';

createApp({
  components: { EnTete, ListeComptes, FormulaireVirement },
  data() {
    return { utilisateur: null, comptes: [] };
  },
  async mounted() {
    this.utilisateur = await lire('/api/moi');
    await this.chargerComptes();
  },
  methods: {
    async chargerComptes() {
      this.comptes = await lire('/api/comptes');
    }
  },
  template: `
    <EnTete :utilisateur="utilisateur" />
    <main>
      <ListeComptes :comptes="comptes" />
      <FormulaireVirement :comptes="comptes" @virement-effectue="chargerComptes" />
    </main>`
}).mount('#app');
